import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();
        // Default values
        assertEquals(1.0, axis.xA, 0.0);
        assertEquals(1.0, axis.xB, 0.0);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
        assertEquals(1, axis.gridStyle);
        assertNull(axis.label);
        assertNotNull(axis.scale);
        assertNotNull(axis.nf);
        // Getters and setters
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
        axis.setScale(new LogAxisScale());
        assertInstanceOf(LogAxisScale.class, axis.getScale());
        axis.setNumberFormat(NumberFormat.getNumberInstance());
        assertNotNull(axis.getNumberFormat());
    }
}