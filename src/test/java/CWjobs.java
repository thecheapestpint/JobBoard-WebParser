import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.scraper.Crawler.Crawler;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by Mitchell on 16/08/2017.
 */
public class CWjobs {

    @Test
    public void cwjobs(){
        String site = "https://www.cwjobs.co.uk";
        Crawler crawler = new Crawler("Junior Developer", "London");
        crawler.startParsing(site);
        List<JobBoardHolder> jobs = crawler.returnJobs();
        Assert.assertEquals("Not 0", 10, jobs.size());
    }

}
