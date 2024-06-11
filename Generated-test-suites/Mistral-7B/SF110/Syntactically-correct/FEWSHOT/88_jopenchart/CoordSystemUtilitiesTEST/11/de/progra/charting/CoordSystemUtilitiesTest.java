package de.progra.charting.test;

import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(1.0);
        constraints.setMaximumColumnValue(2.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        double xmin = constraints.getMinimumColumnValue();
        double xmax = constraints.getMaximumColumnValue();

        assertEquals(20, util.computeLeftMargin());

        xmin = -1.0;
        xmax = 1.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        TextLayout layout = new TextLayout("Y-Axis label", new Font("Serif", Font.BOLD, 12), new FontRenderContext());
        int maxlmargin = util.computeYAxisLabelWidth() + 6;
        maxlmargin = Math.max(maxlmargin, (int)layout.getBounds().getWidth() + 6);
        int margin = (int)(maxlmargin - xmin / (xmin + xmax) * (new CoordSystem().getBounds().getWidth() - new CoordSystem().getRightMargin()));
        margin += 5;

        assertEquals((int)(margin + xmin / (xmin + xmax) * (new CoordSystem().getBounds().getWidth() - 20)), util.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), null, null, null);

        TextLayout layout = new TextLayout("X-Axis label", new Font("Serif", Font.BOLD, 12), new FontRenderContext());
        final double width = layout.getBounds().getWidth();

        assertEquals((int)(width + 15.0), util.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), null, null, null);

        TextLayout layout = new TextLayout("Y-Axis label", new Font("Serif", Font.BOLD, 12), new FontRenderContext());
        final double height = layout.getBounds().getHeight();

        assertEquals((int)(height + 15.0), util.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-1.0);
        constraints.setMaximumValue(1.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        double ymin = constraints.getMinimumValue().doubleValue();
        double ymax = constraints.getMaximumValue().doubleValue();

        assertEquals(20, util.computeBottomMargin());

        ymin = -2.0;
        ymax = 2.0;
        constraints.setMinimumValue(new Double(ymin));
        constraints.setMaximumValue(new Double(ymax));
        util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        layout = new TextLayout("X-Axis label", new Font("Serif", Font.BOLD, 12), new FontRenderContext());
        int maxbmargin = util.computeXAxisLabelHeight() + 6;
        maxbmargin = Math.max(maxbmargin, (int)layout.getBounds().getHeight() + 6);
        int margin = (int)(maxbmargin - ymin / (ymin + ymax) * (new CoordSystem().getBounds().getHeight() - new CoordSystem().getTopMargin()));
        margin += 10;

        assertEquals((int)(margin + ymin / (ymin + ymax) * (new CoordSystem().getBounds().getHeight() - 20)), util.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(new Double(-1.0));
        constraints.setMaximumValue(new Double(1.0));
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int height = util.computeXAxisLabelHeight();

        assertEquals(0, height);

        constraints.setMinimumValue(new Double(0.0));
        constraints.setMaximumValue(new Double(1.0));
        util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        DecimalFormat df = util.c.getXDecimalFormat();
        FontRenderContext frc = util.c.getFontRenderContext();
        Font f = util.c.getFont();
        double min = constraints.getMinimumColumnValue();
        double max = constraints.getMaximumColumnValue();
        double tick = ChartUtilities.calculateTickSpacing(min, max);
        double ypt = 0.0;

        if (constraints.getMinimumValue().doubleValue() > 0.0) {
            ypt = constraints.getMinimumValue().doubleValue();
        }
        else if (constraints.getMaximumValue().doubleValue() < 0.0) {
            ypt = constraints.getMaximumValue().doubleValue();
        }

        boolean paint = false;
        for (double d = min; d <= max; d += tick) {
            if (paint) {
                String sb = df.format(d);
                Rectangle2D r = f.getStringBounds(sb, frc);
                height = Math.max(height, (int)r.getHeight());
            }
            paint = !paint;
        }

        assertEquals(7, height);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(new Double(-1.0));
        constraints.setMaximumValue(new Double(1.0));
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int width = util.computeYAxisLabelWidth();

        assertEquals(0, width);

        constraints.setMinimumColumnValue(new Double(0.0));
        constraints.setMaximumColumnValue(new Double(1.0));
        util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        DecimalFormat df = util.c.getYDecimalFormat();
        FontRenderContext frc = util.c.getFontRenderContext();
        Font f = util.c.getFont();
        double min = constraints.getMinimumValue().doubleValue();
        double max = constraints.getMaximumValue().doubleValue();
        double tick = ChartUtilities.calculateTickSpacing(min, max);
        double xpt = 0.0;

        if (constraints.getMinimumColumnValue() > 0.0) {
            xpt = constraints.getMinimumColumnValue();
        }
        else if (constraints.getMaximumColumnValue() < 0.0 && util.c.getSecondYAxis() != null) {
            xpt = constraints.getMaximumColumnValue();
        }

        boolean paint = false;
        for (double d = min; d <= max; d += tick) {
            if (paint) {
                String sb = df.format(d);
                Rectangle2D r = f.getStringBounds(sb, frc);
                width = Math.max(width, (int)r.getWidth());
            }
            paint = !paint;
        }

        assertEquals(5, width);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        AffineTransform at = coord.getTransform(0);
        double min = constraints.getMinimumColumnValue();
        double max = constraints.getMaximumColumnValue();
        double tick = ChartUtilities.calculateTickSpacing(min, max);
        double ypt = 0.0;

        if (constraints.getMinimumValue().doubleValue() > 0.0) {
            ypt = constraints.getMinimumValue().doubleValue();
        }
        else if (constraints.getMaximumValue().doubleValue() < 0.0) {
            ypt = constraints.getMaximumValue().doubleValue();
        }

        for (double d = min; d <= max; d += tick) {
            Point2D p = new Point2D.Double(d, ypt);
            Point2D v = at.transform(p, null);
            Line2D ticks = new Line2D.Double(v.getX(), v.getY() - 3.0, v.getX(), v.getY() + 3.0);
            Graphics2D g = new StubGraphics2D();
            util.drawNumericalXAxisTicks(g);
            assertEquals(ticks, g.getLastDrawnShape());
        }
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        AffineTransform at = coord.getTransform(0);
        int min = (int)constraints.getMinimumColumnValue();
        int max = (int)constraints.getMaximumColumnValue();
        double tick = 1.0;
        double ypt = 0.0;

        if (constraints.getMinimumValue().doubleValue() > 0.0) {
            ypt = constraints.getMinimumValue().doubleValue();
        }
        else if (constraints.getMaximumValue().doubleValue() < 0.0) {
            ypt = constraints.getMaximumValue().doubleValue();
        }

        Line2D ticks = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
        Graphics2D g = new StubGraphics2D();
        for (int i = min - 1; i < max; ++i) {
            Point2D p = new Point2D.Double(i + 1, ypt);
            Point2D v = at.transform(p, null);
            ticks.setLine(v.getX(), v.getY() - 3.0, v.getX(), v.getY() + 3.0);
            g.setColor(Color.BLACK);
            g.fillOval((int)v.getX() - 2, (int)v.getY() - 2, 4, 4);
            util.drawXAxisTicks(g);
            assertEquals(ticks, g.getLastDrawnShape());
        }
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        AffineTransform at = coord.getTransform(0);
        double min = constraints.getMinimumValue().doubleValue();
        double max = constraints.getMaximumValue().doubleValue();
        double tick = ChartUtilities.calculateTickSpacing(min, max);
        double xpt = 0.0;

        if (constraints.getMinimumColumnValue() > 0.0) {
            xpt = constraints.getMinimumColumnValue();
        }
        else if (constraints.getMaximumColumnValue() < 0.0 && coord.getSecondYAxis() != null) {
            xpt = constraints.getMaximumColumnValue();
        }

        Point2D p = new Point2D.Double(xpt, min);
        Point2D v = at.transform(p, null);
        Line2D ticks = new Line2D.Double(v.getX() - 3.0, v.getY(), v.getX() + 3.0, v.getY());
        Graphics2D g = new StubGraphics2D();
        util.drawYAxisTicks(g);
        assertEquals(ticks, g.getLastDrawnShape());
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystem coord = new CoordSystem();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);

        double ypt = 0.0;
        if (coord.getConstraints().getMinimumValue().doubleValue() > 0.0) {
            ypt = coord.getConstraints().getMinimumValue().doubleValue();
        }
        else if (coord.getConstraints().getMaximumValue().doubleValue() < 0.0) {
            ypt = coord.getConstraints().getMaximumValue().doubleValue();
        }

        AffineTransform at = coord.getTransform(0);
        Point2D l = at.transform(new Point2D.Double(coord.getConstraints().getMinimumColumnValue(), ypt), null);
        Point2D r = at.transform(new Point2D.Double(coord.getConstraints().getMaximumColumnValue(), ypt), null);
        Line2D line = new Line2D.Double(l, r);

        Line2D xAxisLine2D = util.getXAxisLine2D();

        assertEquals(line, xAxisLine2D);
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystem coord = new CoordSystem();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);

        double xpt = 0.0;
        if (coord.getConstraints().getMinimumColumnValue() > 0.0) {
            xpt = coord.getConstraints().getMinimumColumnValue();
        }
        else if (coord.getConstraints().getMaximumColumnValue() < 0.0) {
            xpt = coord.getConstraints().getMaximumColumnValue();
        }

        AffineTransform at = coord.getTransform(0);
        Point2D o = at.transform(new Point2D.Double(xpt, coord.getConstraints().getMaximumValue().doubleValue()), null);
        Point2D u = at.transform(new Point2D.Double(xpt, coord.getConstraints().getMinimumValue().doubleValue()), null);
        Line2D line = new Line2D.Double(o, u);

        Line2D yAxisLine2D = util.getYAxisLine2D();

        assertEquals(line, yAxisLine2D);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystem coord = new CoordSystem();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);

        double xpt = coord.getConstraints2().getMaximumColumnValue();
        AffineTransform at = coord.getTransform(1);
        Point2D o = at.transform(new Point2D.Double(xpt, coord.getConstraints2().getMaximumValue().doubleValue()), null);
        Point2D u = at.transform(new Point2D.Double(xpt, coord.getConstraints2().getMinimumValue().doubleValue()), null);
        Line2D line = new Line2D.Double(o, u);

        Line2D secondYAxisLine2D = util.getSecondYAxisLine2D();

        assertEquals(line, secondYAxisLine2D);
    }
}