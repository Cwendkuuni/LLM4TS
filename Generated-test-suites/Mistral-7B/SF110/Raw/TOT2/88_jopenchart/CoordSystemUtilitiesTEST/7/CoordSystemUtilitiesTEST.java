import static org.junit.Assert.assertEquals;
import de.progra.charting.*;
import de.progra.charting.model.*;
import org.junit.Test;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(0, 10, 1, 2), new ChartDataModelConstraints(1, 5, 1, 2), new ChartDataModel());
        int leftMargin = utils.computeLeftMargin();
        assertEquals(26, leftMargin);
    }

    // Repeat the test for other methods

    // ...
}