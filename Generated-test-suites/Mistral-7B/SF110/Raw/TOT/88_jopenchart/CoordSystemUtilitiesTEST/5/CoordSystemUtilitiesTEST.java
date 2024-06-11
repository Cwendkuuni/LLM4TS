package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystem coordSystem;
 private ChartDataModel dataModel;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 // Initialize CoordSystem and ChartDataModel
 this.coordSystem = new CoordSystem(500, 500);
 this.dataModel = new ChartDataModel();
 this.constraints = new ChartDataModelConstraints();
 this.constraints2 = new ChartDataModelConstraints();
 this.constraints.setMinimumColumnValue(-10);
 this.constraints.setMaximumColumnValue(10);
 this.constraints2.setMaximumColumnValue(5);

 // Set up CoordSystemUtilities
 CoordSystemUtilities utilities = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 }

 @After
 public void tearDown() {
 // Reset CoordSystem and ChartDataModel for next test
 this.coordSystem = null;
 this.dataModel = null;
 this.constraints = null;
 this.constraints2 = null;
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with valid data
 double xmin = -5;
 double xmax = 5;
 DecimalFormat yFormat = new DecimalFormat("#.##");
 Font font = new Font("Arial", Font.BOLD, 12);
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 int actual = util.computeLeftMargin();
 int expected = 56; // Calculated based on the given code
 assertEquals(expected, actual);

 // Test with xmin <= 0 and xmax > 0
 xmin = -3;
 xmax = 4;
 this.constraints.setMinimumColumnValue(xmin);
 this.constraints.setMaximumColumnValue(xmax);
 util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 actual = util.computeLeftMargin();
 expected = 72; // Calculated based on the given code
 assertEquals(expected, actual);

 // Test with xmin and xmax both <= 0
 xmin = -3;
 xmax = -4;
 this.constraints.setMinimumColumnValue(xmin);
 this.constraints.setMaximumColumnValue(xmax);
 util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 actual = util.computeLeftMargin();
 assertEquals(20, actual);
 }

 @Test
 public void testComputeRightMargin() {
 // Test with valid data
 DecimalFormat xFormat = new DecimalFormat("#.##");
 Font font = new Font("Arial", Font.BOLD, 12);
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 int actual = util.computeRightMargin();
 int expected = 21; // Calculated based on the given code
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 // Test with valid data
 double ymin = -5;
 double ymax = 5;
 DecimalFormat xFormat = new DecimalFormat("#.##");
 Font font = new Font("Arial", Font.BOLD, 12);
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 int actual = util.computeTopMargin();
 int expected = 30; // Calculated based on the given code
 assertEquals(expected, actual);

 // Test with ymin <= 0 and ymax > 0
 ymin = -3;
 ymax = 4;
 this.constraints.setMinimumValue(ymin);
 this.constraints.setMaximumValue(ymax);
 util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 actual = util.computeTopMargin();
 expected = 45; // Calculated based on the given code
 assertEquals(expected, actual);

 // Test with ymin and ymax both <= 0
 ymin = -3;
 ymax = -4;
 this.constraints.setMinimumValue(ymin);
 this.constraints.setMaximumValue(ymax);
 util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 actual = util.computeTopMargin();
 assertEquals(20, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test with valid data
 double ymin = -5;
 double ymax = 5;
 DecimalFormat xFormat = new DecimalFormat("#.##");
 Font font = new Font("Arial", Font.BOLD, 12);
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 int actual = util.computeBottomMargin();
 int expected = 30; // Calculated based on the given code
 assertEquals(expected, actual);

 // Test with ymin <= 0 and ymax > 0
 ymin = -3;
 ymax = 4;
 this.constraints.setMinimumValue(ymin);
 this.constraints.setMaximumValue(ymax);
 util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 actual = util.computeBottomMargin();
 expected = 45; // Calculated based on the given code
 assertEquals(expected, actual);

 // Test with ymin and ymax both <= 0
 ymin = -3;
 ymax = -4;
 this.constraints.setMinimumValue(ymin);
 this.constraints.setMaximumValue(ymax);
 util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 actual = util.computeBottomMargin();
 assertEquals(20, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test with valid data
 double min = -5;
 double max = 5;
 DecimalFormat df = new DecimalFormat("#.##");
 Font font = new Font("Arial", Font.BOLD, 12);
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 int actual = util.computeXAxisLabelHeight();
 int expected = 25; // Calculated based on the given code
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test with valid data
 double min = -5;
 double max = 5;
 DecimalFormat df = new DecimalFormat("#.##");
 Font font = new Font("Arial", Font.BOLD, 12);
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 int actual = util.computeYAxisLabelWidth();
 int expected = 72; // Calculated based on the given code
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Given
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 Graphics2D g = this.createMockGraphics2D();

 // When
 util.drawNumericalXAxisTicks(g);

 // Then
 // Add assertions for the expected output when drawing the X-axis ticks
 // For instance, you can check the position and appearance of the ticks and labels
 }

 @Test
 public void testDrawXAxisTicks() {
 // Given
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 Graphics2D g = this.createMockGraphics2D();

 // When
 util.drawXAxisTicks(g);

 // Then
 // Add assertions for the expected output when drawing the X-axis ticks
 // For instance, you can check the position and appearance of the ticks and labels
 }

 @Test
 public void testDrawYAxisTicks() {
 // Given
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 Graphics2D g = this.createMockGraphics2D();

 // When
 util.drawYAxisTicks(g);

 // Then
 // Add assertions for the expected output when drawing the Y-axis ticks
 // For instance, you can check the position and appearance of the ticks and labels
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullCoordSystem() {
 CoordSystemUtilities util = new CoordSystemUtilities(null, this.constraints, this.constraints2, this.dataModel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullConstraints() {
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, null, this.constraints2, this.dataModel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullConstraints2() {
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, null, this.dataModel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullDataModel() {
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullFont() {
 FontRenderContext frc = this.coordSystem.getFontRenderContext();
 CoordSystemUtilities util = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.dataModel);
 util.c.getFont() = null;
 int leftMargin = util.computeLeftMargin();
 fail("Expected IllegalArgumentException");
 }
}