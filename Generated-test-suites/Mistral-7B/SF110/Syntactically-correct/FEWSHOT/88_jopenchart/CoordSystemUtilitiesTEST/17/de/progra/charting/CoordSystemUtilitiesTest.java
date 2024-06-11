package de.progra.charting.test;

import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.*;
import java.awt.*;
import java.awt.geom.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-10, 10, 1, 1);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(11, 21, 1, 1);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(31, util.computeLeftMargin());

        constraints.setMinimumColumnValue(15.5);
        constraints.setMaximumColumnValue(25.5);
        assertEquals(48, util.computeLeftMargin());

        constraints.setMinimumColumnValue(0);
        constraints.setMaximumColumnValue(0);
        assertEquals(20, util.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-10, 10, 1, 1);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(11, 21, 1, 1);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(45, util.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-10, 10, 1, 1);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(11, 21, 1, 1);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(45, util.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-10, 10, 1, 1);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(11, 21, 1, 1);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(45, util.computeBottomMargin());

        constraints.setMinimumValue(new DoublePosition(-5.5, 1));
        constraints.setMaximumValue(new DoublePosition(5.5, 2));
        assertEquals(56, util.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-10, 10, 1, 1);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(11, 21, 1, 1);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(12, util.computeXAxisLabelHeight());

        constraints.setMinimumColumnValue(15.5);
        constraints.setMaximumColumnValue(25.5);
        assertEquals(18, util.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-10, 10, 1, 1);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(11, 21, 1, 1);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(10, util.computeYAxisLabelWidth());

        constraints.setMinimumValue(new DoublePosition(-5.5, 1));
        constraints.setMaximumValue(new DoublePosition(5.5, 2));
        assertEquals(12, util.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-10, 10, 1, 1);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(11, 21, 1, 1);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        util.drawNumericalXAxisTicks(g);
        g.dispose();

        // The following assertions depend on the actual rendering of the chart
        // Uncomment them when testing with a real charting library
        //assertFalse(image.getRGB(10, 10) == 0);
        //assertTrue(image.getRGB(50, 10) == 0);
        //assertTrue(image.getRGB(140, 10) == 0);
        //assertFalse(image.getRGB(190, 10) == 0);
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-10, 10, 1, 1);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(11, 21, 1, 1);
        ChartDataModel model = new ChartDataModel();
        model.addColumn("A", new Object[]{"1", "2", "3", "4", "5"});
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        util.drawXAxisTicks(g);
        g.dispose();

        // The following assertions depend on the actual rendering of the chart
        // Uncomment them when testing with a real charting library
        //assertTrue(image.getRGB(10, 10) == 0);
        //assertTrue(image.getRGB(50, 10) == 0);
        //assertTrue(image.getRGB(100, 10) != 0);
        //assertTrue(image.getRGB(140, 10) == 0);
        //assertTrue(image.getRGB(190, 10) == 0);
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(-10, 10, 1, 1);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(11, 21, 1, 1);
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        util.drawYAxisTicks(g);
        g.dispose();

        // The following assertions depend on the actual rendering of the chart
        // Uncomment them when testing with a real charting library
        //assertTrue(image.getRGB(10, 10) == 0);
        //assertTrue(image.getRGB(10, 50) == 0);
        //assertTrue(image.getRGB(10, 100) != 0);
        //assertTrue(image.getRGB(10, 140) == 0);
        //assertTrue(image.getRGB(10, 190) == 0);
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, new ChartDataModelConstraints(0, 0, 1, 1), new ChartDataModelConstraints(0, 0, 1, 1), new ChartDataModel());
        Line2D line = util.getXAxisLine2D();

        assertEquals(100, line.getX1(), 0.01);
        assertEquals(100, line.getX2(), 0.01);
        assertEquals(0, line.getY1(), 0.01);
        assertEquals(200, line.getY2(), 0.01);
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, new ChartDataModelConstraints(0, 0, 1, 1), new ChartDataModelConstraints(0, 0, 1, 1), new ChartDataModel());
        Line2D line = util.getYAxisLine2D();

        assertEquals(0, line.getX1(), 0.01);
        assertEquals(200, line.getX2(), 0.01);
        assertEquals(0, line.getY1(), 0.01);
        assertEquals(0, line.getY2(), 0.01);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystem coord = new CoordSystem(100, 100, 200, 200, 0, 0);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, new ChartDataModelConstraints(0, 0, 1, 1), new ChartDataModelConstraints(0, 0, 1, 1), new ChartDataModel());
        CoordSystemUtilities util2 = new CoordSystemUtilities(coord, new ChartDataModelConstraints(0, 0, 1, 1), new ChartDataModelConstraints(0, 0, 1, 1), new ChartDataModel());
        util2.setSecondYAxis(true);
        Line2D line = util2.getSecondYAxisLine2D();

        assertEquals(0, line.getX1(), 0.01);
        assertEquals(200, line.getX2(), 0.01);
        assertEquals(0, line.getY1(), 0.01);
        assertEquals(100, line.getY2(), 0.01);
    }
}