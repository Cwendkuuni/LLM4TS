package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.*;

@RunWith(Parameterized.class)
public class AbstractSimplexTest {

 private int dimension;
 private AbstractSimplex simplex;

 @Parameterized.Parameters(name = "{index}: test AbstractSimplex with dimension={0}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] { { 1 }, { 2 }, { 3 } });
 }

 public AbstractSimplexTest(int dimension) {
 this.dimension = dimension;
 }

 @Test
 public void testConstructorWithDimension() {
 simplex = new AbstractSimplex(dimension);
 assertEquals("Dimension not set correctly", dimension, simplex.getDimension());
 }

 @Test
 public void testConstructorWithDimensionAndSideLength() {
 simplex = new AbstractSimplex(dimension, 1.0);
 assertEquals("Dimension not set correctly", dimension, simplex.getDimension());
 }

 @Test(expected = NullArgumentException.class)
 public void testConstructorWithNullSteps() {
 simplex = new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testConstructorWithZeroSteps() {
 simplex = new AbstractSimplex(new double[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithSamePoints() {
 simplex = new AbstractSimplex(new double[] { 0.0, 0.0, 0.0 });
 try {
 simplex = new AbstractSimplex(new double[] { 0.0, 0.0, 0.0 });
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithMismatchedReferenceSimplexSize() {
 simplex = new AbstractSimplex(new double[][] { { 0.0, 0.0 }, { 1.0, 1.0 } });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithMismatchedReferenceSimplexDimension() {
 simplex = new AbstractSimplex(new double[][] { { 0.0, 0.0 }, { 1.0, 1.0, 1.0 } });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEqualVerticesInReferenceSimplex() {
 simplex = new AbstractSimplex(new double[][] { { 0.0, 0.0 }, { 0.0, 0.0 } });
 }

 @Test
 public void testGetDimension() {
 simplex = new AbstractSimplex(dimension);
 assertEquals("Dimension not set correctly", dimension, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 simplex = new AbstractSimplex(dimension);
 assertEquals("Size not set correctly", dimension + 1, simplex.getSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterateWithNullFunction() {
 simplex = new AbstractSimplex(dimension);
 ((AbstractSimplex)simplex).iterate(null);
 }

 @Test
 public void testBuild() {
 simplex = new AbstractSimplex(dimension);
 double[] startPoint = new double[dimension];
 simplex.build(startPoint);
 for (int i = 0; i < dimension + 1; ++i) {
 PointValuePair point = simplex.getPoint(i);
 assertEquals("Point not set correctly for index " + i, startPoint, point.getPointRef());
 }
 }

 @Test
 public void testEvaluate() {
 simplex = new AbstractSimplex(dimension);
 double[] startPoint = new double[dimension];
 MultivariateFunction function = new FunctionWithTwoVariables();
 simplex.build(startPoint);
 simplex.evaluate(function, new DefaultComparator<PointValuePair>());
 for (int i = 0; i < dimension + 1; ++i) {
 PointValuePair point = simplex.getPoint(i);
 double value = point.getValue();
 assertEquals("Value not evaluated correctly for index " + i, Double.isNaN(value), (i == 0));
 if (i != 0) {
 FunctionWithTwoVariables functionInstance = new FunctionWithTwoVariables();
 double pointValue = functionInstance.value(point.getPointRef());
 assertEquals("Value not evaluated correctly for index " + i, pointValue, simplex.getPoint(i).getValue());
 }
 }
 }

 @Test
 public void testReplaceWorstPoint() {
 simplex = new AbstractSimplex(dimension);
 double[] startPoint = new double[dimension];
 PointValuePair pointToReplace = new PointValuePair(new double[] { 1.0, 0.0 }, 1.0);
 simplex.build(startPoint);
 simplex.replaceWorstPoint(pointToReplace, new DefaultComparator<PointValuePair>());
 PointValuePair worstPoint = simplex.getPoint(dimension);
 assertEquals("Worst point not replaced", pointToReplace, worstPoint);
 }

 @Test
 public void testGetPoints() {
 simplex = new AbstractSimplex(dimension);
 double[] startPoint = new double[dimension];
 simplex.build(startPoint);
 PointValuePair[] points = simplex.getPoints();
 for (int i = 0; i < points.length; ++i) {
 assertEquals("Point not copied correctly", simplex.getPoint(i), points[i]);
 }
 }

 @Test
 public void testGetPoint() {
 simplex = new AbstractSimplex(dimension);
 double[] startPoint = new double[dimension];
 simplex.build(startPoint);
 PointValuePair point = simplex.getPoint(0);
 assertEquals("Point not set correctly", startPoint, point.getPointRef());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetPointOutOfRange() {
 simplex = new AbstractSimplex(dimension);
 simplex.getPoint(-1);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetPointOutOfRangePlusSize() {
 simplex = new AbstractSimplex(dimension);
 simplex.getPoint(dimension);
 }

 @Test
 public void testSetPoint() {
 simplex = new AbstractSimplex(dimension);
 double[] startPoint = new double[dimension];
 PointValuePair pointToSet = new PointValuePair(new double[] { 1.0, 0.0 }, 1.0);
 simplex.build(startPoint);
 simplex.setPoint(0, pointToSet);
 PointValuePair point = simplex.getPoint(0);
 assertEquals("Point not set correctly", pointToSet, point);
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetPointOutOfRange() {
 simplex = new AbstractSimplex(dimension);
 simplex.setPoint(-1, new PointValuePair(new double[0], Double.NaN));
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetPointOutOfRangePlusSize() {
 simplex = new AbstractSimplex(dimension);
 simplex.setPoint(dimension, new PointValuePair(new double[0], Double.NaN));
 }

 @Test
 public void testSetPoints() {
 simplex = new AbstractSimplex(dimension);
 PointValuePair[] pointsToSet = new PointValuePair[dimension + 1];
 simplex.setPoints(pointsToSet);
 for (int i = 0; i < pointsToSet.length; ++i) {
 assertEquals("Point not set correctly", pointsToSet[i], simplex.getPoint(i));
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testSetPointsWithMismatchedSize() {
 simplex = new AbstractSimplex(dimension);
 PointValuePair[] pointsToSet = new PointValuePair[dimension + 2];
 simplex.setPoints(pointsToSet);
 }

}