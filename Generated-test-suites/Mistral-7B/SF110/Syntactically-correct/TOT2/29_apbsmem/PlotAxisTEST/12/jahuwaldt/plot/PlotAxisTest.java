import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {
    @Test
    public void testSetAndGetLabel() {
        PlotAxis axis = new PlotAxis();
        String label = "Test Label";
        axis.setLabel(label);
        String actualLabel = axis.getLabel();
        assertEquals(label, actualLabel);
    }
}