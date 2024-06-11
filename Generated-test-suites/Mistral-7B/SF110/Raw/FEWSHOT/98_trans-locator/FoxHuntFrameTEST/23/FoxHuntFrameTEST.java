package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrameConstructor() {
        new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        final FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("10");
        frame.lat.setText("45.123456");
        frame.lon.setText("78.654321");
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));

        final int sizeBefore = frame.points.size();
        final int sizeAfter = frame.points.size();
        assertEquals("Size of points vector should increase by one", sizeBefore + 1, sizeAfter);

        final TrigPoint lastPoint = (TrigPoint) frame.points.lastElement();
        assertEquals("Last added point's angle should be 10", 10, lastPoint.getAngle().intValue());
        assertEquals("Last added point's latitude should be 45.123456", 45.123456, lastPoint.getY(), 0.0001);
        assertEquals("Last added point's longitude should be 78.654321", 78.654321, lastPoint.getX(), 0.0001);
    }

    @Test
    public void testLoadLandmarks() {
        final FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();

        final int numLandmarks = frame.display.getLandmarkCount();
        assertNotEquals("There should be landmarks in the display", 0, numLandmarks);
    }

    @Test
    public void testFoxTableModel() {
        final FoxHuntFrame frame = new FoxHuntFrame();

        for (int i = 0; i < 5; ++i) {
            frame.addPoint();
        }

        final FoxTableModel model = frame.posModel;

        final int columnCount = model.getColumnCount();
        final int rowCount = model.getRowCount();

        assertEquals("Number of columns should be 2", 2, columnCount);
        assertEquals("Number of rows should be equal to the number of fox points", 5, rowCount);

        for (int i = 0; i < rowCount; ++i) {
            final Object valueAt0 = model.getValueAt(i, 0);
            final String formattedValue = new DecimalFormat("###.##############").format(frame.fox.elementAt(i).getY());
            assertEquals("Value at (i, 0) should be the fox's y-coordinate", formattedValue, valueAt0);

            final Object valueAt1 = model.getValueAt(i, 1);
            final String formattedValue1 = new DecimalFormat("###.##############").format(frame.fox.elementAt(i).getX());
            assertEquals("Value at (i, 1) should be the fox's x-coordinate", formattedValue1, valueAt1);
        }
    }
}