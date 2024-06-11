package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();
        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
        assertEquals(1, axis.getGridStyle());
        assertNull(axis.getLabel());
        assertNull(axis.getScale());
        assertNull(axis.getNumberFormat());
    }

    @Test
    public void testConstructorWithLabelScaleFormatAndGridStyle() {
        PlotAxis axis = new PlotAxis("TestAxis", new LinearAxisScale(), NumberFormat.getInstance(), 1);
        assertEquals("TestAxis", axis.getLabel());
        assertEquals(new LinearAxisScale(), axis.getScale());
        assertSame(NumberFormat.getInstance(), axis.getNumberFormat());
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("TestLabel");
        assertEquals("TestLabel", axis.getLabel());
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
        assertTrue(axis.isManualLB());
        axis.manualLowerBounds(false);
        assertFalse(axis.isManualLB());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getALB(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setLowerBounds(1.0);
        assertEquals("1.0", axis.lowerBoundsAsString());
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
        axis.setUpperBounds(2.0);
        assertEquals(2.0, axis.getAUB(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setUpperBounds(2.0);
        assertEquals("2.0", axis.upperBoundsAsString());
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
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getQuantum(), 0.0);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        assertEquals(0, axis.scaleCoord(0.0));
        assertEquals(10, axis.scaleCoord(0.5));
        assertEquals(20, axis.scaleCoord(1.0));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
        assertNotSame(axis, clonedAxis);
        assertEquals(axis.getAX(), clonedAxis.getAX(), 0.0);
        assertEquals(axis.getxB(), clonedAxis.getxB(), 0.0);
        assertEquals(axis.getALB(), clonedAxis.getALB(), 0.0);
        assertEquals(axis.getAUB(), clonedAxis.getAUB(), 0.0);
        assertEquals(axis.getGridStyle(), clonedAxis.getGridStyle());
        assertEquals(axis.getLabel(), clonedAxis.getLabel());
        assertEquals(axis.getScale(), clonedAxis.getScale());
        assertEquals(axis.getNumberFormat(), clonedAxis.getNumberFormat());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneThrowsException() throws CloneNotSupportedException {
        PlotAxis axis = new PlotAxis();
        axis.clone();
    }
}