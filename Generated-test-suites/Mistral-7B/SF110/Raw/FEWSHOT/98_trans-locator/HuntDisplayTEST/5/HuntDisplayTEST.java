package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Vector;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

    @Test
    public void testConstructor() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(Color.WHITE, huntDisplay.getBackground());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
        assertNull(huntDisplay.points);
        assertNull(huntDisplay.landmarks);
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 1.0, Math.PI / 2, true);
        huntDisplay.addPoint(trigPoint);
        Vector<TrigPoint> points = huntDisplay.points;
        assertEquals(1, points.size());
        TrigPoint expectedPoint = new TrigPoint(1.0 / 90.0, 1.0 / 180.0, Math.PI / 2, true);
        assertEquals(expectedPoint, points.elementAt(0));
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 1.0, new ImageIcon(getClass().getResource("/icon.png")));
        huntDisplay.addLandMark(landmark);
        Vector<Landmark> landmarks = huntDisplay.landmarks;
        assertEquals(1, landmarks.size());
        Landmark expectedLandmark = new Landmark(1.0 / 90.0, 1.0 / 180.0, new ImageIcon(getClass().getResource("/icon.png")));
        assertEquals(expectedLandmark, landmarks.elementAt(0));
    }

    @Test
    public void testPaintComponentWithNoPointsAndNoLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Graphics g = null;
        huntDisplay.paintComponent(g);
    }

    @Test
    public void testPaintComponentWithNoPointsButWithLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 1.0, new ImageIcon(getClass().getResource("/icon.png")));
        huntDisplay.addLandMark(landmark);
        Graphics g = null;
        huntDisplay.paintComponent(g);
    }

    @Test
    public void testPaintComponentWithPointsAndNoLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint foxPoint = new TrigPoint(1.0 / 90.0, 1.0 / 180.0, Math.PI / 2, true);
        TrigPoint nonFoxPoint = new TrigPoint(2.0 / 90.0, 2.0 / 180.0, Math.PI / 4, false);
        huntDisplay.addPoint(foxPoint);
        huntDisplay.addPoint(nonFoxPoint);
        Graphics g = null;
        huntDisplay.paintComponent(g);
    }

    @Test
    public void testPaintComponentWithPointsAndLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint foxPoint = new TrigPoint(1.0 / 90.0, 1.0 / 180.0, Math.PI / 2, true);
        TrigPoint nonFoxPoint = new TrigPoint(2.0 / 90.0, 2.0 / 180.0, Math.PI / 4, false);
        Landmark landmark = new Landmark(1.0 / 90.0, 1.0 / 180.0, new ImageIcon(getClass().getResource("/icon.png")));
        huntDisplay.addPoint(foxPoint);
        huntDisplay.addPoint(nonFoxPoint);
        huntDisplay.addLandMark(landmark);
        Graphics g = null;
        huntDisplay.paintComponent(g);
    }

    @Test
    public void testPaintComponentWithPointsAndLandmarksInDifferentPositions() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint foxPoint1 = new TrigPoint(1.0 / 90.0, 1.0 / 180.0, Math.PI / 2, true);
        TrigPoint foxPoint2 = new TrigPoint(3.0 / 90.0, 3.0 / 180.0, Math.PI / 2, true);
        TrigPoint nonFoxPoint = new TrigPoint(2.0 / 90.0, 2.0 / 180.0, Math.PI / 4, false);
        Landmark landmark1 = new Landmark(1.5 / 90.0, 1.5 / 180.0, new ImageIcon(getClass().getResource("/icon.png")));
        Landmark landmark2 = new Landmark(4.5 / 90.0, 4.5 / 180.0, new ImageIcon(getClass().getResource("/icon.png")));
        huntDisplay.addPoint(foxPoint1);
        huntDisplay.addPoint(foxPoint2);
        huntDisplay.addPoint(nonFoxPoint);
        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);
        Graphics g = null;
        huntDisplay.paintComponent(g);
    }
}