package com.web.database.MongoDB;

import com.web.database.MongoDB.Pojo.Form;
import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.database.MongoDB.Pojo.Page;
import com.web.database.MongoDB.Pojo.Website;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB implements MongoInterface {

    MongoDatabase mongoDatabase;

    public MongoDB(String db) {
        try {
            CodecRegistry pojoCodecRegistry = fromRegistries(
                    fromProviders(PojoCodecProvider.builder().register(JobBoardHolder.class, Website.class, Form.class, Page.class).build()),
                    MongoClient.getDefaultCodecRegistry());
            MongoClient mongoClient = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());


            mongoDatabase = mongoClient.getDatabase(db);
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public MongoDatabase getDatabase(){
        return mongoDatabase;
    }

    public List<Document> get(String collection, Document filter) {
        mongoDatabase
                .getCollection(collection)
                .find(filter);
        return null;
    }

    public List<Document> getAll(String collection) {
        List<Document> docs = new ArrayList();
        FindIterable<Document> mongoIterable = mongoDatabase.getCollection(collection).find();
        if (mongoIterable != null) {
            mongoIterable.forEach((Block<Document>) docs::add);
        }
        return docs;
    }

    @Override
    public boolean insertOne(String collection, Document document) {
        mongoDatabase.getCollection(collection).insertOne(document);
        return mongoDatabase.getWriteConcern().isAcknowledged();
    }


    public long documentCount(String collection, Document filter){
        return mongoDatabase.getCollection(collection).count(filter);
    }


    public boolean insertMany(String collection, List<Document> documents) {
        mongoDatabase.getCollection(collection).insertMany(documents);
        return mongoDatabase.getWriteConcern().isAcknowledged();
    }

    @Override
    public MongoCollection<Document> getCollection(String collection) {
        return null;
    }

    public ListCollectionsIterable<Document> listCollections(){
        return mongoDatabase.listCollections();
    }



    public Document createDocument(Map<String, Object> map) {
        Document document = null;

        for (String key : map.keySet()) {
            if (document == null) {
                document = new Document(key, map.get(key));
            } else {
                document.append(key, map.get(key));
            }
        }

        if (document == null){
            document = new Document();
        }

        return document;
    }

}
