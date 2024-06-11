package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.FoxHuntFrame;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("10");
        frame.lat.setText("51.5074");
        frame.lon.setText("0.123456");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());

        TrigPoint point1 = (TrigPoint) frame.points.elementAt(0);
        TrigPoint point2 = (TrigPoint) frame.points.elementAt(1);
        TrigPoint fox = (TrigPoint) frame.fox.elementAt(0);

        assertEquals(51.5074, point1.getY(), 0.001);
        assertEquals(0.123456, point1.getX(), 0.001);
        assertEquals(10, point1.getAngle(), 0.1);

        assertEquals(51.5074, point2.getY(), 0.001);
        assertEquals(0.123456, point2.getX(), 0.001);
        assertEquals(180 - 10, point2.getAngle(), 0.1);

        assertEquals(51.5072, fox.getY(), 0.001);
        assertEquals(0.123456, fox.getX(), 0.001);

        frame.dispose();
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        int numLandmarks = frame.display.getNumLandmarks();
        assertEquals(5, numLandmarks);
        frame.dispose();
    }
}