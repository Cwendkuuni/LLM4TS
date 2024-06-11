package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

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
 public void testAbstractSimplex_doubleArray_throwsException_whenNullArgument() {
 assertThrows(NullArgumentException.class, () -> new AbstractSimplex(null));
 }

 @Test
 public void testAbstractSimplex_doubleArray_throwsException_whenZeroLength() {
 assertThrows(NotStrictlyPositiveException.class, () -> new AbstractSimplex(new double[0]));
 }

 @Test
 public void testAbstractSimplex_doubleArray_throwsException_whenInvalidStep() {
 double[] steps = {0.0, 1.0, 1.0, 1.0};
 assertThrows(ZeroException.class, () -> new AbstractSimplex(steps));
 }

 @Test
 public void testAbstractSimplex_doubleArray() {
 simplex = new AbstractSimplex(new double[] {1.0, 1.0, 2.0});
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_doubleArrayArray_throwsException_whenInvalidDimension() {
 double[][] referenceSimplex = new double[2][3];
 assertThrows(NotStrictlyPositiveException.class, () -> new AbstractSimplex(referenceSimplex));
 }

 @Test
 public void testAbstractSimplex_doubleArrayArray_throwsException_whenInvalidLength() {
 double[][] referenceSimplex = new double[3][1];
 assertThrows(NotStrictlyPositiveException.class, () -> new AbstractSimplex(referenceSimplex));
 }

 @Test
 public void testAbstractSimplex_doubleArrayArray() {
 double[][] referenceSimplex = new double[][] {{1.0, 2.0}, {2.0, 3.0}};
 simplex = new AbstractSimplex(referenceSimplex);
 assertEquals(2, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_doubleArrayArray_throwsException_whenEqualVertices() {
 double[][] referenceSimplex = new double[][] {{1.0, 2.0}, {1.0, 2.0}};
 assertThrows(MathIllegalArgumentException.class, () -> new AbstractSimplex(referenceSimplex));
 }

 @Test
 public void testAbstractSimplex_doubleArrayArray_throwsException_whenReferencePointNotSameLength() {
 double[][] referenceSimplex = new double[][] {{1.0, 2.0}, {2.0, 3.0, 4.0}};
 assertThrows(DimensionMismatchException.class, () -> new AbstractSimplex(referenceSimplex));
 }

 @Test
 public void testAbstractSimplex_getDimension() {
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_getSize() {
 assertEquals(4, simplex.getSize());
 }

 @Test
 public void testIterate_MultivariateFunction_Comparator() {
 MultivariateFunction function = new FunctionWithGradient();
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return o1.getValue().compareTo(o2.getValue());
 }
 };
 simplex.iterate(function, comparator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIterate_MultivariateFunction_Comparator_nullFunction() {
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return 0;
 }
 };
 simplex.iterate(null, comparator);
 }

 @Test(expected = IllegalArgumentArgumentException.class)
 public void testIterate_MultivariateFunction_Comparator_nullComparator() {
 MultivariateFunction function = new FunctionWithGradient();
 simplex.iterate(function, null);
 }

 @Test
 public void testBuild_doubleArray() {
 double[] startPoint = new double[] {1.0, 2.0, 3.0};
 simplex.build(startPoint);
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testBuild_doubleArray_invalidPointLength() {
 double[] startPoint = new double[] {1.0, 2.0};
 simplex.build(startPoint);
 }

 @Test
 public void testEvaluate_MultivariateFunction_Comparator() {
 MultivariateFunction function = new FunctionWithGradient();
 comparator.compare(simplex.getPoint(0), simplex.getPoint(3));
 simplex.evaluate(function, comparator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluate_MultivariateFunction_Comparator_nullFunction() {
 comparator.compare(simplex.getPoint(0), simplex.getPoint(3));
 simplex.evaluate(null, comparator);
 }

 @Test(expected = IllegalArgumentArgumentException.class)
 public void testEvaluate_MultivariateFunction_Comparator_nullComparator() {
 MultivariateFunction function = new FunctionWithGradient();
 simplex.evaluate(function, null);
 }

 @Test
 public void testReplaceWorstPoint_PointValuePair_Comparator() {
 PointValuePair worstPoint = new PointValuePair(new double[] {3.0, 4.0}, Double.MIN_VALUE);
 comparator.compare(worstPoint, simplex.getPoint(1));
 simplex.replaceWorstPoint(worstPoint, comparator);
 assertEquals(worstPoint, simplex.getPoint(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReplaceWorstPoint_PointValuePair_Comparator_invalidIndex() {
 PointValuePair worstPoint = new PointValuePair(new double[] {3.0, 4.0}, Double.MIN_VALUE);
 comparator.compare(worstPoint, simplex.getPoint(5));
 simplex.replaceWorstPoint(worstPoint, comparator);
 }

 @Test
 public void testGetPoints() {
 PointValuePair[] points = simplex.getPoints();
 assertArrayEquals(simplex.getPoints(), points);
 }

 @Test
 public void testGetPoint_int() {
 PointValuePair point = simplex.getPoint(0);
 assertEquals(point.getPointRef(), new double[] {1.0, 2.0, 3.0});
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetPoint_int_invalidIndex() {
 simplex.getPoint(4);
 }

 @Test
 public void testSetPoint_int_PointValuePair() {
 PointValuePair point = new PointValuePair(new double[] {3.0, 4.0}, Double.MAX_VALUE);
 simplex.setPoint(0, point);
 assertEquals(point, simplex.getPoint(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPoint_int_PointValuePair_invalidIndex() {
 PointValuePair point = new PointValuePair(new double[] {3.0, 4.0}, Double.MAX_VALUE);
 simplex.setPoint(4, point);
 }

 @Test
 public void testSetPoints_PointValuePairArray() {
 PointValuePair[] newPoints = new PointValuePair[] {new PointValuePair(new double[] {0.0, 0.0}, Double.NaN), new PointValuePair(new double[] {3.0, 4.0}, Double.MAX_VALUE)};
 simplex.setPoints(newPoints);
 assertArrayEquals(newPoints, simplex.getPoints());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testSetPoints_PointValuePairArray_invalidSize() {
 PointValuePair[] newPoints = new PointValuePair[5];
 simplex.setPoints(newPoints);
 }
}