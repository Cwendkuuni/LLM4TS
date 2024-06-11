package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrameConstruction() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.getContentPane());
        assertEquals("Triangulation", frame.getTitle());
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("51.5074");
        frame.lon.setText("0.1278");
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));

        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());

        TrigPoint point = (TrigPoint) frame.points.elementAt(0);
        assertEquals(45, point.getAngle());
        assertEquals(51.5074, point.getLatitude(), 0.001);
        assertEquals(0.1278, point.getLongitude(), 0.001);

        TrigPoint fox = (TrigPoint) frame.fox.elementAt(0);
        assertNotNull(fox);
    }

    @Test
    public void testAddTwoPoints() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("51.5074");
        frame.lon.setText("0.1278");
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));

        frame.theta.setText("135");
        frame.lat.setText("52.0069");
        frame.lon.setText("1.6584");
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));

        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());

        TrigPoint point1 = (TrigPoint) frame.points.elementAt(0);
        TrigPoint point2 = (TrigPoint) frame.points.elementAt(1);

        TrigPoint fox1 = (TrigPoint) frame.fox.elementAt(0);
        TrigPoint fox2 = (TrigPoint) frame.fox.elementAt(1);

        assertEquals(45, point1.getAngle());
        assertEquals(51.5074, point1.getLatitude(), 0.001);
        assertEquals(0.1278, point1.getLongitude(), 0.001);

        assertEquals(135, point2.getAngle());
        assertEquals(52.0069, point2.getLatitude(), 0.001);
        assertEquals(1.6584, point2.getLongitude(), 0.001);

        assertEquals(51.6537, fox1.getLatitude(), 0.001);
        assertEquals(0.8862, fox1.getLongitude(), 0.001);

        assertEquals(51.6851, fox2.getLatitude(), 0.001);
        assertEquals(2.5142, fox2.getLongitude(), 0.001);
    }

    @Test
    public void testFoxTableModel() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxTableModel model = frame.posModel;

        assertEquals(0, model.getRowCount());

        frame.theta.setText("45");
        frame.lat.setText("51.5074");
        frame.lon.setText("0.1278");
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));

        assertEquals(1, model.getRowCount());

        Object value = model.getValueAt(0, 0);
        assertEquals("51.5074", value);

        value = model.getValueAt(0, 1);
        assertEquals("0.1278", value);
    }
}