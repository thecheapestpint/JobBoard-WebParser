package com.web.database.MongoDB;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Created by Mitchell on 22/07/2017.
 */
interface MongoInterface {
    boolean insertOne(String collection, Document document);
    MongoCollection<Document> getCollection(String collection);
}
