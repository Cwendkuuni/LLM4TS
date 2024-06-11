package de.progra.charting.test;

import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.*;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities testObj;

    @Before
    public void setUp() {
        ChartDataModel model = new ChartDataModel();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        CoordSystem coord = new CoordSystem();
        testObj = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        double min = -10.0;
        double max = 10.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        int leftMargin = testObj.computeLeftMargin();
        assertEquals(36, leftMargin);

        min = -5.0;
        max = 5.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        leftMargin = testObj.computeLeftMargin();
        assertEquals(20, leftMargin);

        min = 0.0;
        max = 5.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        leftMargin = testObj.computeLeftMargin();
        assertEquals(20, leftMargin);

        min = -5.0;
        max = 0.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        leftMargin = testObj.computeLeftMargin();
        assertEquals(36, leftMargin);

        min = 5.0;
        max = 10.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        leftMargin = testObj.computeLeftMargin();
        assertEquals(36, leftMargin);

        min = 10.0;
        max = 15.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        leftMargin = testObj.computeLeftMargin();
        assertEquals(52, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        double xmin = -10.0;
        double xmax = 10.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);

        int rightMargin = testObj.computeRightMargin();
        assertEquals(33, rightMargin);

        xmin = 5.0;
        xmax = 10.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);

        rightMargin = testObj.computeRightMargin();
        assertEquals(15, rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        double ymin = -10.0;
        double ymax = 10.0;
        constraints.setMinimumValue(new Double(ymin));
        constraints.setMaximumValue(new Double(ymax));

        int topMargin = testObj.computeTopMargin();
        assertEquals(31, topMargin);

        ymin = 5.0;
        ymax = 10.0;
        constraints.setMinimumValue(new Double(ymin));
        constraints.setMaximumValue(new Double(ymax));

        topMargin = testObj.computeTopMargin();
        assertEquals(15, topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        double ymin = -10.0;
        double ymax = 10.0;
        constraints.setMinimumValue(new Double(ymin));
        constraints.setMaximumValue(new Double(ymax));

        int bottomMargin = testObj.computeBottomMargin();
        assertEquals(31, bottomMargin);

        ymin = 5.0;
        ymax = 10.0;
        constraints.setMinimumValue(new Double(ymin));
        constraints.setMaximumValue(new Double(ymax));

        bottomMargin = testObj.computeBottomMargin();
        assertEquals(15, bottomMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        double min = -10.0;
        double max = 10.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        int labelHeight = testObj.computeXAxisLabelHeight();
        assertEquals(12, labelHeight);

        min = -5.0;
        max = 5.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        labelHeight = testObj.computeXAxisLabelHeight();
        assertEquals(12, labelHeight);

        min = 5.0;
        max = 10.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        labelHeight = testObj.computeXAxisLabelHeight();
        assertEquals(12, labelHeight);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        double min = -10.0;
        double max = 10.0;
        constraints.setMinimumValue(new Double(min));
        constraints.setMaximumValue(new Double(max));

        int labelWidth = testObj.computeYAxisLabelWidth();
        assertEquals(15, labelWidth);

        min = 5.0;
        max = 10.0;
        constraints.setMinimumValue(new Double(min));
        constraints.setMaximumValue(new Double(max));

        labelWidth = testObj.computeYAxisLabelWidth();
        assertEquals(15, labelWidth);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        double min = -10.0;
        double max = 10.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(new Font("SansSerif", Font.PLAIN, 10));
        when(g.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));

        testObj.drawNumericalXAxisTicks(g);

        verify(g).draw(any(Line2D.class));
        verify(g, times(3)).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testDrawXAxisTicks() {
        int min = 0;
        int max = 5;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);

        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(new Font("SansSerif", Font.PLAIN, 10));
        when(g.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));

        testObj.drawXAxisTicks(g);

        verify(g).draw(any(Line2D.class));
        verify(g, times(4)).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testDrawYAxisTicks() {
        double min = -10.0;
        double max = 10.0;
        constraints.setMinimumValue(new Double(min));
        constraints.setMaximumValue(new Double(max));

        Graphics2D g = mock(Graphics2D.class);
        when(g.getFont()).thenReturn(new Font("SansSerif", Font.PLAIN, 10));
        when(g.getFontRenderContext()).thenReturn(mock(FontRenderContext.class));

        testObj.drawYAxisTicks(g);

        verify(g).draw(any(Line2D.class));
        verify(g, times(3)).drawString(anyString(), anyFloat(), anyFloat());
    }
}