package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrame() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.posModel);
        assertNotNull(frame.display);
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("10");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.1278");
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        TrigPoint point = (TrigPoint) frame.points.elementAt(0);
        TrigPoint fox = (TrigPoint) frame.fox.elementAt(0);
        assertEquals(51.5074, point.getY(), 0.001);
        assertEquals(-0.1278, point.getX(), 0.001);
        assertEquals(10, point.getAngle());
        assertEquals(50.5074, fox.getY(), 0.001);
        assertEquals(-0.2500, fox.getX(), 0.001);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        try {
            frame.loadLandmarks();
            assertEquals(5, frame.display.getLandMarkCount());
        } catch (Exception e) {
            fail("Failed to load landmarks");
        }
    }

    @Test
    public void testFoxTableModel() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxTableModel model = frame.posModel;
        assertEquals(2, model.getColumnCount());
        assertEquals(0, model.getRowCount());
        for (int i = 0; i < frame.fox.size(); i++) {
            TrigPoint fox = (TrigPoint) frame.fox.elementAt(i);
            Object[] row = model.getDataVector().elementAt(i);
            assertEquals(fox.getY(), row[0]);
            assertEquals(fox.getX(), row[1]);
        }
    }

    @Test
    public void testErrorDialog() {
        FoxHuntFrame frame = new FoxHuntFrame() {
            @Override
            void error(String text) {
                fail("Error dialog should not be shown");
            }
        };
        frame.error("Test error");
        fail("Error dialog was shown unexpectedly");
    }
}