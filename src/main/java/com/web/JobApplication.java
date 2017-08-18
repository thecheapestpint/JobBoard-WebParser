package com.web;

import com.web.search.Search;
import com.web.web.JobSearchConfig;
import com.web.web.SearchHealthCheck;
import com.web.web.SearchResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JobApplication extends Application<JobSearchConfig> {

    private static final Logger logger = LogManager.getLogger(JobApplication.class);

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        new JobApplication().run(args);
    }

    public void setupCronJobs(){
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);

        // SETUP CRON CRAWL
        ses.scheduleAtFixedRate(() -> {
            logger.debug("Cron Crawl Started");
            Search s = new Search();
            s.cronCrawl();
        }, 0, 8, TimeUnit.HOURS);

        ses.scheduleAtFixedRate(() -> {
            Search s = new Search();
            logger.debug("Cron List Started");
            s.crawlListCron();
        }, 0, 20, TimeUnit.MINUTES);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<JobSearchConfig> bootstrap) {
    }

    @Override
    public void run(JobSearchConfig configuration,
                    Environment environment) {

        setupCronJobs();
        final SearchResource resource = new SearchResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );

        final SearchHealthCheck healthCheck =
                new SearchHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

        environment.jersey().register(resource);
    }

}