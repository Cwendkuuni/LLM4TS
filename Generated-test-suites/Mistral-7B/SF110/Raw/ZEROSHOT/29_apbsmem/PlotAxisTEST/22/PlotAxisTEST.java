package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

    @Test
    public void testPlotAxisConstructor() {
        PlotAxis axis = new PlotAxis();
        assertNotNull(axis);
        assertEquals("Axis label is not initialized", "", axis.getLabel());
        assertEquals("Scale is not initialized", PlotAxisScale.class, axis.getScale().getClass());
        assertEquals("Number format is not initialized", NumberFormat.class, axis.getNumberFormat().getClass());
        assertEquals("Grid style is not initialized", 1, axis.getGridStyle());
    }

    @Test
    public void testPlotAxisWithLabelConstructor() {
        PlotAxis axis = new PlotAxis("Test Label", new PlotAxisScale(), null, 1);
        assertNotNull(axis);
        assertEquals("Axis label is not set", "Test Label", axis.getLabel());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Test Label");
        assertEquals("Axis label is not set", "Test Label", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LogAxisScale();
        axis.setScale(scale);
        assertEquals("Scale is not set", scale.getClass(), axis.getScale().getClass());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        DecimalFormat format = new DecimalFormat("#0.00");
        axis.setNumberFormat(format);
        assertEquals("Number format is not set", format.getClass(), axis.getNumberFormat().getClass());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(3);
        assertEquals("Grid style is not set", 3, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertTrue("Manual lower bounds is not set to true", axis.isManualLowerBounds());

        axis.manualLowerBounds(false);
        assertFalse("Manual lower bounds is not set to false", axis.isManualLowerBounds());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(10.5);
        assertEquals("Lower bounds is not set", 10.5, axis.getLowerBounds(), 0.001);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        DecimalFormat format = new DecimalFormat("#0.00");
        axis.setLowerBounds(10.5);
        assertEquals("Lower bounds string is not generated correctly", "10.50", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue("Manual upper bounds is not set to true", axis.isManualUpperBounds());

        axis.manualUpperBounds(false);
        assertFalse("Manual upper bounds is not set to false", axis.isManualUpperBounds());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(20.7);
        assertEquals("Upper bounds is not set", 20.7, axis.getUpperBounds(), 0.001);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        DecimalFormat format = new DecimalFormat("#0.00");
        axis.setUpperBounds(20.7);
        assertEquals("Upper bounds string is not generated correctly", "20.70", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue("Manual tick step size is not set to true", axis.isManualTickStepSize());

        axis.manualTickStepSize(false);
        assertFalse("Manual tick step size is not set to false", axis.isManualTickStepSize());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(0.5);
        assertEquals("Tick step size is not set", 0.5, axis.getTickStepSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(-0.5);
    }

    @Test
    public void testDraw() {
        PlotAxis axis = new PlotAxis();
        Graphics g = new Graphics() {};
        Component c = new Component() {};
        Rectangle rect = new Rectangle(0, 0, 100, 100);
        axis.draw(g, c, rect);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new LinearAxis();
        axis.setAxisBounds(null);
        assertEquals("Scale coordinate is not calculated correctly", 50, axis.scaleCoord(0.25));
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
        assertNotNull(clonedAxis);
        assertNotSame(axis, clonedAxis);
        assertEquals("Cloned axis label is not the same as original", axis.getLabel(), clonedAxis.getLabel());
    }
}

class LinearAxis extends PlotAxis {
    @Override
    protected void resizeAxis(final Rectangle p0) {
        // Implement resizeAxis method for LinearAxis here
    }

    @Override
    public void setAxisBounds(final PlotRunList p0) {
        // Implement setAxisBounds method for LinearAxis here
    }
}

class LogAxisScale extends PlotAxisScale {
    // Implement LogAxisScale class here
}