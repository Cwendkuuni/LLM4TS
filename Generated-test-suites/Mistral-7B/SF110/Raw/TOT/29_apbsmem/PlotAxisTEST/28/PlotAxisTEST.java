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
        assertNull(axis.nf);
        assertNull(axis.label);
        
        // Test setters and getters
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
        
        axis.setScale(new LogAxisScale());
        assertEquals(axis.getClass(), LogAxisScale.class);
        
        axis.setNumberFormat(NumberFormat.getPercentInstance());
        assertEquals(axis.getClass().getComponentType(), NumberFormat.class);
        
        axis.setGridStyle(3);
        assertEquals(3, axis.getGridStyle());
        
        // Test manual lower and upper bounds
        axis.manualLowerBounds(true);
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getLowerBounds(), 0.01);
        
        axis.manualUpperBounds(true);
        axis.setUpperBounds(2.0);
        assertEquals(2.0, axis.getUpperBounds(), 0.01);
        
        // Test manual tick step size
        axis.manualTickStepSize(true);
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.01);
    }
}