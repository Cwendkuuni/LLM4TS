package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testConstructor() {
        PlotAxis axis = new PlotAxis();
        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
        assertEquals(1, axis.getGridStyle());
        assertNull(axis.getLabel());
        assertSame(LinearAxisScale.class, axis.getScale());
        assertSame(NumberFormat.getInstance().getClass(), axis.getNumberFormat());
    }

    @Test
    public void testConstructorWithLabelScaleFormatAndGridStyle() {
        PlotAxis axis = new PlotAxis("Label", new LinearAxisScale(), null, 1);
        assertEquals("Label", axis.getLabel());
        assertEquals(LinearAxisScale.class, axis.getScale().getClass());
        assertSame(NumberFormat.getInstance().getClass(), axis.getNumberFormat());
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testSetAndGetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Label");
        assertEquals("Label", axis.getLabel());
    }

    @Test
    public void testSetAndGetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LogAxisScale(2.0);
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetAndGetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
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
        assertTrue(axis.isManualLB());
        axis.manualLowerBounds(false);
        assertFalse(axis.isManualLB());
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
        NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
        axis.setLabel("Label");
        axis.setNumberFormat(format);
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
    public void testSetAndGetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.setUpperBounds(2.0);
        assertEquals(2.0, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
        axis.setLabel("Label");
        axis.setNumberFormat(format);
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
    public void testSetAndGetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        assertThrows(UnsupportedOperationException.class, () -> axis.setAxisBounds(null));
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(0, 0, 100, 100);
        axis.setSize(plotFrame);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        assertEquals(0, axis.scaleCoord(0.0));
        assertEquals(100, axis.scaleCoord(1.0));
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis) axis.clone();
        assertNotSame(axis, clonedAxis);
        assertEquals(axis.getxA(), clonedAxis.getxA(), 0.0);
        assertEquals(axis.getxB(), clonedAxis.getxB(), 0.0);
        assertEquals(axis.getALB(), clonedAxis.getALB(), 0.0);
        assertEquals(axis.getAUB(), clonedAxis.getAUB(), 0.0);
        assertEquals(axis.getGridStyle(), clonedAxis.getGridStyle());
        assertSame(axis.getScale(), clonedAxis.getScale());
        assertSame(axis.getNumberFormat(), clonedAxis.getNumberFormat());
        assertSame(axis.getLabel(), clonedAxis.getLabel());
    }

    @Test
    public void testDraw() {
        // Drawing logic not present in the test case.
        // You would need to mock Graphics and Component objects here.
    }
}