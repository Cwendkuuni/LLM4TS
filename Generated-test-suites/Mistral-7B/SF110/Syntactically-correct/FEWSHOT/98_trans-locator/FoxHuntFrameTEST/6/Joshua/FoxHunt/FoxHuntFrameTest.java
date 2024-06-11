package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    private FoxHuntFrame frame;
    private JTable table;
    private AbstractTableModel model;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
        table = frame.pos;
        model = frame.posModel;
    }

    @After
    public void tearDown() {
        frame.dispose();
    }

    @Test
    public void testFoxHuntFrameConstructor() {
        assertEquals("Triangulation", frame.getTitle());
        assertEquals(3, frame.getDefaultCloseOperation());
        assertNotNull(frame.getIconImage());
    }

    @Test
    public void testAddPoint() {
        frame.theta.setText("123");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.0828");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!"));

        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());

        TrigPoint point = (TrigPoint) frame.points.lastElement();
        assertEquals("51.5074", point.getLatitude());
        assertEquals("-0.0828", point.getLongitude());
        assertEquals(123, point.getAngle());

        TrigPoint fox = (TrigPoint) frame.fox.lastElement();
        assertEquals("Invalid Latitude", fox.getLatitude(), 0.0, 0.0001);
        assertEquals("Invalid Longitude", fox.getLongitude(), 0.0, 0.0001);
        assertEquals(123, fox.getAngle());

        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());

        TrigPoint previousPoint = (TrigPoint) frame.points.elementAt(frame.points.size() - 2);
        TrigPoint previousFox = (TrigPoint) frame.fox.elementAt(frame.fox.size() - 2);
        assertEquals("51.5074", previousPoint.getLatitude());
        assertEquals("-0.0828", previousPoint.getLongitude());
        assertEquals(123, previousPoint.getAngle());

        assertEquals("51.50739605063545", fox.getLatitude(), 0.0);
        assertEquals("-0.08281416179367326", fox.getLongitude(), 0.0);
        assertEquals(123, fox.getAngle());

        frame.theta.setText("56");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.0828");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!"));

        assertEquals(3, frame.points.size());
        assertEquals(3, frame.fox.size());

        TrigPoint newPoint = (TrigPoint) frame.points.lastElement();
        assertEquals("51.5074", newPoint.getLatitude());
        assertEquals("-0.0828", newPoint.getLongitude());
        assertEquals(56, newPoint.getAngle());

        TrigPoint newFox = (TrigPoint) frame.fox.lastElement();
        assertEquals("Invalid Latitude", newFox.getLatitude(), 0.0, 0.0001);
        assertEquals("Invalid Longitude", newFox.getLongitude(), 0.0, 0.0001);
        assertEquals(56, newFox.getAngle());
    }

    @Test
    public void testTableModel() {
        frame.theta.setText("123");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.0828");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!"));

        Object[] pointData = { frame.points.lastElement() };
        String[] columnNames = { "Lattitude", "Longitude" };
        DefaultTableModel expectedModel = new DefaultTableModel(pointData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        assertEquals(expectedModel, model);

        assertEquals(1, table.getRowCount());
        assertEquals("51.5074", table.getValueAt(0, 0).toString());
        assertEquals("-0.0828", table.getValueAt(0, 1).toString());

        frame.theta.setText("56");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.0828");
        frame.actionPerformed(new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "Add It!!!"));

        Object[] newPointData = { frame.points.lastElement() };
        assertEquals(new DefaultTableModel(newPointData, columnNames), model);
        assertEquals(2, table.getRowCount());
        assertEquals("51.5074", table.getValueAt(0, 0).toString());
        assertEquals("-0.0828", table.getValueAt(0, 1).toString());
        assertEquals("51.50739605063545", table.getValueAt(1, 0).toString());
        assertEquals("-0.08281416179367326", table.getValueAt(1, 1).toString());
    }

    @Test
    public void testLoadLandmarks() {
        final int NUM = 1;
        String[] expectedIconFile = new String[NUM];
        double[] lat = new double[NUM];
        double[] lon = new double[NUM];

        frame.loadLandmarks();

        assertEquals(NUM, frame.display.getLandMarkCount());

        Landmark landmark = frame.display.getLandMarkAt(0);
        assertEquals("Invalid Icon File", landmark.getIcon(), expectedIconFile[0]);
        assertEquals("Invalid Latitude", landmark.getLatitude(), lat[0], 0.0001);
        assertEquals("Invalid Longitude", landmark.getLongitude(), lon[0], 0.0001);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundException() {
        frame = new FoxHuntFrame();
        frame.loadLandmarks();
    }

    @Test(expected = IOException.class)
    public void testIOException() {
        frame = new FoxHuntFrame();
        String filename = "./landmarks.cfg";
        final File file = new File(filename);
        final FileInputStream inFile = new FileInputStream(file);
        final BufferedReader in = new BufferedReader(new InputStreamReader(inFile));
        String input = "";

        try {
            frame.loadLandmarks();
        } finally {
            in.close();
            inFile.close();
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatException() {
        frame = new FoxHuntFrame();
        frame.loadLandmarks();
        Frame mockFrame = mock(FoxHuntFrame.class);
        when(mockFrame.error).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new NumberFormatException();
            }
        });
        doReturn(mockFrame).when(frame).display;

        frame.loadLandmarks();
    }

    @Test
    public void testErrorDialog() {
        frame = new FoxHuntFrame();
        Frame mockFrame = mock(FoxHuntFrame.class);
        when(mockFrame.error).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                JOptionPane.showOptionDialog(frame, "Error", "Error", 0, 0, null, new Object[] { "Abort", "Continue" }, 0);
                return new Object[0][];
            }
        });
        doReturn(mockFrame).when(frame).display;

        try {
            frame.loadLandmarks();
            fail("Expected an exception");
        } catch (Exception e) {
            // Expected
        }
    }
}