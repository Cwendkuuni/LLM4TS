package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Test;

import de.progra.charting.model.ChartDataModelConstraints;
import de.progra.charting.model.CoordSystem;
import de.progra.charting.model.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), "Test CoordSystem", null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[] { -10.0, 10.0 }), Arrays.asList(new Double[] { 0.0, 20.0 }));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[] { 30.0, 50.0 }), Arrays.asList(new Double[] { 0.0, 40.0 }));
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);

        assertEquals(20, utilities.computeLeftMargin());

        constraints.setMinimumColumnValue(-20.0);
        constraints.setMaximumColumnValue(20.0);
        assertEquals(52, utilities.computeLeftMargin());

        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(20.0);
        DecimalFormat decimalFormat = utilities.c.getYDecimalFormat();
        TextLayout layout = new TextLayout(decimalFormat.format(1.0), utilities.c.getFont(), utilities.c.getFontRenderContext());
        int maxlmargin = utilities.computeYAxisLabelWidth() + 6;
        maxlmargin += (int)layout.getBounds().getWidth() + 6;
        int xmin = (int)(maxlmargin - constraints.getMinimumColumnValue() / (constraints.getMinimumColumnValue() + constraints.getMaximumColumnValue()) * (coordSystem.getBounds().getWidth() - coordSystem.getRightMargin()));
        xmin += 5;
        assertEquals(xmin, utilities.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), "Test CoordSystem", null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[] { 0.0, 10.0 }), Arrays.asList(new Double[] { 0.0, 20.0 }));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, null, null);

        assertEquals(15, utilities.computeRightMargin());

        constraints.setMinimumColumnValue(-20.0);
        constraints.setMaximumColumnValue(20.0);
        TextLayout layout = new TextLayout(utilities.c.getXAxisUnit(), utilities.c.getFont(), utilities.c.getFontRenderContext());
        int width = (int)(layout.getBounds().getWidth() + 15.0 / 3.0);
        assertEquals(width, utilities.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), "Test CoordSystem", null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[] { 0.0, 10.0 }), Arrays.asList(new Double[] { 0.0, 20.0 }));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, null, null);

        assertEquals(15, utilities.computeTopMargin());

        constraints.setMinimumValue(-20.0);
        constraints.setMaximumValue(20.0);
        TextLayout layout = new TextLayout(utilities.c.getXAxisUnit(), utilities.c.getFont(), utilities.c.getFontRenderContext());
        int height = (int)(layout.getBounds().getHeight() + 15.0 / 3.0 + layout.getDescent());
        assertEquals(height, utilities.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), "Test CoordSystem", null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[] { 0.0, 10.0 }), Arrays.asList(new Double[] { 0.0, 20.0 }));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[] { 0.0, 10.0 }), Arrays.asList(new Double[] { 0.0, 20.0 }));
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);

        assertEquals(20, utilities.computeBottomMargin());

        constraints.setMinimumValue(-20.0);
        constraints.setMaximumValue(20.0);
        TextLayout xAxisLayout = new TextLayout(utilities.c.getXAxisUnit(), utilities.c.getFont(), utilities.c.getFontRenderContext());
        int maxbmargin = utilities.computeXAxisLabelHeight() + 6;
        maxbmargin = Math.max(maxbmargin, (int)xAxisLayout.getBounds().getHeight() + 6);
        int ymin = (int)(maxbmargin - constraints.getMinimumValue().doubleValue() / (constraints.getMinimumValue().doubleValue() + constraints.getMaximumValue().doubleValue()) * (coordSystem.getBounds().getHeight() - coordSystem.getTopMargin()));
        ymin += 10;
        assertEquals(ymin, utilities.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), "Test CoordSystem", null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[] { 0.0, 10.0 }), Arrays.asList(new Double[] { 0.0, 20.0 }));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, null, null);

        assertEquals(0, utilities.computeXAxisLabelHeight());

        constraints.setMinimumColumnValue(-20.0);
        constraints.setMaximumColumnValue(20.0);
        double min = constraints.getMinimumValue().doubleValue();
        double max = constraints.getMaximumValue().doubleValue();
        double tick = ChartUtilities.calculateTickSpacing(min, max);
        double ypt = 0.0;
        if (constraints.getMinimumValue().doubleValue() > 0.0) {
            ypt = constraints.getMinimumValue().doubleValue();
        }
        else if (constraints.getMaximumValue().doubleValue() < 0.0) {
            ypt = constraints.getMaximumValue().doubleValue();
        }
        boolean paint = false;
        DecimalFormat df = utilities.c.getXDecimalFormat();
        FontRenderContext frc = utilities.c.getFontRenderContext();
        Font f = utilities.c.getFont();
        int height = 0;
        for (double d = min; d <= max; d += tick) {
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
        CoordSystem coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), "Test CoordSystem", null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[] { 0.0, 10.0 }), Arrays.asList(new Double[] { 0.0, 20.0 }));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, null, null);

        assertEquals(0, utilities.computeYAxisLabelWidth());

        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(20.0);
        double min = constraints.getMinimumValue().doubleValue();
        double max = constraints.getMaximumValue().doubleValue();
        double tick = ChartUtilities.calculateTickSpacing(min, max);
        double xpt = 0.0;
        if (constraints.getMinimumColumnValue() > 0.0) {
            xpt = constraints.getMinimumColumnValue();
        }
        else if (constraints.getMaximumColumnValue() < 0.0) {
            xpt = constraints.getMaximumColumnValue();
        }
        boolean paint = false;
        DecimalFormat df = utilities.c.getYDecimalFormat();
        FontRenderContext frc = utilities.c.getFontRenderContext();
        Font f = utilities.c.getFont();
        int width = 0;
        for (double d = min; d <= max; d += tick) {
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
        CoordSystem coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), "Test CoordSystem", null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[] { 0.0, 10.0 }), Arrays.asList(new Double[] { 0.0, 20.0 }));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[] { 0.0, 10.0 }), Arrays.asList(new Double[] { 0.0, 20.0 }));
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);

        Graphics2D g = mock(Graphics2D.class);

        utilities.drawNumericalXAxisTicks(g);

        // Not testing the actual drawing, but can be tested with a proper Graphics2D implementation
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), "Test CoordSystem", null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }), Arrays.asList(new Double[] { 0.0, 20.0 }));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, null, null);

        Graphics2D g = mock(Graphics2D.class);

        utilities.drawXAxisTicks(g);

        // Not testing the actual drawing, but can be tested with a proper Graphics2D implementation
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), "Test CoordSystem", null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[] { 0.0, 10.0 }), Arrays.asList(new Double[] { 0.0, 20.0 }));

        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, null, null);

        Graphics2D g = mock(Graphics2D.class);

        utilities.drawYAxisTicks(g);

        // Not testing the actual drawing, but can be tested with a proper Graphics2D implementation
    }
}