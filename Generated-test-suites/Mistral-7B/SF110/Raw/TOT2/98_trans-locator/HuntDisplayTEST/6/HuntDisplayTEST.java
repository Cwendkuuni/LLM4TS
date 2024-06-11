import org.junit.Test;
import Joshua.FoxHunt.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 3.0, false);
        huntDisplay.addPoint(trigPoint);
        int pointCount = huntDisplay.points.size();
        assertTrue(pointCount > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addPoint(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint emptyPoint = new TrigPoint();
        huntDisplay.addPoint(emptyPoint);
    }

    @Test
    public void testAddDuplicatePoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 3.0, false);
        huntDisplay.addPoint(trigPoint);
        huntDisplay.addPoint(trigPoint);
        int pointCount = huntDisplay.points.size();
        assertEquals(pointCount, 1);
    }
}