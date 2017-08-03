package com.web.web;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mitchell on 01/08/2017.
 */
public class SearchRequest {

    private long id;

    public long getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getLocation() {
        return location;
    }

    private String keyword;
    private String location;

    @JsonProperty
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    @JsonProperty
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }
}
