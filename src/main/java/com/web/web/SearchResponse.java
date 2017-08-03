package com.web.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.database.MongoDB.Pojo.JobBoardHolder;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * Created by Mitchell on 01/08/2017.
 */
public class SearchResponse {

    private long id;
    private boolean success;
    private List<JobBoardHolder> jobs;

    public SearchResponse() {
        // Jackson deserialization
    }

    public SearchResponse(boolean success, List<JobBoardHolder> content) {
        this.success = success;
        this.jobs = content;
    }

    public boolean getSuccess(){
        return this.success;
    }

    @JsonProperty
    public List<JobBoardHolder> getJobs() {
        return this.jobs;
    }
}
