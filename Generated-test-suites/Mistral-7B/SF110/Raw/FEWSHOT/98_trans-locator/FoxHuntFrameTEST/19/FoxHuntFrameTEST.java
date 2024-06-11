package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import Joshua.FoxHunt.*;

public class FoxHuntFrameTest {
    @Test
    public void testFoxHuntFrameConstructor() {
        new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("45");
        fhf.lat.setText("40.123456");
        fhf.lon.setText("74.234567");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, ""));
        assertEquals(1, fhf.points.size());
        assertEquals(1, fhf.fox.size());
    }

    @Test
    public void testAddTwoPoints() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("45");
        fhf.lat.setText("40.123456");
        fhf.lon.setText("74.234567");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, ""));
        fhf.theta.setText("60");
        fhf.lat.setText("41.567890");
        fhf.lon.setText("75.456789");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, ""));
        assertEquals(2, fhf.points.size());
        assertEquals(1, fhf.fox.size());
    }

    @Test
    public void testAddThreePoints() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("45");
        fhf.lat.setText("40.123456");
        fhf.lon.setText("74.234567");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, ""));
        fhf.theta.setText("60");
        fhf.lat.setText("41.567890");
        fhf.lon.setText("75.456789");
        fhf.theta.setText("30");
        fhf.lat.setText("42.890123");
        fhf.lon.setText("73.567890");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, ""));
        assertEquals(3, fhf.points.size());
        assertEquals(1, fhf.fox.size());
    }

    @Test
    public void testAddPointsWithTwoSolutions() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("45");
        fhf.lat.setText("40.123456");
        fhf.lon.setText("74.234567");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, ""));
        fhf.theta.setText("60");
        fhf.lat.setText("41.567890");
        fhf.lon.setText("75.456789");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, ""));
        fhf.theta.setText("30");
        fhf.lat.setText("42.890123");
        fhf.lon.setText("73.567890");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, ""));
        fhf.theta.setText("135");
        fhf.lat.setText("38.456789");
        fhf.lon.setText("79.123456");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, ""));
        assertEquals(4, fhf.points.size());
        assertEquals(2, fhf.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.loadLandmarks();
        assertTrue(ffh.display.getLandmarks().size() > 0);
    }
}