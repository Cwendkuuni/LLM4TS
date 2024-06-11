import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PlotAxisTest {
    @Test
    public void testSetAndGetLabel() {
        PlotAxis axis = new PlotAxis();
        String label = "Test Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }
}