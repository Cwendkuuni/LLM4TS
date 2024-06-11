import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PlotAxisTest {
    @Test
    public void testConstructorAndGetLabel() {
        // Test default constructor
        PlotAxis plotAxis = new PlotAxis();
        assertNull(plotAxis.getLabel());

        // Test constructor with arguments
        PlotAxis plotAxisWithLabel = new PlotAxis("Test Label", null, null, 1);
        assertEquals("Test Label", plotAxisWithLabel.getLabel());
    }
}