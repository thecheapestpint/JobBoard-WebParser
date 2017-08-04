package com.web.scraper.Parser;

import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.database.MongoDB.Pojo.Page;
import com.web.database.MongoDB.Pojo.Website;
import com.web.scraper.Misc.ParseURL;
import javassist.bytecode.stackmap.TypeData;
import org.bson.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


public class JobBoardParser extends BaseParser {

    private Page pageConfig;
    private String keywords;
    private String location;
    private List<JobBoardHolder> jobBoards;

    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );

    private String firstPageURL;

    public JobBoardParser(String url) {
        super(url);
        jobBoards = new ArrayList();

    }

    public JobBoardParser newInstance(String url){
        return new JobBoardParser(url);
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setKeyword(String keyword) {
        this.keywords = keyword;
    }

    public void setFirstPageURL(String url) { this.firstPageURL = url; }


    private Elements getJobBoardContainers(String tag, String attributeKey, String attributeValue) {
        Elements found = page.getElementsByAttributeValueMatching(attributeKey, attributeValue);
        Elements sorted = new Elements();
        for (Element el : found) {
            if (el.tagName().equals(tag)) {
                sorted.add(el);
                System.out.println("Job Board Container Found");
            }
        }
        return sorted;
    }

    private String getKeyNotTag(Set<String> keys) {
        for (String key : keys) {
            if (!key.equals("tag")) {
                return key;
            }
        }
        return "";
    }

    public void scrapPage(org.jsoup.nodes.Document page) {
        this.page = page;
        Map<String, String> container = pageConfig.getJobContainer();
        String containerKey = getKeyNotTag(container.keySet());
        if (!containerKey.isEmpty()) {

            Elements containers = getJobBoardContainers(container.get("tag"), containerKey, container.get(containerKey));
            if (container.size() != 0) {
                for (Element contain : containers) {
                    findJobInformation(contain);
                }
            }
            getNextPage();
        }
    }



    private boolean checkNextButton(Element nextPageContainer, Map<String, String> nextPage, String nextKey){
        //return nextPageContainer.tagName().equals(nextPage.get("tag")) && nextPageContainer.text().toLowerCase().contains("next") || nextPageContainer.attributes().get(nextKey).contains(nextPage.get(nextKey));
        return nextPageContainer.tagName().equals(nextPage.get("tag")) && nextPageContainer.text().toLowerCase().contains("next");
    }

    private Elements getPageContainer(String nextKey){
        Map<String, String> nextPage = pageConfig.getNextPage();

        Elements byAttribute = this.page.getElementsByAttributeValueMatching(nextKey, nextPage.get(nextKey));
        return byAttribute.size() == 0 ? this.page.getElementsByTag(nextPage.get("tag")) : byAttribute;
    }

    private void getNextPage() {
        Map<String, String> nextPage = pageConfig.getNextPage();
        String nextPageURL = "";

        if (nextPage != null) {
            String nextKey = getKeyNotTag(nextPage.keySet());
            Elements nextPageContainers = getPageContainer(nextKey);
            for (Element nextPageContainer : nextPageContainers) {

                if (checkNextButton(nextPageContainer, nextPage, nextKey)) {
                    nextPageURL = nextPageContainer.attr("href");
                    break;
                }
            }

            if (!nextPageURL.equals("") && !nextPageURL.contains("http")) {
                String url = ParseURL.convertURL(this.firstPageURL, nextPageURL);
                System.out.println("Next page: " + url);
                boolean callCheck = callPage(url);
                if (callCheck) {
                    org.jsoup.nodes.Document newPage = phantomCall(url);
                    scrapPage(newPage);
                }
            }
        }
    }

    private boolean checkKeywordContains(String check) {
        String[] split = this.keywords.split("\\s+");
        for (String s : split) {
            if (!check.contains(s)) {
                return false;
            }
        }
        return true;
    }


    private void findJobInformation(Element container) {
        String salary = "";
        String link = "";
        String jobTitle = getJobTitle(container);
        String location = getLocation(container);
        //If job titles empty, we don't go further

        if (!jobTitle.isEmpty() && !location.isEmpty()) {
            System.out.println("Job Title " + jobTitle);
            link = getLink(container);
            salary = getSalary(container);
            jobBoards.add(new JobBoardHolder(jobTitle, location, salary, link, this.url));
            System.out.println("Job Added");
        }
    }

    private String getJobTitle(Element container) {
        String jobTitle = "";
        Map<String, String> jobTitleConfig = pageConfig.getKeyword();

        String titleKey = getKeyNotTag(jobTitleConfig.keySet());

        Element titleElement = container.getElementsByAttributeValueContaining(titleKey, jobTitleConfig.get(titleKey)).first();

        if (titleElement != null && checkKeywordContains(titleElement.text())) {
            jobTitle = titleElement.text();
        }

        return jobTitle;

    }

    private String getLocation(Element container) {
        String location = "";
        Map<String, String> locationConfig = pageConfig.getLocation();
        String locationKey = getKeyNotTag(locationConfig.keySet());
        Element locationElement = container.getElementsByAttributeValueMatching(locationKey, locationConfig.get(locationKey)).first();
        if (locationElement != null) {
            location = locationElement.text();
        }
        return location;
    }

    private String getSalary(Element container) {
        String salary = "";
        Map<String, String> salaryConfig = pageConfig.getSalary();
        if (salaryConfig != null) {
            String salaryKey = getKeyNotTag(salaryConfig.keySet());
            Element locationElement = container.getElementsByAttributeValueMatching(salaryKey, salaryConfig.get(salaryKey)).first();
            if (locationElement != null) {
                salary = locationElement.text();
            }
        }
        return salary;
    }

    private String getLink(Element container) {
        String link = "";
        Map<String, String> linkConfig = pageConfig.getLink();

        String linkKey = getKeyNotTag(linkConfig.keySet());
        Element linkElement = container.getElementsByAttributeValueMatching(linkKey, linkConfig.get(linkKey)).first();
        if (linkElement.attributes().hasKey("href")) {
            link = linkElement.attributes().get("href");
        }
        return link;
    }

    public Website getWebsiteConfig() {
        Map<String, Object> search = new HashMap();
        search.put("websiteURL", this.url);
        Document searchDocument = mongoWebsite.createDocument(search);
        Website websiteConfig = mongoWebsite.findOneWebsite("website_config", searchDocument);
        pageConfig = websiteConfig.getPage();
        return websiteConfig;
    }


    public boolean checkIfSiteExists() {
        Map<String, Object> search = new HashMap();
        search.put("websiteURL", this.url);
        Document searchDocument = mongoWebsite.createDocument(search);
        return mongoWebsite.documentCount("website_config", searchDocument) != 0;
    }

    public List<JobBoardHolder> getJobs() {
        return jobBoards;
    }

}
