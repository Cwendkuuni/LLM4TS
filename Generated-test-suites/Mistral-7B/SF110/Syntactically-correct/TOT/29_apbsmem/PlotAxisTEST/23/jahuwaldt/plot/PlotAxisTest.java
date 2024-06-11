import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testDefaultConstructor() {
        // Create a new instance of PlotAxis using the default constructor
        PlotAxis axis = new PlotAxis();

        // Check default values
        assertEquals(1.0, axis.xA, 0.01);
        assertEquals(1.0, axis.xB, 0.01);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.01);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.01);
        assertEquals(1, axis.gridStyle);
        assertNull(axis.label);
        assertNotNull(axis.scale);
        assertInstanceOf(LinearAxisScale.class, axis.scale);
        assertNotNull(axis.nf);
        assertEquals("", axis.label);
        assertEquals(1.0, axis.xA, 0.01);
        assertEquals(1.0, axis.xB, 0.01);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.01);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.01);
    }
}