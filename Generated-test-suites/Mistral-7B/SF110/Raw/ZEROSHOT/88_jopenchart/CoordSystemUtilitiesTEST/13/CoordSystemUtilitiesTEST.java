package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.DecimalFormat;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 600, 600), new Dimension(600, 600), new ChartDataModel(new DataColumn[] { new DataColumn("Col1", Double.class, null) }, new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } }));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, null);
        int actual = util.computeLeftMargin();
        int expected = 20;
        assertEquals(expected, actual);

        double xmin = -1.0;
        double xmax = 1.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        util = new CoordSystemUtilities(coord, constraints, constraints2, null);
        actual = util.computeLeftMargin();
        expected = (int)(5 + Math.max(util.computeYAxisLabelWidth(), 6) + Math.abs(xmin) / (xmin + xmax) * (coord.getBounds().getWidth() - coord.getRightMargin()));
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 600, 600), new Dimension(600, 600), new ChartDataModel(new DataColumn[] { new DataColumn("Col1", Double.class, null) }, new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } }));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        int actual = util.computeRightMargin();
        int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 600, 600), new Dimension(600, 600), new ChartDataModel(new DataColumn[] { new DataColumn("Col1", Double.class, null) }, new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } }));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        int actual = util.computeTopMargin();
        int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 600, 600), new Dimension(600, 600), new ChartDataModel(new DataColumn[] { new DataColumn("Col1", Double.class, null) }, new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } }));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, null);
        int actual = util.computeBottomMargin();
        int expected = 20;
        assertEquals(expected, actual);

        double ymin = -1.0;
        double ymax = 1.0;
        constraints2.setMinimumValue(new Double(ymin));
        constraints2.setMaximumValue(new Double(ymax));
        util = new CoordSystemUtilities(coord, constraints, constraints2, null);
        actual = util.computeBottomMargin();
        expected = (int)(5 + Math.max(util.computeXAxisLabelHeight(), 6) + Math.abs(ymin) / (ymin + ymax) * (coord.getBounds().getHeight() - coord.getTopMargin()));
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 600, 600), new Dimension(600, 600), new ChartDataModel(new DataColumn[] { new DataColumn("Col1", Double.class, null) }, new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } }));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        int actual = util.computeXAxisLabelHeight();
        int expected = 0;
        assertEquals(expected, actual);

        double min = 0.0;
        double max = 10.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);
        util = new CoordSystemUtilities(coord, constraints, null, null);
        actual = util.computeXAxisLabelHeight();
        expected = 40; // Approximate value, depends on the font used
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 600, 600), new Dimension(600, 600), new ChartDataModel(new DataColumn[] { new DataColumn("Col1", Double.class, null) }, new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } }));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        int actual = util.computeYAxisLabelWidth();
        int expected = 0;
        assertEquals(expected, actual);

        double min = 0.0;
        double max = 10.0;
        constraints.setMinimumValue(new Double(min));
        constraints.setMaximumValue(new Double(max));
        util = new CoordSystemUtilities(coord, constraints, null, null);
        actual = util.computeYAxisLabelWidth();
        expected = 30; // Approximate value, depends on the font used
        assertEquals(expected, actual);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 600, 600), new Dimension(600, 600), new ChartDataModel(new DataColumn[] { new DataColumn("Col1", Double.class, null) }, new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } }));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        when(g.getFont()).thenReturn(new Font("SansSerif", Font.BOLD, 12));
        util.drawNumericalXAxisTicks(g);

        verify(g, times(5)).draw(Mockito.any(Line2D.class));
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 600, 600), new Dimension(600, 600), new ChartDataModel(new DataColumn[] { new DataColumn("Col1", Double.class, null) }, new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } }));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        when(g.getFont()).thenReturn(new Font("SansSerif", Font.BOLD, 12));
        util.drawXAxisTicks(g);

        verify(g, times(5)).draw(Mockito.any(Line2D.class));
        verify(g, times(5)).drawString(anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 600, 600), new Dimension(600, 600), new ChartDataModel(new DataColumn[] { new DataColumn("Col1", Double.class, null) }, new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } }));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, null, null, null, null, new DecimalFormat("#.##"), 10, 20, 1.0, null, null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        when(g.getFont()).thenReturn(new Font("SansSerif", Font.BOLD, 12));
        util.drawYAxisTicks(g);

        verify(g, times(5)).draw(Mockito.any(Line2D.class));
    }
}