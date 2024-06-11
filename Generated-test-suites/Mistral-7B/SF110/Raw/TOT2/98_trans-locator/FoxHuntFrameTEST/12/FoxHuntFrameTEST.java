import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {
    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        TrigPoint expectedPoint = new TrigPoint(1.0, 2.0, 30.0);
        TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, null); // Assuming FoxCalc.calcFox already tested
        assertEquals(expectedPoint, foxHuntFrame.points.lastElement());
        assertEquals(expectedFox, foxHuntFrame.fox.lastElement());
    }
}