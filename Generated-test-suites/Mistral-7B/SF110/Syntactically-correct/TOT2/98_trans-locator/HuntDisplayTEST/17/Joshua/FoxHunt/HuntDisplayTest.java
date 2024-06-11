import org.junit.jupiter.api.*;
import Joshua.FoxHunt.*; // Assuming the package is Joshua.FoxHunt

public class HuntDisplayTest {
    private HuntDisplay display;

    @BeforeEach
    public void setup() {
        display = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
        display.addPoint(point);
        assertFalse(display.points.isEmpty());
        assertEquals(1, display.points.size());
    }
}