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
 private DecimalFormat xDecimalFormat;
 private DecimalFormat yDecimalFormat;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 xDecimalFormat = new DecimalFormat("#.##");
 yDecimalFormat = new DecimalFormat("#");
 coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), new java.awt.Font("SansSerif", Font.BOLD, 12), 10, 10, 10, 10, null, null, 0.0, null, null, null, null);
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints(new double[]{10.0, 50.0}, new double[]{5.0, 50.0}, ChartUtilities.createData(10.0, 5.0, 1.0, 50.0), ChartUtilities.createData(1.0, 1.0, 1.0, 5.0));
 constraints2 = new ChartDataModelConstraints(new double[]{100.0, -50.0}, null, null, null);
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test case 1: Valid input
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 int expectedLeftMargin = 100;
 int actualLeftMargin = utilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);

 // Test case 2: Minimum x-values are negative and maximum x-values are positive
 double minXValue = -50.0;
 double maxXValue = 50.0;
 xmin = minXValue;
 xmax = maxXValue;
 String expectedErrorMessage = "Minimum column value cannot be negative and maximum column value cannot be positive at the same time.";
 try {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actualLeftMargin = utilities.computeLeftMargin();
 fail("Expected an error when creating the CoordSystemUtilities instance.");
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }

 // Test case 3: Y-axis label width is larger than the x-axis range
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 Double yAxisUnit = 50.0;
 Font font = new Font("SansSerif", Font.BOLD, 12);
 FontRenderContext frc = coordSystem.getFontRenderContext();
 TextLayout yAxisLabelLayout = new TextLayout(yAxisUnit.toString(), font, frc);
 int yAxisLabelWidth = yAxisLabelLayout.getBounds().getWidth();
 double xminValue = -50.0;
 double xmaxValue = 50.0;
 xmin = xminValue;
 xmax = xmaxValue;
 double xminAbsolute = Math.abs(xminValue);
 double xmaxAbsolute = Math.abs(xmaxValue);
 int expectedLeftMargin = yAxisLabelWidth + 12; // 6 for the margin and 6 for the label width
 int actualLeftMargin = utilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);
 }

 @Test
 public void testComputeRightMargin() {
 // Test case 1: Valid input
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 int expectedRightMargin = 15;
 int actualRightMargin = utilities.computeRightMargin();
 assertEquals(expectedRightMargin, actualRightMargin);

 // Test case 2: Minimum x-values are negative and maximum x-values are positive
 double minXValue = -50.0;
 double maxXValue = 50.0;
 xmin = minXValue;
 xmax = maxXValue;
 String expectedErrorMessage = "Minimum column value cannot be negative and maximum column value cannot be positive at the same time.";
 try {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actualRightMargin = utilities.computeRightMargin();
 fail("Expected an error when creating the CoordSystemUtilities instance.");
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testComputeTopMargin() {
 // Test case 1: Valid input
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 int expectedTopMargin = 15;
 int actualTopMargin = utilities.computeTopMargin();
 assertEquals(expectedTopMargin, actualTopMargin);

 // Test case 2: Minimum y-values are negative and maximum y-values are positive
 double minYValue = -50.0;
 double maxYValue = 50.0;
 ymin = minYValue;
 ymax = maxYValue;
 String expectedErrorMessage = "Minimum value cannot be negative and maximum value cannot be positive at the same time.";
 try {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actualTopMargin = utilities.computeTopMargin();
 fail("Expected an error when creating the CoordSystemUtilities instance.");
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testComputeBottomMargin() {
 // Test case 1: Valid input
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 int expectedBottomMargin = 15;
 int actualBottomMargin = utilities.computeBottomMargin();
 assertEquals(expectedBottomMargin, actualBottomMargin);

 // Test case 2: Minimum y-values are negative and maximum y-values are positive
 double minYValue = -50.0;
 double maxYValue = 50.0;
 ymin = minYValue;
 ymax = maxYValue;
 String expectedErrorMessage = "Minimum value cannot be negative and maximum value cannot be positive at the same time.";
 try {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int actualBottomMargin = utilities.computeBottomMargin();
 fail("Expected an error when creating the CoordSystemUtilities instance.");
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test case 1: Valid input
 double min = constraints.getMinimumValue().doubleValue();
 double max = constraints.getMaximumValue().doubleValue();
 int expectedHeight = 20;
 int actualHeight = utilities.computeXAxisLabelHeight();
 assertEquals(expectedHeight, actualHeight);

 // Test case 2: Minimum value is zero
 double minValue = 0.0;
 double maxValue = 100.0;
 min = minValue;
 int expectedHeight2 = 15;
 int actualHeight2 = utilities.computeXAxisLabelHeight();
 assertEquals(expectedHeight2, actualHeight2);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test case 1: Valid input
 double min = constraints.getMinimumValue().doubleValue();
 double max = constraints.getMaximumValue().doubleValue();
 int expectedWidth = 10;
 int actualWidth = utilities.computeYAxisLabelWidth();
 assertEquals(expectedWidth, actualWidth);

 // Test case 2: Minimum value is zero
 double minValue = 0.0;
 double maxValue = 100.0;
 min = minValue;
 int expectedWidth2 = 15;
 int actualWidth2 = utilities.computeYAxisLabelWidth();
 assertEquals(expectedWidth2, actualWidth2);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test case 1: Valid input
 Graphics2D g = coordSystem.createGraphics();
 utilities.drawNumericalXAxisTicks(g);
 // Verify that all ticks were drawn
 // This might be hard to test directly, but you can test the following assumptions
 // Assumption 1: The number of ticks drawn is equal to the number of data points in the ChartDataModel
 int numDataPoints = model.size();
 int numTicks = 0;
 for (int i = 0; i < numDataPoints; i++) {
 Point2D p = coordSystem.transform(new Point2D.Double(model.getXValueAt(i), model.getYValueAt(i)), null);
 Line2D tick = new Line2D.Double(p.getX(), p.getY() - 3.0, p.getX(), p.getY() + 3.0);
 boolean isTickDrawn = g.getClipBounds().intersects(tick.getBounds2D());
 assertTrue(isTickDrawn);
 numTicks++;
 }
 assertEquals(numDataPoints, numTicks);

 // Test case 2: Minimum x-values are negative and maximum x-values are positive
 double minXValue = -50.0;
 double maxXValue = 50.0;
 xmin = minXValue;
 xmax = maxXValue;
 String expectedErrorMessage = "Minimum column value cannot be negative and maximum column value cannot be positive at the same time.";
 try {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 Graphics2D g2 = coordSystem.createGraphics();
 utilities.drawNumericalXAxisTicks(g2);
 fail("Expected an error when creating the CoordSystemUtilities instance.");
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test case 1: Valid input
 Graphics2D g = coordSystem.createGraphics();
 utilities.drawXAxisTicks(g);
 // Verify that all ticks were drawn
 // This might be hard to test directly, but you can test the following assumptions
 // Assumption 1: The number of ticks drawn is equal to the number of integer values between the minimum and maximum x-values
 double minXValue = 0.0;
 double maxXValue = 10.0;
 xmin = minXValue;
 xmax = maxXValue;
 int numTicks = 10;
 int numDataPoints = numTicks + 1; // One data point for each integer value and one data point for the minimum value
 int expectedNumDataPoints = numDataPoints;
 int actualNumDataPoints = model.size();
 assertEquals(expectedNumDataPoints, actualNumDataPoints);
 for (int i = 0; i < numTicks; i++) {
 Point2D p = coordSystem.transform(new Point2D.Double(i + 1, model.getYValueAt(i)), null);
 Line2D tick = new Line2D.Double(p.getX(), p.getY() - 3.0, p.getX(), p.getY() + 3.0);
 boolean isTickDrawn = g.getClipBounds().intersects(tick.getBounds2D());
 assertTrue(isTickDrawn);
 }
 Point2D p2 = coordSystem.transform(new Point2D.Double(minXValue, model.getYValueAt(numDataPoints - 1)), null);
 Line2D tick2 = new Line2D.Double(p2.getX(), p2.getY() - 3.0, p2.getX(), p2.getY() + 3.0);
 boolean isTick2Drawn = g.getClipBounds().intersects(tick2.getBounds2D());
 assertTrue(isTick2Drawn);
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test case 1: Valid input
 Graphics2D g = coordSystem.createGraphics();
 utilities.drawYAxisTicks(g);
 // Verify that all ticks were drawn
 // This might be hard to test directly, but you can test the following assumptions
 // Assumption 1: The number of ticks drawn is equal to the number of data points in the ChartDataModel
 int numDataPoints = model.size();
 int numTicks = 0;
 for (int i = 0; i < numDataPoints; i++) {
 Point2D p = coordSystem.transform(new Point2D.Double(model.getXValueAt(i), model.getYValueAt(i)), null);
 Line2D tick = new Line2D.Double(p.getX() - 3.0, p.getY(), p.getX() + 3.0, p.getY());
 boolean isTickDrawn = g.getClipBounds().intersects(tick.getBounds2D());
 assertTrue(isTickDrawn);
 numTicks++;
 }
 assertEquals(numDataPoints, numTicks);
 }
}