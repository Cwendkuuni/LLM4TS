package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartDataModel;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;
 private CoordSystem coord;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 CoordSystem c = new CoordSystem(new java.awt.geom.Path2D.Double(), new java.awt.Font("Serif", java.awt.Font.PLAIN, 12), 5, 5);
 this.model = new ChartDataModel();
 this.constraints = new ChartDataModelConstraints();
 this.constraints2 = new ChartDataModelConstraints();
 this.utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with positive xmin and xmax
 this.constraints.setMinimumColumnValue(1.0);
 this.constraints.setMaximumColumnValue(10.0);
 int leftMargin = utils.computeLeftMargin();
 assertEquals(25, leftMargin);

 // Test with negative xmin and xmax
 this.constraints.setMinimumColumnValue(-1.0);
 this.constraints.setMaximumColumnValue(-10.0);
 leftMargin = utils.computeLeftMargin();
 assertEquals(25, leftMargin);

 // Test with zero xmin
 this.constraints.setMinimumColumnValue(0.0);
 this.constraints.setMaximumColumnValue(10.0);
 leftMargin = utils.computeLeftMargin();
 assertEquals(17, leftMargin);

 // Test with zero xmax
 this.constraints.setMinimumColumnValue(1.0);
 this.constraints.setMaximumColumnValue(0.0);
 leftMargin = utils.computeLeftMargin();
 assertEquals(17, leftMargin);
 }

 @Test
 public void testComputeRightMargin() {
 // Test with positive xmin and xmax
 this.constraints.setMinimumColumnValue(1.0);
 this.constraints.setMaximumColumnValue(10.0);
 int rightMargin = utils.computeRightMargin();
 assertEquals(15, rightMargin);

 // Test with negative xmin and xmax
 this.constraints.setMinimumColumnValue(-1.0);
 this.constraints.setMaximumColumnValue(-10.0);
 rightMargin = utils.computeRightMargin();
 assertEquals(15, rightMargin);
 }

 @Test
 public void testComputeTopMargin() {
 // Test with positive ymin and ymax
 this.constraints.setMinimumValue(new Double(1.0));
 this.constraints.setMaximumValue(new Double(10.0));
 int topMargin = utils.computeTopMargin();
 assertEquals(15, topMargin);

 // Test with negative ymin and ymax
 this.constraints.setMinimumValue(new Double(-1.0));
 this.constraints.setMaximumValue(new Double(-10.0));
 topMargin = utils.computeTopMargin();
 assertEquals(15, topMargin);

 // Test with zero ymin
 this.constraints.setMinimumValue(new Double(0.0));
 this.constraints.setMaximumValue(new Double(10.0));
 topMargin = utils.computeTopMargin();
 assertEquals(15, topMargin);

 // Test with zero ymax
 this.constraints.setMinimumValue(new Double(1.0));
 this.constraints.setMaximumValue(new Double(0.0));
 topMargin = utils.computeTopMargin();
 assertEquals(15, topMargin);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test with positive ymin and ymax
 this.constraints.setMinimumValue(new Double(1.0));
 this.constraints.setMaximumValue(new Double(10.0));
 int bottomMargin = utils.computeBottomMargin();
 assertEquals(20, bottomMargin);

 // Test with negative ymin and ymax
 this.constraints.setMinimumValue(new Double(-1.0));
 this.constraints.setMaximumValue(new Double(-10.0));
 bottomMargin = utils.computeBottomMargin();
 assertEquals(20, bottomMargin);

 // Test with zero ymin
 this.constraints.setMinimumValue(new Double(0.0));
 this.constraints.setMaximumValue(new Double(10.0));
 bottomMargin = utils.computeBottomMargin();
 assertEquals(20, bottomMargin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test with positive xmin and xmax
 this.constraints.setMinimumColumnValue(1.0);
 this.constraints.setMaximumColumnValue(10.0);
 int height = utils.computeXAxisLabelHeight();
 assertEquals(21, height);

 // Test with negative xmin and xmax
 this.constraints.setMinimumColumnValue(-1.0);
 this.constraints.setMaximumColumnValue(-10.0);
 height = utils.computeXAxisLabelHeight();
 assertEquals(21, height);

 // Test with zero xmin
 this.constraints.setMinimumColumnValue(0.0);
 this.constraints.setMaximumColumnValue(10.0);
 height = utils.computeXAxisLabelHeight();
 assertEquals(11, height);

 // Test with zero xmax
 this.constraints.setMinimumColumnValue(1.0);
 this.constraints.setMaximumColumnValue(0.0);
 height = utils.computeXAxisLabelHeight();
 assertEquals(11, height);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test with positive ymin and ymax
 this.constraints.setMinimumValue(new Double(1.0));
 this.constraints.setMaximumValue(new Double(10.0));
 int width = utils.computeYAxisLabelWidth();
 assertEquals(5, width);

 // Test with negative ymin and ymax
 this.constraints.setMinimumValue(new Double(-1.0));
 this.constraints.setMaximumValue(new Double(-10.0));
 width = utils.computeYAxisLabelWidth();
 assertEquals(5, width);

 // Test with zero ymin
 this.constraints.setMinimumValue(new Double(0.0));
 this.constraints.setMaximumValue(new Double(10.0));
 width = utils.computeYAxisLabelWidth();
 assertEquals(5, width);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test with empty graphics context
 Graphics2D g = null;
 utils.drawNumericalXAxisTicks(g);

 // Test with non-empty graphics context
 Graphics2D g2 = this.createMockGraphics2D();
 utils.drawNumericalXAxisTicks(g2);
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test with empty graphics context
 Graphics2D g = null;
 utils.drawXAxisTicks(g);

 // Test with non-empty graphics context
 Graphics2D g2 = this.createMockGraphics2D();
 utils.drawXAxisTicks(g2);
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test with empty graphics context
 Graphics2D g = null;
 utils.drawYAxisTicks(g);

 // Test with non-empty graphics context
 Graphics2D g2 = this.createMockGraphics2D();
 utils.drawYAxisTicks(g2);
 }

 @Test
 public void testGetXAxisLine2D() {
 Line2D line = utils.getXAxisLine2D();
 Point2D p1 = new Point2D.Double(0, 0);
 Point2D p2 = new Point2D.Double(1, 0);
 assertEquals(Arrays.asList(p1, p2), line.getPoints());
 }

 @Test
 public void testGetYAxisLine2D() {
 Line2D line = utils.getYAxisLine2D();
 Point2D p1 = new Point2D.Double(0, 0);
 Point2D p2 = new Point2D.Double(0, 1);
 assertEquals(Arrays.asList(p1, p2), line.getPoints());
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 Line2D line = utils.getSecondYAxisLine2D();
 Point2D p1 = new Point2D.Double(0, 0);
 Point2D p2 = new Point2D.Double(0, 1);
 assertEquals(Arrays.asList(p1, p2), line.getPoints());
 }

 private Graphics2D createMockGraphics2D() {
 Graphics2D g = Mockito.mock(Graphics2D.class);
 return g;
 }
}