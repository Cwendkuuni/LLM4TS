package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import java.util.*;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrame() {
        FoxHuntFrame fh = new FoxHuntFrame();
        assertNotNull(fh.panel);
        //Add more assertions to test other components of the frame
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame fh = new FoxHuntFrame();
        JTextField theta = fh.theta;
        JTextField lat = fh.lat;
        JTextField lon = fh.lon;
        theta.setText("123");
        lat.setText("45.123456");
        lon.setText("78.123456");
        fh.actionPerformed(new ActionEvent(fh.add, ActionEvent.ACTION_PERFORMED, ""));
        //Add assertions to test the state of the frame components after adding a point
        FoxTableModel ftm = fh.posModel;
        int rows = ftm.getRowCount();
        assertEquals("There should be one row in the table after adding a point", 1, rows);
        //Add more assertions to test the state of the table after adding a point
    }

    @Test
    public void testFoxTableModel() {
        FoxTableModel ftm = new FoxTableModel();
        String[] colNames = ftm.colNames;
        assertEquals("The number of columns in the table model should be 2", 2, colNames.length);
        Object value = ftm.getValueAt(0, 0);
        assertNotNull("The value of the first cell in the first row should not be null", value);
        //Add more assertions to test the values in the table model
    }
}