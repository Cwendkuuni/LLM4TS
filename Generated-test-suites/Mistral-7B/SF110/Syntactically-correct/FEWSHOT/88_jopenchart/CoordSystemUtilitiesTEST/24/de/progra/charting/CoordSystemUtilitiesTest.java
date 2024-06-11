package de.progra.charting.test;

import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;

import java.awt.*;
import java.text.*;

public class CoordSystemUtilitiesTest {
    @Test
    public void testComputeLeftMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities utils = new CoordSystemUtilities(new StaticAxis(null, null, null), constraints, constraints2, model);

        // Test with valid x values
        double xmin = -10.0;
        double xmax = 10.0;
        constraints.setMinimumColumnValue(xmin);
        constraints.setMaximumColumnValue(xmax);
        int expectedLeftMargin = 53; // Calculated using the code in the test

        int actualLeftMargin = utils.computeLeftMargin();
        assertEquals(expectedLeftMargin, actualLeftMargin);

        // Test with invalid x values
        constraints.setMinimumColumnValue(0.0);
        constraints.setMaximumColumnValue(0.0);
        int actualLeftMargin2 = utils.computeLeftMargin();
        assertEquals(20, actualLeftMargin2);
    }

    @Test
    public void testComputeRightMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities utils = new CoordSystemUtilities(new StaticAxis(null, null, null), constraints, constraints2, model);

        int expectedRightMargin = 21; // Calculated using the code in the test
        int actualRightMargin = utils.computeRightMargin();
        assertEquals(expectedRightMargin, actualRightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities utils = new CoordSystemUtilities(new StaticAxis(null, null, null), constraints, constraints2, model);

        int expectedTopMargin = 21; // Calculated using the code in the test
        int actualTopMargin = utils.computeTopMargin();
        assertEquals(expectedTopMargin, actualTopMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities utils = new CoordSystemUtilities(new StaticAxis(null, null, null), constraints, constraints2, model);

        double ymin = -10.0;
        double ymax = 10.0;
        constraints.setMinimumValue(ymin);
        constraints.setMaximumValue(ymax);
        int expectedBottomMargin = 53; // Calculated using the code in the test

        int actualBottomMargin = utils.computeBottomMargin();
        assertEquals(expectedBottomMargin, actualBottomMargin);

        // Test with invalid y values
        constraints.setMinimumValue(0.0);
        constraints.setMaximumValue(0.0);
        int actualBottomMargin2 = utils.computeBottomMargin();
        assertEquals(20, actualBottomMargin2);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities utils = new CoordSystemUtilities(new StaticAxis(null, null, null), constraints, constraints2, model);

        double min = -10.0;
        double max = 10.0;
        constraints.setMinimumValue(min);
        constraints.setMaximumValue(max);
        int expectedHeight = 7; // Calculated using the code in the test

        int actualHeight = utils.computeXAxisLabelHeight();
        assertEquals(expectedHeight, actualHeight);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities utils = new CoordSystemUtilities(new StaticAxis(null, null, null), constraints, constraints2, model);

        double min = -10.0;
        double max = 10.0;
        constraints.setMinimumValue(min);
        constraints.setMaximumValue(max);
        int expectedWidth = 7; // Calculated using the code in the test

        int actualWidth = utils.computeYAxisLabelWidth();
        assertEquals(expectedWidth, actualWidth);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities utils = new CoordSystemUtilities(new StaticAxis(null, null, null), constraints, constraints2, model);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        double min = -10.0;
        double max = 10.0;
        constraints.setMinimumColumnValue(min);
        constraints.setMaximumColumnValue(max);
        int tick = 1;

        utils.drawNumericalXAxisTicks(g);

        // Verify that the x axis ticks have been drawn
        Line2D expectedTickLine = new Line2D.Double(min, 0.0, min, 0.0 + 6.0);
        Line2D actualTickLine = Mockito.mock(Line2D.class);
        when(g.draw(any(Line2D.class))).thenReturn(actualTickLine);
        verify(g).draw(expectedTickLine);

        // Verify that the x axis labels have been drawn
        DecimalFormat df = new DecimalFormat("#.##");
        String expectedLabel = df.format(min);
        Point2D expectedLabelPosition = new Point2D.Double(min - 3.0, 6.0);
        when(g.drawString(eq(expectedLabel), anyFloat(), anyFloat())).thenReturn(true);
        verify(g).drawString(eq(expectedLabel), eq(expectedLabelPosition.getX()), eq(expectedLabelPosition.getY()));
    }

    @Test
    public void testDrawXAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities utils = new CoordSystemUtilities(new StaticAxis(null, null, null), constraints, constraints2, model);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        int min = 0;
        int max = 10;
        double tick = 1.0;

        utils.drawXAxisTicks(g);

        // Verify that the x axis ticks have been drawn
        Line2D expectedTickLine = new Line2D.Double(min + 1, 0.0, min + 1, 0.0 + 6.0);
        Line2D actualTickLine = Mockito.mock(Line2D.class);
        when(g.draw(any(Line2D.class))).thenReturn(actualTickLine);
        verify(g).draw(expectedTickLine);

        // Verify that the x axis labels have been drawn
        DecimalFormat df = new DecimalFormat("#");
        String expectedLabel = df.format(min + 1);
        Point2D expectedLabelPosition = new Point2D.Double((min + 1) - 3.0, 6.0);
        when(g.drawString(eq(expectedLabel), anyFloat(), anyFloat())).thenReturn(true);
        verify(g).drawString(eq(expectedLabel), eq(expectedLabelPosition.getX()), eq(expectedLabelPosition.getY()));
    }

    @Test
    public void testDrawYAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities utils = new CoordSystemUtilities(new StaticAxis(null, null, null), constraints, constraints2, model);

        Graphics2D g = Mockito.mock(Graphics2D.class);
        double min = -10.0;
        double max = 10.0;
        double tick = 1.0;

        utils.drawYAxisTicks(g);

        // Verify that the y axis ticks have been drawn
        Line2D expectedTickLine = new Line2D.Double(0.0 - 3.0, min, 0.0 + 3.0, min);
        Line2D actualTickLine = Mockito.mock(Line2D.class);
        when(g.draw(any(Line2D.class))).thenReturn(actualTickLine);
        verify(g).draw(expectedTickLine);

        // Verify that the y axis labels have been drawn
        DecimalFormat df = new DecimalFormat("#.##");
        String expectedLabel = df.format(min);
        Point2D expectedLabelPosition = new Point2D.Double(6.0, min - 3.0);
        when(g.drawString(eq(expectedLabel), anyFloat(), anyFloat())).thenReturn(true);
        verify(g).drawString(eq(expectedLabel), eq(expectedLabelPosition.getX()), eq(expectedLabelPosition.getY()));
    }
}