package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        String label = "Test Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis axis = new PlotAxis();
        assertEquals(null, axis.getLabel());
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
        assertEquals(new LinearAxisScale(), axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        assertEquals(NumberFormat.getInstance(), axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(1);
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        PlotAxis axis = new PlotAxis();
        assertEquals(1, axis.getGridStyle());
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
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        double lowerBound = 1.0;
        axis.setLowerBounds(lowerBound);
        assertEquals(lowerBound, axis.getLowerBounds());
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        assertEquals(Double.MAX_VALUE, axis.getLowerBounds());
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        double lowerBound = 1.0;
        axis.setLowerBounds(lowerBound);
        assertEquals("1.0", axis.lowerBoundsAsString());
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
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        double upperBound = 2.0;
        axis.setUpperBounds(upperBound);
        assertEquals(upperBound, axis.getUpperBounds());
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        assertEquals(-1.7976931348623157E308, axis.getUpperBounds());
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        double upperBound = 2.0;
        axis.setUpperBounds(upperBound);
        assertEquals("2.0", axis.upperBoundsAsString());
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
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        double tickStepSize = 0.5;
        axis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, axis.getTickStepSize());
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        assertEquals(0.0, axis.getTickStepSize());
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setAxisBounds(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAxisBoundsWithNullPlotRunList() {
        PlotAxis axis = new PlotAxis();
        axis.setAxisBounds(null);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(0, 0, 10, 10);
        axis.setSize(plotFrame);
    }

    @Test
    public void testSetAxisTickLimits() {
        PlotAxis axis = new PlotAxis();
        axis.setAxisTickLimits();
    }

    @Test
    public void testResizeAxis() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(0, 0, 10, 10);
        axis.resizeAxis(plotFrame);
    }

    @Test
    public void testDraw() {
        PlotAxis axis = new PlotAxis();
        Component component = new Object() {
            @Override
            public Graphics getGraphics() {
                return null;
            }
        };
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        axis.draw(null, component, rectangle);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 1.0;
        int result = axis.scaleCoord(x);
        assertEquals(0, result);
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
        assertNotSame(axis, clonedAxis);
    }

}