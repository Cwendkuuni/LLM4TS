package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;

 @Test
 public void testAbstractSimplex_int() {
 simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_int_double() {
 simplex = new AbstractSimplex(3, 2.0);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_doubleArray_ThrowsException_when_steps_is_null() {
 assertThrows(NullArgumentException.class, () -> new AbstractSimplex(null));
 }

 @Test
 public void testAbstractSimplex_doubleArray_ThrowsException_when_steps_length_is_zero() {
 assertThrows(ZeroException.class, () -> new AbstractSimplex(new double[0]));
 }

 @Test
 public void testAbstractSimplex_doubleArray() {
 simplex = new AbstractSimplex(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_doubleArrayArray_ThrowsException_when_referenceSimpleX_length_is_less_than_one() {
 assertThrows(NotStrictlyPositiveException.class, () -> new AbstractSimplex(new double[][] {}));
 }

 @Test
 public void testAbstractSimplex_doubleArrayArray() {
 simplex = new AbstractSimplex(new double[][] {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}});
 assertEquals(2, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_doubleArrayArray_ThrowsException_when_vertices_are_equal() {
 assertThrows(MathIllegalArgumentException.class, () -> new AbstractSimplex(new double[][] {{1.0, 2.0}, {1.0, 2.0}}));
 }

 @Test
 public void testGetDimension() {
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 assertEquals(4, simplex.getSize());
 }

 @Test
 public void testIterate_ThrowsException_when_evaluationFunction_or_comparator_are_null() {
 MultivariateFunction evaluationFunction = null;
 Comparator<PointValuePair> comparator = null;
 assertThrows(NullArgumentException.class, () -> simplex.iterate(evaluationFunction, comparator));
 }

 @Test
 public void testBuild_ThrowsException_when_startPoint_dimension_is_not_equal_to_dimension() {
 double[] startPoint = new double[] {1.0, 2.0, 3.0, 4.0};
 assertThrows(DimensionMismatchException.class, () -> simplex.build(startPoint));
 }

 @Test
 public void testBuild() {
 simplex.build(new double[] {0.0, 0.0, 0.0});
 PointValuePair[] points = simplex.getPoints();
 assertEquals(4, points.length);
 for (int i = 0; i < 4; ++i) {
 PointValuePair point = points[i];
 assertNotNull(point.getPointRef());
 }
 }

 @Test
 public void testEvaluate_ThrowsException_when_evaluationFunction_or_comparator_are_null() {
 MultivariateFunction evaluationFunction = null;
 Comparator<PointValuePair> comparator = null;
 assertThrows(NullArgumentException.class, () -> simplex.evaluate(evaluationFunction, comparator));
 }

 @Test
 public void testEvaluate() {
 MultivariateFunction evaluationFunction = new ArrayFunction(new double[][] {{-0.5, -0.5}, {0.5, 0.5}, {1.0, 1.0}, {2.0, 2.0}}, f -> f.value(new Point2D(f[0], f[1])));
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return (int) (o1.getValue() - o2.getValue());
 }
 };
 simplex.evaluate(evaluationFunction, comparator);
 PointValuePair worstPoint = simplex.getPoint(3);
 assertEquals(Double.POSITIVE_INFINITY, worstPoint.getValue(), 0.0);
 }

 @Test
 public void testReplaceWorstPoint() {
 PointValuePair pointValuePair = new PointValuePair(new double[] {1.0, 1.0}, 5.0);
 simplex.replaceWorstPoint(pointValuePair, comparator);
 PointValuePair newWorstPoint = simplex.getPoint(3);
 assertEquals(pointValuePair, newWorstPoint);
 }

 @Test
 public void testGetPoints() {
 PointValuePair[] points = simplex.getPoints();
 assertEquals(4, points.length);
 }

 @Test
 public void testGetPoint() {
 PointValuePair point = simplex.getPoint(0);
 assertNotNull(point.getPointRef());
 }

 @Test
 public void testSetPoint() {
 PointValuePair point = new PointValuePair(new double[] {0.0, 0.0}, Double.NaN);
 simplex.setPoint(0, point);
 assertEquals(point, simplex.getPoint(0));
 }

 @Test
 public void testSetPoints_ThrowsException_when_points_length_is_not_equal_to_size() {
 PointValuePair[] points = new PointValuePair[3];
 assertThrows(DimensionMismatchException.class, () -> simplex.setPoints(points));
 }

 @Test
 public void testSetPoints() {
 PointValuePair[] points = new PointValuePair[] {new PointValuePair(new double[] {0.0, 0.0}, Double.NaN), new PointValuePair(new double[] {1.0, 1.0}, Double.NaN), new PointValuePair(new double[] {2.0, 2.0}, Double.NaN)};
 simplex.setPoints(points);
 assertArrayEquals(points, simplex.getPoints());
 }
}