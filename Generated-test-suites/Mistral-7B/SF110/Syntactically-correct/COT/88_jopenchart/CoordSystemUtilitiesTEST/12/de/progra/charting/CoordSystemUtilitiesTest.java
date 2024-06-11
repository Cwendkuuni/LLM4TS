package de.progra.charting.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartDataModel;
import de.progra.charting.model.AxisConstraint;
import de.progra.charting.model.AxisType;
import de.progra.charting.model.DecimalFormatUtil;
import de.progra.charting.util.ChartUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;
    private ChartDataModel model;

    @Before
    public void setUp() {
        model = new ChartDataModel();
        AxisConstraint xConstraint = new AxisConstraint(AxisType.X, 0.0, 10.0, 1.0);
        AxisConstraint yConstraint = new AxisConstraint(AxisType.Y, -5.0, 5.0, 1.0);
        model.addAxisConstraint(xConstraint);
        model.addAxisConstraint(yConstraint);
        CoordSystem coord = new CoordSystem(model, new DecimalFormatUtil(), new ChartUtilities());
        utilities = new CoordSystemUtilities(coord, xConstraint, xConstraint, model);
    }

    @Test
    public void testComputeLeftMargin() {
        double xmin = -5.0;
        double xmax = 5.0;
        int expected = 62;
        int actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);

        xmin = -10.0;
        xmax = 0.0;
        expected = 126;
        actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);

        xmin = 0.0;
        xmax = 5.0;
        expected = 20;
        actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRightMargin() {
        int expected = 21;
        int actual = utilities.computeRightMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeTopMargin() {
        int expected = 21;
        int actual = utilities.computeTopMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeBottomMargin() {
        double ymin = -5.0;
        double ymax = 5.0;
        int expected = 67;
        int actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);

        ymin = -10.0;
        ymax = 0.0;
        expected = 133;
        actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);

        ymin = 0.0;
        ymax = 5.0;
        expected = 20;
        actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        double min = 0.0;
        double max = 10.0;
        int expected = 12;
        int actual = utilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);

        min = -5.0;
        max = 5.0;
        expected = 6;
        actual = utilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        double min = 0.0;
        double max = 10.0;
        int expected = 5;
        int actual = utilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);

        min = -5.0;
        max = 5.0;
        expected = 5;
        actual = utilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenXAxisMinIsLessThanZeroAndMaxIsGreaterThanZero() {
        double xmin = -1.0;
        double xmax = 2.0;
        utilities = new CoordSystemUtilities(null, null, null, null);
        utilities.computeLeftMargin();
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = null;
        utilities.drawNumericalXAxisTicks(g);
        assertNotNull(g);
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = null;
        utilities.drawXAxisTicks(g);
        assertNotNull(g);
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = null;
        utilities.drawYAxisTicks(g);
        assertNotNull(g);
    }

    @Test
    public void testGetXAxisLine2D() {
        Line2D expected = new Line2D.Double(0.0, 0.0, 10.0, 0.0);
        Line2D actual = utilities.getXAxisLine2D();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetYAxisLine2D() {
        Line2D expected = new Line2D.Double(0.0, 0.0, 0.0, 10.0);
        Line2D actual = utilities.getYAxisLine2D();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D expected = new Line2D.Double(10.0, 0.0, 10.0, 10.0);
        Line2D actual = utilities.getSecondYAxisLine2D();
        assertEquals(expected, actual);
    }
}