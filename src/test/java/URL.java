import com.web.scraper.Crawler.JobBoard.JobBoardConfig;
import com.web.scraper.Misc.Convert;
import com.web.scraper.Validation.HTTPValidate;
import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class URL {

    @Test
    public void getURLFromFile(){

        JSONArray jsonArray = new JSONArray();
        jsonArray.add("test");
        jsonArray.add("test2");

        ArrayList correctList = new ArrayList();
        correctList.add("test");
        correctList.add("test2");

        ArrayList testList = Convert.convertFromJSONToArrayList(jsonArray);

        Assert.assertEquals("Does not match", correctList, testList);

    }

    @Test
    public void urlValid(){
        boolean isValid = HTTPValidate.URLValidation("http://google.co.uk");
        Assert.assertEquals("URL is not valid", true, isValid);
    }

    @Test
    public void getUrlSuccess(){
        int expected = 3;
        ArrayList urls = JobBoardConfig.getStartingURLs();
        Assert.assertEquals("Size does not match...", expected, urls.size());
    }

}
