import com.web.database.MongoDB.Pojo.JobBoardHolder;
import com.web.scraper.Crawler.Crawler;
import org.junit.Test;

import java.util.List;

/**
 * Created by Mitchell on 30/07/2017.
 */
public class Monster {

    @Test
    public void monster(){
        String site = "https://www.monster.co.uk";
        Crawler crawler = new Crawler("Junior Developer", "London");
        crawler.startParsing(site);
        List<JobBoardHolder> jobs = crawler.returnJobs();
    }
}
