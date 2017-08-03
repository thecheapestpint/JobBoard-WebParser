package com.web.scraper.Misc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Created by Mitchell on 24/07/2017.
 */
public enum MySingleton {
    INSTANCE;
    WebDriver webDriver = new PhantomJSDriver();

    public WebDriver getWebDriver(){
        return webDriver;
    }
}
