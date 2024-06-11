package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrameConstructor() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.getContentPane());
        assertEquals("Triangulation", frame.getTitle());
        assertTrue(frame.isVisible());
    }

    @Test
    public void testAddPointMethod() {
        FoxHuntFrame frame = new FoxHuntFrame();
        TrigPoint expectedPoint = new TrigPoint(0.0, 0.0, 0);

        frame.theta.setText("45");
        frame.lat.setText("51.5074");
        frame.lon.setText("0.0781");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!"));

        TrigPoint actualPoint = (TrigPoint) frame.points.lastElement();
        assertEquals(expectedPoint.getX(), actualPoint.getX(), 0.001);
        assertEquals(expectedPoint.getY(), actualPoint.getY(), 0.001);
        assertEquals(expectedPoint.getAngle(), actualPoint.getAngle(), 0.1);

        Landmark expectedFox = new Landmark(1.0, 2.0, new ImageIcon("icon.gif"));
        TrigPoint actualFox = (TrigPoint) frame.fox.lastElement();
        Landmark actualLandmark = frame.display.getLandMarks().get(frame.display.getLandMarks().size() - 1);
        assertEquals(expectedFox.getX(), actualLandmark.getX(), 0.001);
        assertEquals(expectedFox.getY(), actualLandmark.getY(), 0.001);
        assertEquals(expectedFox.getIcon(), actualLandmark.getIcon());
    }

    @Test
    public void testLoadLandmarksMethod() {
        FoxHuntFrame frame = new FoxHuntFrame();
        Landmark[] expectedLandmarks = new Landmark[5];
        String[] expectedIconFiles = {"icon1.gif", "icon2.gif", "icon3.gif", "icon4.gif", "icon5.gif"};
        double[] expectedLat = {51.5074, 51.5128, 51.5182, 51.5236, 51.529};
        double[] expectedLon = {0.0781, 0.079, 0.08, 0.081, 0.082};

        frame.loadLandmarks();
        Landmark[] actualLandmarks = frame.display.getLandMarks().toArray(new Landmark[0]);
        assertEquals(expectedLandmarks.length, actualLandmarks.length);
        for (int i = 0; i < expectedLandmarks.length; i++) {
            assertEquals(expectedLandmarks[i].getX(), actualLandmarks[i].getX(), 0.001);
            assertEquals(expectedLandmarks[i].getY(), actualLandmarks[i].getY(), 0.001);
            assertEquals(expectedIconFiles[i], actualLandmarks[i].getIcon().toString());
            assertEquals(expectedLat[i], actualLandmarks[i].getY(), 0.001);
            assertEquals(expectedLon[i], actualLandmarks[i].getX(), 0.001);
        }
    }

    @Test
    public void testGetColumnCount() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxTableModel model = frame.posModel;
        assertEquals(2, model.getColumnCount());
    }

    @Test
    public void testGetRowCount() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxTableModel model = frame.posModel;
        assertEquals(0, model.getRowCount());

        frame.addPoint();
        assertEquals(1, model.getRowCount());

        frame.addPoint();
        assertEquals(2, model.getRowCount());
    }

    @Test
    public void testGetColumnName() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxTableModel model = frame.posModel;

        assertEquals("Lattitude", model.getColumnName(0));
        assertEquals("Longitude", model.getColumnName(1));
    }

    @Test
    public void testgetValueAt() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxTableModel model = frame.posModel;

        frame.addPoint();
        TrigPoint expectedPoint = (TrigPoint) frame.fox.lastElement();
        double expectedLat = expectedPoint.getY();
        double expectedLon = expectedPoint.getX();

        Object latValue = model.getValueAt(0, 0);
        DecimalFormat formatter = new DecimalFormat("###.##############");
        double actualLat = Double.parseDouble(formatter.format(latValue));
        assertEquals(expectedLat, actualLat, 0.001);

        Object lonValue = model.getValueAt(0, 1);
        actualLat = Double.parseDouble(formatter.format(lonValue));
        assertEquals(expectedLon, actualLat, 0.001);
    }

    @Test
    public void testGetColumnClass() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxTableModel model = frame.posModel;

        assertEquals(Double.class, model.getColumnClass(0));
        assertEquals(Double.class, model.getColumnClass(1));
    }

    @Test
    public void testIsCellEditable() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxTableModel model = frame.posModel;

        assertFalse(model.isCellEditable(0, 0));
        assertFalse(model.isCellEditable(0, 1));
    }
}