package com.web.search;

/**
 * Created by Mitchell on 13/08/2017.
 */
public class BasicSearchThread implements Runnable {

    private String keyword;
    private String location;

    public BasicSearchThread(String keyword, String location){
        this.keyword = keyword;
        this.location = location;
    }

    @Override
    public void run() {

    }
}
