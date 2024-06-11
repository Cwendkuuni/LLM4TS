package de.progra.charting.test;

import static org.junit.Assert.*;

import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.*;

import java.awt.*;
import java.text.*;
import java.util.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-1.5);
        constraints.setMaximumColumnValue(3.5);
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int leftMargin = utilities.computeLeftMargin();
        assertEquals(20, leftMargin);

        double xmin = -1.5;
        double xmax = 3.5;
        double xminAbs = Math.abs(xmin);
        double xmaxAbs = Math.abs(xmax);
        TextLayout layout = new TextLayout("Test", new Font("Arial", Font.PLAIN, 12), new FontRenderContext());
        int maxlmargin = utilities.computeYAxisLabelWidth() + 6;
        maxlmargin = Math.max(maxlmargin, (int)layout.getBounds().getWidth() + 6);
        int margin = (int)(maxlmargin - xminAbs / (xminAbs + xmaxAbs) * (utilities.c.getBounds().getWidth() - utilities.c.getRightMargin()));
        margin += 5;
        final int expectedMargin = (int)(margin - xminAbs / (xminAbs + xmaxAbs) * (utilities.c.getBounds().getWidth() - utilities.c.getRightMargin()) + 5);
        assertEquals(expectedMargin, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-1.5);
        constraints.setMaximumColumnValue(3.5);
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int rightMargin = utilities.computeRightMargin();
        assertEquals(15, rightMargin);

        TextLayout layout = new TextLayout("Test", new Font("Arial", Font.PLAIN, 12), new FontRenderContext());
        final double width = layout.getBounds().getWidth();
        int n = (int)(width + 15.0 / 3.0);
        assertEquals(n, rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-1.5);
        constraints.setMaximumValue(3.5);
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), null, constraints, null);

        int topMargin = utilities.computeTopMargin();
        assertEquals(15, topMargin);

        TextLayout layout = new TextLayout("Test", new Font("Arial", Font.PLAIN, 12), new FontRenderContext());
        final double height = layout.getBounds().getHeight();
        int n = (int)(height + 15.0 / 3.0 + layout.getDescent());
        assertEquals(n, topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-1.5);
        constraints.setMaximumValue(3.5);
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), null, constraints, null);

        int bottomMargin = utilities.computeBottomMargin();
        assertEquals(20, bottomMargin);

        double ymin = -1.5;
        double ymax = 3.5;
        if (ymin <= 0.0 && ymax > 0.0) {
            ymin = Math.abs(ymin);
            ymax = Math.abs(ymax);
            TextLayout layout = new TextLayout("Test", new Font("Arial", Font.PLAIN, 12), new FontRenderContext());
            int maxbmargin = utilities.computeXAxisLabelHeight() + 6;
            maxbmargin = Math.max(maxbmargin, (int)layout.getBounds().getHeight() + 6);
            int margin = (int)(maxbmargin - ymin / (ymin + ymax) * (utilities.c.getBounds().getHeight() - utilities.c.getTopMargin()));
            margin += 10;
            final int expectedMargin = (int)(margin - ymin / (ymin + ymax) * (utilities.c.getBounds().getHeight() - utilities.c.getTopMargin()) + 10);
            assertEquals(expectedMargin, bottomMargin);
        }
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-1.5);
        constraints.setMaximumColumnValue(3.5);
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);

        int height = utilities.computeXAxisLabelHeight();
        assertEquals(15, height);

        DecimalFormat df = new DecimalFormat("###.##");
        FontRenderContext frc = new FontRenderContext();
        Font f = new Font("Arial", Font.PLAIN, 12);
        for (double d = -1.5; d <= 3.5; d += 0.5) {
            if (d == 0.0) continue;
            String label = df.format(d);
            TextLayout layout = new TextLayout(label, f, frc);
            int labelHeight = (int)layout.getBounds().getHeight();
            height = Math.max(height, labelHeight);
        }
        assertEquals(height, utilities.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-1.5);
        constraints.setMaximumValue(3.5);
        CoordSystemUtilities utilities = new CoordSystemUtilities(new CoordSystem(), null, constraints, null);

        int width = utilities.computeYAxisLabelWidth();
        assertEquals(15, width);

        DecimalFormat df = new DecimalFormat("###.##");
        FontRenderContext frc = new FontRenderContext();
        Font f = new Font("Arial", Font.PLAIN, 12);
        for (double d = -1.5; d <= 3.5; d += 0.5) {
            if (d == 0.0) continue;
            String label = df.format(d);
            TextLayout layout = new TextLayout(label, f, frc);
            int labelWidth = (int)layout.getBounds().getWidth();
            width = Math.max(width, labelWidth);
        }
        assertEquals(width, utilities.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystem system = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-1.5);
        constraints.setMaximumColumnValue(3.5);
        CoordSystemUtilities utilities = new CoordSystemUtilities(system, constraints, null, null);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        utilities.drawNumericalXAxisTicks(g);

        // Verify that no ticks were drawn before the first one
        Line2D ticks = Mockito.mock(Line2D.class);
        Mockito.verifyNoMoreInteractions(ticks);

        // Verify that the first tick was drawn
        Point2D p = new Point2D.Double(-1.5, 0.0);
        Point2D v = new Point2D.Double(-1.5, 0.0);
        Line2D tick = new Line2D.Double(v.getX(), v.getY() - 3.0, v.getX(), v.getY() + 3.0);
        Mockito.verify(g).draw(tick);

        // Verify that all other ticks were drawn
        for (double d = -1.45; d <= 3.45; d += 0.5) {
            if (d == 0.0) continue;
            p.setLocation(d, 0.0);
            v = utilities.c.getTransform(0).transform(p, null);
            tick.setLine(v.getX(), v.getY() - 3.0, v.getX(), v.getY() + 3.0);
            Mockito.verify(g).draw(tick);
        }
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem system = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-1);
        constraints.setMaximumColumnValue(3);
        CoordSystemUtilities utilities = new CoordSystemUtilities(system, constraints, null, null);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        utilities.drawXAxisTicks(g);

        // Verify that no ticks were drawn before the first one
        Line2D ticks = Mockito.mock(Line2D.class);
        Mockito.verifyNoMoreInteractions(ticks);

        // Verify that the first tick was drawn
        Point2D p = new Point2D.Double(-1, 0.0);
        Point2D v = new Point2D.Double(-1, 0.0);
        Line2D tick = new Line2D.Double(v.getX(), v.getY() - 3.0, v.getX(), v.getY() + 3.0);
        Mockito.verify(g).draw(tick);

        // Verify that all other ticks were drawn
        for (int i = 0; i < 5; ++i) {
            p.setLocation(i + 1, 0.0);
            v = utilities.c.getTransform(0).transform(p, null);
            tick.setLine(v.getX(), v.getY() - 3.0, v.getX(), v.getY() + 3.0);
            Mockito.verify(g).draw(tick);
        }
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem system = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(-1.5);
        constraints.setMaximumValue(3.5);
        CoordSystemUtilities utilities = new CoordSystemUtilities(system, null, constraints, null);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        utilities.drawYAxisTicks(g);

        // Verify that no ticks were drawn before the first one
        Line2D ticks = Mockito.mock(Line2D.class);
        Mockito.verifyNoMoreInteractions(ticks);

        // Verify that the first tick was drawn
        double xpt = 0.0;
        double ypt = -1.5;
        Point2D p = new Point2D.Double(xpt, ypt);
        Point2D v = new Point2D.Double(xpt, ypt);
        Line2D tick = new Line2D.Double(v.getX() - 3.0, v.getY(), v.getX() + 3.0, v.getY());
        Mockito.verify(g).draw(tick);

        // Verify that all other ticks were drawn
        for (double d = -1.45; d <= 3.45; d += 0.5) {
            ypt = d;
            p.setLocation(0.0, ypt);
            v = utilities.c.getTransform(0).transform(p, null);
            tick.setLine(v.getX() - 3.0, v.getY(), v.getX() + 3.0, v.getY());
            Mockito.verify(g).draw(tick);
        }
    }
}