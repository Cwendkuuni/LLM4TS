package de.progra.charting.test;

import static org.junit.Assert.*;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.*;

import java.text.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        CoordSystemUtilities util = new CoordSystemUtilities(null, constraints, null, null);
        int actual = util.computeLeftMargin();
        int expected = 15;
        assertEquals(expected, actual);

        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);
        util = new CoordSystemUtilities(null, constraints, null, null);
        actual = util.computeLeftMargin();
        expected = 20;
        assertEquals(expected, actual);

        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        util = new CoordSystemUtilities(null, constraints, null, null);
        actual = util.computeLeftMargin();
        expected = 32;
        assertEquals(expected, actual);

        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints2.setMinimumValue(-10.0);
        constraints2.setMaximumValue(10.0);
        CoordSystemUtilities util2 = new CoordSystemUtilities(null, null, constraints2, null);
        actual = util2.computeLeftMargin();
        expected = 20;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(null, null, null, null);
        int actual = util.computeRightMargin();
        int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(null, null, null, null);
        int actual = util.computeTopMargin();
        int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeBottomMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(10.0);
        CoordSystemUtilities util = new CoordSystemUtilities(null, constraints, null, null);
        int actual = util.computeBottomMargin();
        int expected = 20;
        assertEquals(expected, actual);

        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(10.0);
        util = new CoordSystemUtilities(null, constraints, null, null);
        actual = util.computeBottomMargin();
        expected = 32;
        assertEquals(expected, actual);

        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints2.setMinimumValue(-10.0);
        constraints2.setMaximumValue(10.0);
        CoordSystemUtilities util2 = new CoordSystemUtilities(null, null, constraints2, null);
        actual = util2.computeBottomMargin();
        expected = 20;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        CoordSystemUtilities util = new CoordSystemUtilities(null, constraints, null, null);
        int actual = util.computeXAxisLabelHeight();
        int expected = 0;
        assertEquals(expected, actual);

        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);
        util = new CoordSystemUtilities(null, constraints, null, null);
        actual = util.computeXAxisLabelHeight();
        expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(10.0);
        CoordSystemUtilities util = new CoordSystemUtilities(null, null, constraints, null);
        int actual = util.computeYAxisLabelWidth();
        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 300, 300);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        Graphics2D g = org.apache.commons.graphics2d.test.TestGraphics2D.getTestInstance();
        util.drawNumericalXAxisTicks(g);

        double[] values = { -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5 };
        Point2D[] expectedXAxisTickLocations = getExpectedXAxisTickLocations(coord, values);
        for (int i = 0; i < values.length; ++i) {
            Point2D actualXAxisTickLocation = g.getClipBounds().getBounds2D().getLocation();
            assertArrayEquals(expectedXAxisTickLocations[i], new Object[] { actualXAxisTickLocation.getX(), actualXAxisTickLocation.getY() }, "X axis tick location is incorrect for index " + i);
        }
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 300, 300);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-5);
        constraints.setMaximumColumnValue(5);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        Graphics2D g = org.apache.commons.graphics2d.test.TestGraphics2D.getTestInstance();
        util.drawXAxisTicks(g);

        int[] expectedXAxisTickLabels = { -4, -3, -2, -1, 0, 1, 2, 3, 4 };
        String[] expectedXAxisTickLabelsValues = { "4", "3", "2", "1", "0", "1", "2", "3", "4" };
        Point2D[] expectedXAxisTickLocations = getExpectedXAxisTickLocations(coord, new int[] { -4, -3, -2, -1, 0, 1, 2, 3, 4 });
        for (int i = 0; i < expectedXAxisTickLabels.length; ++i) {
            int actualXAxisTickLabelIndex = (int)g.getClipBounds().getBounds2D().getLocation().getX();
            assertEquals(expectedXAxisTickLabels[i], actualXAxisTickLabelIndex);
            String actualXAxisTickLabelValue = (String)g.getFont().getStringBounds(g.getFont().getFontName() + " " + Integer.toString(actualXAxisTickLabelIndex), g.getFontRenderContext()).getString();
            assertEquals(expectedXAxisTickLabelsValues[i], actualXAxisTickLabelValue);
            Point2D actualXAxisTickLocation = g.getClipBounds().getBounds2D().getLocation();
            assertArrayEquals(expectedXAxisTickLocations[i], new Object[] { actualXAxisTickLocation.getX(), actualXAxisTickLocation.getY() }, "X axis tick location is incorrect for index " + i);
        }
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 300, 300);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, constraints);
        Graphics2D g = org.apache.commons.graphics2d.test.TestGraphics2D.getTestInstance();
        util.drawYAxisTicks(g);

        double[] values = { -4.5, -3.5, -2.5, -1.5, -0.5, 0.5, 1.5, 2.5, 3.5, 4.5 };
        Point2D[] expectedYAxisTickLocations = getExpectedYAxisTickLocations(coord, values);
        for (int i = 0; i < values.length; ++i) {
            Point2D actualYAxisTickLocation = g.getClipBounds().getBounds2D().getLocation();
            assertArrayEquals(expectedYAxisTickLocations[i], new Object[] { actualYAxisTickLocation.getX(), actualYAxisTickLocation.getY() }, "Y axis tick location is incorrect for index " + i);
        }
    }

    private Point2D[] getExpectedXAxisTickLocations(CoordSystem coord, double[] values) {
        AffineTransform at = coord.getTransform(0);
        double minX = coord.getBounds().getMinX();
        double maxX = coord.getBounds().getMaxX();
        double tick = ChartUtilities.calculateTickSpacing(minX, maxX);
        double ypt = coord.getBounds().getY();

        Point2D[] result = new Point2D[values.length];
        for (int i = 0; i < values.length; ++i) {
            double xpt = minX + values[i] * tick;
            result[i] = new Point2D.Double(xpt, ypt);
        }

        return result;
    }

    private Point2D[] getExpectedYAxisTickLocations(CoordSystem coord, double[] values) {
        AffineTransform at = coord.getTransform(0);
        double minY = coord.getBounds().getMinY();
        double maxY = coord.getBounds().getMaxY();
        double tick = ChartUtilities.calculateTickSpacing(minY, maxY);
        double xpt = coord.getBounds().getX();

        Point2D[] result = new Point2D[values.length];
        for (int i = 0; i < values.length; ++i) {
            double ypt = minY + values[i] * tick;
            result[i] = new Point2D.Double(xpt, ypt);
        }

        return result;
    }
}