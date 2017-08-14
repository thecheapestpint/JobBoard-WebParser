import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.scraper.Crawler.Crawler;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by Mitchell on 10/08/2017.
 */
public class TotalJobs {

    @Test
    public void search(){
        String site = "https://www.totaljobs.com";
        Crawler crawler = new Crawler("Junior Developer", "London");
        crawler.startParsing(site);
        List<JobBoardHolder> jobs = crawler.returnJobs();
        Assert.assertEquals("Not 0", 10, jobs.size());
    }

}
