import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {
    private CoordSystemUtilities util;

    @Before
    public void setup() {
        CoordSystem coord = new CoordSystem(new Dimension(300, 300), null, null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        util = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        // Basic test case
        double xmin = 1.0;
        double xmax = 10.0;
        double expected = 70.0;
        int actual = util.computeLeftMargin();
        assertEquals(expected, actual);

        // Edge case: xmin and xmax are zero
        xmin = 0.0;
        xmax = 0.0;
        expected = 15.0; // minimum margin size
        actual = util.computeLeftMargin();
        assertEquals(expected, actual);

        // Error case: xmin is negative and xmax is positive
        xmin = -1.0;
        xmax = 1.0;
        expected = 20; // default margin size
        actual = util.computeLeftMargin();
        assertEquals(expected, actual);
    }

    // Add test cases for other methods similarly...

    @After
    public void tearDown() {
        util = null;
    }
}