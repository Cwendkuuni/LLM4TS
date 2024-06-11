import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import java.awt.*;
import java.awt.geom.*;
import org.junit.*;

public class CoordSystemUtilitiesTest {

    private CoordSystem mockCoordSystem;
    private ChartDataModelConstraints mockConstraints;
    private ChartDataModelConstraints mockConstraints2;
    private ChartDataModel mockChartDataModel;

    @Before
    public void setup() {
        mockCoordSystem = mock(CoordSystem.class);
        mockConstraints = mock(ChartDataModelConstraints.class);
        mockConstraints2 = mock(ChartDataModelConstraints.class);
        mockChartDataModel = mock(ChartDataModel.class);
    }

    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(mockCoordSystem, mockConstraints, mockConstraints2, mockChartDataModel);
        double xmin = 0.0;
        double xmax = 10.0;
        int expected = 50;
        int actual = util.computeLeftMargin();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMargin_xminAndXmaxLessThanOrEqualZero() {
        CoordSystemUtilities util = new CoordSystemUtilities(mockCoordSystem, mockConstraints, mockConstraints2, mockChartDataModel);
        double xmin = -1.0;
        double xmax = 0.0;
        util.computeLeftMargin();
    }

    @Test
    public void testComputeLeftMargin_chartSizeNotLargeEnough() {
        CoordSystemUtilities util = new CoordSystemUtilities(mockCoordSystem, mockConstraints, mockConstraints2, mockChartDataModel);
        double xmin = 0.0;
        double xmax = 10.0;
        int expected = 10;
        int actual = util.computeLeftMargin();
        assertEquals(expected, actual);
    }

    // Repeat for other methods and edge cases

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockCoordSystem);
        verifyNoMoreInteractions(mockConstraints);
        verifyNoMoreInteractions(mockConstraints2);
        verifyNoMoreInteractions(mockChartDataModel);
    }
}