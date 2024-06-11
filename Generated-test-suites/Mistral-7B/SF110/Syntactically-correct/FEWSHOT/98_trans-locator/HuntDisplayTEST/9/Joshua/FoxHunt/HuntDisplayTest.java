package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstruction() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
        assertEquals(Color.WHITE, huntDisplay.getBackground());
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(1, huntDisplay.points.size());

        huntDisplay.paintComponent(createGraphics2DMock());
        BufferedImage image = (BufferedImage) huntDisplay.getGraphics();
        assertEquals(1, image.getWidth());
        assertEquals(1, image.getHeight());
        assertEquals(Color.BLACK, image.getRGB(0, 0));
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
        huntDisplay.addLandMark(landmark);
        assertEquals(0, huntDisplay.points.size());
        assertEquals(1, huntDisplay.landmarks.size());

        huntDisplay.paintComponent(createGraphics2DMock());
        BufferedImage image = (BufferedImage) huntDisplay.getGraphics();
        assertEquals(1, image.getWidth());
        assertEquals(1, image.getHeight());
        assertEquals(Color.LIGHT_GRAY, image.getRGB(0, 0));
    }

    @Test
    public void testPaintingMultiplePointsAndLandMarks() {
        HuntDisplay huntDisplay = new HuntDisplay();

        TrigPoint point1 = new TrigPoint(1.0, 2.0, 0.0, false);
        TrigPoint point2 = new TrigPoint(3.0, 4.0, 0.0, false);
        Landmark landmark1 = new Landmark(0.5, 0.5, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));

        huntDisplay.addPoint(point1);
        huntDisplay.addPoint(point2);
        huntDisplay.addLandMark(landmark1);

        huntDisplay.paintComponent(createGraphics2DMock());
        BufferedImage image = (BufferedImage) huntDisplay.getGraphics();

        int point1X = (int)((point1.getX() * huntDisplay.getWidth()) + 0.5 - huntDisplay.getWidth() / 2);
        int point1Y = (int)((point1.getY() * huntDisplay.getHeight()) + 0.5 - huntDisplay.getHeight() / 2);
        int point2X = (int)((point2.getX() * huntDisplay.getWidth()) + 0.5 - huntDisplay.getWidth() / 2);
        int point2Y = (int)((point2.getY() * huntDisplay.getHeight()) + 0.5 - huntDisplay.getHeight() / 2);
        int landmark1X = (int)((landmark1.getLon() * huntDisplay.getWidth()) + 0.5 - huntDisplay.getWidth() / 2);
        int landmark1Y = (int)((landmark1.getLat() * huntDisplay.getHeight()) + 0.5 - huntDisplay.getHeight() / 2);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (x >= point1X && x <= point1X + 5 && y >= point1Y && y <= point1Y + 5) {
                    assertEquals(Color.BLACK.getRGB(), image.getRGB(x, y));
                }
                if (x >= point2X && x <= point2X + 5 && y >= point2Y && y <= point2Y + 5) {
                    assertEquals(Color.BLACK.getRGB(), image.getRGB(x, y));
                }
                if (x >= landmark1X && x <= landmark1X + 10 && y >= landmark1Y && y <= landmark1Y + 10) {
                    assertEquals(landmark1.getIcon().getPixelColorAt(0, 0).getRGB(), image.getRGB(x, y));
                }
            }
        }
    }

    @Test
    public void testGetMinSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(50, 50), huntDisplay.getMinSize());
    }

    @Test
    public void testGetPrefSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(new Dimension(200, 200), huntDisplay.getPrefSize());
    }

    private Graphics2D createGraphics2DMock() {
        return (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
    }
}