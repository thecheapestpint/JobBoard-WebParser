package com.web.database.MongoDB.Pojo;

import java.util.Map;

public class Form {

    private Map<String, String> form;
    private Map<String, String> keyword;
    private Map<String, String> location;

    public Form(){

    }

    public Map<String, String> getFormAttributes() {
        return form;
    }

    public Map<String, String> getKeywordAttributes() {
        return keyword;
    }

    public Map<String, String> getLocationAttribtes() {
        return location;
    }

    public Form(Map<String, String> formAttributes, Map<String, String> keywordAttributes, Map<String, String> locationAttribtes) {
        this.form = formAttributes;
        this.keyword = keywordAttributes;
        this.location = locationAttribtes;
    }


}
