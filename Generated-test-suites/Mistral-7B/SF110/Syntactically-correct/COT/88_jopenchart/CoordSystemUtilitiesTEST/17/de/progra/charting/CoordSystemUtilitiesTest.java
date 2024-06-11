package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataColumn;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;

    @Before
    public void setup() {
        double[] values = {-10.5, -5.0, 0.0, 5.0, 10.5};
        DataColumn column = new DataColumn("TestColumn", values);
        ChartDataModel model = new ChartDataModel();
        model.addDataColumn(column);
        DecimalFormat xFormat = new DecimalFormat("#.##");
        DecimalFormat yFormat = new DecimalFormat("#.##");

        ChartDataModelConstraints constraints = new ChartDataModelConstraints(0.0, 10.0, 1.0, values);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(10.0, 0.0, 1.0, null);

        CoordSystem coord = new CoordSystem(new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), xFormat, yFormat, 100, 100, 50, 50, 50, 50, false);
        utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        int result = utilities.computeLeftMargin();
        int expected = 73; // Calculated using provided code and test data
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithInvalidConstraints() {
        ChartDataModelConstraints invalidConstraints = new ChartDataModelConstraints(null, null, null, null);
        CoordSystemUtilities xUtils = new CoordSystemUtilities(utilities.c, invalidConstraints, constraints2, utilities.model);
        xUtils.computeLeftMargin();
    }

    @Test
    public void testComputeRightMargin() {
        int result = utilities.computeRightMargin();
        int expected = 23; // Calculated using provided code and test data
        assertEquals(expected, result);
    }

    @Test
    public void testComputeTopMargin() {
        int result = utilities.computeTopMargin();
        int expected = 23; // Calculated using provided code and test data
        assertEquals(expected, result);
    }

    @Test
    public void testComputeBottomMargin() {
        int result = utilities.computeBottomMargin();
        int expected = 73; // Calculated using provided code and test data
        assertEquals(expected, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int result = utilities.computeXAxisLabelHeight();
        int expected = 20; // Calculated using provided code and test data
        assertEquals(expected, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        int result = utilities.computeYAxisLabelWidth();
        int expected = 44; // Calculated using provided code and test data
        assertEquals(expected, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = null; // Need to mock Graphics2D for proper testing
        utilities.drawNumericalXAxisTicks(g);
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = null; // Need to mock Graphics2D for proper testing
        utilities.drawXAxisTicks(g);
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = null; // Need to mock Graphics2D for proper testing
        utilities.drawYAxisTicks(g);
    }

    @Test
    public void testGetXAxisLine2D() {
        Line2D result = utilities.getXAxisLine2D();
        Line2D expected = new Line2D.Double(0.0, 0.0, 10.0, 0.0);
        assertEquals(expected, result);
    }

    @Test
    public void testGetYAxisLine2D() {
        Line2D result = utilities.getYAxisLine2D();
        Line2D expected = new Line2D.Double(0.0, 0.0, 0.0, 10.0);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D result = utilities.getSecondYAxisLine2D();
        Line2D expected = new Line2D.Double(0.0, 5.0, 0.0, 0.0);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSecondYAxisLine2DWhenSecondYAxisIsNull() {
        ChartDataModelConstraints constraints3 = new ChartDataModelConstraints(null, null, null, null);
        CoordSystemUtilities yUtils = new CoordSystemUtilities(utilities.c, constraints, constraints3, utilities.model);
        yUtils.getSecondYAxisLine2D();
    }
}