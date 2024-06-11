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

 private AbstractSimplex simplex;
 private int dimension;

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 new Object[]{2},
 new Object[]{3}
 });
 }

 public AbstractSimplexTest(int dim) {
 this.dimension = dim;
 }

 @Before
 public void setUp() {
 try {
 simplex = new AbstractSimplex(dim);
 } catch (Exception e) {
 fail("Failed to initialize AbstractSimplex: " + e.getMessage());
 }
 }

 @Test
 public void testGetDimension() {
 assertEquals("Incorrect dimension", dimension, simplex.getDimension());
 }

 @Test(expected = NegativeArraySizeException.class)
 public void testNullInitialization() {
 new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testZeroInitialization() {
 new AbstractSimplex(0);
 }

 @Test
 public void testCreateHypercubeSteps() {
 double[] steps = AbstractSimplex.createHypercubeSteps(dimension, 1.0);
 assertEquals("Incorrect number of steps", dimension, steps.length);
 Arrays.stream(steps).allMatch(d -> d > 0.0);
 }

 @Test(expected = NullArgumentException.class)
 public void testNullSteps() {
 new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testZeroStepsArray() {
 new AbstractSimplex(new double[0]);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMismatchedReferenceSimplexLength() {
 double[][] referenceSimplex = new double[dimension - 1][dimension];
 new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMismatchedReferenceSimplexColumns() {
 double[][] referenceSimplex = new double[dimension][dimension - 1];
 new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testEqualVerticesInSimplex() {
 double[][] referenceSimplex = new double[][]{{0.0, 0.0}, {0.0, 0.0}};
 new AbstractSimplex(referenceSimplex);
 }

 @Test
 public void testBuild() {
 double[] startPoint = new double[dimension];
 simplex.build(startPoint);
 assertEquals("Incorrect simplex size", dimension + 1, simplex.getSize());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testStartPointMismatch() {
 double[] startPoint = new double[dimension + 1];
 simplex.build(startPoint);
 }

 @Test
 public void testGetPoints() {
 PointValuePair[] points = simplex.getPoints();
 assertEquals("Incorrect number of points", dimension + 1, points.length);
 }

 @Test
 public void testGetPoint() {
 PointValuePair point = simplex.getPoint(0);
 assertNotNull(point);
 assertEquals("Incorrect number of dimensions", dimension, point.getPointRef().length);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetPointOutOfRange() {
 simplex.getPoint(-1);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetPointOutOfLast() {
 simplex.getPoint(dimension);
 }

 @Test
 public void testSetPoint() {
 double[] newPoint = new double[dimension];
 PointValuePair newPointPair = new PointValuePair(newPoint, 0.0);
 simplex.setPoint(0, newPointPair);
 PointValuePair oldPoint = simplex.getPoint(0);
 Arrays.equals(newPoint, oldPoint.getPointRef());
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetPointOutOfRange() {
 simplex.setPoint(-1, new PointValuePair(new double[dimension], 0.0));
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetPointOutOfLast() {
 simplex.setPoint(dimension, new PointValuePair(new double[dimension], 0.0));
 }

 @Test
 public void testSetPoints() {
 PointValuePair[] newPoints = new PointValuePair[dimension + 1];
 simplex.setPoints(newPoints);
 PointValuePair[] points = simplex.getPoints();
 Arrays.equals(newPoints, points);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testSetPointsMismatchSize() {
 PointValuePair[] newPoints = new PointValuePair[dimension];
 simplex.setPoints(newPoints);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testSetPointsMismatchNumberOfDimensions() {
 PointValuePair[] newPoints = new PointValuePair[dimension + 1];
 for (int i = 0; i < newPoints.length; ++i) {
 newPoints[i] = new PointValuePair(new double[dimension - 1], 0.0);
 }
 simplex.setPoints(newPoints);
 }
}