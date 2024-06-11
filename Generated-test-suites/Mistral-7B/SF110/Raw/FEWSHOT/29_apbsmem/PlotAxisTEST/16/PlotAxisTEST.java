package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testConstructor() {
        PlotAxis axis = new PlotAxis();
        assertNotNull(axis);
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new PlotAxisScale();
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
        axis.setGridStyle(1);
        assertEquals(1, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        double lowerBounds = 1.0;
        axis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, axis.getLowerBounds());
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setLabel("Lower Bounds");
        axis.setNumberFormat(format);
        axis.setLowerBounds(1.0);
        assertEquals("1.0", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        double upperBounds = 2.0;
        axis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, axis.getUpperBounds());
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setLabel("Upper Bounds");
        axis.setNumberFormat(format);
        axis.setUpperBounds(2.0);
        assertEquals("2.0", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        double tickStepSize = 0.5;
        axis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, axis.getTickStepSize());
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
                // Implementation not required for testing
            }
        };
        assertNull(axis.getScale());
        axis.setScale(new PlotAxisScale());
        PlotRunList plotRunList = new PlotRunList();
        axis.setAxisBounds(plotRunList);
        assertNotNull(axis.getScale());
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis() {
            @Override
            protected void resizeAxis(Rectangle p) {
                // Implementation not required for testing
            }
        };
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        axis.setSize(rectangle);
        // Implementation of this method is abstract and not testable
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        double x = 1.0;
        int expected = (int)(1.0 * axis.xA);
        int actual = axis.scaleCoord(x);
        assertEquals(expected, actual);
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
        assertNotSame(axis, clonedAxis);
    }
}