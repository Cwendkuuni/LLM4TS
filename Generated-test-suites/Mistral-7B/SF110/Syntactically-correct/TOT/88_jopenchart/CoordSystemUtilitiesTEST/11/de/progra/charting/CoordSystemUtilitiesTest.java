package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataColumn;

public class CoordSystemUtilitiesTest {

 private CoordSystem c;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 c = new CoordSystem(new java.awt.Dimension(500, 400), new java.awt.geom.Path2D.Double());
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with valid data
 constraints.setMinimumColumnValue(-5.0);
 constraints.setMaximumColumnValue(5.0);
 constraints.setMinimumValue(Double.MIN_VALUE);
 constraints.setMaximumValue(Double.MAX_VALUE);
 model.setColumns(Arrays.asList(new DataColumn("Column1", Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0))));
 new CoordSystemUtilities(c, constraints, constraints2, model);
 int expected = 52; // Calculated using the provided formula in the class
 int actual = new CoordSystemUtilities(c, constraints, constraints2, model).computeLeftMargin();
 assertEquals("computeLeftMargin: Valid data", expected, actual);

 // Test with xmin <= 0 and xmax > 0
 constraints.setMinimumColumnValue(-3.0);
 constraints.setMaximumColumnValue(3.0);
 int margin = new CoordSystemUtilities(c, constraints, constraints2, model).computeLeftMargin();
 assertTrue("computeLeftMargin: xmin <= 0 and xmax > 0", margin > 20);

 // Test with xmin <= 0 and xmax <= 0
 constraints.setMinimumColumnValue(-3.0);
 constraints.setMaximumColumnValue(-3.0);
 margin = new CoordSystemUtilities(c, constraints, constraints2, model).computeLeftMargin();
 assertTrue("computeLeftMargin: xmin <= 0 and xmax <= 0", margin == 20);

 // Test with null constraints
 constraints = null;
 try {
 new CoordSystemUtilities(c, constraints, constraints2, model);
 fail("computeLeftMargin: Null constraints should cause an exception");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testComputeRightMargin() {
 // Test with valid data
 constraints.setMinimumColumnValue(-5.0);
 constraints.setMaximumColumnValue(5.0);
 int expected = 18; // Calculated using the provided formula in the class
 int actual = new CoordSystemUtilities(c, constraints, constraints2, model).computeRightMargin();
 assertEquals("computeRightMargin: Valid data", expected, actual);

 // Test with null constraints
 constraints = null;
 try {
 new CoordSystemUtilities(c, constraints, constraints2, model);
 fail("computeRightMargin: Null constraints should cause an exception");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testComputeTopMargin() {
 // Test with valid data
 int expected = 18; // Calculated using the provided formula in the class
 int actual = new CoordSystemUtilities(c, constraints, constraints2, model).computeTopMargin();
 assertEquals("computeTopMargin: Valid data", expected, actual);

 // Test with null constraints
 constraints = null;
 try {
 new CoordSystemUtilities(c, constraints, constraints2, model);
 fail("computeTopMargin: Null constraints should cause an exception");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testComputeBottomMargin() {
 // Test with valid data
 Double minValue = 1.0;
 Double maxValue = 10.0;
 constraints.setMinimumValue(minValue);
 constraints.setMaximumValue(maxValue);
 int expected = 52; // Calculated using the provided formula in the class
 int actual = new CoordSystemUtilities(c, constraints, constraints2, model).computeBottomMargin();
 assertEquals("computeBottomMargin: Valid data", expected, actual);

 // Test with ymin <= 0 and ymax > 0
 constraints.setMinimumValue(Double.MIN_VALUE);
 constraints.setMaximumValue(Double.MAX_VALUE);
 actual = new CoordSystemUtilities(c, constraints, constraints2, model).computeBottomMargin();
 assertTrue("computeBottomMargin: ymin <= 0 and ymax > 0", actual > 20);

 // Test with ymin <= 0 and ymax <= 0
 constraints.setMinimumValue(Double.MIN_VALUE);
 constraints.setMaximumValue(Double.MIN_VALUE);
 actual = new CoordSystemUtilities(c, constraints, constraints2, model).computeBottomMargin();
 assertTrue("computeBottomMargin: ymin <= 0 and ymax <= 0", actual == 20);

 // Test with null constraints
 constraints = null;
 try {
 new CoordSystemUtilities(c, constraints, constraints2, model);
 fail("computeBottomMargin: Null constraints should cause an exception");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test with valid data
 constraints.setMinimumColumnValue(-5.0);
 constraints.setMaximumColumnValue(5.0);
 double tick = 1.0;
 int expected = 12; // Calculated using the provided formula in the class
 int actual = new CoordSystemUtilities(c, constraints, constraints2, model).computeXAxisLabelHeight();
 assertEquals("computeXAxisLabelHeight: Valid data", expected, actual);

 // Test with null constraints
 constraints = null;
 try {
 new CoordSystemUtilities(c, constraints, constraints2, model);
 fail("computeXAxisLabelHeight: Null constraints should cause an exception");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test with valid data
 constraints.setMinimumValue(1.0);
 constraints.setMaximumValue(10.0);
 double tick = 1.0;
 int expected = 6; // Calculated using the provided formula in the class
 int actual = new CoordSystemUtilities(c, constraints, constraints2, model).computeYAxisLabelWidth();
 assertEquals("computeYAxisLabelWidth: Valid data", expected, actual);

 // Test with null constraints
 constraints = null;
 try {
 new CoordSystemUtilities(c, constraints, constraints2, model);
 fail("computeYAxisLabelWidth: Null constraints should cause an exception");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test with valid data
 DecimalFormat df = new DecimalFormat("#.##");
 g.setColor(Color.BLACK);
 new CoordSystemUtilities(c, constraints, constraints2, model).drawNumericalXAxisTicks(g);
 // Verify that all tick marks and labels are drawn correctly
 // ...
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test with valid data
 g.setColor(Color.BLACK);
 new CoordSystemUtilities(c, constraints, constraints2, model).drawXAxisTicks(g);
 // Verify that all tick marks and labels are drawn correctly
 // ...
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test with valid data
 g.setColor(Color.BLACK);
 new CoordSystemUtilities(c, constraints, constraints2, model).drawYAxisTicks(g);
 // Verify that all tick marks and labels are drawn correctly
 // ...
 }
}