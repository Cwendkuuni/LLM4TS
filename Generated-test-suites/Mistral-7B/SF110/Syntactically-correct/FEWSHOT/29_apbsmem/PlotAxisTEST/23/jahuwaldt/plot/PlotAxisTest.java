package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testPlotAxisConstruction() {
        PlotAxis plotAxis = new PlotAxis();
        assertNull(plotAxis.getLabel());
        assertSame(LinearAxisScale.class, plotAxis.getScale());
        assertSame(NumberFormat.getInstance().getClass(), plotAxis.getNumberFormat());
        assertEquals(1, plotAxis.getGridStyle());
    }

    @Test
    public void testPlotAxisWithLabelAndFormat() {
        PlotAxis plotAxis = new PlotAxis("Test Axis", null, null, 1);
        assertEquals("Test Axis", plotAxis.getLabel());
        assertSame(LinearAxisScale.class, plotAxis.getScale());
        assertSame(NumberFormat.getInstance().getClass(), plotAxis.getNumberFormat());
        assertEquals(1, plotAxis.getGridStyle());
    }

    @Test
    public void testPlotAxisWithLabelScaleFormatAndGridStyle() {
        PlotAxis plotAxis = new PlotAxis("Test Axis", new LinearAxisScale(), NumberFormat.getInstance(), 2);
        assertEquals("Test Axis", plotAxis.getLabel());
        assertSame(LinearAxisScale.class, plotAxis.getScale());
        assertSame(NumberFormat.getInstance().getClass(), plotAxis.getNumberFormat());
        assertEquals(2, plotAxis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLabel("Test Axis");
        assertEquals("Test Axis", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertSame(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertSame(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setGridStyle(3);
        assertEquals(3, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.isManualLowerBounds());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLowerBounds(0.1);
        assertEquals(0.1, plotAxis.getLowerBounds());
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLowerBounds(123.45);
        assertEquals("123.45", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.isManualUpperBounds());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setUpperBounds(543.21);
        assertEquals(543.21, plotAxis.getUpperBounds());
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setUpperBounds(678.90);
        assertEquals("678.90", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.isManualTickStepSize());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setTickStepSize(1.23);
        assertEquals(1.23, plotAxis.getTickStepSize());
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis plotAxis = new PlotAxis();
        PlotRunList plotRunList = new PlotRunList();
        plotAxis.setAxisBounds(plotRunList);
        // Since this method is abstract, no assertions can be made here
    }

    @Test
    public void testSetSize() {
        PlotAxis plotAxis = new PlotAxis();
        Rectangle rectangle = new Rectangle(10, 20, 30, 40);
        plotAxis.setSize(rectangle);
        // Since this method calls setAxisTickLimits() and resizeAxis(), no assertions can be made here
    }

    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis();
        double x = 5.6;
        int expected = (int) (5.6 * 1.0 + 1.0);
        int actual = plotAxis.scaleCoord(x);
        assertEquals(expected, actual);
    }

    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis();
        PlotAxis clonedPlotAxis = (PlotAxis) plotAxis.clone();
        assertNotNull(clonedPlotAxis);
        assertNotSame(plotAxis, clonedPlotAxis);
    }

    @Test
    public void testDraw() {
        PlotAxis plotAxis = new PlotAxis();
        Graphics graphics = new Graphics() {
            @Override
            public void drawLine(int x1, int y1, int x2, int y2) {
                // Do nothing
            }
        };
        Component component = new Component() {
            @Override
            public Graphics getGraphics() {
                return graphics;
            }
        };
        Rectangle rectangle = new Rectangle(10, 20, 30, 40);
        plotAxis.draw(graphics, component, rectangle);
        // Since this method is abstract, no assertions can be made here
    }
}