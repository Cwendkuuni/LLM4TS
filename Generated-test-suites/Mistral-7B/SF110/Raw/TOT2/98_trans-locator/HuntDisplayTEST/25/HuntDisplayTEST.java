import org.junit.Test;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(trigPoint);
        // Add assertions here to check the functionality of the addPoint method
    }
}