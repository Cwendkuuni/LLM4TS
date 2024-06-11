package de.progra.charting.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;
    private CoordSystem coordSystem;
    private ChartDataModel model;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;

    @Before
    public void setUp() {
        coordSystem = mock(CoordSystem.class);
        model = mock(ChartDataModel.class);
        constraints = new ChartDataModelConstraints();
        constraints2 = new ChartDataModelConstraints();
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        when(coordSystem.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 12));
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        when(coordSystem.getXAxisUnit()).thenReturn("Test Unit");
        when(coordSystem.getYAxisUnit()).thenReturn("Test Unit");

        int result = utilities.computeLeftMargin();
        int expected = 20;
        assertEquals(expected, result);

        constraints.setMinimumColumnValue(-20.0);
        constraints.setMaximumColumnValue(20.0);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        when(coordSystem.getXDecimalFormat()).thenReturn(decimalFormat);
        double labelWidth = 50.0;
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        TextLayout layout = new TextLayout("Test Label", coordSystem.getFont(), coordSystem.getFontRenderContext());
        when(layout.getBounds()).thenReturn(new Rectangle2D.Double(0.0, 0.0, labelWidth, layout.getBounds().getHeight()));

        result = utilities.computeLeftMargin();
        expected = (int) Math.ceil((labelWidth + 6 + 6) / 2.0);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRightMargin() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        when(coordSystem.getFont()).thenReturn(font);
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        when(coordSystem.getXAxisUnit()).thenReturn("Test Unit");

        int result = utilities.computeRightMargin();
        int expected = 15;
        assertEquals(expected, result);

        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        TextLayout layout = new TextLayout("Test Label", coordSystem.getFont(), coordSystem.getFontRenderContext());
        when(layout.getBounds()).thenReturn(new Rectangle2D.Double(0.0, 0.0, layout.getBounds().getWidth() + 10.0, layout.getBounds().getHeight()));

        result = utilities.computeRightMargin();
        expected = 25;
        assertEquals(expected, result);
    }

    @Test
    public void testComputeTopMargin() {
        when(coordSystem.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 12));
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        when(coordSystem.getXAxisUnit()).thenReturn("Test Unit");
        when(coordSystem.getYAxisUnit()).thenReturn("Test Unit");

        int result = utilities.computeTopMargin();
        int expected = 15;
        assertEquals(expected, result);

        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        TextLayout layout = new TextLayout("Test Label", coordSystem.getFont(), coordSystem.getFontRenderContext());
        when(layout.getBounds()).thenReturn(new Rectangle2D.Double(0.0, 0.0, layout.getBounds().getWidth(), layout.getBounds().getHeight() + 5.0));

        result = utilities.computeTopMargin();
        expected = 20;
        assertEquals(expected, result);
    }

    @Test
    public void testComputeBottomMargin() {
        constraints.setMinimumValue(Double.NEGATIVE_INFINITY);
        constraints.setMaximumValue(Double.POSITIVE_INFINITY);
        when(coordSystem.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 12));
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        when(coordSystem.getXAxisUnit()).thenReturn("Test Unit");

        int result = utilities.computeBottomMargin();
        int expected = 20;
        assertEquals(expected, result);

        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(10.0);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        when(coordSystem.getXDecimalFormat()).thenReturn(decimalFormat);
        double labelHeight = 25.0;
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        TextLayout layout = new TextLayout("Test Label", coordSystem.getFont(), coordSystem.getFontRenderContext());
        when(layout.getBounds()).thenReturn(new Rectangle2D.Double(0.0, 0.0, layout.getBounds().getWidth(), labelHeight));

        result = utilities.computeBottomMargin();
        expected = (int) Math.ceil((labelHeight + 6 + 6) / 2.0);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        when(coordSystem.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 12));
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));

        int result = utilities.computeXAxisLabelHeight();
        int expected = 0;
        assertEquals(expected, result);

        constraints.setMinimumColumnValue(-20.0);
        constraints.setMaximumColumnValue(20.0);
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        when(coordSystem.getXDecimalFormat()).thenReturn(decimalFormat);
        double min = -15.0;
        double max = 15.0;
        double tick = 1.0;
        when(coordSystem.getConstraints()).thenReturn(constraints);
        for (double d = min; d <= max; d += tick) {
            String label = decimalFormat.format(d);
            TextLayout layout = new TextLayout(label, coordSystem.getFont(), coordSystem.getFontRenderContext());
            result += layout.getBounds().getHeight();
        }

        expected = (int) Math.ceil(result);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        constraints.setMinimumValue(Double.NEGATIVE_INFINITY);
        constraints.setMaximumValue(Double.POSITIVE_INFINITY);
        when(coordSystem.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 12));
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));

        int result = utilities.computeYAxisLabelWidth();
        int expected = 0;
        assertEquals(expected, result);

        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(10.0);
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        when(coordSystem.getYDecimalFormat()).thenReturn(decimalFormat);
        double min = -15.0;
        double max = 15.0;
        double tick = 1.0;
        when(coordSystem.getConstraints()).thenReturn(constraints);
        for (double d = min; d <= max; d += tick) {
            String label = decimalFormat.format(d);
            TextLayout layout = new TextLayout(label, coordSystem.getFont(), coordSystem.getFontRenderContext());
            result += layout.getBounds().getWidth();
        }

        expected = (int) Math.ceil(result);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeLeftMargin_NullConstraints() {
        when(coordSystem.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 12));
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        when(coordSystem.getXAxisUnit()).thenReturn("Test Unit");
        when(coordSystem.getYAxisUnit()).thenReturn("Test Unit");
        constraints = null;

        utilities.computeLeftMargin();
    }

    @Test(expected = NullPointerException.class)
    public void testComputeLeftMargin_NullCoordSystem() {
        constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        when(coordSystem.getFont()).thenReturn(new Font("Arial", Font.PLAIN, 12));
        when(coordSystem.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));
        when(coordSystem.getXAxisUnit()).thenReturn("Test Unit");
        when(coordSystem.getYAxisUnit()).thenReturn("Test Unit");
        coordSystem = null;

        utilities.computeLeftMargin();
    }
}