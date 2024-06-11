package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testPlotAxisDefaultConstruction() {
        PlotAxis axis = new PlotAxis();
        assertNull(axis.getLabel());
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
        assertEquals(1, axis.gridStyle);
        assertNull(axis.scale);
        assertNotNull(axis.nf);
    }

    @Test
    public void testPlotAxisCustomConstruction() {
        String label = "TestAxis";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat nf = NumberFormat.getInstance();
        int gridStyle = 2;
        PlotAxis axis = new PlotAxis(label, scale, nf, gridStyle);
        assertEquals(label, axis.getLabel());
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
        assertEquals(2, axis.gridStyle);
        assertEquals(scale, axis.scale);
        assertNotNull(axis.nf);
    }

    @Test
    public void testSetAndGetLabel() {
        PlotAxis axis = new PlotAxis();
        String label = "TestAxis";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }

    @Test
    public void testSetAndGetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetAndGetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        assertEquals(nf, axis.getNumberFormat());
    }

    @Test
    public void testSetAndGetGridStyle() {
        PlotAxis axis = new PlotAxis();
        int gridStyle = 3;
        axis.setGridStyle(gridStyle);
        assertEquals(gridStyle, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.manualLB);
        axis.manualLowerBounds(false);
        assertFalse(axis.manualLB);
    }

    @Test
    public void testSetAndGetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        double lowerBounds = 0.0;
        axis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        double lowerBounds = 5.5;
        axis.setLowerBounds(lowerBounds);
        assertEquals("5.5", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.manualUB);
        axis.manualUpperBounds(false);
        assertFalse(axis.manualUB);
    }

    @Test
    public void testSetAndGetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        double upperBounds = 10.0;
        axis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        double upperBounds = 7.5;
        axis.setUpperBounds(upperBounds);
        assertEquals("7.5", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.manualQuant);
        axis.manualTickStepSize(false);
        assertFalse(axis.manualQuant);
    }

    @Test
    public void testSetAndGetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        double tickStepSize = 2.5;
        axis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, axis.getTickStepSize(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(-1.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList plotRunList = new PlotRunList();
        axis.setAxisBounds(plotRunList);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(10, 10, 200, 300);
        axis.setSize(plotFrame);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 5.0;
        int expected = 25;
        int actual = axis.scaleCoord(x);
        assertEquals(expected, actual);
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = axis.clone();
        assertNotSame(axis, clonedAxis);
        assertEquals(axis, clonedAxis);
    }
}