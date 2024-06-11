package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

    private PlotAxis axis;

    @Before
    public void setup() {
        this.axis = new PlotAxis("TestAxis", new PlotAxisScale(), new DecimalFormat("#.####"), 1);
    }

    @Test
    public void testConstructor() {
        assertNotNull(axis);
        assertEquals("TestAxis", axis.getLabel());
        assertEquals(new LinearAxisScale(), axis.getScale());
        assertNotNull(axis.getNumberFormat());
        assertEquals(1, axis.getGridStyle());
        assertTrue(axis.manualLB);
        assertTrue(axis.manualUB);
        assertTrue(axis.manualQuant);
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
        assertEquals(0.0, axis.quantum, 0.0);
    }

    @Test
    public void testSetLabel() {
        axis.setLabel("NewLabel");
        assertEquals("NewLabel", axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        assertEquals("TestAxis", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        final PlotAxisScale newScale = new LogAxisScale();
        axis.setScale(newScale);
        assertEquals(newScale, axis.getScale());
    }

    @Test
    public void testGetScale() {
        assertEquals(new LinearAxisScale(), axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        final DecimalFormat newFormat = new DecimalFormat("#,##0.00");
        axis.setNumberFormat(newFormat);
        assertEquals(newFormat, axis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        assertEquals(DecimalFormat.getInstance(), axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        axis.manualLowerBounds(true);
        assertTrue(axis.manualLB);

        axis.manualLowerBounds(false);
        assertFalse(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        final double newLowerBound = 5.0;
        axis.setLowerBounds(newLowerBound);
        assertEquals(newLowerBound, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        axis.setLowerBounds(5.0);
        final String expected = new DecimalFormat("#.####").format(5.0);
        assertEquals(expected, axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        axis.manualUpperBounds(true);
        assertTrue(axis.manualUB);

        axis.manualUpperBounds(false);
        assertFalse(axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        final double newUpperBound = 10.0;
        axis.setUpperBounds(newUpperBound);
        assertEquals(newUpperBound, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        axis.setUpperBounds(10.0);
        final String expected = new DecimalFormat("#.####").format(10.0);
        assertEquals(expected, axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        axis.manualTickStepSize(true);
        assertTrue(axis.manualQuant);

        axis.manualTickStepSize(false);
        assertFalse(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        final double newTickStepSize = 2.5;
        axis.setTickStepSize(newTickStepSize);
        assertEquals(newTickStepSize, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        assertEquals(0.0, axis.getTickStepSize(), 0.0);
    }

    @Test(expected = AssertionError.class)
    public void testSetAxisBoundsNull() {
        axis.setAxisBounds(null);
    }

    // Add more test methods for the abstract methods and protected methods if needed
}