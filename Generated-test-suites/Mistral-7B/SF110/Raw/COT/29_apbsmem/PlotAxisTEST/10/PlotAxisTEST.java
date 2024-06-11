package jahuwaldt.plot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

    private PlotAxis xAxis;

    @Before
    public void setUp() {
        xAxis = new PlotAxis() {
            @Override
            protected void resizeAxis(final Rectangle plotFrame) {
                // Not implemented
            }

            @Override
            public void draw(final Graphics g, final Component c, final Rectangle r) {
                // Not implemented
            }
        };
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(xAxis);
        assertEquals(1.0, xAxis.getxA(), 0.0);
        assertEquals(1.0, xAxis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, xAxis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, xAxis.getAUBA(), 0.0);
        assertEquals(1, xAxis.getGridStyle());
        assertNull(xAxis.getLabel());
        assertNull(xAxis.getScale());
        assertNull(xAxis.getNumberFormat());
    }

    @Test
    public void testConstructorWithLabelScaleFormatAndGridStyle() {
        final String label = "X Axis";
        final NumberFormat nf = new DecimalFormat("#.##");
        final PlotAxisScale scale = new PlotAxisScale() {
            @Override
            public double func(double x) {
                return x;
            }
        };
        final int gridStyle = 2;

        xAxis = new PlotAxis(label, scale, nf, gridStyle);

        assertEquals(1.0, xAxis.getxA(), 0.0);
        assertEquals(1.0, xAxis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, xAxis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, xAxis.getAUBA(), 0.0);
        assertEquals(gridStyle, xAxis.getGridStyle());
        assertEquals(label, xAxis.getLabel());
        assertNotNull(xAxis.getScale());
        assertEquals(nf, xAxis.getNumberFormat());
    }

    @Test
    public void testSetLabel() {
        final String label = "X Axis";

        xAxis.setLabel(label);

        assertEquals(label, xAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        final PlotAxisScale scale = new PlotAxisScale() {
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
        final NumberFormat nf = new DecimalFormat("#,##0.00");

        xAxis.setNumberFormat(nf);

        assertEquals(nf, xAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        final int gridStyle = 3;

        xAxis.setGridStyle(gridStyle);

        assertEquals(gridStyle, xAxis.getGridStyle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetScaleNull() {
        xAxis.setScale(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumberFormatNull() {
        xAxis.setNumberFormat(null);
    }

    @Test
    public void testManualLowerBounds() {
        xAxis.manualLowerBounds(true);
        assertTrue(xAxis.isManualLB());

        xAxis.manualLowerBounds(false);
        assertFalse(xAxis.isManualLB());
    }

    @Test
    public void testSetLowerBounds() {
        final double lowerBounds = 0.5;

        xAxis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, xAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        final double lowerBounds = 0.5;
        final NumberFormat nf = new DecimalFormat("#.##");

        xAxis.setLabel("X Axis");
        xAxis.setNumberFormat(nf);
        xAxis.setLowerBounds(lowerBounds);

        assertEquals("0.5", xAxis.lowerBoundsAsString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLowerBoundsNegativeNumber() {
        xAxis.setLowerBounds(-1.0);
    }

    @Test
    public void testManualUpperBounds() {
        xAxis.manualUpperBounds(true);
        assertTrue(xAxis.isManualUB());

        xAxis.manualUpperBounds(false);
        assertFalse(xAxis.isManualUB());
    }

    @Test
    public void testSetUpperBounds() {
        final double upperBounds = 2.5;

        xAxis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, xAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        final double upperBounds = 2.5;
        final NumberFormat nf = new DecimalFormat("#.##");

        xAxis.setLabel("X Axis");
        xAxis.setNumberFormat(nf);
        xAxis.setUpperBounds(upperBounds);

        assertEquals("2.5", xAxis.upperBoundsAsString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUpperBoundsNegativeNumber() {
        xAxis.setUpperBounds(-1.0);
    }

    @Test
    public void testManualTickStepSize() {
        xAxis.manualTickStepSize(true);
        assertTrue(xAxis.isManualQuant());

        xAxis.manualTickStepSize(false);
        assertFalse(xAxis.isManualQuant());
    }

    @Test
    public void testSetTickStepSize() {
        final double tickStepSize = 0.1;

        xAxis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, xAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        // Not implemented
    }

    @Test
    public void testSetSize() {
        // Not implemented
    }

    @Test
    public void testScaleCoord() {
        final double x = 2.0;

        assertEquals(4, xAxis.scaleCoord(x));
    }

    @Test
    public void testClone() {
        final PlotAxis clonedAxis = (PlotAxis)xAxis.clone();

        assertEquals(xAxis, clonedAxis);
    }
}