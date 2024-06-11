import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, false);
        hd.addPoint(point);
        // Check the size of the points vector
        assertEquals(1, hd.points.size());
    }
}