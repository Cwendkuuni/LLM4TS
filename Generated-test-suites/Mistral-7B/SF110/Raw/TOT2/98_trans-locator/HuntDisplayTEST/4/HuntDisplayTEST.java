
package Joshua.FoxHunt.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Image;
import javax.imageio.ImageIO;

public class HuntDisplayTest {

    @Test
    public void constructor() {
        HuntDisplay display = new HuntDisplay();
        assertNotNull(display);
    }

    @Test
    public void getPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension preferredSize = display.getPreferredSize();
        assertEquals(display.prefSize, preferredSize);
    }

    @Test
    public void getMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        Dimension minimumSize = display.getMinimumSize();
        assertEquals(display.minSize, minimumSize);
    }

    @Test
    public void addPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
        display.addPoint(point);
        assertEquals(1, display.points.size());
    }

    @Test
    public void addLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
        display.addLandmark(landmark);
        assertEquals(1, display.landmarks.size());
    }

    @Test
    public void paintComponent() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
        display.addPoint(point);
        display.addLandmark(landmark);
        // Test the painting logic here

        // Display the JUnit test frame to verify the painting
        JFrame frame = new JFrame();
        frame.getContentPane().add(display);
        frame.pack();
        frame.setVisible(true);
    }

    @Test
    public void edgeCases() {
        HuntDisplay display = new HuntDisplay();

        // Edge cases for addPoint()
        for (int i = 0; i <= 100; i++) {
            TrigPoint point = new TrigPoint(i / 100.0, i / 180.0, 0.0, false);
            display.addPoint(point);
            assertEquals(i + 1, display.points.size());
        }

        // Edge cases for addLandmark()
        for (int i = 0; i <= 100; i++) {
            Landmark landmark = new Landmark(i / 90.0, i / 180.0, new ImageIcon("icon" + i + ".png"));
            display.addLandmark(landmark);
            assertEquals(i + 1, display.landmarks.size());
        }
    }

    @Test(expected = Exception.class)
    public void exceptionHandling() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(-1.0, 2.0, 0.0, false);
        display.addPoint(point);
        fail("An exception should be thrown when invalid data is provided");
    }
}
