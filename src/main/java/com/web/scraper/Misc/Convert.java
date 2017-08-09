package com.web.scraper.Misc;


import com.web.database.MongoDB.Pojo.JobBoardHolder;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Mitchell on 14/07/2017.
 */
public class Convert {

    public static ArrayList convertFromJSONToArrayList(JSONArray json) {
        ArrayList<String> converted = new ArrayList<String>();
        for (Object aJson : json) {
            converted.add(aJson.toString());
        }
        return converted;
    }


    public static List<JobBoardHolder> getJobBoardList(List<JobBoardHolder> jobs, String keyword, String location, int amount){
        List<JobBoardHolder> foundJobs = new ArrayList<>();

        if (jobs.size() > amount){
            // randomly get them
            for(int x=0; x < amount; x++){
                int randomNum = ThreadLocalRandom.current().nextInt(0, jobs.size() + 1);
                foundJobs.add(jobs.get(randomNum));
                jobs.remove(randomNum);
            }
        } else {
            return jobs;
        }

        return foundJobs;
    }

}
