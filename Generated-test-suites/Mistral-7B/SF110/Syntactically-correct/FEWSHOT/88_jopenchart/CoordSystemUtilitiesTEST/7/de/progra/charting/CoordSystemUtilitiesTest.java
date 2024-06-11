package de.progra.charting.test;

import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.*;
import java.util.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-1.5);
        constraints.setMaximumColumnValue(5.5);
        CoordSystemUtilities util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        int leftMargin = util.computeLeftMargin();
        assertEquals(26, leftMargin);

        constraints.setMinimumColumnValue(0);
        constraints.setMaximumColumnValue(10);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        leftMargin = util.computeLeftMargin();
        assertEquals(20, leftMargin);

        constraints.setMinimumColumnValue(-5);
        constraints.setMaximumColumnValue(5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        leftMargin = util.computeLeftMargin();
        assertEquals(53, leftMargin);

        constraints.setMinimumColumnValue(-10);
        constraints.setMaximumColumnValue(10);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        leftMargin = util.computeLeftMargin();
        assertEquals(106, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        int rightMargin = util.computeRightMargin();
        assertEquals(15, rightMargin);

        constraints.setMinimumColumnValue(-1.5);
        constraints.setMaximumColumnValue(5.5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        rightMargin = util.computeRightMargin();
        assertEquals(32, rightMargin);

        constraints.setMinimumColumnValue(0);
        constraints.setMaximumColumnValue(10);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        rightMargin = util.computeRightMargin();
        assertEquals(15, rightMargin);

        constraints.setMinimumColumnValue(-5);
        constraints.setMaximumColumnValue(5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        rightMargin = util.computeRightMargin();
        assertEquals(32, rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        int topMargin = util.computeTopMargin();
        assertEquals(15, topMargin);

        constraints.setMinimumValue(Double.NEGATIVE_INFINITY);
        constraints.setMaximumValue(0.5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        topMargin = util.computeTopMargin();
        assertEquals(31, topMargin);

        constraints.setMinimumValue(0.5);
        constraints.setMaximumValue(10.5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        topMargin = util.computeTopMargin();
        assertEquals(15, topMargin);

        constraints.setMinimumValue(Double.POSITIVE_INFINITY);
        constraints.setMaximumValue(5.5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        topMargin = util.computeTopMargin();
        assertEquals(31, topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        int bottomMargin = util.computeBottomMargin();
        assertEquals(15, bottomMargin);

        constraints.setMinimumValue(Double.NEGATIVE_INFINITY);
        constraints.setMaximumValue(0.5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        bottomMargin = util.computeBottomMargin();
        assertEquals(31, bottomMargin);

        constraints.setMinimumValue(0.5);
        constraints.setMaximumValue(10.5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        bottomMargin = util.computeBottomMargin();
        assertEquals(15, bottomMargin);

        constraints.setMinimumValue(Double.POSITIVE_INFINITY);
        constraints.setMaximumValue(5.5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        bottomMargin = util.computeBottomMargin();
        assertEquals(31, bottomMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        int height = util.computeXAxisLabelHeight();
        assertEquals(0, height);

        constraints.setMinimumColumnValue(-1.5);
        constraints.setMaximumColumnValue(5.5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        height = util.computeXAxisLabelHeight();
        assertEquals(22, height);

        constraints.setMinimumColumnValue(0);
        constraints.setMaximumColumnValue(10);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        height = util.computeXAxisLabelHeight();
        assertEquals(14, height);

        constraints.setMinimumColumnValue(-5);
        constraints.setMaximumColumnValue(5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        height = util.computeXAxisLabelHeight();
        assertEquals(30, height);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        int width = util.computeYAxisLabelWidth();
        assertEquals(0, width);

        constraints.setMinimumValue(-1.5);
        constraints.setMaximumValue(5.5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        width = util.computeYAxisLabelWidth();
        assertEquals(30, width);

        constraints.setMinimumValue(0);
        constraints.setMaximumValue(10);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        width = util.computeYAxisLabelWidth();
        assertEquals(15, width);

        constraints.setMinimumValue(-5);
        constraints.setMaximumValue(5);
        util = new CoordSystemUtilities(new ChartCoordSystem(), constraints, null, null);

        width = util.computeYAxisLabelWidth();
        assertEquals(30, width);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystem coordSystem = new ChartCoordSystem();
        CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, null, null);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        util.drawNumericalXAxisTicks(g);

        // No ticks to draw since constraints not set up properly

        constraints.setMinimumColumnValue(-1.5);
        constraints.setMaximumColumnValue(5.5);
        util = new CoordSystemUtilities(coordSystem, constraints, null, null);

        Mockito.when(g.getFont()).thenReturn(new Font("Serif", Font.BOLD, 12));
        Mockito.when(g.isPaintLabels()).thenReturn(true);

        util.drawNumericalXAxisTicks(g);

        verify(g, times(15)).drawString(anyString(), anyFloat(), anyFloat());
        verify(g, times(15)).draw(any(Line2D.class));
    }

    @Test
    public void testDrawXAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystem coordSystem = new ChartCoordSystem();
        CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, null, null);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        util.drawXAxisTicks(g);

        // No ticks to draw since constraints not set up properly

        constraints.setMinimumColumnValue(-1);
        constraints.setMaximumColumnValue(11);
        util = new CoordSystemUtilities(coordSystem, constraints, null, null);

        Mockito.when(g.getFont()).thenReturn(new Font("Serif", Font.BOLD, 12));
        Mockito.when(g.isPaintLabels()).thenReturn(true);

        util.drawXAxisTicks(g);

        verify(g, times(11)).drawString(anyString(), anyFloat(), anyFloat());
        verify(g, times(11)).draw(any(Line2D.class));
    }

    @Test
    public void testDrawYAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        CoordSystem coordSystem = new ChartCoordSystem();
        CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, constraints2, null);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        util.drawYAxisTicks(g);

        // No ticks to draw since constraints not set up properly

        constraints.setMinimumValue(-1.5);
        constraints.setMaximumValue(5.5);
        constraints2.setMinimumColumnValue(0);
        constraints2.setMaximumColumnValue(10);
        util = new CoordSystemUtilities(coordSystem, constraints, constraints2, null);

        Mockito.when(g.getFont()).thenReturn(new Font("Serif", Font.BOLD, 12));
        Mockito.when(g.isPaintLabels()).thenReturn(true);

        util.drawYAxisTicks(g);

        verify(g, times(6)).drawString(anyString(), anyFloat(), anyFloat());
        verify(g, times(6)).draw(any(Line2D.class));
    }
}