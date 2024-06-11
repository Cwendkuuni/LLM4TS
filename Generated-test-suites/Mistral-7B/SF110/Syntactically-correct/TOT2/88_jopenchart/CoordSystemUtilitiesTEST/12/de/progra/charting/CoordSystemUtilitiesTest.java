
import org.junit.Test;
import static org.junit.Assert.*;
import de.progra.charting.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(0.0, 1.0, 1.0), new ChartDataModelConstraints(0.0, 1.0, 1.0), new ChartDataModel());
        int leftMargin = utils.computeLeftMargin();
        assertEquals(42, leftMargin); // Adjust this value based on the logic in the method
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithInvalidConstraints() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(-1.0, 1.0, 1.0), new ChartDataModelConstraints(0.0, 1.0, 1.0), new ChartDataModel());
        utils.computeLeftMargin();
    }

    // Test other methods similarly

    // ...
}
