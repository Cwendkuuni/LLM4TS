package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.DecimalFormat;

import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new Dimension(500, 400), new Dimension(20, 20), null, null),
                new ChartDataModelConstraints(new DoubleRange(0.0, 100.0), 1.0),
                new ChartDataModelConstraints(new DoubleRange(-50.0, 50.0), 1.0),
                new ChartDataModel() {
                    @Override
                    public Object getValueAt(int row, int column) {
                        return null;
                    }

                    @Override
                    public String getColumnName(int column) {
                        return "Column " + column;
                    }

                    @Override
                    public int getRowCount() {
                        return 10;
                    }

                    @Override
                    public int getColumnCount() {
                        return 2;
                    }
                });

        assertEquals(166, utils.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new Dimension(500, 400), new Dimension(20, 20), null, null),
                new ChartDataModelConstraints(new DoubleRange(0.0, 100.0), 1.0),
                new ChartDataModelConstraints(new DoubleRange(-50.0, 50.0), 1.0),
                new ChartDataModel() {
                    @Override
                    public Object getValueAt(int row, int column) {
                        return null;
                    }

                    @Override
                    public String getColumnName(int column) {
                        return "Column " + column;
                    }

                    @Override
                    public int getRowCount() {
                        return 10;
                    }

                    @Override
                    public int getColumnCount() {
                        return 2;
                    }
                });

        assertEquals(45, utils.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new Dimension(500, 400), new Dimension(20, 20), null, null),
                new ChartDataModelConstraints(new DoubleRange(0.0, 100.0), 1.0),
                new ChartDataModelConstraints(new DoubleRange(-50.0, 50.0), 1.0),
                new ChartDataModel() {
                    @Override
                    public Object getValueAt(int row, int column) {
                        return null;
                    }

                    @Override
                    public String getColumnName(int column) {
                        return "Column " + column;
                    }

                    @Override
                    public int getRowCount() {
                        return 10;
                    }

                    @Override
                    public int getColumnCount() {
                        return 2;
                    }
                });

        assertEquals(15, utils.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new Dimension(500, 400), new Dimension(20, 20), null, null),
                new ChartDataModelConstraints(new DoubleRange(0.0, 100.0), 1.0),
                new ChartDataModelConstraints(new DoubleRange(-50.0, 50.0), 1.0),
                new ChartDataModel() {
                    @Override
                    public Object getValueAt(int row, int column) {
                        return null;
                    }

                    @Override
                    public String getColumnName(int column) {
                        return "Column " + column;
                    }

                    @Override
                    public int getRowCount() {
                        return 10;
                    }

                    @Override
                    public int getColumnCount() {
                        return 2;
                    }
                });

        assertEquals(15, utils.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new Dimension(500, 400), new Dimension(20, 20), null, null),
                new ChartDataModelConstraints(new DoubleRange(0.0, 100.0), 1.0),
                new ChartDataModelConstraints(new DoubleRange(-50.0, 50.0), 1.0),
                new ChartDataModel() {
                    @Override
                    public Object getValueAt(int row, int column) {
                        return null;
                    }

                    @Override
                    public String getColumnName(int column) {
                        return "Column " + column;
                    }

                    @Override
                    public int getRowCount() {
                        return 10;
                    }

                    @Override
                    public int getColumnCount() {
                        return 2;
                    }
                });

        DecimalFormat df = new DecimalFormat("#.##");

        assertEquals(22, utils.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new Dimension(500, 400), new Dimension(20, 20), null, null),
                new ChartDataModelConstraints(new DoubleRange(0.0, 100.0), 1.0),
                new ChartDataModelConstraints(new DoubleRange(-50.0, 50.0), 1.0),
                new ChartDataModel() {
                    @Override
                    public Object getValueAt(int row, int column) {
                        return null;
                    }

                    @Override
                    public String getColumnName(int column) {
                        return "Column " + column;
                    }

                    @Override
                    public int getRowCount() {
                        return 10;
                    }

                    @Override
                    public int getColumnCount() {
                        return 2;
                    }
                });

        DecimalFormat df = new DecimalFormat("#.##");

        assertEquals(38, utils.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() throws Exception {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new Dimension(500, 400), new Dimension(20, 20), null, null),
                new ChartDataModelConstraints(new DoubleRange(0.0, 100.0), 1.0),
                new ChartDataModelConstraints(new DoubleRange(-50.0, 50.0), 1.0),
                new ChartDataModel() {
                    @Override
                    public Object getValueAt(int row, int column) {
                        return null;
                    }

                    @Override
                    public String getColumnName(int column) {
                        return "Column " + column;
                    }

                    @Override
                    public int getRowCount() {
                        return 10;
                    }

                    @Override
                    public int getColumnCount() {
                        return 2;
                    }
                });

        Graphics2D g = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB).createGraphics();

        utils.drawNumericalXAxisTicks(g);

        g.dispose();
    }

    @Test
    public void testDrawXAxisTicks() throws Exception {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new Dimension(500, 400), new Dimension(20, 20), null, null),
                new ChartDataModelConstraints(new DoubleRange(0.0, 100.0), 1.0),
                new ChartDataModelConstraints(new DoubleRange(-50.0, 50.0), 1.0),
                new ChartDataModel() {
                    @Override
                    public Object getValueAt(int row, int column) {
                        return null;
                    }

                    @Override
                    public String getColumnName(int column) {
                        return "Column " + column;
                    }

                    @Override
                    public int getRowCount() {
                        return 10;
                    }

                    @Override
                    public int getColumnCount() {
                        return 2;
                    }
                });

        Graphics2D g = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB).createGraphics();

        utils.drawXAxisTicks(g);

        g.dispose();
    }

    @Test
    public void testDrawYAxisTicks() throws Exception {
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(new Dimension(500, 400), new Dimension(20, 20), null, null),
                new ChartDataModelConstraints(new DoubleRange(0.0, 100.0), 1.0),
                new ChartDataModelConstraints(new DoubleRange(-50.0, 50.0), 1.0),
                new ChartDataModel() {
                    @Override
                    public Object getValueAt(int row, int column) {
                        return null;
                    }

                    @Override
                    public String getColumnName(int column) {
                        return "Column " + column;
                    }

                    @Override
                    public int getRowCount() {
                        return 10;
                    }

                    @Override
                    public int getColumnCount() {
                        return 2;
                    }
                });

        Graphics2D g = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB).createGraphics();

        utils.drawYAxisTicks(g);

        g.dispose();
    }

}