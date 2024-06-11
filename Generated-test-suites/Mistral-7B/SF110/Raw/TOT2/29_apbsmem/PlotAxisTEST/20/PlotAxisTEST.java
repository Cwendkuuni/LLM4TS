import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {
    // Test starts here
    

    @Test
    public void testSetAndGetLabel() {
        PlotAxis plotAxis = new PlotAxis();
        String label = "Test Label";
        plotAxis.setLabel(label);
        assertEquals("Test Label", plotAxis.getLabel());
    }

    @Test(expected = NullPointerException.class)
    public void testSetLabelWithNullLabel() {
        PlotAxis plotAxis = new PlotAxis();
        plotAxis.setLabel(null);
    }

    
}

// Test for other methods can be added here