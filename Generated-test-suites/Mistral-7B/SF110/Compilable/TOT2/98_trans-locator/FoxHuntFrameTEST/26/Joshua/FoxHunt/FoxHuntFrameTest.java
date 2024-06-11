package Joshua.FoxHunt;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testFoxHuntFrameConstructor() {
        // Basic test for constructor
        assertNotNull(frame);
        assertEquals("Triangulation", frame.getTitle());
    }

    @Test
    public void testActionPerformed() {
        // Basic test for addPoint method
        frame.theta.setText("10");
        frame.lat.setText("40.7128");
        frame.lon.setText("-74.0060");
        frame.add.doClick();

        // Edge case test for invalid latitude and longitude
        frame.theta.setText("180");
        frame.lat.setText("90.0");
        frame.lon.setText("180.0");
        frame.add.doClick();
        assertEquals(5, frame.points.size()); // The list should still have 5 elements

        // Exception handling test for invalid angle
        frame.theta.setText("-180");
        frame.lat.setText("40.7128");
        frame.lon.setText("-74.0060");
        try {
            frame.add.doClick();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add other test methods for each method as needed
}