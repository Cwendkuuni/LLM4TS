package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

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

    @Before
    public void setUp() {
        SwingUtilities.invokeLater(() -> {
            frame = new FoxHuntFrame();
        });
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> {
            if (frame != null) {
                frame.dispose();
            }
        });
    }

    @Test
    public void testFoxHuntFrameConstructor() {
        assertEquals("Triangulation", frame.getTitle());
        assertTrue(frame.isVisible());
        assertEquals(3, frame.getDefaultCloseOperation());
        assertNotNull(frame.getIconImage());
    }

    @Test
    public void testAddPoint() {
        SwingUtilities.invokeLater(() -> {
            frame.theta.setText("45");
            frame.lat.setText("40.7128");
            frame.lon.setText("-74.0060");
            frame.add.doClick();
        });

        final Vector points = frame.points;
        final Vector fox = frame.fox;
        assertEquals(1, points.size());
        assertEquals(1, fox.size());

        final TrigPoint point = (TrigPoint) points.lastElement();
        assertEquals(40.7128, point.getY(), 0.001);
        assertEquals(-74.006, point.getX(), 0.001);
        assertEquals(45, point.getAngle());

        final TrigPoint foxPoint = (TrigPoint) fox.lastElement();
        assertEquals(0.0, foxPoint.getY(), 0.001);
        assertEquals(0.0, foxPoint.getX(), 0.001);
        assertEquals(0.0, foxPoint.getAngle());
    }

    @Test
    public void testAddMultiplePoints() {
        SwingUtilities.invokeLater(() -> {
            frame.theta.setText("45");
            frame.lat.setText("40.7128");
            frame.lon.setText("-74.0060");
            frame.add.doClick();

            frame.theta.setText("30");
            frame.lat.setText("37.7749");
            frame.lon.setText("-122.4194");
            frame.add.doClick();
        });

        final Vector points = frame.points;
        final Vector fox = frame.fox;
        assertEquals(2, points.size());
        assertEquals(2, fox.size());

        final TrigPoint firstPoint = (TrigPoint) points.elementAt(0);
        assertEquals(40.7128, firstPoint.getY(), 0.001);
        assertEquals(-74.006, firstPoint.getX(), 0.001);
        assertEquals(45, firstPoint.getAngle());

        final TrigPoint secondPoint = (TrigPoint) points.elementAt(1);
        assertEquals(37.7749, secondPoint.getY(), 0.001);
        assertEquals(-122.419, secondPoint.getX(), 0.001);
        assertEquals(30, secondPoint.getAngle());

        final TrigPoint firstFoxPoint = (TrigPoint) fox.elementAt(0);
        assertEquals(0.0, firstFoxPoint.getY(), 0.001);
        assertEquals(0.0, firstFoxPoint.getX(), 0.001);
        assertEquals(0.0, firstFoxPoint.getAngle());

        final TrigPoint secondFoxPoint = (TrigPoint) fox.elementAt(1);
        assertNotEquals(firstFoxPoint, secondFoxPoint);
        FoxCalcTest.assertFoxPoint(secondFoxPoint, firstPoint, secondPoint);
    }

    @Test
    public void testLoadLandmarks() {
        SwingUtilities.invokeLater(() -> {
            try {
                frame.loadLandmarks();
            } catch (FileNotFoundException e) {
                fail("Expected no exception, but got: " + e.getMessage());
            } catch (IOException e) {
                fail("Expected no exception, but got: " + e.getMessage());
            }
        });

        final HuntDisplay display = frame.display;
        assertEquals(0, display.getLandmarkCount());

        SwingUtilities.invokeLater(() -> {
            frame.loadLandmarks();
        });

        final HuntDisplay updatedDisplay = frame.display;
        assertEquals(5, updatedDisplay.getLandmarkCount());
    }

    @Test
    public void testFoxTableModel() {
        final FoxTableModel model = new FoxTableModel();
        assertEquals(2, model.getColumnCount());
        assertEquals(0, model.getRowCount());

        final TrigPoint point = new TrigPoint(1.0, 2.0, 30);
        model.points = new Vector() {{
            add(point);
        }};

        SwingUtilities.invokeLater(() -> {
            frame.posModel = model;
        });

        SwingUtilities.invokeLater(() -> {
            frame.pos.setModel(model);
        });

        final JTable table = frame.pos;
        assertEquals(1, table.getRowCount());

        final Object[] values = table.getValueAt(0, 0).toString().split("\\s+");
        assertEquals("###.##############", values[0]);
        assertEquals("2.0", values[1]);

        final Object[] values1 = table.getValueAt(0, 1).toString().split("\\s+");
        assertEquals("###.##############", values1[0]);
        assertEquals("1.0", values1[1]);
    }
}