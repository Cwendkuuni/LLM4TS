package de.progra.charting.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.util.ChartUtilities;

public class CoordSystemUtilitiesTest {
    private CoordSystemUtilities utilities;

    @Before
    public void setUp() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystem coord = new CoordSystem();
        utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    

    @Test
    public void testComputeLeftMargin() {
        // Positive test case
        double xmin = 10.0;
        double xmax = 20.0;
        double ymin = 0.0;
        double ymax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        int expected = 70;
        int actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);

        // Negative test case (xmin < 0 and xmax > 0)
        xmin = -5.0;
        xmax = 7.0;
        constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        expected = 86;
        actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);

        // Edge case (xmin = 0 and xmax = 0)
        xmin = 0.0;
        xmax = 0.0;
        constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        expected = 20;
        actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);

        // Exception test case (null constraints)
        constraints = null;
        try {
            utilities.computeLeftMargin();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeRightMargin() {
        // Positive test case
        double xmin = -5.0;
        double xmax = 7.0;
        double ymin = 0.0;
        double ymax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        int expected = 31;
        int actual = utilities.computeRightMargin();
        assertEquals(expected, actual);

        // Edge case (xmin = 0 and xmax = 0)
        xmin = 0.0;
        xmax = 0.0;
        constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        expected = 15;
        actual = utilities.computeRightMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeTopMargin() {
        // Positive test case
        double xmin = 10.0;
        double xmax = 20.0;
        double ymin = 0.0;
        double ymax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        int expected = 51;
        int actual = utilities.computeTopMargin();
        assertEquals(expected, actual);

        // Edge case (ymin = 0 and ymax = 0)
        ymin = 0.0;
        ymax = 0.0;
        constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        expected = 20;
        actual = utilities.computeTopMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeBottomMargin() {
        // Positive test case
        double xmin = 10.0;
        double xmax = 20.0;
        double ymin = 0.0;
        double ymax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        int expected = 51;
        int actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);

        // Negative test case (ymin < 0 and ymax > 0)
        ymin = -5.0;
        ymax = 7.0;
        constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        expected = 76;
        actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);

        // Edge case (ymin = 0 and ymax = 0)
        ymin = 0.0;
        ymax = 0.0;
        constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        expected = 20;
        actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);

        // Exception test case (null constraints)
        constraints = null;
        try {
            utilities.computeBottomMargin();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        // Positive test case
        double xmin = 0.0;
        double xmax = 10.0;
        double ymin = 0.0;
        double ymax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        int expected = 21;
        int actual = utilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);

        // Edge case (xmin = 0 and xmax = 0)
        xmin = 0.0;
        xmax = 0.0;
        constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        expected = 15;
        actual = utilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        // Positive test case
        double xmin = 0.0;
        double xmax = 10.0;
        double ymin = 0.0;
        double ymax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        int expected = 7;
        int actual = utilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);

        // Edge case (xmin = 0 and xmax = 0)
        xmin = 0.0;
        xmax = 0.0;
        constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        expected = 15;
        actual = utilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Positive test case
        Graphics2D g = Mockito.mock(Graphics2D.class);
        double xmin = 0.0;
        double xmax = 10.0;
        double ymin = 0.0;
        double ymax = 10.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(ymin, ymax, xmin, xmax);
        utilities.drawNumericalXAxisTicks(g);

        // Verification (number of drawn ticks)
        Mockito.verify(g, Mockito.times(11)).draw(Mockito.isA(Line2D.class));

        // Verification (number of drawn labels)
        Mockito.verify(g, Mockito.times(6)).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }

    @Test
    public void testDrawXAxisTicks() {
        // Positive test case
        Graphics2D g = Mockito.mock(Graphics2D.class);
        int min = 0;
        int max = 10;
        double ypt = 0.0;
        utilities.drawXAxisTicks(g);

        // Verification (number of drawn ticks)
        Mockito.verify(g, Mockito.times(10)).draw(Mockito.isA(Line2D.class));

        // Verification (number of drawn labels)
        Mockito.verify(g, Mockito.times(9)).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }

    @Test
    public void testDrawYAxisTicks() {
        // Positive test case
        Graphics2D g = Mockito.mock(Graphics2D.class);
        double min = 0.0;
        double max = 10.0;
        double tick = 1.0;
        double xpt = 0.0;
        utilities.drawYAxisTicks(g);

        // Verification (number of drawn ticks)
        Mockito.verify(g, Mockito.times(11)).draw(Mockito.isA(Line2D.class));

        // Verification (number of drawn labels)
        Mockito.verify(g, Mockito.times(6)).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }

    @Test
    public void testGetXAxisLine2D() {
        // Positive test case
        double xpt = 0.0;
        double ymin = 0.0;
        double ymax = 10.0;
        Line2D line = utilities.getXAxisLine2D();
        double expectedX1 = -4.5;
        double expectedX2 = 10.5;
        double expectedY1 = ymin;
        double expectedY2 = ymin;
        assertEquals(expectedX1, line.getX1(), 0.001);
        assertEquals(expectedX2, line.getX2(), 0.001);
        assertEquals(expectedY1, line.getY1(), 0.001);
        assertEquals(expectedY2, line.getY2(), 0.001);
    }

    @Test
    public void testGetYAxisLine2D() {
        // Positive test case
        double xmin = 0.0;
        double xmax = 10.0;
        double ymin = 0.0;
        double ymax = 10.0;
        Line2D line = utilities.getYAxisLine2D();
        double expectedX1 = xmin;
        double expectedX2 = xmin;
        double expectedY1 = 0.0;
        double expectedY2 = 10.0;
        assertEquals(expectedX1, line.getX1(), 0.001);
        assertEquals(expectedX2, line.getX2(), 0.001);
        assertEquals(expectedY1, line.getY1(), 0.001);
        assertEquals(expectedY2, line.getY2(), 0.001);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // Positive test case
        double xmin = 0.0;
        double xmax = 10.0;
        double ymin2 = 0.0;
        double ymax2 = 10.0;
        Line2D line = utilities.getSecondYAxisLine2D();
        double expectedX1 = xmax;
        double expectedX2 = xmax;
        double expectedY1 = ymin2;
        double expectedY2 = ymax2;
        assertEquals(expectedX1, line.getX1(), 0.001);
        assertEquals(expectedX2, line.getX2(), 0.001);
        assertEquals(expectedY1, line.getY1(), 0.001);
        assertEquals(expectedY2, line.getY2(), 0.001);
    }

    
}