package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.HuntDisplay;

public class HuntDisplayTest {

    @Test
    public void testConstructor() {
        HuntDisplay display = new HuntDisplay();
        assertNotNull(display);
        assertEquals(new Dimension(200, 200), display.getPrefSize());
        assertEquals(new Dimension(50, 50), display.getMinSize());
        assertNull(display.getPoints());
        assertNull(display.getLandmarks());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
        display.addPoint(point);
        Vector points = display.getPoints();
        assertEquals(1, points.size());
        TrigPoint displayedPoint = (TrigPoint) points.elementAt(0);
        assertEquals(1.0, displayedPoint.getY(), 0.01);
        assertEquals(2.0, displayedPoint.getX(), 0.01);
        assertEquals(0.0, displayedPoint.getTheta(), 0.01);
        assertFalse(displayedPoint.isFox());
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
        display.addLandmark(landmark);
        Vector landmarks = display.getLandmarks();
        assertEquals(1, landmarks.size());
        Landmark displayedLandmark = (Landmark) landmarks.elementAt(0);
        assertEquals(1.0, displayedLandmark.getLat(), 0.01);
        assertEquals(2.0, displayedLandmark.getLon(), 0.01);
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = createMockGraphics();
        display.paintComponent(g);

        // Test paint points
        TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
        display.addPoint(point1);
        TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
        display.addPoint(point2);

        display.paintComponent(g);

        int width = display.getWidth();
        int height = display.getHeight();

        for (int i = 0; i < display.getPoints().size(); i++) {
            TrigPoint point = (TrigPoint) display.getPoints().elementAt(i);
            double x = point.getX();
            double y = point.getY();

            int X = (int) ((x * width) + 0.5);
            int Y = (int) ((y * height) + 0.5);

            assertEquals(Color.BLACK, g.getColor());
            g.fillOval(X - 2, Y - 2, 6, 6);
            assertEquals(Color.BLACK, g.getColor());
            g.fillOval(X, Y, 5, 5);
        }

        // Test paint landmarks
        Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
        display.addLandmark(landmark);

        display.paintComponent(g);

        for (int i = 0; i < display.getLandmarks().size(); i++) {
            Landmark landmark2 = (Landmark) display.getLandmarks().elementAt(i);
            double x = landmark2.getLon();
            double y = landmark2.getLat();

            int X = (int) ((x * width) + 0.5);
            int Y = (int) ((y * height) + 0.5);

            assertEquals(Color.getBackgroundColors()[0], g.getColor());
            g.drawImage(landmark2.getIcon().getImage(), X - 5, Y - 5, 10, 10, null);
        }
    }

    private Graphics createMockGraphics() {
        return (Graphics) new Object() {
            public void fillOval(int x, int y, int width, int height) {}
            public void drawImage(BufferedImage img, int x, int y, int width, int height, Object observer, Shape clip) {}
            public void setColor(Color color) {}
        };
    }
}