package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.util.*;

import Joshua.FoxHunt.*;

public class FoxHuntFrameTest {
    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        // Set text fields with valid values
        frame.theta.setText("45");
        frame.lat.setText("40.7128");
        frame.lon.setText("-74.0060");

        // Simulate button click
        ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
        frame.actionPerformed(event);

        // Check if point was added to the points vector
        assertEquals(1, frame.points.size());

        // Check if point was added to the display
        assertEquals(1, frame.display.getPoints().size());

        // Check if fox was calculated and added to the fox vector
        assertEquals(1, frame.fox.size());

        // Check if table was updated
        assertEquals(1, frame.posModel.getRowCount());
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidTheta() {
        // Set text fields with invalid values
        frame.theta.setText("abc");
        frame.lat.setText("40.7128");
        frame.lon.setText("-74.0060");

        // Simulate button click
        ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
        frame.actionPerformed(event);

        // Verify that an exception is thrown
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidLat() {
        // Set text fields with invalid values
        frame.theta.setText("45");
        frame.lat.setText("ddd.mm.mmm");
        frame.lon.setText("-74.0060");

        // Simulate button click
        ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
        frame.actionPerformed(event);

        // Verify that an exception is thrown
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidLon() {
        // Set text fields with invalid values
        frame.theta.setText("45");
        frame.lat.setText("40.7128");
        frame.lon.setText("ddd*mm.mmm");

        // Simulate button click
        ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!");
        frame.actionPerformed(event);

        // Verify that an exception is thrown
    }

    @Test
    public void testLoadLandmarks() {
        // Stub the FileInputStream and BufferedReader to return predefined data
        final ByteArrayInputStream inputStream = new ByteArrayInputStream("NUM=5\nFILE icon1.jpg\nLAT 40.7128\nLON -74.0060\nLAT 42.3678\nLON -73.9822\nLAT 43.1234\nLON -72.1212\nLON FILE icon2.jpg".getBytes());
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            frame.loadLandmarks();
        } finally {
            reader.close();
            inputStream.close();
        }

        // Check if landmarks were added to the display
        assertEquals(2, frame.display.getLandmarks().size());
    }
}