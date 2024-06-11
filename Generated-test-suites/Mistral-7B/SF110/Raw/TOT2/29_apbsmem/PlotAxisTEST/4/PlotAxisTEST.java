package jahuwaldt.plot;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        String newLabel = "Test Label";
        axis.setLabel(newLabel);
        String result = axis.getLabel();
        assertEquals(newLabel, result);
    }

    @Test(expected = NullPointerException.class)
    public void testSetLabelNull() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel(null);
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new PlotAxisScale();
        axis.setScale(scale);
        PlotAxisScale result = axis.getScale();
        assertEquals(scale, result);
    }

    @Test(expected = NullPointerException.class)
    public void testSetScaleNull() {
        PlotAxis axis = new PlotAxis();
        axis.setScale(null);
    }
}