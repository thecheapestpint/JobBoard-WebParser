package com.web.web;

import com.codahale.metrics.annotation.Timed;
import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.search.Search;
import com.web.web.POST.SearchPost;
import com.web.web.POST.SmartPost;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("/jobs")
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public SearchResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Path("/cron")
    @Timed
    public String cron(){
        Search s = new Search();
        s.cronCrawl();
        return "Cron has finished";
    }


    @GET
    @Path("/search/{keyword}/{location}")
    @Timed
    public SearchResponse searchGet(
            @PathParam("keyword") String keyword,
            @PathParam("location") String location){
        SearchPost post = new SearchPost();
        post.setKeyword(keyword);
        post.setLocation(location);
        return search(post);
    }

    @POST
    @Path("/search")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public SearchResponse search(SearchPost searchPost) {
        //String keyword = "Junior Android";
        //String location = "London";
        String keyword = searchPost.getKeyword();
        String location = searchPost.getLocation();
        System.out.println("Keyword = " + keyword);
        Search s = new Search();
        List<JobBoardHolder> jobs = s.searchJobs(keyword, location);
        return new SearchResponse(true, jobs);
    }


    @POST
    @Path("/search/smart")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public SearchResponse smartSearch(SmartPost smartPost){
        String keyword = smartPost.getKeyword();
        String location = smartPost.getLocation();
        int amount = smartPost.getAmount();

        Search s = new Search();
        List<JobBoardHolder> jobs = s.smartJobs(keyword, location, amount);
        return new SearchResponse(true, jobs);
    }

}