package de.progra.charting.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartDataModel;
import de.progra.charting.model.DataColumn;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;

    @Before
    public void setup() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        ChartDataModel model = new ChartDataModel();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        double xmin = -5.0;
        double xmax = 5.0;
        int expected = 40; // computed margin value

        double result = utilities.computeLeftMargin();

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithInvalidConstraints() {
        double xmin = -5.0;
        double xmax = 5.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();

        CoordSystemUtilities utilities = new CoordSystemUtilities(null, constraints, null, null);

        utilities.computeLeftMargin();
    }

    @Test
    public void testComputeRightMargin() {
        double expected = 30; // computed margin value

        int result = utilities.computeRightMargin();

        assertEquals(expected, result);
    }

    @Test
    public void testComputeTopMargin() {
        double expected = 30; // computed margin value

        int result = utilities.computeTopMargin();

        assertEquals(expected, result);
    }

    @Test
    public void testComputeBottomMargin() {
        double ymin = -5.0;
        double ymax = 5.0;
        int expected = 40; // computed margin value

        double result = utilities.computeBottomMargin();

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeBottomMarginWithInvalidConstraints() {
        double ymin = -5.0;
        double ymax = 5.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();

        CoordSystemUtilities utilities = new CoordSystemUtilities(null, null, constraints, null);

        utilities.computeBottomMargin();
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        double min = -5.0;
        double max = 5.0;
        int expected = 50; // computed label height

        int result = utilities.computeXAxisLabelHeight();

        assertEquals(expected, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        double min = -5.0;
        double max = 5.0;
        int expected = 70; // computed label width

        int result = utilities.computeYAxisLabelWidth();

        assertEquals(expected, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();

        utilities.drawNumericalXAxisTicks(g);

        assertNotNull(g.getStroke());
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();

        utilities.drawXAxisTicks(g);

        assertNotNull(g.getStroke());
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();

        utilities.drawYAxisTicks(g);

        assertNotNull(g.getStroke());
    }

    @Test
    public void testGetXAxisLine2D() {
        Line2D expected = new Line2D.Double(0.0, 0.0, 100.0, 0.0);
        Line2D result = utilities.getXAxisLine2D();

        assertEquals(expected, result);
    }

    @Test
    public void testGetYAxisLine2D() {
        Line2D expected = new Line2D.Double(0.0, 0.0, 0.0, 100.0);
        Line2D result = utilities.getYAxisLine2D();

        assertEquals(expected, result);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D expected = new Line2D.Double(0.0, 100.0, 0.0, 0.0);
        Line2D result = utilities.getSecondYAxisLine2D();

        assertEquals(expected, result);
    }
}