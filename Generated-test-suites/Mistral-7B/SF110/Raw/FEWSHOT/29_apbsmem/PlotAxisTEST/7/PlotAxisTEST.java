package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.text.NumberFormat;

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
        PlotAxis axis = new PlotAxis("Test", new LinearAxisScale(), NumberFormat.getInstance(), 1);
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
        assertEquals(1, axis.gridStyle);
        assertEquals("Test", axis.label);
        assertNotNull(axis.scale);
        assertNotNull(axis.nf);
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Test");
        assertEquals("Test", axis.getLabel());
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
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
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
        assertTrue(axis.manualLB);
        axis.manualLowerBounds(false);
        assertFalse(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getLowerBounds());
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setLabel("Test");
        axis.setNumberFormat(format);
        axis.setLowerBounds(1.0);
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
        axis.setUpperBounds(1.0);
        assertEquals(1.0, axis.getUpperBounds());
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setLabel("Test");
        axis.setNumberFormat(format);
        axis.setUpperBounds(1.0);
        assertEquals("1.0", axis.upperBoundsAsString());
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
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList list = new PlotRunList();
        axis.setAxisBounds(list);
        // The implementation is abstract, so we cannot test it directly
    }

    @Test
    public void testSetSize() throws IOException {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(0, 0, 100, 100);
        axis.setSize(plotFrame);
        // The implementation is abstract, so we cannot test it directly
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 0.5;
        int result = axis.scaleCoord(x);
        double expected = axis.xA * x + axis.xB;
        assertEquals(Math.round(expected), result);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
        assertNotSame(axis, clonedAxis);
    }
}