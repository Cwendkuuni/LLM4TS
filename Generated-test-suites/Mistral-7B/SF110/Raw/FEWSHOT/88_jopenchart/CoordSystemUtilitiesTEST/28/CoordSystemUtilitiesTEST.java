package de.progra.charting.test;

import static org.junit.Assert.*;

import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        constraints.setMinimumColumnValue(-10);
        constraints.setMaximumColumnValue(10);
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        int leftMargin = util.computeLeftMargin();
        assertEquals(20, leftMargin);

        constraints.setMinimumColumnValue(0);
        constraints.setMaximumColumnValue(0);
        util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        leftMargin = util.computeLeftMargin();
        assertEquals(20, leftMargin);

        constraints.setMinimumColumnValue(-1);
        constraints.setMaximumColumnValue(1);
        DecimalFormat df = new DecimalFormat("#.##");
        FontRenderContext frc = new FontRenderContext();
        Font font = new Font("Serif", Font.BOLD, 12);
        TextLayout layout = new TextLayout("1.23", font, frc);
        int maxlmargin = util.computeYAxisLabelWidth() + 6;
        int margin = 0;
        if (maxlmargin > leftMargin) {
            margin = maxlmargin;
        }
        margin += 5;
        util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        leftMargin = util.computeLeftMargin();
        assertEquals(margin, leftMargin);

        constraints.setMinimumColumnValue(-100);
        constraints.setMaximumColumnValue(100);
        util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        leftMargin = util.computeLeftMargin();
        assertEquals(20, leftMargin);
    }

    @Test
    public void testComputeRightMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        int rightMargin = util.computeRightMargin();
        assertEquals(15, rightMargin);

        constraints.setMinimumColumnValue(0);
        constraints.setMaximumColumnValue(0);
        util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        rightMargin = util.computeRightMargin();
        assertEquals(15, rightMargin);

        DecimalFormat df = new DecimalFormat("#.##");
        FontRenderContext frc = new FontRenderContext();
        Font font = new Font("Serif", Font.BOLD, 12);
        TextLayout layout = new TextLayout("1.23", font, frc);
        constraints.setMinimumColumnValue(-1);
        constraints.setMaximumColumnValue(1);
        int width = (int)(layout.getBounds().getWidth() + 15.0 / 3.0);
        util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        rightMargin = util.computeRightMargin();
        assertEquals(Math.max(width, 15), rightMargin);
    }

    @Test
    public void testComputeTopMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        int topMargin = util.computeTopMargin();
        assertEquals(15, topMargin);

        TextLayout layout = new TextLayout("1.23", new Font("Serif", Font.BOLD, 12), new FontRenderContext());
        int height = (int)(layout.getBounds().getHeight() + 15.0 / 3.0 + layout.getDescent());
        constraints.setMinimumValue(new Double(-1));
        constraints.setMaximumValue(new Double(1));
        util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        topMargin = util.computeTopMargin();
        assertEquals(Math.max(height, 15), topMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        int bottomMargin = util.computeBottomMargin();
        assertEquals(20, bottomMargin);

        TextLayout layout = new TextLayout("1.23", new Font("Serif", Font.BOLD, 12), new FontRenderContext());
        int maxbmargin = util.computeXAxisLabelHeight() + 6;
        int margin = 0;
        if (maxbmargin > bottomMargin) {
            margin = maxbmargin;
        }
        margin += 10;
        util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        bottomMargin = util.computeBottomMargin();
        assertEquals(margin, bottomMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        int height = util.computeXAxisLabelHeight();
        assertEquals(0, height);

        constraints.setMinimumColumnValue(-1);
        constraints.setMaximumColumnValue(1);
        DecimalFormat df = new DecimalFormat("#.##");
        FontRenderContext frc = new FontRenderContext();
        Font font = new Font("Serif", Font.BOLD, 12);
        double min = -1;
        double max = 1;
        double tick = ChartUtilities.calculateTickSpacing(min, max);
        double ypt = 0;
        boolean paint = false;
        TextLayout label = null;
        for (double d = min; d <= max; d += tick) {
            if (paint) {
                label = new TextLayout(df.format(d), font, frc);
                break;
            }
            paint = !paint;
        }
        int labelHeight = (int)label.getBounds().getHeight();
        util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        height = util.computeXAxisLabelHeight();
        assertEquals(labelHeight, height);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        int width = util.computeYAxisLabelWidth();
        assertEquals(0, width);

        constraints.setMinimumValue(new Double(-1));
        constraints.setMaximumValue(new Double(1));
        DecimalFormat df = new DecimalFormat("#.##");
        FontRenderContext frc = new FontRenderContext();
        Font font = new Font("Serif", Font.BOLD, 12);
        double min = -1;
        double max = 1;
        double tick = ChartUtilities.calculateTickSpacing(min, max);
        double xpt = 0;
        boolean paint = false;
        TextLayout label = null;
        for (double d = min; d <= max; d += tick) {
            if (paint) {
                label = new TextLayout(df.format(d), font, frc);
                break;
            }
            paint = !paint;
        }
        int labelWidth = (int)label.getBounds().getWidth();
        util = new CoordSystemUtilities(new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null), constraints, null, null);
        width = util.computeYAxisLabelWidth();
        assertEquals(labelWidth, width);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        Graphics2D g = (Graphics2D)new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB).createGraphics();
        util.drawNumericalXAxisTicks(g);
        assertEquals(0, g.getColor().getRGB());
        g.dispose();
    }

    @Test
    public void testDrawXAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        model.setSize(10);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), model);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        Graphics2D g = (Graphics2D)new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB).createGraphics();
        util.drawXAxisTicks(g);
        assertEquals(0, g.getColor().getRGB());
        g.dispose();
    }

    @Test
    public void testDrawYAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        model.setSize(10);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), model);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        Graphics2D g = (Graphics2D)new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB).createGraphics();
        util.drawYAxisTicks(g);
        assertEquals(0, g.getColor().getRGB());
        g.dispose();
    }

    @Test
    public void testGetXAxisLine2D() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        Line2D line = util.getXAxisLine2D();
        assertEquals(0, line.getX1());
        assertEquals(0, line.getY1());
        assertEquals(400, line.getX2());
        assertEquals(0, line.getY2());
    }

    @Test
    public void testGetYAxisLine2D() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, null, null);
        Line2D line = util.getYAxisLine2D();
        assertEquals(0, line.getX1());
        assertEquals(0, line.getY1());
        assertEquals(0, line.getX2());
        assertEquals(400, line.getY2());
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        constraints2.setMinimumColumnValue(1);
        constraints2.setMaximumColumnValue(1);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), null);
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, null);
        Line2D line = util.getSecondYAxisLine2D();
        assertEquals(400, line.getX1());
        assertEquals(0, line.getY1());
        assertEquals(400, line.getX2());
        assertEquals(400, line.getY2());
    }

}