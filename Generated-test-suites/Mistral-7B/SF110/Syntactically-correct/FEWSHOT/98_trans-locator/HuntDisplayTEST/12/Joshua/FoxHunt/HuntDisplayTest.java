package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testAddPointAndPaint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
        display.addPoint(point);
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        display.paintComponent(g);
        int x = (int) (point.getX() * 200.0);
        int y = (int) (point.getY() * 200.0);
        assertEquals(Color.BLACK.getRGB(), image.getRGB(x, y));
        assertEquals(Color.WHITE.getRGB(), image.getRGB(x - 1, y));
        assertEquals(Color.WHITE.getRGB(), image.getRGB(x + 1, y));
        assertEquals(Color.WHITE.getRGB(), image.getRGB(x, y - 1));
        assertEquals(Color.WHITE.getRGB(), image.getRGB(x, y + 1));
        g.dispose();
    }

    @Test
    public void testAddLandmarkAndPaint() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
        display.addLandmark(landmark);
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        display.paintComponent(g);
        int x = (int) (landmark.getLon() * 200.0);
        int y = (int) (landmark.getLat() * 200.0);
        BufferedImage icon = landmark.getIcon().getImage();
        for (int i = 0; i < icon.getWidth(); i++) {
            for (int j = 0; j < icon.getHeight(); j++) {
                int expectedRGB = icon.getRGB(i, j);
                int actualRGB = image.getRGB(x + i, y + j);
                assertEquals(expectedRGB, actualRGB);
            }
        }
        g.dispose();
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(200, display.getPreferredSize().height);
        assertEquals(200, display.getPreferredSize().width);
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(50, display.getMinimumSize().height);
        assertEquals(50, display.getMinimumSize().width);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullPoint() {
        HuntDisplay display = new HuntDisplay();
        display.addPoint(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullLandmark() {
        HuntDisplay display = new HuntDisplay();
        display.addLandmark(null);
    }
}