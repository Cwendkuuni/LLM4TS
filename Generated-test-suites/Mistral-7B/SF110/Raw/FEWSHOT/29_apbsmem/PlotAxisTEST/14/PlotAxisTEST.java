package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testDefaultConstruction() {
        PlotAxis axis = new PlotAxis();
        assertNull(axis.getLabel());
        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
        assertEquals(1, axis.getGridStyle());
        assertNotNull(axis.getScale());
        assertNotNull(axis.getNumberFormat());
    }

    @Test
    public void testCustomConstruction() {
        String label = "Test Label";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat nf = NumberFormat.getInstance();
        int gridStyle = 2;
        PlotAxis axis = new PlotAxis(label, scale, nf, gridStyle);
        assertEquals(label, axis.getLabel());
        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
        assertEquals(gridStyle, axis.getGridStyle());
        assertEquals(scale, axis.getScale());
        assertEquals(nf, axis.getNumberFormat());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        assertEquals(nf, axis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        assertEquals(nf, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.isManualLB());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getALB(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getALB(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.0);
        NumberFormat nf = NumberFormat.getInstance();
        assertEquals("1.0", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUB());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(2.0);
        assertEquals(2.0, axis.getAUB(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(2.0);
        assertEquals(2.0, axis.getAUB(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(2.0);
        NumberFormat nf = NumberFormat.getInstance();
        assertEquals("2.0", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.isManualQuant());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getQuantum(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getQuantum(), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setAxisBounds(null);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(10, 10, 20, 20);
        axis.setSize(plotFrame);
        // The implementation of setAxisTickLimits() and resizeAxis() is missing.
        // Thus, we cannot test the behavior of these methods here.
    }
}