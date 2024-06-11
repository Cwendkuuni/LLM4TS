package Joshua.FoxHunt; // Import necessary packages here

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
    public void testConstructor() {
        // Basic test to check if the constructor is working correctly
        assertNotNull(frame);
    }

    @Test
    public void testActionPerformed() {
        // Test adding a new point
        frame.theta.setText("10");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.1278");
        frame.add.doClick();

        // Check if the table has been updated with the new point
        assertEquals(1, frame.pos.getRowCount());

        // Test adding another point
        frame.theta.setText("20");
        frame.lat.setText("51.5174");
        frame.lon.setText("-0.1378");
        frame.add.doClick();

        // Check if the table has been updated with the second point
        assertEquals(2, frame.pos.getRowCount());
    }

    @Test
    public void testAddPointWithInvalidData() {
        // Test adding a point with invalid data (empty fields)
        frame.theta.setText("");
        frame.lat.setText("");
        frame.lon.setText("");
        frame.add.doClick();

        // Check if an error message is displayed
        assertEquals("Abort", JOptionPane.showOptionDialog(frame, "Error", "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{"Abort", "Continue"}, "Abort"));
    }

    // Add more test cases for other methods and edge cases here
}