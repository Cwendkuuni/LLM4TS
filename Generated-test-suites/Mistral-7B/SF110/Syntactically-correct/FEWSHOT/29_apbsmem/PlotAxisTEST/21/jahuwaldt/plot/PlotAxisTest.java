package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testPlotAxisConstructor() {
        PlotAxis axis = new PlotAxis();
        assertNotNull(axis);
    }

    @Test
    public void testPlotAxisConstructorWithLabelAndScale() {
        String label = "Test Label";
        PlotAxisScale scale = new LinearAxisScale();
        PlotAxis axis = new PlotAxis(label, scale, null, 1);
        assertEquals(label, axis.getLabel());
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testPlotAxisConstructorWithLabelScaleFormatAndGridStyle() {
        String label = "Test Label";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat nf = NumberFormat.getInstance();
        int gridStyle = 2;
        PlotAxis axis = new PlotAxis(label, scale, nf, gridStyle);
        assertEquals(label, axis.getLabel());
        assertEquals(scale, axis.getScale());
        assertEquals(nf, axis.getNumberFormat());
        assertEquals(gridStyle, axis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        String oldLabel = "Old Label";
        String newLabel = "New Label";
        axis.setLabel(newLabel);
        assertEquals(newLabel, axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale oldScale = new LinearAxisScale();
        PlotAxisScale newScale = new LogAxisScale();
        axis.setScale(newScale);
        assertEquals(newScale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat oldFormat = NumberFormat.getInstance();
        DecimalFormat newFormat = new DecimalFormat("#0.000");
        axis.setNumberFormat(newFormat);
        assertEquals(newFormat, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        int oldGridStyle = 1;
        int newGridStyle = 3;
        axis.setGridStyle(newGridStyle);
        assertEquals(newGridStyle, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.isManualLowerBounds());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        double lowerBounds = 1.23;
        axis.setLowerBounds(lowerBounds);
        assertEquals(lowerBounds, axis.getLowerBounds());
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        double lowerBounds = 1.23;
        axis.setLowerBounds(lowerBounds);
        assertEquals("1.23", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.isManualUpperBounds());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        double upperBounds = 4.56;
        axis.setUpperBounds(upperBounds);
        assertEquals(upperBounds, axis.getUpperBounds());
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        double upperBounds = 4.56;
        axis.setUpperBounds(upperBounds);
        assertEquals("4.56", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.isManualTickStepSize());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        double tickStepSize = 0.1;
        axis.setTickStepSize(tickStepSize);
        assertEquals(tickStepSize, axis.getTickStepSize());
    }

    // Add more test cases for the abstract methods and other functionalities here.

    @Test
    public void testClone() {
        PlotAxis originalAxis = new PlotAxis();
        PlotAxis clonedAxis = originalAxis.clone();
        assertNotSame(originalAxis, clonedAxis);
        assertEquals(originalAxis.getClass(), clonedAxis.getClass());
    }
}