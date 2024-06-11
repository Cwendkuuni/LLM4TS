package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private CoordSystem coord;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 coord = new CoordSystem(new java.awt.Dimension(500, 500), new DecimalFormat("#.##"), new DecimalFormat("#.###"), Color.WHITE, Color.BLACK, 6, 15);
 constraints = new ChartDataModelConstraints(model, 1.0, 10.0, 2.0, 12.0);
 constraints2 = new ChartDataModelConstraints(model, 2.0, -5.0, 3.0, 7.0);
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // test case for positive minimum and maximum values
 double xmin = 1.0;
 double xmax = 10.0;
 assertEquals(35, utilities.computeLeftMargin());

 // test case for negative minimum and positive maximum values
 double xminNeg = -2.0;
 double xmaxPos = 5.0;
 assertEquals(35, utilities.computeLeftMargin());

 // test case for both minimum and maximum values are zero
 double xminZero = 0.0;
 double xmaxZero = 0.0;
 assertEquals(20, utilities.computeLeftMargin());

 // test case for both minimum and maximum values are negative
 double xminNegative = -3.0;
 double xmaxNegative = -8.0;
 assertEquals(20, utilities.computeLeftMargin());

 // test case for error: null constraints
 constraints = null;
 try {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }

 // test case for error: null model
 model = null;
 try {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testComputeRightMargin() {
 // test case for positive minimum and maximum values
 double xmin = 1.0;
 double xmax = 10.0;
 assertEquals(22, utilities.computeRightMargin());

 // test case for negative minimum and positive maximum values
 double xminNeg = -2.0;
 double xmaxPos = 5.0;
 assertEquals(15, utilities.computeRightMargin());

 // test case for both minimum and maximum values are zero
 double xminZero = 0.0;
 double xmaxZero = 0.0;
 assertEquals(15, utilities.computeRightMargin());

 // test case for both minimum and maximum values are negative
 double xminNegative = -3.0;
 double xmaxNegative = -8.0;
 assertEquals(15, utilities.computeRightMargin());
}

 @Test
 public void testComputeTopMargin() {
 // test case for positive minimum and maximum values
 double ymin = 1.0;
 double ymax = 10.0;
 assertEquals(31, utilities.computeTopMargin());

 // test case for negative minimum and positive maximum values
 double yminNeg = -2.0;
 double ymaxPos = 5.0;
 assertEquals(31, utilities.computeTopMargin());

 // test case for both minimum and maximum values are zero
 double yminZero = 0.0;
 double ymaxZero = 0.0;
 assertEquals(15, utilities.computeTopMargin());

 // test case for both minimum and maximum values are negative
 double yminNegative = -3.0;
 double ymaxNegative = -8.0;
 assertEquals(15, utilities.computeTopMargin());
}

 @Test
 public void testComputeBottomMargin() {
 // test case for positive minimum and maximum values
 double ymin = 1.0;
 double ymax = 10.0;
 assertEquals(31, utilities.computeBottomMargin());

 // test case for negative minimum and positive maximum values
 double yminNeg = -2.0;
 double ymaxPos = 5.0;
 assertEquals(31, utilities.computeBottomMargin());

 // test case for both minimum and maximum values are zero
 double yminZero = 0.0;
 double ymaxZero = 0.0;
 assertEquals(20, utilities.computeBottomMargin());

 // test case for both minimum and maximum values are negative
 double yminNegative = -3.0;
 double ymaxNegative = -8.0;
 assertEquals(20, utilities.computeBottomMargin());

 // test case for error: null constraints
 constraints = null;
 try {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }

 // test case for error: null model
 model = null;
 try {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testComputeXAxisLabelHeight() {
 // test case for positive minimum and maximum values
 double min = 1.0;
 double max = 10.0;
 double tick = 1.0;
 int height = 0;
 assertEquals(2, (int)utilities.computeXAxisLabelHeight());

 // test case for negative minimum and positive maximum values
 double minNeg = -2.0;
 double maxPos = 5.0;
 assertEquals(4, (int)utilities.computeXAxisLabelHeight());

 // test case for both minimum and maximum values are zero
 double minZero = 0.0;
 double maxZero = 0.0;
 assertEquals(0, (int)utilities.computeXAxisLabelHeight());

 // test case for both minimum and maximum values are negative
 double minNegative = -3.0;
 double maxNegative = -8.0;
 assertEquals(4, (int)utilities.computeXAxisLabelHeight());

 // test case for error: null constraints
 constraints = null;
 try {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }

 // test case for error: null model
 model = null;
 try {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testComputeYAxisLabelWidth() {
 // test case for positive minimum and maximum values
 double min = 1.0;
 double max = 10.0;
 int width = 0;
 assertEquals(8, (int)utilities.computeYAxisLabelWidth());

 // test case for negative minimum and positive maximum values
 double minNeg = -2.0;
 double maxPos = 5.0;
 assertEquals(8, (int)utilities.computeYAxisLabelWidth());

 // test case for both minimum and maximum values are zero
 double minZero = 0.0;
 double maxZero = 0.0;
 assertEquals(0, (int)utilities.computeYAxisLabelWidth());

 // test case for both minimum and maximum values are negative
 double minNegative = -3.0;
 double maxNegative = -8.0;
 assertEquals(8, (int)utilities.computeYAxisLabelWidth());

 // test case for error: null constraints
 constraints = null;
 try {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }

 // test case for error: null model
 model = null;
 try {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
}
}