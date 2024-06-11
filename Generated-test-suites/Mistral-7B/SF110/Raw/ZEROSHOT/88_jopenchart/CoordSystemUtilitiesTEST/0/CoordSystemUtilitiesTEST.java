package de.progra.charting.test;

import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;

import org.junit.Test;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;

 @Test
 public void testComputeLeftMargin() {
  ChartDataModelConstraints constraints = new ChartDataModelConstraints();
  ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
  ChartDataModel model = new ChartDataModel();
  CoordSystem coord = new CoordSystem(new java.awt.geom.Rectangle2D.Double(100, 100, 400, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), null, 5, 5, null, null);

  utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

  double xmin = -10.0;
  double xmax = 10.0;

  constraints.setMinimumColumnValue(xmin);
  constraints.setMaximumColumnValue(xmax);

  int expected = 72; // hardcoded value, assuming the test font's descent is 12 and ascent is 15
  int actual = utilities.computeLeftMargin();

  assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_XValuesBothZero() {
  ChartDataModelConstraints constraints = new ChartDataModelConstraints();
  ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
  ChartDataModel model = new ChartDataModel();
  CoordSystem coord = new CoordSystem(new java.awt.geom.Rectangle2D.Double(100, 100, 400, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), null, 5, 5, null, null);

  utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

  constraints.setMinimumColumnValue(0.0);
  constraints.setMaximumColumnValue(0.0);

  int expected = 20;
  int actual = utilities.computeLeftMargin();

  assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
  ChartDataModelConstraints constraints = new ChartDataModelConstraints();
  ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
  ChartDataModel model = new ChartDataModel();
  CoordSystem coord = new CoordSystem(new java.awt.geom.Rectangle2D.Double(100, 100, 400, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), null, 5, 5, null, null);

  utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

  int expected = 15;
  int actual = utilities.computeRightMargin();

  assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
  ChartDataModelConstraints constraints = new ChartDataModelConstraints();
  ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
  ChartDataModel model = new ChartDataModel();
  CoordSystem coord = new CoordSystem(new java.awt.geom.Rectangle2D.Double(100, 100, 400, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), null, 5, 5, null, null);

  utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

  int expected = 15;
  int actual = utilities.computeTopMargin();

  assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
  ChartDataModelConstraints constraints = new ChartDataModelConstraints();
  ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
  ChartDataModel model = new ChartDataModel();
  CoordSystem coord = new CoordSystem(new java.awt.geom.Rectangle2D.Double(100, 100, 400, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), null, 5, 5, null, null);

  utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

  double minValue = -10.0;
  double maxValue = 10.0;

  constraints.setMinimumValue(minValue);
  constraints.setMaximumValue(maxValue);

  int expected = 72; // hardcoded value, assuming the test font's descent is 12 and ascent is 15
  int actual = utilities.computeBottomMargin();

  assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_YValuesBothZero() {
  ChartDataModelConstraints constraints = new ChartDataModelConstraints();
  ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
  ChartDataModel model = new ChartDataModel();
  CoordSystem coord = new CoordSystem(new java.awt.geom.Rectangle2D.Double(100, 100, 400, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), null, 5, 5, null, null);

  utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

  constraints.setMinimumValue(0.0);
  constraints.setMaximumValue(0.0);

  int expected = 20;
  int actual = utilities.computeBottomMargin();

  assertEquals(expected, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
  ChartDataModelConstraints constraints = new ChartDataModelConstraints();
  ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
  ChartDataModel model = new ChartDataModel();
  CoordSystem coord = new CoordSystem(new java.awt.geom.Rectangle2D.Double(100, 100, 400, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), null, 5, 5, null, null);

  utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

  double min = -10.0;
  double max = 10.0;
  double tick = 1.0;

  constraints.setMinimumColumnValue(min);
  constraints.setMaximumColumnValue(max);

  int expected = 20; // hardcoded value for testing
  int actual = utilities.computeXAxisLabelHeight();

  // Note: this test is not perfect as it depends on the font used in the system
  // To make it more robust, use a mock Font and its FontRenderContext for testing
  assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
  ChartDataModelConstraints constraints = new ChartDataModelConstraints();
  ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
  ChartDataModel model = new ChartDataModel();
  CoordSystem coord = new CoordSystem(new java.awt.geom.Rectangle2D.Double(100, 100, 400, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), null, 5, 5, null, null);

  utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

  double min = -10.0;
  double max = 10.0;
  double tick = 1.0;

  constraints.setMinimumValue(min);
  constraints.setMaximumValue(max);

  int expected = 20; // hardcoded value for testing
  int actual = utilities.computeYAxisLabelWidth();

  // Note: this test is not perfect as it depends on the font used in the system
  // To make it more robust, use a mock Font and its FontRenderContext for testing
  assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
  // Test case to be implemented
 }

 @Test
 public void testDrawXAxisTicks() {
  ChartDataModelConstraints constraints = new ChartDataModelConstraints();
  ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
  ChartDataModel model = new ChartDataModel();
  CoordSystem coord = new CoordSystem(new java.awt.geom.Rectangle2D.Double(100, 100, 400, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), null, 5, 5, null, null);

  Utils.setGraphics2DForTesting(new StubGraphics2D());

  utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

  int min = 0;
  int max = 5;

  constraints.setMinimumColumnValue(min);
  constraints.setMaximumColumnValue(max);

  utilities.drawXAxisTicks(Utils.getGraphics2DForTesting());

  // Test if the number of ticks is correct
  StubGraphics2D graphics2d = (StubGraphics2D) Utils.getGraphics2DForTesting();
  int expectedTicks = max - min + 1;
  int actualTicks = graphics2d.getDrawnLines().size();

  assertEquals(expectedTicks, actualTicks);
 }

 @Test
 public void testDrawYAxisTicks() {
  ChartDataModelConstraints constraints = new ChartDataModelConstraints();
  ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
  ChartDataModel model = new ChartDataModel();
  CoordSystem coord = new CoordSystem(new java.awt.geom.Rectangle2D.Double(100, 100, 400, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), null, 5, 5, null, null);

  Utils.setGraphics2DForTesting(new StubGraphics2D());

  utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

  double minValue = -10.0;
  double maxValue = 10.0;
  double tick = 1.0;

  constraints.setMinimumValue(minValue);
  constraints.setMaximumValue(maxValue);

  utilities.drawYAxisTicks(Utils.getGraphics2DForTesting());

  // Test if the number of ticks is correct
  StubGraphics2D graphics2d = (StubGraphics2D) Utils.getGraphics2DForTesting();
  int expectedTicks = (int)Math.ceil((maxValue - minValue) / tick) + 1;
  int actualTicks = graphics2d.getDrawnLines().size();

  assertEquals(expectedTicks, actualTicks);
 }
}

class StubGraphics2D extends Graphics2D {
 private final java.util.ArrayList<Line2D> drawnLines = new ArrayList<>();

 @Override
 public void draw(Line2D line) {
  drawnLines.add(line);
 }
}