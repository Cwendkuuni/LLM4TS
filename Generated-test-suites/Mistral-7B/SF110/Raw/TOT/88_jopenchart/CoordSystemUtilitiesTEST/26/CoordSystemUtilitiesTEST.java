package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.XYData;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 // Setup a sample CoordSystem, ChartDataModel, and ChartDataModelConstraints
 Double minX = -10.0;
 Double maxX = 10.0;
 Double minY = -5.0;
 Double maxY = 5.0;
 Double[] xValues = {-5.0, 0.0, 5.0};
 Double[] yValues = {2.5, 3.5, 4.5};
 XYData[] data = new XYData[3];
 for (int i = 0; i < 3; i++) {
 data[i] = new XYData(xValues[i], yValues[i]);
 }
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(minX, maxX, data);
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(null, null, null);
 ChartDataModel model = new ChartDataModel(data);
 CoordSystem coordSystem = new CoordSystem(200, 400, 50, 50, 50, 50, null, null);
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test when xmin and xmax are both positive
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 assertEquals(22, utilities.computeLeftMargin());

 // Test when xmin is negative and xmax is positive
 xmin = -5.0;
 xmax = 5.0;
 assertEquals(41, utilities.computeLeftMargin());

 // Test when both xmin and xmax are negative
 xmin = -5.0;
 xmax = -2.0;
 assertEquals(39, utilities.computeLeftMargin());

 // Test when xmin is zero and xmax is positive
 xmin = 0.0;
 xmax = 5.0;
 assertEquals(22, utilities.computeLeftMargin());

 // Error case: xmin and xmax are both zero
 xmin = 0.0;
 xmax = 0.0;
 assertThrows(IllegalArgumentException.class, () -> utilities.computeLeftMargin());
 }

 @Test
 public void testComputeRightMargin() {
 // Test when xmin and xmax are both positive
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 assertEquals(21, utilities.computeRightMargin());

 // Test when xmin is negative and xmax is positive
 xmin = -5.0;
 xmax = 5.0;
 assertEquals(41, utilities.computeRightMargin());

 // Test when both xmin and xmax are negative
 xmin = -5.0;
 xmax = -2.0;
 assertEquals(39, utilities.computeRightMargin());

 // Test when xmin is zero and xmax is positive
 xmin = 0.0;
 xmax = 5.0;
 assertEquals(21, utilities.computeRightMargin());

 // Test when xmin is negative and xmax is zero
 xmin = -5.0;
 xmax = 0.0;
 assertEquals(39, utilities.computeRightMargin());
 }

 @Test
 public void testComputeTopMargin() {
 // Test when ymin and ymax are both positive
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 assertEquals(16, utilities.computeTopMargin());

 // Test when ymin is negative and ymax is positive
 ymin = -5.0;
 ymax = 5.0;
 assertEquals(31, utilities.computeTopMargin());

 // Test when both ymin and ymax are negative
 ymin = -5.0;
 ymax = -2.0;
 assertEquals(33, utilities.computeTopMargin());

 // Test when ymin is zero and ymax is positive
 ymin = 0.0;
 ymax = 5.0;
 assertEquals(16, utilities.computeTopMargin());
 }

 @Test
 public void testComputeBottomMargin() {
 // Test when ymin and ymax are both positive
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 assertEquals(20, utilities.computeBottomMargin());

 // Test when ymin is negative and ymax is positive
 ymin = -5.0;
 ymax = 5.0;
 assertEquals(40, utilities.computeBottomMargin());

 // Test when both ymin and ymax are negative
 ymin = -5.0;
 ymax = -2.0;
 assertEquals(38, utilities.computeBottomMargin());

 // Test when ymin is zero and ymax is positive
 ymin = 0.0;
 ymax = 5.0;
 assertEquals(20, utilities.computeBottomMargin());
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test when there are no ticks
 assertEquals(0, utilities.computeXAxisLabelHeight());

 // Test when there is one tick
 double minX = -5.0;
 double maxX = 5.0;
 double tick = 2.0;
 assertEquals(12, utilities.computeXAxisLabelHeight());

 // Test when there are multiple ticks
 minX = -5.0;
 maxX = 5.0;
 tick = 1.0;
 assertEquals(24, utilities.computeXAxisLabelHeight());
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test when there are no ticks
 assertEquals(0, utilities.computeYAxisLabelWidth());

 // Test when there is one tick
 double minY = -5.0;
 double maxY = 5.0;
 double tick = 2.0;
 assertEquals(6, utilities.computeYAxisLabelWidth());

 // Test when there are multiple ticks
 minY = -5.0;
 maxY = 5.0;
 tick = 1.0;
 assertEquals(12, utilities.computeYAxisLabelWidth());
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Setup a sample Graphics2D object
 Graphics2D g = null;
 try {
 g = (Graphics2D)coordSystem.createGraphics();
 } catch (Exception e) {
 // Handle exception
 }

 // Test when there is only one tick
 double minX = -5.0;
 double maxX = 5.0;
 double tick = 2.0;
 utilities.drawNumericalXAxisTicks(g);

 // Test when there are multiple ticks
 minX = -5.0;
 maxX = 5.0;
 tick = 1.0;
 utilities.drawNumericalXAxisTicks(g);
 }

 @Test
 public void testDrawXAxisTicks() {
 // Setup a sample Graphics2D object
 Graphics2D g = null;
 try {
 g = (Graphics2D)coordSystem.createGraphics();
 } catch (Exception e) {
 // Handle exception
 }

 // Test when there is only one tick
 double minX = -5.0;
 double maxX = 5.0;
 double tick = 2.0;
 utilities.drawXAxisTicks(g);

 // Test when there are multiple ticks
 minX = -5.0;
 maxX = 5.0;
 tick = 1.0;
 utilities.drawXAxisTicks(g);
 }

 @Test
 public void testDrawYAxisTicks() {
 // Setup a sample Graphics2D object
 Graphics2D g = null;
 try {
 g = (Graphics2D)coordSystem.createGraphics();
 } catch (Exception e) {
 // Handle exception
 }

 // Test when there is only one tick
 double minY = -5.0;
 double maxY = 5.0;
 double tick = 2.0;
 utilities.drawYAxisTicks(g);

 // Test when there are multiple ticks
 minY = -5.0;
 maxY = 5.0;
 tick = 1.0;
 utilities.drawYAxisTicks(g);
 }

 @Test
 public void testGetXAxisLine2D() {
 Line2D line = utilities.getXAxisLine2D();
 assertEquals(coordSystem.getWidth(), line.getX1(), 0.0);
 assertEquals(coordSystem.getHeight() / 2.0, line.getY1(), 0.0);
 assertEquals(0.0, line.getX2(), 0.0);
 assertEquals(coordSystem.getHeight() / 2.0, line.getY2(), 0.0);
 }

 @Test
 public void testGetYAxisLine2D() {
 Line2D line = utilities.getYAxisLine2D();
 assertEquals(coordSystem.getWidth() / 2.0, line.getX1(), 0.0);
 assertEquals(0.0, line.getY1(), 0.0);
 assertEquals(coordSystem.getWidth() / 2.0, line.getX2(), 0.0);
 assertEquals(coordSystem.getHeight(), line.getY2(), 0.0);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 // Setup a sample CoordSystem with a second Y-Axis
 CoordSystem coordSystem2 = new CoordSystem(200, 400, 50, 50, 50, 50, null, null, null, null);
 Line2D line = utilities.getSecondYAxisLine2D();
 assertEquals(coordSystem.getWidth(), line.getX1(), 0.0);
 assertEquals(coordSystem.getHeight(), line.getY1(), 0.0);
 assertEquals(coordSystem.getWidth(), line.getX2(), 0.0);
 assertEquals(coordSystem.getHeight() - 100, line.getY2(), 0.0);
 }
}