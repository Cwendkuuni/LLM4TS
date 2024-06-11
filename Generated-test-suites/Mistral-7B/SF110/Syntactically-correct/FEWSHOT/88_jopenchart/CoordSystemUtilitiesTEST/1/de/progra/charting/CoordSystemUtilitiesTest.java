package de.progra.charting.test;

import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;
import java.awt.*;
import java.text.*;
import java.util.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), new ChartDataModelConstraints(new DoubleRange(0.0, 10.0), new DoubleRange(-5.0, 5.0)), null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        assertEquals(20, util.computeLeftMargin());

        DoubleRange xRange = new DoubleRange(-5.0, 5.0);
        coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), xRange, null, null);
        util = new CoordSystemUtilities(coord, null, null, null);
        double xmin = xRange.getMinimum().doubleValue();
        double xmax = xRange.getMaximum().doubleValue();
        double labelWidth = 100.0;
        double labelHeight = 50.0;
        double tickSpacing = 1.0;
        int labelWidthInt = (int)labelWidth;
        int labelHeightInt = (int)labelHeight;
        int margin = (int)((labelWidthInt + 6 + labelHeight + 6 + 5) / (xmin + xmax) * (coord.getBounds().getWidth() - coord.getRightMargin()));
        margin += 20;
        assertEquals(margin, util.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), null, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        assertEquals(15, util.computeRightMargin());

        TextLayout layout = new TextLayout("X-Axis", coord.getFont(), coord.getFontRenderContext());
        double width = layout.getBounds().getWidth();
        int margin = (int)(width + 15.0 / 3.0);
        assertEquals(margin, util.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), null, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        assertEquals(15, util.computeTopMargin());

        TextLayout layout = new TextLayout("Y-Axis", coord.getFont(), coord.getFontRenderContext());
        double height = layout.getBounds().getHeight();
        int margin = (int)(height + 15.0 / 3.0 + layout.getDescent());
        assertEquals(margin, util.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), new DoubleRange(-5.0, 5.0), null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        assertEquals(20, util.computeBottomMargin());

        DoubleRange yRange = new DoubleRange(-5.0, 5.0);
        coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), null, yRange, null);
        util = new CoordSystemUtilities(coord, null, null, null);
        double ymin = yRange.getMinimum().doubleValue();
        double ymax = yRange.getMaximum().doubleValue();
        double labelHeight = 50.0;
        int labelHeightInt = (int)labelHeight;
        int margin = (int)((labelHeightInt + 6 + 6) / (ymin + ymax) * (coord.getBounds().getHeight() - coord.getTopMargin()));
        margin += 20;
        assertEquals(margin, util.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), new DoubleRange(-5.0, 5.0), null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        assertEquals(0, util.computeXAxisLabelHeight());

        DecimalFormat df = new DecimalFormat("#.#");
        FontRenderContext frc = coord.getFontRenderContext();
        Font f = coord.getFont();
        DoubleRange xRange = new DoubleRange(-5.0, 5.0);
        int tick = 1;
        double min = xRange.getMinimum().doubleValue();
        double max = xRange.getMaximum().doubleValue();
        int height = 0;
        for (double d = min; d <= max; d += tick) {
            String sb = df.format(d);
            TextLayout layout = new TextLayout(sb, f, frc);
            height += layout.getBounds().getHeight();
        }
        assertEquals(height, util.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), null, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        assertEquals(0, util.computeYAxisLabelWidth());

        DecimalFormat df = new DecimalFormat("#.#");
        FontRenderContext frc = coord.getFontRenderContext();
        Font f = coord.getFont();
        DoubleRange yRange = new DoubleRange(-5.0, 5.0);
        int tick = 1;
        double min = yRange.getMinimum().doubleValue();
        double max = yRange.getMaximum().doubleValue();
        int width = 0;
        for (double d = min; d <= max; d += tick) {
            String sb = df.format(d);
            TextLayout layout = new TextLayout(sb, f, frc);
            width += (int)layout.getBounds().getWidth();
        }
        assertEquals(width, util.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), new DoubleRange(-5.0, 5.0), null, null);
        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(coord.getFont());
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        util.drawNumericalXAxisTicks(g);
        verify(g, times(1)).draw(any(Line2D.class));
        verify(g, times(1)).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), new IntRange(0, 5), null, null);
        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(coord.getFont());
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        util.drawXAxisTicks(g);
        verify(g, times(5)).draw(any(Line2D.class));
        verify(g, times(5)).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), null, new DoubleRange(-5.0, 5.0), null);
        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(coord.getFont());
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        util.drawYAxisTicks(g);
        verify(g, times(1)).draw(any(Line2D.class));
        verify(g, times(1)).setColor(Color.lightGray);
        verify(g, times(1)).draw(any(Line2D.class));
        verify(g, times(1)).setColor(argThat(isA(Color.class)));
        verify(g, times(1)).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), new DoubleRange(-5.0, 5.0), null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        Line2D line = util.getXAxisLine2D();
        assertEquals(0.0, line.getX1(), 0.01);
        assertEquals(coord.getBounds().getHeight(), line.getY1(), 0.01);
        assertEquals(coord.getBounds().getWidth(), line.getX2(), 0.01);
        assertEquals(coord.getBounds().getY(), line.getY2(), 0.01);
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), null, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        Line2D line = util.getYAxisLine2D();
        assertEquals(coord.getBounds().getX(), line.getX1(), 0.01);
        assertEquals(0.0, line.getY1(), 0.01);
        assertEquals(coord.getBounds().getX() + coord.getBounds().getWidth(), line.getX2(), 0.01);
        assertEquals(coord.getBounds().getHeight(), line.getY2(), 0.01);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 100, 100), null, null, new DoubleRange(-5.0, 5.0));
        CoordSystemUtilities util = new CoordSystemUtilities(coord, null, null, null);
        Line2D line = util.getSecondYAxisLine2D();
        assertEquals(coord.getBounds().getX() + coord.getBounds().getWidth(), line.getX1(), 0.01);
        assertEquals(coord.getBounds().getY(), line.getY1(), 0.01);
        assertEquals(coord.getBounds().getX(), line.getX2(), 0.01);
        assertEquals(coord.getBounds().getHeight(), line.getY2(), 0.01);
    }
}