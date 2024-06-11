package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataColumn;
import de.progra.charting.model.DataSeries;

public class CoordSystemUtilitiesTest {

    private ChartDataModel model;
    private CoordSystem c;
    private CoordSystemUtilities utilities;

    @Before
    public void setup() {
        model = new ChartDataModel();
        c = new CoordSystem(new java.awt.Dimension(600, 400), new java.awt.Rectangle(0, 0, 600, 400));
        DecimalFormat df = new DecimalFormat("#.###", Locale.ENGLISH);
        c.setXDecimalFormat(df);
        DecimalFormat df2 = new DecimalFormat("#,##0.00", Locale.ENGLISH);
        c.setYDecimalFormat(df2);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        utilities = new CoordSystemUtilities(c, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        double xmin = -10.0;
        double xmax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);

        int expected = 71; // Calculated using the given formula in the class
        int actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeLeftMarginWithMinValueGreaterThanZero() {
        double xmin = 10.0;
        double xmax = 20.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);

        int expected = 20; // Given in the method implementation
        int actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithInvalidConstraints() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(Double.NaN);
        constraints.setMaximumColumnValue(Double.NaN);

        utilities = new CoordSystemUtilities(c, constraints, constraints2, model);
        int actual = utilities.computeLeftMargin();
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testComputeRightMargin() {
        double xmin = -10.0;
        double xmax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);

        int expected = 33; // Calculated using the given formula in the class
        int actual = utilities.computeRightMargin();
        assertEquals(expected, actual);
    }

    // Add test cases for computeTopMargin(), computeBottomMargin(), computeXAxisLabelHeight(), and computeYAxisLabelWidth() methods

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Add test cases for given edge cases and exception handling scenarios
    }

    @Test
    public void testDrawXAxisTicks() {
        // Add test cases for given edge cases and exception handling scenarios
    }

    @Test
    public void testDrawYAxisTicks() {
        // Add test cases for given edge cases and exception handling scenarios
    }
}