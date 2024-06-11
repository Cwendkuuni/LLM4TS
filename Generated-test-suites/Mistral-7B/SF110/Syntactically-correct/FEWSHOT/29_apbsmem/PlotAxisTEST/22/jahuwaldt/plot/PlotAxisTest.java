package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        String label = "Test Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        PlotAxisScale scale = new PlotAxisScale() {
            @Override
            public AxisLimitData findGoodLimits(double lb, double ub) {
                return new AxisLimitData(0.0, 0.0, 1.0);
            }
        };

        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        NumberFormat numberFormat = NumberFormat.getInstance();
        axis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        int gridStyle = 3;
        axis.setGridStyle(gridStyle);
        assertEquals(gridStyle, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        axis.manualLowerBounds(true);
        assertTrue(axis.isManualLowerBoundsSet());
        axis.manualLowerBounds(false);
        assertFalse(axis.isManualLowerBoundsSet());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        double lowerBounds = 5.0;
        axis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, axis.getLowerBounds());
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        NumberFormat numberFormat = NumberFormat.getInstance();
        axis.setNumberFormat(numberFormat);
        double lowerBounds = 5.0;
        axis.setLowerBounds(lowerBounds);
        assertEquals("5.0", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUpperBoundsSet());
        axis.manualUpperBounds(false);
        assertFalse(axis.isManualUpperBoundsSet());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        double upperBounds = 10.0;
        axis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, axis.getUpperBounds());
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        NumberFormat numberFormat = NumberFormat.getInstance();
        axis.setNumberFormat(numberFormat);
        double upperBounds = 10.0;
        axis.setUpperBounds(upperBounds);
        assertEquals("10.0", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        axis.manualTickStepSize(true);
        assertTrue(axis.isManualTickStepSizeSet());
        axis.manualTickStepSize(false);
        assertFalse(axis.isManualTickStepSizeSet());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }
        };

        double tickStepSize = 2.5;
        axis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, axis.getTickStepSize());
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisTickLimits() {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }

            @Override
            public void setAxisBounds(PlotRunList p) {
            }
        };

        PlotRunList plotRunList = new PlotRunList() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public PlotRun get(int index) {
                return null;
            }
        };

        axis.setAxisBounds(plotRunList);
        // Verify that the method does not throw an exception
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisTickLimits() {
            }

            @Override
            protected void resizeAxis(Rectangle p) {
            }

            @Override
            public void draw(Graphics g, Component c, Rectangle r) {
            }

            @Override
            public int scaleCoord(double x) {
                return 0;
            }

            @Override
            public void setAxisBounds(PlotRunList p) {
            }

            @Override
            public void setSize(Rectangle plotFrame) {
            }
        };

        Rectangle plotFrame = new Rectangle(0, 0, 100, 100);
        axis.setSize(plotFrame);
        // Verify that the method does not throw an exception
    }
}