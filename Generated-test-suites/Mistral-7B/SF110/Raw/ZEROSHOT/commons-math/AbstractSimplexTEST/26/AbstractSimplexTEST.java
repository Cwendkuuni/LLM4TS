package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import java.util.*;

public class AbstractSimplexTest {

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 // Initialize test data
 int dimension = 3;
 double sideLength = 1.0;
 AbstractSimplex simplex = new AbstractSimplex(dimension, sideLength);
 }

 @Test
 public void testAbstractSimplexConstructorInt() {
 AbstractSimplex simplex = new AbstractSimplex(dimension);
 assertEquals(dimension, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplexConstructorIntDouble() {
 AbstractSimplex simplex = new AbstractSimplex(dimension, sideLength);
 assertEquals(dimension, simplex.getDimension());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexConstructorNullSteps() {
 AbstractSimplex simplex = new AbstractSimplex(new double[0]);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexConstructorZeroSteps() {
 AbstractSimplex simplex = new AbstractSimplex(new double[] { 0.0 });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexConstructorInvalidSteps() {
 AbstractSimplex simplex = new AbstractSimplex(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0 });
 }

 @Test
 public void testAbstractSimplexConstructorReferenceSimplex() {
 PointValuePair[] refSimplex = {
 new PointValuePair(new double[] { 0.0, 0.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 0.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 0.0, 1.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 0.0, 0.1, 1.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 1.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 0.1, 1.0 }, Double.NaN),
 new PointValuePair(new double[] { 0.1, 1.0, 1.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.1, 1.1, 1.1 }, Double.NaN)
 };
 AbstractSimplex simplex = new AbstractSimplex(refSimplex);
 assertEquals(dimension, simplex.getDimension());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexConstructorEmptyReferenceSimplex() {
 AbstractSimplex simplex = new AbstractSimplex(new PointValuePair[0]);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexConstructorInvalidReferenceSimplex() {
 PointValuePair[] refSimplex = {
 new PointValuePair(new double[] { 0.0, 0.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 0.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 0.0, 1.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 0.0, 0.1, 1.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 1.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 0.1, 1.0 }, Double.NaN),
 new PointValuePair(new double[] { 0.1, 1.0, 1.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.1, 1.1, 1.1 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 1.1, 1.1 }, Double.NaN)
 };
 AbstractSimplex simplex = new AbstractSimplex(refSimplex);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexConstructorInvalidReferenceSimplexDifferentSize() {
 PointValuePair[] refSimplex = {
 new PointValuePair(new double[] { 0.0, 0.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 0.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 0.0, 1.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 0.0, 0.1, 1.0 }, Double.NaN)
 };
 AbstractSimplex simplex = new AbstractSimplex(refSimplex);
 }

 @Test(expected = EqualVerticesInSimplexException.class)
 public void testAbstractSimplexConstructorEqualVertices() {
 AbstractSimplex simplex = new AbstractSimplex(new double[] { 1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0 });
 }

 @Test(expected = EqualVerticesInSimplexException.class)
 public void testAbstractSimplexConstructorEqualVerticesInReferenceSimplex() {
 PointValuePair[] refSimplex = {
 new PointValuePair(new double[] { 1.0, 0.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 0.0, 0.0 }, Double.NaN)
 };
 AbstractSimplex simplex = new AbstractSimplex(refSimplex);
 }

 @Test
 public void testGetDimension() {
 AbstractSimplex simplex = new AbstractSimplex(dimension, sideLength);
 assertEquals(dimension, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 AbstractSimplex simplex = new AbstractSimplex(dimension, sideLength);
 assertEquals(dimension + 1, simplex.getSize());
 }

 @Test
 public void testIterate() throws Exception {
 MultivariateFunction function = new AckleyFunction();
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 simplex.iterate(function, comparator);
 }

 @Test
 public void testBuild() {
 double[] startPoint = { 1.2, 3.4, 5.6 };
 simplex.build(startPoint);
 assertEquals(dimension + 1, simplex.getSize());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testBuildInvalidDimension() {
 double[] startPoint = { 1.2, 3.4 };
 simplex.build(startPoint);
 }

 @Test
 public void testGetPoints() {
 PointValuePair[] points = simplex.getPoints();
 assertEquals(dimension + 1, points.length);
 }

 @Test
 public void testGetPoint() {
 PointValuePair point = simplex.getPoint(0);
 assertNotNull(point);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetPointInvalidIndex() {
 simplex.getPoint(dimension);
 }

 @Test
 public void testSetPoint() {
 PointValuePair newPoint = new PointValuePair(new double[] { 2.3, 4.5, 6.7 }, -1.23);
 simplex.setPoint(0, newPoint);
 PointValuePair point = simplex.getPoint(0);
 assertEquals(newPoint.getPointRef(), point.getPointRef());
 assertEquals(newPoint.getValue(), point.getValue(), 0.001);
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetPointInvalidIndex() {
 simplex.setPoint(dimension, new PointValuePair(new double[] { 2.3, 4.5, 6.7 }, -1.23));
 }

 @Test
 public void testSetPoints() {
 PointValuePair[] newPoints = new PointValuePair[dimension + 1];
 for (int i = 0; i < newPoints.length; i++) {
 newPoints[i] = new PointValuePair(new double[] { i, i + 1, i + 2 }, i * 3.0);
 }
 simplex.setPoints(newPoints);
 assertEquals(newPoints.length, simplex.getSize());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testSetPointsInvalidSize() {
 PointValuePair[] newPoints = new PointValuePair[dimension];
 simplex.setPoints(newPoints);
 }
}