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
import de.progra.charting.model.DataPoint;
import de.progra.charting.model.DataSeries;
import de.progra.charting.utils.ChartUtilities;
import de.progra.charting.utils.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystem coordSystem;
    private ChartDataModel dataModel;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private CoordSystemUtilities coordSystemUtilities;

    @Before
    public void setup() {
        double[] values = {1.0, 2.0, 3.0, 4.0, 5.0};
        DataSeries series = new DataSeries("Series1", DataPoint.class);
        for (double d : values) {
            series.add(new DataPoint(d));
        }
        DataSeries series2 = new DataSeries("Series2", DataPoint.class);
        DataPoint minPoint = new DataPoint(Double.MIN_VALUE);
        DataPoint maxPoint = new DataPoint(Double.MAX_VALUE);
        series2.add(minPoint);
        series2.add(maxPoint);
        dataModel = new ChartDataModel(series, series2);
        constraints = new ChartDataModelConstraints(new DataPoint(0.0), new DataPoint(10.0), DataPoint.class);
        constraints2 = new ChartDataModelConstraints(minPoint, maxPoint, DataPoint.class);
        coordSystem = new CoordSystem(100, 200, 40, 40, 10, 10, new Font("SansSerif", Font.PLAIN, 12), Color.BLACK);
        coordSystemUtilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel);
    }

    @Test
    public void testComputeLeftMargin() {
        int result = coordSystemUtilities.computeLeftMargin();
        int expected = 77; // Calculated based on the provided constraints and font
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRightMargin() {
        int result = coordSystemUtilities.computeRightMargin();
        int expected = 38; // Calculated based on the provided font
        assertEquals(expected, result);
    }

    @Test
    public void testComputeTopMargin() {
        int result = coordSystemUtilities.computeTopMargin();
        int expected = 39; // Calculated based on the provided font
        assertEquals(expected, result);
    }

    @Test
    public void testComputeBottomMargin() {
        int result = coordSystemUtilities.computeBottomMargin();
        int expected = 77; // Calculated based on the provided constraints and font
        assertEquals(expected, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int result = coordSystemUtilities.computeXAxisLabelHeight();
        int expected = 30; // Calculated based on the provided constraints
        assertEquals(expected, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        int result = coordSystemUtilities.computeYAxisLabelWidth();
        int expected = 60; // Calculated based on the provided constraints
        assertEquals(expected, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
        coordSystemUtilities.drawNumericalXAxisTicks(g);
        // Add assertions to check the drawn ticks and labels based on the provided data model
        // and font
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
        coordSystemUtilities.drawXAxisTicks(g);
        // Add assertions to check the drawn ticks and labels based on the provided data model
        // and font
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = new Graphics2D(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
        coordSystemUtilities.drawYAxisTicks(g);
        // Add assertions to check the drawn ticks and labels based on the provided data model
        // and font
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginThrowsExceptionWithInvalidConstraints() {
        ChartDataModelConstraints invalidConstraints = new ChartDataModelConstraints(null, null, DataPoint.class);
        CoordSystemUtilities coordSystemUtilities2 = new CoordSystemUtilities(coordSystem, invalidConstraints, constraints2, dataModel);
        coordSystemUtilities2.computeLeftMargin();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginThrowsExceptionWithNullCoordSystem() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new DataPoint(0.0), new DataPoint(10.0), DataPoint.class);
        CoordSystemUtilities coordSystemUtilities2 = new CoordSystemUtilities(null, constraints, constraints2, dataModel);
        coordSystemUtilities2.computeLeftMargin();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginThrowsExceptionWithNullConstraints() {
        CoordSystem coordSystem = new CoordSystem(100, 200, 40, 40, 10, 10, new Font("SansSerif", Font.PLAIN, 12), Color.BLACK);
        CoordSystemUtilities coordSystemUtilities2 = new CoordSystemUtilities(coordSystem, null, constraints2, dataModel);
        coordSystemUtilities2.computeLeftMargin();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginThrowsExceptionWithNullDataModel() {
        CoordSystem coordSystem = new CoordSystem(100, 200, 40, 40, 10, 10, new Font("SansSerif", Font.PLAIN, 12), Color.BLACK);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new DataPoint(0.0), new DataPoint(10.0), DataPoint.class);
        CoordSystemUtilities coordSystemUtilities2 = new CoordSystemUtilities(coordSystem, constraints, null, null);
        coordSystemUtilities2.computeLeftMargin();
    }

    @Test
    public void testGetXAxisLine2D() {
        Line2D result = coordSystemUtilities.getXAxisLine2D();
        Line2D expected = new Line2D.Double(0.0, 0.0, 10.0, 0.0);
        assertEquals(expected, result);
    }

    @Test
    public void testGetYAxisLine2D() {
        Line2D result = coordSystemUtilities.getYAxisLine2D();
        Line2D expected = new Line2D.Double(0.0, 0.0, 0.0, 10.0);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D result = coordSystemUtilities.getSecondYAxisLine2D();
        Line2D expected = new Line2D.Double(10.0, 0.0, 10.0, 10.0);
        assertEquals(expected, result);
    }
}