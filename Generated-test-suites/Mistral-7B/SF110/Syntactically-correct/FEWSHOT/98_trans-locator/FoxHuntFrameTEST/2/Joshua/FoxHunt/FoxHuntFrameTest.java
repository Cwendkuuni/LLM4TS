package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrame() {
        FoxHuntFrame fxf = new FoxHuntFrame();
        assertNotNull(fxf);
    }

    @Test
    public void testTextPrep() {
        FoxHuntFrame fxf = new FoxHuntFrame();
        assertNotNull(fxf.theta);
        assertNotNull(fxf.lat);
        assertNotNull(fxf.lon);
        assertNotNull(fxf.add);
    }

    @Test
    public void testTablePrep() {
        FoxHuntFrame fxf = new FoxHuntFrame();
        assertNotNull(fxf.posModel);
        assertNotNull(fxf.pos);
        assertNotNull(fxf.posModel.getColumnName(0));
        assertNotNull(fxf.posModel.getColumnName(1));
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame fxf = new FoxHuntFrame();
        fxf.theta.setText("45");
        fxf.lat.setText("40.7128");
        fxf.lon.setText("-74.0060");
        fxf.add.doClick();
        assertEquals(1, fxf.points.size());
        assertEquals(1, fxf.fox.size());
    }

    @Test
    public void testAddMultiplePoints() {
        FoxHuntFrame fxf = new FoxHuntFrame();
        fxf.theta.setText("45");
        fxf.lat.setText("40.7128");
        fxf.lon.setText("-74.0060");
        fxf.add.doClick();
        fxf.theta.setText("35");
        fxf.lat.setText("40.7145");
        fxf.lon.setText("-74.0065");
        fxf.add.doClick();
        assertEquals(2, fxf.points.size());
        assertEquals(2, fxf.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame fxf = new FoxHuntFrame();
        fxf.loadLandmarks();
        assertTrue(fxf.display.getLandmarkCount() > 0);
    }

    @Test
    public void testErrorDialog() {
        FoxHuntFrame fxf = new FoxHuntFrame();
        fxf.error("Test Error");
    }

    @Test
    public void testFoxTableModel() {
        FoxHuntFrame fxf = new FoxHuntFrame();
        FoxTableModel model = fxf.posModel;
        assertNotNull(model);
        assertEquals(2, model.getColumnCount());
        assertEquals(0, model.getRowCount());
        String[] columnNames = model.getColumnNames();
        assertEquals(2, columnNames.length);
        assertEquals("Lattitude", columnNames[0]);
        assertEquals("Longitude", columnNames[1]);
    }
}