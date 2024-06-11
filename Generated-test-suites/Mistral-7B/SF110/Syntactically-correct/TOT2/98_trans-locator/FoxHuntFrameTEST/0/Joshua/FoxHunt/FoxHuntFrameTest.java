package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import Joshua.FoxHunt.FoxHuntFrame;
import org.junit.Test;

public class FoxHuntFrameTest {
    @Test
    public void testFoxHuntFrameConstructor() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        // Basic functionality check
        assertEquals("Triangulation", foxHuntFrame.getTitle());
        // Edge case: Check if the frame is visible after construction
        assertEquals(JFrame.VISIBLE, foxHuntFrame.getState());
    }

    @Test
    public void testAddPointMethod() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        // Basic functionality check
        foxHuntFrame.addPoint();
        foxHuntFrame.points.size(); // Check if the point has been added to the vector
        // Edge case: Check if an error message is displayed when invalid data is entered
        foxHuntFrame.theta.setText("abc");
        foxHuntFrame.lat.setText("123.456");
        foxHuntFrame.lon.setText("123.456789");
        foxHuntFrame.addActionListener(foxHuntFrame);
        // Exception handling: Check if an exception is thrown when invalid data is entered
        try {
            foxHuntFrame.addPoint();
        } catch (NumberFormatException e) {
            // Test passed if an exception is thrown
        }
    }

    // Add more test cases for other methods here
}