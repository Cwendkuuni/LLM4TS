package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxHuntFrame;
import java.awt.*;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrameConstruction() {
        FoxHuntFrame fhFrame = new FoxHuntFrame();
        assertNotNull(fhFrame.getContentPane());
        assertEquals("Triangulation", fhFrame.getTitle());
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame fhFrame = new FoxHuntFrame();
        JTextField theta = fhFrame.theta;
        JTextField lat = fhFrame.lat;
        JTextField lon = fhFrame.lon;
        JButton add = fhFrame.add;
        JTable pos = fhFrame.pos;
        FoxTableModel posModel = fhFrame.posModel;
        HuntDisplay display = fhFrame.display;
        Vector points = fhFrame.points;
        Vector fox = fhFrame.fox;

        theta.setText("45");
        lat.setText("51.506928");
        lon.setText("-0.127782");
        add.doClick();

        assertEquals(1, points.size());
        assertEquals(1, fox.size());

        TrigPoint point = (TrigPoint) points.elementAt(0);
        TrigPoint foxLocation = (TrigPoint) fox.elementAt(0);
        assertEquals(45, point.getAngle());
        assertEquals(51.506928, point.getLatitude(), 0.000001);
        assertEquals(-0.127782, point.getLongitude(), 0.000001);
        assertEquals(51.506928, foxLocation.getLatitude(), 0.000001);
        assertEquals(-0.127782, foxLocation.getLongitude(), 0.000001);

        assertEquals(1, posModel.getRowCount());
        assertEquals("51.506928", posModel.getValueAt(0, 0).toString());
        assertEquals("-0.127782", posModel.getValueAt(0, 1).toString());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame fhFrame = new FoxHuntFrame();
        HuntDisplay display = fhFrame.display;

        assertTrue(display.getLandMarkCount() > 0);
    }
}