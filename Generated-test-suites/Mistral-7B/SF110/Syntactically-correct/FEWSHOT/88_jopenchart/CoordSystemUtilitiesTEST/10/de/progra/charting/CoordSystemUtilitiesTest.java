package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;

import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemConstraints;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.ChartDataModelConstraints;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(10.0, 50.0);
        ChartDataModel model = new ChartDataModel();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(20.0, 50.0);

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        double xmin = 5.0;
        double xmax = 15.0;
        assertEquals(29, util.computeLeftMargin());

        xmin = -10.0;
        xmax = 10.0;
        assertEquals(31, util.computeLeftMargin());

        xmin = -10.0;
        xmax = 20.0;
        assertEquals(42, util.computeLeftMargin());

        xmin = 20.0;
        xmax = 30.0;
        assertEquals(20, util.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(10.0, 50.0);

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        assertEquals(33, util.computeRightMargin());

        coord = new CoordSystem(100, 100, 50, 50);
        constraints = new ChartDataModelConstraints(20.0, 50.0);

        util = new CoordSystemUtilities(coord, constraints, null, null);

        assertEquals(21, util.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(10.0, 50.0);

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        assertEquals(31, util.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(new Double[] {-5.0, 5.0}, new Double[] {-5.0, 5.0});

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        assertEquals(31, util.computeBottomMargin());

        coord = new CoordSystem(100, 100, 50, 50);
        constraints = new ChartDataModelConstraints(new Double[] {-5.0, 5.0}, new Double[] {-10.0, 10.0});

        util = new CoordSystemUtilities(coord, constraints, null, null);

        assertEquals(42, util.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        assertEquals(12, util.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        assertEquals(12, util.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Test with default values
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        Graphics2D g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawNumericalXAxisTicks(g);
        g.dispose();

        // Test with custom paint labels flag
        coord = new CoordSystem(100, 100, 50, 50);
        constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});
        model = new ChartDataModel();

        util = new CoordSystemUtilities(coord, constraints, null, model);
        util.setPaintLabels(false);

        g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawNumericalXAxisTicks(g);
        g.dispose();

        // Test with custom x decimal format
        coord = new CoordSystem(100, 100, 50, 50);
        constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});
        model = new ChartDataModel();
        DecimalFormat df = new DecimalFormat("0.00");

        util = new CoordSystemUtilities(coord, constraints, null, model);
        util.setXDecimalFormat(df);

        g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawNumericalXAxisTicks(g);
        g.dispose();
    }

    @Test
    public void testDrawXAxisTicks() {
        // Test with default values
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        Graphics2D g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawXAxisTicks(g);
        g.dispose();

        // Test with custom paint labels flag
        coord = new CoordSystem(100, 100, 50, 50);
        constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});
        model = new ChartDataModel();

        util = new CoordSystemUtilities(coord, constraints, null, model);
        util.setPaintLabels(false);

        g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawXAxisTicks(g);
        g.dispose();

        // Test with custom x decimal format
        coord = new CoordSystem(100, 100, 50, 50);
        constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});
        model = new ChartDataModel();
        DecimalFormat df = new DecimalFormat("0.00");

        util = new CoordSystemUtilities(coord, constraints, null, model);
        util.setXDecimalFormat(df);

        g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawXAxisTicks(g);
        g.dispose();
    }

    @Test
    public void testDrawYAxisTicks() {
        // Test with default values
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});
        ChartDataModel model = new ChartDataModel();

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, model);

        Graphics2D g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawYAxisTicks(g);
        g.dispose();

        // Test with custom paint labels flag
        coord = new CoordSystem(100, 100, 50, 50);
        constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});
        model = new ChartDataModel();

        util = new CoordSystemUtilities(coord, constraints, null, model);
        util.setPaintLabels(false);

        g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawYAxisTicks(g);
        g.dispose();

        // Test with custom y decimal format
        coord = new CoordSystem(100, 100, 50, 50);
        constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});
        model = new ChartDataModel();
        DecimalFormat df = new DecimalFormat("0.00");

        util = new CoordSystemUtilities(coord, constraints, null, model);
        util.setYDecimalFormat(df);

        g = (Graphics2D)new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawYAxisTicks(g);
        g.dispose();
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        Line2D line = util.getXAxisLine2D();

        Point2D p1 = new Point2D.Double(0.0, 0.0);
        Point2D p2 = new Point2D.Double(10.0, 0.0);

        assertEquals(p1.getX(), line.getX1(), 0.01);
        assertEquals(p1.getY(), line.getY1(), 0.01);
        assertEquals(p2.getX(), line.getX2(), 0.01);
        assertEquals(p2.getY(), line.getY2(), 0.01);
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);

        Line2D line = util.getYAxisLine2D();

        Point2D p1 = new Point2D.Double(0.0, 0.0);
        Point2D p2 = new Point2D.Double(0.0, 10.0);

        assertEquals(p1.getX(), line.getX1(), 0.01);
        assertEquals(p1.getY(), line.getY1(), 0.01);
        assertEquals(p2.getX(), line.getX2(), 0.01);
        assertEquals(p2.getY(), line.getY2(), 0.01);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystem coord = new CoordSystem(100, 100, 50, 50);
        CoordSystemConstraints constraints = new ChartDataModelConstraints(new Double[] {0.0, 10.0}, new Double[] {0.0, 10.0});
        CoordSystemConstraints constraints2 = new ChartDataModelConstraints(new Double[] {10.0, 20.0}, new Double[] {0.0, 10.0});

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, null);

        Line2D line = util.getSecondYAxisLine2D();

        Point2D p1 = new Point2D.Double(10.0, 0.0);
        Point2D p2 = new Point2D.Double(10.0, 10.0);

        assertEquals(p1.getX(), line.getX1(), 0.01);
        assertEquals(p1.getY(), line.getY1(), 0.01);
        assertEquals(p2.getX(), line.getX2(), 0.01);
        assertEquals(p2.getY(), line.getY2(), 0.01);
    }
}