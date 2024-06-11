package Joshua.FoxHunt;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {

    FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testConstructor() {
        // Check if the frame is initialized correctly
        assertEquals("Triangulation", frame.getTitle());
        assertNotNull(frame.getContentPane());
        assertNotNull(frame.getIconImage());
    }

    @Test
    public void testAddPoint() {
        // Test adding a valid point
        frame.theta.setText("45");
        frame.lat.setText("41.375000");
        frame.lon.setText("-87.420000");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(2, frame.points.size());
        TrigPoint firstPoint = (TrigPoint) frame.points.get(0);
        TrigPoint secondPoint = (TrigPoint) frame.points.get(1);
        assertEquals(45.0, firstPoint.getAngle(), 0.01);
        assertEquals(41.375000, secondPoint.getLatitude(), 0.001);
        assertEquals(-87.420000, secondPoint.getLongitude(), 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testAddPointInvalidLat() {
        // Test adding an invalid latitude point
        frame.theta.setText("45");
        frame.lat.setText("41.37");
        frame.lon.setText("87.420000");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
    }

    @Test(expected = NumberFormatException.class)
    public void testAddPointInvalidLon() {
        // Test adding an invalid longitude point
        frame.theta.setText("45");
        frame.lat.setText("41.375000");
        frame.lon.setText("dd123.420000");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
    }

    @Test(expected = NumberFormatException.class)
    public void testAddPointInvalidTheta() {
        // Test adding an invalid theta point
        frame.theta.setText("abc");
        frame.lat.setText("41.375000");
        frame.lon.setText("87.420000");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
    }

    @Test
    public void testDisplayPoints() {
        // Test displaying points after adding them
        frame.theta.setText("45");
        frame.lat.setText("41.375000");
        frame.lon.setText("87.420000");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, ""));
        assertEquals(2, frame.pos.getRowCount());
        assertEquals("Lattitude", frame.pos.getColumnName(0));
        assertEquals("Longitude", frame.pos.getColumnName(1));
        assertEquals("41.375000", frame.pos.getValueAt(0, 0).toString());
        assertEquals("87.420000", frame.pos.getValueAt(0, 1).toString());
        assertEquals("41.375000", frame.pos.getValueAt(1, 0).toString());
        assertEquals("-87.420000", frame.pos.getValueAt(1, 1).toString());
    }
}