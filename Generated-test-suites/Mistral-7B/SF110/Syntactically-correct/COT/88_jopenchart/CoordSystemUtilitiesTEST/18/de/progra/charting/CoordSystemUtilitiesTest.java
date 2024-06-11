import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import org.junit.*;
import java.awt.*;
import java.text.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);

        // Test case 1: Standard scenario
        constraints.setMinimumColumnValue(-2.5);
        constraints.setMaximumColumnValue(2.5);
        assertEquals(37, utils.computeLeftMargin());

        // Test case 2: Minimum column value is negative and maximum is positive
        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        assertEquals(81, utils.computeLeftMargin());

        // Test case 3: Only minimum column value is zero
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(5.0);
        assertEquals(15, utils.computeLeftMargin());

        // Test case 4: Only maximum column value is zero
        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(0.0);
        assertEquals(15, utils.computeLeftMargin());

        // Test case 5: Exception handling
        try {
            utils.computeLeftMargin(null, constraints, constraints2, model);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test case 6: Minimum and maximum column values are the same
        constraints.setMinimumColumnValue(2.5);
        constraints.setMaximumColumnValue(2.5);
        assertEquals(15, utils.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();

        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, null, null);

        // Test case 1: Standard scenario
        assertEquals(30, utils.computeRightMargin());

        // Test case 2: Exception handling
        try {
            utils.computeRightMargin(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();

        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, null, null);

        // Test case 1: Standard scenario
        assertEquals(30, utils.computeTopMargin());

        // Test case 2: Exception handling
        try {
            utils.computeTopMargin(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();

        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, null, null);

        // Test case 1: Standard scenario
        assertEquals(31, utils.computeBottomMargin());

        // Test case 2: Minimum value is negative and maximum is positive
        constraints.setMinimumValue(-1.0);
        constraints.setMaximumValue(1.0);
        assertEquals(62, utils.computeBottomMargin());

        // Test case 3: Minimum and maximum values are the same
        constraints.setMinimumValue(1.0);
        constraints.setMaximumValue(1.0);
        assertEquals(15, utils.computeBottomMargin());

        // Test case 4: Exception handling
        try {
            utils.computeBottomMargin(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();

        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, null, null);

        // Test case 1: Standard scenario
        assertEquals(30, utils.computeXAxisLabelHeight());

        // Test case 2: Minimum column value is negative
        constraints.setMinimumColumnValue(-2.5);
        assertEquals(45, utils.computeXAxisLabelHeight());

        // Test case 3: Maximum column value is positive
        constraints.setMaximumColumnValue(2.5);
        assertEquals(45, utils.computeXAxisLabelHeight());

        // Test case 4: Exception handling
        try {
            utils.computeXAxisLabelHeight(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();

        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, null, null);

        // Test case 1: Standard scenario
        assertEquals(110, utils.computeYAxisLabelWidth());

        // Test case 2: Minimum value is negative
        constraints.setMinimumValue(-1.0);
        assertEquals(125, utils.computeYAxisLabelWidth());

        // Test case 3: Maximum value is positive
        constraints.setMaximumValue(1.0);
        assertEquals(125, utils.computeYAxisLabelWidth());

        // Test case 4: Exception handling
        try {
            utils.computeYAxisLabelWidth(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();

        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, null, null);

        // Test case 1: Standard scenario
        Graphics2D g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        utils.drawNumericalXAxisTicks(g);

        // Test case 2: No data points
        constraints.setMinimumColumnValue(Double.MIN_VALUE);
        constraints.setMaximumColumnValue(Double.MAX_VALUE);
        utils.drawNumericalXAxisTicks(g);

        // Test case 3: Exception handling
        try {
            utils.drawNumericalXAxisTicks(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        g.dispose();
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();

        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, null, null);

        // Test case 1: Standard scenario
        Graphics2D g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        utils.drawXAxisTicks(g);

        // Test case 2: No data points
        constraints.setMinimumColumnValue(Double.MIN_VALUE);
        constraints.setMaximumColumnValue(Double.MAX_VALUE);
        utils.drawXAxisTicks(g);

        // Test case 3: Exception handling
        try {
            utils.drawXAxisTicks(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        g.dispose();
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();

        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, null, null);

        // Test case 1: Standard scenario
        Graphics2D g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        utils.drawYAxisTicks(g);

        // Test case 2: No data points
        constraints.setMinimumValue(Double.MIN_VALUE);
        constraints.setMaximumValue(Double.MAX_VALUE);
        utils.drawYAxisTicks(g);

        // Test case 3: Exception handling
        try {
            utils.drawYAxisTicks(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        g.dispose();
    }
}