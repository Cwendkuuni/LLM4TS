package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;

    @Before
    public void setup() {
        ChartDataModel model = new ChartDataModel();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        CoordSystem coord = new CoordSystem(new Dimension(300, 300), new Dimension(500, 500), new Font("Arial", Font.BOLD, 12));
        utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        double xmin = -5.0;
        double xmax = 10.0;
        double ymin = -5.0;
        double ymax = 5.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        int expectedLeftMargin = 133; // Calculated using the given coordinates

        int actualLeftMargin = utilities.computeLeftMargin();

        assertEquals("Left margin incorrect", expectedLeftMargin, actualLeftMargin);

        // Test with negative xmin and xmax values
        xmin = -15.0;
        xmax = 5.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        int expectedLeftMarginNegative = 242; // Calculated using the new coordinates

        int actualLeftMarginNegative = utilities.computeLeftMargin();

        assertEquals("Left margin incorrect for negative xmin and xmax", expectedLeftMarginNegative, actualLeftMarginNegative);

        // Test with zero xmin and xmax values
        xmin = 0.0;
        xmax = 0.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        int expectedLeftMarginZero = 20;

        int actualLeftMarginZero = utilities.computeLeftMargin();

        assertEquals("Left margin incorrect for zero xmin and xmax", expectedLeftMarginZero, actualLeftMarginZero);

        // Test with null constraints
        constraints = null;
        int expectedLeftMarginNull = 20;

        int actualLeftMarginNull = utilities.computeLeftMargin();

        assertEquals("Left margin incorrect for null constraints", expectedLeftMarginNull, actualLeftMarginNull);
    }

    @Test
    public void testComputeRightMargin() {
        double xmin = -5.0;
        double xmax = 10.0;
        double ymin = -5.0;
        double ymax = 5.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        int expectedRightMargin = 35; // Calculated using the given coordinates

        int actualRightMargin = utilities.computeRightMargin();

        assertEquals("Right margin incorrect", expectedRightMargin, actualRightMargin);

        // Test with negative xmin and xmax values
        xmin = -15.0;
        xmax = 5.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        int expectedRightMarginNegative = 48; // Calculated using the new coordinates

        int actualRightMarginNegative = utilities.computeRightMargin();

        assertEquals("Right margin incorrect for negative xmin and xmax", expectedRightMarginNegative, actualRightMarginNegative);

        // Test with zero xmin and xmax values
        xmin = 0.0;
        xmax = 0.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        int expectedRightMarginZero = 15;

        int actualRightMarginZero = utilities.computeRightMargin();

        assertEquals("Right margin incorrect for zero xmin and xmax", expectedRightMarginZero, actualRightMarginZero);

        // Test with null constraints
        constraints = null;
        int expectedRightMarginNull = 15;

        int actualRightMarginNull = utilities.computeRightMargin();

        assertEquals("Right margin incorrect for null constraints", expectedRightMarginNull, actualRightMarginNull);
    }

    @Test
    public void testComputeTopMargin() {
        double xmin = -5.0;
        double xmax = 10.0;
        double ymin = -5.0;
        double ymax = 5.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        int expectedTopMargin = 43; // Calculated using the given coordinates

        int actualTopMargin = utilities.computeTopMargin();

        assertEquals("Top margin incorrect", expectedTopMargin, actualTopMargin);

        // Test with negative ymin and ymax values
        ymin = -15.0;
        ymax = 5.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        int expectedTopMarginNegative = 72; // Calculated using the new coordinates

        int actualTopMarginNegative = utilities.computeTopMargin();

        assertEquals("Top margin incorrect for negative ymin and ymax", expectedTopMarginNegative, actualTopMarginNegative);

        // Test with zero ymin and ymax values
        ymin = 0.0;
        ymax = 0.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        int expectedTopMarginZero = 15;

        int actualTopMarginZero = utilities.computeTopMargin();

        assertEquals("Top margin incorrect for zero ymin and ymax", expectedTopMarginZero, actualTopMarginZero);

        // Test with null constraints
        constraints = null;
        int expectedTopMarginNull = 15;

        int actualTopMarginNull = utilities.computeTopMargin();

        assertEquals("Top margin incorrect for null constraints", expectedTopMarginNull, actualTopMarginNull);
    }

    @Test
    public void testComputeBottomMargin() {
        double xmin = -5.0;
        double xmax = 10.0;
        double ymin = -5.0;
        double ymax = 5.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        int expectedBottomMargin = 43; // Calculated using the given coordinates

        int actualBottomMargin = utilities.computeBottomMargin();

        assertEquals("Bottom margin incorrect", expectedBottomMargin, actualBottomMargin);

        // Test with negative ymin and ymax values
        ymin = -15.0;
        ymax = 5.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        int expectedBottomMarginNegative = 72; // Calculated using the new coordinates

        int actualBottomMarginNegative = utilities.computeBottomMargin();

        assertEquals("Bottom margin incorrect for negative ymin and ymax", expectedBottomMarginNegative, actualBottomMarginNegative);

        // Test with zero ymin and ymax values
        ymin = 0.0;
        ymax = 0.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        int expectedBottomMarginZero = 15;

        int actualBottomMarginZero = utilities.computeBottomMargin();

        assertEquals("Bottom margin incorrect for zero ymin and ymax", expectedBottomMarginZero, actualBottomMarginZero);

        // Test with null constraints
        constraints = null;
        int expectedBottomMarginNull = 15;

        int actualBottomMarginNull = utilities.computeBottomMargin();

        assertEquals("Bottom margin incorrect for null constraints", expectedBottomMarginNull, actualBottomMarginNull);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        double xmin = 0.0;
        double xmax = 10.0;
        double ymin = 0.0;
        double ymax = 10.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        int expectedXAxisLabelHeight = 80; // Calculated using the given coordinates and format

        int actualXAxisLabelHeight = utilities.computeXAxisLabelHeight();

        assertEquals("X axis label height incorrect", expectedXAxisLabelHeight, actualXAxisLabelHeight);

        // Test with null constraints
        constraints = null;
        int expectedXAxisLabelHeightNull = 0;

        int actualXAxisLabelHeightNull = utilities.computeXAxisLabelHeight();

        assertEquals("X axis label height incorrect for null constraints", expectedXAxisLabelHeightNull, actualXAxisLabelHeightNull);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        double xmin = 0.0;
        double xmax = 10.0;
        double ymin = 0.0;
        double ymax = 10.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        int expectedYAxisLabelWidth = 100; // Calculated using the given coordinates and format

        int actualYAxisLabelWidth = utilities.computeYAxisLabelWidth();

        assertEquals("Y axis label width incorrect", expectedYAxisLabelWidth, actualYAxisLabelWidth);

        // Test with null constraints
        constraints = null;
        int expectedYAxisLabelWidthNull = 0;

        int actualYAxisLabelWidthNull = utilities.computeYAxisLabelWidth();

        assertEquals("Y axis label width incorrect for null constraints", expectedYAxisLabelWidthNull, actualYAxisLabelWidthNull);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNumericalXAxisTicksWithNullGraphics2D() {
        Graphics2D g = null;
        utilities.drawNumericalXAxisTicks(g);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        double xmin = 0.0;
        double xmax = 10.0;
        double ymin = 0.0;
        double ymax = 10.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        Font font = new Font("Arial", Font.BOLD, 12);
        DecimalFormat decimalFormat = new DecimalFormat("#");
        DecimalFormat decimalFormat2 = new DecimalFormat("#.##");
        utilities = new CoordSystemUtilities(new CoordSystem(new Dimension(300, 300), new Dimension(500, 500), font), constraints, constraints, new ChartDataModel());
        Graphics2D g = new MockGraphics2D();
        utilities.drawNumericalXAxisTicks(g);
        // Add assertions here based on the expected output
    }

    // Add other test methods for the remaining methods in the CoordSystemUtilities class
}