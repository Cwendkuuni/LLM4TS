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
        Dimension size = hd.getPreferredSize();
        assertEquals(200, size.getWidth());
        assertEquals(200, size.getHeight());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay hd = new HuntDisplay();
        Dimension size = hd.getMinimumSize();
        assertEquals(50, size.getWidth());
        assertEquals(50, size.getHeight());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay hd = new HuntDisplay();
        TrigPoint point = new TrigPoint(0.5, 0.5, 0, false);
        hd.addPoint(point);
        // assertRepaint() or use other means to check if component is repainted
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay hd = new HuntDisplay();
        Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon("landmark.png"));
        hd.addLandMark(landmark);
        // assertRepaint() or use other means to check if component is repainted
    }

    // Uncomment and implement testPaintComponent() method
    /*
    @Test
    public void testPaintComponent() {
        HuntDisplay hd = new HuntDisplay();
        Graphics g = null; // need mocking
        hd.paintComponent(g);
        // add assertions for each draw operation in paintComponent()
    }
    */
}