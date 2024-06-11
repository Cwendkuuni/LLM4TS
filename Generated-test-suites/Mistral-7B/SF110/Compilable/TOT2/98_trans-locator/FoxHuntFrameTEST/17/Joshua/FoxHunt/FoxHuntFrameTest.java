package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    // Test start
    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testConstructor() {
        assertNotNull(frame);
        assertEquals("Triangulation", frame.getTitle());
    }

    @Test
    public void testActionPerformed() {
        frame.theta.setText("45");
        frame.lat.setText("40.7128");
        frame.lon.setText("-74.0060");
        frame.add.doClick();

        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());
    }

    // Test end
}