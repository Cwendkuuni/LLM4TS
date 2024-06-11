package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.util.ChartUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystem coordSystem;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;

    @Before
    public void setUp() {
        coordSystem = new CoordSystem(new Dimension(400, 400), new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12), new java.awt.Color(255, 255, 255), new java.awt.Color(0, 0, 0));
        model = new ChartDataModel();
        constraints = new ChartDataModelConstraints(new MinMaxColumns(new double[]{-5.0, 10.0}, new double[]{5.0, 15.0}), new MinMaxValues(new Double[]{Double.MIN_VALUE, 5.0}, new Double[]{5.0, Double.MAX_VALUE}));
        constraints2 = new ChartDataModelConstraints(new MinMaxColumns(new double[]{-1.0, 6.0}, new double[]{7.0, 11.0}), new MinMaxValues(new Double[]{Double.MIN_VALUE, -2.0}, new Double[]{2.0, Double.MAX_VALUE}));
    }

    // Test for computeLeftMargin() method
    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        int leftMargin = utils.computeLeftMargin();
        assertEquals(23, leftMargin);

        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);
        utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        leftMargin = utils.computeLeftMargin();
        assertEquals(61, leftMargin);

        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        leftMargin = utils.computeLeftMargin();
        assertEquals(86, leftMargin);

        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        constraints2.setMinimumColumnValue(-2.0);
        constraints2.setMaximumColumnValue(2.0);
        utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        leftMargin = utils.computeLeftMargin();
        assertEquals(111, leftMargin);
    }

    // Test for computeRightMargin() method
    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        int rightMargin = utils.computeRightMargin();
        assertEquals(15, rightMargin);

        constraints.setMinimumColumnValue(5.0);
        constraints.setMaximumColumnValue(15.0);
        utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        rightMargin = utils.computeRightMargin();
        assertEquals(36, rightMargin);
    }

    // Test for computeTopMargin() method
    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        int topMargin = utils.computeTopMargin();
        assertEquals(15, topMargin);

        constraints.setMinimumValue(Double.MIN_VALUE);
        constraints.setMaximumValue(5.0);
        utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        topMargin = utils.computeTopMargin();
        assertEquals(23, topMargin);
    }

    // Test for computeBottomMargin() method
    @Test
    public void testComputeBottomMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        int bottomMargin = utils.computeBottomMargin();
        assertEquals(20, bottomMargin);

        constraints.setMinimumValue(Double.MIN_VALUE);
        constraints.setMaximumValue(5.0);
        utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        bottomMargin = utils.computeBottomMargin();
        assertEquals(101, bottomMargin);
    }

    // Test for computeXAxisLabelHeight() method
    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        int height = utils.computeXAxisLabelHeight();
        assertEquals(12, height);

        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        height = utils.computeXAxisLabelHeight();
        assertEquals(21, height);
    }

    // Test for computeYAxisLabelWidth() method
    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        int width = utils.computeYAxisLabelWidth();
        assertEquals(19, width);

        constraints.setMinimumColumnValue(5.0);
        constraints.setMaximumColumnValue(15.0);
        utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        width = utils.computeYAxisLabelWidth();
        assertEquals(37, width);
    }

    // Test for drawNumericalXAxisTicks() method
    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = new MockGraphics2D();
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        utils.drawNumericalXAxisTicks(g);

        assertEquals(10, g.getNumCalls("draw"));
        assertEquals(5, g.getNumCalls("drawString"));
    }

    // Test for drawXAxisTicks() method
    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = new MockGraphics2D();
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        utils.drawXAxisTicks(g);

        assertEquals(14, g.getNumCalls("drawLine"));
        assertEquals(5, g.getNumCalls("drawString"));
    }

    // Test for drawYAxisTicks() method
    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = new MockGraphics2D();
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        utils.drawYAxisTicks(g);

        assertEquals(11, g.getNumCalls("drawLine"));
        assertEquals(11, g.getNumCalls("setColor"));
        assertEquals(11, g.getNumCalls("drawString"));
    }

    // Test for getXAxisLine2D() method
    @Test
    public void testGetXAxisLine2D() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        Line2D line = utils.getXAxisLine2D();

        double[] expectedX = new double[]{0.0, 400.0};
        double[] expectedY = new double[]{0.0, 0.0};
        Arrays.sort(expectedX);

        double[] obtainedX = new double[2];
        double[] obtainedY = new double[2];
        line.getPoints(obtainedX, obtainedY);

        assertEquals(expectedX[0], obtainedX[0], 0.0);
        assertEquals(expectedX[1], obtainedX[1], 0.0);
        assertEquals(expectedY[0], obtainedY[0], 0.0);
        assertEquals(expectedY[1], obtainedY[1], 0.0);
    }

    // Test for getYAxisLine2D() method
    @Test
    public void testGetYAxisLine2D() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        Line2D line = utils.getYAxisLine2D();

        double[] expectedX = new double[]{0.0, 0.0};
        double[] expectedY = new double[]{0.0, 400.0};
        Arrays.sort(expectedX);

        double[] obtainedX = new double[2];
        double[] obtainedY = new double[2];
        line.getPoints(obtainedX, obtainedY);

        assertEquals(expectedX[0], obtainedX[0], 0.0);
        assertEquals(expectedX[1], obtainedX[1], 0.0);
        assertEquals(expectedY[0], obtainedY[0], 0.0);
        assertEquals(expectedY[1], obtainedY[1], 0.0);
    }

    // Test for getSecondYAxisLine2D() method
    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        Line2D line = utils.getSecondYAxisLine2D();

        double[] expectedX = new double[]{200.0, 200.0};
        double[] expectedY = new double[]{0.0, 400.0};
        Arrays.sort(expectedX);

        double[] obtainedX = new double[2];
        double[] obtainedY = new double[2];
        line.getPoints(obtainedX, obtainedY);

        assertEquals(expectedX[0], obtainedX[0], 0.0);
        assertEquals(expectedX[1], obtainedX[1], 0.0);
        assertEquals(expectedY[0], obtainedY[0], 0.0);
        assertEquals(expectedY[1], obtainedY[1], 0.0);
    }

    // Test for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testCoordSystemUtilitiesWithInvalidCoordSystem() {
        new CoordSystemUtilities(null, constraints, constraints2, model);
    }

    // Test for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testCoordSystemUtilitiesWithInvalidConstraints1() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, null, constraints2, model);
    }

    // Test for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testCoordSystemUtilitiesWithInvalidConstraints2() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, null, model);
    }

    // Test for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testCoordSystemUtilitiesWithInvalidModel() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, null);
    }

    // Test for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithInvalidConstraints() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, null, constraints2, model);
        utils.computeLeftMargin();
    }

    // Test for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testComputeRightMarginWithInvalidConstraints() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, null, model);
        utils.computeRightMargin();
    }

    // Test for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testComputeTopMarginWithInvalidConstraints() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, null);
        utils.computeTopMargin();
    }

    // Test for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testComputeBottomMarginWithInvalidConstraints() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, null);
        utils.computeBottomMargin();
    }

    // Test for exception handling
    @Test(expected = NumberFormatException.class)
    public void testFormatDecimalValueWithInvalidFormat() {
        Format decimalFormat = new DecimalFormat("%");
        DecimalFormat formatter = new DecimalFormat();
        String text = formatter.format(1.23);
        String formattedText = decimalFormat.format(Double.parseDouble(text));
    }

    // Test for exception handling
    @Test(expected = NullPointerException.class)
    public void testCalculateTickSpacingWithNullArray() {
        double[] minMaxValues = null;
        double tick = ChartUtilities.calculateTickSpacing(minMaxValues[0], minMaxValues[1]);
    }
}

class MockGraphics2D extends Graphics2D {
    int numCalls = 0;

    @Override
    public void draw(Shape shape) {
        numCalls++;
    }

    @Override
    public void drawString(String string, float x, float y) {
        numCalls++;
    }

    @Override
    public void setColor(Color color) {
        numCalls++;
    }

    public int getNumCalls(String methodName) {
        return numCalls;
    }
}