package de.progra.charting.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;

 @Before
 public void setUp() {
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 this.utilities = new CoordSystemUtilities(new CoordSystem(new java.awt.Dimension(500, 500), 0), constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin_MinValueLessThanZeroAndMaxValueGreaterThanZero() {
 double xmin = -10.5;
 double xmax = 5.5;
 double ymin = 0.1;
 double ymax = 15.5;
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xmin, xmax, ymin, ymax);
 this.utilities.setConstraints(constraints);
 this.utilities.setConstraints2(null);
 this.utilities.setModel(null);

 double expected = 116.0; // Calculated value using given coordinates and formulas
 int actual = this.utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_MinValueAndMaxValueGreaterThanZero() {
 double xmin = 2.5;
 double xmax = 7.5;
 double ymin = 0.1;
 double ymax = 15.5;
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xmin, xmax, ymin, ymax);
 this.utilities.setConstraints(constraints);
 this.utilities.setConstraints2(null);
 this.utilities.setModel(null);

 double expected = 25.0; // Calculated value using given coordinates and formulas
 int actual = this.utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 double xmin = 2.5;
 double xmax = 7.5;
 double ymin = 0.1;
 double ymax = 15.5;
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xmin, xmax, ymin, ymax);
 this.utilities.setConstraints(constraints);
 this.utilities.setConstraints2(null);
 this.utilities.setModel(null);

 double expected = 32.0; // Calculated value using given coordinates and formulas
 int actual = this.utilities.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 double xmin = 2.5;
 double xmax = 7.5;
 double ymin = 0.1;
 double ymax = 15.5;
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xmin, xmax, ymin, ymax);
 this.utilities.setConstraints(constraints);
 this.utilities.setConstraints2(null);
 this.utilities.setModel(null);

 double expected = 32.0; // Calculated value using given coordinates and formulas
 int actual = this.utilities.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_MinValueLessThanZeroAndMaxValueGreaterThanZero() {
 double xmin = 2.5;
 double xmax = 7.5;
 double ymin = -5.5;
 double ymax = 5.5;
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xmin, xmax, ymin, ymax);
 this.utilities.setConstraints(constraints);
 this.utilities.setConstraints2(null);
 this.utilities.setModel(null);

 double expected = 125.0; // Calculated value using given coordinates and formulas
 int actual = this.utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_MinValueAndMaxValueGreaterThanZero() {
 double xmin = 2.5;
 double xmax = 7.5;
 double ymin = 0.1;
 double ymax = 15.5;
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xmin, xmax, ymin, ymax);
 this.utilities.setConstraints(constraints);
 this.utilities.setConstraints2(null);
 this.utilities.setModel(null);

 double expected = 25.0; // Calculated value using given coordinates and formulas
 int actual = this.utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testComputeLeftMargin_NullConstraints() {
 this.utilities.setConstraints(null);
 this.utilities.setConstraints2(null);
 this.utilities.setModel(null);

 try {
 this.utilities.computeLeftMargin();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test (expected = IllegalArgumentException.class)
 public void testComputeLeftMargin_NullModel() {
 this.utilities.setConstraints(new ChartDataModelConstraints());
 this.utilities.setConstraints2(null);
 this.utilities.setModel(null);

 try {
 this.utilities.computeLeftMargin();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}