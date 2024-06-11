package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;
import java.util.*;

import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new Double[]{0.0, 10.0}, new Double[]{0.0, 5.0}, null);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(100.0, 100.0, 600.0, 400.0), new Line2D.Double(100.0, 100.0, 700.0, 100.0), new Line2D.Double(100.0, 100.0, 100.0, 500.0), null, null, null);

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints, new ChartDataModel());

        int testedValue = util.computeLeftMargin();
        int expectedValue = 32; // (computeYAxisLabelWidth() + 6 + layout.getBounds().getWidth() + 6) + 5 + (int)(maxlmargin - xmin / (xmin + xmax) * (c.getBounds().getWidth() - c.getRightMargin()))

        assertEquals(expectedValue, testedValue);
    }

    @Test
    public void testComputeRightMargin() {
        TextAttribute attr = new TextAttribute();
        attr.setFont(new Font("Arial", Font.BOLD, 12));

        TextLayout layout = new TextLayout("X Axis", attr, new FontRenderContext());

        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new Double[]{0.0, 10.0}, new Double[]{0.0, 5.0}, null);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(100.0, 100.0, 600.0, 400.0), new Line2D.Double(100.0, 100.0, 700.0, 100.0), new Line2D.Double(100.0, 100.0, 100.0, 500.0), null, null, null);

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints, new ChartDataModel());

        int testedValue = util.computeRightMargin();
        int expectedValue = 45; // (int)(width + 15.0 / 3.0);

        assertEquals(expectedValue, testedValue);
    }

    @Test
    public void testComputeTopMargin() {
        TextAttribute attr = new TextAttribute();
        attr.setFont(new Font("Arial", Font.BOLD, 12));

        TextLayout layout = new TextLayout("Y Axis", attr, new FontRenderContext());

        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new Double[]{0.0, 10.0}, new Double[]{0.0, 5.0}, null);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(100.0, 100.0, 600.0, 400.0), new Line2D.Double(100.0, 100.0, 700.0, 100.0), new Line2D.Double(100.0, 100.0, 100.0, 500.0), null, null, null);

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints, new ChartDataModel());

        int testedValue = util.computeTopMargin();
        int expectedValue = 48; // (int)(height + 15.0 / 3.0 + layout.getDescent());

        assertEquals(expectedValue, testedValue);
    }

    @Test
    public void testComputeBottomMargin() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new Double[]{0.0, 10.0}, new Double[]{0.0, 5.0}, null);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(100.0, 100.0, 600.0, 400.0), new Line2D.Double(100.0, 100.0, 700.0, 100.0), new Line2D.Double(100.0, 100.0, 100.0, 500.0), null, null, null);

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints, new ChartDataModel());

        int testedValue = util.computeBottomMargin();
        int expectedValue = 20;

        assertEquals(expectedValue, testedValue);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new Double[]{0.0, 10.0}, new Double[]{0.0, 5.0}, null);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(100.0, 100.0, 600.0, 400.0), new Line2D.Double(100.0, 100.0, 700.0, 100.0), new Line2D.Double(100.0, 100.0, 100.0, 500.0), null, null, null);

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints, new ChartDataModel());

        int testedValue = util.computeXAxisLabelHeight();
        int expectedValue = 18;

        assertEquals(expectedValue, testedValue);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new Double[]{0.0, 10.0}, new Double[]{0.0, 5.0}, null);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(100.0, 100.0, 600.0, 400.0), new Line2D.Double(100.0, 100.0, 700.0, 100.0), new Line2D.Double(100.0, 100.0, 100.0, 500.0), null, null, null);

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints, new ChartDataModel());

        int testedValue = util.computeYAxisLabelWidth();
        int expectedValue = 50;

        assertEquals(expectedValue, testedValue);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new Double[]{0.0, 10.0}, new Double[]{0.0, 5.0}, null);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(100.0, 100.0, 600.0, 400.0), new Line2D.Double(100.0, 100.0, 700.0, 100.0), new Line2D.Double(100.0, 100.0, 100.0, 500.0), null, null, null);
        Graphics2D g = new Graphics2D(new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB));

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints, new ChartDataModel());

        util.drawNumericalXAxisTicks(g);

        // Add assertions here for the drawn ticks and labels
    }

    @Test
    public void testDrawXAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new Double[]{0.0, 10.0}, new Double[]{0.0, 5.0}, null);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(100.0, 100.0, 600.0, 400.0), new Line2D.Double(100.0, 100.0, 700.0, 100.0), new Line2D.Double(100.0, 100.0, 100.0, 500.0), null, null, null);
        Graphics2D g = new Graphics2D(new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB));

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints, new ChartDataModel());

        util.drawXAxisTicks(g);

        // Add assertions here for the drawn ticks and labels
    }

    @Test
    public void testDrawYAxisTicks() {
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(new Double[]{0.0, 10.0}, new Double[]{0.0, 5.0}, null);
        CoordSystem coord = new CoordSystem(new Rectangle2D.Double(100.0, 100.0, 600.0, 400.0), new Line2D.Double(100.0, 100.0, 700.0, 100.0), new Line2D.Double(100.0, 100.0, 100.0, 500.0), null, null, null);
        Graphics2D g = new Graphics2D(new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB));

        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints, new ChartDataModel());

        util.drawYAxisTicks(g);

        // Add assertions here for the drawn ticks and labels
    }
}