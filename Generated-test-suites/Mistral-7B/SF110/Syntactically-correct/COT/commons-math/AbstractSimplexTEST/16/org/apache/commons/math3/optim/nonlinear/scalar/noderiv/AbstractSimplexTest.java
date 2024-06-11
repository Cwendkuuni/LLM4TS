package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.junit.Test;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;

 @Test
 public void testAbstractSimplexConstructorWithInt() {
     simplex = new AbstractSimplex(3);
     assertEquals(3, simplex.getDimension());
     assertNotNull(simplex.getPoints());
 }

 @Test
 public void testAbstractSimplexConstructorWithIntAndDouble() {
     simplex = new AbstractSimplex(3, 2.0);
     assertEquals(3, simplex.getDimension());
     assertNotNull(simplex.getPoints());
 }

 @Test
 public void testAbstractSimplexConstructorWithDoubleArray() {
     double[] steps = {1.0, 2.0, 3.0};
     simplex = new AbstractSimplex(steps);
     assertEquals(3, simplex.getDimension());
     assertNotNull(simplex.getPoints());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexConstructorWithNullDoubleArray() {
     simplex = new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexConstructorWithZeroLengthDoubleArray() {
     simplex = new AbstractSimplex(new double[0]);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexConstructorWithInvalidDimensionDoubleArray() {
     double[] steps = {1.0, 2.0, 3.0, 4.0};
     simplex = new AbstractSimplex(steps);
 }

 @Test
 public void testAbstractSimplexConstructorWithDoubleMatrix() {
     double[][] referenceSimplex = {
         {1.0, 0.0, 0.0},
         {0.5, 1.1, 0.0},
         {-0.5, 1.1, 0.0},
         {0.0, 1.0, 1.0}
     };
     simplex = new AbstractSimplex(referenceSimplex);
     assertEquals(3, simplex.getDimension());
     assertNotNull(simplex.getPoints());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexConstructorWithInvalidMatrixSize() {
     double[][] referenceSimplex = {
         {1.0, 0.0, 0.0},
         {1.0, 1.1, 0.0}
     };
     simplex = new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexConstructorWithInvalidMatrixColumns() {
     double[][] referenceSimplex = {
         {1.0, 0.0, 0.0},
         {1.0, 1.1, 1.1}
     };
     simplex = new AbstractSimplex(referenceSimplex);
 }

 @Test
 public void testAbstractSimplexGetDimension() {
     simplex = new AbstractSimplex(new AbstractSimplex(3));
     assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplexGetSize() {
     simplex = new AbstractSimplex(new AbstractSimplex(3));
     assertEquals(4, simplex.getSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAbstractSimplexIterateUnsupported() {
     simplex = new AbstractSimplex(new AbstractSimplex(3));
     OptimizationFunction function = new DummyFunction();
     simplex.iterate(function, null);
 }

 @Test
 public void testAbstractSimplexBuild() {
     double[] startPoint = {1.0, 2.0};
     simplex = new AbstractSimplex(new AbstractSimplex(2));
     simplex.build(startPoint);
     double[][] expectedConfiguration = {
         {1.0, 1.0},
         {2.0, 1.0},
         {1.0, 2.0}
     };
     assertArrayEquals(expectedConfiguration, simplex.startConfiguration);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexBuildWithInvalidStartPointSize() {
     double[] startPoint = {1.0, 2.0, 3.0};
     simplex = new AbstractSimplex(new AbstractSimplex(2));
     simplex.build(startPoint);
 }

 @Test
 public void testAbstractSimplexEvaluate() {
     MultivariateFunction evaluationFunction = new DummyFunction();
     simplex = new AbstractSimplex(new AbstractSimplex(3));
     simplex.evaluate(evaluationFunction, null);
     double[] expectedValues = {Double.NaN, Double.NaN, Double.NaN, Double.NaN};
     assertArrayEquals(expectedValues, Arrays.stream(simplex.simplex).mapToDouble(p -> p.getValue()).toArray(), 0.001);
 }

 @Test
 public void testAbstractSimplexReplaceWorstPoint() {
     double[] newPoint = {3.0, 4.0};
     PointValuePair newPointPair = new PointValuePair(newPoint, 2.0);
     simplex = new AbstractSimplex(new AbstractSimplex(3));
     simplex.replaceWorstPoint(newPointPair, null);
     PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
     assertEquals(newPointPair, worstPoint);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAbstractSimplexGetPointOutOfBounds() {
     simplex = new AbstractSimplex(new AbstractSimplex(3));
     simplex.getPoint(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAbstractSimplexSetPointOutOfBounds() {
     simplex = new AbstractSimplex(new AbstractSimplex(3));
     PointValuePair newPointPair = new PointValuePair(new double[3], 0.0);
     simplex.setPoint(-1, newPointPair);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexSetPointsWithInvalidSize() {
     PointValuePair[] newPoints = new PointValuePair[4];
     simplex = new AbstractSimplex(new AbstractSimplex(3));
     simplex.setPoints(newPoints);
 }

 @Test
 public void testAbstractSimplexSetPoints() {
     PointValuePair[] newPoints = new PointValuePair[3];
     double[][] newConfiguration = {
         {1.0, 1.0},
         {2.0, 2.0},
         {3.0, 3.0}
     };
     simplex = new AbstractSimplex(new AbstractSimplex(3));
     simplex.setPoints(newPoints);
     for (int i = 0; i < newPoints.length; i++) {
         simplex.simplex[i] = newPoints[i];
         assertArrayEquals(newConfiguration[i], simplex.startConfiguration[i]);
     }
 }

}