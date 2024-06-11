package de.progra.charting.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

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
        // Test with valid values
        double xmin = 10.0;
        double xmax = 20.0;
        assertEquals(38, utilities.computeLeftMargin());

        // Test with negative x values
        xmin = -10.0;
        xmax = 20.0;
        assertEquals(100, utilities.computeLeftMargin());

        // Test with only positive x values
        xmin = 20.0;
        xmax = 30.0;
        assertEquals(38, utilities.computeLeftMargin());

        // Test with empty constraints
        constraints = null;
        assertEquals(20, utilities.computeLeftMargin());
    }

    @Test(expected = NullPointerException.class)
    public void testComputeLeftMarginWithNullCoordSystem() {
        utilities = new CoordSystemUtilities(null, constraints, constraints2, model);
    }

    @Test
    public void testComputeRightMargin() {
        // Test with valid values
        assertEquals(33, utilities.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        // Test with valid values
        double ymin = 10.0;
        double ymax = 20.0;
        assertEquals(45, utilities.computeTopMargin());

        // Test with only negative y values
        ymin = -10.0;
        ymax = 20.0;
        assertEquals(33, utilities.computeTopMargin());

        // Test with empty constraints
        constraints = null;
        assertEquals(15, utilities.computeTopMargin());
    }

    @Test(expected = NullPointerException.class)
    public void testComputeTopMarginWithNullCoordSystem() {
        utilities = new CoordSystemUtilities(new CoordSystem(), null, constraints2, model);
    }

    @Test
    public void testComputeBottomMargin() {
        // Test with valid values
        double ymin = 10.0;
        double ymax = 20.0;
        assertEquals(47, utilities.computeBottomMargin());

        // Test with only positive y values
        ymin = 20.0;
        ymax = 30.0;
        assertEquals(47, utilities.computeBottomMargin());

        // Test with empty constraints
        constraints = null;
        assertEquals(20, utilities.computeBottomMargin());
    }

    @Test(expected = NullPointerException.class)
    public void testComputeBottomMarginWithNullCoordSystem() {
        utilities = new CoordSystemUtilities(new CoordSystem(), constraints, null, model);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        // Test with valid values
        double min = 0.0;
        double max = 10.0;
        assertEquals(30, utilities.computeXAxisLabelHeight());

        // Test with only positive values
        min = 10.0;
        max = 20.0;
        assertEquals(21, utilities.computeXAxisLabelHeight());

        // Test with empty constraints
        constraints = null;
        assertEquals(15, utilities.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        // Test with valid values
        double min = 0.0;
        double max = 10.0;
        assertEquals(12, utilities.computeYAxisLabelWidth());

        // Test with only positive values
        min = 10.0;
        max = 20.0;
        assertEquals(11, utilities.computeYAxisLabelWidth());

        // Test with empty constraints
        constraints = null;
        assertEquals(15, utilities.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Test with valid values
        Graphics2D g = null;
        utilities.drawNumericalXAxisTicks(g);
        assertNotNull(g);
    }

    @Test(expected = NullPointerException.class)
    public void testDrawNumericalXAxisTicksWithNullGraphics() {
        utilities.drawNumericalXAxisTicks(null);
    }
}