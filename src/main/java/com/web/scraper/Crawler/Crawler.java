package com.web.scraper.Crawler;

import com.web.database.MongoDB.MongoJobBoard;
import com.web.database.MongoDB.Pojo.Website;
import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.scraper.Misc.MySingleton;
import com.web.scraper.Parser.JobBoardParser;
import com.web.scraper.Parser.SearchParser;
import com.web.scraper.Robot.Robot;
import org.jsoup.nodes.Document;

import java.util.*;

/**
 * Created by Mitchell on 12/07/2017.
 */
public class Crawler {


    private String keywords;
    private String location;

    private boolean searchFound = false;

    private List<JobBoardHolder> jobs;

    private List<String> pagesAllowed = new ArrayList<String>();
    private List<String> pagesDisallowed = new ArrayList<String>();

    private JobBoardParser parser;

    private boolean returnJobs = false;

    public Crawler(String keywords, String location) {
        this.keywords = keywords;
        this.location = location;
    }

    public static synchronized Crawler newInstance(String keywords, String location){
        return new Crawler(keywords, location);
    }

    public List<JobBoardHolder> returnJobs(){
        return this.jobs;
    }

    public void setReturnJobs(boolean returnJobs){
        this.returnJobs = returnJobs;
    }


    public void startParsing(String startingURl) {
        parser = new JobBoardParser(startingURl);
        parser.setKeyword(keywords);
        parser.setLocation(location);

        List<JobBoardHolder> jobs = null;

        boolean check = parser.callPage("");
        if (!check) {
            System.out.println("Page Not Found");
        } else {

            if (parser.checkIfSiteExists()) {
                Website config = parser.getWebsiteConfig();
                SearchParser search = new SearchParser(startingURl);

                search.setPage(parser.getPage());
                search.setKeyword(this.keywords);
                search.setLocation(this.location);
                search.search(config.getSearchForm());
                Document newPage = search.getFirstResultsPage();
                String phantomURL = search.getPhantomURL();
                parser.setFirstPageURL(phantomURL);
                parser.scrapePage(newPage);
                this.jobs = parser.getJobs();
                System.out.println(this.jobs.size() + "");
                if (this.jobs.size() > 0) {
                    addToDatabase(this.jobs);
                }
            }
        }
    }


    private void addToDatabase(List<JobBoardHolder> jobs) {

        String collection = this.keywords + "-" + this.location;

        MongoJobBoard mongoJobs = MySingleton.INSTANCE.getMongoJobBoard();
        for (JobBoardHolder job : jobs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("jobLink", job.getJobLink());
            if (mongoJobs.documentExists(collection, mongoJobs.createDocument(map))) {
                System.out.println("Job Exists");
            } else {
                boolean insertJob = mongoJobs.insertJob(collection, job);
                if (!insertJob) {
                    System.out.println("Unable to insert");
                } else {
                    System.out.println("Inserted");
                }
            }
        }
    }

    private void checkRobot(String url) {
        Robot robot = new Robot(url);
        if (robot.checkRobotExists()) {
            robot.getRobotFile();
            pagesAllowed = robot.getAllowedURLs();
            pagesDisallowed = robot.getDisallowedURLs();
        }
    }


}
