import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.scraper.Crawler.Crawler;
import org.junit.Test;

import java.util.List;

/**
 * Created by Mitchell on 03/08/2017.
 */
public class Reed {

    @Test
    public void reed(){
        String site = "https://www.reed.co.uk";
        Crawler crawler = new Crawler("Junior Developer", "London");
        crawler.startParsing(site);
        List<JobBoardHolder> jobs = crawler.returnJobs();
    }

}
