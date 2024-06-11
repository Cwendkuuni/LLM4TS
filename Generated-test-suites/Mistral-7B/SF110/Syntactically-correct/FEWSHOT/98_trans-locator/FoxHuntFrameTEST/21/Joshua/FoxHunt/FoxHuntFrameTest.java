package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.FoxHuntFrame;
import java.util.*;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrameConstructor() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        assertNotNull( fhf );
        assertEquals("Triangulation", fhf.getTitle());
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("45");
        fhf.lat.setText("40.7128");
        fhf.lon.setText("-74.0060");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, "", new ActionEvent.ActionEvent(new Object(), 0, "", null)));
        assertEquals(1, fhf.points.size());
        assertEquals(1, fhf.fox.size());
        TrigPoint point = (TrigPoint) fhf.points.elementAt(0);
        TrigPoint fox = (TrigPoint) fhf.fox.elementAt(0);
        assertEquals("40.7128", point.getLatitude(), 0.001);
        assertEquals(-74.006, point.getLongitude(), 0.001);
        assertEquals("40.7128", fox.getLatitude(), 0.001);
        assertEquals(-74.006, fox.getLongitude(), 0.001);
        assertEquals(45, fox.getBearingTo(point), 0.1);
    }

    @Test
    public void testAddMultiplePoints() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.theta.setText("45");
        fhf.lat.setText("40.7128");
        fhf.lon.setText("-74.0060");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, "", new ActionEvent.ActionEvent(new Object(), 0, "", null)));
        fhf.theta.setText("135");
        fhf.lat.setText("41.8742");
        fhf.lon.setText("-72.6766");
        fhf.actionPerformed(new ActionEvent(new Object(), 0, "", new ActionEvent.ActionEvent(new Object(), 0, "", null)));
        assertEquals(2, fhf.points.size());
        assertEquals(2, fhf.fox.size());
        TrigPoint point1 = (TrigPoint) fhf.points.elementAt(0);
        TrigPoint point2 = (TrigPoint) fhf.points.elementAt(1);
        TrigPoint fox1 = (TrigPoint) fhf.fox.elementAt(0);
        TrigPoint fox2 = (TrigPoint) fhf.fox.elementAt(1);
        assertEquals("40.7128", point1.getLatitude(), 0.001);
        assertEquals(-74.006, point1.getLongitude(), 0.001);
        assertEquals("41.8742", point2.getLatitude(), 0.001);
        assertEquals(-72.676, point2.getLongitude(), 0.001);
        assertEquals("41.8742", fox1.getLatitude(), 0.001);
        assertEquals(-72.676, fox1.getLongitude(), 0.001);
        assertEquals("40.7128", fox2.getLatitude(), 0.001);
        assertEquals(-74.006, fox2.getLongitude(), 0.001);
        assertEquals(90, fox1.getBearingTo(fox2), 0.1);
        assertEquals(270, fox2.getBearingTo(fox1), 0.1);
    }

    @Test
    public void testFoxTableModel() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        FoxTableModel model = fhf.posModel;
        assertEquals(0, model.getRowCount());
        TrigPoint point1 = new TrigPoint(40.7128, -74.006, 45);
        TrigPoint point2 = new TrigPoint(41.8742, -72.676, 135);
        fhf.points.add(point1);
        fhf.points.add(point2);
        fhf.display.addPoint(point1);
        fhf.display.addPoint(point2);
        assertEquals(2, model.getRowCount());
        Object[] row1 = model.getDataVector().get(0);
        Object[] row2 = model.getDataVector().get(1);
        String[] columns = model.getColumnNames();
        assertEquals("40.7128", row1[0]);
        assertEquals("-74.006", row1[1]);
        assertEquals("41.8742", row2[0]);
        assertEquals("-72.676", row2[1]);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        try {
            fhf.loadLandmarks();
            HuntDisplay display = fhf.display;
            assertEquals(10, display.getLandmarks().size());
            Landmark landmark1 = (Landmark) display.getLandmarks().elementAt(0);
            Landmark landmark2 = (Landmark) display.getLandmarks().elementAt(1);
            String iconFile1 = landmark1.getIcon().getDescription(null);
            String iconFile2 = landmark2.getIcon().getDescription(null);
            assertEquals("landmark1.png", iconFile1);
            assertEquals("landmark2.png", iconFile2);
            double lat1 = landmark1.getLocation().getY();
            double lon1 = landmark1.getLocation().getX();
            double lat2 = landmark2.getLocation().getY();
            double lon2 = landmark2.getLocation().getX();
            assertEquals(40.75, lat1, 0.01);
            assertEquals(-74.10, lon1, 0.01);
            assertEquals(41.80, lat2, 0.01);
            assertEquals(-72.65, lon2, 0.01);
        } catch (FileNotFoundException e) {
            fail("Landmarks file not found");
        } catch (IOException e) {
            fail("Error communicating with the landmarks file");
        }
    }
}