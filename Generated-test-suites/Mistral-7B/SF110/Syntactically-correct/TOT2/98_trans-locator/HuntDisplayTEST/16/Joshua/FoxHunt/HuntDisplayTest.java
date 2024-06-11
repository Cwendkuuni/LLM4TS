package Joshua.FoxHunt.Test;

import org.junit.Test;
import org.junit.Assert.*;
import Joshua.FoxHunt.*;
import java.awt.image.*;

public class HuntDisplayTest {

    @Test
    public void testConstructor() {
        HuntDisplay hd = new HuntDisplay();
        assertNotNull(hd);
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension size = hd.getPreferredSize();
        assertEquals(new Dimension(200, 200), size);
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension size = hd.getMinimumSize();
        assertEquals(new Dimension(50, 50), size);
    }

    @Test
    public void testAddPoint() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, false);
        hd.addPoint(point);
        assertEquals(1, hd.points.size());
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay hd = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
        hd.addLandMark(landmark);
        assertEquals(1, hd.landmarks.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullPoint() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint nullPoint = null;
        hd.addPoint(nullPoint);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddPointBeyondMaxSize() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, false);
        for (int i = 0; i < 1000; i++) {
            hd.addPoint(point);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPointInvalidCoordinates() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint point = new TrigPoint(-100.0, -200.0, 3.0, false);
        hd.addPoint(point);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullLandMark() {
        HuntDisplay hd = new HuntDisplay();
        Landmark nullLandmark = null;
        hd.addLandMark(nullLandmark);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddLandMarkBeyondMaxSize() {
        HuntDisplay hd = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
        for (int i = 0; i < 1000; i++) {
            hd.addLandMark(landmark);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLandMarkInvalidCoordinates() {
        HuntDisplay hd = new HuntDisplay();
        Landmark landmark = new Landmark(-100.0, -200.0, new ImageIcon("icon.png"));
        hd.addLandMark(landmark);
    }

    @Test
    public void testPaintPointsOutsideBounds() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint point = new TrigPoint(10.0, -100.0, 3.0, false);
        hd.addPoint(point);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        HuntDisplay testDisplay = new HuntDisplay() {
            @Override
            public void paintComponent(Graphics gg) {
                super.paintComponent(gg);
                this.paintComponent(g);
            }
        };
        testDisplay.addPoint(point);
        testDisplay.paintComponent(g);
        g.dispose();
        assertFalse(image.getRGB(0, 0) == g.getColor().getRGB());
    }

}