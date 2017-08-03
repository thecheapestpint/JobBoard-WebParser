package com.web.scraper.Parser;


import com.web.database.MongoDB.Pojo.Form;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

public class SearchParser extends BaseParser {

    private Document newPage;
    private String keyword;
    private String location;

    public SearchParser(String url) {
        super(url);
        this.page = null;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPage(Document page){
        this.page = page;
    }


    private Element getForm(Map<String, String> formAttributes) {
        Element foundForm = null;
        if (formAttributes.containsKey("tag") && (formAttributes.containsKey("id") || formAttributes.containsKey("name")
                || formAttributes.containsKey("class"))) {

            Elements forms = this.page.getElementsByTag(formAttributes.get("tag"));

            // We don't need this for the next bit
            formAttributes.remove("tag");

            for (Element form : forms) {
                for (String key : formAttributes.keySet()) {
                    if (form.attributes().html().contains(formAttributes.get(key))) {
                        foundForm = form;
                    }
                }
            }

        }
        return foundForm;
    }


    private Element getInput(Element el, Map<String, String> attributes) {
        Element foundInput = null;

        if (attributes.containsKey("tag") && el.html().contains("name")) {

            Element element = el.getElementsByAttributeValueMatching("name", attributes.get("name")).get(0);
            if (element != null) {
                foundInput = element;
            }
        }

        return foundInput;
    }

    public void search(Form formConfig) {

        Element form = getForm(formConfig.getFormAttributes());
        Element keywordSearch = getInput(form, formConfig.getKeywordAttributes());
        Element locationSearch = getInput(form, formConfig.getLocationAttribtes());


        if (keywordSearch != null && locationSearch != null) {

            String url = form.attr("action");
            if (!url.contains("http")) {
                url = this.url + url;
            }

            try {
                newPage = Jsoup.connect(url)
                        .data(keywordSearch.attr("name"), keyword)
                        .data(locationSearch.attr("name"), location)
                        .timeout(10 * 1000).get();

                Document phantomDoc = phantomCall(newPage.baseUri());

                if (phantomDoc != null){
                    newPage = phantomDoc;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        destroy();

    }



    public Document getFirstResultsPage(){
        return this.newPage;
    }

}
