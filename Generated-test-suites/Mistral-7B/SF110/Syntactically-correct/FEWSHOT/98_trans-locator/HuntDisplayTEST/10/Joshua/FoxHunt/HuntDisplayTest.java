package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstructor() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension preferredSize = huntDisplay.getPreferredSize();
        assertEquals(200, preferredSize.getHeight());
        assertEquals(200, preferredSize.getWidth());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension minimumSize = huntDisplay.getMinimumSize();
        assertEquals(50, minimumSize.getHeight());
        assertEquals(50, minimumSize.getWidth());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(3.14, 2.71, 0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(1, huntDisplay.points.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddPointNullTrigPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addPoint(null);
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(3.14, 2.71, 0, false);
        huntDisplay.addPoint(trigPoint);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        huntDisplay.paintComponent(graphics);
        graphics.dispose();

        // Add assertions for the point's position and color
        // based on the HuntDisplay's paintComponent logic.
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(3.14, 2.71, new ImageIcon(getClass().getResource("/icon.png")));
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddLandMarkNullLandmark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        huntDisplay.addLandMark(null);
    }
}