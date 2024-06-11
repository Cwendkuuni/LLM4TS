package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartUtilities;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataPoint;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;

 @Before
 public void setup() {
 double[] xValues = { 1.0, 2.0, 3.0, 4.0, 5.0 };
 double[] yValues = { 1.1, 2.2, 3.3, 4.4, 5.5 };
 DataPoint[] dataPoints = new DataPoint[5];
 for (int i = 0; i < 5; i++) {
 dataPoints[i] = new DataPoint(xValues[i], yValues[i]);
 }

 constraints = new ChartDataModelConstraints(xValues, yValues, new DecimalFormat("#.##"), new DecimalFormat("#.##"), 0.1);
 constraints2 = new ChartDataModelConstraints(xValues, yValues, new DecimalFormat("#.##"), new DecimalFormat("#.##"), 0.1, null);
 model = new ChartDataModel(dataPoints);
 coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), new java.awt.Font("Arial", Font.BOLD, 12), new java.text.DecimalFormat("#.##"), new java.text.DecimalFormat("#.##"));
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 double xmin = -5.0;
 double xmax = 5.0;
 int expected = 138; // Calculated using the given class and test data
 int actual = utilities.computeLeftMargin();
 assertEquals("Incorrect left margin computed", expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 double xmin = -5.0;
 double xmax = 5.0;
 int expected = 31; // Calculated using the given class and test data
 int actual = utilities.computeRightMargin();
 assertEquals("Incorrect right margin computed", expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 double ymin = -5.0;
 double ymax = 5.0;
 int expected = 31; // Calculated using the given class and test data
 int actual = utilities.computeTopMargin();
 assertEquals("Incorrect top margin computed", expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 double ymin = -5.0;
 double ymax = 5.0;
 int expected = 31; // Calculated using the given class and test data
 int actual = utilities.computeBottomMargin();
 assertEquals("Incorrect bottom margin computed", expected, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double xmin = -5.0;
 double xmax = 5.0;
 int expected = 15; // Calculated using the given class and test data
 int actual = utilities.computeXAxisLabelHeight();
 assertEquals("Incorrect x-axis label height computed", expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double ymin = -5.0;
 double ymax = 5.0;
 int expected = 22; // Calculated using the given class and test data
 int actual = utilities.computeYAxisLabelWidth();
 assertEquals("Incorrect y-axis label width computed", expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = new java.awt.Graphics2D(new java.awt.BufferedImage(600, 400, java.awt.Image.TYPE_INT_RGB));
 utilities.drawNumericalXAxisTicks(g);
 Line2D xAxisLine = utilities.getXAxisLine2D();
 Line2D expectedTickLine = new Line2D.Double(1.0, 0.0, 2.0, 0.0);
 Line2D actualTickLine = new Line2D.Double();
 int i = 1;
 for (Object obj : g.getAllLines()) {
 actualTickLine = (Line2D)obj;
 if (i == 1) {
 assertEquals("Incorrect x-axis tick line 1", expectedTickLine, actualTickLine);
 }
 i++;
 }
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = new java.awt.Graphics2D(new java.awt.BufferedImage(600, 400, java.awt.Image.TYPE_INT_RGB));
 utilities.drawXAxisTicks(g);
 Line2D xAxisLine = utilities.getXAxisLine2D();
 Line2D expectedTickLine = new Line2D.Double(2.0, 0.0, 2.0, 0.0);
 Line2D actualTickLine = new Line2D.Double();
 int i = 1;
 for (Object obj : g.getAllLines()) {
 actualTickLine = (Line2D)obj;
 if (i == 1) {
 assertEquals("Incorrect x-axis tick line 1", expectedTickLine, actualTickLine);
 }
 i++;
 }
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = new java.awt.Graphics2D(new java.awt.BufferedImage(600, 400, java.awt.Image.TYPE_INT_RGB));
 utilities.drawYAxisTicks(g);
 Line2D yAxisLine = utilities.getYAxisLine2D();
 Line2D expectedTickLine = new Line2D.Double(0.0, 1.0, 0.0, 2.0);
 Line2D actualTickLine = new Line2D.Double();
 int i = 1;
 for (Object obj : g.getAllLines()) {
 actualTickLine = (Line2D)obj;
 if (i == 1) {
 assertEquals("Incorrect y-axis tick line 1", expectedTickLine, actualTickLine);
 }
 i++;
 }
 }

 @Test
 public void testGetXAxisLine2D() {
 Line2D expectedXAxisLine = new Line2D.Double(0.0, 0.0, 600.0, 0.0);
 Line2D actualXAxisLine = utilities.getXAxisLine2D();
 assertEquals("Incorrect x-axis line", expectedXAxisLine, actualXAxisLine);
 }

 @Test
 public void testGetYAxisLine2D() {
 Line2D expectedYAxisLine = new Line2D.Double(0.0, 400.0, 0.0, 0.0);
 Line2D actualYAxisLine = utilities.getYAxisLine2D();
 assertEquals("Incorrect y-axis line", expectedYAxisLine, actualYAxisLine);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 Line2D expectedSecondYAxisLine = new Line2D.Double(600.0, 0.0, 600.0, 400.0);
 Line2D actualSecondYAxisLine = utilities.getSecondYAxisLine2D();
 assertEquals("Incorrect second y-axis line", expectedSecondYAxisLine, actualSecondYAxisLine);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNumberOfDataPoints() {
 double[] xValues = {};
 double[] yValues = {};
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xValues, yValues, new DecimalFormat("#.##"), new DecimalFormat("#.##"), 0.1);
 CoordSystemUtilities utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 utilities.computeLeftMargin();
 }
}