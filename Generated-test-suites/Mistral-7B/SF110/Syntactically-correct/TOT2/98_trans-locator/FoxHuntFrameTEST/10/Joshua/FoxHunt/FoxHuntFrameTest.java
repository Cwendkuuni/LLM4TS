import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("45");
        fhf.lat.setText("51.5074");
        fhf.lon.setText("-0.1278");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, ""));
        assertEquals(2, fhf.points.size());
        assertEquals(2, fhf.fox.size());
    }
}