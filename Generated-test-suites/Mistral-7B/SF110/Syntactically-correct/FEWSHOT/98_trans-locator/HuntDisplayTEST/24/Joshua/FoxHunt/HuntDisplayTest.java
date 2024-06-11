package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstruction() {
        HuntDisplay hd = new HuntDisplay();
        assertNotNull(hd);
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension size = hd.getPreferredSize();
        assertEquals(200, size.getWidth());
        assertEquals(200, size.getHeight());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension size = hd.getMinimumSize();
        assertEquals(50, size.getWidth());
        assertEquals(50, size.getHeight());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint tp = new TrigPoint(1.0, 2.0, 3.0, true);
        hd.addPoint(tp);
        TrigPoint[] points = hd.getPoints().toArray(new TrigPoint[0]);
        assertEquals(1, points.length);
        assertEquals(1.0, points[0].getX(), 0.01);
        assertEquals(2.0, points[0].getY(), 0.01);
        assertEquals(3.0, points[0].getTheta(), 0.01);
        assertTrue(points[0].isFox());
        hd.repaint();
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay hd = new HuntDisplay();
        Landmark lm = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.png")));
        hd.addLandmark(lm);
        Landmark[] landmarks = hd.getLandmarks().toArray(new Landmark[0]);
        assertEquals(1, landmarks.length);
        assertEquals(1.0, landmarks[0].getLon(), 0.01);
        assertEquals(1.0, landmarks[0].getLat(), 0.01);
        assertNotNull(landmarks[0].getIcon());
        hd.repaint();
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint tp1 = new TrigPoint(1.0, 2.0, 3.0, true);
        TrigPoint tp2 = new TrigPoint(3.0, 4.0, 5.0, false);
        Landmark lm = new Landmark(5.0, 6.0, new ImageIcon(getClass().getResource("/icon.png")));
        hd.addPoint(tp1);
        hd.addPoint(tp2);
        hd.addLandmark(lm);
        hd.setSize(400, 400);
        Graphics2D g = (Graphics2D) hd.getGraphics();
        hd.paintComponent(g);
        g.dispose();
        // Add additional asserts for checking the drawing of points and landmarks
    }

    @Test
    public void testCentering() {
        HuntDisplay hd = new HuntDisplay();
        hd.addPoint(new TrigPoint(0.5, 0.5, 0, false));
        hd.setSize(800, 800);
        double xAdd = 0.5 - 0.5;
        double yAdd = 0.5 - 0.5;
        assertEquals(400, hd.getWidth());
        assertEquals(400, hd.getHeight());
        TrigPoint[] points = hd.getPoints().toArray(new TrigPoint[0]);
        int x, y;
        for (TrigPoint point : points) {
            x = (int)((point.getX() + xAdd) * hd.getWidth());
            y = (int)((point.getY() + yAdd) * hd.getHeight());
            if (point.isFox()) {
                assertTrue(x >= 0 && x < hd.getWidth() && y >= 0 && y < hd.getHeight());
            } else {
                assertTrue(x >= 0 && x < hd.getWidth() && y >= 0 && y < hd.getHeight());
            }
        }
    }
}