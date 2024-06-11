package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.text.*;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.*;
import de.progra.charting.model.*;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;

 @Before
 public void setup() {
 // Assuming the CoordSystem, ChartDataModelConstraints and ChartDataModel instances are initialized
 // elsewhere in your test suite
 coordSystem = new CoordSystem(new Dimension(500, 500), new Dimension(50, 50), 10, 10, null, null);
 constraints = new ChartDataModelConstraints(new Double[] { 1.0, 2.0, 3.0, 4.0 }, new Double[] { 5.0, 10.0, 15.0, 20.0 }, null);
 constraints2 = new ChartDataModelConstraints(new Double[] { -3.0, -2.0, -1.0, 0.0 }, new Double[] { 0.0, 5.0, 10.0, 15.0 }, null);
 model = new ChartDataModel(new Object[] { "A", "B", "C", "D" }, new Double[][] { new Double[] { 1.0, 2.0, 3.0, 4.0 }, new Double[] { 5.0, 10.0, 15.0, 20.0 } }, null);

 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 int expected = 132; // Calculate the expected value based on your test data
 int actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 int expected = 21; // Calculate the expected value based on your test data
 int actual = utilities.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 int expected = 43; // Calculate the expected value based on your test data
 int actual = utilities.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 int expected = 81; // Calculate the expected value based on your test data
 int actual = utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 int expected = 15; // Calculate the expected value based on your test data
 int actual = utilities.computeXAxisLabelHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 int expected = 80; // Calculate the expected value based on your test data
 int actual = utilities.computeYAxisLabelWidth();
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawNumericalXAxisTicks(g);
 // Add assertions here based on your requirements
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawXAxisTicks(g);
 // Add assertions here based on your requirements
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawYAxisTicks(g);
 // Add assertions here based on your requirements
 }
}