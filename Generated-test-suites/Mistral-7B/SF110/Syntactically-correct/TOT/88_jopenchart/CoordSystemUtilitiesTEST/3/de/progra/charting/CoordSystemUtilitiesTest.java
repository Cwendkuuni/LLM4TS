package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.5);
        constraints.setMaximumColumnValue(15.0);

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, null, null);
        int leftMargin = util.computeLeftMargin();

        CoordSystem c2 = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints2.setMinimumColumnValue(10.5);
        constraints2.setMaximumColumnValue(20.0);

        CoordSystemUtilities util2 = new CoordSystemUtilities(c2, constraints2, null, null);
        int leftMargin2 = util2.computeLeftMargin();

        assertEquals(20, leftMargin);
        assertEquals(31, leftMargin2);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        Font font = new Font("Arial", Font.BOLD, 12);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DecimalFormat decimalFormat2 = new DecimalFormat("#.##");
        DecimalFormat decimalFormat3 = new DecimalFormat("#.#####");

        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.5);
        constraints.setMaximumColumnValue(15.0);

        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints2.setMinimumColumnValue(10.5);
        constraints2.setMaximumColumnValue(20.0);

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, null, null);
        int rightMargin = util.computeRightMargin();

        CoordSystemUtilities util2 = new CoordSystemUtilities(c, constraints2, null, null);
        int rightMargin2 = util2.computeRightMargin();

        assertEquals(15, rightMargin);
        assertEquals(23, rightMargin2);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        Font font = new Font("Arial", Font.BOLD, 12);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DecimalFormat decimalFormat2 = new DecimalFormat("#.##");
        DecimalFormat decimalFormat3 = new DecimalFormat("#.#####");

        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.5);
        constraints.setMaximumColumnValue(15.0);

        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints2.setMinimumColumnValue(10.5);
        constraints2.setMaximumColumnValue(20.0);

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, null, null);
        int topMargin = util.computeTopMargin();

        CoordSystemUtilities util2 = new CoordSystemUtilities(c, constraints2, null, null);
        int topMargin2 = util2.computeTopMargin();

        assertEquals(15, topMargin);
        assertEquals(21, topMargin2);
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        Font font = new Font("Arial", Font.BOLD, 12);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DecimalFormat decimalFormat2 = new DecimalFormat("#.##");
        DecimalFormat decimalFormat3 = new DecimalFormat("#.#####");

        ChartDataModel model = new ChartDataModel();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints.setMinimumValue(-10.0);
        constraints.setMaximumValue(15.0);
        constraints2.setMinimumValue(10.0);
        constraints2.setMaximumValue(20.0);

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, constraints2, model);
        int bottomMargin = util.computeBottomMargin();

        CoordSystemUtilities util2 = new CoordSystemUtilities(c, constraints, constraints2, model);
        int bottomMargin2 = util2.computeBottomMargin();

        assertEquals(20, bottomMargin);
        assertEquals(20, bottomMargin2);

        ChartDataModelConstraints constraints3 = new ChartDataModelConstraints();
        constraints3.setMinimumValue(-5.0);
        constraints3.setMaximumValue(5.0);

        CoordSystemUtilities util3 = new CoordSystemUtilities(c, constraints, constraints2, model);
        int bottomMargin3 = util3.computeBottomMargin();

        CoordSystemUtilities util4 = new CoordSystemUtilities(c, constraints, constraints3, model);
        int bottomMargin4 = util4.computeBottomMargin();

        assertEquals(20, bottomMargin3);
        assertEquals(36, bottomMargin4);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        Font font = new Font("Arial", Font.BOLD, 12);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DecimalFormat decimalFormat2 = new DecimalFormat("#.##");
        DecimalFormat decimalFormat3 = new DecimalFormat("#.#####");

        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.5);
        constraints.setMaximumColumnValue(15.0);

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, null, null);
        int xAxisLabelHeight = util.computeXAxisLabelHeight();

        CoordSystemUtilities util2 = new CoordSystemUtilities(c, constraints, null, null);
        int xAxisLabelHeight2 = util2.computeXAxisLabelHeight();

        assertEquals(33, xAxisLabelHeight);
        assertEquals(33, xAxisLabelHeight2);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        Font font = new Font("Arial", Font.BOLD, 12);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DecimalFormat decimalFormat2 = new DecimalFormat("#.##");
        DecimalFormat decimalFormat3 = new DecimalFormat("#.#####");

        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.5);
        constraints.setMaximumColumnValue(15.0);

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, null, null);
        int yAxisLabelWidth = util.computeYAxisLabelWidth();

        CoordSystemUtilities util2 = new CoordSystemUtilities(c, constraints, null, null);
        int yAxisLabelWidth2 = util2.computeYAxisLabelWidth();

        assertEquals(66, yAxisLabelWidth);
        assertEquals(66, yAxisLabelWidth2);
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        Font font = new Font("Arial", Font.BOLD, 12);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DecimalFormat decimalFormat2 = new DecimalFormat("#.##");
        DecimalFormat decimalFormat3 = new DecimalFormat("#.#####");

        ChartDataModel model = new ChartDataModel();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.5);
        constraints.setMaximumColumnValue(15.0);
        constraints2.setMinimumColumnValue(10.5);
        constraints2.setMaximumColumnValue(20.0);

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, constraints2, model);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        when(g.getFont()).thenReturn(font);
        when(g.isPaintLabels()).thenReturn(true);

        util.drawXAxisTicks(g);

        verify(g, times(15)).drawString(argThat(matcher(containsString("-10.5"))), argThat(isA(Float.class)), argThat(isA(Float.class)));
        verify(g, times(15)).drawLine(argThat(isA(Double.class)), argThat(isA(Double.class)), argThat(isA(Double.class)), argThat(isA(Double.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNegativeXMinimum() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.5);
        constraints.setMaximumColumnValue(15.0);

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, null, null);
        int leftMargin = util.computeLeftMargin();

        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithZeroXMinimum() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(15.0);

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, null, null);
        int leftMargin = util.computeLeftMargin();

        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullConstraints() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        ChartDataModelConstraints constraints = null;

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, null, null);
        int leftMargin = util.computeLeftMargin();

        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullModel() {
        CoordSystem c = new CoordSystem(100, 100, 50, 50, 5, 5, 5, 5, null, null, null);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModel model = null;

        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, null, model);
        int leftMargin = util.computeLeftMargin();

        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullCoordSystem() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(null, constraints, constraints2, model);
        int leftMargin = util.computeLeftMargin();

        fail("Expected IllegalArgumentException");
    }
}