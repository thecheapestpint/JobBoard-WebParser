package com.web.scraper.Validation;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class HTTPValidate {

    public static boolean URLValidation(String url) {
        URL u;
        try {
            u = new URL(url);
            u.toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }

        return true;
    }

}
