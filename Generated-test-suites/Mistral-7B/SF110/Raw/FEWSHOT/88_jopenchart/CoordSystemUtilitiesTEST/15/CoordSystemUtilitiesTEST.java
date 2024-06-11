package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;
import java.util.*;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {
    @Test
    public void testComputeLeftMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        double xmin = 1.0;
        double xmax = 10.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        double xminAbs = Math.abs(xmin);
        double xmaxAbs = Math.abs(xmax);

        TextLayout layout = new TextLayout("XAxisUnit", new Font("Serif", Font.PLAIN, 12), new FontRenderContext());
        int maxlmargin = util.computeYAxisLabelWidth() + 6;
        maxlmargin += layout.getBounds().getWidth() + 6;
        int margin = (int)(maxlmargin - xminAbs / (xminAbs + xmaxAbs) * (util.getC().getBounds().getWidth() - util.getC().getRightMargin()));
        margin += 5;

        assertEquals(21, margin);

        xmin = -1.0;
        xmax = 0.0;
        xminAbs = Math.abs(xmin);
        xmaxAbs = Math.abs(xmax);

        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);

        margin = util.computeLeftMargin();

        assertEquals(20, margin);

        xmin = 0.0;
        xmax = 1.0;

        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);

        margin = util.computeLeftMargin();

        assertEquals(20, margin);
    }

    @Test
    public void testComputeRightMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        TextLayout layout = new TextLayout("XAxisUnit", new Font("Serif", Font.PLAIN, 12), new FontRenderContext());
        double width = layout.getBounds().getWidth();

        constraints.setMinimumColumnValue(1.0);

        int n = (int)(width + 15.0 / 3.0);
        int margin = Math.max(n, 15);

        assertEquals(16, margin);

        constraints.setMinimumColumnValue(-1.0);
        constraints2 = new ChartDataModelConstraints();
        constraints2.setMaximumColumnValue(1.0);
        util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        margin = util.computeRightMargin();

        assertEquals(15, margin);
    }

    @Test
    public void testComputeTopMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        TextLayout layout = new TextLayout("YAxisUnit", new Font("Serif", Font.PLAIN, 12), new FontRenderContext());
        double height = layout.getBounds().getHeight();
        double descent = layout.getDescent();

        constraints.setMinimumValue(1.0);

        int n = (int)(height + 15.0 / 3.0 + descent);
        int margin = Math.max(n, 15);

        assertEquals(16, margin);

        constraints.setMinimumValue(-1.0);
        constraints2 = new ChartDataModelConstraints();
        constraints2.setMaximumValue(1.0);
        util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        margin = util.computeTopMargin();

        assertEquals(15, margin);
    }

    @Test
    public void testComputeBottomMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        TextLayout layout = new TextLayout("XAxisUnit", new Font("Serif", Font.PLAIN, 12), new FontRenderContext());
        int maxbmargin = util.computeXAxisLabelHeight() + 6;
        maxbmargin += (int)layout.getBounds().getHeight() + 6;
        double ymin = 1.0;
        double ymax = 10.0;
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        double yminAbs = Math.abs(ymin);
        double ymaxAbs = Math.abs(ymax);

        int margin = (int)(maxbmargin - yminAbs / (yminAbs + ymaxAbs) * (util.getC().getBounds().getHeight() - util.getC().getTopMargin()));
        margin += 10;

        assertEquals(21, margin);

        ymin = -1.0;
        ymax = 0.0;

        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);

        margin = util.computeBottomMargin();

        assertEquals(20, margin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        DecimalFormat df = util.c.getXDecimalFormat();
        FontRenderContext frc = util.c.getFontRenderContext();
        Font f = util.c.getFont();

        double min = -1.0;
        double max = 1.0;
        double tick = 0.5;
        int height = 0;

        boolean paint = false;

        for (double d = min; d <= max; d += tick) {
            if (paint) {
                final String sb = df.format(d);
                Rectangle2D r = f.getStringBounds(sb, frc);
                height = Math.max(height, (int)r.getHeight());
            }
            paint = !paint;
        }

        assertEquals(10, height);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        DecimalFormat df = util.c.getYDecimalFormat();
        FontRenderContext frc = util.c.getFontRenderContext();
        Font f = util.c.getFont();

        double min = -1.0;
        double max = 1.0;
        double tick = 0.5;
        int width = 0;

        boolean paint = false;

        for (double d = min; d <= max; d += tick) {
            if (paint) {
                final String sb = df.format(d);
                Rectangle2D r = f.getStringBounds(sb, frc);
                width = Math.max(width, (int)r.getWidth());
            }
            paint = !paint;
        }

        assertEquals(10, width);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(util.c.getFont());
        when(g.isPaintLabels()).thenReturn(true);

        double min = -1.0;
        double max = 1.0;
        double tick = 0.5;
        int height = 10;

        util.drawNumericalXAxisTicks(g);

        verify(g, times(0)).draw(argThat(isA(Line2D.class)));
        verify(g, times(min + 1)).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testDrawXAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(util.c.getFont());
        when(g.isPaintLabels()).thenReturn(true);

        int min = -5;
        int max = 5;
        int tick = 1;
        double ypt = 0.0;

        util.drawXAxisTicks(g);

        verify(g, times(min + 1)).drawLine(anyDouble(), anyDouble(), anyDouble(), anyDouble());
        verify(g, times(min + 1)).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testDrawYAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);

        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(util.c.getFont());
        when(g.isPaintLabels()).thenReturn(true);

        double min = 1.0;
        double max = 10.0;
        double tick = 1.0;

        util.drawYAxisTicks(g);

        verify(g, times(min + 1)).drawLine(anyDouble(), anyDouble(), anyDouble(), anyDouble());
        verify(g, times(min + 1)).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystem coord = new CoordSystem();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);

        double xmin = 1.0;
        double xmax = 5.0;
        double ypt = 3.0;

        coord.setTransform(0);
        coord.setMinimumColumnValue(xmin);
        coord.setMaximumColumnValue(xmax);

        Point2D l = util.getXAxisLine2D().p1;
        Point2D r = util.getXAxisLine2D().p2;

        assertEquals(xmin, l.getX(), 0.01);
        assertEquals(ypt, l.getY(), 0.01);
        assertEquals(xmax, r.getX(), 0.01);
        assertEquals(ypt, r.getY(), 0.01);
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystem coord = new CoordSystem();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);

        double xpt = 1.0;
        double ymin = 1.0;
        double ymax = 5.0;

        coord.setTransform(0);
        coord.setMinimumValue(ymin);
        coord.setMaximumValue(ymax);

        Point2D o = util.getYAxisLine2D().p1;
        Point2D u = util.getYAxisLine2D().p2;

        assertEquals(xpt, o.getX(), 0.01);
        assertEquals(ymin, o.getY(), 0.01);
        assertEquals(xpt, u.getX(), 0.01);
        assertEquals(ymax, u.getY(), 0.01);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystem coord = new CoordSystem();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);

        double xpt = 5.0;
        double ymin2 = 1.0;
        double ymax2 = 10.0;

        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints2.setMaximumColumnValue(xpt);
        constraints2.setMinimumValue(ymin2);
        constraints2.setMaximumValue(ymax2);

        coord.setTransform(1);

        Point2D o = util.getSecondYAxisLine2D().p1;
        Point2D u = util.getSecondYAxisLine2D().p2;

        assertEquals(xpt, o.getX(), 0.01);
        assertEquals(ymin2, o.getY(), 0.01);
        assertEquals(xpt, u.getX(), 0.01);
        assertEquals(ymax2, u.getY(), 0.01);
    }
}