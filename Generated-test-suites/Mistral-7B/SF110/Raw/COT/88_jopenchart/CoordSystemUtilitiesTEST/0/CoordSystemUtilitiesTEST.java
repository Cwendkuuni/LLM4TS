package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.util.ChartDataModelConstraints;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utils;
    private CoordSystem coord;
    private ChartDataModel model;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;

    @Before
    public void setUp() {
        coord = new CoordSystem(newjava.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), new java.awt.Font("SansSerif", Font.PLAIN, 12), new java.text.DecimalFormat("0.00"), 1000, 1000, 50, 50);
        model = new ChartDataModel();
        constraints = new ChartDataModelConstraints(-10.0, 10.0, 0.0, 10.0);
        constraints2 = new ChartDataModelConstraints(15.0, 25.0, 5.0, 15.0);
        utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        assertEquals(31, utils.computeLeftMargin());
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);
        assertEquals(88, utils.computeLeftMargin());
        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        assertEquals(53, utils.computeLeftMargin());
        constraints.setMinimumColumnValue(-15.0);
        constraints.setMaximumColumnValue(15.0);
        assertEquals(133, utils.computeLeftMargin());
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(0.0);
        assertEquals(20, utils.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        assertEquals(15, utils.computeRightMargin());
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(10.0);
        assertEquals(21, utils.computeRightMargin());
        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        assertEquals(15, utils.computeRightMargin());
        constraints.setMinimumColumnValue(-15.0);
        constraints.setMaximumColumnValue(15.0);
        assertEquals(31, utils.computeRightMargin());
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(0.0);
        assertEquals(15, utils.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        assertEquals(15, utils.computeTopMargin());
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(10.0);
        assertEquals(21, utils.computeTopMargin());
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);
        assertEquals(15, utils.computeTopMargin());
        constraints.setMinimumValue(-15.0);
        constraints.setMaximumValue(15.0);
        assertEquals(31, utils.computeTopMargin());
        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(0.0);
        assertEquals(15, utils.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        assertEquals(31, utils.computeBottomMargin());
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(10.0);
        assertEquals(53, utils.computeBottomMargin());
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);
        assertEquals(31, utils.computeBottomMargin());
        constraints.setMinimumValue(-15.0);
        constraints.setMaximumValue(15.0);
        assertEquals(77, utils.computeBottomMargin());
        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(0.0);
        assertEquals(20, utils.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        AtomicInteger height = new AtomicInteger();
        DecimalFormat df = utils.c.getXDecimalFormat();
        FontRenderContext frc = utils.c.getFontRenderContext();
        Font f = utils.c.getFont();
        for (double d = constraints.getMinimumColumnValue(); d <= constraints.getMaximumColumnValue(); d += ChartUtilities.calculateTickSpacing(constraints.getMinimumColumnValue(), constraints.getMaximumColumnValue())) {
            if (height.get() % 2 == 0) {
                String sb = df.format(d);
                Rectangle2D r = f.getStringBounds(sb, frc);
                height.addAndGet(Math.max(r.getHeight(), 5));
            }
        }
        assertEquals(165, height.get());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        AtomicInteger width = new AtomicInteger();
        DecimalFormat df = utils.c.getYDecimalFormat();
        FontRenderContext frc = utils.c.getFontRenderContext();
        Font f = utils.c.getFont();
        for (double d = constraints.getMinimumValue(); d <= constraints.getMaximumValue(); d += ChartUtilities.calculateTickSpacing(constraints.getMinimumValue(), constraints.getMaximumValue())) {
            if (width.get() % 2 == 0) {
                String sb = df.format(d);
                Rectangle2D r = f.getStringBounds(sb, frc);
                width.addAndGet(Math.max(r.getWidth(), 5));
            }
        }
        assertEquals(233, width.get());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = new java.awt.Graphics2D(new java.awt.BufferedImage(1000, 1000, java.awt.Image.TYPE_INT_RGB));
        utils.drawNumericalXAxisTicks(g);
        Line2D ticks = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
        for (double d = constraints.getMinimumColumnValue(); d <= constraints.getMaximumColumnValue(); d += ChartUtilities.calculateTickSpacing(constraints.getMinimumColumnValue(), constraints.getMaximumColumnValue())) {
            ticks = utils.getXAxisLine2D();
            assertEquals(true, g.getClipBounds().intersects(ticks.getBounds2D()));
        }
    }

    @Test (expected = NullPointerException.class)
    public void testDrawNumericalXAxisTicksNullGraphics() {
        utils.drawNumericalXAxisTicks(null);
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = new java.awt.Graphics2D(new java.awt.BufferedImage(1000, 1000, java.awt.Image.TYPE_INT_RGB));
        utils.drawXAxisTicks(g);
        Line2D ticks = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
        for (int i = 0; i < model.getSize(); i++) {
            String label = (String) model.getValueAt(i);
            ticks = utils.getXAxisLine2D();
            assertEquals(true, g.getClipBounds().intersects(ticks.getBounds2D()));
            assertEquals(label, utils.c.getXDecimalFormat().format(constraints.getMinimumColumnValue() + i));
        }
    }

    @Test (expected = NullPointerException.class)
    public void testDrawXAxisTicksNullGraphics() {
        utils.drawXAxisTicks(null);
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = new java.awt.Graphics2D(new java.awt.BufferedImage(1000, 1000, java.awt.Image.TYPE_INT_RGB));
        utils.drawYAxisTicks(g);
        Line2D ticks = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
        for (double d = constraints.getMinimumValue(); d <= constraints.getMaximumValue(); d += ChartUtilities.calculateTickSpacing(constraints.getMinimumValue(), constraints.getMaximumValue())) {
            ticks = utils.getYAxisLine2D();
            assertEquals(true, g.getClipBounds().intersects(ticks.getBounds2D()));
            assertEquals(utils.c.getYDecimalFormat().format(d), utils.c.getFont().getStringBounds(utils.c.getYDecimalFormat().format(d), utils.c.getFontRenderContext()).getBounds().getCenterX(), utils.c.getFont().getStringBounds(utils.c.getYDecimalFormat().format(d), utils.c.getFontRenderContext()).getBounds().getCenterY());
        }
    }

    @Test (expected = NullPointerException.class)
    public void testDrawYAxisTicksNullGraphics() {
        utils.drawYAxisTicks(null);
    }
}