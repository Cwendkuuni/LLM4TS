
package jahuwaldt.plot;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {
    @Test
    public void testEmptyConstructor() {
        // Create a new instance of PlotAxis using the empty constructor
        PlotAxis testAxis = new PlotAxis();

        // Check default values
        assertEquals(1.0, testAxis.xA, 0.01);
        assertEquals(1.0, testAxis.xB, 0.01);
        assertEquals(Double.MAX_VALUE, testAxis.aLB, 0.01);
        assertEquals(-1.7976931348623157E308, testAxis.aUB, 0.01);
        assertEquals(1, testAxis.gridStyle);
        assertNull(testAxis.scale);
        assertNotNull(testAxis.nf);
        assertEquals("", testAxis.label);
    }

    @Test
    public void testConstructWithLabelScaleAndNumberFormat() {
        // Create a new instance of PlotAxis using the constructor with arguments
        String label = "TestAxis";
        PlotAxisScale scale = new PlotAxisScale();
        NumberFormat nf = NumberFormat.getInstance();
        PlotAxis testAxis = new PlotAxis(label, scale, nf, 1);

        // Check constructor parameters
        assertEquals(label, testAxis.getLabel());
        assertSame(scale, testAxis.getScale());
        assertSame(nf, testAxis.getNumberFormat());
        assertEquals(1, testAxis.getGridStyle());
    }

    @Test
    public void testSetAxisBounds() {
        // Create a new instance of PlotAxis
        PlotAxis testAxis = new PlotAxis();

        // Create a new instance of PlotRunList
        PlotRunList testRunList = new PlotRunList();

        // Set axis bounds
        double lowerBound = 0.0;
        double upperBound = 1.0;
        testAxis.setAxisBounds(testRunList);

        // Check axis bounds
        assertEquals(lowerBound, testAxis.getLowerBounds(), 0.01);
        assertEquals(upperBound, testAxis.getUpperBounds(), 0.01);
    }
}