package de.progra.charting.test;

import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, 2.0);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(1, 3, 0.5, 2.0);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(40, util.computeLeftMargin());

        constraints.setMinimumColumnValue(0.1);
        constraints.setMaximumColumnValue(1.2);
        constraints2.setMinimumColumnValue(2.1);
        constraints2.setMaximumColumnValue(3.2);

        util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(105, util.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, 2.0);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(1, 3, 0.5, 2.0);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(21, util.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, 2.0);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(1, 3, 0.5, 2.0);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(31, util.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, new Double(-2));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(1, 3, 0.5, new Double(2));
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(51, util.computeBottomMargin());

        ChartDataModelConstraints constraints3 = new ChartDataModelConstraints(-1, 1, 0.5, new Double(1));
        CoordSystemUtilities util2 = new CoordSystemUtilities(coord, constraints3, constraints2, model);

        assertEquals(51, util2.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, 2.0);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        assertEquals(15, util.computeXAxisLabelHeight());

        constraints.setMinimumColumnValue(0.1);
        constraints.setMaximumColumnValue(1.2);

        util = new CoordSystemUtilities(coord, constraints, null, model);

        assertEquals(39, util.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, 2.0);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        assertEquals(15, util.computeYAxisLabelWidth());

        constraints.setMinimumValue(new Double(-1));
        constraints.setMaximumValue(new Double(1));

        util = new CoordSystemUtilities(coord, constraints, null, model);

        assertEquals(39, util.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, 2.0);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(1, 3, 0.5, 2.0);
        ChartDataModel model = new ChartDataModel();
        Graphics2D g = Mockito.mock(Graphics2D.class);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        util.drawNumericalXAxisTicks(g);

        Mockito.verify(g, Mockito.times(3)).draw(Mockito.any(Line2D.class));
        Mockito.verify(g, Mockito.times(3)).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, 2.0);
        ChartDataModel model = new ChartDataModel();
        Graphics2D g = Mockito.mock(Graphics2D.class);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        util.drawXAxisTicks(g);

        Mockito.verify(g, Mockito.times(9)).draw(Mockito.any(Line2D.class));
        Mockito.verify(g, Mockito.times(9)).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, new Double(-2));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(1, 3, 0.5, new Double(2));
        ChartDataModel model = new ChartDataModel();
        Graphics2D g = Mockito.mock(Graphics2D.class);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        util.drawYAxisTicks(g);

        Mockito.verify(g, Mockito.times(3)).draw(Mockito.any(Line2D.class));
        Mockito.verify(g, Mockito.times(3)).setColor(Mockito.any(Color.class));
        Mockito.verify(g, Mockito.times(3)).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, new Double(-2));
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        Line2D line = util.getXAxisLine2D();

        assertEquals(100.0, line.getX1(), 0.01);
        assertEquals(100.0, line.getX2(), 0.01);
        assertEquals(-2.0, line.getY1(), 0.01);
        assertEquals(-2.0, line.getY2(), 0.01);
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, new Double(-2));
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        Line2D line = util.getYAxisLine2D();

        assertEquals(100.0, line.getX1(), 0.01);
        assertEquals(200.0, line.getX2(), 0.01);
        assertEquals(-2.0, line.getY1(), 0.01);
        assertEquals(-1.5, line.getY2(), 0.01);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 50, 50);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-1, 1, 0.5, new Double(-2));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(1, 3, 0.5, new Double(2));
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, null);

        Line2D line = util.getSecondYAxisLine2D();

        assertEquals(200.0, line.getX1(), 0.01);
        assertEquals(200.0, line.getX2(), 0.01);
        assertEquals(2.0, line.getY1(), 0.01);
        assertEquals(1.5, line.getY2(), 0.01);
    }
}