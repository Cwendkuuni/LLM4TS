package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

    private PlotAxis xAxis;
    private PlotAxis yAxis;

    @Before
    public void setUp() {
        xAxis = new PlotAxis("X-Axis", new PlotAxisScale(), null, 1) {
            protected void resizeAxis(Rectangle plotFrame) {
                // empty implementation
            }

            @Override
            public void setAxisBounds(PlotRunList p0) {
                // empty implementation
            }

            @Override
            public void draw(Graphics g, Component comp, Rectangle rect) {
                // empty implementation
            }

            @Override
            public int scaleCoord(double x) {
                return (int)(x * 1.0 + 1);
            }
        };

        yAxis = new PlotAxis("Y-Axis", new PlotAxisScale(), null, 1) {
            protected void resizeAxis(Rectangle plotFrame) {
                // empty implementation
            }

            @Override
            public void setAxisBounds(PlotRunList p0) {
                // empty implementation
            }

            @Override
            public void draw(Graphics g, Component comp, Rectangle rect) {
                // empty implementation
            }

            @Override
            public int scaleCoord(double x) {
                return (int)(x * 1.0 + 1);
            }
        };
    }

    @Test
    public void testDefaultInitialization() {
        PlotAxis axis = new PlotAxis();
        assertNotNull(axis);
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
        assertEquals(1, axis.gridStyle);
        assertNull(axis.scale);
        assertNull(axis.nf);
        assertNull(axis.label);
    }

    @Test
    public void testCustomInitialization() {
        PlotAxis axis = new PlotAxis("Custom Axis", null, null, 0) {
            protected void resizeAxis(Rectangle plotFrame) {
                // empty implementation
            }

            @Override
            public void setAxisBounds(PlotRunList p0) {
                // empty implementation
            }

            @Override
            public void draw(Graphics g, Component comp, Rectangle rect) {
                // empty implementation
            }

            @Override
            public int scaleCoord(double x) {
                return (int)(x * 1.0 + 1);
            }
        };
        assertEquals("Custom Axis", axis.getLabel());
        assertNull(axis.scale);
        assertNull(axis.nf);
        assertEquals(0, axis.gridStyle);
    }

    @Test
    public void testSetLabel() {
        xAxis.setLabel("X-Axis with label");
        assertEquals("X-Axis with label", xAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxisScale scale = new PlotAxisScale() {
            @Override
            public double func(double x) {
                return x * 2.0;
            }
        };
        xAxis.setScale(scale);
        assertEquals(scale, xAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        DecimalFormat format = new DecimalFormat("#0.000");
        xAxis.setNumberFormat(format);
        assertEquals(format, xAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        xAxis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, xAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        xAxis.manualLowerBounds(true);
        assertTrue(xAxis.manualLB);
        xAxis.manualLowerBounds(false);
        assertFalse(xAxis.manualLB);
        assertEquals(Double.MAX_VALUE, xAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testSetLowerBounds() {
        xAxis.setLowerBounds(1.0);
        assertEquals(1.0, xAxis.getLowerBounds(), 0.0);
        assertEquals("1.0", xAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        xAxis.manualUpperBounds(true);
        assertTrue(xAxis.manualUB);
        xAxis.manualUpperBounds(false);
        assertFalse(xAxis.manualUB);
        assertEquals(-1.7976931348623157E308, xAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testSetUpperBounds() {
        xAxis.setUpperBounds(2.0);
        assertEquals(2.0, xAxis.getUpperBounds(), 0.0);
        assertEquals("2.0", xAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        xAxis.manualTickStepSize(true);
        assertTrue(xAxis.manualQuant);
        xAxis.manualTickStepSize(false);
        assertFalse(xAxis.manualQuant);
        assertEquals(1.0, xAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetTickStepSize() {
        xAxis.setTickStepSize(0.5);
        assertEquals(0.5, xAxis.getTickStepSize(), 0.0);
    }
}