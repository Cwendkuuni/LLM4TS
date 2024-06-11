import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("51.5064");
        frame.lon.setText("0.123456");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
        TrigPoint point = (TrigPoint) frame.fox.lastElement();
        assertEquals(point.getX(), 45.0, 0.01);
        assertEquals(point.getY(), 51.5064, 0.001);
    }
}