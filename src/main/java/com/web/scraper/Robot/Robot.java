package com.web.scraper.Robot;

import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pamitchell on 14/07/2017.
 */
public class Robot {

    private Document page;
    private String baseURL;
    private List<String> robotLines;
    private List<String> disallowedURLs;
    private List<String> allowedURLS;


    public Robot(String baseURL) {
        this.baseURL = baseURL;
        robotLines = new ArrayList();
        allowedURLS = new ArrayList();
        disallowedURLs = new ArrayList();
    }

    public boolean checkRobotExists() {
        String robotURL = this.baseURL + "/robots.txt";
        try {
            URL u = new URL(robotURL);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int responseCode = huc.getResponseCode();
            return responseCode == 200;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getRobotFile() {
        URL oracle;
        try {
            oracle = new URL(this.baseURL + "/robots.txt");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("Allow")) {
                    allowedURLS.add(inputLine.replace("Allow:", "").trim());
                }
                if (inputLine.contains("Disallow")) {
                    disallowedURLs.add(inputLine.replace("Disallow::", "").trim());
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<String> getAllowedURLs() {
        return allowedURLS;
    }

    public List<String> getDisallowedURLs() {
        return disallowedURLs;
    }

}
