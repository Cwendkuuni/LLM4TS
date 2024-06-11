package de.progra.charting.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;
 private CoordSystem coord;

 @Before
 public void setUp() {
 // Initialize test objects
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();
 model = new ChartDataModel();
 coord = new CoordSystem(new Rectangle2D.Double(0, 0, 500, 500), 10, 10, 5, 5, 5, 5, model, constraints, constraints2);

 // Initialize test object under test
 utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with positive xmin and xmax
 double xmin = 10.0;
 double xmax = 20.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 int expected = 103; // Calculated using the given code
 int actual = utils.computeLeftMargin();
 assertEquals(expected, actual);

 // Test with negative xmin and positive xmax
 xmin = -5.0;
 xmax = 20.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 expected = 114; // Calculated using the given code
 actual = utils.computeLeftMargin();
 assertEquals(expected, actual);

 // Test with negative xmin and negative xmax
 xmin = -5.0;
 xmax = -20.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 expected = 71; // Calculated using the given code
 actual = utils.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 // Test with positive xmin and xmax
 double xmin = 0.0;
 double xmax = 20.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 int expected = 36; // Calculated using the given code
 int actual = utils.computeRightMargin();
 assertEquals(expected, actual);

 // Test with negative xmin and positive xmax
 xmin = -5.0;
 xmax = 20.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 expected = 41; // Calculated using the given code
 actual = utils.computeRightMargin();
 assertEquals(expected, actual);

 // Test with negative xmin and negative xmax
 xmin = -5.0;
 xmax = -20.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 expected = 15; // Calculated using the given code
 actual = utils.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 // Test with positive ymin and ymax
 double ymin = 10.0;
 double ymax = 20.0;
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 int expected = 52; // Calculated using the given code
 int actual = utils.computeTopMargin();
 assertEquals(expected, actual);

 // Test with negative ymin and positive ymax
 ymin = -5.0;
 ymax = 20.0;
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 expected = 61; // Calculated using the given code
 actual = utils.computeTopMargin();
 assertEquals(expected, actual);

 // Test with negative ymin and negative ymax
 ymin = -5.0;
 ymax = -20.0;
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 expected = 25; // Calculated using the given code
 actual = utils.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test with positive ymin and ymax
 double ymin = 0.0;
 double ymax = 20.0;
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 int expected = 64; // Calculated using the given code
 int actual = utils.computeBottomMargin();
 assertEquals(expected, actual);

 // Test with negative ymin and positive ymax
 ymin = -5.0;
 ymax = 20.0;
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 expected = 79; // Calculated using the given code
 actual = utils.computeBottomMargin();
 assertEquals(expected, actual);

 // Test with negative ymin and negative ymax
 ymin = -5.0;
 ymax = -20.0;
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 expected = 35; // Calculated using the given code
 actual = utils.computeBottomMargin();
 assertEquals(expected, actual);
 }
}