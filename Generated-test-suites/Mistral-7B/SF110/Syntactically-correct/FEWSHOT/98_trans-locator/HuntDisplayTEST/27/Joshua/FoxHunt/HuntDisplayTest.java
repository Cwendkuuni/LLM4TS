package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

@RunWith(MockitoJUnitRunner.class)
public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstruction() {
        HuntDisplay display = new HuntDisplay();
        assertNotNull(display);
        assertNull(display.getPoints());
        assertNull(display.getLandmarks());
        assertEquals(new Dimension(200, 200), display.getPrefSize());
        assertEquals(new Dimension(50, 50), display.getMinSize());
        assertEquals(Color.WHITE, display.getBackground());
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(new Dimension(200, 200), display.getPrefSize());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(new Dimension(50, 50), display.getMinSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        display.addPoint(trigPoint);
        List points = display.getPoints();
        assertEquals(1, points.size());
        TrigPoint point = (TrigPoint) points.get(0);
        assertEquals(1.0, point.getX(), 0.01);
        assertEquals(1.0, point.getY(), 0.01);
        assertEquals(0.0, point.getTheta(), 0.01);
        assertFalse(point.isFox());
    }

    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, Mockito.mock(Image.class));
        display.addLandmark(landmark);
        List landmarks = display.getLandmarks();
        assertEquals(1, landmarks.size());
        Landmark l = (Landmark) landmarks.get(0);
        assertEquals(1.0, l.getX(), 0.01);
        assertEquals(1.0, l.getY(), 0.01);
        assertNotNull(l.getIcon());
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, true);
        Landmark landmark = new Landmark(1.5, 2.5, Mockito.mock(Image.class));

        display.addPoint(trigPoint1);
        display.addPoint(trigPoint2);
        display.addLandmark(landmark);

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        display.paintComponent(g);

        // Check points
        int foxPointIndex = -1;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color c = new Color(image.getRGB(x, y));
                if (c.equals(Color.GREEN)) {
                    foxPointIndex = indexOf(trigPoint2, display.getPoints());
                    int expectedX = (int)((trigPoint2.getX() + display.getXAdd()) * image.getWidth());
                    int expectedY = (int)((trigPoint2.getY() + display.getYAdd()) * image.getHeight());
                    assertEquals(expectedX, x);
                    assertEquals(expectedY, y);
                    break;
                }
                if (c.equals(Color.BLACK)) {
                    int expectedX = (int)((trigPoint1.getX() + display.getXAdd()) * image.getWidth());
                    int expectedY = (int)((trigPoint1.getY() + display.getYAdd()) * image.getHeight());
                    assertEquals(expectedX, x);
                    assertEquals(expectedY, y);
                    break;
                }
            }
            if (foxPointIndex != -1) {
                break;
            }
        }

        // Check landmark
        int landmarkX = (int)((landmark.getLon() + display.getXAdd()) * image.getWidth());
        int landmarkY = (int)((landmark.getLat() + display.getYAdd()) * image.getHeight());
        BufferedImage landmarkImage = (BufferedImage) landmark.getIcon().getImage();
        for (int y = landmarkY; y < landmarkY + landmarkImage.getHeight(); y++) {
            for (int x = landmarkX; x < landmarkX + landmarkImage.getWidth(); x++) {
                Color c = new Color(image.getRGB(x, y));
                Color expectedColor = new Color(landmarkImage.getRGB(x - landmarkX, y - landmarkY));
                assertEquals(expectedColor, c);
            }
        }
    }

    private int indexOf(TrigPoint point, List list) {
        for (int i = 0; i < list.size(); i++) {
            TrigPoint trigPoint = (TrigPoint) list.get(i);
            if (trigPoint.equals(point)) {
                return i;
            }
        }
        return -1;
    }
}