import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        String expectedTheta = "35";
        String expectedLat = "51.5074";
        String expectedLon = "0.0828";

        frame.theta.setText(expectedTheta);
        frame.lat.setText(expectedLat);
        frame.lon.setText(expectedLon);
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

        TrigPoint expectedPoint = new TrigPoint(Double.parseDouble(expectedLat), Double.parseDouble(expectedLon), Integer.parseInt(expectedTheta));
        TrigPoint actualPoint = (TrigPoint) frame.fox.elementAt(frame.fox.size() - 1);

        assertEquals(expectedPoint.getX(), actualPoint.getX(), 0.0);
        assertEquals(expectedPoint.getY(), actualPoint.getY(), 0.0);
        assertEquals(expectedPoint.getAngle(), actualPoint.getAngle(), 0.0);
    }
}