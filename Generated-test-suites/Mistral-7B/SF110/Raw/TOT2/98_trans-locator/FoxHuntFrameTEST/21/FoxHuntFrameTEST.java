package Joshua.FoxHunt.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Joshua.FoxHunt.*;
import java.io.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setup() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testFoxHuntFrameConstructor() {
        assertNotNull(foxHuntFrame);
    }

    @Test
    public void testAddPoint() {
        foxHuntFrame.theta.setText("25");
        foxHuntFrame.lat.setText("51.5074");
        foxHuntFrame.lon.setText("0.1278");
        foxHuntFrame.add.doClick();

        assertEquals(3, foxHuntFrame.points.size());
        assertEquals(FoxHuntFrame.FoxCalc.calcFox(foxHuntFrame.points.elementAt(2), foxHuntFrame.points.elementAt(1)), foxHuntFrame.fox.elementAt(1));
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidInputInAddPoint() {
        foxHuntFrame.theta.setText("abc");
        foxHuntFrame.lat.setText("51.5074");
        foxHuntFrame.lon.setText("0.1278");
        foxHuntFrame.add.doClick();

        // The test should fail with NumberFormatException, as the input is not numeric
    }

    @Test
    public void testFrameIsVisible() {
        assertTrue(foxHuntFrame.isVisible());
    }

    @Test(expected = FileNotFoundException.class)
    public void testErrorWhileReadingFile() {
        try {
            foxHuntFrame.loadLandmarks();
            fail("An exception should have been thrown when the file was not found.");
        } catch (FileNotFoundException e) {
            // The test should pass because an exception was thrown
        }
    }

    @Test(expected = IOException.class)
    public void testErrorCommunicatingWithFile() {
        try {
            foxHuntFrame.loadLandmarks();
            fail("An exception should have been thrown when there was an error communicating with the file.");
        } catch (IOException e) {
            // The test should pass because an exception was thrown
        }
    }
}