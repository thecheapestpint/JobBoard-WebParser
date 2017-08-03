package com.web;

import com.web.web.JobSearchConfig;
import com.web.web.SearchHealthCheck;
import com.web.web.SearchResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class JobApplication extends Application<JobSearchConfig> {
    public static void main(String[] args) throws Exception {
        new JobApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<JobSearchConfig> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(JobSearchConfig configuration,
                    Environment environment) {
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