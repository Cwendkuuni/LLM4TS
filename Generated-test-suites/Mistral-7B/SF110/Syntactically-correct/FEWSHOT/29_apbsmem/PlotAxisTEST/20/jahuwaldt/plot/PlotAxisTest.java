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
    public void testPlotAxis() {
        PlotAxis axis = new PlotAxis();
        assertNotNull(axis);
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
        assertEquals(1, axis.gridStyle);
        assertNull(axis.label);
        assertNull(axis.scale);
        assertNull(axis.nf);
    }

    @Test
    public void testPlotAxisWithLabelScaleFormatAndGridStyle() {
        String label = "Test Label";
        PlotAxisAxisScale scale = new PlotAxisAxisScale();
        NumberFormat nf = NumberFormat.getInstance();
        int gridStyle = 2;
        PlotAxis axis = new PlotAxis(label, scale, nf, gridStyle);
        assertNotNull(axis);
        assertEquals(label, axis.getLabel());
        assertEquals(scale, axis.getScale());
        assertEquals(nf, axis.getNumberFormat());
        assertEquals(gridStyle, axis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        String label = "Test Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new PlotAxisAxisScale();
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
        assertTrue(axis.manualLB);
        axis.manualLowerBounds(false);
        assertFalse(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        double lowerBounds = 10.0;
        axis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        double lowerBounds = 10.0;
        axis.setLowerBounds(lowerBounds);
        assertEquals("10.0", axis.lowerBoundsAsString());
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
        double upperBounds = 20.0;
        axis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        double upperBounds = 20.0;
        axis.setUpperBounds(upperBounds);
        assertEquals("20.0", axis.upperBoundsAsString());
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
        double tickStepSize = 5.0;
        axis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotRunList p0 = new PlotRunList();
        PlotAxis axis = new PlotAxis();
        axis.setAxisBounds(p0);
    }

    @Test
    public void testSetSize() {
        Graphics graphics = new Graphics() {};
        Component component = new Component() {};
        Rectangle plotFrame = new Rectangle(0, 0, 100, 100);
        PlotAxis axis = new PlotAxis();
        axis.setSize(plotFrame);
        assertEquals(plotFrame.width, axis.xA * plotFrame.width + axis.xB, 0.0);
        assertTrue(axis.resizeAxis(plotFrame) instanceof RuntimeException);
    }

    @Test
    public void testDraw() {
        Graphics graphics = new Graphics() {};
        Component component = new Component() {};
        Rectangle plotFrame = new Rectangle(0, 0, 100, 100);
        PlotAxis axis = new PlotAxis();
        axis.setSize(plotFrame);
        assertNull(axis.draw(graphics, component, plotFrame));
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 1.0;
        int expected = (int)(axis.xA * x + axis.xB);
        int result = axis.scaleCoord(x);
        assertEquals(expected, result);
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
        assertNotNull(clonedAxis);
        assertEquals(axis, clonedAxis);
    }
}