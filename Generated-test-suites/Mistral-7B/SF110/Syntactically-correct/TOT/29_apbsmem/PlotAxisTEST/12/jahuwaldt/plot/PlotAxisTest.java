package jahuwaldt.plot;
import org.junit.*;
import static org.junit.Assert.*;

public class PlotAxisTest {

    private PlotAxis axis;

    @Before
    public void setUp() {
        axis = new PlotAxis();
    }

    @Test
    public void testConstructors() {
        // Test default constructor
        PlotAxis axis = new PlotAxis();
        assertNull(axis.getLabel());
        assertNotNull(axis.getScale());
        assertEquals(1, axis.getGridStyle());

        // Test constructor with label, scale, number format, and grid style
        PlotAxis axisWithArgs = new PlotAxis("TestLabel", new LinearAxisScale(), NumberFormat.getInstance(), 2);
        assertEquals("TestLabel", axisWithArgs.getLabel());
        assertSame(new LinearAxisScale(), axisWithArgs.getScale());
        assertEquals(2, axisWithArgs.getGridStyle());
    }

    @Test
    public void testSetLabelAndGetLabel() {
        axis.setLabel("TestLabel");
        assertEquals("TestLabel", axis.getLabel());
    }

    @Test
    public void testSetNullLabel() {
        axis.setLabel(null);
        assertNull(axis.getLabel());
    }

    @Test
    public void testSetGridStyleAndGetGridStyle() {
        axis.setGridStyle(3);
        assertEquals(3, axis.getGridStyle());
    }

    @Test
    public void testSetNegativeGridStyle() {
        axis.setGridStyle(-1);
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testSetLowerBoundsAndGetLowerBounds() {
        axis.setLowerBounds(10.5);
        assertEquals(10.5, axis.getLowerBounds(), 0.001);
    }

    @Test
    public void testSetNegativeLowerBounds() {
        axis.setLowerBounds(-5.5);
        assertEquals(Double.MIN_VALUE, axis.getLowerBounds(), 0.001);
    }

    @Test
    public void testSetUpperBoundsAndGetUpperBounds() {
        axis.setUpperBounds(20.5);
        assertEquals(20.5, axis.getUpperBounds(), 0.001);
    }

    @Test
    public void testSetNegativeUpperBounds() {
        axis.setUpperBounds(-15.5);
        assertEquals(Double.MAX_VALUE, axis.getUpperBounds(), 0.001);
    }

    @Test
    public void testSetTickStepSizeAndGetTickStepSize() {
        axis.setTickStepSize(2.5);
        assertEquals(2.5, axis.getTickStepSize(), 0.001);
    }

    @Test
    public void testSetNegativeTickStepSize() {
        axis.setTickStepSize(-1.5);
        assertEquals(1.0, axis.getTickStepSize(), 0.001);
    }

    @Test
    public void testSetScaleAndGetScale() {
        axis.setScale(new LogAxisScale());
        assertSame(new LogAxisScale(), axis.getScale());
    }

    @Test
    public void testSetNullScale() {
        axis.setScale(null);
        assertNull(axis.getScale());
    }
}