package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.ChartDataModelConstraints;
import de.progra.charting.model.CoordSystem;
import de.progra.charting.model.CoordSystemConstraints;
import de.progra.charting.util.ChartUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;

 @Before
 public void setUp() {
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();

 double[] values = { -10.5, -5.2, 0.1, 5.3, 10.5 };
 double[] values2 = { 15.3, 20.7, 25.8, 30.9, 35.6 };
 constraints.setData(values);
 constraints2.setData(values2);
 CoordSystem coord = new CoordSystem(100, 100, 50, 50, 50, 50, 5, 5, constraints, constraints2, model);
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 double expected = 164;
 int actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 double expected = 45;
 int actual = utilities.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 double expected = 45;
 int actual = utilities.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 double expected = 164;
 int actual = utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double expected = 21;
 int actual = utilities.computeXAxisLabelHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double expected = 34;
 int actual = utilities.computeYAxisLabelWidth();
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // TODO(developer): implement test for this method
 }

 @Test
 public void testDrawXAxisTicks() {
 // TODO(developer): implement test for this method
 }

 @Test
 public void testDrawYAxisTicks() {
 // TODO(developer): implement test for this method
 }

 @Test
 public void testGetXAxisLine2D() {
 Line2D expected = new Line2D.Double(37.0, 50.0, 133.0, 50.0);
 Line2D actual = utilities.getXAxisLine2D();
 assertEquals(expected.getX1(), actual.getX1(), 0.0);
 assertEquals(expected.getX2(), actual.getX2(), 0.0);
 assertEquals(expected.getY1(), actual.getY1(), 0.0);
 assertEquals(expected.getY2(), actual.getY2(), 0.0);
 }

 @Test
 public void testGetYAxisLine2D() {
 Line2D expected = new Line2D.Double(50.0, 133.0, 50.0, 37.0);
 Line2D actual = utilities.getYAxisLine2D();
 assertEquals(expected.getX1(), actual.getX1(), 0.0);
 assertEquals(expected.getX2(), actual.getX2(), 0.0);
 assertEquals(expected.getY1(), actual.getY1(), 0.0);
 assertEquals(expected.getY2(), actual.getY2(), 0.0);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 Line2D expected = new Line2D.Double(133.0, 50.0, 133.0, 100.0);
 Line2D actual = utilities.getSecondYAxisLine2D();
 assertEquals(expected.getX1(), actual.getX1(), 0.0);
 assertEquals(expected.getX2(), actual.getX2(), 0.0);
 assertEquals(expected.getY1(), actual.getY1(), 0.0);
 assertEquals(expected.getY2(), actual.getY2(), 0.0);
 }

}