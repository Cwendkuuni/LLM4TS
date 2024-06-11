package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel dataModel;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setup() {
 // Create a mock CoordSystem
 coordSystem = new CoordSystem() {
 // Override getTransform to return a mock AffineTransform
 @Override
 public AffineTransform getTransform(int axis) {
 return new AffineTransform();
 }
 };

 // Create mock ChartDataModel and constraints
 dataModel = new ChartDataModel();
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();

 // Initialize the test object with the mock objects
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test case 1: xmin <= 0.0 && xmax > 0.0
 constraints.setMinimumColumnValue(-10.0);
 constraints.setMaximumColumnValue(5.0);

 int leftMargin = utilities.computeLeftMargin();
 assertEquals(52, leftMargin); // 52 is a random value, please adjust according to your implementation

 // Test case 2: xmin > 0.0
 constraints.setMinimumColumnValue(5.0);
 constraints.setMaximumColumnValue(10.0);

 leftMargin = utilities.computeLeftMargin();
 assertEquals(20, leftMargin);
 }

 @Test
 public void testComputeRightMargin() {
 // Test case 1: no second Y-Axis
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(10.0);

 int rightMargin = utilities.computeRightMargin();
 assertEquals(15, rightMargin);

 // Test case 2: second Y-Axis exists
 constraints2.setMinimumColumnValue(15.0);
 constraints2.setMaximumColumnValue(20.0);

 rightMargin = utilities.computeRightMargin();
 assertEquals(30, rightMargin); // 30 is a random value, please adjust according to your implementation
 }

 @Test
 public void testComputeTopMargin() {
 // Test case 1: ymin <= 0.0 && ymax > 0.0
 constraints.setMinimumValue(-5.0);
 constraints.setMaximumValue(5.0);

 int topMargin = utilities.computeTopMargin();
 assertEquals(30, topMargin); // 30 is a random value, please adjust according to your implementation

 // Test case 2: ymin > 0.0
 constraints.setMinimumValue(5.0);
 constraints.setMaximumValue(10.0);

 topMargin = utilities.computeTopMargin();
 assertEquals(20, topMargin);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test case 1: ymin <= 0.0 && ymax > 0.0
 constraints.setMinimumValue(-5.0);
 constraints.setMaximumValue(5.0);

 int bottomMargin = utilities.computeBottomMargin();
 assertEquals(52, bottomMargin); // 52 is a random value, please adjust according to your implementation

 // Test case 2: ymin > 0.0
 constraints.setMinimumValue(5.0);
 constraints.setMaximumValue(10.0);

 bottomMargin = utilities.computeBottomMargin();
 assertEquals(20, bottomMargin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test case 1: min < 0.0 and max > 0.0
 constraints.setMinimumValue(-5.0);
 constraints.setMaximumValue(5.0);

 int height = utilities.computeXAxisLabelHeight();
 assertEquals(15, height); // 15 is a random value, please adjust according to your implementation

 // Test case 2: min >= 0.0 and max >= 0.0
 constraints.setMinimumValue(5.0);
 constraints.setMaximumValue(10.0);

 height = utilities.computeXAxisLabelHeight();
 assertEquals(10, height); // 10 is a random value, please adjust according to your implementation
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test case 1: min < 0.0 and max > 0.0
 constraints.setMinimumValue(-5.0);
 constraints.setMaximumValue(5.0);

 int width = utilities.computeYAxisLabelWidth();
 assertEquals(15, width); // 15 is a random value, please adjust according to your implementation

 // Test case 2: min >= 0.0 and max >= 0.0
 constraints.setMinimumValue(5.0);
 constraints.setMaximumValue(10.0);

 width = utilities.computeYAxisLabelWidth();
 assertEquals(10, width); // 10 is a random value, please adjust according to your implementation
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test case 1: draw ticks and labels
 Graphics2D g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(new Font("Arial", Font.BOLD, 12));

 utilities.drawNumericalXAxisTicks(g);

 verify(g, times(1)).draw(argThat(isA(Line2D.class)));
 verify(g, times(1)).setFont(argThat(isA(Font.class)));
 verify(g, times(1)).drawString(eq(""), anyFloat(), anyFloat());

 // Test case 2: don't draw labels
 when(g.isPaintLabels()).thenReturn(false);

 utilities.drawNumericalXAxisTicks(g);

 verify(g, never()).drawString(eq(""), anyFloat(), anyFloat());
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test case 1: draw ticks and labels
 Graphics2D g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(new Font("Arial", Font.BOLD, 12));

 utilities.drawXAxisTicks(g);

 verify(g, times(1)).draw(argThat(isA(Line2D.class)));
 verify(g, times(1)).setFont(argThat(isA(Font.class)));
 verify(g, times(10)).drawString(anyString(), anyFloat(), anyFloat()); // Replace "10" with the number of axis ticks

 // Test case 2: don't draw labels
 when(g.isPaintLabels()).thenReturn(false);

 utilities.drawXAxisTicks(g);

 verify(g, times(1)).draw(argThat(isA(Line2D.class)));
 verify(g, never()).drawString(anyString(), anyFloat(), anyFloat());
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test case 1: draw ticks and labels
 Graphics2D g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(new Font("Arial", Font.BOLD, 12));

 utilities.drawYAxisTicks(g);

 verify(g, times(1)).draw(argThat(isA(Line2D.class)));
 verify(g, times(1)).setFont(argThat(isA(Font.class)));
 verify(g, times(10)).drawString(anyString(), anyFloat(), anyFloat()); // Replace "10" with the number of axis ticks

 // Test case 2: don't draw labels
 when(g.isPaintLabels()).thenReturn(false);

 utilities.drawYAxisTicks(g);

 verify(g, times(1)).draw(argThat(isA(Line2D.class)));
 verify(g, never()).drawString(anyString(), anyFloat(), anyFloat());
 }

 @Test
 public void testGetXAxisLine2D() {
 // Test case 1: xmin <= 0.0 && xmax > 0.0
 constraints.setMinimumColumnValue(-10.0);
 constraints.setMaximumColumnValue(5.0);

 Line2D xAxisLine = utilities.getXAxisLine2D();
 assertEquals(xAxisLine.getX1(), -4.0, 0.01); // Adjust the expected value based on your implementation
 assertEquals(xAxisLine.getY1(), 0.0);
 assertEquals(xAxisLine.getX2(), 4.0, 0.01);
 assertEquals(xAxisLine.getY2(), 0.0);

 // Test case 2: xmin > 0.0
 constraints.setMinimumColumnValue(5.0);
 constraints.setMaximumColumnValue(10.0);

 xAxisLine = utilities.getXAxisLine2D();
 assertEquals(xAxisLine.getX1(), 5.0, 0.01);
 assertEquals(xAxisLine.getY1(), 0.0);
 assertEquals(xAxisLine.getX2(), 10.0, 0.01);
 assertEquals(xAxisLine.getY2(), 0.0);
 }

 @Test
 public void testGetYAxisLine2D() {
 // Test case 1: ymin <= 0.0 && ymax > 0.0
 constraints.setMinimumValue(-5.0);
 constraints.setMaximumValue(5.0);

 Line2D yAxisLine = utilities.getYAxisLine2D();
 assertEquals(yAxisLine.getX1(), 0.0, 0.01);
 assertEquals(yAxisLine.getY1(), 5.0, 0.01);
 assertEquals(yAxisLine.getX2(), 0.0, 0.01);
 assertEquals(yAxisLine.getY2(), -5.0, 0.01);

 // Test case 2: ymin > 0.0
 constraints.setMinimumValue(5.0);
 constraints.setMaximumValue(10.0);

 yAxisLine = utilities.getYAxisLine2D();
 assertEquals(yAxisLine.getX1(), 0.0, 0.01);
 assertEquals(yAxisLine.getY1(), 10.0, 0.01);
 assertEquals(yAxisLine.getX2(), 0.0, 0.01);
 assertEquals(yAxisLine.getY2(), 5.0, 0.01);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 // Test case 1: second Y-Axis exists
 constraints2.setMinimumColumnValue(15.0);
 constraints2.setMaximumColumnValue(20.0);

 Line2D secondYAxisLine = utilities.getSecondYAxisLine2D();
 assertEquals(secondYAxisLine.getX1(), 20.0, 0.01);
 assertEquals(secondYAxisLine.getY1(), 5.0, 0.01);
 assertEquals(secondYAxisLine.getX2(), 20.0, 0.01);
 assertEquals(secondYAxisLine.getY2(), -5.0, 0.01);

 // Test case 2: second Y-Axis does not exist
 constraints2 = new ChartDataModelConstraints();

 secondYAxisLine = utilities.getSecondYAxisLine2D();
 assertNull(secondYAxisLine);
 }
}