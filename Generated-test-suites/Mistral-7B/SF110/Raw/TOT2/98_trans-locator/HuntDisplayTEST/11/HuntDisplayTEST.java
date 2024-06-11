import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplayDefaultConstructor() {
        HuntDisplay huntDisplay = new HuntDisplay();
        // Add assertions here
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

    // Other tests for addPoint, addLandMark, and paintComponent methods
}