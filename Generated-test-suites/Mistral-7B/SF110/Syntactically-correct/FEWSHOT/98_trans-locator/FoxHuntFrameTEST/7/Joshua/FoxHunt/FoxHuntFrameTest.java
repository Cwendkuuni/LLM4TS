package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

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
    private DefaultTableModel tableModel;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
        tableModel = (FoxTableModel) frame.posModel;
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(frame::dispose);
    }

    @Test
    public void testConstructor() {
        assertNotNull(frame.getTitle());
        assertEquals("Triangulation", frame.getTitle());
    }

    @Test
    public void testActionPerformed() {
        frame.theta.setText("123");
        frame.lat.setText("45.123456");
        frame.lon.setText("78.123456");
        SwingUtilities.invokeLater(() -> frame.add.doClick());

        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(TrigPoint.class, frame.points.elementAt(0).getClass());
        assertEquals(TrigPoint.class, frame.fox.elementAt(0).getClass());

        TrigPoint point = (TrigPoint) frame.points.elementAt(0);
        TrigPoint fox = (TrigPoint) frame.fox.elementAt(0);

        assertEquals("45.123456", point.getY(), 0.001);
        assertEquals("78.123456", point.getX(), 0.001);
        assertEquals("45.123456", fox.getY(), 0.001);
        assertEquals("78.123456", fox.getX(), 0.001);

        assertEquals(2, tableModel.getRowCount());
        assertEquals("45.123456", tableModel.getValueAt(0, 0));
        assertEquals("78.123456", tableModel.getValueAt(0, 1));
        assertEquals("45.123456", tableModel.getValueAt(1, 0));
        assertEquals("78.123456", tableModel.getValueAt(1, 1));
    }

    @Test
    public void testLoadLandmarks() throws Exception {
        assertFalse(frame.display.size() > 0);

        SwingUtilities.invokeLater(() -> frame.loadLandmarks());

        assertTrue(frame.display.size() > 0);

        Landmark landmark = frame.display.elementAt(0);
        BufferedImage image = ImageIO.read(new File("icon.gif"));
        assertEquals(image, landmark.getIcon());

        TrigPoint point = (TrigPoint) frame.points.elementAt(0);
        double lat = point.getY();
        double lon = point.getX();
        Landmark landmarkFromTable = null;

        for (Object obj : frame.display) {
            Landmark landmarkFromDisplay = (Landmark) obj;
            if (Math.abs(landmarkFromDisplay.getLatitude() - lat) < 0.001 && Math.abs(landmarkFromDisplay.getLongitude() - lon) < 0.001) {
                landmarkFromTable = landmarkFromDisplay;
                break;
            }
        }

        assertEquals(landmarkFromTable, landmark);
    }

    @Test
    public void testErrorDialog() {
        EventQueue.invokeLater(() -> frame.error("Test Error"));

        JFrame errorDialog = frame.getFrame();
        assertNotNull(errorDialog);
        assertEquals("Error", errorDialog.getTitle());
        String message = (String) errorDialog.getContentPane().getComponent(0).getComponent(0).getText();
        assertEquals("Test Error", message);

        Object[] options = ((JOptionPane) errorDialog).getOptions();
        assertEquals(2, options.length);
        assertEquals("Abort", options[0]);
        assertEquals("Continue", options[1]);
    }
}