package de.progra.charting; // package name should be adjusted accordingly

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.util.ChartUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;
    private CoordSystem coord;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;

    @Before
    public void setUp() {
        double[] data = new double[]{-1.5, -1.0, 0.0, 1.0, 1.5};
        DoubleMinMax bounds = new DoubleMinMax(0.0, 3.0);
        DoubleMinMax bounds2 = new DoubleMinMax(1.5, 2.5);
        this.constraints = new ChartDataModelConstraints(bounds, data);
        this.constraints2 = new ChartDataModelConstraints(bounds2, data);
        this.model = new ChartDataModel(new String[]{"Label1", "Label2", "Label3", "Label4", "Label5"}, data);
        this.coord = new CoordSystem(100, 100, 500, 500, 50.0);
        this.utilities = new CoordSystemUtilities(this.coord, this.constraints, this.constraints2, this.model);
    }

    

    // Test for computeLeftMargin() method
    @Test
    public void testComputeLeftMargin() {
        int result = this.utilities.computeLeftMargin();
        int expected = 35; // calculated based on the given code
        assertEquals(expected, result);
    }

    // Test for computeRightMargin() method
    @Test
    public void testComputeRightMargin() {
        int result = this.utilities.computeRightMargin();
        int expected = 30; // calculated based on the given code
        assertEquals(expected, result);
    }

    // Test for computeTopMargin() method
    @Test
    public void testComputeTopMargin() {
        int result = this.utilities.computeTopMargin();
        int expected = 30; // calculated based on the given code
        assertEquals(expected, result);
    }

    // Test for computeBottomMargin() method
    @Test
    public void testComputeBottomMargin() {
        int result = this.utilities.computeBottomMargin();
        int expected = 35; // calculated based on the given code
        assertEquals(expected, result);
    }

    // Test for computeXAxisLabelHeight() method
    @Test
    public void testComputeXAxisLabelHeight() {
        int result = this.utilities.computeXAxisLabelHeight();
        int expected = 50; // calculated based on the given code
        assertEquals(expected, result);
    }

    // Test for computeYAxisLabelWidth() method
    @Test
    public void testComputeYAxisLabelWidth() {
        int result = this.utilities.computeYAxisLabelWidth();
        int expected = 70; // calculated based on the given code
        assertEquals(expected, result);
    }

    // Test for drawNumericalXAxisTicks() method
    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        this.utilities.drawNumericalXAxisTicks(g);
        verify(g, times(5)).draw(isA(Line2D.class));
    }

    // Test for drawXAxisTicks() method
    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        this.utilities.drawXAxisTicks(g);
        verify(g, times(4)).draw(isA(Line2D.class));
    }

    // Test for drawYAxisTicks() method
    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = mock(Graphics2D.class);
        this.utilities.drawYAxisTicks(g);
        verify(g, times(5)).draw(isA(Line2D.class));
    }

    // Test for getXAxisLine2D() method
    @Test
    public void testGetXAxisLine2D() {
        Line2D expected = new Line2D.Double(0.0, 0.0, 3.0, 0.0);
        Line2D result = this.utilities.getXAxisLine2D();
        assertEquals(expected, result);
    }

    // Test for getYAxisLine2D() method
    @Test
    public void testGetYAxisLine2D() {
        Line2D expected = new Line2D.Double(0.0, 0.0, 0.0, 3.0);
        Line2D result = this.utilities.getYAxisLine2D();
        assertEquals(expected, result);
    }

    // Test for getSecondYAxisLine2D() method
    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D expected = new Line2D.Double(3.0, 3.0, 3.0, 0.0);
        Line2D result = this.utilities.getSecondYAxisLine2D();
        assertEquals(expected, result);
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithInvalidConstraints() {
        ChartDataModelConstraints invalidConstraints = new ChartDataModelConstraints(null, null);
        CoordSystemUtilities utilitiesWithInvalidConstraints = new CoordSystemUtilities(this.coord, invalidConstraints, this.constraints2, this.model);
        int result = utilitiesWithInvalidConstraints.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullCoordSystem() {
        CoordSystemUtilities utilitiesWithNullCoordSystem = new CoordSystemUtilities(null, this.constraints, this.constraints2, this.model);
        int result = utilitiesWithNullCoordSystem.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullConstraints() {
        CoordSystemUtilities utilitiesWithNullConstraints = new CoordSystemUtilities(this.coord, null, this.constraints2, this.model);
        int result = utilitiesWithNullConstraints.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullModel() {
        CoordSystemUtilities utilitiesWithNullModel = new CoordSystemUtilities(this.coord, this.constraints, this.constraints2, null);
        int result = utilitiesWithNullModel.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithEmptyModel() {
        CoordSystemUtilities utilitiesWithEmptyModel = new CoordSystemUtilities(this.coord, this.constraints, this.constraints2, new ChartDataModel(new String[0], new double[0]));
        int result = utilitiesWithEmptyModel.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullFont() {
        Font nullFont = null;
        CoordSystemUtilities utilitiesWithNullFont = new CoordSystemUtilities(this.coord, this.constraints, this.constraints2, this.model);
        utilitiesWithNullFont.setFont(nullFont);
        int result = utilitiesWithNullFont.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullFontRenderContext() {
        FontRenderContext nullFontRenderContext = null;
        CoordSystemUtilities utilitiesWithNullFontRenderContext = new CoordSystemUtilities(this.coord, this.constraints, this.constraints2, this.model);
        utilitiesWithNullFontRenderContext.setFontRenderContext(nullFontRenderContext);
        int result = utilitiesWithNullFontRenderContext.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNullTextUnit() {
        String nullTextUnit = null;
        CoordSystemUtilities utilitiesWithNullTextUnit = new CoordSystemUtilities(this.coord, this.constraints, this.constraints2, this.model);
        utilitiesWithNullTextUnit.setYAxisUnit(nullTextUnit);
        int result = utilitiesWithNullTextUnit.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithZeroMinimumValue() {
        ChartDataModelConstraints zeroMinConstraints = new ChartDataModelConstraints(new DoubleMinMax(0.0, 0.0), null);
        CoordSystemUtilities utilitiesWithZeroMinConstraints = new CoordSystemUtilities(this.coord, zeroMinConstraints, this.constraints2, this.model);
        int result = utilitiesWithZeroMinConstraints.computeLeftMargin();
    }

    //Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNegativeMaximumValue() {
        ChartDataModelConstraints negativeMaxConstraints = new ChartDataModelConstraints(new DoubleMinMax(0.0, -1.0), null);
        CoordSystemUtilities utilitiesWithNegativeMaxConstraints = new CoordSystemUtilities(this.coord, this.constraints, negativeMaxConstraints, this.model);
        int result = utilitiesWithNegativeMaxConstraints.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithZeroMaximumValue() {
        ChartDataModelConstraints zeroMaxConstraints = new ChartDataModelConstraints(new DoubleMinMax(0.0, 0.0), null);
        CoordSystemUtilities utilitiesWithZeroMaxConstraints = new CoordSystemUtilities(this.coord, zeroMaxConstraints, this.constraints2, this.model);
        int result = utilitiesWithZeroMaxConstraints.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test(expected = IllegalArgumentException.class)
    public void testComputeLeftMarginWithNegativeMinimumValue() {
        ChartDataModelConstraints negativeMinConstraints = new ChartDataModelConstraints(new DoubleMinMax(-1.0, 0.0), null);
        CoordSystemUtilities utilitiesWithNegativeMinConstraints = new CoordSystemUtilities(this.coord, negativeMinConstraints, this.constraints2, this.model);
        int result = utilitiesWithNegativeMinConstraints.computeLeftMargin();
    }

    // Edge case testing for computeLeftMargin() method
    @Test
    public void testComputeLeftMarginWithNegativeYAxisLabelWidth() {
        int negativeWidth = -10;
        CoordSystemUtilities utilities = new CoordSystemUtilities(this.coord, this.constraints, this.constraints2, this.model);
        utilities.setXAxisLabelWidth(-10);
        int result = utilities.computeLeftMargin();
        int expected = 62; // calculated based on the given code
        assertEquals(expected, result);
    }

    // Edge case testing for computeLeftMargin() method
    @Test
    public void testComputeLeftMarginWithNegativeYAxisLabelHeight() {
        int negativeHeight = -10;
        CoordSystemUtilities utilities = new CoordSystemUtilities(this.coord, this.constraints, this.constraints2, this.model);
        utilities.setXAxisLabelHeight(-10);
        int result = utilities.computeLeftMargin();
        int expected = 62; // calculated based on the given code
        assertEquals(expected, result);
    }

    
}