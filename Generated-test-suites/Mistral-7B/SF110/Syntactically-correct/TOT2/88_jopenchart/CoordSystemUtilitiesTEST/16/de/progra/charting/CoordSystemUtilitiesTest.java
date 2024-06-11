import static org.junit.Assert.assertEquals;
import de.progra.charting. CoordSystemUtilities;
import de.progra.charting.model.ChartDataModelConstraints;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMarginPositiveXMinAndXMax() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        CoordSystemUtilities utils = new CoordSystemUtilities(null, constraints, null, null);
        int leftMargin = utils.computeLeftMargin();
        int expectedLeftMargin = 20; // hardcoded value based on the logic in the method
        assertEquals(expectedLeftMargin, leftMargin);
    }

    // Add edge cases and exception handling tests here
}