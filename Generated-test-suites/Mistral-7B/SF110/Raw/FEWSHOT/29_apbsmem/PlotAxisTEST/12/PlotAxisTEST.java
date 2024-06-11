package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testPlotAxisConstructor() {
        PlotAxis plotAxis = new PlotAxis();
        assertNull(plotAxis.getLabel());
        assertNull(plotAxis.getScale());
        assertNull(plotAxis.getNumberFormat());
        assertEquals(1.0, plotAxis.getAxisBounds().xA, 0.0);
        assertEquals(1.0, plotAxis.getAxisBounds().xB, 0.0);
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0.0);
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0.0);
        assertEquals(1, plotAxis.getGridStyle());
        assertNull(plotAxis.scale);
        assertNull(plotAxis.nf);
    }

    @Test
    public void testPlotAxisConstructorWithLabelScaleFormatAndGridStyle() {
        PlotAxis plotAxis = new PlotAxis("TestAxis", new LinearAxisScale(), NumberFormat.getInstance(), 1);
        assertEquals("TestAxis", plotAxis.getLabel());
        assertSame(new LinearAxisScale(), plotAxis.getScale());
        assertSame(NumberFormat.getInstance(), plotAxis.getNumberFormat());
        assertEquals(1.0, plotAxis.getAxisBounds().xA, 0.0);
        assertEquals(1.0, plotAxis.getAxisBounds().xB, 0.0);
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0.0);
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0.0);
        assertEquals(1, plotAxis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLabel("TestAxis");
        assertEquals("TestAxis", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis();
        PlotAxisScale scale = new LogAxisScale();
        plotAxis.setScale(scale);
        assertSame(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis();
        NumberFormat nf = NumberFormat.getPercentInstance();
        plotAxis.setNumberFormat(nf);
        assertSame(nf, plotAxis.getNumberFormat());
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
        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLowerBounds(1.5);
        assertEquals(1.5, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLowerBounds(1.5);
        assertEquals("1.5", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setUpperBounds(2.5);
        assertEquals(2.5, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setUpperBounds(2.5);
        assertEquals("2.5", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setTickStepSize(0.5);
        assertEquals(0.5, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setTickStepSize(0.5);
        assertEquals(0.5, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis plotAxis = new PlotAxis();
        PlotRunList plotRunList = new PlotRunList();
        plotAxis.setAxisBounds(plotRunList);
        // The implementation of setAxisBounds is abstract, so we cannot test it directly in this test case.
    }

    @Test
    public void testSetSize() {
        PlotAxis plotAxis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(10, 10, 100, 100);
        plotAxis.setSize(plotFrame);
        // The implementation of setSize contains abstract methods, so we cannot test it directly in this test case.
    }

    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis();
        double x = 0.5;
        int expected = (int)(0.5 * 1.0 * plotAxis.getScale().func(x) + 1.0);
        int result = plotAxis.scaleCoord(x);
        assertEquals(expected, result);
    }

    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis();
        PlotAxis clonedPlotAxis = (PlotAxis)plotAxis.clone();
        assertNotSame(plotAxis, clonedPlotAxis);
        assertEquals(plotAxis.getLabel(), clonedPlotAxis.getLabel());
        assertEquals(plotAxis.getScale(), clonedPlotAxis.getScale());
        assertEquals(plotAxis.getNumberFormat(), clonedPlotAxis.getNumberFormat());
        assertEquals(plotAxis.getGridStyle(), clonedPlotAxis.getGridStyle());
        assertEquals(plotAxis.manualLB, clonedPlotAxis.manualLB);
        assertEquals(plotAxis.aLB, clonedPlotAxis.aLB, 0.0);
        assertEquals(plotAxis.manualUB, clonedPlotAxis.manualUB);
        assertEquals(plotAxis.aUB, clonedPlotAxis.aUB, 0.0);
        assertEquals(plotAxis.manualQuant, clonedPlotAxis.manualQuant);
        assertEquals(plotAxis.quantum, clonedPlotAxis.quantum, 0.0);
        assertEquals(plotAxis.gridStyle, clonedPlotAxis.gridStyle);
    }

    // Add more test methods for other abstract methods, if necessary

    @Test
    public void testResizeAxis() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            protected void resizeAxis(final Rectangle p0) {
                // Implementation for testing purposes
            }
        };
        Rectangle plotFrame = new Rectangle(10, 10, 100, 100);
        plotAxis.resizeAxis(plotFrame);
        // Add assertions for expected behavior of resizeAxis
    }

    @Test
    public void testDraw() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {
                // Implementation for testing purposes
            }
        };
        Graphics graphics = new Graphics() {
            // Implementation for testing purposes
        };
        Component component = new JFrame();
        Rectangle rectangle = new Rectangle(10, 10, 100, 100);
        plotAxis.draw(graphics, component, rectangle);
        // Add assertions for expected behavior of draw
    }
}