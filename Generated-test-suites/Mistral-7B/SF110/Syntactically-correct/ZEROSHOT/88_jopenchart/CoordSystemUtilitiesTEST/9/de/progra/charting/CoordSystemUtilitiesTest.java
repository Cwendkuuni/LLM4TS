package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities utilities;
    private CoordSystem coordSystem;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;

    @Before
    public void setup() {
        final DecimalFormat xFormat = new DecimalFormat("#.##");
        final DecimalFormat yFormat = new DecimalFormat("#.##");
        coordSystem = new CoordSystem(new Rectangle2D.Double(0, 0, 500, 500), xFormat, yFormat, new Font("Arial", Font.BOLD, 12), 20, 20, null);
        constraints = new ChartDataModelConstraints(new DoubleRange(Double.MIN_VALUE, Double.MAX_VALUE, 0.1), new DoubleRange(Double.MIN_VALUE, Double.MAX_VALUE, 0.1));
        constraints2 = new ChartDataModelConstraints(new DoubleRange(Double.MAX_VALUE, Double.MIN_VALUE, 0.1), new DoubleRange(Double.MIN_VALUE, Double.MAX_VALUE, 0.1));
        model = new ChartDataModel();
        utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        assertEquals(20, utilities.computeLeftMargin());
        constraints.setMinimumColumnValue(-10);
        constraints.setMaximumColumnValue(10);
        assertEquals(88, utilities.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        assertEquals(15, utilities.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        assertEquals(15, utilities.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        assertEquals(20, utilities.computeBottomMargin());
        constraints.setMinimumValue(new DoubleRange(Double.MIN_VALUE, -10, 0.1));
        constraints.setMaximumValue(new DoubleRange(10, Double.MAX_VALUE, 0.1));
        assertEquals(113, utilities.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        assertEquals(15, utilities.computeXAxisLabelHeight());
        constraints.setMinimumColumnValue(10);
        constraints.setMaximumColumnValue(100);
        assertEquals(45, utilities.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        assertEquals(15, utilities.computeYAxisLabelWidth());
        constraints.setMinimumValue(new DoubleRange(-10, 0, 0.1));
        constraints.setMaximumValue(new DoubleRange(0, 10, 0.1));
        assertEquals(45, utilities.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        final Graphics2D g = new Graphics2D(new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB));
        utilities.drawNumericalXAxisTicks(g);
        // Check number of drawn ticks
        // Check positions and labels of drawn ticks
    }

    @Test
    public void testDrawXAxisTicks() {
        final Graphics2D g = new Graphics2D(new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB));
        utilities.drawXAxisTicks(g);
        // Check number of drawn ticks
        // Check positions of drawn ticks
        // Check labels of drawn ticks
    }

    @Test
    public void testDrawYAxisTicks() {
        final Graphics2D g = new Graphics2D(new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB));
        utilities.drawYAxisTicks(g);
        // Check number of drawn ticks
        // Check positions and labels of drawn ticks
    }

    @Test
    public void testGetXAxisLine2D() {
        Line2D line = utilities.getXAxisLine2D();
        assertEquals(0, line.getX1(), 0.01);
        assertEquals(utilities.coordSystem.getBounds().getHeight(), line.getY1(), 0.01);
        assertEquals(500, line.getX2(), 0.01);
        assertEquals(utilities.coordSystem.getBounds().getHeight(), line.getY2(), 0.01);
    }

    @Test
    public void testGetYAxisLine2D() {
        Line2D line = utilities.getYAxisLine2D();
        assertEquals(utilities.coordSystem.getBounds().getX(), line.getX1(), 0.01);
        assertEquals(0, line.getY1(), 0.01);
        assertEquals(utilities.coordSystem.getBounds().getX(), line.getX2(), 0.01);
        assertEquals(500, line.getY2(), 0.01);
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D line = utilities.getSecondYAxisLine2D();
        assertEquals(500, line.getX1(), 0.01);
        assertEquals(0, line.getY1(), 0.01);
        assertEquals(500, line.getX2(), 0.01);
        assertEquals(500, line.getY2(), 0.01);
    }
}