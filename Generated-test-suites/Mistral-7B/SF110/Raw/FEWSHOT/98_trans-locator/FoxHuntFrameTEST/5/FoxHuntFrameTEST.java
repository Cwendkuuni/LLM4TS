package Joshua.FoxHunt.Tests;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        frame.theta.setText("30");
        frame.lat.setText("40.123456");
        frame.lon.setText("-74.567890");
        frame.actionPerformed(new ActionEvent(frame.add, 0, 0));

        Vector<TrigPoint> points = frame.points;
        Vector<TrigPoint> fox = frame.fox;
        assertEquals(1, points.size());
        assertEquals(1, fox.size());

        TrigPoint point = points.elementAt(0);
        TrigPoint foxPoint = fox.elementAt(0);
        assertEquals(30.0, point.getAngle(), 0.01);
        assertEquals(40.123456, point.getLat(), 0.0001);
        assertEquals(-74.56789, point.getLon(), 0.0001);

        assertEquals(40.123456, foxPoint.getLat(), 0.0001);
        assertEquals(-74.56789, foxPoint.getLon(), 0.0001);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidLat() {
        frame.theta.setText("30");
        frame.lat.setText("foo");
        frame.lon.setText("74.567890");
        frame.actionPerformed(new ActionEvent(frame.add, 0, 0));
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidLon() {
        frame.theta.setText("30");
        frame.lat.setText("40.123456");
        frame.lon.setText("foo");
        frame.actionPerformed(new ActionEvent(frame.add, 0, 0));
    }

    @Test
    public void testAddPointsWithTwoPoints() {
        frame.theta.setText("30");
        frame.lat.setText("40.123456");
        frame.lon.setText("74.567890");
        frame.actionPerformed(new ActionEvent(frame.add, 0, 0));

        frame.theta.setText("60");
        frame.lat.setText("50.012345");
        frame.lon.setText("-80.123456");
        frame.actionPerformed(new ActionEvent(frame.add, 0, 0));

        Vector<TrigPoint> points = frame.points;
        Vector<TrigPoint> fox = frame.fox;
        assertEquals(2, points.size());
        assertEquals(1, fox.size());

        TrigPoint firstPoint = points.elementAt(0);
        TrigPoint secondPoint = points.elementAt(1);
        TrigPoint foxPoint = fox.elementAt(0);

        assertEquals(30.0, firstPoint.getAngle(), 0.01);
        assertEquals(40.123456, firstPoint.getLat(), 0.0001);
        assertEquals(-74.56789, firstPoint.getLon(), 0.0001);

        assertEquals(50.012345, secondPoint.getLat(), 0.0001);
        assertEquals(-80.123456, secondPoint.getLon(), 0.0001);

        assertEquals(55.303873, foxPoint.getLat(), 0.0001);
        assertEquals(-79.952495, foxPoint.getLon(), 0.0001);
    }
}