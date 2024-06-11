package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;
    private TrigPoint trigPoint1;
    private TrigPoint trigPoint2;
    private Landmark landmark1;

    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
        trigPoint1 = new TrigPoint(1.0, 0.0, 0.0, false);
        trigPoint2 = new TrigPoint(0.5, 1.0, Math.PI / 2, true);
        landmark1 = new Landmark(0.6, 1.2, createImageIcon("landmark.png"));
    }

    @Test
    public void testHuntDisplayConstruction() {
        assertNotNull(huntDisplay);
        assertEquals(0, huntDisplay.getPoints().size());
        assertEquals(0, huntDisplay.getLandmarks().size());
    }

    @Test
    public void testAddPoint() {
        huntDisplay.addPoint(trigPoint1);
        assertEquals(1, huntDisplay.getPoints().size());
        huntDisplay.addPoint(trigPoint2);
        assertEquals(2, huntDisplay.getPoints().size());
        huntDisplay.repaint();

        // Test painting points
        Graphics2D g = huntDisplay.createGraphics();
        huntDisplay.paintComponent(g);
        g.dispose();

        BufferedImage image = (BufferedImage) huntDisplay.getImage();
        int x1 = (int) (trigPoint1.getX() * image.getWidth());
        int y1 = (int) (trigPoint1.getY() * image.getHeight());
        int x2 = (int) (trigPoint2.getX() * image.getWidth());
        int y2 = (int) (trigPoint2.getY() * image.getHeight());

        assertTrue(image.getRGB(x1, y1) == trigPoint1.isFox() ? Color.GREEN.getRGB() : Color.BLACK.getRGB());
        assertTrue(image.getRGB(x2, y2) == Color.GREEN.getRGB());
    }

    @Test
    public void testAddLandmark() {
        huntDisplay.addLandMark(landmark1);
        assertEquals(0, huntDisplay.getPoints().size());
        assertEquals(1, huntDisplay.getLandmarks().size());
        huntDisplay.repaint();

        // Test painting landmarks
        Graphics2D g = huntDisplay.createGraphics();
        huntDisplay.paintComponent(g);
        g.dispose();

        BufferedImage image = (BufferedImage) huntDisplay.getImage();
        int x = (int) (landmark1.getLon() * image.getWidth());
        int y = (int) (landmark1.getLat() * image.getHeight());

        assertNotNull(image.getSubimage(x, y, 10, 10));
    }

    @Test
    public void testGetPreferredSize() {
        assertEquals(200, huntDisplay.getPreferredSize().width);
        assertEquals(200, huntDisplay.getPreferredSize().height);
    }

    @Test
    public void testGetMinimumSize() {
        assertEquals(50, huntDisplay.getMinimumSize().width);
        assertEquals(50, huntDisplay.getMinimumSize().height);
    }

    private Icon createImageIcon(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        BufferedImage image = ImageIO.read(new File(path));
        return new ImageIcon(image);
    }
}