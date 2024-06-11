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
}