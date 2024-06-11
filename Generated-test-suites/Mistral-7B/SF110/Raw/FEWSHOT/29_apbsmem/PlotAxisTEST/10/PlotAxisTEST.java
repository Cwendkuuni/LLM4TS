package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testInitialization() {
        PlotAxis axis = new PlotAxis();
        assertNull(axis.getLabel());
        assertNotNull(axis.getScale());
        assertNotNull(axis.getNumberFormat());
        assertEquals(1.0, axis.getXRangemin(), 0.0);
        assertEquals(1.0, axis.getXRangemax(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALowerBounds(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUpperBounds(), 0.0);
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testInitializationWithLabelScaleFormatGridStyle() {
        String label = "TestLabel";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat nf = NumberFormat.getInstance();
        int gridStyle = 1;
        PlotAxis axis = new PlotAxis(label, scale, nf, gridStyle);
        assertEquals(label, axis.getLabel());
        assertSame(scale, axis.getScale());
        assertSame(nf, axis.getNumberFormat());
        assertEquals(1.0, axis.getXRangemin(), 0.0);
        assertEquals(1.0, axis.getXRangemax(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALowerBounds(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUpperBounds(), 0.0);
        assertEquals(gridStyle, axis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        String label = "TestLabel";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
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
        int gridStyle = 2;
        axis.setGridStyle(gridStyle);
        assertEquals(gridStyle, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.isManualLowerBounds());
        axis.manualLowerBounds(false);
        assertFalse(axis.isManualLowerBounds());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        double lowerBounds = 0.5;
        axis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        double lowerBounds = 0.5;
        axis.setLowerBounds(lowerBounds);
        assertEquals("0.5", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUpperBounds());
        axis.manualUpperBounds(false);
        assertFalse(axis.isManualUpperBounds());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        double upperBounds = 1.5;
        axis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        double upperBounds = 1.5;
        axis.setUpperBounds(upperBounds);
        assertEquals("1.5", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.isManualTickStepSize());
        axis.manualTickStepSize(false);
        assertFalse(axis.isManualTickStepSize());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        double tickStepSize = 0.1;
        axis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, axis.getTickStepSize(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(-0.1);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList p0 = new PlotRunList();
        axis.setAxisBounds(p0);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(10, 10, 200, 200);
        axis.setSize(plotFrame);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 0.5;
        int expected = (int)(axis.getXRangemin() * 0.5 + axis.getXRangemax());
        int result = axis.scaleCoord(x);
        assertEquals(expected, result);
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = axis.clone();
        assertNotSame(axis, clonedAxis);
        assertEquals(axis.getLabel(), clonedAxis.getLabel());
        assertEquals(axis.getScale(), clonedAxis.getScale());
        assertEquals(axis.getNumberFormat(), clonedAxis.getNumberFormat());
        assertEquals(axis.getGridStyle(), clonedAxis.getGridStyle());
        assertEquals(axis.isManualLowerBounds(), clonedAxis.isManualLowerBounds());
        assertEquals(axis.getALowerBounds(), clonedAxis.getALowerBounds(), 0.0);
        assertEquals(axis.isManualUpperBounds(), clonedAxis.isManualUpperBounds());
        assertEquals(axis.getAUpperBounds(), clonedAxis.getAUpperBounds(), 0.0);
        assertEquals(axis.isManualTickStepSize(), clonedAxis.isManualTickStepSize());
        assertEquals(axis.getTickStepSize(), clonedAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testFindGoodLimits() {
        PlotAxisScale scale = new LinearAxisScale();
        double lb = 0.0;
        double ub = 1.0;
        AxisLimitData limData = scale.findGoodLimits(lb, ub);
        assertEquals(lb, limData.lb, 0.0);
        assertEquals(ub, limData.ub, 0.0);
        assertEquals(0.1, limData.quantum, 0.0);
    }
}