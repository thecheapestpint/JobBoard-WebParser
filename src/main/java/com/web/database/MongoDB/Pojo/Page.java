package com.web.database.MongoDB.Pojo;

import java.util.Map;

public class Page {

    public Page(){

    }

    private Map<String, String> jobContainer;


    public Map<String, String> getJobContainer() {
        return jobContainer;
    }

    public Map<String, String> getKeyword() {
        return keyword;
    }

    public Map<String, String> getLocation() {
        return location;
    }

    public Map<String, String> getSalary() {
        return salary;
    }

    public Map<String, String> getLink(){
        return link;
    }

    public Map<String, String> getNextPage(){
        return nextPage;
    }

    private Map<String, String> keyword;
    private Map<String, String> location;
    private Map<String, String> salary;
    private Map<String, String> link;
    private Map<String, String> nextPage;

    public Page(Map<String, String> jobContainer, Map<String, String> keyword, Map<String, String> location, Map<String, String> link, Map<String, String> salary){
        this.jobContainer = jobContainer;
        this.keyword = keyword;
        this.location = location;
        this.salary = salary;
        this.link = link;
    }

    public Page(Map<String, String> jobContainer, Map<String, String> keyword, Map<String, String> location, Map<String, String> link, Map<String, String> salary, Map<String, String> nextPage){
        this.jobContainer = jobContainer;
        this.keyword = keyword;
        this.location = location;
        this.salary = salary;
        this.link = link;
        this.nextPage = nextPage;
    }


    public Page(Map<String, String> jobContainer, Map<String, String> keyword, Map<String, String> location, Map<String, String> link){
        this.jobContainer = jobContainer;
        this.keyword = keyword;
        this.location = location;
        this.link = link;
    }

}
