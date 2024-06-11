import jahuwaldt.plot.PlotAxis;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PlotAxisTest {
    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        String newLabel = "TestLabel";
        axis.setLabel(newLabel);
        String result = axis.getLabel();
        assertEquals("TestLabel", result);
    }
}