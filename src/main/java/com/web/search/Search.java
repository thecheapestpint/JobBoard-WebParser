package com.web.search;

import com.google.gson.Gson;
import com.web.database.MongoDB.MongoJobBoard;
import com.web.database.MongoDB.MongoWebsite;
import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.database.MongoDB.Pojo.Website;
import com.web.database.MySQL.MySQL;
import com.web.scraper.Crawler.Crawler;
import com.web.scraper.Crawler.JobBoard.JobBoardConfig;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import javafx.beans.binding.IntegerBinding;
import javassist.bytecode.stackmap.TypeData;

import javax.print.attribute.standard.JobHoldUntil;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Mitchell on 28/07/2017.
 */
public class Search {

    private MySQL mySQL;

    private String keyword;
    private int keywordID;

    private String location;
    private int locationID;

    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );

    private ArrayList args;


    /*

        VERY VERY VERY VERY MESSY

     */


    public Search() {
        mySQL = new MySQL("job_search");
        args = new ArrayList();
        Handler fh = null;
        try {
            fh = new FileHandler("Search.log");
            Logger.getLogger("").addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cronCrawl(){

        List<Map<String,String>> searchRes = getSearchKeywordsAndLocations();
        if (searchRes.size() > 0) {
            for (Map<String,String> res : searchRes) {
                keyword = res.get("keyword");
                location = res.get("location");
                crawlForJobs(false);
                updateSearchByID((Integer.parseInt(res.get("searchID"))));
            }
        }
    }

    private List<Map<String, String>> getSearchKeywordsAndLocations(){

        List<Map<String, String>> results = new ArrayList();

        String query = "SELECT search_query.search_id, keywords.keyword, locations.location FROM search_query " +
                "LEFT JOIN keywords ON keywords.keyword_id = search_query.keyword_id " +
                "LEFT JOIN locations ON locations.location_id = search_query.location_id";
        ResultSet rs = mySQL.select(query, null);

        try {
            while (rs.next()){
                Map<String, String> search = new HashMap<String, String>();
                search.put("searchID", rs.getString(1));
                search.put("keyword", rs.getString(2));
                search.put("location", rs.getString(3));
                results.add(search);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }


    public List<JobBoardHolder> searchJobs(String keyword, String location) {

        List<JobBoardHolder> jobResults = null;

        this.keyword = keyword;
        this.location = location;

        if (!checkKeywordExists() || !checkLocationExists()) {
            System.out.println("SQL error when inserting keyword or location...");
        } else {
            int lastSearched = checkWhenLastSearched();
            int HOURS_LAST_SEARCHED = 6;
            jobResults = lastSearched == -1 || ((lastSearched > -1) && (com.web.search.Date.findTimeInHours(lastSearched) > HOURS_LAST_SEARCHED)) ? crawlForJobs(true) : searchDBForJobs();
            boolean updated = (lastSearched == -1) ? addSearch() : updateSearch();
        }

        return jobResults;
    }

    private List<JobBoardHolder> searchDBForJobs() {
        String collection = this.keyword + "-" + this.location;
        List<JobBoardHolder> jobs = new ArrayList();
        MongoJobBoard mongoJobBoard = new MongoJobBoard("job_search");
        if (mongoJobBoard.documentCount(collection, null) > 0) {
            jobs = mongoJobBoard.getJobs(collection);
        }
        //Convert to JSON
        return jobs;
    }

    private List<JobBoardHolder> crawlForJobs(boolean returnJobs){
        List<JobBoardHolder> jobs = new ArrayList();
        List<String> websites = JobBoardConfig.getStartingURLs();
        for (String site : websites) {
            System.out.println("Checking " + site);
            Crawler crawler = new Crawler(keyword, location);
            crawler.startParsing(site);
            List<JobBoardHolder> jobsFound = crawler.returnJobs();
            if (jobsFound != null){
                jobs.addAll(jobsFound);
            }
        }

        if (returnJobs){
            return (jobs);
        }
        return null;
    }


    private void crawlForJobs_() {
        List<Thread> threads = new ArrayList();
        List<String> websites = JobBoardConfig.getStartingURLs();
        for (String site : websites) {
            Runnable th = new JobThread(this.keyword, this.location, site);
            Thread worker = new Thread(th);
            worker.setName(site);
            worker.start();
            System.out.println("New Thread Started");
            LOGGER.log(Level.FINE, "New Thread started for: " + site);
            threads.add(worker);
        }

        int running = 0;
        do {
            running = 0;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    running++;
                }
            }
            System.out.println("We have " + running + " running threads. ");
        } while (running > 0);

        int i = 0;
    }

    private int checkWhenLastSearched() {
        int lastSearched = -1;
        String searchQuery = "SELECT last_search FROM search_query WHERE keyword_id = ? AND location_id = ? LIMIT 1";

        ArrayList<Integer> args = new ArrayList();
        args.add(keywordID);
        args.add(locationID);

        ResultSet res = mySQL.select(searchQuery, args);
        try {
            if (res.next()) {
                lastSearched = res.getInt("last_search");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastSearched;
    }


    private boolean checkKeywordExists() {
        String keywordQuery = "SELECT keyword_id FROM keywords WHERE keyword = ? LIMIT 1";
        args.clear();
        args.add(this.keyword);
        ResultSet resultSet = mySQL.select(keywordQuery, args);
        int count;
        try {

            count = resultSet.next() ? resultSet.getInt(1) : 0;

            if (count == 0) {
                int inserted = addKeyword();
                if (inserted == 0) {
                    return false;
                }

                // If not 0 then this has to be the next inserted ID
                this.keywordID = inserted;

            } else {
                this.keywordID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    private int addKeyword() {
        String insertKeyword = "INSERT INTO keywords (keyword) VALUE (?)";
        ArrayList<String> args = new ArrayList<String>();
        args.add(keyword);
        int inserted = mySQL.update(insertKeyword, args);
        System.out.println("Keyword insert number: " + inserted);
        return inserted;
    }


    private boolean checkLocationExists() {
        args.clear();
        args.add(this.location);
        String locationQuery = "SELECT * FROM locations WHERE location = ? LIMIT 1";
        ResultSet resultSet = mySQL.select(locationQuery, args);
        int count;
        try {
            count = resultSet.next() ? resultSet.getInt(1) : 0;
            if (count == 0) {
                int inserted = addLocation();
                if (inserted == 0) {
                    return false;
                }
                // If not 0 then this has to be the next inserted ID
                this.locationID = inserted;
            } else {
                this.locationID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    private int addLocation() {
        String insertLocation = "INSERT INTO locations (location) VALUE (?)";
        ArrayList<String> args = new ArrayList<String>();
        args.add(location);
        int inserted = mySQL.update(insertLocation, args);
        System.out.println("Location insert number: " + inserted);
        return inserted;
    }

    private boolean checkSearchExists() {
        String rowQuery = "SELECT search_id FROM search_query WHERE keyword_id IN (SELECT keyword_id FROM keywords WHERE keyword = ?)" +
                " AND location_id IN (SELECT location_id FROM locations WHERE location = ?) LIMIT 1";
        args.clear();
        args.add(keywordID);
        args.add(locationID);
        int count = mySQL.rowCount(rowQuery, args);
        return count != 0;
    }

    private boolean updateSearch(){
        String updateQuery = "UPDATE search_query WHERE keyword_id = ? AND location_id = ?";
        args.clear();
        args.add(new Timestamp(Calendar.getInstance().getTime().getTime()));
        args.add(keywordID);
        args.add(locationID);
        int count = mySQL.update(updateQuery, args);
        return count != 0;
    }

    private boolean updateSearchByID(int id){
        String updateQuery = "UPDATE search_query SET last_search = ? WHERE search_id = ?";
        args.clear();
        args.add(new Timestamp(Calendar.getInstance().getTime().getTime()));
        args.add(id);
        int count = mySQL.update(updateQuery, args);
        return count != 0;
    }

    private boolean addSearch(){
        String insertQuery = "INSERT INTO search_query (keyword_id, location_id) VALUES (?, ?)";
        args.clear();
        args.add(keywordID);
        args.add(locationID);
        int count = mySQL.update(insertQuery, args);
        return count != 0;
    }

}
