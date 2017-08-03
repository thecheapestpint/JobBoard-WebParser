package com.web.database.MongoDB.Pojo;

import org.bson.types.ObjectId;

import java.time.Instant;

/**
 * Created by Mitchell on 22/07/2017.
 */
public class JobBoardHolder {

    private ObjectId id;

    private String jobTitle;
    private String jobLocation;
    private String jobSalary;
    private String jobLink;
    private String jobBoardURL;
    private long timeAdded;

    public JobBoardHolder(String title, String location, String salary, String link, String jobBoardURL ){
        this.jobTitle = title;
        this.jobLocation = location;
        this.jobSalary = salary;
        this.jobLink = link;
        this.jobBoardURL = jobBoardURL;
        this.timeAdded = Instant.now().toEpochMilli();
    }

    public JobBoardHolder(String title, String location, String link ){
        this.jobTitle = title;
        this.jobLocation = location;
        this.jobLink = link;
        this.timeAdded = Instant.now().toEpochMilli();
    }


    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public String getJobSalary() {
        return jobSalary;
    }

    public String getJobLink() {
        return jobLink;
    }

    public String getJobBoardURL(){
        return jobBoardURL;
    }

    public Long getTimeAdded() { return timeAdded; }

}
