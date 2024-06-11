package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.XYDataItem;
import de.progra.charting.utils.ChartUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;
    private CoordSystem coordSystem;
    private ChartDataModel dataModel;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;

    @Before
    public void setUp() {
        coordSystem = new CoordSystem(new java.awt.Dimension(500, 500), new java.awt.Dimension(500, 500), null);
        DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormat df2 = new DecimalFormat("0");
        dataModel = new ChartDataModel();
        dataModel.addColumn("Column1", new XYDataItem[0]);
        dataModel.addColumn("Column2", new XYDataItem[0]);
        constraints = new ChartDataModelConstraints(df, df2, -100.0, 100.0, 0.0, 0.0);
        constraints2 = new ChartDataModelConstraints(df2, df2, 100.0, 200.0, 0.0, 0.0);
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel);
    }

    @Test
    public void testComputeLeftMargin() {
        assertEquals(20, utilities.computeLeftMargin());
        double minX = -10.0;
        double maxX = 20.0;
        double xmin = Math.min(minX, 0.0);
        double xmax = Math.max(maxX, 0.0);
        TextLayout layout = new TextLayout(utilities.c.getYAxisUnit(), utilities.c.getFont(), utilities.c.getFontRenderContext());
        int maxlmargin = utilities.computeYAxisLabelWidth() + 6;
        maxlmargin = Math.max(maxlmargin, (int)layout.getBounds().getWidth() + 6);
        int margin = (int)(maxlmargin - xmin / (xmin + xmax) * (utilities.c.getBounds().getWidth() - utilities.c.getRightMargin()));
        margin += 5;
        assertEquals(margin, utilities.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        assertEquals(15, utilities.computeRightMargin());
        TextLayout layout = new TextLayout(utilities.c.getXAxisUnit(), utilities.c.getFont(), utilities.c.getFontRenderContext());
        final double width = layout.getBounds().getWidth();
        assertEquals((int)(width + 15.0 / 3.0), utilities.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        assertEquals(15, utilities.computeTopMargin());
        TextLayout layout = new TextLayout(utilities.c.getYAxisUnit(), utilities.c.getFont(), utilities.c.getFontRenderContext());
        final double height = layout.getBounds().getHeight();
        assertEquals((int)(height + 15.0 / 3.0 + layout.getDescent()), utilities.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        assertEquals(20, utilities.computeBottomMargin());
        double ymin = -10.0;
        double ymax = 10.0;
        if (ymin <= 0.0 && ymax > 0.0) {
            ymin = Math.abs(ymin);
            ymax = Math.abs(ymax);
        }
        TextLayout layout = new TextLayout(utilities.c.getXAxisUnit(), utilities.c.getFont(), utilities.c.getFontRenderContext());
        int maxbmargin = utilities.computeXAxisLabelHeight() + 6;
        maxbmargin = Math.max(maxbmargin, (int)layout.getBounds().getHeight() + 6);
        int margin = (int)(maxbmargin - ymin / (ymin + ymax) * (utilities.c.getBounds().getHeight() - utilities.c.getTopMargin()));
        margin += 10;
        assertEquals(margin, utilities.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        double minX = -10.0;
        double maxX = 10.0;
        double tick = 1.0;
        double ypt = 0.0;
        int height = 0;
        if (constraints.getMinimumValue().doubleValue() > 0.0) {
            ypt = constraints.getMinimumValue().doubleValue();
        } else if (constraints.getMaximumValue().doubleValue() < 0.0) {
            ypt = constraints.getMaximumValue().doubleValue();
        }
        boolean paint = false;
        DecimalFormat df = utilities.c.getXDecimalFormat();
        FontRenderContext frc = utilities.c.getFontRenderContext();
        Font f = utilities.c.getFont();
        for (double d = minX; d <= maxX; d += tick) {
            if (paint) {
                String sb = df.format(d);
                Rectangle2D r = f.getStringBounds(sb, frc);
                height = Math.max(height, (int)r.getHeight());
            }
            paint = !paint;
        }
        assertEquals(height, utilities.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        double minY = -10.0;
        double maxY = 10.0;
        double tick = 1.0;
        double xpt = 0.0;
        int width = 0;
        if (constraints.getMinimumColumnValue() > 0.0) {
            xpt = constraints.getMinimumColumnValue();
        } else if (constraints.getMaximumColumnValue() < 0.0 && utilities.c.getSecondYAxis() != null) {
            xpt = constraints.getMaximumColumnValue();
        }
        boolean paint = false;
        DecimalFormat df = utilities.c.getYDecimalFormat();
        FontRenderContext frc = utilities.c.getFontRenderContext();
        Font f = utilities.c.getFont();
        for (double d = minY; d <= maxY; d += tick) {
            if (paint) {
                String sb = df.format(d);
                Rectangle2D r = f.getStringBounds(sb, frc);
                width = Math.max((int)r.getWidth(), width);
            }
            paint = !paint;
        }
        assertEquals(width, utilities.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(utilities.c.getFont());
        utilities.drawNumericalXAxisTicks(g);
        verify(g, times(1)).draw(argThat(isA(Line2D.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawNumericalXAxisTicksWithNullGraphics2D() {
        utilities.drawNumericalXAxisTicks(null);
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(utilities.c.getFont());
        utilities.drawXAxisTicks(g);
        verify(g, times(1)).draw(argThat(isA(Line2D.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawXAxisTicksWithNullGraphics2D() {
        utilities.drawXAxisTicks(null);
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(utilities.c.getFont());
        utilities.drawYAxisTicks(g);
        verify(g, times(1)).draw(argThat(isA(Line2D.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawYAxisTicksWithNullGraphics2D() {
        utilities.drawYAxisTicks(null);
    }

    @Test
    public void testGetXAxisLine2D() {
        Line2D xAxisLine2D = utilities.getXAxisLine2D();
        Point2D p1 = new Point2D.Double(0.0, 0.0);
        Point2D p2 = new Point2D.Double(utilities.constraints.getMaximumColumnValue(), 0.0);
        AffineTransform at = utilities.c.getTransform(0);
        Point2D l = at.transform(p1, null);
        Point2D r = at.transform(p2, null);
        assertEquals(l.getX(), xAxisLine2D.getX1());
        assertEquals(l.getY(), xAxisLine2D.getY1());
        assertEquals(r.getX(), xAxisLine2D.getX2());
        assertEquals(r.getY(), xAxisLine2D.getY2());
    }

    @Test
    public void testGetYAxisLine2D() {
        Line2D yAxisLine2D = utilities.getYAxisLine2D();
        Point2D p1 = new Point2D.Double(0.0, utilities.constraints.getMaximumValue().doubleValue());
        Point2D p2 = new Point2D.Double(0.0, utilities.constraints.getMinimumValue().doubleValue());
        AffineTransform at = utilities.c.getTransform(0);
        Point2D o = at.transform(p1, null);
        Point2D u = at.transform(p2, null);
        assertEquals(o.getX(), yAxisLine2D.getX1());
        assertEquals(o.getY(), yAxisLine2D.getY1());
        assertEquals(u.getX(), yAxisLine2D.getX2());
        assertEquals(u.getY(), yAxisLine2D.getY2());
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D secondYAxisLine2D = utilities.getSecondYAxisLine2D();
        Point2D p1 = new Point2D.Double(utilities.constraints2.getMaximumColumnValue(), 0.0);
        Point2D p2 = new Point2D.Double(utilities.constraints2.getMaximumColumnValue(), utilities.constraints2.getMinimumValue().doubleValue());
        AffineTransform at = utilities.c.getTransform(1);
        Point2D o = at.transform(p1, null);
        Point2D u = at.transform(p2, null);
        assertEquals(o.getX(), secondYAxisLine2D.getX1());
        assertEquals(o.getY(), secondYAxisLine2D.getY1());
        assertEquals(u.getX(), secondYAxisLine2D.getX2());
        assertEquals(u.getY(), secondYAxisLine2D.getY2());
    }
}