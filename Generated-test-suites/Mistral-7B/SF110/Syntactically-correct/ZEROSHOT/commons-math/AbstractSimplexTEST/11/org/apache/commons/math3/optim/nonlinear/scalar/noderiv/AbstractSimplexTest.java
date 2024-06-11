package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.junit.Test;

public class AbstractSimplexTest {

 private PointValuePair[] createTestPoints(int dimension, double sideLength) {
 PointValuePair[] testPoints = new PointValuePair[dimension + 1];
 testPoints[0] = new PointValuePair(new double[dimension], Double.NaN);
 for (int i = 1; i <= dimension; i++) {
 double[] point = new double[dimension];
 for (int j = 0; j < dimension; j++) {
 point[j] = sideLength * Math.pow(2, j) / Math.pow(2, i);
 }
 testPoints[i] = new PointValuePair(point, Double.NaN);
 }
 return testPoints;
 }

 @Test
 public void testAbstractSimplexConstruction() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(dimension);
 assertEquals(dimension, simplex.getDimension());
 assertEquals(dimension + 1, simplex.getSize());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexNullConfiguration() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(dimension, null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexEmptyConfiguration() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(new double[0]);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexZeroSteps() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(new double[] { 0.0 });
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexNegativeSteps() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(new double[] { -1.0 });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexMismatchedConfiguration() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(new double[] { 1.0, 1.0, 2.0 });
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexEqualVertices() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(new double[] { 1.0, 1.0, 1.0 });
 }

 @Test
 public void testAbstractSimplexReferenceSimplex() {
 int dimension = 2;
 double[][] referenceSimplex = { { 1.0, 0.0 }, { 0.5, 0.5 }, { 0.0, 1.0 } };
 AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
 assertEquals(dimension, simplex.getDimension());
 assertEquals(dimension + 1, simplex.getSize());
 for (int i = 0; i < dimension; i++) {
 PointValuePair testPoint = simplex.getPoint(i);
 PointValuePair referencePoint = referenceSimplex[i + 1];
 assertArrayEquals(referencePoint.getPointRef(), testPoint.getPointRef());
 }
 }

 @Test
 public void testBuild() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(dimension);
 double[] startPoint = { 1.0, 1.0 };
 simplex.build(startPoint);
 for (int i = 0; i <= dimension; i++) {
 PointValuePair testPoint = simplex.getPoint(i);
 PointValuePair expectedPoint = new PointValuePair(new double[dimension], Double.NaN);
 System.arraycopy(startPoint, 0, expectedPoint.getPointRef(), 0, dimension);
 expectedPoint.setPoint(i == 0 ? null : expectedPoint.getPointRef());
 assertEquals(expectedPoint, testPoint);
 }
 }

 @Test
 public void testEvaluate() {
 int dimension = 2;
 MultivariateFunction testFunction = new QuadraticFunction(new double[][] { { 1.0, 1.0 }, { 1.0, 1.0 }, { 1.0, 1.0 } }, 1.0);
 AbstractSimplex simplex = new AbstractSimplex(dimension);
 simplex.build(new double[] { 1.0, 1.0 });
 simplex.evaluate(testFunction, new ComparatorSimple<PointValuePair>() {});
 for (int i = 0; i <= dimension; i++) {
 PointValuePair testPoint = simplex.getPoint(i);
 double expectedValue = testFunction.value(testPoint.getPointRef());
 assertEquals(expectedValue, testPoint.getValue(), 0.001);
 }
 }

 @Test
 public void testReplaceWorstPoint() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(dimension);
 simplex.build(new double[] { 1.0, 1.0 });
 PointValuePair newPoint = new PointValuePair(new double[] { 0.1, 0.1 }, 0.0);
 simplex.replaceWorstPoint(newPoint, new ComparatorSimple<PointValuePair>() {});
 PointValuePair oldWorstPoint = simplex.getPoint(dimension);
 assertArrayEquals(newPoint.getPointRef(), oldWorstPoint.getPointRef());
 }

 @Test
 public void testGetPoints() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(dimension);
 simplex.build(new double[] { 1.0, 1.0 });
 PointValuePair[] testPoints = simplex.getPoints();
 for (int i = 0; i <= dimension; i++) {
 PointValuePair testPoint = testPoints[i];
 PointValuePair expectedPoint = simplex.getPoint(i);
 assertEquals(expectedPoint, testPoint);
 }
 }

 @Test
 public void testSetPoint() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(dimension);
 double[] newPoint = new double[] { 0.1, 0.1 };
 simplex.setPoints(new PointValuePair[] {
 new PointValuePair(newPoint, Double.NaN),
 new PointValuePair(createTestPoints(dimension, 1.0)[0], Double.NaN),
 new PointValuePair(createTestPoints(dimension, 1.0)[1], Double.NaN)
 });
 simplex.setPoint(1, new PointValuePair(new double[] { 0.2, 0.2 }, Double.NaN));
 PointValuePair testPoint = simplex.getPoint(1);
 assertArrayEquals(new double[] { 0.2, 0.2 }, testPoint.getPointRef());
 }

 @Test
 public void testSetPoints() {
 int dimension = 2;
 AbstractSimplex simplex = new AbstractSimplex(dimension);
 PointValuePair[] testPoints = createTestPoints(dimension, 1.0);
 simplex.setPoints(testPoints);
 for (int i = 0; i <= dimension; i++) {
 PointValuePair testPoint = simplex.getPoint(i);
 assertEquals(testPoints[i], testPoint);
 }
 }

}