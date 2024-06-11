package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 // Create a sample CoordSystem
 double minX = -10.0;
 double maxX = 10.0;
 double minY = -5.0;
 double maxY = 5.0;
 double xTickSpacing = 1.0;
 double yTickSpacing = 1.0;
 ChartDataModelConstraints xAxisConstraints = new ChartDataModelConstraints(minX, maxX, xTickSpacing);
 ChartDataModelConstraints yAxisConstraints = new ChartDataModelConstraints(minY, maxY, yTickSpacing);
 ChartDataModelConstraints xAxisConstraints2 = new ChartDataModelConstraints(maxX, minX, yTickSpacing);
 ChartDataModelConstraints yAxisConstraints2 = new ChartDataModelConstraints(minY, maxY, xTickSpacing);
 DecimalFormat xDecimalFormat = new DecimalFormat("#.#");
 DecimalFormat yDecimalFormat = new DecimalFormat("#.#");
 coordSystem = new CoordSystem(new Dimension(200, 200), xDecimalFormat, yDecimalFormat, xAxisConstraints, yAxisConstraints, new Dimension(10, 10), null, null, null);
 model = new ChartDataModel(new String[]{"X1", "X2", "X3"}, Arrays.asList(Arrays.asList(1.0, 2.0, 3.0), Arrays.asList(4.0, 5.0, 6.0), Arrays.asList(7.0, 8.0, 9.0)));
 constraints = new ChartDataModelConstraints(xAxisConstraints, yAxisConstraints, model);
 constraints2 = new ChartDataModelConstraints(xAxisConstraints2, yAxisConstraints2, model);
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 int expectedLeftMargin = 145; // Calculate the expected left margin using the given coordinates and constraints
 int actualLeftMargin = utilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);
 }

 @Test
 public void testComputeRightMargin() {
 int expectedRightMargin = 21; // Calculate the expected right margin based on the given font and width
 int actualRightMargin = utilities.computeRightMargin();
 assertEquals(expectedRightMargin, actualRightMargin);
 }

 @Test
 public void testComputeTopMargin() {
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 int expectedTopMargin = 31; // Calculate the expected top margin using the given coordinates and constraints
 int actualTopMargin = utilities.computeTopMargin();
 assertEquals(expectedTopMargin, actualTopMargin);
 }

 @Test
 public void testComputeBottomMargin() {
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 int expectedBottomMargin = 128; // Calculate the expected bottom margin using the given coordinates, constraints, and font
 int actualBottomMargin = utilities.computeBottomMargin();
 assertEquals(expectedBottomMargin, actualBottomMargin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 int expectedXAxisLabelHeight = 32; // Calculate the expected height of the X-axis labels based on the given data and font
 int actualXAxisLabelHeight = utilities.computeXAxisLabelHeight();
 assertEquals(expectedXAxisLabelHeight, actualXAxisLabelHeight);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 int expectedYAxisLabelWidth = 68; // Calculate the expected width of the Y-axis labels based on the given data and font
 int actualYAxisLabelWidth = utilities.computeYAxisLabelWidth();
 assertEquals(expectedYAxisLabelWidth, actualYAxisLabelWidth);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawNumericalXAxisTicks(g);
 // Add verification code here to check if the X-axis ticks were drawn correctly
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawXAxisTicks(g);
 // Add verification code here to check if the X-axis ticks and labels were drawn correctly
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawYAxisTicks(g);
 // Add verification code here to check if the Y-axis ticks and labels were drawn correctly
 }

}