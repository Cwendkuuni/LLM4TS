package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataColumn;
import de.progra.charting.model.DataSet;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel dataModel;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 // Initialize test objects
 DecimalFormat xDecimalFormat = new DecimalFormat("#.##");
 DecimalFormat yDecimalFormat = new DecimalFormat("#");
 Format xAxisLabelFormat = xDecimalFormat;
 Format yAxisLabelFormat = yDecimalFormat;
 coordSystem = new CoordSystem(new java.awt.Dimension(600, 600), xAxisLabelFormat, yAxisLabelFormat);
 dataModel = new ChartDataModel();
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test basic functionality
 double xmin = 1.0;
 double xmax = 2.0;
 int expected = 100; // arbitrary expected value
 int actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);

 // Test edge cases
 xmin = 0.0;
 xmax = 1.0;
 expected = 120; // arbitrary expected value for negative xmin and positive xmax
 actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);

 xmin = -1.0;
 xmax = 0.0;
 expected = 80; // arbitrary expected value for negative xmin and negative xmax
 actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);

 xmin = 0.0;
 xmax = 0.0;
 expected = 15; // arbitrary expected value for xmin = xmax = 0
 actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);

 xmin = Double.MIN_VALUE;
 xmax = Double.MAX_VALUE;
 expected = 20; // arbitrary expected value for extreme values of xmin and xmax
 actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 // Test basic functionality
 int expected = 30; // arbitrary expected value
 int actual = utilities.computeRightMargin();
 assertEquals(expected, actual);

 // Test edge cases
 expected = 10; // arbitrary expected value for small width
 actual = utilities.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 // Test basic functionality
 int expected = 30; // arbitrary expected value
 int actual = utilities.computeTopMargin();
 assertEquals(expected, actual);

 // Test edge cases
 expected = 10; // arbitrary expected value for small height
 actual = utilities.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test basic functionality
 double ymin = -1.0;
 double ymax = 1.0;
 int expected = 100; // arbitrary expected value
 int actual = utilities.computeBottomMargin();
 assertEquals(expected, actual);

 // Test edge cases
 ymin = 0.0;
 ymax = 1.0;
 expected = 120; // arbitrary expected value for negative ymin and positive ymax
 actual = utilities.computeBottomMargin();
 assertEquals(expected, actual);

 ymin = -1.0;
 ymax = 0.0;
 expected = 80; // arbitrary expected value for negative ymin and negative ymax
 actual = utilities.computeBottomMargin();
 assertEquals(expected, actual);

 ymin = 0.0;
 ymax = 0.0;
 expected = 15; // arbitrary expected value for ymin = ymax = 0
 actual = utilities.computeBottomMargin();
 assertEquals(expected, actual);

 ymin = Double.MIN_VALUE;
 ymax = Double.MAX_VALUE;
 expected = 20; // arbitrary expected value for extreme values of ymin and ymax
 actual = utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithInvalidCoordSystem() {
 utilities = new CoordSystemUtilities(null, constraints, constraints2, dataModel);
 utilities.computeLeftMargin();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithInvalidConstraints() {
 utilities = new CoordSystemUtilities(coordSystem, null, constraints2, dataModel);
 utilities.computeLeftMargin();
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithInvalidDataModel() {
 utilities = new CoordSystemUtilities(coordSystem, constraints, null, dataModel);
 utilities.computeLeftMargin();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test basic functionality
 double min = 0.0;
 double max = 10.0;
 double tick = 1.0;
 int expected = 20; // arbitrary expected value
 int actual = utilities.computeXAxisLabelHeight();
 assertEquals(expected, actual);

 // Test edge cases
 min = Double.MIN_VALUE;
 max = Double.MAX_VALUE;
 tick = 0.1;
 expected = 50; // arbitrary expected value for extreme values of min, max, and tick
 actual = utilities.computeXAxisLabelHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test basic functionality
 double min = 0.0;
 double max = 10.0;
 int expected = 50; // arbitrary expected value
 int actual = utilities.computeYAxisLabelWidth();
 assertEquals(expected, actual);

 // Test edge cases
 min = Double.MIN_VALUE;
 max = Double.MAX_VALUE;
 expected = 100; // arbitrary expected value for extreme values of min and max
 actual = utilities.computeYAxisLabelWidth();
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test basic functionality
 Graphics2D g = null;
 utilities.drawNumericalXAxisTicks(g);
 assertNull(g);

 // Test edge cases
 g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(mock(Font.class));
 when(g.isPaintLabels()).thenReturn(true);
 utilities.drawNumericalXAxisTicks(g);
 verify(g, times(2)).drawString(anyString(), anyFloat(), anyFloat());
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test basic functionality
 Graphics2D g = null;
 utilities.drawXAxisTicks(g);
 assertNull(g);

 // Test edge cases
 g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(mock(Font.class));
 DataColumn column = new DataColumn("Test", new DataSet(Arrays.asList(1.0, 2.0, 3.0)));
 constraints.setColumnValues(Arrays.asList(column));
 utilities.drawXAxisTicks(g);
 verify(g, times(3)).drawString(anyString(), anyFloat(), anyFloat());
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test basic functionality
 Graphics2D g = null;
 utilities.drawYAxisTicks(g);
 assertNull(g);

 // Test edge cases
 g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(mock(Font.class));
 when(g.isPaintLabels()).thenReturn(true);
 utilities.drawYAxisTicks(g);
 verify(g, times(2)).drawLine(anyDouble(), anyDouble(), anyDouble(), anyDouble());
 }
}