package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstruction() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
        assertEquals(Color.WHITE, huntDisplay.getBackground());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(1, huntDisplay.points.size());
        huntDisplay.repaint();
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/landmark.png")));
        huntDisplay.addLandmark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
        huntDisplay.repaint();
    }

    @Test
    public void testPaintComponentWithNoPointsOrLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Graphics2D g = (Graphics2D) huntDisplay.createGraphics();
        huntDisplay.paintComponent(g);
        g.dispose();
        assertTrue(huntDisplay.isOpaque());
        assertTrue(huntDisplay.getSize().height >= 200 && huntDisplay.getSize().width >= 200);
    }

    @Test
    public void testPaintComponentWithPoints() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
        TrigPoint point3 = new TrigPoint(5.0, 6.0, 0.0, true);
        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        huntDisplay.addPoint(point3);
        Graphics2D g = (Graphics2D) huntDisplay.createGraphics();
        huntDisplay.paintComponent(g);
        g.dispose();
        int x1 = (int) (point1.getX() * huntDisplay.getWidth());
        int y1 = (int) (point1.getY() * huntDisplay.getHeight());
        int x2 = (int) (point2.getX() * huntDisplay.getWidth());
        int y2 = (int) (point2.getY() * huntDisplay.getHeight());
        int x3 = (int) (point3.getX() * huntDisplay.getWidth());
        int y3 = (int) (point3.getY() * huntDisplay.getHeight());
        assertEquals(Color.GREEN, huntDisplay.getForeground());
        assertEquals(3, huntDisplay.points.size());
        assertTrue(huntDisplay.isOpaque());
        assertTrue(huntDisplay.getSize().height >= 200 && huntDisplay.getSize().width >= 200);
        assertTrue(g.getFontMetrics().stringWidth("TrigPoint[x = ") >= huntDisplay.getWidth() / 5);
        assertTrue(g.getFontMetrics().stringWidth("] [y = ") >= huntDisplay.getWidth() / 5);
        assertTrue(g.getFontMetrics().stringWidth("] [theta = ") >= huntDisplay.getWidth() / 5);
        assertTrue(g.getFontMetrics().stringWidth("] isFox = ") >= huntDisplay.getWidth() / 5);
        assertTrue(Math.abs(x1 - 50) <= 5 && Math.abs(y1 - 50) <= 5);
        assertTrue(Math.abs(x2 - 150) <= 5 && Math.abs(y2 - 150) <= 5);
        assertTrue(Math.abs(x3 - 250) <= 5 && Math.abs(y3 - 250) <= 5);
    }

    @Test
    public void testPaintComponentWithLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/landmark1.png")));
        Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/landmark2.png")));
        huntDisplay.addLandmark(landmark1);
        huntDisplay.addLandmark(landmark2);
        Graphics2D g = (Graphics2D) huntDisplay.createGraphics();
        huntDisplay.paintComponent(g

);
g.dispose();
assertEquals(2, huntDisplay.landmarks.size());
assertTrue(huntDisplay.isOpaque());
assertTrue(huntDisplay.getSize().height >= 200 && huntDisplay.getSize().width >= 200);
assertTrue(g.getFontMetrics().stringWidth("Landmark[lat = ") >= huntDisplay.getWidth() / 5);
assertTrue(g.getFontMetrics().stringWidth("] [lon = ") >= huntDisplay.getWidth() / 5);
int x1 = (int) (landmark1.getLon() * huntDisplay.getWidth());
int y1 = (int) (landmark1.getLat() * huntDisplay.getHeight());
int x2 = (int) (landmark2.getLon() * huntDisplay.getWidth());
int y2 = (int) (landmark2.getLat() * huntDisplay.getHeight());
assertTrue(Math.abs(x1 - 50) <= 10 && Math.abs(y1 - 50) <= 10);
assertTrue(Math.abs(x2 - 150) <= 10 && Math.abs(y2 - 150) <= 10);
}

@Test
public void testPaintComponentWithPointsAndLandmarks() {
HuntDisplay huntDisplay = new HuntDisplay();
TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
TrigPoint point3 = new TrigPoint(5.0, 6.0, 0.0, true);
Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/landmark1.png")));
Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/landmark2.png")));
huntDisplay.addPoint(point1);
huntDisplay.addPoint(point2);
huntDisplay.addLandmark(landmark1);
huntDisplay.addLandmark(landmark2);
Graphics2D g = (Graphics2D) huntDisplay.createGraphics();
huntDisplay.paintComponent(g);
g.dispose();
assertEquals(4, huntDisplay.points.size() + huntDisplay.landmarks.size());
assertTrue(huntDisplay.isOpaque());
assertTrue(huntDisplay.getSize().height >= 200 && huntDisplay.getSize().width >= 200);
}
}