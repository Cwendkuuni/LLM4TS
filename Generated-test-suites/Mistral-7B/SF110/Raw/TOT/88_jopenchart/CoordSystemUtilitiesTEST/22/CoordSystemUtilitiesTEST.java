package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

 private CoordSystem coordSystem;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;

 @Before
 public void setUp() {
 coordSystem = new CoordSystem(500, 500, 10, 10);
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();
 model = new ChartDataModel();
 }

 @Test
 public void testComputeLeftMargin() {
 // Test case for positive xmin and xmax
 constraints.setMinimumColumnValue(10.0);
 constraints.setMaximumColumnValue(50.0);
 CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int leftMargin = util.computeLeftMargin();
 assertEquals(34, leftMargin); // expected value based on given xmin and xmax

 // Test case for negative xmin and positive xmax
 constraints.setMinimumColumnValue(-10.0);
 constraints.setMaximumColumnValue(50.0);
 util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 leftMargin = util.computeLeftMargin();
 assertEquals(122, leftMargin); // expected value based on negative xmin and positive xmax

 // Test case for both xmin and xmax being zero
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(0.0);
 util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 leftMargin = util.computeLeftMargin();
 assertEquals(20, leftMargin); // default value when xmin and xmax are zero
 }

 @Test
 public void testComputeRightMargin() {
 // Test case for positive xmin and xmax
 constraints.setMinimumColumnValue(10.0);
 constraints.setMaximumColumnValue(50.0);
 CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int rightMargin = util.computeRightMargin();
 assertEquals(27, rightMargin); // expected value based on given xmin and xmax

 // Test case for negative xmin and positive xmax
 constraints.setMinimumColumnValue(-10.0);
 constraints.setMaximumColumnValue(50.0);
 util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 rightMargin = util.computeRightMargin();
 assertEquals(32, rightMargin); // expected value based on negative xmin and positive xmax
 }

 @Test
 public void testComputeTopMargin() {
 // Test case for positive ymin and ymax
 constraints.setMinimumValue(new Double(10.0));
 constraints.setMaximumValue(new Double(50.0));
 CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int topMargin = util.computeTopMargin();
 assertEquals(30, topMargin); // expected value based on given ymin and ymax

 // Test case for negative ymin and positive ymax
 constraints.setMinimumValue(new Double(-10.0));
 constraints.setMaximumValue(new Double(50.0));
 util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 topMargin = util.computeTopMargin();
 assertEquals(111, topMargin); // expected value based on negative ymin and positive ymax
 }

 @Test
 public void testComputeBottomMargin() {
 // Test case for positive ymin and ymax
 constraints.setMinimumValue(new Double(10.0));
 constraints.setMaximumValue(new Double(50.0));
 CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int bottomMargin = util.computeBottomMargin();
 assertEquals(30, bottomMargin); // expected value based on given ymin and ymax

 // Test case for negative ymin and positive ymax
 constraints.setMinimumValue(new Double(-10.0));
 constraints.setMaximumValue(new Double(50.0));
 util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 bottomMargin = util.computeBottomMargin();
 assertEquals(111, bottomMargin); // expected value based on negative ymin and positive ymax

 // Test case for ymin and ymax being the same
 constraints.setMinimumValue(new Double(50.0));
 constraints.setMaximumValue(new Double(50.0));
 util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 bottomMargin = util.computeBottomMargin();
 assertEquals(20, bottomMargin); // default value when
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test case for a small range
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(10.0);
 CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int labelHeight = util.computeXAxisLabelHeight();
 assertEquals(12, labelHeight); // expected value based on given xmin and xmax

 // Test case for a large range
 constraints.setMinimumColumnValue(-50.0);
 constraints.setMaximumColumnValue(50.0);
 util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 labelHeight = util.computeXAxisLabelHeight();
 assertEquals(113, labelHeight); // expected value based on given xmin and xmax
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test case for a small range
 constraints.setMinimumValue(new Double(0.0));
 constraints.setMaximumValue(new Double(10.0));
 CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 int labelWidth = util.computeYAxisLabelWidth();
 assertEquals(15, labelWidth); // expected value based on given ymin and ymax

 // Test case for a large range
 constraints.setMinimumValue(new Double(-50.0));
 constraints.setMaximumValue(new Double(50.0));
 util = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 labelWidth = util.computeYAxisLabelWidth();
 assertEquals(121, labelWidth); // expected value based on given ymin and ymax
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullCoordSystem() {
 constraints.setMinimumColumnValue(10.0);
 constraints.setMaximumColumnValue(50.0);
 try {
 CoordSystemUtilities util = new CoordSystemUtilities(null, constraints, constraints2, model);
 util.computeLeftMargin();
 fail("Expected IllegalArgumentException when CoordSystem is null.");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullConstraints() {
 coordSystem.setBounds(new Rectangle(10, 10, 500, 500));
 try {
 CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, null, constraints2, model);
 util.computeLeftMargin();
 fail("Expected IllegalArgumentException when ChartDataModelConstraints is null.");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullConstraints2() {
 coordSystem.setBounds(new Rectangle(10, 10, 500, 500));
 constraints.setMinimumColumnValue(10.0);
 constraints.setMaximumColumnValue(50.0);
 try {
 CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, null, model);
 util.computeLeftMargin();
 fail("Expected IllegalArgumentException when ChartDataModelConstraints2 is null.");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullModel() {
 coordSystem.setBounds(new Rectangle(10, 10, 500, 500));
 constraints.setMinimumColumnValue(10.0);
 constraints2.setMaximumColumnValue(50.0);
 try {
 CoordSystemUtilities util = new CoordSystemUtilities(coordSystem, constraints, constraints2, null);
 util.computeLeftMargin();
 fail("Expected IllegalArgumentException when ChartDataModel is null.");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}