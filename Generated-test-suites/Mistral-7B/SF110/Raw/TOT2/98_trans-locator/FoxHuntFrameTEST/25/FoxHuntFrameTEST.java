// Test cases for the FoxHuntFrame class

package Joshua.FoxHunt.Test;

import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("45.123456");
        foxHuntFrame.lon.setText("-123.456789");
        foxHuntFrame.actionPerformed(new ActionEvent(foxHuntFrame.add, ActionEvent.ACTION_PERFORMED, ""));
        TrigPoint expectedPoint = new TrigPoint(45.123456, -123.456789, 45);
        TrigPoint actualPoint = (TrigPoint) foxHuntFrame.points.lastElement();
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void testAddPointWithEmptyVectors() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.actionPerformed(new ActionEvent(foxHuntFrame.add, ActionEvent.ACTION_PERFORMED, ""));
        assertTrue(foxHuntFrame.points.isEmpty());
        assertTrue(foxHuntFrame.fox.isEmpty());
    }

    @Test(expected = NumberFormatException.class)
    public void testAddPointWithInvalidInput() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("Invalid");
        foxHuntFrame.lat.setText("Invalid");
        foxHuntFrame.lon.setText("Invalid");
        foxHuntFrame.actionPerformed(new ActionEvent(foxHuntFrame.add, ActionEvent.ACTION_PERFORMED, ""));
    }

    @Test(expected = NumberFormatException.class)
    public void testAddPointWithInvalidAngle() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("361");
        foxHuntFrame.lat.setText("45.123456");
        foxHuntFrame.lon.setText("-123.456789");
        foxHuntFrame.actionPerformed(new ActionEvent(foxHuntFrame.add, ActionEvent.ACTION_PERFORMED, ""));
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadLandmarksWithFileNotFoundException() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.loadLandmarks();
    }

    @Test(expected = IOException.class)
    public void testLoadLandmarksWithIOException() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        try {
            File file = new File("./landmarks.cfg");
            FileInputStream inFile = new FileInputStream(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(inFile));
            // Simulate an IOException
            in.readLine();
            in.close();
            inFile.close();
        } catch (FileNotFoundException e) {
            // Should not reach here
            fail("Unexpected FileNotFoundException");
        }
        foxHuntFrame.loadLandmarks();
    }
}