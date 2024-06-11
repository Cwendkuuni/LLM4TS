package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private DecimalFormat xFormat;
 private DecimalFormat yFormat;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private CoordSystem coord;

 @Before
 public void setUp() {
 xFormat = new DecimalFormat("#.##");
 yFormat = new DecimalFormat("#.##");
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints(1.0, 10.0, 0.0, 20.0);
 constraints2 = new ChartDataModelConstraints(-5.0, 5.0, 0.0, 10.0);
 coord = new CoordSystem(100, 100, 100, 100, 10, 10);
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin_PositiveValues() {
 double xmin = 1.0;
 double xmax = 10.0;
 double expected = 51; // 50 + 1 for margin
 int actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_NegativeValues() throws ParseException {
 double xmin = -5.0;
 double xmax = 5.0;
 double expected = 29; // 28 + 1 for margin
 int actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMargin_InvalidConstraints() {
 constraints = new ChartDataModelConstraints(0.0, 0.0, 0.0, 0.0);
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 utilities.computeLeftMargin();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testComputeRightMargin() {
 double xmin = 1.0;
 double xmax = 10.0;
 int expected = 31; // 30 + 1 for margin
 int actual = utilities.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 double ymin = 0.0;
 double ymax = 20.0;
 int expected = 57; // 55 + 1 for margin + text height
 int actual = utilities.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 double ymin = -5.0;
 double ymax = 5.0;
 double xmin = 1.0;
 double xmax = 10.0;
 int expected = 57; // 55 + 1 for margin + text height
 int actual = utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_InvalidConstraints() {
 constraints = new ChartDataModelConstraints(0.0, 0.0, 0.0, 0.0);
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 utilities.computeBottomMargin();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double min = 1.0;
 double max = 10.0;
 int expected = 32;
 int actual = utilities.computeXAxisLabelHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double min = 0.0;
 double max = 20.0;
 int expected = 42;
 int actual = utilities.computeYAxisLabelWidth();
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawNumericalXAxisTicks(g);
 // Verify that all ticks are drawn
 // and labels are drawn only if paintLabels is true
 verify(g, times(10)).draw(any(Line2D.class));
 verify(g, times(5)).drawString(anyString(), anyFloat(), anyFloat());
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawXAxisTicks(g);
 // Verify that all ticks are drawn
 // and labels are drawn only if paintLabels is true
 verify(g, times(9)).draw(any(Line2D.class));
 verify(g, times(4)).drawString(eq("Column 1"), anyFloat(), anyFloat());
 verify(g, times(4)).drawString(eq("Column 2"), anyFloat(), anyFloat());
 verify(g, times(1)).drawString(eq("Column 3"), anyFloat(), anyFloat());
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawYAxisTicks(g);
 // Verify that all ticks are drawn
 // and labels are drawn only if paintLabels is true
 verify(g, times(10)).draw(any(Line2D.class));
 verify(g, times(9)).drawString(anyString(), anyFloat(), anyFloat());
 }
}