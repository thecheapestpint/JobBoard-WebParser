package com.web.database.MongoDB;

import com.web.database.MongoDB.Pojo.Website;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell on 22/07/2017.
 */
public class MongoWebsite extends MongoDB {

    private List<Website> websites;

    public MongoWebsite(String db) {
        super(db);
        websites = new ArrayList();
    }

    public static MongoWebsite getInstance(String db){
        return new MongoWebsite(db);
    }

    public boolean insertOnePojo(String collection, Website website) {
        mongoDatabase.getCollection(collection, Website.class).insertOne(website);
        return mongoDatabase.getWriteConcern().isAcknowledged();
    }

    public boolean insertManyPojo(String collection, List<Website> websites) {
        mongoDatabase.getCollection(collection, Website.class).insertMany(websites);
        return mongoDatabase.getWriteConcern().isAcknowledged();
    }

    public List findAllWebsites(String collection) {
        List<Website> docs = new ArrayList();
        FindIterable websites = mongoDatabase.getCollection(collection, Website.class).find();
        if (websites != null) {
            websites.forEach((Block<Website>) docs::add);
        }
        return docs;
    }


    public Website findOneWebsite(String collection, Document search) {
        return mongoDatabase.getCollection(collection, Website.class).find(search).first();
    }


}
