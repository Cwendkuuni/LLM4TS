import org.junit.jupiter.api.*;
import Joshua.FoxHunt.*;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(2, huntDisplay.points.size());
    }
}