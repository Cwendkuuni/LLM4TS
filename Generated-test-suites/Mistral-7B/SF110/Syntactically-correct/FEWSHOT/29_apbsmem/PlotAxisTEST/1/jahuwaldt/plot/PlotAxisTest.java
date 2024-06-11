package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.0);
        assertEquals(1, axis.getGridStyle());
        assertNull(axis.getScale());
        assertNull(axis.getNumberFormat());
        assertEquals("", axis.getLabel());
    }

    @Test
    public void testConstructorWithLabelScaleFormatAndGridStyle() {
        PlotAxis axis = new PlotAxis("Test Label", null, null, 1);
        assertEquals("Test Label", axis.getLabel());
        assertNull(axis.getScale());
        assertNull(axis.getNumberFormat());
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
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
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getNumberInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(3);
        assertEquals(3, axis.getGridStyle());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(10.5);
        assertEquals(10.5, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getNumberInstance();
        axis.setLowerBounds(10.5);
        assertEquals("10.5", axis.lowerBoundsAsString());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(20.5);
        assertEquals(20.5, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getNumberInstance();
        axis.setUpperBounds(20.5);
        assertEquals("20.5", axis.upperBoundsAsString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(-5.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(-5.5);
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertFalse(axis.manualLB);
        axis.manualLowerBounds(false);
        assertTrue(axis.manualLB);
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertFalse(axis.manualUB);
        axis.manualUpperBounds(false);
        assertTrue(axis.manualUB);
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertFalse(axis.manualQuant);
        axis.manualTickStepSize(false);
        assertTrue(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.0);
    }
}