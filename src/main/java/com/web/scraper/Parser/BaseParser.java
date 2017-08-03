package com.web.scraper.Parser;

import com.web.database.MongoDB.MongoWebsite;
import com.web.scraper.Misc.MySingleton;
import com.web.scraper.Validation.HTTPValidate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Mitchell on 22/07/2017.
 */
public class BaseParser {

    protected String url;
    protected Document page;
    protected String phantomURL;


    protected MongoWebsite mongoWebsite;

    public BaseParser(String url) {
        this.url = url;
        mongoWebsite = new MongoWebsite("test");
    }

    public void destroy(){
        this.page = null;
        this.mongoWebsite = null;
    }

    public boolean callPage(String url) {
        String urlToCall = url.isEmpty() ? this.url : url;
        if (HTTPValidate.URLValidation(urlToCall)) {
            try {
                Document con = Jsoup.connect(urlToCall).get();
                page = Jsoup.parse(con.html(), urlToCall, org.jsoup.parser.Parser.xmlParser());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }



    public Document phantomCall(String url) {

        // We need to get let the javascript run on the page.
        // Phantom allows this. Then we pass it back into JSOUP
        Path path = null;
        WebDriver webDriver = null;
        try {
            // path = Paths.get(ClassLoader.getSystemResource("Phantom/phantomjs.exe").toURI());
            URI uri = Thread.currentThread().getContextClassLoader().getResource("Phantom/phantomjs.exe").toURI();
            path = Paths.get(uri);
            System.setProperty("phantomjs.binary.path", path.toString());
            webDriver = MySingleton.INSTANCE.getWebDriver();
            webDriver.get(url);
            phantomURL = webDriver.getCurrentUrl();
            return Jsoup.parse(webDriver.getPageSource());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getPhantomURL(){
        return this.phantomURL;
    }

    public Document getPage() {
        return this.page;
    }

    private boolean checkAttributes(Element el, String toFind) {
        Attributes attrs = el.attributes();
        for (Attribute attr : attrs) {
            if (attr.getValue().equals(toFind)) {
                return true;
            }
        }
        return false;
    }


    public Element loopUntilFindAttribute(Element el, String toFind) {
        Element found = null;
        if (el.html().contains(toFind)) {
            if (el.children().size() == 0 && checkAttributes(el, toFind)) {
                found = el;
            } else {
                Elements elChildren = el.children();
                for (Element elChild : elChildren) {
                    Element foundChild = loopUntilFindAttribute(el, toFind);
                    if (foundChild != null) {
                        found = foundChild;
                        break;
                    }
                }
            }
        }
        return found;
    }

    public Element loopUntilFindText(Element el, String toFind) {
        Element found = null;

        if (el.text().contains(toFind)) {
            if (el.children().size() == 0) {
                found = el;
            } else {
                Elements elChildren = el.children();
                for (Element elChild : elChildren) {
                    Element foundChild = loopUntilFindText(elChild, toFind);
                    if (foundChild != null) {
                        found = foundChild;
                        break;
                    }
                }
            }
        }
        return found;
    }


}
