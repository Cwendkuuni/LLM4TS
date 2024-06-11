package de.progra.charting;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        int leftMargin = util.computeLeftMargin();
        assertEquals(261, leftMargin);

        constraints = new CoordSystemConstraints(-5.0, 0.0, 3.0, 4.5);
        util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        leftMargin = util.computeLeftMargin();
        assertEquals(19, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        int rightMargin = util.computeRightMargin();
        assertEquals(45, rightMargin);

        constraints = new CoordSystemConstraints(0.0, -5.0, 3.0, 4.5);
        util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        rightMargin = util.computeRightMargin();
        assertEquals(15, rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        int topMargin = util.computeTopMargin();
        assertEquals(78, topMargin);

        constraints = new CoordSystemConstraints(10.0, -5.0, 30.0, 4.5);
        util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        topMargin = util.computeTopMargin();
        assertEquals(31, topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        int bottomMargin = util.computeBottomMargin();
        assertEquals(235, bottomMargin);

        constraints = new CoordSystemConstraints(10.0, 20.0, 3.0, -5.5);
        util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        bottomMargin = util.computeBottomMargin();
        assertEquals(135, bottomMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        int height = util.computeXAxisLabelHeight();
        assertEquals(31, height);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        int width = util.computeYAxisLabelWidth();
        assertEquals(85, width);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        Graphics2D g = Mockito.mock(Graphics2D.class);
        util.drawNumericalXAxisTicks(g);
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10, 20, 3, 4);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        Graphics2D g = Mockito.mock(Graphics2D.class);
        util.drawXAxisTicks(g);
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        Graphics2D g = Mockito.mock(Graphics2D.class);
        util.drawYAxisTicks(g);
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        Line2D line = util.getXAxisLine2D();
        assertNotNull(line);
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, null, null);
        Line2D line = util.getYAxisLine2D();
        assertNotNull(line);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystemConstraints constraints = new CoordSystemConstraints(10.0, 20.0, 30.0, 40.0);
        CoordSystemConstraints constraints2 = new CoordSystemConstraints(50.0, 20.0, 30.0, 40.0);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), constraints, constraints2, null);
        Line2D line = util.getSecondYAxisLine2D();
        assertNotNull(line);
    }
}