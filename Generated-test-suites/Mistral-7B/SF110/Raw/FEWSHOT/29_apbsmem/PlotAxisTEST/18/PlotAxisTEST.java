package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

    private PlotAxis axis;

    @Before
    public void setup() {
        this.axis = new PlotAxis() {
            @Override
            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }

            @Override
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
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
    public void testParametersConstructor() {
        String label = "Test Label";
        NumberFormat numberFormat = NumberFormat.getInstance();
        PlotAxisScale scale = new PlotAxisScale();
        int gridStyle = 3;

        axis = new PlotAxis(label, scale, numberFormat, gridStyle);

        assertEquals(1.0, axis.getxA(), 0.0);
        assertEquals(1.0, axis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
        assertEquals(3, axis.getGridStyle());
        assertEquals(label, axis.getLabel());
        assertEquals(scale, axis.getScale());
        assertEquals(numberFormat, axis.getNumberFormat());
    }

    @Test
    public void testSetLabel() {
        String label = "Test Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        String label = "Test Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxisScale scale = new PlotAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxisScale scale = new PlotAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        axis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, axis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        axis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        axis.setGridStyle(5);
        assertEquals(5, axis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        axis.setGridStyle(5);
        assertEquals(5, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        axis.manualLowerBounds(true);
        assertTrue(axis.isManualLB());
        axis.manualLowerBounds(false);
        assertFalse(axis.isManualLB());
    }

    @Test
    public void testSetLowerBounds() {
        double lowerBounds = 10.5;
        axis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        double lowerBounds = 10.5;
        axis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        axis.setNumberFormat(numberFormat);
        axis.setLowerBounds(10.5);
        assertEquals("10.5", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUB());
        axis.manualUpperBounds(false);
        assertFalse(axis.isManualUB());
    }

    @Test
    public void testSetUpperBounds() {
        double upperBounds = 20.5;
        axis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        double upperBounds = 20.5;
        axis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        axis.setNumberFormat(numberFormat);
        axis.setUpperBounds(20.5);
        assertEquals("20.5", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        axis.manualTickStepSize(true);
        assertTrue(axis.isManualQuant());
        axis.manualTickStepSize(false);
        assertFalse(axis.isManualQuant());
    }

    @Test
    public void testSetTickStepSize() {
        double tickStepSize = 5.0;
        axis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        double tickStepSize = 5.0;
        axis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, axis.getTickStepSize(), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAxisBounds() {
        axis.setAxisBounds(null);
    }

    @Test
    public void testSetSize() {
        Rectangle plotFrame = new Rectangle(10, 10, 200, 200);
        axis.setSize(plotFrame);
        // Since resizeAxis and setAxisTickLimits are abstract, this test case doesn't cover those methods.
    }
}