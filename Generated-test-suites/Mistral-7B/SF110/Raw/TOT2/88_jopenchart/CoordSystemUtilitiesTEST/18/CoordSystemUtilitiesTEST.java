import org.junit.Test;
import static org.junit.Assert.*;
import de.progra.charting.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(-1.0, 1.0, 0.1), new ChartDataModelConstraints(1.0, 2.0, 0.1), new ChartDataModel());
        int actual = utilities.computeLeftMargin();
        int expected = 32; // Calculate the expected value based on the given constraints
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeLeftMargin_MinColumnValueLessThanZeroAndMaxColumnValueGreaterThanZero() {
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(-1.0, 1.0, 0.1), new ChartDataModelConstraints(1.0, 2.0, 0.1), new ChartDataModel());
        int actual = utilities.computeLeftMargin();
        int expected = 32; // Calculate the expected value based on the given constraints
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMargin_ConstraintsNull() {
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), null, new ChartDataModelConstraints(1.0, 2.0, 0.1), new ChartDataModel());
    }

    // Add more test cases for other methods with similar pattern
}