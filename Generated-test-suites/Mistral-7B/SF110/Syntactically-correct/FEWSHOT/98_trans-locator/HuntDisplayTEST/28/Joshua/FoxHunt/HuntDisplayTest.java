package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstruction() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
        assertTrue(huntDisplay.isOpaque());
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 1.0, 0.0, false);
        huntDisplay.addPoint(point);
        assertEquals(1, huntDisplay.points.size());
        huntDisplay.repaint();
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 1.0, new ImageIcon(getClass().getResource("/testIcon.png")));
        huntDisplay.addLandmark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
        huntDisplay.repaint();
    }

    @Test
    public void testPaintComponentWithPoints() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(1.0, 1.0, 0.0, false);
        TrigPoint point2 = new TrigPoint(2.0, 2.0, 0.0, false);
        TrigPoint point3 = new TrigPoint(3.0, 3.0, 0.0, false);
        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        huntDisplay.addPoint(point3);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(huntDisplay);
        frame.setVisible(true);

        Graphics2D g = huntDisplay.createGraphics();
        huntDisplay.paintComponent(g);
        g.dispose();

        TrigPoint[] trigpoints = new TrigPoint[huntDisplay.points.size()];
        for (int a = 0; a < huntDisplay.points.size(); ++a) {
            trigpoints[a] = (TrigPoint) huntDisplay.points.elementAt(a);
        }
        for (int t = 0; t < trigpoints.length; ++t) {
            int expectedX = (int) ((trigpoints[t].getX() + 0.5) * huntDisplay.getWidth());
            int expectedY = (int) ((trigpoints[t].getY() + 0.5) * huntDisplay.getHeight());
            int actualX = (int) (trigpoints[t].getLocation().x);
            int actualY = (int) (trigpoints[t].getLocation().y);
            assertEquals("Incorrect X position for point [" + t + "]. Expected: " + expectedX + ", but actual: " + actualX, expectedX, actualX);
            assertEquals("Incorrect Y position for point [" + t + "]. Expected: " + expectedY + ", but actual: " + actualY, expectedY, actualY);
        }

        frame.dispose();
    }

    @Test
    public void testPaintComponentWithLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark1 = new Landmark(1.0, 1.0, new ImageIcon(getClass().getResource("/testIcon.png")));
        Landmark landmark2 = new Landmark(2.0, 2.0, new ImageIcon(getClass().getResource("/testIcon.png")));
        huntDisplay.addLandmark(landmark1);
        huntDisplay.addLandmark(landmark2);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(huntDisplay);
        frame.setVisible(true);

        Graphics2D g = huntDisplay.createGraphics();
        huntDisplay.paintComponent(g);
        g.dispose();

        Landmark[] landmarks = new Landmark[huntDisplay.landmarks.size()];
        for (int l = 0; l < huntDisplay.landmarks.size(); ++l) {
            landmarks[l] = (Landmark) huntDisplay.landmarks.elementAt(l);
        }
        for (int l = 0; l < landmarks.length; ++l) {
            int expectedX = (int) ((landmarks[l].getLon() + 0.5) * huntDisplay.getWidth());
            int expectedY = (int) ((landmarks[l].getLat() + 0.5) * huntDisplay.getHeight());
            int actualX = (int) (landmarks[l].getLocation().x);
            int actualY = (int) (landmarks[l].getLocation().y);
            assertEquals("Incorrect X position for landmark [" + l + "]. Expected: " + expectedX + ", but actual: " + actualX, expectedX, actualX);
            assertEquals("Incorrect Y position for landmark [" + l + "]. Expected: " + expectedY + ", but actual: " + actualY, expectedY, actualY);
        }

        frame.dispose();
    }
}