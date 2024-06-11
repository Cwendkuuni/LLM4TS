import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();

        // Check default values
        assertEquals(1.0, axis.xA, 0.01);
        assertEquals(1.0, axis.xB, 0.01);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.01);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.01);
        assertEquals(1, axis.gridStyle);
        assertNull(axis.scale);
        assertNotNull(axis.nf);
        assertEquals("Axis label is not initialized", axis.label);
    }
}