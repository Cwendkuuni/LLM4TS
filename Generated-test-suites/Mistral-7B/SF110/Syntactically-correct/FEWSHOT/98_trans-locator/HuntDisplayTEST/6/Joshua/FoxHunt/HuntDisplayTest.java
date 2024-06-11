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
    public void testConstructor() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
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
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Image image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Landmark landmark = new Landmark(1.0, 2.0, image);
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
        huntDisplay.repaint();
    }

    @Test
    public void testPaintComponentWithNoPointsAndLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.paintComponent(mockGraphics);
        assertTrue(huntDisplay.getWidth() > 0 && huntDisplay.getHeight() > 0);
        assertTrue(huntDisplay.getBackground().getRGB() == Color.WHITE.getRGB());
    }

    @Test
    public void testPaintComponentWithOnlyPoints() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        huntDisplay.paintComponent(mockGraphics);
        assertTrue(huntDisplay.getWidth() > 0 && huntDisplay.getHeight() > 0);
        for (int i = 0; i < huntDisplay.points.size(); i++) {
            TrigPoint trigPoint = (TrigPoint) huntDisplay.points.elementAt(i);
            int x = (int) ((trigPoint.getX() * huntDisplay.getWidth()) + 0.5);
            int y = (int) ((trigPoint.getY() * huntDisplay.getHeight()) + 0.5);
            assertEquals(Color.BLACK, huntDisplay.getForeground());
            assertEquals(5, huntDisplay.getWidth());
            assertEquals(5, huntDisplay.getHeight());
            assertEquals(trigPoint.isFox(), huntDisplay.getBackground().equals(Color.GREEN));
            huntDisplay.getGraphics().fillOval(x, y, 5, 5);
        }
    }

    @Test
    public void testPaintComponentWithOnlyLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark1 = new Landmark(1.0, 2.0, mockImage);
        Landmark landmark2 = new Landmark(3.0, 4.0, mockImage);
        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);
        huntDisplay.paintComponent(mockGraphics);
        assertTrue(huntDisplay.getWidth() > 0 && huntDisplay.getHeight() > 0);
        for (int i = 0; i < huntDisplay.landmarks.size(); i++) {
            Landmark landMark = (Landmark) huntDisplay.landmarks.elementAt(i);
            int x = (int) ((landMark.getLon() * huntDisplay.getWidth()) + 0.5);
            int y = (int) ((landMark.getLat() * huntDisplay.getHeight()) + 0.5);
            assertEquals(landMark.getIcon().getImage().getWidth(), 10);
            assertEquals(landMark.getIcon().getImage().getHeight(), 10);
            huntDisplay.getGraphics().drawImage(landMark.getIcon().getImage(), x, y, 10, 10, huntDisplay.getBackground(), huntDisplay);
        }
    }

    @Test
    public void testPaintComponentWithPointsAndLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
        Landmark landmark1 = new Landmark(1.0, 2.0, mockImage);
        Landmark landmark2 = new Landmark(3.0, 4.0, mockImage);
        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);
        huntDisplay.paintComponent(mockGraphics);
        assertTrue(huntDisplay.getWidth() > 0 && huntDisplay.getHeight() > 0);
        for (int i = 0; i < huntDisplay.points.size(); i++) {
            TrigPoint trigPoint = (TrigPoint) huntDisplay.points.elementAt(i);
            int x = (int) ((trigPoint.getX() * huntDisplay.getWidth()) + 0.5);
            int y = (int) ((trigPoint.getY() * huntDisplay.getHeight()) + 0.5);
            assertEquals(Color.BLACK, huntDisplay.getForeground());
            assertEquals(5, huntDisplay.getWidth());
            assertEquals(5, huntDisplay.getHeight());
            assertEquals(trigPoint.isFox(), huntDisplay.getBackground().equals(Color.GREEN));
            huntDisplay.getGraphics().fillOval(x, y, 5, 5);
        }
        for (int i = 0; i < huntDisplay.landmarks.size(); i++) {
            Landmark landMark = (Landmark) huntDisplay.landmarks.elementAt(i);
            int x = (int) ((landMark.getLon() * huntDisplay.getWidth()) + 0.5);
            int y = (int) ((landMark.getLat() * huntDisplay.getHeight()) + 0.5);
            assertEquals(landMark.getIcon().getImage().getWidth(), 10);
            assertEquals(landMark.getIcon().getImage().getHeight(), 10);
            huntDisplay.getGraphics().drawImage(landMark.getIcon().getImage(), x, y, 10, 10, huntDisplay.getBackground(), huntDisplay);
        }
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

    private Graphics mockGraphics = (Graphics2D) new FakeGraphics2D();
    private Image mockImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    private static class FakeGraphics2D extends Graphics2D {
        FakeGraphics2D() {
            super(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
        }

        @Override
        public void fillOval(int x, int y, int width, int height) {
        }

        @Override
        public void drawImage(Image img, int x, int y, ImageObserver observer) {
        }
    }
}