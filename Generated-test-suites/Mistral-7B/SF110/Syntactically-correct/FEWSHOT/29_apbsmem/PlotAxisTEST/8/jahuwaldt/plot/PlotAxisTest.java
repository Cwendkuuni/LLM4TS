package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Label");
        assertEquals("Label", axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis axis = new PlotAxis();
        assertNull(axis.getLabel());
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
        assertNull(axis.getScale());
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
        assertNull(axis.getNumberFormat());
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
        assertTrue(axis.isManualLowerBounds());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getLowerBounds(), 0.001);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.lowerBoundsAsString();
        fail("Method should not be called without setting a label or number format");
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUpperBounds());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(1.0);
        assertEquals(1.0, axis.getUpperBounds(), 0.001);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.upperBoundsAsString();
        fail("Method should not be called without setting a label or number format");
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.isManualTickStepSize());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getTickStepSize(), 0.001);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList p0 = new PlotRunList(); // Assuming PlotRunList is properly mocked or implemented
        axis.setAxisBounds(p0);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(10, 10, 100, 100);
        axis.setSize(plotFrame);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 1.0;
        int expected = (int)(axis.xA * axis.scale.func(x) + axis.xB);
        int result = axis.scaleCoord(x);
        assertEquals(expected, result);
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = axis.clone();
        assertNotSame(axis, clonedAxis);
    }
}