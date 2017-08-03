package com.web.scraper.Parser.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mitchell on 16/07/2017.
 */
class PageConfig {


    static Map<String, String[]> getConfig(){
        Map<String, String[]> config = new HashMap();

        config.put("jobTitle", getJobTitle());
        config.put("jobLocation", getLocation());
        config.put("jobSalary", getSalary());
        return config;
    }


    static String[] getJobTitle(){
        return new String[] {
                "title",
                "job_title",
                "jobTitle",
                "position"
        };
    }

    static String[] getLocation(){
        return new String[] {
                "location",
                "l",
                "town",
                "city",
                "postcode",
                "post code"
        };
    }

    private static String[] getSalary(){

        return new String[] {
                "salary",
                "pay"
        };
    }


}
