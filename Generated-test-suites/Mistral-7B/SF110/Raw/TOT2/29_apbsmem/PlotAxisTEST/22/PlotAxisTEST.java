import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {
    // 
    @Test
    public void testSetAndGetLabel() {
        PlotAxis plotAxis = new PlotAxis();
        String originalLabel = plotAxis.getLabel();
        plotAxis.setLabel("Test Label");
        String newLabel = plotAxis.getLabel();
        assertEquals("Test Label", newLabel);
        assertNotEquals(originalLabel, newLabel);
    }
    // 
}