package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.awt.font.TextAttribute;
import java.awt.font.TextRenderInfo;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumColumnValue(10.0);
        constraints.setMaximumColumnValue(20.0);

        CoordSystemConstraints constraints2 = new CoordSystemConstraints();
        constraints2.setMinimumColumnValue(15.0);
        constraints2.setMaximumColumnValue(30.0);

        CoordSystemModel model = new CoordSystemModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), constraints, constraints2, model);

        assertEquals(120, util.computeLeftMargin());
    }

    @Test
    public void testComputeLeftMargin_yMinLessThanZero() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumColumnValue(10.0);
        constraints.setMaximumColumnValue(20.0);
        constraints.setMinimumValue(-5.0);

        CoordSystemConstraints constraints2 = new CoordSystemConstraints();
        constraints2.setMinimumColumnValue(15.0);
        constraints2.setMaximumColumnValue(30.0);

        CoordSystemModel model = new CoordSystemModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), constraints, constraints2, model);

        assertEquals(120, util.computeLeftMargin());
    }

    @Test
    public void testComputeLeftMargin_yMaxGreaterThanZero() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumColumnValue(10.0);
        constraints.setMaximumColumnValue(20.0);
        constraints.setMaximumValue(5.0);

        CoordSystemConstraints constraints2 = new CoordSystemConstraints();
        constraints2.setMinimumColumnValue(15.0);
        constraints2.setMaximumColumnValue(30.0);

        CoordSystemModel model = new CoordSystemModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), constraints, constraints2, model);

        assertEquals(120, util.computeLeftMargin());
    }

    @Test
    public void testComputeLeftMargin_nothingToCompute() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(0.0);

        CoordSystemConstraints constraints2 = new CoordSystemConstraints();
        constraints2.setMinimumColumnValue(0.0);
        constraints2.setMaximumColumnValue(0.0);

        CoordSystemModel model = new CoordSystemModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), constraints, constraints2, model);

        assertEquals(20, util.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumColumnValue(10.0);
        constraints.setMaximumColumnValue(20.0);

        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), constraints, null, null);

        assertEquals(54, util.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);

        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), null, null, null);

        assertEquals(45, util.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);

        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), null, null, null);

        assertEquals(45, util.computeBottomMargin());
    }

    @Test
    public void testComputeBottomMargin_yMinLessThanZero() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);

        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), null, null, null);

        assertEquals(80, util.computeBottomMargin());
    }

    @Test
    public void testComputeBottomMargin_yMaxGreaterThanZero() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);

        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), null, null, null);

        assertEquals(15, util.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);

        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        FontRenderContext frc = new FontRenderContext();
        Font f = new Font("SansSerif", Font.BOLD, 12);

        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), constraints, null, null);

        int height = util.computeXAxisLabelHeight();

        assertEquals(21, height);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);

        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        FontRenderContext frc = new FontRenderContext();
        Font f = new Font("SansSerif", Font.BOLD, 12);

        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), null, constraints, null);

        int width = util.computeYAxisLabelWidth();

        assertEquals(30, width);
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);

        ChartChart chart = new ChartChart();
        chart.setBackgroundPaint(Color.WHITE);

        StaticAxis xAxis = new StaticAxis();
        xAxis.setConstraints(constraints);
        xAxis.setUnitsPerLabel(1.0);

        ChartAxis chartAxisX = new ChartAxis();
        chartAxisX.setAxis(xAxis);
        chartAxisX.setLabelFormatter(null);

        ChartPlot plot = new ChartPlot(chartAxisX, null);

        Graphics2D g = plot.getGraphics();

        CoordSystemUtilities util = new CoordSystemUtilities(xAxis, constraints, null, null);
        util.drawXAxisTicks(g);

        Line2D xAxisLine2D = util.getXAxisLine2D();
        assertEquals(10.0, xAxisLine2D.getX2(), 0.001);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);

        ChartChart chart = new ChartChart();
        chart.setBackgroundPaint(Color.WHITE);

        StaticAxis xAxis = new StaticAxis();
        xAxis.setConstraints(constraints);
        xAxis.setUnitsPerLabel(1.0);

        ChartAxis chartAxisX = new ChartAxis();
        chartAxisX.setAxis(xAxis);
        chartAxisX.setLabelFormatter(null);

        ChartPlot plot = new ChartPlot(chartAxisX, null);

        Graphics2D g = plot.getGraphics();

        CoordSystemUtilities util = new CoordSystemUtilities(xAxis, constraints, null, null);
        util.drawNumericalXAxisTicks(g);

        Line2D xAxisLine2D = util.getXAxisLine2D();
        assertEquals(10.0, xAxisLine2D.getX2(), 0.001);
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);

        ChartChart chart = new ChartChart();
        chart.setBackgroundPaint(Color.WHITE);

        StaticAxis yAxis = new StaticAxis();
        yAxis.setConstraints(constraints);

        ChartAxis chartAxisY = new ChartAxis();
        chartAxisY.setAxis(yAxis);
        chartAxisY.setLabelFormatter(null);

        ChartPlot plot = new ChartPlot(chartAxisY, null);

        Graphics2D g = plot.getGraphics();

        CoordSystemUtilities util = new CoordSystemUtilities(null, null, constraints, null);
        util.drawYAxisTicks(g);

        Line2D yAxisLine2D = util.getYAxisLine2D();
        assertEquals(5.0, yAxisLine2D.getY2(), 0.001);
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);

        ChartSystem chartSystem = new ChartSystem();
        chartSystem.setBounds(new Rectangle2D.Double(0.0, 0.0, 400.0, 400.0));

        CoordSystem coordSystem = new CoordSystem();
        coordSystem.setTransform(chartSystem.getTransformer());
        coordSystem.setBackgroundPaint(Color.WHITE);
        coordSystem.setAxis(new StaticAxis[2]);

        CoordSystemUtilities util = new CoordSystemUtilities(new StaticAxis(0), constraints, null, null);

        Line2D xAxisLine2D = util.getXAxisLine2D();

        assertEquals(0.0, xAxisLine2D.getX1(), 0.001);
        assertEquals(400.0, xAxisLine2D.getX2(), 0.001);
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);

        ChartSystem chartSystem = new ChartSystem();
        chartSystem.setBounds(new Rectangle2D.Double(0.0, 0.0, 400.0, 400.0));

        CoordSystem coordSystem = new CoordSystem();
        coordSystem.setTransform(chartSystem.getTransformer());
        coordSystem.setBackgroundPaint(Color.WHITE);
        coordSystem.setAxis(new StaticAxis[2]);

        CoordSystemUtilities util = new CoordSystemUtilities(null, null, constraints, null);

        Line2D yAxisLine2D = util.getYAxisLine2D();

        assertEquals(0.0, yAxisLine2D.getY1(), 0.001);
        assertEquals(400.0, yAxisLine2D.getY2(), 0.001);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystemConstraints constraints = new CoordSystemConstraints();
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);

        ChartSystem chartSystem = new ChartSystem();
        chartSystem.setBounds(new Rectangle2D.Double(0.0, 0.0, 400.0, 400.0));

        CoordSystem coordSystem = new CoordSystem();
        coordSystem.setTransform(chartSystem.getTransformer());
        coordSystem.setBackgroundPaint(Color.WHITE);

        CoordSystemConstraints2 constraints2 = new CoordSystemConstraints2();
        constraints2.setMinimumColumnValue(15.0);
        constraints2.setMaximumColumnValue(30.0);

        CoordSystemUtilities util = new CoordSystemUtilities(null, null, null, constraints2);

        Line2D secondYAxisLine2D = util.getSecondYAxisLine2D();

        assertEquals(400.0, secondYAxisLine2D.getX1(), 0.001);
        assertEquals(0.0, secondYAxisLine2D.getY1(), 0.001);
        assertEquals(400.0, secondYAxisLine2D.getX2(), 0.001);
        assertEquals(-250.0, secondYAxisLine2D.getY2(), 0.001);
    }
}