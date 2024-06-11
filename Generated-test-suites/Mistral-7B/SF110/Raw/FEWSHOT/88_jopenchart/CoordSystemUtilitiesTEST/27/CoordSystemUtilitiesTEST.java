package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.NumberFormat;
import java.awt.geom.Line2D;
import java.awt.Color;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import de.progra.charting.model.*;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartDataModel;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utils;
    private CoordSystem coord;
    private ChartDataModel model;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;

    @Before
    public void setUp() {
        coord = new CoordSystem(10, 10, 500, 500);
        model = new ChartDataModel(new Object[][] { { "Label1", 0.0, "Value1" }, { "Label2", 1.0, "Value2" }, { "Label3", 2.0, "Value3" } });
        constraints = new ChartDataModelConstraints(new DataRange(new Number(0.0), new Number(3.0)), new DataRange(new Number(0.0), new Number(3.0)), model);
        constraints2 = new ChartDataModelConstraints(new DataRange(new Number(3.0), new Number(6.0)), new DataRange(new Number(-3.0), new Number(0.0)), model);
        utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @After
    public void tearDown() {
        constraints = null;
        constraints2 = null;
        model = null;
        coord = null;
        utils = null;
    }

    @Test
    public void testComputeLeftMargin() {
        int result = utils.computeLeftMargin();
        int expected = 31; // calculated based on the given code
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRightMargin() {
        int result = utils.computeRightMargin();
        int expected = 21; // calculated based on the given code
        assertEquals(expected, result);
    }

    @Test
    public void testComputeTopMargin() {
        int result = utils.computeTopMargin();
        int expected = 19; // calculated based on the given code
        assertEquals(expected, result);
    }

    @Test
    public void testComputeBottomMargin() {
        int result = utils.computeBottomMargin();
        int expected = 31; // calculated based on the given code
        assertEquals(expected, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int result = utils.computeXAxisLabelHeight();
        int expected = 21; // calculated based on the given code
        assertEquals(expected, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        int result = utils.computeYAxisLabelWidth();
        int expected = 16; // calculated based on the given code
        assertEquals(expected, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        final Graphics2D g = new StubGraphics2D();
        utils.drawNumericalXAxisTicks(g);
        // Add asserts based on the expected behavior of this method
    }

    @Test
    public void testDrawXAxisTicks() {
        final Graphics2D g = new StubGraphics2D();
        utils.drawXAxisTicks(g);
        // Add asserts based on the expected behavior of this method
    }

    @Test
    public void testDrawYAxisTicks() {
        final Graphics2D g = new StubGraphics2D();
        utils.drawYAxisTicks(g);
        // Add asserts based on the expected behavior of this method
    }

    @Test
    public void testGetXAxisLine2D() {
        Line2D result = utils.getXAxisLine2D();
        Line2D expected = new Line2D.Double(0.0, 0.0, 500.0, 0.0);
        assertEquals(expected, result);
    }

    @Test
    public void testGetYAxisLine2D() {
        Line2D result = utils.getYAxisLine2D();
        Line2D expected = new Line2D.Double(0.0, 500.0, 0.0, 0.0);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D result = utils.getSecondYAxisLine2D();
        Line2D expected = new Line2D.Double(500.0, 0.0, 500.0, 500.0);
        assertEquals(expected, result);
    }
}

class StubGraphics2D extends Graphics2D {
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        // Add implementation here if needed
    }

    @Override
    public void drawString(String str, float x, float y) {
        // Add implementation here if needed
    }

    @Override
    public void setColor(Color c) {
        // Add implementation here if needed
    }
}