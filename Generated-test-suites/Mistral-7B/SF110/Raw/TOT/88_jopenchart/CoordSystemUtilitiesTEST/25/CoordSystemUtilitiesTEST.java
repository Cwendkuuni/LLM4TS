package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemConstraints;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setup() {
 final DecimalFormat xdf = new DecimalFormat("#.##");
 final DecimalFormat ydf = new DecimalFormat("#.##");

 this.coordSystem = new CoordSystem(new Dimension(600, 400), 10, 10, 15, 15, 15, 15, xdf, ydf, null, null, null);
 this.model = new ChartDataModel();
 this.constraints = new ChartDataModelConstraints(new double[]{0.0, 10.0}, new double[]{0.0, 10.0});
 this.constraints2 = new ChartDataModelConstraints(new double[]{10.0, 0.0}, new double[]{0.0, 10.0});
 this.utilities = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.model);
 }

 @Test
 public void testComputeLeftMargin() {
 final int expected = 88;
 final int actual = this.utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_MinimumValueIsNegative() {
 final int expected = 88;
 this.constraints.setMinimumColumnValue(-10.0);
 final int actual = this.utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_MaximumValueIsNegative() {
 final int expected = 88;
 this.constraints.setMaximumColumnValue(-10.0);
 this.constraints2.setMinimumColumnValue(10.0);
 this.constraints2.setMaximumColumnValue(0.0);
 final int actual = this.utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_NoYAxisLabel() {
 final int expected = 20;
 this.coordSystem.setShowYAxisLabel(false);
 final int actual = this.utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 final int expected = 35;
 final int actual = this.utilities.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 final int expected = 35;
 final int actual = this.utilities.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 final int expected = 88;
 final int actual = this.utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_MinimumValueIsNegative() {
 final int expected = 88;
 this.constraints.setMinimumValue(-5.0);
 final int actual = this.utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_MaximumValueIsNegative() {
 final int expected = 88;
 this.constraints.setMaximumValue(-5.0);
 this.constraints2.setMinimumValue(5.0);
 this.constraints2.setMaximumValue(0.0);
 final int actual = this.utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 final int expected = 17;
 final int actual = this.utilities.computeXAxisLabelHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 final int expected = 30;
 final int actual = this.utilities.computeYAxisLabelWidth();
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 final Graphics2D g = this.coordSystem.createGraphics2DRenderer();
 this.utilities.drawNumericalXAxisTicks(g);
 final int expected = 10;
 final int actual = (int) g.getFontMetrics().getHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawXAxisTicks() {
 final Graphics2D g = this.coordSystem.createGraphics2DRenderer();
 this.utilities.drawXAxisTicks(g);
 final int expected = 11;
 final int actual = g.getGraphicsConfig().getRGB();
 assertEquals(Color.BLACK.getRGB(), actual);
 }

 @Test
 public void testDrawYAxisTicks() {
 final Graphics2D g = this.coordSystem.createGraphics2DRenderer();
 this.utilities.drawYAxisTicks(g);
 final int expected = 11;
 final int actual = g.getGraphicsConfig().getRGB();
 assertEquals(Color.BLACK.getRGB(), actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDrawYAxisTicks_InvalidChartDataModelConstraints() {
 final Graphics2D g = this.coordSystem.createGraphics2DRenderer();
 this.constraints = null;
 this.utilities = new CoordSystemUtilities(this.coordSystem, null, this.constraints2, this.model);
 this.utilities.drawYAxisTicks(g);
 }
}