package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstruction() {
        HuntDisplay display = new HuntDisplay();
        assertNotNull(display);
        assertEquals(200, display.getPreferredSize().getHeight());
        assertEquals(200, display.getPreferredSize().getWidth());
        assertEquals(50, display.getMinimumSize().getHeight());
        assertEquals(50, display.getMinimumSize().getWidth());
        assertTrue(display.getBackground() instanceof Color);
        assertEquals(Color.WHITE, display.getBackground());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, true);
        display.addPoint(point);
        assertEquals(1, display.points.size());
        assertEquals(1.0, display.points.elementAt(0).getX(), 0.01);
        assertEquals(1.0, display.points.elementAt(0).getY(), 0.01);
        assertTrue(display.points.elementAt(0).isFox());
        display.repaint();
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
        display.addLandmark(landmark);
        assertEquals(0, display.landmarks.size());
        assertEquals(1, display.landmarks.size());
        assertEquals(1.0, display.landmarks.elementAt(0).getLon(), 0.01);
        assertEquals(1.0, display.landmarks.elementAt(0).getLat(), 0.01);
        assertNotNull(display.landmarks.elementAt(0).getIcon());
        display.repaint();
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, true);
        TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
        Landmark landmark = new Landmark(5.0, 6.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
        display.addPoint(point1);
        display.addPoint(point2);
        display.addLandmark(landmark);

        Graphics2D g = (Graphics2D) createGraphicsMock();
        display.paintComponent(g);

        verify(g).setColor(Color.BLACK);
        verify(g).fillOval((int) (point1.getX() * display.getWidth()), (int) (point1.getY() * display.getHeight()), 5, 5);
        verify(g).setColor(Color.GREEN);
        verify(g).fillOval((int) (point2.getX() * display.getWidth()), (int) (point2.getY() * display.getHeight()), 5, 5);
        verify(g).setColor(display.getForeground());
        verify(g).drawImage(landmark.getIcon().getImage(), (int) (landmark.getLon() * display.getWidth()), (int) (landmark.getLat() * display.getHeight()), 10, 10, (Color) display.getBackground(), display);
    }
}