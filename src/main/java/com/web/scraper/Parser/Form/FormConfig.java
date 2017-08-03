package com.web.scraper.Parser.Form;

/**
 * Created by Mitchell on 15/07/2017.
 */
public class FormConfig {

    public static String[] formKeywords(){

        return new String[] {
                "what",
                "keyword",
                "keywords",
                "job title",
                "title",
                "q"
        };
    }

    public static String[] formLocations(){

        return new String[] {
                "location",
                "city",
                "where",
                "town",
                "country",
                "l"
        };
    }

}
