package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setup() {
 // Create mock objects for CoordSystem, ChartDataModel, ChartDataModelConstraints
 coordSystem = new CoordSystem(new AffineTransform(), new AffineTransform(), new Dimension(500, 500), new Dimension(500, 500), new DecimalFormat(), new DecimalFormat(), null, null, null, null);
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();
 }

 @Test
 public void testComputeLeftMargin_MinAndMaxGreaterThanZero() {
 // Test case for computeLeftMargin() method when minimum and maximum values are greater than zero
 constraints.setMinimumColumnValue(10.5);
 constraints.setMaximumColumnValue(20.5);
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actual = utilities.computeLeftMargin();
 int expected = 22; // expected margin based on given test data
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_MinLessThanZeroAndMaxGreaterThanZero() {
 // Test case for computeLeftMargin() method when minimum value is less than zero and maximum value is greater than zero
 constraints.setMinimumColumnValue(-5.5);
 constraints.setMaximumColumnValue(20.5);
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actual = utilities.computeLeftMargin();
 int expected = 38; // expected margin based on given test data
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_MinAndMaxLessThanZero() {
 // Test case for computeLeftMargin() method when minimum and maximum values are less than zero
 constraints.setMinimumColumnValue(-10.5);
 constraints.setMaximumColumnValue(-20.5);
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actual = utilities.computeLeftMargin();
 int expected = 20; // expected margin based on given test data
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 // Test case for computeRightMargin() method
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(15.0);
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actual = utilities.computeRightMargin();
 int expected = 15; // expected margin based on given test data
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 // Test case for computeTopMargin() method
 Font font = new Font("SansSerif", Font.BOLD, 12);
 DecimalFormat decimalFormat = new DecimalFormat("#.##");
 coordSystem.setFont(font);
 coordSystem.setXDecimalFormat(decimalFormat);
 constraints.setMinimumValue(new Double(10.0));
 constraints.setMaximumValue(new Double(30.0));
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actual = utilities.computeTopMargin();
 int expected = 31; // expected margin based on given test data
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test case for computeBottomMargin() method when minimum and maximum values are greater than zero
 constraints.setMinimumValue(new Double(10.0));
 constraints.setMaximumValue(new Double(30.0));
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actual = utilities.computeBottomMargin();
 int expected = 31; // expected margin based on given test data
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_MinLessThanZeroAndMaxGreaterThanZero() {
 // Test case for computeBottomMargin() method when minimum value is less than zero and maximum value is greater than zero
 constraints.setMinimumValue(new Double(-10.0));
 constraints.setMaximumValue(new Double(30.0));
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actual = utilities.computeBottomMargin();
 int expected = 41; // expected margin based on given test data
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_MinAndMaxLessThanZero() {
 // Test case for computeBottomMargin() method when minimum and maximum values are less than zero
 constraints.setMinimumValue(new Double(-10.0));
 constraints.setMaximumValue(new Double(-30.0));
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actual = utilities.computeBottomMargin();
 int expected = 20; // expected margin based on given test data
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test case for computeXAxisLabelHeight() method
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(10.0);
 constraints.setMinimumValue(new Double(0.0));
 constraints.setMaximumValue(new Double(100.0));
 DecimalFormat decimalFormat = new DecimalFormat("#.##");
 Font font = new Font("SansSerif", Font.BOLD, 12);
 coordSystem.setFont(font);
 coordSystem.setXDecimalFormat(decimalFormat);
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actual = utilities.computeXAxisLabelHeight();
 int expected = 12; // expected height based on given test data
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test case for computeYAxisLabelWidth() method
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(10.0);
 constraints.setMinimumValue(new Double(0.0));
 constraints.setMaximumValue(new Double(100.0));
 DecimalFormat decimalFormat = new DecimalFormat("#.##");
 Font font = new Font("SansSerif", Font.BOLD, 12);
 coordSystem.setFont(font);
 coordSystem.setYDecimalFormat(decimalFormat);
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actual = utilities.computeYAxisLabelWidth();
 int expected = 4; // expected width based on given test data
 assertEquals(expected, actual);
 }

 // Add more test cases for other methods such as drawNumericalXAxisTicks(), drawXAxisTicks(), drawYAxisTicks(), getXAxisLine2D(), getYAxisLine2D(), and getSecondYAxisLine2D()

}