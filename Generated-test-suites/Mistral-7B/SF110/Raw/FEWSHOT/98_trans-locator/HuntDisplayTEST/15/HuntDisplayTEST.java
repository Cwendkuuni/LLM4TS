package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplay() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
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
        Landmark landmark = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/landmarkIcon.png")));
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
        huntDisplay.repaint();
    }

    @Test
    public void testPaintComponentWithPoints() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, true);
        TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
        huntDisplay.addPoint(trigPoint1);
        huntDisplay.addPoint(trigPoint2);
        huntDisplay.repaint();
        BufferedImage image = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        huntDisplay.paintComponent(g);
        g.dispose();
        int greenFoxCount = 0;
        int blackPointCount = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                if (pixelColor.getGreen() > 100) {
                    greenFoxCount++;
                } else {
                    blackPointCount++;
                }
            }
        }
        assertEquals(1, greenFoxCount);
        assertEquals(1, blackPointCount);
    }

    @Test
    public void testPaintComponentWithLandmarks() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/landmarkIcon.png")));
        Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/landmarkIcon.png")));
        huntDisplay.addLandMark(landmark1);
        huntDisplay.addLandMark(landmark2);
        huntDisplay.repaint();
        BufferedImage image = new BufferedImage(huntDisplay.getWidth(), huntDisplay.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        huntDisplay.paintComponent(g);
        g.dispose();
        int iconCount = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                if (pixelColor.getAlpha() == 0) {
                    continue;
                }
                BufferedImage iconImage = landmark1.getIcon().getImage();
                if (iconImage.getWidth() > x && iconImage.getHeight() > y && landmark1.getIcon() == huntDisplay.landmarks.get(0).getIcon()) {
                    iconCount++;
                    break;
                }
                BufferedImage iconImage2 = landmark2.getIcon().getImage();
                if (iconImage2.getWidth() > x && iconImage2.getHeight() > y && landmark2.getIcon() == huntDisplay.landmarks.get(1).getIcon()) {
                    iconCount++;
                    break;
                }
            }
        }
        assertEquals(2, iconCount);
    }
}