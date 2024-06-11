package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int leftMargin = utils.computeLeftMargin();
        assertEquals(36, leftMargin);

        constraints.setMinimumColumnValue(-20.0);
        constraints.setMaximumColumnValue(20.0);
        utils = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        leftMargin = utils.computeLeftMargin();
        assertEquals(80, leftMargin);

        constraints.setMinimumColumnValue(Double.MIN_VALUE);
        constraints.setMaximumColumnValue(Double.MAX_VALUE);
        utils = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        leftMargin = utils.computeLeftMargin();
        assertEquals(20, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int rightMargin = utils.computeRightMargin();
        assertEquals(15, rightMargin);

        constraints.setMinimumColumnValue(-20.0);
        constraints.setMaximumColumnValue(20.0);
        utils = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        rightMargin = utils.computeRightMargin();
        assertEquals(30, rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(10.0);
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), null, constraints, null);

        int topMargin = utils.computeTopMargin();
        assertEquals(15, topMargin);

        constraints.setMinimumValue(-20.0);
        constraints.setMaximumValue(20.0);
        utils = new CoordSystemUtilities(new CoordSystem(), null, constraints, null);

        topMargin = utils.computeTopMargin();
        assertEquals(30, topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(10.0);
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), null, constraints, null);

        int bottomMargin = utils.computeBottomMargin();
        assertEquals(15, bottomMargin);

        constraints.setMinimumValue(-20.0);
        constraints.setMaximumValue(20.0);
        utils = new CoordSystemUtilities(new CoordSystem(), null, constraints, null);

        bottomMargin = utils.computeBottomMargin();
        assertEquals(30, bottomMargin);

        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints2.setMinimumValue(-10.0);
        constraints2.setMaximumValue(10.0);
        CoordSystemUtilities utils2 = new CoordSystemUtilities(new CoordSystem(), null, null, null, constraints2);

        bottomMargin = utils2.computeBottomMargin();
        assertEquals(15, bottomMargin);

        constraints2.setMinimumValue(-20.0);
        constraints2.setMaximumValue(20.0);
        utils2 = new CoordSystemUtilities(new CoordSystem(), null, null, null, constraints2);

        bottomMargin = utils2.computeBottomMargin();
        assertEquals(30, bottomMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int height = utils.computeXAxisLabelHeight();
        assertEquals(10, height);

        constraints.setMinimumColumnValue(-20.0);
        constraints.setMaximumColumnValue(20.0);
        utils = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        height = utils.computeXAxisLabelHeight();
        assertEquals(20, height);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(10.0);
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), null, constraints, null);

        int width = utils.computeYAxisLabelWidth();
        assertEquals(10, width);

        constraints.setMinimumValue(-20.0);
        constraints.setMaximumValue(20.0);
        utils = new CoordSystemUtilities(new CoordSystem(), null, constraints, null);

        width = utils.computeYAxisLabelWidth();
        assertEquals(20, width);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystem cs = new CoordSystem();
        CoordSystemUtilities utils = new CoordSystemUtilities(cs, null, null, null);

        Graphics2D g = mock(Graphics2D.class);
        when(g.isPaintLabels()).thenReturn(true);

        double[] data = {-1.0, 0.0, 1.0};
        utils.drawNumericalXAxisTicks(g);

        verify(g).draw(argThat(new IsLine2DWithStartPoint(new Point2D.Double(-1.0, 0.0))));
        verify(g).draw(argThat(new IsLine2DWithStartPoint(new Point2D.Double(0.0, 0.0))));
        verify(g).draw(argThat(new IsLine2DWithStartPoint(new Point2D.Double(1.0, 0.0))));

        String[] labels = {"-1.0", "0.0", "1.0"};
        for (int i = 0; i < labels.length; i++) {
            verify(g).drawString(eq(labels[i]), eq(Float.intBitsToFloat(anyInt())), eq(Float.intBitsToFloat(anyInt())));
        }
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem cs = new CoordSystem();
        CoordSystemUtilities utils = new CoordSystemUtilities(cs, null, null, null);

        Graphics2D g = mock(Graphics2D.class);
        when(g.isPaintLabels()).thenReturn(true);

        double[] data = {0.0, 1.0, 2.0};
        int[] labels = {1, 2, 3};
        utils.drawXAxisTicks(g, data, labels);

        Line2D line = new Line2D.Double(0.0, 0.0, 3.0, 0.0);
        verify(g).draw(line);

        for (int i = 0; i < labels.length; i++) {
            String label = (String) data[i];
            Point2D point = new Point2D.Double(i + 1, 0.0);
            Line2D labelLine = new Line2D.Double(point.getX() - 3.0, point.getY(), point.getX() + 3.0, point.getY());
            verify(g).draw(line);
            verify(g).drawString(eq(label), argThat(new IsPoint2DWithX(point.getX())), argThat(new IsPointY(point.getY() + 6.0)));
        }
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem cs = new CoordSystem();
        CoordSystemUtilities utils = new CoordSystemUtilities(cs, null, null, null);

        Graphics2D g = mock(Graphics2D.class);
        when(g.isPaintLabels()).thenReturn(true);

        double min = -10.0;
        double max = 10.0;
        double tick = 1.0;
        int width = 10;
        int height = 10;
        utils.drawYAxisTicks(g, min, max, tick, width, height);

        Line2D xAxisLine = utils.getXAxisLine2D();
        List<Line2D> ticks = new ArrayList<>();
        for (int i = 0; i <= 10; i += 1) {
            double y = min + i * tick;
            Point2D point = new Point2D.Double(0.0, y);
            Line2D tickLine = new Line2D.Double(0.0, y, xAxisLine.getX2(), y);
            ticks.add(tickLine);
        }

        for (Line2D tick : ticks) {
            verify(g).draw(tick);
        }

        for (int i = 0; i <= 10; i++) {
            double y = min + i * tick;
            String label = String.valueOf(i);
            verify(g).drawString(eq(label), argThat(new IsPoint2DWithY(y - 6.0)), argThat(new IsPointX(0.0)));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawYAxisTicksWithNullGraphics2D() {
        CoordSystem cs = new CoordSystem();
        CoordSystemUtilities utils = new CoordSystemUtilities(cs, null, null, null);

        utils.drawYAxisTicks(null, -10.0, 10.0, 1.0, 10, 10);
    }
}