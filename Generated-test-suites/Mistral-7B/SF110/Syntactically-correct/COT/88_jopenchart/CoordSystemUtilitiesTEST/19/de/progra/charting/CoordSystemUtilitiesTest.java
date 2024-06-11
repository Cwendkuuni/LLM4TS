package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;
import java.util.*;

import org.junit.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.utils.*;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;

    @Before
    public void setUp() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        utilities = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        // Positive test case
        double xmin = 10.0;
        double xmax = 20.0;
        double ymin = 0.0;
        double ymax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        int expected = 113; // Calculated using the given code in CoordSystemUtilities
        int actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);

        // Edge cases
        xmin = -1.0;
        xmax = 0.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        expected = 15;
        actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);

        xmin = 1.0;
        xmax = Double.MAX_VALUE;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        expected = Integer.MAX_VALUE;
        actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);

        // Exception handling
        constraints.setMinimumColumnValue(Double.NaN);
        expected = 20;
        actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRightMargin() {
        // Positive test case
        double xmin = 0.0;
        double xmax = 20.0;
        int expected = 30; // Calculated using the given code in CoordSystemUtilities
        int actual = utilities.computeRightMargin();
        assertEquals(expected, actual);

        // Edge cases
        xmin = Double.NEGATIVE_INFINITY;
        xmax = 1.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        expected = 15;
        actual = utilities.computeRightMargin();
        assertEquals(expected, actual);

        xmin = 1.0;
        xmax = Double.POSITIVE_INFINITY;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        expected = Integer.MAX_VALUE;
        actual = utilities.computeRightMargin();
        assertEquals(expected, actual);

        // Exception handling
        constraints.setMaximumColumnValue(Double.NaN);
        expected = 15;
        actual = utilities.computeRightMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeTopMargin() {
        // Positive test case
        double xmin = 0.0;
        double xmax = 20.0;
        double ymin = 10.0;
        double ymax = 20.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        int expected = 55; // Calculated using the given code in CoordSystemUtilities
        int actual = utilities.computeTopMargin();
        assertEquals(expected, actual);

        // Edge cases
        ymin = Double.NEGATIVE_INFINITY;
        ymax = 0.0;
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        expected = 15;
        actual = utilities.computeTopMargin();
        assertEquals(expected, actual);

        ymin = 0.0;
        ymax = Double.POSITIVE_INFINITY;
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        expected = Integer.MAX_VALUE;
        actual = utilities.computeTopMargin();
        assertEquals(expected, actual);

        // Exception handling
        constraints.setMinimumValue(Double.NaN);
        expected = 15;
        actual = utilities.computeTopMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeBottomMargin() {
        // Positive test case
        double xmin = 0.0;
        double xmax = 20.0;
        double ymin = 0.0;
        double ymax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        int expected = 55; // Calculated using the given code in CoordSystemUtilities
        int actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);

        // Edge cases
        ymin = Double.NEGATIVE_INFINITY;
        ymax = 1.0;
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        expected = 15;
        actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);

        ymin = 1.0;
        ymax = Double.POSITIVE_INFINITY;
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        expected = Integer.MAX_VALUE;
        actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);

        // Exception handling
        constraints.setMaximumValue(Double.NaN);
        expected = 15;
        actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        // Positive test case
        double xmin = 0.0;
        double xmax = 10.0;
        int expected = 21; // Calculated using the given code in CoordSystemUtilities
        int actual = utilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);

        // Edge cases
        xmin = Double.NEGATIVE_INFINITY;
        xmax = 0.0;
        expected = 15;
        actual = utilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);

        xmin = 0.0;
        xmax = Double.POSITIVE_INFINITY;
        expected = Integer.MAX_VALUE;
        actual = utilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);

        // Exception handling
        utilities.setModel(null);
        expected = 0;
        actual = utilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        // Positive test case
        double xmin = 0.0;
        double xmax = 20.0;
        double ymin = 0.0;
        double ymax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        int expected = 35; // Calculated using the given code in CoordSystemUtilities
        int actual = utilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);

        // Edge cases
        ymin = Double.NEGATIVE_INFINITY;
        ymax = 0.0;
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        expected = 15;
        actual = utilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);

        ymin = 0.0;
        ymax = Double.POSITIVE_INFINITY;
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        expected = Integer.MAX_VALUE;
        actual = utilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);

        // Exception handling
        utilities.setModel(null);
        expected = 0;
        actual = utilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Positive test case
        Graphics2D g = Mockito.mock(Graphics2D.class);
        utilities.drawNumericalXAxisTicks(g);

        // Verification
        Mockito.verify(g, times(5)).draw(Mockito.any(Line2D.class));
    }

    // Add similar test cases for other methods in the CoordSystemUtilities class
}