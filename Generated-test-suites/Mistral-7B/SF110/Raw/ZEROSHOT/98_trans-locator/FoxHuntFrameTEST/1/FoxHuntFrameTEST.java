package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest
{
    private FoxHuntFrame frame;
    private FoxTableModel posModel;

    @Before
    public void setUp()
    {
        frame = new FoxHuntFrame();
        posModel = frame.posModel;
    }

    @Test
    public void testConstructor()
    {
        assertNotNull(frame);
        assertEquals("Triangulation", frame.getTitle());
    }

    @Test
    public void testActionPerformed()
    {
        ActionEvent event = new ActionEvent(new Object(), 0, "ActionEvent");
        frame.actionPerformed(event);
        assertEquals(1, frame.points.size());
        assertEquals(0, frame.fox.size());
    }

    @Test
    public void testAddPoint()
    {
        frame.theta.setText("123");
        frame.lat.setText("45.123456");
        frame.lon.setText("78.123456");
        frame.actionPerformed(new ActionEvent(new Object(), 0, "ActionEvent"));
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        TrigPoint expectedPoint = new TrigPoint(45.123456, 78.123456, 123);
        TrigPoint actualPoint = (TrigPoint) frame.points.lastElement();
        assertEquals(expectedPoint.getX(), actualPoint.getX(), 0.0001);
        assertEquals(expectedPoint.getY(), actualPoint.getY(), 0.0001);
        assertEquals(expectedPoint.getAngle(), actualPoint.getAngle(), 0.001);

        TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, (TrigPoint) frame.points.elementAt(0));
        TrigPoint actualFox = (TrigPoint) frame.fox.lastElement();
        assertEquals(expectedFox.getX(), actualFox.getX(), 0.0001);
        assertEquals(expectedFox.getY(), actualFox.getY(), 0.0001);

        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());
    }

    @Test
    public void testFinalPrep()
    {
        assertNotNull(frame.panel1);
        assertNotNull(frame.panel2);
        assertNotNull(frame.panel3);
        assertNotNull(frame.pos);
        assertNotNull(frame.display);
    }

    @Test
    public void testTextPrep()
    {
        assertNotNull(frame.theta);
        assertNotNull(frame.lat);
        assertNotNull(frame.lon);
        assertNotNull(frame.add);
        assertEquals("Theta in Degrees", frame.panel1.getComponent(0));
        assertEquals("Add It!!!", frame.panel1.getComponent(3));
    }

    @Test
    public void testTablePrep()
    {
        assertNotNull(posModel);
        assertNotNull(frame.pos);
        assertEquals("Lattitude", posModel.getColumnName(0));
        assertEquals("Longitude", posModel.getColumnName(1));
    }

    @Test
    public void testDisplayPrep()
    {
        try
        {
            frame.loadLandmarks();
            assertTrue(frame.display.getLandMarkCount() > 0);
        }
        catch (Exception e)
        {
            fail("Failed to load landmarks: " + e.getMessage());
        }
    }

    @Test
    public void testFoxTableModel()
    {
        assertEquals(2, posModel.getRowCount());
        assertEquals("Lattitude", posModel.getColumnName(0));
        assertEquals("Longitude", posModel.getColumnName(1));

        Object value = posModel.getValueAt(0, 0);
        DecimalFormat formatter = new DecimalFormat("###.##############");
        assertEquals("45.123456", formatter.format(value));

        value = posModel.getValueAt(0, 1);
        assertEquals("78.123456", formatter.format(value));

        value = posModel.getValueAt(1, 0);
        assertEquals("51.507351", formatter.format(value));

        value = posModel.getValueAt(1, 1);
        assertEquals("-0.089676", formatter.format(value));
    }
}