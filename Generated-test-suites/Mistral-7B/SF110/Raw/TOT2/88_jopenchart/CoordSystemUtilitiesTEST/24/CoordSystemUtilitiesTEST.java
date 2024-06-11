import org.junit.Test;
import static org.junit.Assert.*;
import de.progra.charting.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(0.1, 1.0), new ChartDataModelConstraints(1.1, 2.0), new ChartDataModel());
        int actual = utilities.computeLeftMargin();
        int expected = 120; // calculate the expected value based on the given constraints and the class logic
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeLeftMarginWithZeroXValues() {
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(-1.0, 1.0), new ChartDataModelConstraints(1.1, 2.0), new ChartDataModel());
        int actual = utilities.computeLeftMargin();
        int expected = 20;
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithInvalidConstraints() {
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), null, new ChartDataModelConstraints(1.1, 2.0), new ChartDataModel());
        int actual = utilities.computeLeftMargin();
        fail("An IllegalArgumentException should be thrown when the constraints are null.");
    }

    // Add test cases for other methods similarly
}