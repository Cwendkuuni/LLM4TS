package Joshua.FoxHunt;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        display.addPoint(trigPoint);
        int expectedSize = 1;
        int actualSize = display.points.size();
        assertEquals(expectedSize, actualSize);
    }
}