package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.FoxHuntFrame;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrame() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame);
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("51.506961");
        frame.lon.setText("-0.127782");
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertTrue(frame.display.getLandmarkSize() > 0);
    }

    @Test
    public void testTableModel() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxTableModel tableModel = frame.posModel;
        assertEquals(2, tableModel.getColumnCount());
        assertEquals(1, tableModel.getRowCount());
        assertEquals("Lattitude", tableModel.getColumnName(0));
        assertEquals("Longitude", tableModel.getColumnName(1));
        assertEquals("51.506961", tableModel.getValueAt(0, 0).toString());
        assertEquals("-0.127782", tableModel.getValueAt(0, 1).toString());
    }
}