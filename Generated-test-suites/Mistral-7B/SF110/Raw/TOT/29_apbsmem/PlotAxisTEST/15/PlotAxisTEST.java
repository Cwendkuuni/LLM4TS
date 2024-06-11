import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();
        // Test default values
        assertEquals(1.0, axis.xA, 0.01);
        assertEquals(1.0, axis.xB, 0.01);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.01);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.01);
        assertEquals(1, axis.gridStyle);
        assertNull(axis.scale);
        assertNotNull(axis.nf);
        assertNull(axis.label);
        // Test setters and getters
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
        axis.setScale(new LogarithmicAxisScale());
        assertInstanceOf(LogarithmicAxisScale.class, axis.getScale());
        axis.setNumberFormat(NumberFormat.getPercentInstance());
        assertNotNull(axis.getNumberFormat());
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
    }
}