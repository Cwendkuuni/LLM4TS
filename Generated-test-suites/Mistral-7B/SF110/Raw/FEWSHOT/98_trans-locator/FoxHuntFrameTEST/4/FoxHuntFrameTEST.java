package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.FoxHuntFrame;
import javax.swing.table.DefaultTableModel;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrame() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        assertNotNull(foxHuntFrame);
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("51.5074");
        foxHuntFrame.lon.setText("0.1278");
        foxHuntFrame.actionPerformed(new ActionEvent(foxHuntFrame.add, ActionEvent.ACTION_PERFORMED, null));
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
        foxHuntFrame.theta.setText("78");
        foxHuntFrame.lat.setText("51.5167");
        foxHuntFrame.lon.setText("0.1283");
        foxHuntFrame.actionPerformed(new ActionEvent(foxHuntFrame.add, ActionEvent.ACTION_PERFORMED, null));
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.fox.size());
    }

    @Test
    public void testFoxTableModel() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        FoxTableModel tableModel = foxHuntFrame.posModel;
        assertEquals(2, tableModel.getRowCount());
        assertEquals(2, tableModel.getColumnCount());
        assertEquals("Lattitude", tableModel.getColumnName(0));
        assertEquals("Longitude", tableModel.getColumnName(1));
        Object value = tableModel.getValueAt(0, 0);
        assertTrue(value instanceof String);
        double expectedLat = 51.5074;
        assertEquals(expectedLat, Double.parseDouble(value.toString()));
        value = tableModel.getValueAt(0, 1);
        assertTrue(value instanceof String);
        double expectedLon = 0.1278;
        assertEquals(expectedLon, Double.parseDouble(value.toString()));
        value = tableModel.getValueAt(1, 0);
        assertTrue(value instanceof String);
        double expectedNewLat = 51.5167;
        assertEquals(expectedNewLat, Double.parseDouble(value.toString()));
        value = tableModel.getValueAt(1, 1);
        assertTrue(value instanceof String);
        double expectedNewLon = 0.1283;
        assertEquals(expectedNewLon, Double.parseDouble(value.toString()));
    }
}