package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testConstructor() {
        PlotAxis plotAxis = new PlotAxis();
        assertEquals(1.0, plotAxis.getxA(), 0.0);
        assertEquals(1.0, plotAxis.getxB(), 0.0);
        assertEquals(Double.MAX_VALUE, plotAxis.getALB(), 0.0);
        assertEquals(-1.7976931348623157E308, plotAxis.getAUB(), 0.0);
        assertEquals(1, plotAxis.getGridStyle());
        assertNull(plotAxis.getLabel());
        assertNull(plotAxis.getScale());
        assertNull(plotAxis.getNumberFormat());
    }

    @Test
    public void testConstructorWithLabelAndScale() {
        String label = "Test Label";
        PlotAxisScale scale = new LinearAxisScale();
        PlotAxis plotAxis = new PlotAxis(label, scale, null, 1);
        assertEquals("Test Label", plotAxis.getLabel());
        assertEquals(scale, plotAxis.getScale());
        assertNull(plotAxis.getNumberFormat());
        assertEquals(1, plotAxis.getGridStyle());
    }

    @Test
    public void testConstructorWithNullLabel() {
        PlotAxisScale scale = new LinearAxisScale();
        PlotAxis plotAxis = new PlotAxis(null, scale, null, 1);
        assertNull(plotAxis.getLabel());
    }

    @Test
    public void testConstructorWithNullScale() {
        String label = "Test Label";
        PlotAxis plotAxis = new PlotAxis(label, null, null, 1);
        assertEquals(label, plotAxis.getLabel());
        assertNull(plotAxis.getScale());
    }

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis plotAxis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis();
        NumberFormat numberFormat = NumberFormat.getInstance();
        plotAxis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, plotAxis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis();
        NumberFormat numberFormat = NumberFormat.getInstance();
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
    public void testGetGridStyle() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setGridStyle(3);
        assertEquals(3, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.isManualLB());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis();
        double lowerBound = 5.5;
        plotAxis.setLowerBounds(lowerBound);
        assertEquals(lowerBound, plotAxis.getALB(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis();
        NumberFormat numberFormat = new DecimalFormat("#.##");
        plotAxis.setNumberFormat(numberFormat);
        plotAxis.setLowerBounds(5.5);
        assertEquals("5.50", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.isManualUB());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis();
        double upperBound = 10.0;
        plotAxis.setUpperBounds(upperBound);
        assertEquals(upperBound, plotAxis.getAUB(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis();
        NumberFormat numberFormat = new DecimalFormat("#.##");
        plotAxis.setNumberFormat(numberFormat);
        plotAxis.setUpperBounds(10.0);
        assertEquals("10.00", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.isManualQuant());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis();
        double tickStepSize = 2.5;
        plotAxis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, plotAxis.getQuantum(), 0.0);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testClone() throws CloneNotSupportedException {
        PlotAxis plotAxis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)plotAxis.clone();
        assertNotSame(plotAxis, clonedAxis);
    }
}