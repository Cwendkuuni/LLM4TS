package de.progra.charting.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;

    @Before
    public void setUp() {
        CoordSystem coord = new CoordSystem(new java.awt.Dimension(600, 600), 1.0, 1.0);
        ChartDataModel model = new ChartDataModel();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        this.utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    

    @Test
    public void testComputeLeftMargin() {
        double xmin = -10.0;
        double xmax = 20.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        CoordSystemUtilities utilities = new CoordSystemUtilities(null, constraints, null, null);
        int actual = utilities.computeLeftMargin();
        int expected = 76; // Calculated based on the given code
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithInvalidConstraints() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities utilities = new CoordSystemUtilities(null, constraints, null, null);
        utilities.computeLeftMargin();
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities utilities = this.utilities;
        int actual = utilities.computeRightMargin();
        int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities utilities = this.utilities;
        int actual = utilities.computeTopMargin();
        int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeBottomMargin() {
        double ymin = -10.0;
        double ymax = 20.0;
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        CoordSystemUtilities utilities = new CoordSystemUtilities(null, null, constraints, null);
        int actual = utilities.computeBottomMargin();
        int expected = 76; // Calculated based on the given code
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeBottomMarginWithInvalidConstraints() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities utilities = new CoordSystemUtilities(null, null, constraints, null);
        utilities.computeBottomMargin();
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        double min = -10.0;
        double max = 20.0;
        double tick = 1.0;
        CoordSystemUtilities utilities = this.utilities;
        int actual = utilities.computeXAxisLabelHeight();
        int expected = 21; // Calculated based on the given code
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        double min = -10.0;
        double max = 20.0;
        double tick = 1.0;
        CoordSystemUtilities utilities = this.utilities;
        int actual = utilities.computeYAxisLabelWidth();
        int expected = 11; // Calculated based on the given code
        assertEquals(expected, actual);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        CoordSystemUtilities utilities = this.utilities;
        utilities.drawNumericalXAxisTicks(g);
        verify(g, times(1)).draw(any(Line2D.class));
        verify(g, times(1)).setFont(any(Font.class));
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        CoordSystemUtilities utilities = this.utilities;
        utilities.drawXAxisTicks(g);
        verify(g, times(2)).draw(any(Line2D.class));
        verify(g, times(2)).setFont(any(Font.class));
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        CoordSystemUtilities utilities = this.utilities;
        utilities.drawYAxisTicks(g);
        verify(g, times(1)).draw(any(Line2D.class));
        verify(g, times(1)).setFont(any(Font.class));
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystemUtilities utilities = this.utilities;
        Line2D actual = utilities.getXAxisLine2D();
        Line2D expected = new Line2D.Double(0.0, 0.0, 1.0, 0.0);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystemUtilities utilities = this.utilities;
        Line2D actual = utilities.getYAxisLine2D();
        Line2D expected = new Line2D.Double(0.0, 0.0, 0.0, 1.0);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystemUtilities utilities = this.utilities;
        Line2D actual = utilities.getSecondYAxisLine2D();
        Line2D expected = new Line2D.Double(1.0, 0.0, 1.0, 1.0);
        assertEquals(expected, actual);
    }

    
}