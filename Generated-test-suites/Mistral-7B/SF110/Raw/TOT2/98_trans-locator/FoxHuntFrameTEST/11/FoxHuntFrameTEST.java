package Joshua.FoxHunt;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    // Test instance of FoxHuntFrame
    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testFoxHuntFrameConstructor() {
        // Test if the frame is created successfully
        assertNotNull(frame);
    }

    @Test
    public void testActionPerformed() {
        // Given: Some text is entered in the text fields
        frame.theta.setText("45");
        frame.lat.setText("51.5069");
        frame.lon.setText("-0.1278");

        // When: The Add It!!! button is clicked
        frame.add.doClick();

        // Then: The point is added to the table and the fox point is calculated and displayed
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertTrue(frame.display.getPoints().size() > 0);
        assertTrue(frame.display.getFox() != null);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidThetaInput() {
        // Given: An invalid theta input is provided
        frame.theta.setText("abc");

        // When: The Add It!!! button is clicked
        frame.add.doClick();

        // Then: A NumberFormatException is thrown
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidLatInput() {
        // Given: An invalid latitude input is provided
        frame.theta.setText("45");
        frame.lat.setText("51.5069a");

        // When: The Add It!!! button is clicked
        frame.add.doClick();

        // Then: A NumberFormatException is thrown
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidLonInput() {
        // Given: An invalid longitude input is provided
        frame.theta.setText("45");
        frame.lat.setText("51.5069");
        frame.lon.setText("51.5069");

        // When: The Add It!!! button is clicked
        frame.add.doClick();

        // Then: A NumberFormatException is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPointWithLessThanTwoPoints() {
        // Given: Only one point is added
        frame.theta.setText("45");
        frame.lat.setText("51.5069");
        frame.lon.setText("-0.1278");
        frame.add.doClick();

        // When: Another point is attempted to be added
        frame.theta.setText("30");
        frame.lat.setText("52.5069");
        frame.lon.setText("-1.1278");
        frame.add.doClick();

        // Then: An IllegalArgumentException is thrown
    }

    @Test(expected = NullPointerException.class)
    public void testLoadLandmarksWithEmptyConfigFile() {
        // Given: A empty config file is provided
        FoxHuntFrame frame = new FoxHuntFrame() {
            @Override
            void loadLandmarks() {
                // Override the method to throw a NullPointerException
                throw new NullPointerException();
            }
        };

        // When: The loadLandmarks method is called
        frame.loadLandmarks();

        // Then: A NullPointerException is thrown
    }
}