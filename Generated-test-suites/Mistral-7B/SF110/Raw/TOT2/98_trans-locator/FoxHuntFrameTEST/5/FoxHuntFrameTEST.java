import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame fhFrame = new FoxHuntFrame();

        // Basic Test
        fhFrame.theta.setText("15");
        fhFrame.lat.setText("37.774929");
        fhFrame.lon.setText("-122.41942");
        fhFrame.actionPerformed(new ActionEvent(fhFrame.add, ActionEvent.ACTION_PERFORMED, null));
        assertEquals(1, fhFrame.points.size());
        assertEquals(1, fhFrame.fox.size());

        // Edge Cases
        fhFrame.theta.setText("0");
        fhFrame.lat.setText("0");
        fhFrame.lon.setText("0");
        fhFrame.actionPerformed(new ActionEvent(fhFrame.add, ActionEvent.ACTION_PERFORMED, null));
        assertEquals(0, fhFrame.points.size());
        assertEquals(0, fhFrame.fox.size());

        fhFrame.theta.setText("360");
        fhFrame.lat.setText("90.99999");
        fhFrame.lon.setText("180.99999");
        fhFrame.actionPerformed(new ActionEvent(fhFrame.add, ActionEvent.ACTION_PERFORMED, null));
        assertEquals(1, fhFrame.points.size());
        assertEquals(1, fhFrame.fox.size());

        // Exception Handling
        fhFrame.theta.setText("abc");
        fhFrame.lat.setText("37.774929");
        fhFrame.lon.setText("-122.41942");
        try {
            fhFrame.actionPerformed(new ActionEvent(fhFrame.add, ActionEvent.ACTION_PERFORMED, null));
            fail("Expected IllegalArgumentException for invalid theta input");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}