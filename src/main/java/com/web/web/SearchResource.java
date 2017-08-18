package com.web.web;

import com.codahale.metrics.annotation.Timed;
import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.search.Search;
import com.web.web.POST.SearchPost;
import com.web.web.POST.SmartPost;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("/jobs")
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {
    private final String template;
    private final String defaultName;

    public SearchResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;

    }

    @GET
    @Path("/cron")
    @Timed
    public void cron() {
        Search s = new Search();
        s.cronCrawl();
    }


    @GET
    @Path("/search/{keyword}/{location}")
    @Timed
    public SearchResponse searchGet(
            @PathParam("keyword") String keyword,
            @PathParam("location") String location) {
        SearchPost post = new SearchPost();
        post.setKeyword(keyword.toLowerCase());
        post.setLocation(location.toLowerCase());
        return search(keyword, location);
    }

    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    public SearchResponse search(SearchPost searchPost) {
        String keyword = searchPost.getKeyword().toLowerCase();
        String location = searchPost.getLocation().toLowerCase();
        System.out.println("Keyword = " + keyword);
        return new Search().searchJobs(keyword, location);
    }


    @POST
    @Path("/search")
    @Timed
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public SearchResponse search(@FormParam("keyword") String keyword, @FormParam("location") String location) {
        System.out.println("Keyword = " + keyword);
        return new Search().searchJobs(keyword.toLowerCase(), location.toLowerCase());
    }


    @POST
    @Path("/search/smart")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public SearchResponse smartSearch(SmartPost smartPost) {
        String keyword = smartPost.getKeyword().toLowerCase();
        String location = smartPost.getLocation().toLowerCase();
        int amount = smartPost.getAmount();

        Search s = new Search();
        List<JobBoardHolder> jobs = s.smartJobs(keyword, location, amount);
        return new SearchResponse(true, jobs);
    }

}