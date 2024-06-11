package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testConstructor() {
        PlotAxis axis = new PlotAxis();
        assertNull(axis.getLabel());
        assertNull(axis.getScale());
        assertNull(axis.getNumberFormat());
        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
        assertEquals(1, axis.getGridStyle());
        assertNull(axis.getScale());
        assertEquals(NumberFormat.getInstance(), axis.getNumberFormat());
    }

    @Test
    public void testConstructorWithArguments() {
        String label = "TestLabel";
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        PlotAxisScale scale = new LinearAxisScale();
        int gridStyle = 2;
        PlotAxis axis = new PlotAxis(label, scale, numberFormat, gridStyle);
        assertEquals(label, axis.getLabel());
        assertEquals(scale, axis.getScale());
        assertEquals(numberFormat, axis.getNumberFormat());
        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
        assertEquals(gridStyle, axis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("TestLabel");
        assertEquals("TestLabel", axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("TestLabel");
        assertEquals("TestLabel", axis.getLabel());
        axis.setLabel(null);
        assertNull(axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
        assertNull(axis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
        assertNull(axis.getScale());
        axis.setScale(null);
        assertNull(axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        axis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, axis.getNumberFormat());
        assertNull(axis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        axis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, axis.getNumberFormat());
        assertNull(axis.getNumberFormat());
        axis.setNumberFormat(null);
        assertNull(axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
        axis.setGridStyle(-1);
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
        axis.setGridStyle(-1);
        assertEquals(1, axis.getGridStyle());
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
        assertEquals(1.0, axis.getALB(), 0.0);
        axis.setLowerBounds(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getALB(), 0.0);
        axis.setLowerBounds(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        axis.setLowerBounds(-1.0);
        assertEquals(Double.MIN_VALUE, axis.getALB(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        PlotAxis axis = new PlotAxis();
        axis.setLabel("TestLabel");
        axis.setLowerBounds(1.0);
        axis.setNumberFormat(numberFormat);
        String expected = numberFormat.format(1.0f);
        assertEquals(expected, axis.lowerBoundsAsString());
        axis.setLowerBounds(Double.MAX_VALUE);
        expected = numberFormat.format(Double.MAX_VALUE);
        assertEquals(expected, axis.lowerBoundsAsString());
        axis.setLowerBounds(-1.0);
        expected = numberFormat.format(Double.MIN_VALUE);
        assertEquals(expected, axis.lowerBoundsAsString());
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
        axis.setUpperBounds(1.0);
        assertEquals(1.0, axis.getAUB(), 0.0);
        axis.setUpperBounds(Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, axis.getAUB(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(1.0);
        assertEquals(1.0, axis.getAUB(), 0.0);
        axis.setUpperBounds(Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, axis.getAUB(), 0.0);
        axis.setUpperBounds(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, axis.getAUB(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        PlotAxis axis = new PlotAxis();
        axis.setLabel("TestLabel");
        axis.setUpperBounds(1.0);
        axis.setNumberFormat(numberFormat);
        String expected = numberFormat.format(1.0f);
        assertEquals(expected, axis.upperBoundsAsString());
        axis.setUpperBounds(Double.MAX_VALUE);
        expected = numberFormat.format(Double.MAX_VALUE);
        assertEquals(expected, axis.upperBoundsAsString());
        axis.setUpperBounds(Double.MIN_VALUE);
        expected = numberFormat.format(Double.MIN_VALUE);
        assertEquals(expected, axis.upperBoundsAsString());
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
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getQuantum(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getQuantum(), 0.0);
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getQuantum(), 0.0);
        axis.setTickStepSize(-1.0);
        assertEquals(-1.0, axis.getQuantum(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList list = new PlotRunList();
        axis.setAxisBounds(list);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle rectangle = new Rectangle(0, 0, 100, 100);
        axis.setSize(rectangle);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 0.5;
        int expected = (int)(0.5 * axis.getxA() + axis.getxB());
        int result = axis.scaleCoord(x);
        assertEquals(expected, result);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clone = (PlotAxis)axis.clone();
    }
}