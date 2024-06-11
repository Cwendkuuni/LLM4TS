package jahuwaldt.plot;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

public class PlotAxisTest {

    private PlotAxis axis;

    @Before
    public void setUp() {
        axis = new PlotAxis() {
            @Override
            protected void setAxisTickLimits() {
                // Do nothing as we're testing the limits setting in this test
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // Do nothing as we're testing the limits setting in this test
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing as we're testing the limits setting in this test
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(axis.getLabel());
        assertEquals(1.0, axis.getxA(), 0.001);
        assertEquals(1.0, axis.getxB(), 0.001);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.001);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.001);
        assertEquals(1, axis.getGridStyle());
        assertNotNull(axis.getScale());
        assertInstanceOf(LinearAxisScale.class, axis.getScale());
        assertNotNull(axis.getNumberFormat());
        assertEquals(DecimalFormat.getInstance(), axis.getNumberFormat());
    }

    @Test
    public void testCustomConstructor() {
        axis = new PlotAxis("Test Axis", new LinearAxisScale(), null, 1);
        assertNotNull(axis.getLabel());
        assertEquals("Test Axis", axis.getLabel());
        assertEquals(1.0, axis.getxA(), 0.001);
        assertEquals(1.0, axis.getxB(), 0.001);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.001);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.001);
        assertEquals(1, axis.getGridStyle());
        assertNotNull(axis.getScale());
        assertInstanceOf(LinearAxisScale.class, axis.getScale());
        assertNull(axis.getNumberFormat());
    }

    @Test
    public void testSetLabel() {
        axis.setLabel("New Label");
        assertEquals("New Label", axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        assertEquals("Test Axis", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        axis.setScale(new LogAxisScale());
        assertInstanceOf(LogAxisScale.class, axis.getScale());
    }

    @Test
    public void testGetScale() {
        assertInstanceOf(LinearAxisScale.class, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        final DecimalFormat customFormat = new DecimalFormat("0.000");
        axis.setNumberFormat(customFormat);
        assertInstanceOf(DecimalFormat.class, axis.getNumberFormat());
        assertEquals(customFormat, axis.getNumberFormat());
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

    @Test(expected = IllegalArgumentException.class)
    public void testSetLowerBoundsWithNegativeValue() {
        axis.setLowerBounds(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUpperBoundsWithNegativeValue() {
        axis.setUpperBounds(-1.0);
    }

    @Test
    public void testSetLowerBounds() {
        axis.setLowerBounds(0.0);
        assertEquals(0.0, axis.getALB(), 0.001);
    }

    @Test
    public void testGetLowerBounds() {
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getALB(), 0.001);
    }

    @Test
    public void testLowerBoundsAsString() {
        axis.setLowerBounds(2.5);
        assertEquals("2.5", axis.lowerBoundsAsString());
    }

    @Test
    public void testSetUpperBounds() {
        axis.setUpperBounds(3.0);
        assertEquals(3.0, axis.getAUB(), 0.001);
    }

    @Test
    public void testGetUpperBounds() {
        axis.setUpperBounds(4.0);
        assertEquals(4.0, axis.getAUB(), 0.001);
    }

    @Test
    public void testUpperBoundsAsString() {
        axis.setUpperBounds(5.5);
        assertEquals("5.5", axis.upperBoundsAsString());
    }

    @Test
    public void testManualLowerBounds() {
        axis.manualLowerBounds(true);
        assertFalse(axis.isManualLB());
    }

    @Test
    public void testSetManualLowerBounds() {
        axis.manualLowerBounds(true);
        assertTrue(axis.isManualLB());
    }

    @Test
    public void testIsManualLowerBounds() {
        axis.manualLowerBounds(false);
        assertFalse(axis.isManualLB());
    }

    @Test
    public void testManualUpperBounds() {
        axis.manualUpperBounds(true);
        assertFalse(axis.isManualUB());
    }

    @Test
    public void testSetManualUpperBounds() {
        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUB());
    }

    @Test
    public void testIsManualUpperBounds() {
        axis.manualUpperBounds(false);
        assertFalse(axis.isManualUB());
    }

    @Test
    public void testManualTickStepSize() {
        axis.manualTickStepSize(true);
        assertFalse(axis.isManualQuant());
    }

    @Test
    public void testSetManualTickStepSize() {
        axis.manualTickStepSize(true);
        assertTrue(axis.isManualQuant());
    }

    @Test
    public void testIsManualTickStepSize() {
        axis.manualTickStepSize(false);
        assertFalse(axis.isManualQuant());
    }

    @Test
    public void testGetTickStepSize() {
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.001);
    }

    @Test
    public void testClone() {
        PlotAxis clonedAxis = axis.clone();
        assertNotNull(clonedAxis);
        assertNotSame(axis, clonedAxis);
        assertEquals(axis.getClass(), clonedAxis.getClass());
        assertEquals(axis.getALB(), clonedAxis.getALB(), 0.001);
        assertEquals(axis.getAUB(), clonedAxis.getAUB(), 0.001);
        assertEquals(axis.getGridStyle(), clonedAxis.getGridStyle());
        assertInstanceOf(LinearAxisScale.class, clonedAxis.getScale());
        assertSame(axis.getNumberFormat(), clonedAxis.getNumberFormat());
        assertSame(axis.getLabel(), clonedAxis.getLabel());
    }
}