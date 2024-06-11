package jahuwaldt.plot;
import org.junit.Test;
import org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        String label = "Test Label";
        axis.setLabel(label);
        String actualLabel = axis.getLabel();
        assertEquals(label, actualLabel);
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        int gridStyle = 3;
        axis.setGridStyle(gridStyle);
        int actualGridStyle = axis.getGridStyle();
        assertEquals(gridStyle, actualGridStyle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGridStyleWithNegativeValue() {
        PlotAxis axis = new PlotAxis();
        int gridStyle = -1;
        axis.setGridStyle(gridStyle);
    }

    @Test
    public void testSetAxisTickLimits() {
        PlotAxis axis = new PlotAxis();
        double lb = 0.0;
        double ub = 10.0;
        axis.setAxisTickLimits();
        double expectedLowerBound = 0.0;
        double expectedUpperBound = 10.0;
        double actualLowerBound = axis.getLowerBounds();
        double actualUpperBound = axis.getUpperBounds();
        assertEquals(expectedLowerBound, actualLowerBound, 0.01);
        assertEquals(expectedUpperBound, actualUpperBound, 0.01);
    }

    @Test
    public void testSetAxisTickLimitsWithManualBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        axis.setLowerBounds(1.0);
        axis.manualUpperBounds(true);
        axis.setUpperBounds(5.0);
        axis.setAxisTickLimits();
        double expectedLowerBound = 1.0;
        double expectedUpperBound = 5.0;
        double actualLowerBound = axis.getLowerBounds();
        double actualUpperBound = axis.getUpperBounds();
        assertEquals(expectedLowerBound, actualLowerBound, 0.01);
        assertEquals(expectedUpperBound, actualUpperBound, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLowerBoundsWithNegativeValue() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(false);
        axis.setLowerBounds(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUpperBoundsWithNegativeValue() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(false);
        axis.setUpperBounds(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTickStepSizeWithNegativeValue() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(false);
        axis.setTickStepSize(-1.0);
    }

    @Test
    public void testCloneObject() {
        PlotAxis axis = new PlotAxis();
        PlotAxis cloneAxis = axis.clone();
        assertNotSame(axis, cloneAxis);
        assertEquals(axis, cloneAxis);
    }
}