package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();

        // Test valid input
        frame.theta.setText("30");
        frame.lat.setText("40.123456");
        frame.lon.setText("-73.567890");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());

        // Test invalid input
        frame.theta.setText("");
        frame.lat.setText("40.123456");
        frame.lon.setText("73.567890");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.fox.size());

        // Test valid input with existing points
        frame.theta.setText("60");
        frame.lat.setText("35.234567");
        frame.lon.setText("-80.987654");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
        TrigPoint expectedFox = new TrigPoint(35.234567, -80.987654, 60);
        TrigPoint actualFox = (TrigPoint) frame.fox.elementAt(0);
        assertEquals(expectedFox.getX(), actualFox.getX(), 0.00001);
        assertEquals(expectedFox.getY(), actualFox.getY(), 0.00001);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        // This is a simplified test, assuming there is at least one landmark in the file
        assertTrue(frame.display.getLandmarks().size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Test Error");
    }

}