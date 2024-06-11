package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;

 @Test
 public void testConstructorWithInt() {
 simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testConstructorWithIntAndSideLength() {
 simplex = new AbstractSimplex(3, 2.0);
 assertEquals(3, simplex.getDimension());
 }

 @Test(expected = NullArgumentException.class)
 public void testConstructorWithNullSteps() {
 simplex = new AbstractSimplex(new double[0]);
 }

 @Test(expected = ZeroException.class)
 public void testConstructorWithZeroSteps() {
 simplex = new AbstractSimplex(new double[]{0.0});
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeDimension() {
 simplex = new AbstractSimplex(-1);
 }

 @Test(expected = ZeroException.class)
 public void testConstructorWithZeroDimension() {
 simplex = new AbstractSimplex(0);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithNonMatchingReferenceSimplexLengthAndDimension() {
 double[][] referenceSimplex = new double[2][3];
 simplex = new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithNonMatchingReferenceSimplexPointLength() {
 double[][] referenceSimplex = new double[2][2];
 simplex = new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testConstructorWithEqualVerticesInReferenceSimplex() {
 double[][] referenceSimplex = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
 simplex = new AbstractSimplex(referenceSimplex);
 }

 @Test
 public void testGetDimension() {
 simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 simplex = new AbstractSimplex(3);
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = RuntimeException.class)
 public void testIterateNullFunction() {
 simplex = new AbstractSimplex(3);
 ((AbstractSimplex) simplex).iterate(null, null);
 }

 @Test(expected = RuntimeException.class)
 public void testIterateNullComparator() {
 simplex = new AbstractSimplex(3);
 ((AbstractSimplex) simplex).iterate(new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return 0.0;
 }
 }, null);
 }

 @Test
 public void testBuild() {
 double[] startPoint = new double[]{1.0, 2.0};
 simplex = new AbstractSimplex(3);
 simplex.build(startPoint);
 assertEquals(4, simplex.getSize());
 PointValuePair expectedPoint = new PointValuePair(new double[]{1.0, 2.0}, Double.NaN);
 assertEquals(expectedPoint, simplex.getPoint(0));
 for (int i = 1; i < simplex.getSize(); ++i) {
 PointValuePair expectedVertex = new PointValuePair(new double[]{startPoint[0] + i, startPoint[1]}, Double.NaN);
 assertEquals(expectedVertex, simplex.getPoint(i));
 }
}

 @Test
 public void testEvaluate() {
 MultivariateFunction evaluationFunction = new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0);
 }
 };
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{1.0, 1.0});
 simplex.evaluate(evaluationFunction, null);
 PointValuePair expectedBestPoint = new PointValuePair(new double[]{1.0, 1.0}, 2.0, false);
 assertEquals(expectedBestPoint, simplex.getPoint(0));
 for (int i = 1; i < simplex.getSize(); ++i) {
 PointValuePair expectedPoint = new PointValuePair(new double[]{1.0 + i, 1.0 + i}, Double.NaN, false);
 assertEquals(expectedPoint, simplex.getPoint(i));
 }
}

 @Test
 public void testReplaceWorstPoint() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{1.0, 1.0});
 simplex.evaluate(new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0);
 }
 }, null);
 PointValuePair worstPoint = simplex.getPoint(1);
 simplex.replaceWorstPoint(worstPoint, null);
 PointValuePair newWorstPoint = simplex.getPoint(3);
 assertNotEquals(worstPoint.getPointRef(), newWorstPoint.getPointRef());
}

 @Test
 public void testGetPoints() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{1.0, 1.0});
 simplex.evaluate(new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0);
 }
 }, null);
 PointValuePair[] expectedPoints = new PointValuePair[]{
 new PointValuePair(new double[]{1.0, 1.0}, 2.0, false),
 new PointValuePair(new double[]{2.0, 2.0}, Double.NaN, false),
 new PointValuePair(new double[]{3.0, 3.0}, Double.NaN, false),
 new PointValuePair(new double[]{1.0, 2.0}, Double.NaN, false)
 };
 PointValuePair[] resultPoints = simplex.getPoints();
 assertArrayEquals(expectedPoints, resultPoints);
 }

 @Test
 public void testGetPointIndexOutOfRange() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{1.0, 1.0});
 simplex.evaluate(new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0);
 }
 }, null);
 try {
 simplex.getPoint(-1);
 fail();
 } catch (OutOfRangeException e) {
 // Expected
 }
}

 @Test
 public void testSetPointIndexOutOfRange() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{1.0, 1.0});
 try {
 simplex.setPoint(-1, new PointValuePair(new double[]{1.0, 1.0}, Double.NaN));
 fail();
 } catch (OutOfRangeException e) {
 // Expected
 }
}

 @Test
 public void testSetPointsNull() {
 simplex = new AbstractSimplex(3);
 try {
 simplex.setPoints(null);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testSetPointsSizeMismatch() {
 simplex = new AbstractSimplex(3);
 PointValuePair[] points = new PointValuePair[4];
 try {
 simplex.setPoints(points);
 fail();
 } catch (DimensionMismatchException e) {
 // Expected
 }
}

}