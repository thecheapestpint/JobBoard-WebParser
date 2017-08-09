package com.web.database.MongoDB;

import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoJobBoard extends MongoDB {
    public MongoJobBoard(String db) {
        super(db);
    }

    public boolean insertJob(String collection, JobBoardHolder job){
        mongoDatabase.getCollection(collection, JobBoardHolder.class).insertOne(job);
        return mongoDatabase.getWriteConcern().isAcknowledged();
    }

    public List<JobBoardHolder> getJobs(String collection){
        List<JobBoardHolder> jobs = new ArrayList();
        FindIterable<JobBoardHolder> iterable = mongoDatabase.getCollection(collection).find(JobBoardHolder.class);
        if (iterable != null) {
            for (JobBoardHolder job : iterable) {
                jobs.add(job);
            }
        }
        return jobs;
    }

    public boolean documentExists(String collection, Document filter){
        return super.documentCount(collection, filter) != 0;
    }


}
