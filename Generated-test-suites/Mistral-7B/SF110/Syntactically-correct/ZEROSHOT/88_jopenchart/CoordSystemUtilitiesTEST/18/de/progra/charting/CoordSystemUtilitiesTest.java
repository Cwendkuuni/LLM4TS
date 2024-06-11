package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Geometrie2D;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.ChartDataModel;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 // Create a mock CoordSystem
 coordSystem = new CoordSystem() {
 @Override
 public AffineTransform getTransform(int axis) {
 return null;
 }

 @Override
 public double getXAxisUnit() {
 return 1.0;
 }

 @Override
 public double getYAxisUnit() {
 return 1.0;
 }

 @Override
 public Font getFont() {
 return new Font("Arial", Font.PLAIN, 12);
 }

 @Override
 public DecimalFormat getXDecimalFormat() {
 return new DecimalFormat("#.##");
 }

 @Override
 public DecimalFormat getYDecimalFormat() {
 return new DecimalFormat("#.##");
 }

 @Override
 public FontRenderContext getFontRenderContext() {
 return null;
 }
 };

 // Create a mock ChartDataModel
 model = new ChartDataModel();

 // Create constraints and constraints2
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();

 // Initialize the test object
 utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with positive xmin and xmax
 constraints.setMinimumColumnValue(1.0);
 constraints.setMaximumColumnValue(2.0);
 int leftMargin = utils.computeLeftMargin();
 assertEquals(26, leftMargin);

 // Test with negative xmin and xmax, and non-zero ymin and ymax
 constraints.setMinimumColumnValue(-1.5);
 constraints.setMaximumColumnValue(-0.5);
 constraints.setMinimumValue(-3.0);
 constraints.setMaximumValue(3.0);
 int leftMargin2 = utils.computeLeftMargin();
 assertEquals(37, leftMargin2);

 // Test with negative xmin and xmax, and zero ymin and ymax
 constraints.setMinimumColumnValue(-1.5);
 constraints.setMaximumColumnValue(-0.5);
 constraints.setMinimumValue(0.0);
 constraints.setMaximumValue(0.0);
 int leftMargin3 = utils.computeLeftMargin();
 assertEquals(16, leftMargin3);
 }

 @Test
 public void testComputeRightMargin() {
 // Test with positive xmin and xmax
 constraints.setMinimumColumnValue(1.0);
 constraints.setMaximumColumnValue(2.0);
 int rightMargin = utils.computeRightMargin();
 assertEquals(21, rightMargin);

 // Test with negative xmin and xmax
 constraints.setMinimumColumnValue(-1.5);
 constraints.setMaximumColumnValue(-0.5);
 int rightMargin2 = utils.computeRightMargin();
 assertEquals(16, rightMargin2);
 }

 @Test
 public void testComputeTopMargin() {
 // Test with positive ymin and ymax
 constraints.setMinimumValue(1.0);
 constraints.setMaximumValue(2.0);
 int topMargin = utils.computeTopMargin();
 assertEquals(21, topMargin);

 // Test with negative ymin and ymax
 constraints.setMinimumValue(-1.5);
 constraints.setMaximumValue(-0.5);
 int topMargin2 = utils.computeTopMargin();
 assertEquals(21, topMargin2);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test with positive ymin and ymax
 constraints.setMinimumValue(1.0);
 constraints.setMaximumValue(2.0);
 int bottomMargin = utils.computeBottomMargin();
 assertEquals(21, bottomMargin);

 // Test with negative ymin and ymax
 constraints.setMinimumValue(-1.5);
 constraints.setMaximumValue(-0.5);
 int bottomMargin2 = utils.computeBottomMargin();
 assertEquals(21, bottomMargin2);
 }

 @Test
 public void testComputeXAxisLabelWidth() {
 // Test with positive xmin and xmax
 constraints.setMinimumColumnValue(1.0);
 constraints.setMaximumColumnValue(2.0);
 int labelWidth = utils.computeXAxisLabelWidth();
 assertEquals(6, labelWidth);

 // Test with negative xmin and xmax
 constraints.setMinimumColumnValue(-1.5);
 constraints.setMaximumColumnValue(-0.5);
 int labelWidth2 = utils.computeXAxisLabelWidth();
 assertEquals(6, labelWidth2);
 }

 @Test
 public void testComputeYAxisLabelHeight() {
 // Test with positive ymin and ymax
 constraints.setMinimumValue(1.0);
 constraints.setMaximumValue(2.0);
 int labelHeight = utils.computeYAxisLabelHeight();
 assertEquals(9, labelHeight);

 // Test with negative ymin and ymax
 constraints.setMinimumValue(-1.5);
 constraints.setMaximumValue(-0.5);
 int labelHeight2 = utils.computeYAxisLabelHeight();
 assertEquals(9, labelHeight2);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Mock the draw method of Graphics2D
 Graphics2D gMock = mock(Graphics2D.class);
 when(gMock.draw(any(Shape.class))).thenReturn(true);
 when(gMock.setFont(any(Font.class))).thenReturn(true);
 when(gMock.isPaintLabels()).thenReturn(true);

 // Test with positive xmin and xmax
 utils.drawNumericalXAxisTicks(gMock);
 verify(gMock, times(11)).draw(any(Line2D.class));

 // Test with negative xmin and xmax
 constraints.setMinimumColumnValue(-1.5);
 constraints.setMaximumColumnValue(-0.5);
 utils.drawNumericalXAxisTicks(gMock);
 verify(gMock, times(11)).draw(any(Line2D.class));
 }

 @Test
 public void testDrawXAxisTicks() {
 // Mock the draw method of Graphics2D
 Graphics2D gMock = mock(Graphics2D.class);
 when(gMock.draw(any(Shape.class))).thenReturn(true);
 when(gMock.setFont(any(Font.class))).thenReturn(true);

 // Test with positive xmin and xmax
 utils.drawXAxisTicks(gMock);
 verify(gMock, times(46)).draw(any(Line2D.class));

 // Test with negative xmin and xmax
 constraints.setMinimumColumnValue(-1.5);
 constraints.setMaximumColumnValue(-0.5);
 utils.drawXAxisTicks(gMock);
 verify(gMock, times(46)).draw(any(Line2D.class));
 }

 @Test
 public void testDrawYAxisTicks() {
 // Mock the draw method of Graphics2D
 Graphics2D gMock = mock(Graphics2D.class);
 when(gMock.draw(any(Shape.class))).thenReturn(true);
 when(gMock.setFont(any(Font.class))).thenReturn(true);
 when(gMock.isPaintLabels()).thenReturn(true);

 // Test with positive ymin and ymax
 utils.drawYAxisTicks(gMock);
 verify(gMock, times(11)).draw(any(Line2D.class));

 // Test with negative ymin and ymax
 constraints.setMinimumValue(-1.5);
 constraints.setMaximumValue(-0.5);
 utils.drawYAxisTicks(gMock);
 verify(gMock, times(11)).draw(any(Line2D.class));
 }

 @Test
 public void testGetXAxisLine2D() {
 Line2D line = utils.getXAxisLine2D();
 assertEquals(coordSystem.getXAxisLine2D(), line);
 }

 @Test
 public void testGetYAxisLine2D() {
 Line2D line = utils.getYAxisLine2D();
 assertEquals(coordSystem.getYAxisLine2D(), line);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 // Test with a second y-axis defined
 constraints2.setMinimumColumnValue(1.0);
 constraints2.setMaximumColumnValue(2.0);
 Line2D line = utils.getSecondYAxisLine2D();
 assertEquals(new Line2D.Double(1.0, 2.0, 1.0, 0.0), line);

 // Test with no second y-axis defined
 constraints2.setMinimumColumnValue(null);
 constraints2.setMaximumColumnValue(null);
 line = utils.getSecondYAxisLine2D();
 assertNull(line);
 }
}