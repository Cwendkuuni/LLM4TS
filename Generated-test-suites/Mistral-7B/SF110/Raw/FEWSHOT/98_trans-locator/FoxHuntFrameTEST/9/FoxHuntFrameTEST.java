package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrameConstruction() {
        new FoxHuntFrame();
    }

    @Test
    public void testAddPointWithValidInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("120");
        frame.lat.setText("51.5074");
        frame.lon.setText("0.123456");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
        TrigPoint point = (TrigPoint) frame.points.lastElement();
        TrigPoint fox = (TrigPoint) frame.fox.lastElement();
        assertEquals(120, point.getAngle());
        assertEquals(51.5074, point.getY(), 0.001);
        assertEquals(0.123456, point.getX(), 0.0001);
        assertEquals(51.5143, fox.getY(), 0.001);
        assertEquals(-0.0023, fox.getX(), 0.0001);
    }

    @Test(expected = NumberFormatException.class)
    public void testAddPointWithInvalidLatInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("120");
        frame.lat.setText("foo");
        frame.lon.setText("0.123456");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
    }

    @Test(expected = NumberFormatException.class)
    public void testAddPointWithInvalidLonInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("120");
        frame.lat.setText("51.5074");
        frame.lon.setText("foo");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
    }

    @Test
    public void testAddPointWithInvalidThetaInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("foo");
        frame.lat.setText("51.5074");
        frame.lon.setText("0.123456");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionPerformed"));
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertEquals(3, frame.display.getLandMarkCount());
        Landmark landmark1 = frame.display.getLandMark(0);
        Landmark landmark2 = frame.display.getLandMark(1);
        Landmark landmark3 = frame.display.getLandMark(2);
        String iconFile1 = landmark1.getIconFile();
        String iconFile2 = landmark2.getIconFile();
        String iconFile3 = landmark3.getIconFile();
        double lat1 = landmark1.getLat();
        double lat2 = landmark2.getLat();
        double lat3 = landmark3.getLat();
        double lon1 = landmark1.getLon();
        double lon2 = landmark2.getLon();
        double lon3 = landmark3.getLon();
        assertEquals("icon1.png", iconFile1);
        assertEquals("icon2.png", iconFile2);
        assertEquals("icon3.png", iconFile3);
        assertEquals(55.123456, lat1, 0.001);
        assertEquals(10.456789, lat2, 0.001);
        assertEquals(15.789012, lat3, 0.001);
        assertEquals(-0.123456, lon1, 0.0001);
        assertEquals(45.678901, lon2, 0.0001);
        assertEquals(78.901123, lon3, 0.0001);
    }
}