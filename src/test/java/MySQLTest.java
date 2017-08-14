import com.web.database.MySQL.MySQL;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

/**
 * Created by Mitchell on 28/07/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class MySQLTest {

   private ArrayList testArgs;

    public MySQLTest(){
        testArgs = new ArrayList<String>();
    }

    @Test
    public void aCheckKeywordEmpty(){
        testArgs.clear();
        testArgs.add("test keyword");
        MySQL mySQL = MySQL.instance("job_search");
        String query = "SELECT COUNT(*) FROM keywords WHERE keyword = ? LIMIT 1";
        int count = mySQL.rowCount(query, testArgs);
        Assert.assertEquals("Should not exist", 0, count);
    }

    @Test
    public void bInsertKeyword(){
        testArgs.clear();
        testArgs.add("test keyword");
        MySQL mySQL = MySQL.instance("job_search");
        String query = "INSERT INTO keywords (keyword) VALUE (?)";
        int res = mySQL.update(query, testArgs, false);
        Assert.assertEquals("Didn't insert", 1, res);
    }

    @Test
    public void checkKeywordExists(){
        testArgs.clear();
        testArgs.add("test keyword");
        MySQL mySQL = MySQL.instance("job_search");
        String query = "SELECT COUNT(*) FROM keywords WHERE keyword = ? LIMIT 1";
        int count = mySQL.rowCount(query, testArgs);
        Assert.assertEquals("Should exist", 1, count);
    }

    @Test
    public void deleteKeyword(){
        testArgs.clear();
        testArgs.add("test keyword");
        MySQL mySQL = MySQL.instance("job_search");
        String query = "DELETE FROM keywords WHERE keyword = ?";
        int count = mySQL.update(query, testArgs, false);
        Assert.assertEquals("Should of deleted", 1, count);
    }

    @Test
    public void eAddSearch(){
        testArgs.clear();
        testArgs.add(999);
        MySQL mySQL = MySQL.instance("job_search");
        String query = "INSERT INTO search_crawl (search_id) VALUE (?)";
        int count = mySQL.update(query, testArgs, false);
        Assert.assertEquals("Should of inserted", 1, count);
    }

    @Test
    public void fUpdateSearch(){
        testArgs.clear();
        testArgs.add('1');
        MySQL mySQL = MySQL.instance("job_search");
        String query = "UPDATE search_crawl SET searched = ?";
        int count = mySQL.update(query, testArgs, false);
        Assert.assertEquals("Should of inserted", 1, count);
    }


    @Test
    public void gDeleteSearch(){
        testArgs.clear();
        testArgs.add(999);
        MySQL mySQL = MySQL.instance("job_search");
        String query = "DELETE FROM search_crawl WHERE search_id = ?";
        int count = mySQL.update(query, testArgs, false);
        Assert.assertEquals("Should of deleted", 1, count);
    }

}
