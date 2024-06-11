package Joshua.FoxHunt;
import org.junit.Test;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(0.1, 0.2, 0.3, false);
        display.addPoint(point);
        // Assertions for checking the functionality of addPoint method
    }
}