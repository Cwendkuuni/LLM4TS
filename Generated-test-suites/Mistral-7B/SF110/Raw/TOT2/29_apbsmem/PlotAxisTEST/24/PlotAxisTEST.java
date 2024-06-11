import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {
    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis();
        String newLabel = "Test Label";
        plotAxis.setLabel(newLabel);
        String actualLabel = plotAxis.getLabel();
        assertEquals(newLabel, actualLabel);
    }
}