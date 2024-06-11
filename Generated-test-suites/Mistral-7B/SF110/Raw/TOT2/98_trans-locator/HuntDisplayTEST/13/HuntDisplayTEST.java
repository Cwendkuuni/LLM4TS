import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstructor() {
        HuntDisplay hd = new HuntDisplay();
        assertNotNull(hd);
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension d = hd.getPreferredSize();
        assertEquals(200, d.getWidth());
        assertEquals(200, d.getHeight());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension d = hd.getMinimumSize();
        assertEquals(50, d.getWidth());
        assertEquals(50, d.getHeight());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint tp = new TrigPoint(1.0, 2.0, 3.0, false);
        hd.addPoint(tp);
        assertEquals(1, hd.points.size());
        hd.paintComponent(new Graphics2D()); // Call paintComponent for testing
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay hd = new HuntDisplay();
        Landmark lm = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
        hd.addLandMark(lm);
        assertEquals(1, hd.landmarks.size());
        hd.paintComponent(new Graphics2D()); // Call paintComponent for testing
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay hd = new HuntDisplay();
        Graphics2D g = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
        hd.paintComponent(g);
        // Add assertions for checking drawing of points and landmarks
    }
}