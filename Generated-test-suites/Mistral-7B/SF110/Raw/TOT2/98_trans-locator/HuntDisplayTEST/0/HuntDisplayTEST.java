import org.junit.jupiter.api.*;
import Joshua.FoxHunt.TrigPoint;
import static org.junit.jupiter.api.Assertions.*;

class HuntDisplayTest {
    HuntDisplay huntDisplay;

    @BeforeEach
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, false);
        huntDisplay.addPoint(point);
        assertEquals(2, huntDisplay.points.size());
    }
}