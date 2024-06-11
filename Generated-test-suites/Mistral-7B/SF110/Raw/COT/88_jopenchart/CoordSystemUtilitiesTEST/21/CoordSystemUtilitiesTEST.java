package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;
    private CoordSystem coordSystem;
    private ChartDataModel chartDataModel;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;

    @Before
    public void setup() {
        double[] values = {-10.0, 0.0, 5.0, 10.0};
        double[] values2 = {10.0, 5.0, 0.0, -5.0};
        double[] columnValues = new double[4];
        DecimalFormat xDecimalFormat = new DecimalFormat("#.##");
        DecimalFormat yDecimalFormat = new DecimalFormat("#.##");

        coordSystem = new CoordSystem(100, 100, 50, 50);
        chartDataModel = new ChartDataModel(values, columnValues);
        constraints = new ChartDataModelConstraints(new double[] { 0.0, 10.0 }, new double[] { -5.0, 5.0 }, chartDataModel);
        constraints2 = new ChartDataModelConstraints(new double[] { -10.0, 5.0 }, new double[] { 5.0, 10.0 }, chartDataModel);

        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);
    }

    @Test
    public void testComputeLeftMargin() {
        int leftMargin = utilities.computeLeftMargin();
        int expectedLeftMargin = 20;
        assertEquals(expectedLeftMargin, leftMargin);

        // Test for negative xmin and positive xmax
        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);
        leftMargin = utilities.computeLeftMargin();
        expectedLeftMargin = 67;
        assertEquals(expectedLeftMargin, leftMargin);

        // Test for negative xmin and negative xmax
        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(-5.0);
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);
        leftMargin = utilities.computeLeftMargin();
        expectedLeftMargin = 6;
        assertEquals(expectedLeftMargin, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        int rightMargin = utilities.computeRightMargin();
        int expectedRightMargin = 15;
        assertEquals(expectedRightMargin, rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        int topMargin = utilities.computeTopMargin();
        int expectedTopMargin = 15;
        assertEquals(expectedTopMargin, topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        int bottomMargin = utilities.computeBottomMargin();
        int expectedBottomMargin = 20;
        assertEquals(expectedBottomMargin, bottomMargin);

        // Test for negative ymin and positive ymax
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);
        bottomMargin = utilities.computeBottomMargin();
        expectedBottomMargin = 67;
        assertEquals(expectedBottomMargin, bottomMargin);

        // Test for positive ymin and negative ymax
        constraints.setMinimumValue(5.0);
        constraints.setMaximumValue(-5.0);
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);
        bottomMargin = utilities.computeBottomMargin();
        expectedBottomMargin = 6;
        assertEquals(expectedBottomMargin, bottomMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int xAxisLabelHeight = utilities.computeXAxisLabelHeight();
        int expectedXAxisLabelHeight = 12;
        assertEquals(expectedXAxisLabelHeight, xAxisLabelHeight);

        // Test for negative minimum column value
        constraints.setMinimumColumnValue(-5.0);
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);
        xAxisLabelHeight = utilities.computeXAxisLabelHeight();
        expectedXAxisLabelHeight = 41;
        assertEquals(expectedXAxisLabelHeight, xAxisLabelHeight);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        int yAxisLabelWidth = utilities.computeYAxisLabelWidth();
        int expectedYAxisLabelWidth = 12;
        assertEquals(expectedYAxisLabelWidth, yAxisLabelWidth);

        // Test for minimum column value less than zero
        constraints.setMinimumColumnValue(-5.0);
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);
        yAxisLabelWidth = utilities.computeYAxisLabelWidth();
        expectedYAxisLabelWidth = 41;
        assertEquals(expectedYAxisLabelWidth, yAxisLabelWidth);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new CoordSystemUtilities(null, constraints, constraints2, chartDataModel);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstraints() {
        new CoordSystemUtilities(coordSystem, null, constraints2, chartDataModel);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstraints2() {
        new CoordSystemUtilities(coordSystem, constraints, null, chartDataModel);
    }

    @Test(expected = NullPointerException.class)
    public void testNullChartDataModel() {
        new CoordSystemUtilities(coordSystem, constraints, constraints2, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidChartDataModel() {
        ChartDataModel invalidChartDataModel = new ChartDataModel(Arrays.asList(1.0, 2.0, 3.0), null);
        new CoordSystemUtilities(coordSystem, constraints, constraints2, invalidChartDataModel);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D graphics2D = null;
        utilities.drawNumericalXAxisTicks(graphics2D);
        assertNull(graphics2D);

        graphics2D = mock(Graphics2D.class);
        utilities.drawNumericalXAxisTicks(graphics2D);
        verifyNoMoreInteractions(graphics2D);
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D graphics2D = null;
        utilities.drawXAxisTicks(graphics2D);
        assertNull(graphics2D);

        graphics2D = mock(Graphics2D.class);
        utilities.drawXAxisTicks(graphics2D);
        verifyNoMoreInteractions(graphics2D);
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D graphics2D = null;
        utilities.drawYAxisTicks(graphics2D);
        assertNull(graphics2D);

        graphics2D = mock(Graphics2D.class);
        utilities.drawYAxisTicks(graphics2D);
        verifyNoMoreInteractions(graphics2D);
    }
}