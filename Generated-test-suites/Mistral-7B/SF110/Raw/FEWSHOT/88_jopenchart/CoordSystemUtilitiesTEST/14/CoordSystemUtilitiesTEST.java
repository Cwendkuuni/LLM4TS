package de.progra.charting.test;

import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;

import java.text.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(50.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int result = util.computeLeftMargin();
        int expected = 45;

        assertEquals("Incorrect left margin calculated", expected, result);
    }

    @Test
    public void testComputeLeftMargin_MinValueGreaterThanZero() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(5.0);
        constraints.setMaximumColumnValue(50.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int result = util.computeLeftMargin();
        int expected = 20;

        assertEquals("Incorrect left margin calculated", expected, result);
    }

    @Test
    public void testComputeLeftMargin_MaxValueLessThanZero() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(5.0);
        constraints.setMaximumColumnValue(-10.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int result = util.computeLeftMargin();
        int expected = 20;

        assertEquals("Incorrect left margin calculated", expected, result);
    }

    @Test
    public void testComputeLeftMargin_NoLabels() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(5.0);
        constraints.setMaximumColumnValue(50.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        util.c.setPaintLabels(false);

        int result = util.computeLeftMargin();
        int expected = 20;

        assertEquals("Incorrect left margin calculated", expected, result);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), null, null, null);

        int result = util.computeRightMargin();
        int expected = 15;

        assertEquals("Incorrect right margin calculated", expected, result);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), null, null, null);

        int result = util.computeTopMargin();
        int expected = 15;

        assertEquals("Incorrect top margin calculated", expected, result);
    }

    @Test
    public void testComputeBottomMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-20.0);
        constraints.setMaximumValue(50.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int result = util.computeBottomMargin();
        int expected = 45;

        assertEquals("Incorrect bottom margin calculated", expected, result);
    }

    @Test
    public void testComputeBottomMargin_MinValueGreaterThanZero() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(5.0);
        constraints.setMaximumValue(50.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int result = util.computeBottomMargin();
        int expected = 20;

        assertEquals("Incorrect bottom margin calculated", expected, result);
    }

    @Test
    public void testComputeBottomMargin_MaxValueLessThanZero() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(5.0);
        constraints.setMaximumValue(-10.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int result = util.computeBottomMargin();
        int expected = 20;

        assertEquals("Incorrect bottom margin calculated", expected, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(10.0);
        constraints.setMaximumColumnValue(100.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int result = util.computeXAxisLabelHeight();
        int expected = 40; // Magic number, adjust as needed based on test data and font

        assertEquals("Incorrect X axis label height calculated", expected, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(10.0);
        constraints.setMaximumValue(100.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int result = util.computeYAxisLabelWidth();
        int expected = 50; // Magic number, adjust as needed based on test data and font

        assertEquals("Incorrect Y axis label width calculated", expected, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Add test code here
    }

    @Test
    public void testDrawXAxisTicks() {
        // Add test code here
    }

    @Test
    public void testDrawYAxisTicks() {
        // Add test code here
    }
}