import com.web.JobApplication;
import com.web.search.Search;
import com.web.web.SearchResource;
import com.web.web.SearchResponse;
import org.junit.Test;

/**
 * Created by Mitchell on 13/08/2017.
 */
public class search {

    @Test
    public void mainSearch(){
        SearchResource sr = new SearchResource("","");
        sr.search("test","london");
    }

    @Test
    public void crawlCron(){
        Search s = new Search();
        s.crawlListCron();
    }

    @Test
    public void cronTest(){
        JobApplication ja = new JobApplication();
        ja.setupCronJobs();
    }

}
