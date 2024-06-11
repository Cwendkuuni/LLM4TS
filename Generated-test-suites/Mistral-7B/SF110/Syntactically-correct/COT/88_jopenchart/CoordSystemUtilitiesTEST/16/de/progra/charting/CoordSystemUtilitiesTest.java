package de.progra.charting.test;

import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import org.junit.Before;
import org.junit.Test;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utils;

    @Before
    public void setup() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        this.utils = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        double xmin = 1.0;
        double xmax = 10.0;
        double expected = 26;
        int actual = this.utils.computeLeftMargin();
        assertEquals(expected, actual);

        xmin = -1.0;
        xmax = 1.0;
        expected = 20;
        actual = this.utils.computeLeftMargin();
        assertEquals(expected, actual);

        xmin = -1.0;
        xmax = 1.0;
        TextLayout layout = new TextLayout("YAxisUnit", this.utils.c.getFont(), this.utils.c.getFontRenderContext());
        int maxlmargin = this.utils.computeYAxisLabelWidth() + 6;
        maxlmargin += layout.getBounds().getWidth() + 6;
        double x = 5.0;
        double totalWidth = this.utils.c.getBounds().getWidth() - this.utils.c.getRightMargin();
        double leftMargin = (maxlmargin * x) / (xmax + xmin) * totalWidth;
        expected = (int) Math.ceil(leftMargin);
        actual = this.utils.computeLeftMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRightMargin() {
        TextLayout layout = new TextLayout("XAxisUnit", this.utils.c.getFont(), this.utils.c.getFontRenderContext());
        double width = layout.getBounds().getWidth();
        int expected = 15;
        int actual = this.utils.computeRightMargin();
        assertEquals(expected, actual);

        width = -15.0;
        expected = 15;
        actual = this.utils.computeRightMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeTopMargin() {
        TextLayout layout = new TextLayout("YAxisUnit", this.utils.c.getFont(), this.utils.c.getFontRenderContext());
        double height = layout.getBounds().getHeight();
        this.utils.c.getClass();
        int expected = 15;
        int actual = this.utils.computeTopMargin();
        assertEquals(expected, actual);

        height = -15.0;
        expected = 15;
        actual = this.utils.computeTopMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeBottomMargin() {
        double ymin = 1.0;
        double ymax = 10.0;
        double expected = 26;
        int actual = this.utils.computeBottomMargin();
        assertEquals(expected, actual);

        ymin = -1.0;
        ymax = 1.0;
        TextLayout layout = new TextLayout("XAxisUnit", this.utils.c.getFont(), this.utils.c.getFontRenderContext());
        int maxbmargin = this.utils.computeXAxisLabelHeight() + 6;
        maxbmargin += layout.getBounds().getHeight() + 6;
        double y = 5.0;
        double totalHeight = this.utils.c.getBounds().getHeight() - this.utils.c.getTopMargin();
        double bottomMargin = (maxbmargin * y) / (ymax + ymin) * totalHeight;
        expected = (int) Math.ceil(bottomMargin);
        actual = this.utils.computeBottomMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        double min = -10.0;
        double max = 10.0;
        double tick = 1.0;
        double ypt = 0.0;
        int expected = 21;
        int actual = this.utils.computeXAxisLabelHeight();
        assertEquals(expected, actual);

        min = 1.0;
        max = 10.0;
        ypt = 1.0;
        int height = 0;
        boolean paint = false;
        DecimalFormat df = this.utils.c.getXDecimalFormat();
        FontRenderContext frc = this.utils.c.getFontRenderContext();
        Font f = this.utils.c.getFont();
        for (double d = min; d <= max; d += tick) {
            if (paint) {
                String sb = df.format(d);
                Rectangle2D r = f.getStringBounds(sb, frc);
                height += Math.max(height, (int) r.getHeight());
            }
            paint = !paint;
        }
        expected = height;
        actual = this.utils.computeXAxisLabelHeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        double min = -10.0;
        double max = 10.0;
        double tick = 1.0;
        double xpt = 0.0;
        int expected = 21;
        int actual = this.utils.computeYAxisLabelWidth();
        assertEquals(expected, actual);

        min = 1.0;
        max = 10.0;
        xpt = 1.0;
        int width = 0;
        boolean paint = false;
        DecimalFormat df = this.utils.c.getYDecimalFormat();
        FontRenderContext frc = this.utils.c.getFontRenderContext();
        Font f = this.utils.c.getFont();
        for (double d = min; d <= max; d += tick) {
            if (paint) {
                String sb = df.format(d);
                Rectangle2D r = f.getStringBounds(sb, frc);
                width += Math.max(width, (int) r.getWidth());
            }
            paint = !paint;
        }
        expected = width;
        actual = this.utils.computeYAxisLabelWidth();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithInvalidConstraints() {
        this.utils.constraints = null;
        this.utils.computeLeftMargin();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeRightMarginWithInvalidConstraints() {
        this.utils.constraints = null;
        this.utils.computeRightMargin();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeTopMarginWithInvalidConstraints() {
        this.utils.constraints = null;
        this.utils.computeTopMargin();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeBottomMarginWithInvalidConstraints() {
        this.utils.constraints = null;
        this.utils.computeBottomMargin();
    }
}