package jahuwaldt.plot;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    PlotAxis axis = new PlotAxis();

    @Test
    public void testSetLabel() {
        axis.setLabel("Original Label");
        String expectedLabel = "Original Label";
        String actualLabel = axis.getLabel();
        assertEquals(expectedLabel, actualLabel);
    }

    @Test
    public void testSetEmptyLabel() {
        axis.setLabel("");
        String expectedLabel = "";
        String actualLabel = axis.getLabel();
        assertEquals(expectedLabel, actualLabel);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullLabel() {
        axis.setLabel(null);
    }

    @Test
    public void testGetLabel() {
        axis.setLabel("Test Label");
        String expectedLabel = "Test Label";
        String actualLabel = axis.getLabel();
        assertEquals(expectedLabel, actualLabel);
    }

    @Test
    public void testSetScaleValid() {
        PlotAxisScale scale = new LogAxisScale();
        axis.setScale(scale);
        PlotAxisScale expectedScale = scale;
        PlotAxisScale actualScale = axis.getScale();
        assertEquals(expectedScale, actualScale);
    }

    @Test(expected = NullPointerException.class)
    public void testSetScaleNull() {
        axis.setScale(null);
    }

    @Test
    public void testGetScale() {
        PlotAxisScale scale = new LogAxisScale();
        axis.setScale(scale);
        PlotAxisScale expectedScale = scale;
        PlotAxisScale actualScale = axis.getScale();
        assertEquals(expectedScale, actualScale);
    }
}