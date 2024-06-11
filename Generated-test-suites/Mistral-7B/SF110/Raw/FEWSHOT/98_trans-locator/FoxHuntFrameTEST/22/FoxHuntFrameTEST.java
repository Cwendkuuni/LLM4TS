package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxHuntFrame;
import java.util.*;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrameConstructor() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        assertNotNull(fhf.theta);
        assertNotNull(fhf.lat);
        assertNotNull(fhf.lon);
        assertNotNull(fhf.zoom);
        assertNotNull(fhf.add);
        assertNotNull(fhf.pos);
        assertNotNull(fhf.posModel);
        assertNotNull(fhf.display);
        assertNotNull(fhf.panel1);
        assertNotNull(fhf.panel2);
        assertNotNull(fhf.panel3);
        assertNotNull(fhf.panel);
        assertNotNull(fhf.points);
        assertNotNull(fhf.fox);
    }

    @Test
    public void testAddPointMethod() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("123");
        fhf.lat.setText("51.123456");
        fhf.lon.setText("0.123456");
        fhf.actionPerformed(new ActionEvent(fhf.add, 0, "Add It!!!"));
        assertEquals(1, fhf.points.size());
        assertEquals(1, fhf.fox.size());
        TrigPoint point = (TrigPoint) fhf.points.elementAt(0);
        assertEquals(123, point.getAngle(), 0.01);
        assertEquals(51.123456, point.getLatitude(), 0.00001);
        assertEquals(0.123456, point.getLongitude(), 0.00001);
    }

    @Test
    public void testAddPointMethodWithTwoPoints() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("123");
        fhf.lat.setText("51.123456");
        fhf.lon.setText("0.123456");
        fhf.actionPerformed(new ActionEvent(fhf.add, 0, "Add It!!!"));
        fhf.theta.setText("45");
        fhf.lat.setText("53.123456");
        fhf.lon.setText("1.123456");
        fhf.actionPerformed(new ActionEvent(fhf.add, 0, "Add It!!!"));
        assertEquals(2, fhf.points.size());
        assertEquals(2, fhf.fox.size());
        TrigPoint firstPoint = (TrigPoint) fhf.points.elementAt(0);
        TrigPoint secondPoint = (TrigPoint) fhf.points.elementAt(1);
        TrigPoint fox = (TrigPoint) fhf.fox.elementAt(0);
        TrigPoint fox2 = (TrigPoint) fhf.fox.elementAt(1);
        assertEquals(123, firstPoint.getAngle(), 0.01);
        assertEquals(51.123456, firstPoint.getLatitude(), 0.00001);
        assertEquals(0.123456, firstPoint.getLongitude(), 0.00001);
        assertEquals(45, secondPoint.getAngle(), 0.01);
        assertEquals(53.123456, secondPoint.getLatitude(), 0.00001);
        assertEquals(1.123456, secondPoint.getLongitude(), 0.00001);
        assertEquals(52.124536, fox.getLatitude(), 0.00001);
        assertEquals(0.623456, fox.getLongitude(), 0.00001);
        assertEquals(53.123456, fox2.getLatitude(), 0.00001);
        assertEquals(1.776546, fox2.getLongitude(), 0.00001);
    }

    @Test
    public void testLoadLandmarksMethod() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.loadLandmarks();
        assertTrue(fhf.display.getLandmarks().size() > 0);
    }

    @Test(expected = IOException.class)
    public void testLoadLandmarksMethodWithFileNotFoundException() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.loadLandmarks();
        File file = new File("./landmarksNonExistent.cfg");
        fhf.loadLandmarks();
    }
}