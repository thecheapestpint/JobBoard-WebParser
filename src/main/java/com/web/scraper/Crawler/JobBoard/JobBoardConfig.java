package com.web.scraper.Crawler.JobBoard;

import com.web.database.MySQL.MySQL;
import com.web.scraper.Misc.Convert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mitchell on 14/07/2017.
 */
public class JobBoardConfig {

    public static ArrayList getStartingURLs_(){
        ArrayList urls = null;
        JSONParser parser = new JSONParser();
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("JobBoards/jobboards.json").toURI());
            Object jsonObj = parser.parse(new FileReader(path.toString()));
            JSONObject boardInfo = (JSONObject) jsonObj;
            JSONArray jsonLinks =  (JSONArray) boardInfo.get("links");
            urls = Convert.convertFromJSONToArrayList(jsonLinks);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return urls;
    }


    public static ArrayList<String> getStartingURLs(){
        ArrayList<String> urls = new ArrayList();

        String query = "SELECT website_url FROM website";
        MySQL mysql = MySQL.instance("job_search");
        ResultSet res = mysql.select(query, null);
        try {
            while (res.next()){
                urls.add(res.getString(1));
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return urls;
    }

}