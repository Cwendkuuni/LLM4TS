package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        String label = "Test";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        assertEquals(nf, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        int gridStyle = 1;
        axis.setGridStyle(gridStyle);
        assertEquals(gridStyle, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        boolean flag = true;
        axis.manualLowerBounds(flag);
        assertEquals(flag, axis.isManualLowerBounds());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        double value = 1.0;
        axis.setLowerBounds(value);
        assertEquals(value, axis.getLowerBounds());
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        double value = 1.0;
        axis.setLowerBounds(value);
        assertEquals(nf.format((float)value), axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        boolean flag = true;
        axis.manualUpperBounds(flag);
        assertEquals(flag, axis.isManualUpperBounds());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        double value = 2.0;
        axis.setUpperBounds(value);
        assertEquals(value, axis.getUpperBounds());
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        NumberFormat nf = NumberFormat.getInstance();
        axis.setNumberFormat(nf);
        double value = 2.0;
        axis.setUpperBounds(value);
        assertEquals(nf.format((float)value), axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        boolean flag = true;
        axis.manualTickStepSize(flag);
        assertEquals(flag, axis.isManualTickStepSize());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        double value = 0.5;
        axis.setTickStepSize(value);
        assertEquals(value, axis.getTickStepSize());
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList p0 = null; // Replace with a valid PlotRunList instance for testing
        axis.setAxisBounds(p0);
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle(0, 0, 10, 10);
        axis.setSize(plotFrame);
        // Add more assertions for testing the resizeAxis() method and axis limits here
    }
}