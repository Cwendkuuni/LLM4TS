package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.text.NumberFormat;
import java.text.ParseException;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
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
        assertEquals("", axis.getLabel());
    }

    @Test
    public void testConstructorWithLabelScaleFormatAndGridStyle() {
        PlotAxis axis = new PlotAxis("TestAxis", new LinearAxisScale(), NumberFormat.getInstance(), 1);
        assertEquals("TestAxis", axis.getLabel());
        assertEquals(new LinearAxisScale(), axis.getScale());
        assertSame(NumberFormat.getInstance(), axis.getNumberFormat());
        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testSetAndGetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("TestAxis");
        assertEquals("TestAxis", axis.getLabel());
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
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        assertSame(nf, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
    }

    @Test
    public void testSetManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.isManualLB());
        axis.manualLowerBounds(false);
        assertFalse(axis.isManualLB());
    }

    @Test
    public void testSetLowerBounds() throws ParseException {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setParseIntOptions(NumberFormat.USE_EXPLICIT_GROUPING);
        String valueString = "1,234";
        double value = nf.parse(valueString).doubleValue();
        axis.setLowerBounds(value);
        assertEquals(value, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        assertEquals("", axis.lowerBoundsAsString());

        NumberFormat nf = NumberFormat.getInstance();
        nf.setParseIntOptions(NumberFormat.USE_EXPLICIT_GROUPING);
        String valueString = "1,234";
        axis.setLowerBounds(Double.parseDouble(valueString));
        assertEquals(valueString, axis.lowerBoundsAsString());
    }

    @Test
    public void testSetManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUB());
        axis.manualUpperBounds(false);
        assertFalse(axis.isManualUB());
    }

    @Test
    public void testSetUpperBounds() throws ParseException {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setParseIntOptions(NumberFormat.USE_EXPLICIT_GROUPING);
        String valueString = "1,234";
        double value = nf.parse(valueString).doubleValue();
        axis.setUpperBounds(value);
        assertEquals(value, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        assertEquals("", axis.upperBoundsAsString());

        NumberFormat nf = NumberFormat.getInstance();
        nf.setParseIntOptions(NumberFormat.USE_EXPLICIT_GROUPING);
        String valueString = "1,234";
        axis.setUpperBounds(Double.parseDouble(valueString));
        assertEquals(valueString, axis.upperBoundsAsString());
    }

    @Test
    public void testSetManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.isManualQuant());
        axis.manualTickStepSize(false);
        assertFalse(axis.isManualQuant());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(5.5);
        assertEquals(5.5, axis.getTickStepSize(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTickStepSizeNegativeValue() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(-5.5);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        assertEquals(0.0, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList p = new PlotRunList();
        axis.setAxisBounds(p);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(1, 1, 10, 10);
        axis.setSize(plotFrame);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        assertEquals(0, axis.scaleCoord(0.0));
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
        assertNotSame(axis, clonedAxis);
    }
}