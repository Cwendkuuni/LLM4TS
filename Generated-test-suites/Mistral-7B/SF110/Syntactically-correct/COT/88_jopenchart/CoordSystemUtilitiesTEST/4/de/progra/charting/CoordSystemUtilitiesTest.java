package de.progra.charting.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.*;
import java.awt.*;
import java.awt.geom.*;

public class CoordSystemUtilitiesTest {

    private CoordSystem coordSystem;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;
    private CoordSystemUtilities utilities;

    @Before
    public void setUp() {
        coordSystem = new CoordSystem(200, 400, new Point2D.Double(100, 100), new Point2D.Double(600, 600), 50, 10, 50, 10, null, null);
        constraints = new ChartDataModelConstraints(new DoubleRange(0.0, 10.0), new DoubleRange(0.0, 10.0));
        constraints2 = new ChartDataModelConstraints(new DoubleRange(0.0, -10.0), new DoubleRange(-10.0, 0.0));
        model = new ChartDataModel();

        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        assertEquals(20, utilities.computeLeftMargin());

        constraints.setMinimumColumnValue(-5.0);
        constraints.setMaximumColumnValue(5.0);
        double xmin = constraints.getMinimumColumnValue();
        double xmax = constraints.getMaximumColumnValue();
        double xminAbs = Math.abs(xmin);
        double xmaxAbs = Math.abs(xmax);

        int expectedLeftMargin = (int)(utilities.computeYAxisLabelWidth() + 12);
        expectedLeftMargin = Math.max(expectedLeftMargin, (int)(new TextLayout("Test", coordSystem.getFont(), coordSystem.getFontRenderContext()).getBounds().getWidth() + 6));
        expectedLeftMargin = Math.max(expectedLeftMargin, (int)((xminAbs / (xminAbs + xmaxAbs) * (coordSystem.getBounds().getWidth() - coordSystem.getRightMargin())) + 5));
        if (expectedLeftMargin < 20) {
            expectedLeftMargin = 20;
        }

        assertEquals(expectedLeftMargin, utilities.computeLeftMargin());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullConstraints() {
        utilities = new CoordSystemUtilities(coordSystem, null, constraints2, model);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullModel() {
        utilities = new CoordSystemUtilities(coordSystem, constraints, null, model);
    }

    @Test
    public void testComputeRightMargin() {
        assertEquals(15, utilities.computeRightMargin());

        final TextLayout layout = new TextLayout("Test", coordSystem.getFont(), coordSystem.getFontRenderContext());
        final double width = layout.getBounds().getWidth();

        int expectedRightMargin = (int)(width + 7.5);
        expectedRightMargin = Math.max(expectedRightMargin, 15);

        assertEquals(expectedRightMargin, utilities.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        assertEquals(15, utilities.computeTopMargin());

        final TextLayout layout = new TextLayout("Test", coordSystem.getFont(), coordSystem.getFontRenderContext());
        final double height = layout.getBounds().getHeight() + layout.getDescent();

        int expectedTopMargin = (int)(height + 7.5);
        expectedTopMargin = Math.max(expectedTopMargin, 15);

        assertEquals(expectedTopMargin, utilities.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        assertEquals(20, utilities.computeBottomMargin());

        double ymin = constraints.getMinimumValue().doubleValue();
        double ymax = constraints.getMaximumValue().doubleValue();
        double yminAbs = Math.abs(ymin);
        double ymaxAbs = Math.abs(ymax);

        int expectedBottomMargin = (int)(utilities.computeXAxisLabelHeight() + 12);
        expectedBottomMargin = Math.max(expectedBottomMargin, (int)(new TextLayout("Test", coordSystem.getFont(), coordSystem.getFontRenderContext()).getBounds().getHeight() + 6));
        expectedBottomMargin = Math.max(expectedBottomMargin, (int)((yminAbs / (yminAbs + ymaxAbs) * (coordSystem.getBounds().getHeight() - coordSystem.getTopMargin())) + 10));
        if (expectedBottomMargin < 20) {
            expectedBottomMargin = 20;
        }

        assertEquals(expectedBottomMargin, utilities.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        assertEquals(10, utilities.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        assertEquals(5, utilities.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);

        utilities.drawNumericalXAxisTicks(g);

        verify(g).draw(argThat(line -> line instanceof Line2D));
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);

        utilities.drawXAxisTicks(g);

        verify(g).draw(argThat(line -> line instanceof Line2D));
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);

        utilities.drawYAxisTicks(g);

        verify(g).draw(argThat(line -> line instanceof Line2D));
    }

    @Test
    public void testGetXAxisLine2D() {
        Line2D expected = new Line2D.Double(0.0, 0.0, 1000.0, 0.0);

        Line2D actual = utilities.getXAxisLine2D();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetYAxisLine2D() {
        Line2D expected = new Line2D.Double(0.0, 0.0, 0.0, 1000.0);

        Line2D actual = utilities.getYAxisLine2D();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D expected = new Line2D.Double(0.0, 1000.0, 0.0, 0.0);

        Line2D actual = utilities.getSecondYAxisLine2D();

        assertEquals(expected, actual);
    }
}