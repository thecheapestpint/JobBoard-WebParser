package com.web.web;

import com.codahale.metrics.health.HealthCheck;

public class SearchHealthCheck extends HealthCheck {

    private String template;

    public SearchHealthCheck(String template){
        this.template = template;
    }


    protected Result check() throws Exception {
        final String saying = String.format(template, "TEST");
        if (!saying.contains("TEST")) {
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }
}
