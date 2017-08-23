package com.web.job;

import com.google.gson.annotations.Expose;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoIterable;
import com.web.database.MongoDB.MongoJobBoard;
import org.bson.Document;

/**
 * Created by pamitchell on 23/08/2017.
 */
public class Job {

    private static int EXPIRY_DAYS = 7;
    private MongoJobBoard mongoJobBoard;

    public Job(){
        mongoJobBoard = new MongoJobBoard("job_search");
    }


    public void findExpired(){
        MongoIterable<String> collections = mongoJobBoard.getCollectionNames();

        for (String collection : collections){
            checkCollections(collection);
        }

    }

    private void checkCollections(String collection){

    }



}
