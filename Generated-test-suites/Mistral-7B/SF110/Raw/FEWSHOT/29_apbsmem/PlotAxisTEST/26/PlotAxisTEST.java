package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();
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
    public void testConstructorWithLabelScaleFormatAndGridStyle() {
        PlotAxis axis = new PlotAxis("MyAxisLabel", null, null, 1);
        assertEquals("MyAxisLabel", axis.getLabel());
        assertNull(axis.scale);
        assertNull(axis.nf);
        assertEquals(1, axis.gridStyle);
    }

    @Test
    public void testSetAndGetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("MyAxisLabel");
        assertEquals("MyAxisLabel", axis.getLabel());
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
        DecimalFormat numberFormat = new DecimalFormat("#.#####");
        axis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, axis.getNumberFormat());
    }

    @Test
    public void testSetAndGetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
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
        axis.setLowerBounds(3.14159265);
        assertEquals(3.14159265, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(3.14159265);
        String expected = "#.#####3.14159";
        String result = axis.lowerBoundsAsString();
        assertEquals(expected, result);
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
        axis.setUpperBounds(2.718281828);
        assertEquals(2.718281828, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(2.718281828);
        String expected = "#.#####2.71828";
        String result = axis.upperBoundsAsString();
        assertEquals(expected, result);
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
        axis.setTickStepSize(1.2345);
        assertEquals(1.2345, axis.getTickStepSize(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(-1.2345);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList p = new PlotRunList();
        axis.setAxisBounds(p);
        // Since this method is abstract, no further validation is possible in a unit test
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(10, 20, 100, 50);
        axis.setSize(plotFrame);
        // Since this method calls setAxisTickLimits() and resizeAxis(), no further validation is
        // possible in a unit test
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 0.5;
        int expected = (int)(0.5 * axis.xA + axis.xB);
        int result = axis.scaleCoord(x);
        assertEquals(expected, result);
    }
}