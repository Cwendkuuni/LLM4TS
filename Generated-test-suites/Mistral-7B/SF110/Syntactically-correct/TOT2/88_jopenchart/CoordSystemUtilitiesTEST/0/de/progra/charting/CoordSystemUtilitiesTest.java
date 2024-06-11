
import org.junit.Test;
import org.junit.Assert.*;
import java.awt.*;
import java.awt.geom.*;
import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(100, 100), new ChartDataModelConstraints(0, 10, 5), new ChartDataModelConstraints(0, 5, 5), new ChartDataModel());
        int leftMargin = utils.computeLeftMargin();
        assertEquals(25, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(100, 100), new ChartDataModelConstraints(5, 10, 5), new ChartDataModelConstraints(5, 5, 5), new ChartDataModel());
        int rightMargin = utils.computeRightMargin();
        assertEquals(15, rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(100, 100), new ChartDataModelConstraints(0, 10, 5), new ChartDataModelConstraints(0, 5, 5), new ChartDataModel());
        int topMargin = utils.computeTopMargin();
        assertEquals(15, topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(100, 100), new ChartDataModelConstraints(0, -10, 5), new ChartDataModelConstraints(0, -5, 5), new ChartDataModel());
        int bottomMargin = utils.computeBottomMargin();
        assertEquals(20, bottomMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(100, 100), new ChartDataModelConstraints(0, 10, 5), new ChartDataModelConstraints(0, 5, 5), new ChartDataModel());
        int height = utils.computeXAxisLabelHeight();
        assertEquals(30, height);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(100, 100), new ChartDataModelConstraints(0, 10, 5), new ChartDataModelConstraints(0, 5, 5), new ChartDataModel());
        int width = utils.computeYAxisLabelWidth();
        assertEquals(30, width);
    }

    // Test cases for edge cases and exception handling scenarios go here

}
