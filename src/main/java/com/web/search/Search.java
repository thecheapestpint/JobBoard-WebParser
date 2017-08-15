package com.web.search;


import com.web.JobApplication;
import com.web.database.MongoDB.MongoJobBoard;

import com.web.database.MongoDB.Pojo.JobBoardHolder;

import com.web.database.MySQL.MySQL;
import com.web.scraper.Crawler.Crawler;
import com.web.scraper.Crawler.JobBoard.JobBoardConfig;

import com.web.scraper.Misc.Convert;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import com.web.web.SearchResponse;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
/**
 * Created by Mitchell on 28/07/2017.
 */
public class Search {

    private MySQL mySQL;

    private String keyword;
    private int keywordID;

    private String location;
    private int locationID;

    private ArrayList args;

    private static final Logger logger = LogManager.getLogger(Search.class);

    /*

        VERY VERY VERY VERY MESSY

     */


    public Search() {
        mySQL = new MySQL("job_search");
        args = new ArrayList();
        BasicConfigurator.configure();
        logger.trace("Search INIT.");
    }

    public void cronCrawl() {

        logger.debug("Cron Started.");
        List<Map<String, String>> searchRes = getSearchKeywordsAndLocations();
        if (searchRes.size() > 0) {
            for (Map<String, String> res : searchRes) {
                keyword = res.get("keyword");
                location = res.get("location");
                logger.debug("Cron keyword: " + keyword);
                logger.debug("Cron Location:" + location);
                crawlForJobs();
                updateSearchByID((Integer.parseInt(res.get("searchID"))));
            }
        }
    }

    private List<Map<String, String>> getSearchKeywordsAndLocations() {

        List<Map<String, String>> results = new ArrayList();

        logger.trace("Searching keywords and locations.");

        String query = "SELECT search_query.search_id, keywords.keyword, locations.location FROM search_query " +
                "LEFT JOIN keywords ON keywords.keyword_id = search_query.keyword_id " +
                "LEFT JOIN locations ON locations.location_id = search_query.location_id";
        ResultSet rs = mySQL.select(query, null);

        try {
            while (rs.next()) {
                Map<String, String> search = new HashMap<String, String>();
                search.put("searchID", rs.getString(1));
                search.put("keyword", rs.getString(2));
                search.put("location", rs.getString(3));
                logger.trace("found keywords and location: " + search.toString());
                results.add(search);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }


    public void crawlListCron(){
        List<Integer> crawlList = getCrawlList();
        for (int searchID : crawlList){
            Map<String, String> keywordLocation = getCrawlListKeywordsLocations(searchID);
            this.keyword = keywordLocation.get("keyword");
            this.location = keywordLocation.get("location");
            crawlForJobs();
            boolean crawlUpdated = searchCrawlUpdate(searchID);
            logger.debug("Crawler updated " + searchID + ": " + crawlUpdated);
            boolean searchUpdated = updateSearchByID(searchID);
            logger.debug("Search Updated " + searchID + ": " + searchUpdated);
        }
    }

    /*
       I HATE HOW I'VE DONE THIS.
       ALL OF THIS CAN BE TIDIER.
     */

    private Map<String, String> getCrawlListKeywordsLocations(int searchID) {
        args.clear();
        String query = "SELECT search_query.search_id, keywords.keyword, locations.location FROM search_query " +
                "LEFT JOIN keywords ON keywords.keyword_id = search_query.keyword_id " +
                "LEFT JOIN locations ON locations.location_id = search_query.location_id WHERE search_query.search_id = ? LIMIT 1";
        args.add(searchID);
        ResultSet rs = mySQL.select(query, args);

        Map<String, String> results = new HashMap<String, String>();

        try {
            while (rs.next()) {
                results.put("keyword", rs.getString(2));
                results.put("location", rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    private List<Integer> getCrawlList(){
        args.clear();
        List<Integer> crawlList = new ArrayList<>();
        String crawlListQuery = "SELECT search_id FROM search_crawl WHERE searched = ?";
        args.add(1);
        ResultSet resultSet = mySQL.select(crawlListQuery, args);
        try {
            if (resultSet.next()) {
                crawlList.add(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return crawlList;
    }

    public SearchResponse searchJobs(String keyword, String location) {

        List<JobBoardHolder> jobResults;

        this.keyword = keyword;
        this.location = location;

        if (!checkKeywordExists() || !checkLocationExists()) {
            System.out.println("");
            logger.trace("SQL error when inserting keyword or location...");
            return new SearchResponse(false, "Could not process your request. Please try again later.");
        } else {
            Timestamp lastSearched = checkWhenLastSearched();
            if (lastSearched == null) {
                addSearchToCrawlList();
                return new SearchResponse(true, "We need to scrape some websites for your search. Please give us 10 minutes.");
            } else {
                logger.trace("searchJobs - Search for jobs");
                jobResults = searchDBForJobs();
            }
            updateSearch();
        }

        return new SearchResponse(true, jobResults);
    }


    private void addSearchToCrawlList(){
        int searchID = addSearch();
        if (searchID == 0) {
            System.out.println("Unable to insert into database");
        } else {
            String query = "INSERT INTO search_crawl (search_id) VALUE (?)";
            args.clear();
            args.add((searchID));
            int insert = mySQL.update(query, args, false);
            if (insert == 0){
                System.out.println("Error inserting search crawl");
            }
        }
    }

    public List<JobBoardHolder> smartJobs(String keyword, String location, int amount){
        List<JobBoardHolder> jobResults;

        this.keyword = keyword;
        this.location = location;

        // If Keyword or Location has not been found, then put it in the list.
        // This will be done in the next cron.
        // Don't care as much about smart devices
        if (!checkKeywordExists() || !checkLocationExists()){
            jobResults = null;
        } else{
            jobResults = searchDBForJobs();
            if (jobResults.size() > amount){
                jobResults = Convert.getJobBoardList(jobResults, keyword, location, amount);
            }
        }
        return jobResults;
    }


    private List<JobBoardHolder> searchDBForJobs() {
        String collection = this.keyword + "-" + this.location;
        logger.trace("searchDB - collection :" + collection);
        List<JobBoardHolder> jobs = new ArrayList();
        MongoJobBoard mongoJobBoard = new MongoJobBoard("job_search");
        if (mongoJobBoard.documentCount(collection, null) > 0) {
            logger.trace("document count is greater than 0");
            jobs = mongoJobBoard.getJobs(collection);
        }
        return jobs;
    }

    private void crawlForJobs() {

        List<String> websites = JobBoardConfig.getStartingURLs();
        for (String site : websites) {
            System.out.println("Checking " + site);
            Crawler crawler = new Crawler(keyword, location);
            crawler.startParsing(site);
        }

    }



    private Timestamp checkWhenLastSearched() {
        Timestamp lastSearched = null;
        String searchQuery = "SELECT last_search FROM search_query WHERE keyword_id = ? AND location_id = ? LIMIT 1";

        ArrayList<Integer> args = new ArrayList();
        args.add(keywordID);
        args.add(locationID);

        ResultSet res = mySQL.select(searchQuery, args);
        try {
            if (res.next()) {
                lastSearched = res.getTimestamp("last_search");
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
        int inserted = mySQL.update(insertKeyword, args, false);
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
        int inserted = mySQL.update(insertLocation, args, false);
        System.out.println("Location insert number: " + inserted);
        return inserted;
    }



    private boolean updateSearch() {
        String updateQuery = "UPDATE search_query set last_search = ? WHERE keyword_id = ? AND location_id = ?";
        args.clear();
        args.add(new Timestamp(Calendar.getInstance().getTime().getTime()));
        args.add(keywordID);
        args.add(locationID);
        int count = mySQL.update(updateQuery, args, false);
        return count != 0;
    }

    private boolean updateSearchByID(int id) {
        String updateQuery = "UPDATE search_query SET last_search = ? WHERE search_id = ?";
        args.clear();
        args.add(new Timestamp(Calendar.getInstance().getTime().getTime()));
        args.add(id);
        int count = mySQL.update(updateQuery, args, false);
        return count != 0;
    }

    private int addSearch() {
        String insertQuery = "INSERT INTO search_query (keyword_id, location_id) VALUES (?, ?)";
        args.clear();
        args.add(keywordID);
        args.add(locationID);
        return mySQL.update(insertQuery, args, true);
    }

    private boolean searchCrawlUpdate(int searchID){
        String query = "UPDATE search_crawl SET searched = ? WHERE search_id = ?";
        args.clear();
        args.add('1');
        args.add(searchID);
        return mySQL.update(query, args,false) != 0;
    }

}
