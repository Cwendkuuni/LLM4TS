package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.*;
import java.awt.geom.*;
import java.awt.*;
import de.progra.charting.*;
import de.progra.charting.model.*;

import org.junit.*;

public class CoordSystemUtilitiesTest {
    

    private CoordSystemUtilities utilities;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;
    private CoordSystem coord;

    @Before
    public void setUp() {
        constraints = new ChartDataModelConstraints();
        constraints2 = new ChartDataModelConstraints();
        model = new ChartDataModel();
        coord = new CoordSystem(new java.awt.Rectangle(10, 10, 600, 600), model, constraints, constraints2);
        utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        // Test with valid values
        double xmin = -10.0;
        double xmax = 10.0;
        int expectedLeftMargin = 76;
        int actualLeftMargin = utilities.computeLeftMargin();
        assertEquals(expectedLeftMargin, actualLeftMargin);

        // Test with minimum column value less than 0
        xmin = -5.0;
        xmax = 5.0;
        expectedLeftMargin = 105;
        actualLeftMargin = utilities.computeLeftMargin();
        assertEquals(expectedLeftMargin, actualLeftMargin);

        // Test with maximum column value greater than 0
        xmin = 5.0;
        xmax = 15.0;
        expectedLeftMargin = 44;
        actualLeftMargin = utilities.computeLeftMargin();
        assertEquals(expectedLeftMargin, actualLeftMargin);

        // Test with minimum column value and maximum column value equal to 0
        xmin = 0.0;
        xmax = 0.0;
        expectedLeftMargin = 20;
        actualLeftMargin = utilities.computeLeftMargin();
        assertEquals(expectedLeftMargin, actualLeftMargin);

        // Test with exception
        xmin = Double.NaN;
        xmax = xmin;
        try {
            utilities.computeLeftMargin();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeRightMargin() {
        // Test with valid values
        int expectedRightMargin = 33;
        int actualRightMargin = utilities.computeRightMargin();
        assertEquals(expectedRightMargin, actualRightMargin);

        // Test with exception
        try {
            utilities.computeRightMargin();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testComputeTopMargin() {
        // Test with valid values
        int expectedTopMargin = 33;
        int actualTopMargin = utilities.computeTopMargin();
        assertEquals(expectedTopMargin, actualTopMargin);

        // Test with exception
        try {
            utilities.computeTopMargin();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testComputeBottomMargin() {
        // Test with valid values
        double ymin = -10.0;
        double ymax = 10.0;
        int expectedBottomMargin = 76;
        int actualBottomMargin = utilities.computeBottomMargin();
        assertEquals(expectedBottomMargin, actualBottomMargin);

        // Test with minimum value less than 0
        ymin = -5.0;
        ymax = 5.0;
        expectedBottomMargin = 116;
        actualBottomMargin = utilities.computeBottomMargin();
        assertEquals(expectedBottomMargin, actualBottomMargin);

        // Test with maximum value greater than 0
        ymin = 5.0;
        ymax = 15.0;
        expectedBottomMargin = 44;
        actualBottomMargin = utilities.computeBottomMargin();
        assertEquals(expectedBottomMargin, actualBottomMargin);

        // Test with minimum value and maximum value equal to 0
        ymin = 0.0;
        ymax = 0.0;
        expectedBottomMargin = 20;
        actualBottomMargin = utilities.computeBottomMargin();
        assertEquals(expectedBottomMargin, actualBottomMargin);

        // Test with exception
        ymin = Double.NaN;
        ymax = ymin;
        try {
            utilities.computeBottomMargin();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        // Test with valid values
        double min = -10.0;
        double max = 10.0;
        int expectedXAxisLabelHeight = 50;
        int actualXAxisLabelHeight = utilities.computeXAxisLabelHeight();
        assertEquals(expectedXAxisLabelHeight, actualXAxisLabelHeight);

        // Test with minimum value less than 0
        min = -5.0;
        expectedXAxisLabelHeight = 76;
        actualXAxisLabelHeight = utilities.computeXAxisLabelHeight();
        assertEquals(expectedXAxisLabelHeight, actualXAxisLabelHeight);

        // Test with maximum value greater than 0
        max = 5.0;
        expectedXAxisLabelHeight = 24;
        actualXAxisLabelHeight = utilities.computeXAxisLabelHeight();
        assertEquals(expectedXAxisLabelHeight, actualXAxisLabelHeight);

        // Test with minimum value and maximum value equal to 0
        min = 0.0;
        max = 0.0;
        expectedXAxisLabelHeight = 20;
        actualXAxisLabelHeight = utilities.computeXAxisLabelHeight();
        assertEquals(expectedXAxisLabelHeight, actualXAxisLabelHeight);

        // Test with exception
        try {
            utilities.computeXAxisLabelHeight();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        // Test with valid values
        double min = -10.0;
        double max = 10.0;
        int expectedYAxisLabelWidth = 50;
        int actualYAxisLabelWidth = utilities.computeYAxisLabelWidth();
        assertEquals(expectedYAxisLabelWidth, actualYAxisLabelWidth);

        // Test with minimum value less than 0
        min = -5.0;
        expectedYAxisLabelWidth = 76;
        actualYAxisLabelWidth = utilities.computeYAxisLabelWidth();
        assertEquals(expectedYAxisLabelWidth, actualYAxisLabelWidth);

        // Test with maximum value greater than 0
        max = 5.0;
        expectedYAxisLabelWidth = 24;
        actualYAxisLabelWidth = utilities.computeYAxisLabelWidth();
        assertEquals(expectedYAxisLabelWidth, actualYAxisLabelWidth);

        // Test with minimum value and maximum value equal to 0
        min = 0.0;
        max = 0.0;
        expectedYAxisLabelWidth = 20;
        actualYAxisLabelWidth = utilities.computeYAxisLabelWidth();
        assertEquals(expectedYAxisLabelWidth, actualYAxisLabelWidth);

        // Test with exception
        try {
            utilities.computeYAxisLabelWidth();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Test with valid values
        Graphics2D g = mock(Graphics2D.class);
        when(g.getTransform()).thenReturn(mock(AffineTransform.class));
        when(g.getFont()).thenReturn(mock(Font.class));
        utilities.drawNumericalXAxisTicks(g);

        // Test with exception
        g = mock(Graphics2D.class);
        when(g.getTransform()).thenThrow(new RuntimeException());
        utilities.drawNumericalXAxisTicks(g);
    }

    @Test
    public void testDrawXAxisTicks() {
        // Test with valid values
        Graphics2D g = mock(Graphics2D.class);
        when(g.getTransform()).thenReturn(mock(AffineTransform.class));
        when(g.getFont()).thenReturn(mock(Font.class));
        utilities.drawXAxisTicks(g);

        // Test with exception
        g = mock(Graphics2D.class);
        when(g.getTransform()).thenThrow(new RuntimeException());
        utilities.drawXAxisTicks(g);
    }

    @Test
    public void testDrawYAxisTicks() {
        // Test with valid values
        Graphics2D g = mock(Graphics2D.class);
        when(g.getTransform()).thenReturn(mock(AffineTransform.class));
        when(g.getFont()).thenReturn(mock(Font.class));
        utilities.drawYAxisTicks(g);

        // Test with exception
        g = mock(Graphics2D.class);
        when(g.getTransform()).thenThrow(new RuntimeException());
        utilities.drawYAxisTicks(g);
    }

    @Test
    public void testGetXAxisLine2D() {
        // Test with valid values
        Line2D expectedXAxisLine = new Line2D.Double(0.0, 0.0, 600.0, 0.0);
        Line2D actualXAxisLine = utilities.getXAxisLine2D();
        assertEquals(expectedXAxisLine, actualXAxisLine);

        // Test with exception
        try {
            utilities.getXAxisLine2D();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetYAxisLine2D() {
        // Test with valid values
        Line2D expectedYAxisLine = new Line2D.Double(0.0, 600.0, 0.0, 0.0);
        Line2D actualYAxisLine = utilities.getYAxisLine2D();
        assertEquals(expectedYAxisLine, actualYAxisLine);

        // Test with exception
        try {
            utilities.getYAxisLine2D();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // Test with valid values
        Line2D expectedSecondYAxisLine = new Line2D.Double(600.0, 0.0, 600.0, 600.0);
        Line2D actualSecondYAxisLine = utilities.getSecondYAxisLine2D();
        assertEquals(expectedSecondYAxisLine, actualSecondYAxisLine);

        // Test with exception
        try {
            utilities.getSecondYAxisLine2D();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    
}