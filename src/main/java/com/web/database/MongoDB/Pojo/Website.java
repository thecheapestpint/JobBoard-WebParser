package com.web.database.MongoDB.Pojo;

import org.bson.types.ObjectId;

/**
 * Created by Mitchell on 22/07/2017.
 */
public class Website {

    private ObjectId id;
    private String websiteURL;
    private Form searchForm;
    private Page page;

    public Website(){

    }

    public Website(String websiteURL, Form form, Page page){
        this.searchForm = form;
        this.page = page;
        this.websiteURL = websiteURL;
    }

    public Form getSearchForm(){
        return searchForm;
    }

    public Page getPage(){
        return page;
    }

    public String getWebsiteURL(){
        return this.websiteURL;
    }
}
