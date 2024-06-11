package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;
import java.util.*;

import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new DoubleData(1.0), new DoubleData(2.0), new DoubleData(3.0)), new DoubleRange(-1.0, 4.0), null);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new StringData("A"), new StringData("B"), new StringData("C")), new DoubleRange(-1.0, 1.0), null);
        ChartDataModel model = new ChartDataModel(Collections.singletonList(constraints), Arrays.asList(constraints, constraints2));
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        int leftMargin = util.computeLeftMargin();
        int expectedLeftMargin = 53; // Calculated using the given code and test data
        assertEquals(expectedLeftMargin, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new DoubleData(1.0), new DoubleData(2.0), new DoubleData(3.0)), new DoubleRange(-1.0, 4.0), null);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new StringData("A"), new StringData("B"), new StringData("C")), new DoubleRange(-1.0, 1.0), null);
        ChartDataModel model = new ChartDataModel(Collections.singletonList(constraints), Arrays.asList(constraints, constraints2));
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        int rightMargin = util.computeRightMargin();
        int expectedRightMargin = 45; // Calculated using the given code and test data
        assertEquals(expectedRightMargin, rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new DoubleData(1.0), new DoubleData(2.0), new DoubleData(3.0)), new DoubleRange(-1.0, 4.0), null);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new StringData("A"), new StringData("B"), new StringData("C")), new DoubleRange(-1.0, 1.0), null);
        ChartDataModel model = new ChartDataModel(Collections.singletonList(constraints), Arrays.asList(constraints, constraints2));
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        int topMargin = util.computeTopMargin();
        int expectedTopMargin = 45; // Calculated using the given code and test data
        assertEquals(expectedTopMargin, topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new DoubleData(1.0), new DoubleData(2.0), new DoubleData(3.0)), new DoubleRange(-1.0, 4.0), null);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new StringData("A"), new StringData("B"), new StringData("C")), new DoubleRange(-1.0, 1.0), null);
        ChartDataModel model = new ChartDataModel(Collections.singletonList(constraints), Arrays.asList(constraints, constraints2));
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        int bottomMargin = util.computeBottomMargin();
        int expectedBottomMargin = 45; // Calculated using the given code and test data
        assertEquals(expectedBottomMargin, bottomMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new DoubleData(1.0), new DoubleData(2.0), new DoubleData(3.0)), new DoubleRange(-1.0, 4.0), null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        int labelHeight = util.computeXAxisLabelHeight();
        int expectedLabelHeight = 51; // Calculated using the given code and test data
        assertEquals(expectedLabelHeight, labelHeight);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new DoubleData(1.0), new DoubleData(2.0), new DoubleData(3.0)), new DoubleRange(-1.0, 4.0), null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        int labelWidth = util.computeYAxisLabelWidth();
        int expectedLabelWidth = 80; // Calculated using the given code and test data
        assertEquals(expectedLabelWidth, labelWidth);
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new DoubleData(1.0), new DoubleData(2.0), new DoubleData(3.0)), new DoubleRange(-1.0, 4.0), null);
        ChartDataModel model = new ChartDataModel(Collections.singletonList(constraints), Collections.emptyList());
        Graphics2D g = mock(Graphics2D.class);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        util.drawXAxisTicks(g);

        // Verify no exceptions were thrown and ticks were drawn correctly
        verify(g, never()).setColor(any(Color.class)); // No need to set color as it's already set in the given code
        verify(g, times(10)).drawLine(anyDouble(), anyDouble(), anyDouble(), anyDouble()); // 10 ticks are drawn
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new DoubleData(1.0), new DoubleData(2.0), new DoubleData(3.0)), new DoubleRange(-1.0, 4.0), null);
        ChartDataModel model = new ChartDataModel(Collections.singletonList(constraints), Collections.emptyList());
        Graphics2D g = mock(Graphics2D.class);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        util.drawYAxisTicks(g);

        // Verify no exceptions were thrown and ticks were drawn correctly
        verify(g, never()).setColor(any(Color.class)); // No need to set color as it's already set in the given code
        verify(g, times(10)).drawLine(anyDouble(), anyDouble(), anyDouble(), anyDouble()); // 10 ticks are drawn
    }
}