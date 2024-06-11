package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();
        assertNull(axis.getLabel());
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.0);
        assertEquals(1, axis.getGridStyle());
        assertNull(axis.getScale());
        assertNull(axis.getNumberFormat());
    }

    @Test
    public void testConstructorWithLabelScaleNumberFormatAndGridStyle() {
        PlotAxis axis = new PlotAxis("TestAxis", new LinearAxisScale(), NumberFormat.getInstance(), 1);
        assertEquals("TestAxis", axis.getLabel());
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.0);
        assertEquals(1, axis.getGridStyle());
        assertEquals(axis.getScale(), new LinearAxisScale());
        assertEquals(axis.getNumberFormat(), NumberFormat.getInstance());
    }

    @Test
    public void testSetAndGetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("TestLabel");
        assertEquals("TestLabel", axis.getLabel());
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
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
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
        assertTrue(axis.isManualLowerBounds());
        axis.manualLowerBounds(false);
        assertFalse(axis.isManualLowerBounds());
    }

    @Test
    public void testSetAndGetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setLowerBounds(1.0);
        NumberFormat format = NumberFormat.getInstance();
        assertEquals("1.0", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUpperBounds());
        axis.manualUpperBounds(false);
        assertFalse(axis.isManualUpperBounds());
    }

    @Test
    public void testSetAndGetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(2.0);
        assertEquals(2.0, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(2.0);
        NumberFormat format = NumberFormat.getInstance();
        assertEquals("2.0", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.isManualTickStepSize());
        axis.manualTickStepSize(false);
        assertFalse(axis.isManualTickStepSize());
    }

    @Test
    public void testSetAndGetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethodSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setAxisBounds(null);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(0, 0, 100, 100);
        axis.setSize(plotFrame);
        // Here you should implement the assertions for the properties that change after setSize() call.
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 0.5;
        int coord = axis.scaleCoord(x);
        int expectedCoord = (int)(axis.xA * x * plotWidth + axis.xB);
        assertEquals(expectedCoord, coord);
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clone = axis.clone();
        // Here you should implement the assertions for the properties that should be equal in axis and clone objects.
    }

    @Test
    public void testDraw() {
        PlotAxis axis = new PlotAxis();
        Graphics graphics = null;
        Component component = null;
        Rectangle rectangle = null;
        axis.draw(graphics, component, rectangle);
        // Here you should implement the assertions for the drawing logic.
    }
}