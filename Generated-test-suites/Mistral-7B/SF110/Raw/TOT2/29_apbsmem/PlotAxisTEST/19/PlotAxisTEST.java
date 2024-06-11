package jahuwaldt.plot;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {
    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new PlotAxis();
        String expectedLabel = "";
        plotAxis.setLabel(expectedLabel);
        String actualLabel = plotAxis.getLabel();
        assertEquals(expectedLabel, actualLabel);
    }

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis();
        String expectedLabel = "Test Label";
        plotAxis.setLabel(expectedLabel);
        String actualLabel = plotAxis.getLabel();
        assertEquals(expectedLabel, actualLabel);
    }

    @Test
    public void testGetLabelWithNullLabel() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLabel(null);
        String actualLabel = plotAxis.getLabel();
        assertEquals("", actualLabel);
    }

    @Test
    public void testGetLabelWithEmptyStringLabel() {
        PlotAxis plotAxis = new PlotAxis();
        String expectedLabel = "";
        plotAxis.setLabel(expectedLabel);
        String actualLabel = plotAxis.getLabel();
        assertEquals(expectedLabel, actualLabel);
    }

    @Test
    public void testSetLabelWithNullLabel() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLabel(null);
        String actualLabel = plotAxis.getLabel();
        assertEquals("", actualLabel);
    }

    @Test
    public void testSetLabelWithEmptyStringLabel() {
        PlotAxis plotAxis = new PlotAxis();
        String expectedLabel = "";
        plotAxis.setLabel(expectedLabel);
        String actualLabel = plotAxis.getLabel();
        assertEquals(expectedLabel, actualLabel);
    }
}