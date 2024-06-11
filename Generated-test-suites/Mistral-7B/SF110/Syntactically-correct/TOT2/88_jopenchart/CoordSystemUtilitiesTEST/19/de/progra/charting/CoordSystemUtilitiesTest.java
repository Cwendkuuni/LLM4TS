package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utils;
    private ChartDataModel model;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private CoordSystem coord;

    @Before
    public void setUp() {
        coord = new CoordSystem(100, 100, 10, 10);
        NumberFormat xFormat = new DecimalFormat("#,##0.0");
        NumberFormat yFormat = new DecimalFormat("#,##0.0");
        model = new ChartDataModel(new Object[][] {{ "A", xFormat.format(1), yFormat.format(1) }, { "B", xFormat.format(2), yFormat.format(2) }, { "C", xFormat.format(3), yFormat.format(3) } });
        constraints = new ChartDataModelConstraints(model, null, null, null);
        constraints2 = new ChartDataModelConstraints(null, null, null, model);
    }

    @Test
    public void testComputeLeftMargin() {
        utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int actual = utils.computeLeftMargin();
        int expected = 20 + 15 + (int)Math.ceil((1.0 / (1.0 + 2.0)) * (coord.getBounds().getWidth() - coord.getRightMargin()));
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRightMargin() {
        utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int actual = utils.computeRightMargin();
        int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeTopMargin() {
        utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int actual = utils.computeTopMargin();
        int expected = 15 + 15 / 3 + 15 / 3 + 15 / 3;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeBottomMargin() {
        utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int actual = utils.computeBottomMargin();
        int expected = 20 + 15 + (int)Math.ceil((1.0 / (1.0 + 1.0)) * (coord.getBounds().getHeight() - coord.getTopMargin()));
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int actual = utils.computeXAxisLabelHeight();
        int expected = 1 + (int)(Math.log10(3) + 1) * 10;
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int actual = utils.computeYAxisLabelWidth();
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeLeftMarginWithNullConstraints() {
        utils = new CoordSystemUtilities(coord, null, constraints2, model);
        utils.computeLeftMargin();
    }

    @Test(expected = NullPointerException.class)
    public void testComputeLeftMarginWithNullModel() {
        utils = new CoordSystemUtilities(coord, constraints, null, null);
        utils.computeLeftMargin();
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        Graphics2D g = Mockito.mock(Graphics2D.class);
        utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        utils.drawNumericalXAxisTicks(g);
        // Add assertions based on the expected behavior
    }

    // Add other test methods for other methods in the CoordSystemUtilities class
}