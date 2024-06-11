import jahuwaldt.plot.*;
import org.junit.Test;

public class PlotAxisTest {
    @Test
    public void testDefaultConstructor() {
        PlotAxis axis = new PlotAxis();
        // Check default values
        assertFalse(axis.isManualLB());
        assertFalse(axis.isManualUB());
        assertFalse(axis.isManualQuant());
        assertEquals(1.0, axis.getxA(), 0.01);
        assertEquals(1.0, axis.getxB(), 0.01);
        assertEquals(Double.MAX_VALUE, axis.getALB(), 0.01);
        assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.01);
        assertEquals(1, axis.getGridStyle());
        assertNotNull(axis.getScale());
        assertInstanceOf(LinearAxisScale.class, axis.getScale());
        assertNotNull(axis.getNumberFormat());
        assertEquals("", axis.getLabel());
        // Test setters and getters
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
        axis.setScale(new LogAxisScale());
        assertNotNull(axis.getScale());
        assertInstanceOf(LogAxisScale.class, axis.getScale());
        axis.setNumberFormat(NumberFormat.getPercentInstance());
        assertNotNull(axis.getNumberFormat());
        assertInstanceOf(NumberFormat.class, axis.getNumberFormat());
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
        axis.manualLowerBounds(true);
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getLowerBounds(), 0.01);
        axis.manualUpperBounds(true);
        axis.setUpperBounds(2.0);
        assertEquals(2.0, axis.getUpperBounds(), 0.01);
        axis.manualTickStepSize(true);
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.01);
    }
}