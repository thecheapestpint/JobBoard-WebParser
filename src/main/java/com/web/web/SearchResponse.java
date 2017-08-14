package com.web.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.search.Search;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * Created by Mitchell on 01/08/2017.
 */
public class SearchResponse {

    private int count;
    private boolean success;
    private String message;
    private List<JobBoardHolder> jobs;

    public SearchResponse() {
        // Jackson deserialization
    }

    public SearchResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public SearchResponse(boolean success, List<JobBoardHolder> content) {
        this.success = success;
        this.jobs = content;
        this.count = content == null ? 0 : content.size();
    }

    public boolean getSuccess(){
        return this.success;
    }

    @JsonProperty
    public List<JobBoardHolder> getJobs() {
        return this.jobs;
    }
}
