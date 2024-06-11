package jahuwaldt.plot.test;

import org.junit.Test;
import static org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import jahuwaldt.plot.LinearAxisScale;
import jahuwaldt.plot.LogAxisScale;

public class PlotAxisTest {

    @Test
    public void testLabel() {
        // Create a new PlotAxis instance with default values
        PlotAxis axis = new PlotAxis();

        // Set a new label and check if it's correctly set
        String newLabel = "Test Label";
        axis.setLabel(newLabel);
        String label = axis.getLabel();
        assertTrue("Label should be set correctly", label.equals(newLabel));

        // Test getting the label with the default value
        axis.setLabel(null);
        label = axis.getLabel();
        assertNull("Label should be null when not set", label);
    }

    @Test
    public void testScale() {
        // Create a new PlotAxis instance with default values
        PlotAxis axis = new PlotAxis();

        // Set a new scale and check if it's correctly set
        PlotAxisScale newScale = new LogAxisScale();
        axis.setScale(newScale);
        PlotAxisScale scale = axis.getScale();
        assertEquals("Scale should be set correctly", newScale, scale);

        // Test getting the scale with the default value
        axis.setScale(null);
        scale = axis.getScale();
        assertNull("Scale should be null when not set", scale);
    }

    @Test
    public void testGridStyle() {
        // Create a new PlotAxis instance with default values
        PlotAxis axis = new PlotAxis();

        // Set a new grid style and check if it's correctly set
        int newGridStyle = 2;
        axis.setGridStyle(newGridStyle);
        int gridStyle = axis.getGridStyle();
        assertEquals("Grid style should be set correctly", newGridStyle, gridStyle);

        // Test getting the grid style with the default value
        axis.setGridStyle(0);
        gridStyle = axis.getGridStyle();
        assertEquals("Grid style should be 0 when not set", 0, gridStyle);
    }
}