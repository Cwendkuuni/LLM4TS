import org.junit.Test;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
import static org.junit.Assert.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Toolkit;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstructor() {
        HuntDisplay hd = new HuntDisplay();
        assertNotNull(hd);
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension expected = new Dimension(200, 200);
        Dimension actual = hd.getPreferredSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension expected = new Dimension(50, 50);
        Dimension actual = hd.getMinimumSize();
        assertEquals(expected, actual);
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
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.jpg")));
        hd.addLandMark(landmark);
        assertEquals(1, hd.landmarks.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMoreThanMaxPoints() {
        HuntDisplay hd = new HuntDisplay();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, false);
            hd.addPoint(point);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPointWithIncorrectCoordinates() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint point = new TrigPoint(-1.0, 2.0, 3.0, false);
        hd.addPoint(point);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPointWithNullTrigPoint() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint point = null;
        hd.addPoint(point);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLandMarkWithIncorrectCoordinates() {
        HuntDisplay hd = new HuntDisplay();
        Landmark landmark = new Landmark(-1.0, 2.0, new ImageIcon(getClass().getResource("/icon.jpg")));
        hd.addLandMark(landmark);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLandMarkWithNullLandmark() {
        HuntDisplay hd = new HuntDisplay();
        Landmark landmark = null;
        hd.addLandMark(landmark);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaintComponentWithNullGraphics() {
        HuntDisplay hd = new HuntDisplay();
        Graphics g = null;
        hd.paintComponent(g);
    }

    @Test
    public void testPaintComponentWithValidData() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint[] trigpoints = new TrigPoint[10];
        for (int i = 0; i < 10; i++) {
            trigpoints[i] = new TrigPoint(i + 1.0, i + 1.0, 3.0, false);
            hd.addPoint(trigpoints[i]);
        }

        Landmark[] landmarks = new Landmark[5];
        for (int i = 0; i < 5; i++) {
            landmarks[i] = new Landmark(i + 1.0, i + 1.0, new ImageIcon(getClass().getResource("/icon.jpg")));
            hd.addLandMark(landmarks[i]);
        }

        Graphics g = createMockGraphics(); // Replace with a mock Graphics or JUnit's Test component
        hd.paintComponent(g);

        // Add assertions based on the expected output of paintComponent
        BufferedImage image = (BufferedImage) g.getImage();
        for (int i = 0; i < 10; i++) {
            TrigPoint trigpoint = hd.points.elementAt(i);
            int x = (int)((trigpoint.getX() + 0.5) * hd.getWidth());
            int y = (int)((trigpoint.getY() + 0.5) * hd.getHeight());
            assertEquals(trigpoint.isFox() ? Color.GREEN : Color.BLACK, image.getRGB(x, y));
        }

        for (int i = 0; i < 5; i++) {
            Landmark landmark = hd.landmarks.elementAt(i);
            int x = (int)((landmark.getLon() + 0.5) * hd.getWidth());
            int y = (int)((landmark.getLat() + 0.5) * hd.getHeight());
            File imageFile = new File(landmark.getIcon().getDescription(null));
            BufferedImage expectedImage = ImageIO.read(imageFile);
            int width = expectedImage.getWidth();
            int height = expectedImage.getHeight();
            for (int xx = 0; xx < width; xx++) {
                for (int yy = 0; yy < height; yy++) {
                    int expectedRGB = expectedImage.getRGB(xx, yy);
                    int actualRGB = image.getRGB(x + xx, y + yy);
                    assertEquals(expectedRGB, actualRGB);
                }
            }
        }
    }

    private Graphics createMockGraphics() {
        return new MockGraphics2D(); // Replace with JUnit's Test component or a mock Graphics
    }
}