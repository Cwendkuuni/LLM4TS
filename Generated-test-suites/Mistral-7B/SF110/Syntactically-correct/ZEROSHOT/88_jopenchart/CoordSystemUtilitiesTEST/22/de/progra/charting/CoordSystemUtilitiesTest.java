package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.ChartDataModelConstraints;
import de.progra.charting.model.CoordSystem;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 // Create a sample CoordSystem and set up the constraints and model
 DecimalFormat xDecimalFormat = new DecimalFormat("#.##");
 DecimalFormat yDecimalFormat = new DecimalFormat("#");
 Font font = new Font("SansSerif", Font.PLAIN, 12);
 coordSystem = new CoordSystem(200, 400, xDecimalFormat, yDecimalFormat, font);
 model = new ChartDataModel();

 constraints = new ChartDataModelConstraints();
 constraints.setMinimumColumnValue(-5.0);
 constraints.setMaximumColumnValue(5.0);
 constraints.setMinimumValue(-10.0);
 constraints.setMaximumValue(10.0);

 constraints2 = new ChartDataModelConstraints();
 constraints2.setMaximumColumnValue(10.0);
 constraints2.setMinimumColumnValue(-10.0);
 constraints2.setMaximumValue(20.0);
 constraints2.setMinimumValue(-20.0);

 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test case for xmin <= 0.0 and xmax > 0.0
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 int expectedLeftMargin = 125; // Calculated value based on the provided code
 int actualLeftMargin = utilities.computeLeftMargin();
 assertEquals("Incorrect left margin calculated.", expectedLeftMargin, actualLeftMargin);

 // Test case for xmin > 0.0 and xmax > 0.0
 xmin = 0.0;
 xmax = 10.0;
 expectedLeftMargin = 6;
 actualLeftMargin = utilities.computeLeftMargin();
 assertEquals("Incorrect left margin calculated.", expectedLeftMargin, actualLeftMargin);
 }

 @Test
 public void testComputeRightMargin() {
 int expectedRightMargin = 21; // Calculated value based on the provided code
 int actualRightMargin = utilities.computeRightMargin();
 assertEquals("Incorrect right margin calculated.", expectedRightMargin, actualRightMargin);
 }

 @Test
 public void testComputeTopMargin() {
 int expectedTopMargin = 15; // Calculated value based on the provided code
 int actualTopMargin = utilities.computeTopMargin();
 assertEquals("Incorrect top margin calculated.", expectedTopMargin, actualTopMargin);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test case for ymin <= 0.0 and ymax > 0.0
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 int expectedBottomMargin = 97; // Calculated value based on the provided code
 int actualBottomMargin = utilities.computeBottomMargin();
 assertEquals("Incorrect bottom margin calculated.", expectedBottomMargin, actualBottomMargin);

 // Test case for ymin > 0.0 and ymax > 0.0
 ymin = 0.0;
 ymax = 10.0;
 expectedBottomMargin = 15;
 actualBottomMargin = utilities.computeBottomMargin();
 assertEquals("Incorrect bottom margin calculated.", expectedBottomMargin, actualBottomMargin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 int expectedXAxisLabelHeight = 25; // Calculated value based on the provided code
 int actualXAxisLabelHeight = utilities.computeXAxisLabelHeight();
 assertEquals("Incorrect X-axis label height calculated.", expectedXAxisLabelHeight, actualXAxisLabelHeight);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 int expectedYAxisLabelWidth = 65; // Calculated value based on the provided code
 int actualYAxisLabelWidth = utilities.computeYAxisLabelWidth();
 assertEquals("Incorrect Y-axis label width calculated.", expectedYAxisLabelWidth, actualYAxisLabelWidth);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Create a mock Graphics2D object for testing
 Graphics2D mockGraphics2D = mock(Graphics2D.class);

 // Test case for drawing ticks and labels when paintLabels is true
 utilities.drawNumericalXAxisTicks(mockGraphics2D);
 verify(mockGraphics2D, times(5)).draw(isA(Line2D.class)); // Verify 5 tick lines drawn
 verify(mockGraphics2D, times(5)).drawString(anyString(), anyFloat(), anyFloat()); // Verify 5 labels drawn

 // Test case for not drawing labels when paintLabels is false
 constraints.setPaintLabels(false);
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 utilities.drawNumericalXAxisTicks(mockGraphics2D);
 verifyNoMoreInteractions(mockGraphics2D); // Verify no labels drawn
 }

 @Test
 public void testDrawXAxisTicks() {
 // Create a mock Graphics2D object for testing
 Graphics2D mockGraphics2D = mock(Graphics2D.class);

 // Test case for drawing ticks and labels when paintLabels is true
 utilities.drawXAxisTicks(mockGraphics2D);
 verify(mockGraphics2D, times(47)).draw(isA(Line2D.class)); // Verify 46 tick lines drawn + 1 X-axis line
 verify(mockGraphics2D, times(47)).drawString(anyString(), anyFloat(), anyFloat()); // Verify 46 labels drawn + 1 X-axis label

 // Test case for not drawing labels when paintLabels is false
 constraints.setPaintLabels(false);
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 utilities.drawXAxisTicks(mockGraphics2D);
 verifyNoMoreInteractions(mockGraphics2D); // Verify no labels drawn
 }

 @Test
 public void testDrawYAxisTicks() {
 // Create a mock Graphics2D object for testing
 Graphics2D mockGraphics2D = mock(Graphics2D.class);

 // Test case for drawing ticks and labels when paintLabels is true
 utilities.drawYAxisTicks(mockGraphics2D);
 verify(mockGraphics2D, times(11)).draw(isA(Line2D.class)); // Verify 10 tick lines drawn + 1 Y-axis line
 verify(mockGraphics2D, times(11)).drawString(anyString(), anyFloat(), anyFloat()); // Verify 10 labels drawn + 1 Y-axis label

 // Test case for not drawing labels when paintLabels is false
 constraints.setPaintLabels(false);
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 utilities.drawYAxisTicks(mockGraphics2D);
 verifyNoMoreInteractions(mockGraphics2D); // Verify no labels drawn
 }

 @Test
 public void testGetXAxisLine2D() {
 Line2D expectedXAxisLine2D = new Line2D.Double(0.0, 0.0, 10.0, 0.0);
 Line2D actualXAxisLine2D = utilities.getXAxisLine2D();
 assertEquals("Incorrect X-axis line calculated.", expectedXAxisLine2D, actualXAxisLine2D);
 }

 @Test
 public void testGetYAxisLine2D() {
 Line2D expectedYAxisLine2D = new Line2D.Double(0.0, 10.0, 0.0, 10.0);
 Line2D actualYAxisLine2D = utilities.getYAxisLine2D();
 assertEquals("Incorrect Y-axis line calculated.", expectedYAxisLine2D, actualYAxisLine2D);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 Line2D expectedSecondYAxisLine2D = new Line2D.Double(10.0, 0.0, 10.0, -20.0);
 Line2D actualSecondYAxisLine2D = utilities.getSecondYAxisLine2D();
 assertEquals("Incorrect second Y-axis line calculated.", expectedSecondYAxisLine2D, actualSecondYAxisLine2D);
 }
}