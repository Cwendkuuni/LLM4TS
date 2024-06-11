package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities coordSystemUtilities;
 private CoordSystem coordSystem;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;

 @Before
 public void setUp() {
 // Initialize test data
 double[] values = { 0.5, 1.0, 2.0, 3.0 };
 Number[] constraintsValues = { 0.0, 4.0, 6.0, 8.0 };
 Number[] constraintsValues2 = { 5.0, 7.0, 9.0, 11.0 };
 ChartColumn[] columns = new ChartColumn[4];
 for (int i = 0; i < 4; i++) {
 columns[i] = new ChartColumn("Column " + (i + 1), values[i]);
 }
 model = new ChartDataModel(columns);
 coordSystem = new CoordSystem(100, 100, 100, 100, 1, new DecimalFormat("#.##"), new DecimalFormat("#.##"));
 constraints = new ChartDataModelConstraints(constraintsValues, false, false);
 constraints2 = new ChartDataModelConstraints(constraintsValues2, false, false);
 coordSystemUtilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test positive case with non-zero minimum and maximum x values
 double[] expectedValues = { 28, 49, 71, 93 };
 for (int i = 0; i < 4; i++) {
 assertEquals(expectedValues[i], coordSystemUtilities.computeLeftMargin());
 }

 // Test positive case with zero minimum x value and non-zero maximum x value
 constraints.setMinimumColumnValue(0.0);
 for (int i = 0; i < 4; i++) {
 double min = constraintsValues[i];
 double max = constraintsValues[i + 1];
 constraints.setMinimumColumnValue(min);
 constraints.setMaximumColumnValue(max);
Double expectedLeftMargin = computeExpectedLeftMargin(min, max, coordSystem.getBounds().getWidth(), coordSystem.getRightMargin());
 int actualLeftMargin = coordSystemUtilities.computeLeftMargin();
 assertEquals(expectedLeftMargin.intValue(), actualLeftMargin);
 }

 // Test positive case with non-zero minimum and maximum y values
 double[] expectedValuesY = { 31, 57, 82, 108 };
 for (int i = 0; i < 4; i++) {
 constraints2.setMinimumValue(constraintsValues2[i].doubleValue());
 constraints2.setMaximumValue(constraintsValues2[i + 1].doubleValue());
 assertEquals(expectedValuesY[i], coordSystemUtilities.computeLeftMargin());
 }

 // Test negative case with zero minimum and maximum x values
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(0.0);
 assertEquals(20, coordSystemUtilities.computeLeftMargin());

 // Test negative case with zero minimum x value and maximum x value less than zero
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(-1.0);
 assertEquals(20, coordSystemUtilities.computeLeftMargin());
 }

 @Test
 public void testComputeRightMargin() {
 // Test positive case with non-zero minimum and maximum x values
 double[] expectedValues = { 27, 46, 65, 84 };
 for (int i = 0; i < 4; i++) {
 assertEquals(expectedValues[i], coordSystemUtilities.computeRightMargin(), 0.1);
 }

 // Test positive case with zero minimum x value and non-zero maximum x value
 constraints.setMinimumColumnValue(0.0);
 for (int i = 0; i < 4; i++) {
 double min = constraintsValues[i];
 double max = constraintsValues[i + 1];
 constraints.setMinimumColumnValue(min);
 constraints.setMaximumColumnValue(max);
 double width = computeExpectedWidth(min, max);
 assertEquals((int)Math.ceil(width), coordSystemUtilities.computeRightMargin(), 0.1);
 }

 // Test positive case with non-zero minimum and maximum y values
 double[] expectedValuesY = { 32, 58, 83, 109 };
 for (int i = 0; i < 4; i++) {
 constraints2.setMinimumValue(constraintsValues2[i].doubleValue());
 constraints2.setMaximumValue(constraintsValues2[i + 1].doubleValue());
 assertEquals(expectedValuesY[i], coordSystemUtilities.computeRightMargin(), 0.1);
 }

 // Test negative case with zero minimum and maximum x values
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(0.0);
 assertEquals(15, coordSystemUtilities.computeRightMargin(), 0.1);
}

 @Test
 public void testComputeTopMargin() {
 // Test positive case with non-zero minimum and maximum y values
 double[] expectedValues = { 29, 53, 76, 98 };
 for (int i = 0; i < 4; i++) {
 assertEquals(expectedValues[i], coordSystemUtilities.computeTopMargin(), 0.1);
 }

 // Test positive case with zero minimum y value and non-zero maximum y value
 constraints2.setMinimumValue(0.0);
 for (int i = 0; i < 4; i++) {
 double min = constraintsValues2[i];
 double max = constraintsValues2[i + 1];
 constraints2.setMinimumValue(min);
 constraints2.setMaximumValue(max);
 double height = computeExpectedHeight(min, max);
 assertEquals((int)Math.ceil(height), coordSystemUtilities.computeTopMargin(), 0.1);
 }

 // Test negative case with zero minimum and maximum y values
 constraints2.setMinimumValue(0.0);
 constraints2.setMaximumValue(0.0);
 assertEquals(15, coordSystemUtilities.computeTopMargin(), 0.1);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test positive case with non-zero minimum and maximum y values
 double[] expectedValues = { 33, 59, 84, 109 };
 for (int i = 0; i < 4; i++) {
 assertEquals(expectedValues[i], coordSystemUtilities.computeBottomMargin(), 0.1);
 }

 // Test positive case with zero minimum y value and non-zero maximum y value
 constraints2.setMinimumValue(0.0);
 for (int i = 0; i < 4; i++) {
 double min = constraintsValues2[i];
 double max = constraintsValues2[i + 1];
 constraints2.setMinimumValue(min);
 constraints2.setMaximumValue(max);
 double height = computeExpectedHeight(min, max);
 assertEquals((int)Math.ceil(height), coordSystemUtilities.computeBottomMargin(), 0.1);
 }

 // Test negative case with zero minimum and maximum y values
 constraints2.setMinimumValue(0.0);
 constraints2.setMaximumValue(0.0);
 assertEquals(20, coordSystemUtilities.computeBottomMargin(), 0.1);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test positive case with non-zero minimum and maximum x values
 double[] expectedValues = { 20, 26, 32, 38 };
 for (int i = 0; i < 4; i++) {
 assertEquals(expectedValues[i], coordSystemUtilities.computeXAxisLabelHeight(), 0.1);
 }

 // Test positive case with zero minimum x value and non-zero maximum x value
 constraints.setMinimumColumnValue(0.0);
 for (int i = 0; i < 4; i++) {
 double min = constraintsValues[i];
 double max = constraintsValues[i + 1];
 constraints.setMinimumColumnValue(min);
 constraints.setMaximumColumnValue(max);
 assertEquals(computeExpectedXAxisLabelHeight(min, max), coordSystemUtilities.computeXAxisLabelHeight(), 0.1);
 }

 // Test negative case with zero minimum and maximum x values
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(0.0);
 assertEquals(12, coordSystemUtilities.computeXAxisLabelHeight(), 0.1);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test positive case with non-zero minimum and maximum y values
 double[] expectedValues = { 15, 22, 29, 36 };
 for (int i = 0; i < 4; i++) {
 assertEquals(expectedValues[i], coordSystemUtilities.computeYAxisLabelWidth(), 0.1);
 }

 // Test positive case with zero minimum y value and non-zero maximum y value
 constraints2.setMinimumValue(0.0);
 for (int i = 0; i < 4; i++) {
 double min = constraintsValues2[i];
 double max = constraintsValues2[i + 1];
 constraints2.setMinimumValue(min);
 constraints2.setMaximumValue(max);
 assertEquals(computeExpectedYAxisLabelWidth(min, max), coordSystemUtilities.computeYAxisLabelWidth(), 0.1);
 }

 // Test negative case with zero minimum and maximum y values
 constraints2.setMinimumValue(0.0);
 constraints2.setMaximumValue(0.0);
 assertEquals(21, coordSystemUtilities.computeYAxisLabelWidth(), 0.1);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test positive case with non-zero minimum and maximum x values
 Graphics2D g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(coordSystem.getFont());
 when(g.isPaintLabels()).thenReturn(true);
 coordSystemUtilities.drawNumericalXAxisTicks(g);
 // Verify drawing of ticks and labels
 verify(g, atLeast(4)).draw(argThat(isA(Line2D.class)));
 verify(g, atLeast(4)).drawString(anyString(), argThat(isA(Float.class)), argThat(isA(Float.class)));

 // Test positive case with zero minimum x value and non-zero maximum x value
 constraints.setMinimumColumnValue(0.0);
 when(g.getFont()).thenReturn(coordSystem.getFont());
 when(g.isPaintLabels()).thenReturn(true);
 coordSystemUtilities.drawNumericalXAxisTicks(g);
 // Verify drawing of ticks and labels
 verify(g, atLeast(4)).draw(argThat(isA(Line2D.class)));
 verify(g, times(2)).drawString(anyString(), argThat(isA(Float.class)), argThat(isA(Float.class)));
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test positive case with non-zero minimum and maximum x values
 Graphics2D g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(coordSystem.getFont());
 when(g.isPaintLabels()).thenReturn(true);
 coordSystemUtilities.drawXAxisTicks(g);
 // Verify drawing of ticks and labels
 verify(g, atLeast(6)).draw(argThat(isA(Line2D.class)));
 verify(g, times(4)).drawString(anyString(), argThat(isA(Float.class)), argThat(isA(Float.class)));
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test positive case with non-zero minimum and maximum y values
 Graphics2D g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(coordSystem.getFont());
 when(g.isPaintLabels()).thenReturn(true);
 coordSystemUtilities.drawYAxisTicks(g);
 // Verify drawing of ticks and labels
 verify(g, times(4)).draw(argThat(isA(Line2D.class)));
 verify(g, atLeast(4)).drawString(anyString(), argThat(isA(Float.class)), argThat(isA(Float.class)));
 }

 private double computeExpectedLeftMargin(double min, double max, double chartWidth, double rightMargin) {
 double xAxisLabelWidth = coordSystem.getXDecimalFormat().format(max).length() * 6 + 6;
 double yAxisLabelHeight = coordSystem.getYDecimalFormat().format(max).length() * 6 + 6;
 double xAxisLabelWidthWithMargin = xAxisLabelWidth + 6;
 double chartWidthWithMargins = chartWidth - rightMargin - 2 * marginOffset;
 double xAxisWidth = (chartWidthWithMargins - xAxisLabelWidthWithMargin) * (max - min) / (max - min + min);
 double leftMargin = (int)(xAxisWidth + xAxisLabelWidthWithMargin + yAxisLabelHeight + 6);
 return leftMargin;
 }

 private double computeExpectedRightMargin(double min, double max) {
 double width = (max - min) * 6;
 return width;
 }

 private double computeExpectedHeight(double min, double max) {
 double yAxisLabelHeight = coordSystem.getYDecimalFormat().format(max).length() * 6 + 6;
 return yAxisLabelHeight;
 }

 private double computeExpectedXAxisLabelHeight(double min, double max) {
 double xAxisLabelHeight = coordSystem.getXDecimalFormat().format(max).length() * 6 + 6;
 double chartHeight = coordSystem.getBounds().getHeight() - coordSystem.getTopMargin() - coordSystem.getBottomMargin();
 double xAxisLabelHeightWithMargin = xAxisLabelHeight + 6;
 double xAxisHeight = (chartHeight - xAxisLabelHeightWithMargin) * (max - min) / (max - min + min);
 double xAxisLabelHeightWithTickSpacing = xAxisLabelHeightWithMargin + ChartUtilities.calculateTickSpacing(min, max);
 return xAxisLabelHeightWithTickSpacing;
 }

 private double computeExpectedYAxisLabelWidth(double min, double max) {
 double yAxisLabelWidth = coordSystem.getYDecimalFormat().format(max).length() * 6 + 6;
 return yAxisLabelWidth;
 }
}