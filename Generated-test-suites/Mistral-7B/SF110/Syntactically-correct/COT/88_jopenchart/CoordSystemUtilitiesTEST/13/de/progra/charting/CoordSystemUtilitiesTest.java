package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataPoint;
import de.progra.charting.model.XYChartData;

public class CoordSystemUtilitiesTest {
    private CoordSystemUtilities utilities;
    private CoordSystem coordSystem;
    private ChartDataModel model;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;

    @Before
    public void setUp() {
        double[] xValues = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yValues = new double[] { 1.5, 2.5, 3.5, 4.5, 5.5 };
        XYChartData data = new XYChartData("Test Data", "X Values", "Y Values", xValues, yValues);
        model = new ChartDataModel(data);
        DecimalFormat xDecimalFormat = new DecimalFormat("#.#");
        DecimalFormat yDecimalFormat = new DecimalFormat("#.#");
        constraints = new ChartDataModelConstraints(-5.0, 10.0, 0.0, 5.0, xDecimalFormat, yDecimalFormat);
        constraints2 = new ChartDataModelConstraints(5.0, -10.0, 5.0, 0.0, xDecimalFormat, yDecimalFormat);
        coordSystem = new CoordSystem(new java.awt.Dimension(500, 500), constraints, constraints2);
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        int leftMargin = utilities.computeLeftMargin();
        int expectedLeftMargin = 212; // Calculated based on given constraints
        assertEquals(expectedLeftMargin, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        int rightMargin = utilities.computeRightMargin();
        int expectedRightMargin = 55; // Calculated based on given constraints
        assertEquals(expectedRightMargin, rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        int topMargin = utilities.computeTopMargin();
        int expectedTopMargin = 56; // Calculated based on given constraints
        assertEquals(expectedTopMargin, topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        int bottomMargin = utilities.computeBottomMargin();
        int expectedBottomMargin = 122; // Calculated based on given constraints
        assertEquals(expectedBottomMargin, bottomMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int height = utilities.computeXAxisLabelHeight();
        int expectedHeight = 38; // Calculated based on given constraints and data
        assertEquals(expectedHeight, height);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        int width = utilities.computeYAxisLabelWidth();
        int expectedWidth = 50; // Calculated based on given constraints and data
        assertEquals(expectedWidth, width);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        utilities.drawNumericalXAxisTicks(g);
        // Add verification code for the drawString and drawLine calls
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        utilities.drawXAxisTicks(g);
        // Add verification code for the drawString and drawLine calls
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        utilities.drawYAxisTicks(g);
        // Add verification code for the drawString and drawLine calls
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginNegativeConstraints() {
        ChartDataModelConstraints negativeConstraints = new ChartDataModelConstraints(-1.0, 1.0, 0.0, 5.0, null, null);
        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, negativeConstraints, constraints2, model);
        utilities.computeLeftMargin();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginZeroXMax() {
        ChartDataModelConstraints zeroConstraints = new ChartDataModelConstraints(0.0, 1.0, 0.0, 0.0, null, null);
        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, zeroConstraints, constraints2, model);
        utilities.computeLeftMargin();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginZeroXMinAndPositiveXMax() {
        ChartDataModelConstraints zeroConstraints = new ChartDataModelConstraints(0.0, 1.0, 0.0, 5.0, null, null);
        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, zeroConstraints, constraints2, model);
        utilities.computeLeftMargin();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginNullCoordSystem() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1.0, 1.0, 0.0, 5.0, null, null);
        CoordSystemUtilities utilities = new CoordSystemUtilities(null, constraints, constraints2, model);
        utilities.computeLeftMargin();
    }

    // Add test cases for other methods and edge cases as necessary
}