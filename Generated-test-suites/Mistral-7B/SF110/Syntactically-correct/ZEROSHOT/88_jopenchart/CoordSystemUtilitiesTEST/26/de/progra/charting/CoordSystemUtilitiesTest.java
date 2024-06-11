package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.ChartDataModelConstraints;
import de.progra.charting.model.CoordSystem;
import de.progra.charting.model.CoordSystemUtilities;
import de.progra.charting.utils.ChartUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;

 @Before
 public void setup() {
 // Create a mock CoordSystem
 double xMin = -10.0;
 double xMax = 10.0;
 double yMin = -5.0;
 double yMax = 5.0;
 coordSystem = new CoordSystem(xMin, xMax, yMin, yMax);

 // Create ChartDataModelConstraints
 constraints = new ChartDataModelConstraints(Arrays.asList(new Double[] { 1.5, 2.5, 3.5, 4.5 }), new Double[] { 1.0, 2.0, 3.0, 4.0 });
 constraints2 = new ChartDataModelConstraints(Arrays.asList(new Double[] { 5.5, 6.5, 7.5 }), new Double[] { 5.0, 6.0, 7.0 });

 // Create ChartDataModel
 model = new ChartDataModel(new String[] { "Label1", "Label2", "Label3", "Label4" }, new Double[][] { new Double[] { 1.5, 2.5, 3.5, 4.5 }, new Double[] { 5.5, 6.5, 7.5 } });

 // Initialize CoordSystemUtilities with mock objects
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test case with xmin <= 0.0 and xmax > 0.0
 double xmin = -5.0;
 double xmax = 5.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 int expectedLeftMargin = 42; // Calculate expected margin value
 int actualLeftMargin = utilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);
 }

 @Test
 public void testComputeRightMargin() {
 // Test case with xmin <= 0.0 and xmax > 0.0
 double xmin = -5.0;
 double xmax = 5.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 int expectedRightMargin = 21; // Calculate expected margin value
 int actualRightMargin = utilities.computeRightMargin();
 assertEquals(expectedRightMargin, actualRightMargin);
 }

 @Test
 public void testComputeTopMargin() {
 // Test case with ymin <= 0.0 and ymax > 0.0
 double ymin = -5.0;
 double ymax = 5.0;
 double tickSpacing = ChartUtilities.calculateTickSpacing(ymin, ymax);
 int expectedTopMargin = 30; // Calculate expected margin value
 int actualTopMargin = utilities.computeTopMargin();
 assertEquals(expectedTopMargin, actualTopMargin);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test case with ymin <= 0.0 and ymax > 0.0
 double ymin = -5.0;
 double ymax = 5.0;
 double tickSpacing = ChartUtilities.calculateTickSpacing(ymin, ymax);
 int expectedBottomMargin = 42; // Calculate expected margin value
 int actualBottomMargin = utilities.computeBottomMargin();
 assertEquals(expectedBottomMargin, actualBottomMargin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test case with min and max values
 double min = 0.0;
 double max = 10.0;
 int expectedXAxisLabelHeight = 18; // Calculate expected label height
 int actualXAxisLabelHeight = utilities.computeXAxisLabelHeight();
 assertEquals(expectedXAxisLabelHeight, actualXAxisLabelHeight);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test case with min and max values
 double min = 0.0;
 double max = 10.0;
 int expectedYAxisLabelWidth = 18; // Calculate expected label width
 int actualYAxisLabelWidth = utilities.computeYAxisLabelWidth();
 assertEquals(expectedYAxisLabelWidth, actualYAxisLabelWidth);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Create a mock Graphics2D
 Graphics2D gMock = mock(Graphics2D.class);
 // Call method to be tested
 utilities.drawNumericalXAxisTicks(gMock);
 // Verify that method was called correctly
 verify(gMock).draw(anyLine2D());
 }

 @Test
 public void testDrawXAxisTicks() {
 // Create a mock Graphics2D
 Graphics2D gMock = mock(Graphics2D.class);
 // Call method to be tested
 utilities.drawXAxisTicks(gMock);
 // Verify that method was called correctly
 verify(gMock, times(3)).draw(anyLine2D()); // Verify that 3 lines were drawn
 }

 @Test
 public void testDrawYAxisTicks() {
 // Create a mock Graphics2D
 Graphics2D gMock = mock(Graphics2D.class);
 // Call method to be tested
 utilities.drawYAxisTicks(gMock);
 // Verify that method was called correctly
 verify(gMock, times(3)).draw(anyLine2D()); // Verify that 3 lines were drawn
 }

}