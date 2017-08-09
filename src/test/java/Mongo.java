import com.web.database.MongoDB.MongoWebsite;
import com.web.database.MongoDB.Pojo.Form;
import com.web.database.MongoDB.Pojo.Page;
import com.web.database.MongoDB.Pojo.Website;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mitchell on 22/07/2017.
 */
public class Mongo {


    @Test
    public void getAllCollections(){
    }

    @Test
    public void insertOne(){

        MongoWebsite mongo = new MongoWebsite("job_search");

        Map<String, String> keyword = new HashMap<>();
        keyword.put("class", "test");
        keyword.put("tag", "testTag");

        Map<String, String> location = new HashMap<>();
        location.put("class", "test2");
        location.put("tag", "div");

        Map<String, String> formA = new HashMap<>();
        formA.put("class", "test1");
        formA.put("tag", "form");

        Map<String, String> jobContainer = new HashMap<>();
        jobContainer.put("class", "container");
        jobContainer.put("tag", "div");

        Map<String, String> containerJobTitle = new HashMap<>();
        containerJobTitle.put("class", "testJobTitle");
        containerJobTitle.put("tag", "div");

        Map<String, String> containerLocation = new HashMap<>();
        containerLocation.put("class", "testLoc");
        containerLocation.put("tag", "div");

        Map<String, String> containerLink = new HashMap<>();
        containerLink.put("class", "testLink");
        containerLink.put("tag", "div");

        Form form = new Form(formA, keyword, location);
        Page page = new Page(jobContainer, containerJobTitle, containerLocation, containerLink);

        Website website = new Website("test.co.uk",form, page);

        boolean inserted = mongo.insertOnePojo("test", website);

        Assert.assertEquals("Failed..", true, inserted);
    }

    @Test
    public void getAllDocumentsInCollection(){
        MongoWebsite mon = new MongoWebsite("test");
        int size = 1;
        // List<Document>

    }

    @Test
    public void findOneDocument(){
        MongoWebsite mon = new MongoWebsite("test");
        Map<String, Object> map = new HashMap<>();
        map.put("websiteURL","test.co.uk");
        Document doc = mon.createDocument(map);
        Website web = mon.findOneWebsite("test", doc);

        Assert.assertEquals("Website URL does not match", "test.co.uk", web.getWebsiteURL());

    }



}
