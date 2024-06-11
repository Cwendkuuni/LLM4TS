package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
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
        assertNotNull(axis.getLabel());
        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
        assertEquals(1, axis.getGridStyle());
        assertNull(axis.getScale());
        assertNotNull(axis.getNumberFormat());
    }

    @Test
    public void testPlotAxisCustomConstructor() {
        PlotAxisScale scale = new LinearAxisScale();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        PlotAxis axis = new PlotAxis("Test Axis", scale, decimalFormat, 1);
        assertEquals("Test Axis", axis.getLabel());
        assertNotNull(axis.getScale());
        assertEquals(decimalFormat, axis.getNumberFormat());
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Test Axis");
        assertEquals("Test Axis", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        axis.setNumberFormat(decimalFormat);
        assertEquals(decimalFormat, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
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
        axis.setLowerBounds(0.0);
        assertEquals(0.0, axis.getALB(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(0.0);
        assertEquals(0.0, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(0.0);
        DecimalFormat decimalFormat = (DecimalFormat)axis.getNumberFormat();
        assertEquals("0.00", axis.lowerBoundsAsString());
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
        axis.setUpperBounds(5.0);
        assertEquals(5.0, axis.getAUB(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(5.0);
        assertEquals(5.0, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(5.0);
        DecimalFormat decimalFormat = (DecimalFormat)axis.getNumberFormat();
        assertEquals("5.00", axis.upperBoundsAsString());
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
        assertEquals(1.0, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getTickStepSize(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(-1.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        Rectangle bounds = new Rectangle(10, 10, 100, 100);
        axis.setAxisBounds(new PlotRunList());
        axis.setSize(bounds);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle bounds = new Rectangle(10, 10, 100, 100);
        axis.setSize(bounds);
    }

    @Test
    public void testSetAxisTickLimits() {
        PlotAxis axis = new PlotAxis();
        axis.setAxisTickLimits();
        fail("Method should not be called directly");
    }

    @Test
    public void testResizeAxis() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(10, 10, 100, 100);
        axis.resizeAxis(plotFrame);
        fail("Method should not be called directly");
    }

    @Test
    public void testDraw() {
        PlotAxis axis = new PlotAxis();
        Component component = new Component() {
            @Override
            public void paint(Graphics g) {
                fail("Drawing should not be performed in this test");
            }
        };
        Rectangle plotFrame = new Rectangle(10, 10, 100, 100);
        axis.draw(component.getGraphics(), component, plotFrame);
        fail("Drawing should not be performed in this test");
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double input = 1.5;
        int output = axis.scaleCoord(input);
        fail("Method should not be called directly");
        assertEquals(output, (int)(axis.getxA() * input + axis.getxB()), 0);
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
        assertEquals(axis.isManualLB(), clonedAxis.isManualLB());
        assertEquals(axis.getALB(), clonedAxis.getALB(), 0.0);
        assertEquals(axis.getAUB(), clonedAxis.getAUB(), 0.0);
        assertEquals(axis.isManualQuant(), clonedAxis.isManualQuant());
        assertEquals(axis.getQuantum(), clonedAxis.getQuantum(), 0.0);
    }
}