package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Test;

import jahuwaldt.plot.LinearAxisScale;
import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis plotAxis = new PlotAxis();
        assertEquals(1.0, plotAxis.getxA(), 0.01);
        assertEquals(1.0, plotAxis.getxB(), 0.01);
        assertEquals(Double.MAX_VALUE, plotAxis.getALB(), 0.01);
        assertEquals(-1.7976931348623157E308, plotAxis.getAUB(), 0.01);
        assertEquals(1, plotAxis.getGridStyle());
        assertNull(plotAxis.getLabel());
        assertNull(plotAxis.getScale());
        assertNull(plotAxis.getNumberFormat());
    }

    @Test
    public void testCustomConstructor() {
        String label = "TestAxis";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat numberFormat = new DecimalFormat("#.##");
        int gridStyle = 2;
        PlotAxis plotAxis = new PlotAxis(label, scale, numberFormat, gridStyle);

        assertEquals(label, plotAxis.getLabel());
        assertEquals(scale, plotAxis.getScale());
        assertEquals(numberFormat, plotAxis.getNumberFormat());
        assertEquals(gridStyle, plotAxis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLabel("TestLabel");
        assertEquals("TestLabel", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis();
        NumberFormat numberFormat = new DecimalFormat("#.##");
        plotAxis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, plotAxis.getNumberFormat());
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
        assertTrue(plotAxis.isManualLB());
        plotAxis.manualLowerBounds(false);
        assertFalse(plotAxis.isManualLB());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis();
        double lowerBounds = 0.5;
        plotAxis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, plotAxis.getLowerBounds(), 0.01);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLowerBounds(0.5);
        String expected = "#0.5";
        DecimalFormat decimalFormat = new DecimalFormat(expected);
        String actual = plotAxis.lowerBoundsAsString();
        assertEquals(decimalFormat.format(0.5), actual);
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.isManualUB());
        plotAxis.manualUpperBounds(false);
        assertFalse(plotAxis.isManualUB());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis();
        double upperBounds = 2.5;
        plotAxis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, plotAxis.getUpperBounds(), 0.01);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setUpperBounds(2.5);
        String expected = "#2.5";
        DecimalFormat decimalFormat = new DecimalFormat(expected);
        String actual = plotAxis.upperBoundsAsString();
        assertEquals(decimalFormat.format(2.5), actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidGridStyle() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setGridStyle(-1);
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.isManualQuant());
        plotAxis.manualTickStepSize(false);
        assertFalse(plotAxis.isManualQuant());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis();
        double tickStepSize = 0.25;
        plotAxis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, plotAxis.getTickStepSize(), 0.01);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setTickStepSize(0.25);
        assertEquals(0.25, plotAxis.getTickStepSize(), 0.01);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis plotAxis = new PlotAxis();
        Rectangle rectangle = new Rectangle(10, 10, 200, 200);
        plotAxis.setAxisBounds(new PlotRunList());
        assertEquals(0, plotAxis.getxA(), 0.01);
        assertEquals(0, plotAxis.getxB(), 0.01);
        plotAxis.setAxisBounds(rectangle);
        assertEquals(100.0, plotAxis.getxA(), 0.01);
        assertEquals(300.0, plotAxis.getxB(), 0.01);
    }

    @Test
    public void testSetSize() {
        PlotAxis plotAxis = new PlotAxis();
        Rectangle rectangle = new Rectangle(10, 10, 200, 200);
        plotAxis.setSize(rectangle);
        assertEquals(100.0 / 200.0, plotAxis.getxA(), 0.01);
        assertEquals(300.0 / 200.0, plotAxis.getxB(), 0.01);
    }

    @Test
    public void testSetAxisTickLimits() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualLowerBounds(true);
        plotAxis.manualUpperBounds(true);
        plotAxis.manualTickStepSize(true);
        plotAxis.setAxisTickLimits();
        assertFalse(plotAxis.isManualLB());
        assertFalse(plotAxis.isManualUB());
        assertFalse(plotAxis.isManualQuant());
    }

    @Test
    public void testDraw() {
        PlotAxis plotAxis = new PlotAxis();
        Component component = new Object() {
            public Graphics getGraphics() {
                return new Graphics() {
                    public void drawLine(int x1, int y1, int x2, int y2) {}
                    public void drawString(String s, int x, int y) {}
                };
            }
        };
        Rectangle rectangle = new Rectangle(10, 10, 200, 200);
        plotAxis.draw(component.getGraphics(), component, rectangle);
        // Draw method should be implemented in the subclass
    }

    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis();
        double input = 1.5;
        int expectedOutput = 150;
        int output = plotAxis.scaleCoord(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis) plotAxis.clone();
        assertNotSame(plotAxis, clonedAxis);
        assertEquals(plotAxis.getLabel(), clonedAxis.getLabel());
        assertEquals(plotAxis.getScale(), clonedAxis.getScale());
        assertEquals(plotAxis.getNumberFormat(), clonedAxis.getNumberFormat());
        assertEquals(plotAxis.getGridStyle(), clonedAxis.getGridStyle());
    }
}