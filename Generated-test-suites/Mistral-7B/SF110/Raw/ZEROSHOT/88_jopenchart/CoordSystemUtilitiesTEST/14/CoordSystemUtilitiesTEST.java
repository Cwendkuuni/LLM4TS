package de.progra.charting.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.text.DecimalFormat;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontRenderContext;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.ChartDataModelConstraints;
import de.progra.charting.model.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities coordSystemUtilities;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;
 private CoordSystem coordSystem;

 @Before
 public void setUp() {
 constraints = new ChartDataModelConstraints();
 constraints.setMinimumColumnValue(-10.0);
 constraints.setMaximumColumnValue(10.0);
 constraints.setMinimumValue(Double.MIN_VALUE);
 constraints.setMaximumValue(Double.MAX_VALUE);

 constraints2 = new ChartDataModelConstraints();
 constraints2.setMinimumColumnValue(20.0);
 constraints2.setMaximumColumnValue(30.0);
 constraints2.setMinimumValue(Double.MIN_VALUE);
 constraints2.setMaximumValue(Double.MAX_VALUE);

 model = new ChartDataModel();

 coordSystem = new CoordSystem(100, 100, 200, 200);
 coordSystem.setFont(new Font("Arial", Font.PLAIN, 12));
 coordSystem.setFontRenderContext(new FontRenderContext());

 coordSystemUtilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 final int expectedLeftMargin = 65;
 final int actualLeftMargin = coordSystemUtilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);
 }

 @Test
 public void testComputeRightMargin() {
 final int expectedRightMargin = 31;
 final int actualRightMargin = coordSystemUtilities.computeRightMargin();
 assertEquals(expectedRightMargin, actualRightMargin);
 }

 @Test
 public void testComputeTopMargin() {
 final int expectedTopMargin = 31;
 final int actualTopMargin = coordSystemUtilities.computeTopMargin();
 assertEquals(expectedTopMargin, actualTopMargin);
 }

 @Test
 public void testComputeBottomMargin() {
 final int expectedBottomMargin = 65;
 final int actualBottomMargin = coordSystemUtilities.computeBottomMargin();
 assertEquals(expectedBottomMargin, actualBottomMargin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 final int expectedXAxisLabelHeight = 15;
 final int actualXAxisLabelHeight = coordSystemUtilities.computeXAxisLabelHeight();
 assertEquals(expectedXAxisLabelHeight, actualXAxisLabelHeight);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 final int expectedYAxisLabelWidth = 21;
 final int actualYAxisLabelWidth = coordSystemUtilities.computeYAxisLabelWidth();
 assertEquals(expectedYAxisLabelWidth, actualYAxisLabelWidth);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 final Graphics2D graphics = new Graphics2D(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 final DecimalFormat decimalFormat = new DecimalFormat("#.##");
 coordSystemUtilities.drawNumericalXAxisTicks(graphics);
 final Line2D expectedXAxisLine = new Line2D.Double(0.0, 0.0, 10.0, 0.0);
 final Line2D actualXAxisLine = graphics.getClip().getBounds2D().getLine2D();
 assertEquals(expectedXAxisLine, actualXAxisLine);
 }

 @Test
 public void testDrawXAxisTicks() {
 final Graphics2D graphics = new Graphics2D(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 final DecimalFormat decimalFormat = new DecimalFormat("#");
 coordSystemUtilities.drawXAxisTicks(graphics);
 final Line2D expectedXAxisLine = new Line2D.Double(0.0, 0.0, 10.0, 0.0);
 final Line2D actualXAxisLine = graphics.getClip().getBounds2D().getLine2D();
 assertEquals(expectedXAxisLine, actualXAxisLine);
 }

 @Test
 public void testDrawYAxisTicks() {
 final Graphics2D graphics = new Graphics2D(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
 final DecimalFormat decimalFormat = new DecimalFormat("#.##");
 coordSystemUtilities.drawYAxisTicks(graphics);
 final Line2D expectedYAxisLine = new Line2D.Double(0.0, 0.0, 0.0, 10.0);
 final Line2D actualYAxisLine = graphics.getClip().getBounds2D().getLine2D();
 assertEquals(expectedYAxisLine, actualYAxisLine);
 }

 @Test
 public void testGetXAxisLine2D() {
 final Line2D expectedXAxisLine = new Line2D.Double(0.0, 0.0, 10.0, 0.0);
 final Line2D actualXAxisLine = coordSystemUtilities.getXAxisLine2D();
 assertEquals(expectedXAxisLine, actualXAxisLine);
 }

 @Test
 public void testGetYAxisLine2D() {
 final Line2D expectedYAxisLine = new Line2D.Double(0.0, 0.0, 0.0, 10.0);
 final Line2D actualYAxisLine = coordSystemUtilities.getYAxisLine2D();
 assertEquals(expectedYAxisLine, actualYAxisLine);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 final Line2D expectedSecondYAxisLine = new Line2D.Double(0.0, 10.0, 0.0, 0.0);
 final Line2D actualSecondYAxisLine = coordSystemUtilities.getSecondYAxisLine2D();
 assertEquals(expectedSecondYAxisLine, actualSecondYAxisLine);
 }

 @Test
 public void testComputeLeftMarginWithNegativeMinX() {
 final ChartDataModelConstraints newConstraints = new ChartDataModelConstraints();
 newConstraints.setMinimumColumnValue(-10.0);
 newConstraints.setMaximumColumnValue(10.0);
 newConstraints.setMinimumValue(Double.MIN_VALUE);
 newConstraints.setMaximumValue(Double.MAX_VALUE);

 coordSystemUtilities = new CoordSystemUtilities(coordSystem, newConstraints, constraints2, model);
 final int expectedLeftMargin = 111;
 final int actualLeftMargin = coordSystemUtilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);
 }

 @Test
 public void testComputeLeftMarginWithNegativeMaxX() {
 final ChartDataModelConstraints newConstraints = new ChartDataModelConstraints();
 newConstraints.setMinimumColumnValue(-10.0);
 newConstraints.setMaximumColumnValue(10.0);
 newConstraints.setMinimumValue(Double.MIN_VALUE);
 newConstraints.setMaximumValue(Double.MAX_VALUE);

 coordSystemUtilities = new CoordSystemUtilities(coordSystem, constraints, newConstraints, model);
 final int expectedLeftMargin = 111;
 final int actualLeftMargin = coordSystemUtilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);
 }

 @Test
 public void testComputeLeftMarginWithZeroMinX() {
 final ChartDataModelConstraints newConstraints = new ChartDataModelConstraints();
 newConstraints.setMinimumColumnValue(0.0);
 newConstraints.setMaximumColumnValue(10.0);
 newConstraints.setMinimumValue(Double.MIN_VALUE);
 newConstraints.setMaximumValue(Double.MAX_VALUE);

 coordSystemUtilities = new CoordSystemUtilities(coordSystem, newConstraints, constraints2, model);
 final int expectedLeftMargin = 21;
 final int actualLeftMargin = coordSystemUtilities.computeLeftMargin();
 assertNotEquals(expectedLeftMargin, actualLeftMargin);
 }

 @Test
 public void testComputeLeftMarginWithZeroMaxX() {
 final ChartDataModelConstraints newConstraints = new ChartDataModelConstraints();
 newConstraints.setMinimumColumnValue(Double.MIN_VALUE);
 newConstraints.setMaximumColumnValue(0.0);
 newConstraints.setMinimumValue(Double.MIN_VALUE);
 newConstraints.setMaximumValue(Double.MAX_VALUE);

 coordSystemUtilities = new CoordSystemUtilities(coordSystem, constraints, newConstraints, model);
 final int expectedLeftMargin = 21;
 final int actualLeftMargin = coordSystemUtilities.computeLeftMargin();
 assertNotEquals(expectedLeftMargin, actualLeftMargin);
 }

 @Test
 public void testComputeLeftMarginWithZeroMinAndMaxX() {
 final ChartDataModelConstraints newConstraints = new ChartDataModelConstraints();
 newConstraints.setMinimumColumnValue(0.0);
 newConstraints.setMaximumColumnValue(0.0);
 newConstraints.setMinimumValue(Double.MIN_VALUE);
 newConstraints.setMaximumValue(Double.MAX_VALUE);

 coordSystemUtilities = new CoordSystemUtilities(coordSystem, newConstraints, constraints2, model);
 final int expectedLeftMargin = 20;
 final int actualLeftMargin = coordSystemUtilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);
 }
}