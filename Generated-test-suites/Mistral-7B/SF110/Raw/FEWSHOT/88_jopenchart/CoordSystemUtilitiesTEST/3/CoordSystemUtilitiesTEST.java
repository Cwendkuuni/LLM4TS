package de.progra.charting.test;

import static org.junit.Assert.*;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.ChartDataModelConstraints;
import de.progra.charting.model.CoordSystem;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Arrays;

import org.junit.Test;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem(new ChartDataModel(), 100, 100, 150, 150);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[]{-1.0, 1.0}), new DecimalFormat("#.##"));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[]{1.0, 2.0}), new DecimalFormat("#.##"));
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(20, util.computeLeftMargin());
        constraints.setMinimumColumnValue(0.5);
        constraints.setMaximumColumnValue(2.5);
        assertEquals(167, util.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem(new ChartDataModel(), 100, 100, 150, 150);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[]{0.0, 1.0}), new DecimalFormat("#.##"));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[]{1.0, 2.0}), new DecimalFormat("#.##"));
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(15, util.computeRightMargin());
        constraints.setMinimumColumnValue(-1.5);
        constraints.setMaximumColumnValue(-0.5);
        assertEquals(65, util.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem(new ChartDataModel(), 100, 100, 150, 150);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[]{-1.0, 1.0}), new DecimalFormat("#.##"));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[]{1.0, 2.0}), new DecimalFormat("#.##"));
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(15, util.computeTopMargin());
        constraints.setMinimumValue(new Double(-2.0));
        constraints.setMaximumValue(new Double(2.0));
        assertEquals(56, util.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem(new ChartDataModel(), 100, 100, 150, 150);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[]{-1.0, 1.0}), new DecimalFormat("#.##"));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[]{1.0, 2.0}), new DecimalFormat("#.##"));
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(20, util.computeBottomMargin());
        constraints.setMinimumValue(new Double(-2.0));
        constraints.setMaximumValue(new Double(2.0));
        assertEquals(136, util.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelWidth() {
        CoordSystem coord = new CoordSystem(new ChartDataModel(), 100, 100, 150, 150);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[]{-1.0, 1.0}), new DecimalFormat("#.##"));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[]{1.0, 2.0}), new DecimalFormat("#.##"));
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(11, util.computeXAxisLabelWidth());
        constraints.setMinimumColumnValue(0.5);
        constraints.setMaximumColumnValue(2.5);
        assertEquals(34, util.computeXAxisLabelWidth());
    }

    @Test
    public void testComputeYAxisLabelHeight() {
        CoordSystem coord = new CoordSystem(new ChartDataModel(), 100, 100, 150, 150);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[]{-1.0, 1.0}), new DecimalFormat("#.##"));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[]{1.0, 2.0}), new DecimalFormat("#.##"));
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        assertEquals(11, util.computeYAxisLabelHeight());
        constraints.setMinimumValue(new Double(-2.0));
        constraints.setMaximumValue(new Double(2.0));
        assertEquals(22, util.computeYAxisLabelHeight());
    }

    @Test
    public void testDrawXAxisTicks() throws Exception {
        CoordSystem coord = new CoordSystem(new ChartDataModel(), 100, 100, 150, 150);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[]{-1.0, 1.0}), new DecimalFormat("#.##"));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[]{1.0, 2.0}), new DecimalFormat("#.##"));
        ChartDataModel model = new ChartDataModel();
        model.addSeries("Series1", Arrays.asList(new Number[]{0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}));
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        Graphics2D g = (Graphics2D) new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawXAxisTicks(g);

        double[] expectedXValues = {-0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0};
        for (int i = 0; i < expectedXValues.length; i++) {
            String expectedLabel = String.valueOf(expectedXValues[i]);
            Point2D expectedPoint = new Point2D.Double(expectedXValues[i], 0.0);
            Point2D actualPoint = g.getTransform()
                    .transformPoint(expectedPoint, null);
            String actualLabel = g.getFontMetrics().getStringBounds(expectedLabel, g).getBounds().getCenterX() > 0 ? g.getFont().getSize2D().width > 5 ? g.getFont().getSize2D().width * 2 : 11 : 6;
            assertEquals(expectedLabel, g.getFont().getStringBounds(g.getFont().getSize2D() > 5 ? expectedLabel : "", g).getBounds().getCenterX() + actualLabel, actualPoint.getX(), 0.1);
        }
    }

    @Test
    public void testDrawYAxisTicks() throws Exception {
        CoordSystem coord = new CoordSystem(new ChartDataModel(), 100, 100, 150, 150);
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(Arrays.asList(new Double[]{-1.0, 1.0}), new DecimalFormat("#.##"));
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[]{1.0, 2.0}), new DecimalFormat("#.##"));
        ChartDataModel model = new ChartDataModel();
        model.addSeries("Series1", Arrays.asList(new Number[]{0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}));
        CoordSystemUtilities util = new CoordSystemUtilities(coord, constraints, constraints2, model);

        Graphics2D g = (Graphics2D) new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB).getGraphics();
        util.drawYAxisTicks(g);

        double[] expectedYValues = {-1.0, -0.5, 0.0, 0.5, 1.0};
        for (int i = 0; i < expectedYValues.length; i++) {
            String expectedLabel = String.valueOf(expectedYValues[i]);
            double expectedY = i * 10 + 5;
            Point2D expectedPoint = new Point2D.Double(0.0, expectedY);
            Point2D actualPoint = g.getTransform()
                    .transformPoint(expectedPoint, null);
            assertEquals(expectedLabel, g.getFont().getStringBounds(expectedLabel, g).getBounds().getCenterY() > 0 ? g.getFont().getSize2D().height * 2 : 11, actualPoint.getY(), 0.1);
        }
    }
}