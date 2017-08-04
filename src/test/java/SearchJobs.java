import com.web.search.Search;
import org.junit.Test;

/**
 * Created by Mitchell on 29/07/2017.
 */
public class SearchJobs {

    @Test
    public void searchJobs(){

        Search s = new Search();
       // String test = s.searchJobs("Junior Android", "London");
    }

    @Test
    public void cronSearch(){
        Search s = new Search();
        s.cronCrawl();
    }
}
