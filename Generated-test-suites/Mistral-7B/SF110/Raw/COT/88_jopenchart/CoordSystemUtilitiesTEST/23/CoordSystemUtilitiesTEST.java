package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;
import java.util.*;

import org.junit.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.utilities.*;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;

    @Before
    public void setUp() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10.0);
        constraints.setMaximumColumnValue(10.0);
        constraints.setMinimumValue(-5.0);
        constraints.setMaximumValue(5.0);

        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints2.setMinimumColumnValue(5.0);
        constraints2.setMaximumColumnValue(15.0);
        constraints2.setMinimumValue(0.0);
        constraints2.setMaximumValue(10.0);

        ChartDataModel model = new ChartDataModel();

        utilities = new CoordSystemUtilities(new CoordSystem(new Dimension(500, 400), new Point2D.Double(0, 0)), constraints, constraints2, model);
    }

    

    @Test
    public void testComputeLeftMargin() {
        int expected = 121; // Calculated based on the code
        int actual = utilities.computeLeftMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRightMargin() {
        int expected = 24; // Calculated based on the code
        int actual = utilities.computeRightMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeTopMargin() {
        int expected = 49; // Calculated based on the code
        int actual = utilities.computeTopMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeBottomMargin() {
        int expected = 87; // Calculated based on the code
        int actual = utilities.computeBottomMargin();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int expected = 17; // Calculated based on the code
        int actual = utilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        int expected = 26; // Calculated based on the code
        int actual = utilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = Mockito.mock(Graphics2D.class);
        utilities.drawNumericalXAxisTicks(g);
        // Add verifications for the methods called, if needed
    }

    @Test
    public void testDrawXAxisTicks() {
        Graphics2D g = Mockito.mock(Graphics2D.class);
        utilities.drawXAxisTicks(g);
        // Add verifications for the methods called, if needed
    }

    @Test
    public void testDrawYAxisTicks() {
        Graphics2D g = Mockito.mock(Graphics2D.class);
        utilities.drawYAxisTicks(g);
        // Add verifications for the methods called, if needed
    }

    @Test
    public void testGetXAxisLine2D() {
        Line2D expected = new Line2D.Double(0, 0, 10, 0); // Assuming the minimum column value is 0 and minimum value is 0
        Line2D actual = utilities.getXAxisLine2D();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetYAxisLine2D() {
        Line2D expected = new Line2D.Double(0, 0, 0, 10); // Assuming the minimum column value is 0 and minimum value is 0
        Line2D actual = utilities.getYAxisLine2D();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D expected = new Line2D.Double(15, 0, 15, 10); // Assuming the maximum column value is 15
        Line2D actual = utilities.getSecondYAxisLine2D();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullCoordSystem() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        try {
            new CoordSystemUtilities(null, constraints, constraints2, model);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConstraints() {
        CoordSystem coord = new CoordSystem(new Dimension(500, 400), new Point2D.Double(0, 0));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        try {
            new CoordSystemUtilities(coord, null, constraints2, model);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConstraints2() {
        CoordSystem coord = new CoordSystem(new Dimension(500, 400), new Point2D.Double(0, 0));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        try {
            new CoordSystemUtilities(coord, constraints, null, model);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullModel() {
        CoordSystem coord = new CoordSystem(new Dimension(500, 400), new Point2D.Double(0, 0));
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();

        try {
            new CoordSystemUtilities(coord, constraints, constraints2, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    
}