package com.web.scraper.Misc;


import org.json.simple.JSONArray;

import java.util.ArrayList;

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

}
