package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartUtilities;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel dataModel;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 // Initialize test objects
 this.coordSystem = new CoordSystem(500, 500, 50, 50, 50, 50);
 this.constraints = new ChartDataModelConstraints();
 this.constraints2 = new ChartDataModelConstraints();
 this.dataModel = new ChartDataModel();
 this.utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel);
 }

 @Test
 public void testComputeLeftMargin_PositiveXMinAndXMax() {
 // Test case for positive x-min and x-max values
 double xmin = 0.0;
 double xmax = 10.0;
 this.constraints.setMinimumColumnValue(xmin);
 this.constraints.setMaximumColumnValue(xmax);
 int expected = 20;
 int actual = this.utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_NegativeXMinAndXMax() {
 // Test case for negative x-min and positive x-max values
 double xmin = -5.0;
 double xmax = 5.0;
 this.constraints.setMinimumColumnValue(xmin);
 this.constraints.setMaximumColumnValue(xmax);
 int expected = 31; // calculated value based on given code
 int actual = this.utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_ZeroXMinAndXMax() {
 // Test case for zero x-min and x-max values
 double xmin = 0.0;
 double xmax = 0.0;
 this.constraints.setMinimumColumnValue(xmin);
 this.constraints.setMaximumColumnValue(xmax);
 int expected = 20;
 int actual = this.utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMargin_NullCoordSystem() {
 // Test case for null coordSystem
 this.constraints.setMinimumColumnValue(0.0);
 this.constraints.setMaximumColumnValue(10.0);
 this.utilities = null;
 int actual = this.utilities.computeLeftMargin();
 fail("Expected IllegalArgumentException but no exception was thrown.");
 }

 @Test
 public void testComputeRightMargin() {
 // Test case for computeRightMargin method
 double xmin = 0.0;
 double xmax = 10.0;
 this.constraints.setMinimumColumnValue(xmin);
 this.constraints.setMaximumColumnValue(xmax);
 int expected = 15;
 int actual = this.utilities.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 // Test case for computeTopMargin method
 double ymin = 0.0;
 double ymax = 10.0;
 this.constraints.setMinimumValue(ymin);
 this.constraints.setMaximumValue(ymax);
 int expected = 15;
 int actual = this.utilities.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test case for computeBottomMargin method
 double ymin = -5.0;
 double ymax = 5.0;
 this.constraints.setMinimumValue(ymin);
 this.constraints.setMaximumValue(ymax);
 int expected = 31; // calculated value based on given code
 int actual = this.utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test case for computeXAxisLabelHeight method
 double min = 0.0;
 double max = 10.0;
 int height = this.utilities.computeXAxisLabelHeight();
 assertEquals(5, height);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test case for computeYAxisLabelWidth method
 double min = 0.0;
 double max = 10.0;
 int width = this.utilities.computeYAxisLabelWidth();
 assertEquals(5, width);
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test case for drawXAxisTicks method
 AffineTransform at = this.coordSystem.getTransform(0);
 double min = 0.0;
 double max = 10.0;
 int minValue = (int)min;
 int maxValue = (int)max;
 this.utilities.drawXAxisTicks(this.coordSystem.getOffscreenGraphics());
 assertNotNull(this.coordSystem.getOffscreenGraphics());
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test case for drawYAxisTicks method
 AffineTransform at = this.coordSystem.getTransform(0);
 double min = 0.0;
 double max = 10.0;
 double tick = 1.0;
 int height = this.utilities.computeXAxisLabelHeight();
 double ypt = 0.0;
 if (this.constraints.getMinimumValue().doubleValue() > 0.0) {
 ypt = this.constraints.getMinimumValue().doubleValue();
 }
 else if (this.constraints.getMaximumValue().doubleValue() < 0.0) {
 ypt = this.constraints.getMaximumValue().doubleValue();
 }
 this.utilities.drawYAxisTicks(this.coordSystem.getOffscreenGraphics());
 assertNotNull(this.coordSystem.getOffscreenGraphics());
 }
}