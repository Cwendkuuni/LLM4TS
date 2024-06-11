import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PlotAxisTest {
    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        String oldLabel = axis.getLabel();
        axis.setLabel("New Label");
        String newLabel = axis.getLabel();
        assertEquals("New Label", newLabel);
        assertEquals(oldLabel, axis.getLabel()); // checks if old label is replaced
    }
}