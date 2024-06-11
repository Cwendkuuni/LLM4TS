package jahuwaldt.plot;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testInitialization() {
        PlotAxis axis = new PlotAxis();
        assertNull(axis.getLabel());
        assertNull(axis.getScale());
        assertNull(axis.getNumberFormat());
        assertEquals(1, axis.getGridStyle());
        assertFalse(axis.isManualLB());
        assertFalse(axis.isManualUB());
        assertFalse(axis.isManualQuant());
        assertEquals(1.0, axis.getxA(), 0.01);
        assertEquals(1.0, axis.getxB(), 0.01);
        assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.01);
        assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.01);
    }

    @Test
    public void testConstruction() {
        PlotAxis axis = new PlotAxis("Test Label", new LinearAxisScale(), null, 1);
        assertEquals("Test Label", axis.getLabel());
        assertSame(new LinearAxisScale(), axis.getScale());
        assertNull(axis.getNumberFormat());
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertSame(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        assertSame(nf, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(3);
        assertEquals(3, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.isManualLB());
        axis.manualLowerBounds(false);
        assertFalse(axis.isManualLB());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(5.5);
        assertEquals(5.5, axis.getLowerBounds(), 0.01);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(5.5);
        assertEquals("5.5", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUB());
        axis.manualUpperBounds(false);
        assertFalse(axis.isManualUB());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(10.5);
        assertEquals(10.5, axis.getUpperBounds(), 0.01);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(10.5);
        assertEquals("10.5", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.isManualQuant());
        axis.manualTickStepSize(false);
        assertFalse(axis.isManualQuant());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(2.5);
        assertEquals(2.5, axis.getTickStepSize(), 0.01);
    }
}