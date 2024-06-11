package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

    private PlotAxis axis;

    @Before
    public void setUp() {
        axis = new LinearPlotAxis("TestAxis", null, NumberFormat.getInstance(), 1);
    }

    @Test
    public void testSetLabel() {
        axis.setLabel("NewLabel");
        assertEquals("NewLabel", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        final LinearAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        final DecimalFormat decimalFormat = new DecimalFormat("#.###");
        axis.setNumberFormat(decimalFormat);
        assertEquals(decimalFormat, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        axis.setGridStyle(kTickMarksOnly);
        assertEquals(kTickMarksOnly, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        axis.manualLowerBounds(true);
        assertTrue(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        axis.setLowerBounds(1.23);
        assertEquals(1.23, axis.getLowerBounds(), 0.001);
    }

    @Test
    public void testLowerBoundsAsString() {
        axis.setLowerBounds(1.23);
        assertEquals("1.23", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        axis.manualUpperBounds(true);
        assertTrue(axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        axis.setUpperBounds(3.45);
        assertEquals(3.45, axis.getUpperBounds(), 0.001);
    }

    @Test
    public void testUpperBoundsAsString() {
        axis.setUpperBounds(3.45);
        assertEquals("3.45", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        axis.manualTickStepSize(true);
        assertTrue(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.001);
    }

    @Test
    public void testDraw() {
        // This test case is not comprehensive as it depends on the Graphics object and Component provided.
        // You may want to create mock objects for these parameters or test the draw method in the context
        // of a specific plot implementation that uses this PlotAxis class.
    }

    @Test
    public void testScaleCoord() {
        final int expectedPixel = 100;
        axis.setAxisBounds(null);
        axis.setSize(new Rectangle(0, 0, 200, 200));
        final double x = 1.5;
        final int resultPixel = axis.scaleCoord(x);
        assertEquals(expectedPixel, resultPixel);
    }

    @Test
    public void testClone() {
        final PlotAxis clonedAxis = (PlotAxis)axis.clone();
        assertNotSame(axis, clonedAxis);
        assertEquals(axis.getClass(), clonedAxis.getClass());
    }
}