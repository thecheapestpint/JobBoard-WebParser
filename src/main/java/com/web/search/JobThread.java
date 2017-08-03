package com.web.search;

import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.scraper.Crawler.Crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell on 29/07/2017.
 */
public class JobThread implements Runnable {

    private static String keywords;
    private static String location;
    private static String url;
    private static List<JobBoardHolder> jobs;

    private static final ThreadLocal<Crawler> localCrawler = new ThreadLocal<Crawler>(){
        @Override
        protected Crawler initialValue()
        {
            return new Crawler(keywords, location);
        }
    };

    private volatile boolean running = true;

    JobThread(String keywords, String location, String url){
        this.keywords = keywords;
        this.location = location;
        this.url = url;
        jobs = new ArrayList();
    }

    @Override
    public void run() {
        while (running) {
            Crawler crawler = localCrawler.get();
            List<JobBoardHolder> jobs = crawler.startParsing(url);
            running = false;
        }
    }

}
