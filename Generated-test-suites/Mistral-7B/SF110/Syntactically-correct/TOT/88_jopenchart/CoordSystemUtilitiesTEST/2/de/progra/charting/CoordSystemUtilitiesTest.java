package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.XAxisLabelFormatter;
import de.progra.charting.model.YAxisLabelFormatter;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel chartDataModel;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 double[] data = { 1.0, 2.0, 3.0, 4.0, 5.0 };
 String[] columnNames = { "Label1", "Label2", "Label3", "Label4", "Label5" };
 chartDataModel = new ChartDataModel(data, columnNames);
 constraints = new ChartDataModelConstraints(chartDataModel, null, null);
 constraints2 = new ChartDataModelConstraints(chartDataModel, null, null);
 coordSystem = new CoordSystem(100, 400, 50, 50, 50, 50, 5, 5, 5, 5, new XAxisLabelFormatter(), new YAxisLabelFormatter());
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);
 }

 @Test
 public void testComputeLeftMargin() {
 double[] xValues = { -5.0, -3.0, 0.0, 2.0, 5.0 };
 DecimalFormat df = new DecimalFormat("#.##");
 for (double xValue : xValues) {
 constraints.setMinimumColumnValue(xValue);
 constraints.setMaximumColumnValue(Math.abs(xValue) + 1.0);
 int result = utilities.computeLeftMargin();
 int expected = calculateExpectedLeftMargin(xValue);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testComputeRightMargin() {
 double[] xValues = { -5.0, -3.0, 0.0, 2.0, 5.0 };
 for (double xValue : xValues) {
 constraints.setMinimumColumnValue(xValue);
 constraints.setMaximumColumnValue(Math.abs(xValue) + 1.0);
 int result = utilities.computeRightMargin();
 int expected = calculateExpectedRightMargin(xValue);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testComputeTopMargin() {
 double[] yValues = { -5.0, -3.0, 0.0, 2.0, 5.0 };
 DecimalFormat df = new DecimalFormat("#.##");
 for (double yValue : yValues) {
 constraints.setMinimumValue(yValue);
 constraints.setMaximumValue(Math.abs(yValue) + 1.0);
 int result = utilities.computeTopMargin();
 int expected = calculateExpectedTopMargin(yValue);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testComputeBottomMargin() {
 double[] yValues = { -5.0, -3.0, 0.0, 2.0, 5.0 };
 for (double yValue : yValues) {
 constraints.setMinimumValue(yValue);
 constraints.setMaximumValue(Math.abs(yValue) + 1.0);
 int result = utilities.computeBottomMargin();
 int expected = calculateExpectedBottomMargin(yValue);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double min = -10.0;
 double max = 10.0;
 int result = utilities.computeXAxisLabelHeight();
 int expected = calculateExpectedXAxisLabelHeight(min, max);
 assertEquals(expected, result);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double min = -10.0;
 double max = 10.0;
 int result = utilities.computeYAxisLabelWidth();
 int expected = calculateExpectedYAxisLabelWidth(min, max);
 assertEquals(expected, result);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = null; // add a mock or stub implementation here
 utilities.drawNumericalXAxisTicks(g);
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = null; // add a mock or stub implementation here
 utilities.drawXAxisTicks(g);
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = null; // add a mock or stub implementation here
 utilities.drawYAxisTicks(g);
 }

 private int calculateExpectedLeftMargin(double xValue) {
 // implementation of expected calculation logic here
 return 0;
 }

 private int calculateExpectedRightMargin(double xValue) {
 // implementation of expected calculation logic here
 return 0;
 }

 private int calculateExpectedTopMargin(double yValue) {
 // implementation of expected calculation logic here
 return 0;
 }

 private int calculateExpectedBottomMargin(double yValue) {
 // implementation of expected calculation logic here
 return 0;
 }

 private int calculateExpectedXAxisLabelHeight(double min, double max) {
 // implementation of expected calculation logic here
 return 0;
 }

 private int calculateExpectedYAxisLabelWidth(double min, double max) {
 // implementation of expected calculation logic here
 return 0;
 }
}