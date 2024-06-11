package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {

    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testFoxHuntFrameConstructor() {
        assertNotNull(frame);
        assertEquals("Triangulation", frame.getTitle());
    }

    @Test
    public void testAddPointMethod() {
        frame.theta.setText("45");
        frame.lat.setText("42.12345678");
        frame.lon.setText("87.65432109");
        frame.add.doClick();

        final TrigPoint point = (TrigPoint) frame.points.lastElement();
        final TrigPoint expectedFox = FoxCalc.calcFox(point, (TrigPoint) frame.points.lastElement() - 2);

        assertEquals(expectedFox.getX(), frame.fox.lastElement().getX(), 0.001);
        assertEquals(expectedFox.getY(), frame.fox.lastElement().getY(), 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidAddPoint() {
        frame.theta.setText("abc");
        frame.lat.setText("42.12345678");
        frame.lon.setText("87.65432109");
        frame.add.doClick();
    }

    @Test
    public void testLoadLandmarksMethod() {
        frame.loadLandmarks();
        assertTrue(frame.display.getLandmarks().size() > 0);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadLandmarksFileNotFound() {
        frame.loadLandmarks();
        frame.error("Can't Find the file");
    }

    @Test(expected = IOException.class)
    public void testLoadLandmarksIOException() {
        frame.loadLandmarks();
        frame.error("Error communicating with the file");
    }
}