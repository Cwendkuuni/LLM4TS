import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.*;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayConstructor() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertNotNull(huntDisplay);
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(huntDisplay.getPreferredSize(), new Dimension(200, 200));
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay huntDisplay = new HuntDisplay();
        assertEquals(huntDisplay.getMinimumSize(), new Dimension(50, 50));
    }

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 3.0, false);
        huntDisplay.addPoint(trigPoint);
        assertEquals(huntDisplay.points.size(), 1);
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.png")));
        huntDisplay.addLandMark(landmark);
        assertEquals(huntDisplay.landmarks.size(), 1);
    }

    // test paintComponent is not trivial and requires a Swing JUnit test framework
}