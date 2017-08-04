import com.web.scraper.Robot.Robot;
import org.junit.Assert;
import org.junit.Test;


public class Robots {

    @Test
    public void robotFoundCheckTrue(){
        String url = "https://java.com";
        Robot robot = new Robot(url);
        boolean check = robot.checkRobotExists();
        Assert.assertEquals("Robots.txt was not found", true, check);
    }

    @Test
    public void robotFoundCheckFalse(){
        String url = "https://java.com/test";
        Robot robot = new Robot(url);
        boolean check = robot.checkRobotExists();
        Assert.assertEquals("Robots.txt was found", false, check);
    }

    @Test
    public void getRobotText(){
        String url = "https://google.com";
        Robot robot = new Robot(url);
        robot.getRobotFile();
        boolean size = robot.getAllowedURLs().size() > 0;
        Assert.assertEquals("Size is 0...", true, size);
    }


}
