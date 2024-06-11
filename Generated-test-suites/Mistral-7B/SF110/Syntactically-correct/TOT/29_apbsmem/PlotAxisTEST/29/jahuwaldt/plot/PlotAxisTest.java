import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {
    @Test
    public void testDefaultConstructor() {
        // Create a new instance of PlotAxis using the default constructor
        PlotAxis axis = new PlotAxis();

        // Check default values
        assertEquals(1.0, axis.xA, 0.01); // default value
        assertEquals(1.0, axis.xB, 0.01); // default value
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.01); // default value
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.01); // default value
        assertEquals(1, axis.gridStyle); // default value
        assertNotNull(axis.scale); // default value is LinearAxisScale
        assertNotNull(axis.nf); // default value is NumberFormat.getInstance()
        assertNull(axis.label); // default value is null
    }
}