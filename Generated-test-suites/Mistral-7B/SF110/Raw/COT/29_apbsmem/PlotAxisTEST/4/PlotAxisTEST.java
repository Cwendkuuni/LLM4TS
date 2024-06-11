package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

    @Test
    public void testPlotAxisConstructor() {
        PlotAxis axis = new PlotAxis();
        assertNotNull(axis);

        PlotAxis axisWithLabel = new PlotAxis("TestAxis", new PlotAxisScale(), null, 1);
        assertNotNull(axisWithLabel);
        assertEquals("TestAxis", axisWithLabel.getLabel());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("TestLabel");
        assertEquals("TestLabel", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new PlotAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        DecimalFormat df = new DecimalFormat("#.##");
        axis.setNumberFormat(df);
        assertEquals(df, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
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
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getLowerBounds(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLowerBoundsNegativeValue() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(-1.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        DecimalFormat df = new DecimalFormat("#.##");
        axis.setNumberFormat(df);
        axis.setLowerBounds(1.23);
        assertEquals("1.23", axis.lowerBoundsAsString());
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
        axis.setUpperBounds(2.0);
        assertEquals(2.0, axis.getUpperBounds(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUpperBoundsNegativeValue() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(-1.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        DecimalFormat df = new DecimalFormat("#.##");
        axis.setNumberFormat(df);
        axis.setUpperBounds(3.45);
        assertEquals("3.45", axis.upperBoundsAsString());
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
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.01);
    }

    @Test
    public void testSetAxisBounds() {
        // Abstract method, cannot be tested directly
    }

    @Test
    public void testSetSize() {
        // Abstract method, cannot be tested directly
    }

    @Test
    public void testSetAxisTickLimits() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(-1.0);
        axis.setUpperBounds(1.0);
        axis.setGridStyle(PlotAxis.kTickMarksOnly);

        Rectangle plotFrame = new Rectangle(0, 0, 100, 100);
        axis.setSize(plotFrame);

        assertEquals(0, axis.getLowerBounds(), 0.01);
        assertEquals(100, axis.getUpperBounds(), 0.01);
    }

    @Test
    public void testDraw() {
        // Cannot create a test for this abstract method without a concrete implementation
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(0.0);
        axis.setUpperBounds(1.0);

        assertEquals(0, axis.scaleCoord(0.0), 0);
        assertEquals(50, axis.scaleCoord(0.25), 0);
        assertEquals(100, axis.scaleCoord(1.0), 0);
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
        assertNotSame(axis, clonedAxis);
        assertEquals(axis.getLabel(), clonedAxis.getLabel());
        assertEquals(axis.getScale(), clonedAxis.getScale());
        assertEquals(axis.getNumberFormat(), clonedAxis.getNumberFormat());
        assertEquals(axis.getGridStyle(), clonedAxis.getGridStyle());
    }
}