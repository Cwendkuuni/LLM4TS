package Joshua.FoxHunt;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import org.junit.Assert.*;

public class HuntDisplayTest {
    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 3.0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(2, huntDisplay.points.size());
    }
    
    @Test
    public void testAddPointEdgeCases() {
        HuntDisplay huntDisplay = new HuntDisplay();
        // Adding a null TrigPoint
        assertThrows(NullPointerException.class, () -> huntDisplay.addPoint(null));
        // Adding a TrigPoint with invalid coordinates
        assertThrows(IllegalArgumentException.class, () -> huntDisplay.addPoint(new TrigPoint(Double.NaN, 2.0, 3.0, false)));
        // Adding a TrigPoint with an invalid theta value
        assertThrows(IllegalArgumentException.class, () -> huntDisplay.addPoint(new TrigPoint(1.0, Double.NaN, 3.0, false)));
        // Adding a TrigPoint with an isFox value other than boolean
        assertThrows(IllegalArgumentException.class, () -> huntDisplay.addPoint(new TrigPoint(1.0, 2.0, 3.0, 'A')));
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
        huntDisplay.addLandMark(landmark);
        assertEquals(1, huntDisplay.landmarks.size());
    }
    
    @Test
    public void testAddLandMarkEdgeCases() {
        HuntDisplay huntDisplay = new HuntDisplay();
        // Adding a null Landmark
        assertThrows(NullPointerException.class, () -> huntDisplay.addLandMark(null));
        // Adding a Landmark with invalid coordinates
        assertThrows(IllegalArgumentException.class, () -> huntDisplay.addLandMark(new Landmark(Double.NaN, 2.0, null)));
        // Adding a Landmark with a null icon
        Landmark landmark = new Landmark(1.0, 2.0, null);
        assertThrows(NullPointerException.class, () -> huntDisplay.addLandMark(landmark));
    }
    
    @Test
    public void testGetPreferredSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension expectedSize = new Dimension(200, 200);
        Dimension actualSize = huntDisplay.getPreferredSize();
        assertEquals(expectedSize, actualSize);
    }
    
    @Test
    public void testGetMinimumSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Dimension expectedSize = new Dimension(50, 50);
        Dimension actualSize = huntDisplay.getMinimumSize();
        assertEquals(expectedSize, actualSize);
    }
}