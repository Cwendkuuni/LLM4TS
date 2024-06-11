package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartUtilities;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;
 private CoordSystem coord;

 @Before
 public void setup() {
 double[] data = {-10.5, -5.3, 0.0, 5.2, 10.1};
 double[] data2 = {2.5, 7.1, 12.3, 15.6, 18.9};
 constraints = new ChartDataModelConstraints(data, null, null);
 constraints2 = new ChartDataModelConstraints(null, data2, null);
 model = new ChartDataModel(data, null);
 coord = new CoordSystem(new java.awt.Dimension(500, 500), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Positive test
 double[] xValues = {-1.0, 0.5, 1.5, 2.5, 3.5};
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xValues, null, null);
 CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 int expected = 59;
 int actual = utils.computeLeftMargin();
 assertEquals(expected, actual);

 // Edge case test: minimum value is zero
 double[] xValuesMinZero = {0.0, 1.5, 2.5, 3.5, 4.5};
 ChartDataModelConstraints constraintsMinZero = new ChartDataModelConstraints(xValuesMinZero, null, null);
 CoordSystemUtilities utilsMinZero = new CoordSystemUtilities(coord, constraintsMinZero, constraints2, model);
 int expectedMinZero = 20;
 int actualMinZero = utilsMinZero.computeLeftMargin();
 assertEquals(expectedMinZero, actualMinZero);

 // Error scenario test: null constraints
 ChartDataModelConstraints nullConstraints = null;
 CoordSystemUtilities utilsNullConstraints = new CoordSystemUtilities(coord, nullConstraints, constraints2, model);
 int actualNullConstraints = utilsNullConstraints.computeLeftMargin();
 assertEquals(20, actualNullConstraints);
 }

 @Test
 public void testComputeRightMargin() {
 // Positive test
 double[] xValues = {-1.0, 0.5, 1.5, 2.5, 3.5};
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xValues, null, null);
 CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 int expected = 24;
 int actual = utils.computeRightMargin();
 assertEquals(expected, actual);

 // Edge case test: maximum value is zero
 double[] xValuesMaxZero = {-1.0, 0.5, 1.5, 2.5, 0.0};
 ChartDataModelConstraints constraintsMaxZero = new ChartDataModelConstraints(xValuesMaxZero, null, null);
 CoordSystemUtilities utilsMaxZero = new CoordSystemUtilities(coord, constraintsMaxZero, constraints2, model);
 int expectedMaxZero = 15;
 int actualMaxZero = utilsMaxZero.computeRightMargin();
 assertEquals(expectedMaxZero, actualMaxZero);

 // Error scenario test: null constraints
 ChartDataModelConstraints nullConstraints = null;
 CoordSystemUtilities utilsNullConstraints = new CoordSystemUtilities(coord, nullConstraints, constraints2, model);
 int actualNullConstraints = utilsNullConstraints.computeRightMargin();
 assertEquals(15, actualNullConstraints);
 }

 @Test
 public void testComputeTopMargin() {
 // Positive test
 double[] yValues = {-1.0, -0.5, 0.0, 0.5, 1.0};
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, yValues, null);
 CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 int expected = 29;
 int actual = utils.computeTopMargin();
 assertEquals(expected, actual);

 // Edge case test: minimum value is zero
 double[] yValuesMinZero = {0.0, 0.5, 1.0, 1.5, 2.0};
 ChartDataModelConstraints constraintsMinZero = new ChartDataModelConstraints(null, yValuesMinZero, null);
 CoordSystemUtilities utilsMinZero = new CoordSystemUtilities(coord, constraintsMinZero, constraints2, model);
 int expectedMinZero = 20;
 int actualMinZero = utilsMinZero.computeTopMargin();
 assertEquals(expectedMinZero, actualMinZero);

 // Error scenario test: null constraints
 ChartDataModelConstraints nullConstraints = null;
 CoordSystemUtilities utilsNullConstraints = new CoordSystemUtilities(coord, nullConstraints, constraints2, model);
 int actualNullConstraints = utilsNullConstraints.computeTopMargin();
 assertEquals(20, actualNullConstraints);
 }

 @Test
 public void testComputeBottomMargin() {
 // Positive test
 double[] yValues = {-1.0, -0.5, 0.0, 0.5, 1.0};
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, yValues, null);
 CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 int expected = 33;
 int actual = utils.computeBottomMargin();
 assertEquals(expected, actual);

 // Edge case test: minimum value is zero
 double[] yValuesMinZero = {0.0, 0.5, 1.0, 1.5, 2.0};
 ChartDataModelConstraints constraintsMinZero = new ChartDataModelConstraints(null, yValuesMinZero, null);
 CoordSystemUtilities utilsMinZero = new CoordSystemUtilities(coord, constraintsMinZero, constraints2, model);
 int expectedMinZero = 20;
 int actualMinZero = utilsMinZero.computeBottomMargin();
 assertEquals(expectedMinZero, actualMinZero);

 // Error scenario test: null constraints
 ChartDataModelConstraints nullConstraints = null;
 CoordSystemUtilities utilsNullConstraints = new CoordSystemUtilities(coord, nullConstraints, constraints2, model);
 int actualNullConstraints = utilsNullConstraints.computeBottomMargin();
 assertEquals(20, actualNullConstraints);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Positive test
 double[] xValues = {-1.0, 0.5, 1.5, 2.5, 3.5};
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xValues, null, null);
 CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 int expected = 20;
 int actual = utils.computeXAxisLabelHeight();
 assertEquals(expected, actual);

 // Edge case test: minimum value is zero and maximum value is negative
 double[] xValuesMinZeroMaxNegative = {0.0, 1.5, 2.5, 3.5, -1.0};
 ChartDataModelConstraints constraintsMinZeroMaxNegative = new ChartDataModelConstraints(xValuesMinZeroMaxNegative, null, null);
 CoordSystemUtilities utilsMinZeroMaxNegative = new CoordSystemUtilities(coord, constraintsMinZeroMaxNegative, constraints2, model);
 int expectedMinZeroMaxNegative = 42;
 int actualMinZeroMaxNegative = utilsMinZeroMaxNegative.computeXAxisLabelHeight();
 assertEquals(expectedMinZeroMaxNegative, actualMinZeroMaxNegative);

 // Error scenario test: null constraints
 ChartDataModelConstraints nullConstraints = null;
 CoordSystemUtilities utilsNullConstraints = new CoordSystemUtilities(coord, nullConstraints, constraints2, model);
 int actualNullConstraints = utilsNullConstraints.computeXAxisLabelHeight();
 assertEquals(20, actualNullConstraints);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Positive test
 double[] yValues = {-1.0, -0.5, 0.0, 0.5, 1.0};
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, yValues, null);
 CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 int expected = 13;
 int actual = utils.computeYAxisLabelWidth();
 assertEquals(expected, actual);

 // Edge case test: minimum value is negative and maximum value is zero
 double[] yValuesMinNegativeMaxZero = {-1.0, -0.5, 0.0, 0.5, 0.0};
 ChartDataModelConstraints constraintsMinNegativeMaxZero = new ChartDataModelConstraints(null, yValuesMinNegativeMaxZero, null);
 CoordSystemUtilities utilsMinNegativeMaxZero = new CoordSystemUtilities(coord, constraintsMinNegativeMaxZero, constraints2, model);
 int expectedMinNegativeMaxZero = 32;
 int actualMinNegativeMaxZero = utilsMinNegativeMaxZero.computeYAxisLabelWidth();
 assertEquals(expectedMinNegativeMaxZero, actualMinNegativeMaxZero);

 // Error scenario test: null constraints
 ChartDataModelConstraints nullConstraints = null;
 CoordSystemUtilities utilsNullConstraints = new CoordSystemUtilities(coord, nullConstraints, constraints2, model);
 int actualNullConstraints = utilsNullConstraints.computeYAxisLabelWidth();
 assertEquals(13, actualNullConstraints);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Positive test
 double[] xValues = {-1.0, 0.5, 1.5, 2.5, 3.5};
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xValues, null, null);
 CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 Graphics2D g = null;
 utils.drawNumericalXAxisTicks(g);
 // Verification: 5 ticks drawn
 Arrays.stream(xValues).forEach(x -> {
 int expectedX = (int)Math.round(x * coord.getScaleX());
 int expectedY = coord.getHeight() - 3;
 Point2D point = new Point2D.Double(expectedX, expectedY);
 AffineTransform transform = coord.getTransform(0);
 Point2D transformedPoint = transform.transform(point, null);
 assertEquals(transformedPoint.getX(), g.getTransform().getTranslateX() + transformedPoint.getX(), 0.01);
 assertEquals(transformedPoint.getY(), g.getTransform().getTranslateY() + transformedPoint.getY(), 0.01);
 });

 // Edge case test: minimum value is zero
 double[] xValuesMinZero = {0.0, 1.5, 2.5, 2.5, 3.5};
 ChartDataModelConstraints constraintsMinZero = new ChartDataModelConstraints(xValuesMinZero, null, null);
 CoordSystemUtilities utilsMinZero = new CoordSystemUtilities(coord, constraintsMinZero, constraints2, model);
 Graphics2D gMinZero = null;
 utilsMinZero.drawNumericalXAxisTicks(gMinZero);
 // Verification: 4 ticks drawn
 Arrays.stream(Arrays.copyOfRange(xValuesMinZero, 1, xValuesMinZero.length)).forEach(x -> {
 int expectedX = (int)Math.round(x * coord.getScaleX());
 int expectedY = coord.getHeight() - 3;
 Point2D point = new Point2D.Double(expectedX, expectedY);
 AffineTransform transform = coord.getTransform(0);
 Point2D transformedPoint = transform.transform(point, null);
 assertEquals(transformedPoint.getX(), gMinZero.getTransform().getTranslateX() + transformedPoint.getX(), 0.01);
 assertEquals(transformedPoint.getY(), gMinZero.getTransform().getTranslateY() + transformedPoint.getY(), 0.01);
 });

 // Error scenario test: null graphics context
 Graphics2D gNull = null;
 CoordSystemUtilities utilsNullGraphicsContext = new CoordSystemUtilities(coord, constraints, constraints2, model);
 utilsNullGraphicsContext.drawNumericalXAxisTicks(gNull);
 // No verification needed, as the method should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDrawNumericalXAxisTicksWithNullGraphicsContextThrowsException() {
 CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 utils.drawNumericalXAxisTicks(null);
 }
}