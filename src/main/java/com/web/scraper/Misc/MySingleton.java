package com.web.scraper.Misc;

import com.web.database.MongoDB.MongoJobBoard;
import com.web.database.MongoDB.MongoWebsite;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Created by Mitchell on 24/07/2017.
 */
public enum MySingleton {
    INSTANCE;
    MongoWebsite mongoWebsite;
    MongoJobBoard mongoJobBoard;

    public MongoWebsite getMongoWebsite(){
        if(mongoWebsite == null){
            mongoWebsite = new MongoWebsite("job_search");
        }
        return mongoWebsite;
    }

    public MongoJobBoard getMongoJobBoard(){
        if (mongoJobBoard == null){
            mongoJobBoard = new MongoJobBoard("job_search");
        }
        return mongoJobBoard;
    }

}


