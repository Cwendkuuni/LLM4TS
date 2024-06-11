package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystem coordSystem;
    private ChartDataModel model;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;

    @Before
    public void setup() {
        coordSystem = new CoordSystem(100, 100, 50, 50);
        model = new ChartDataModel();
        constraints = new ChartDataModelConstraints();
        constraints2 = new ChartDataModelConstraints();
    }

    @Test
    public void testComputeLeftMargin_ValidData() {
        constraints.setMinimumColumnValue(-100.0);
        constraints.setMaximumColumnValue(100.0);
        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        int leftMargin = utilities.computeLeftMargin();
        int expectedLeftMargin = 116; // Calculate the expected left margin
        assertEquals(expectedLeftMargin, leftMargin);
    }

    @Test
    public void testComputeLeftMargin_ZeroAxisValues() {
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(0.0);
        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        int leftMargin = utilities.computeLeftMargin();
        int expectedLeftMargin = 20; // Calculate the expected left margin
        assertEquals(expectedLeftMargin, leftMargin);
    }

    // Add similar test cases for computeRightMargin(), computeTopMargin(), and computeBottomMargin()

    @Test
    public void testComputeXAxisLabelHeight_ValidData() {
        constraints.setMinimumValue(-50.0);
        constraints.setMaximumValue(50.0);
        constraints.setMinimumColumnValue(0.0);
        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        int height = utilities.computeXAxisLabelHeight();
        int expectedHeight = 39; // Calculate the expected height
        assertEquals(expectedHeight, height);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeXAxisLabelHeight_NullConstraints() {
        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, null, constraints2, model);
        int height = utilities.computeXAxisLabelHeight();
    }

    // Add similar test cases for computeYAxisLabelWidth()

    @Test
    public void testDrawNumericalXAxisTicks_ValidData() {
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        Graphics2D mockGraphics = mock(Graphics2D.class);
        utilities.drawNumericalXAxisTicks(mockGraphics);
        // Add verification for the drawn ticks and labels if needed
    }

    @Test(expected = NullPointerException.class)
    public void testDrawNumericalXAxisTicks_NullGraphics() {
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        Graphics2D mockGraphics = null;
        utilities.drawNumericalXAxisTicks(mockGraphics);
    }

    // Add similar test cases for drawXAxisTicks(), drawYAxisTicks(), getXAxisLine2D(), getYAxisLine2D(), and getSecondYAxisLine2D()

    @Test(expected = IllegalArgumentException.class)
    public void testDrawXAxisTicks_InvalidData() {
        constraints.setMinimumColumnValue(Double.NaN);
        constraints.setMaximumColumnValue(Double.NaN);
        CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        Graphics2D mockGraphics = mock(Graphics2D.class);
        utilities.drawXAxisTicks(mockGraphics);
    }
}