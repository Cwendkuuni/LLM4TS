package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.utils.ChartUtilities;

public class CoordSystemUtilitiesTest {
    private CoordSystem coord;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;
    private CoordSystemUtilities utilities;

    @Before
    public void setup() {
        coord = new CoordSystem(100, 100, 500, 500, 0.1, 0.1);
        constraints = new ChartDataModelConstraints(new DoubleRange(0.0, 10.0), new DoubleRange(0.0, 10.0));
        constraints2 = new ChartDataModelConstraints(new DoubleRange(0.0, -10.0), new DoubleRange(-10.0, 10.0));
        model = new ChartDataModel();
        utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        // Positive test case
        double xmin = 1.0;
        double xmax = 11.0;
        int expected = 41;
        int result = utilities.computeLeftMargin();
        assertEquals(expected, result);

        // Negative test case
        xmin = -5.0;
        xmax = 5.0;
        expected = 31;
        result = utilities.computeLeftMargin();
        assertEquals(expected, result);

        // Exception test case
        xmin = -1.0;
        xmax = 0.0;
        try {
            result = utilities.computeLeftMargin();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeRightMargin() {
        // Positive test case
        int expected = 21;
        int result = utilities.computeRightMargin();
        assertEquals(expected, result);

        // Edge case
        constraints.setMaximumColumnValue(0.0);
        expected = 1;
        result = utilities.computeRightMargin();
        assertEquals(expected, result);

        // Exception test case
        constraints.setMinimumColumnValue(-1.0);
        try {
            result = utilities.computeRightMargin();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeTopMargin() {
        // Positive test case
        double ymin = 0.0;
        double ymax = 11.0;
        int expected = 41;
        int result = utilities.computeTopMargin();
        assertEquals(expected, result);

        // Negative test case
        ymin = -5.0;
        ymax = 5.0;
        expected = 31;
        result = utilities.computeTopMargin();
        assertEquals(expected, result);

        // Edge case
        constraints.setMinimumValue(new DoubleRange(-10.0, 0.0));
        expected = 1;
        result = utilities.computeTopMargin();
        assertEquals(expected, result);

        // Exception test case
        constraints.setMaximumValue(new DoubleRange(10.0, 0.0));
        try {
            result = utilities.computeTopMargin();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeBottomMargin() {
        // Positive test case
        double ymin = 1.0;
        double ymax = 11.0;
        int expected = 41;
        int result = utilities.computeBottomMargin();
        assertEquals(expected, result);

        // Negative test case
        ymin = -5.0;
        ymax = 5.0;
        expected = 31;
        result = utilities.computeBottomMargin();
        assertEquals(expected, result);

        // Edge case
        constraints.setMinimumValue(new DoubleRange(0.0, -10.0));
        expected = 1;
        result = utilities.computeBottomMargin();
        assertEquals(expected, result);

        // Exception test case
        constraints.setMaximumValue(new DoubleRange(0.0, 10.0));
        try {
            result = utilities.computeBottomMargin();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        // Positive test case
        double min = 0.0;
        double max = 10.0;
        int expected = 15;
        int result = utilities.computeXAxisLabelHeight();
        assertEquals(expected, result);

        // Edge case
        min = -10.0;
        max = 10.0;
        expected = 31;
        result = utilities.computeXAxisLabelHeight();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        // Positive test case
        double min = 0.0;
        double max = 10.0;
        int expected = 15;
        int result = utilities.computeYAxisLabelWidth();
        assertEquals(expected, result);

        // Edge case
        constraints.setMinimumValue(new DoubleRange(-10.0, 0.0));
        expected = 31;
        result = utilities.computeYAxisLabelWidth();
        assertEquals(expected, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Positive test case
        Graphics2D g = null;
        utilities.drawNumericalXAxisTicks(g);

        // Edge case
        g = new Graphics2DImpl();
        utilities.drawNumericalXAxisTicks(g);

        // Exception test case
        g = new Graphics2DImpl();
        constraints.setMinimumColumnValue(Double.NaN);
        try {
            utilities.drawNumericalXAxisTicks(g);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDrawXAxisTicks() {
        // Positive test case
        Graphics2D g = null;
        utilities.drawXAxisTicks(g);

        // Edge case
        g = new Graphics2DImpl();
        utilities.drawXAxisTicks(g);

        // Exception test case
        g = new Graphics2DImpl();
        constraints.setMinimumColumnValue(Double.NaN);
        try {
            utilities.drawXAxisTicks(g);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDrawYAxisTicks() {
        // Positive test case
        Graphics2D g = null;
        utilities.drawYAxisTicks(g);

        // Edge case
        g = new Graphics2DImpl();
        utilities.drawYAxisTicks(g);

        // Exception test case
        g = new Graphics2DImpl();
        constraints.setMinimumValue(new DoubleRange(Double.NaN, Double.NaN));
        try {
            utilities.drawYAxisTicks(g);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetXAxisLine2D() {
        // Positive test case
        Line2D expected = new Line2D.Double(0.0, 0.0, 1000.0, 0.0);
        Line2D result = utilities.getXAxisLine2D();
        assertEquals(expected, result);

        // Edge case
        constraints.setMinimumColumnValue(-10.0);
        expected = new Line2D.Double(1000.0, 0.0, 1000.0, 500.0);
        result = utilities.getXAxisLine2D();
        assertEquals(expected, result);
    }

    @Test
    public void testGetYAxisLine2D() {
        // Positive test case
        Line2D expected = new Line2D.Double(0.0, 0.0, 0.0, 1000.0);
        Line2D result = utilities.getYAxisLine2D();
        assertEquals(expected, result);

        // Edge case
        constraints.setMinimumValue(new DoubleRange(-10.0, 0.0));
        expected = new Line2D.Double(0.0, 500.0, 1000.0, 500.0);
        result = utilities.getYAxisLine2D();
        assertEquals(expected, result);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // Positive test case
        Line2D expected = new Line2D.Double(1000.0, 0.0, 1000.0, 1000.0);
        Line2D result = utilities.getSecondYAxisLine2D();
        assertEquals(expected, result);

        // Edge case
        constraints2.setMinimumColumnValue(-10.0);
        expected = new Line2D.Double(1000.0, 500.0, 1000.0, 0.0);
        result = utilities.getSecondYAxisLine2D();
        assertEquals(expected, result);
    }
}

class Graphics2DImpl implements Graphics2D {
    @Override
    public void draw(Shape s) {
        // Do nothing
    }

    @Override
    public void drawString(String s, float x, float y) {
        // Do nothing
    }

    @Override
    public void setFont(Font f) {
        // Do nothing
    }

    @Override
    public void setColor(Color c) {
        // Do nothing
    }
}