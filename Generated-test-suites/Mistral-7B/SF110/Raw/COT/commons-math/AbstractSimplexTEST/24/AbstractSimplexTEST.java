package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;
import org.junit.*;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;

 @Before
 public void setUp() {
 simplex = new AbstractSimplex(3);
 }

 @Test
 public void testConstructor_Int() {
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testConstructor_Int_Double() {
 simplex = new AbstractSimplex(3, 1.0);
 assertEquals(3, simplex.getDimension());
 }

 @Test(expected = NullArgumentException.class)
 public void testConstructor_NullArray() {
 simplex = new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testConstructor_EmptyArray() {
 simplex = new AbstractSimplex(new double[0]);
 }

 @Test(expected = ZeroException.class)
 public void testConstructor_ZeroSteps() {
 simplex = new AbstractSimplex(new double[]{0.0, 0.0, 0.0});
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructor_NegativeStep() {
 simplex = new AbstractSimplex(new double[]{-1.0, 1.0, 1.0});
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructor_EmptyReferenceSimplex() {
 simplex = new AbstractSimplex(new double[][]{});
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructor_InvalidReferenceSimplexSize() {
 simplex = new AbstractSimplex(new double[][]{
         {1.0, 2.0},
         {1.0, 2.0},
         {1.0, 2.0},
         {1.0, 2.0},
         {1.0, 2.0}
 });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructor_InvalidReferenceSimplexDimension() {
 simplex = new AbstractSimplex(new double[][]{
         {1.0, 2.0},
         {1.0, 2.0},
         {1.0, 2.0},
         {1.0, 2.0, 3.0}
 });
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testConstructor_EqualVerticesInSimplex() {
 simplex = new AbstractSimplex(new double[][]{
         {1.0, 2.0},
         {1.0, 2.0},
         {1.0, 2.0},
         {1.0, 2.0}
 });
 }

 @Test
 public void testGetDimension() {
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIterate_NullFunction() {
 simplex.iterate(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIterate_NullComparator() {
 simplex.iterate(new MockFunction(), null);
 }

 @Test
 public void testBuild() {
 double[] startPoint = {1.0, 2.0, 3.0};
 simplex.build(startPoint);
 PointValuePair[] points = simplex.getPoints();
 assertEquals(4, points.length);
 for (int i = 0; i < points.length; ++i) {
 PointValuePair point = points[i];
 assertNotNull(point.getValue());
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testBuild_InvalidStartPointSize() {
 simplex.build(new double[]{1.0, 2.0, 3.0, 4.0});
 }

 @Test
 public void testEvaluate_NullFunction() {
 simplex.evaluate(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testEvaluate_NullComparator() {
 simplex.evaluate(new MockFunction(), null);
 }

 @Test
 public void testEvaluate() {
 MultivariateFunction evaluationFunction = new MockFunction();
 simplex.evaluate(evaluationFunction, new Comparator<PointValuePair>() {
     @Override
     public int compare(PointValuePair o1, PointValuePair o2) {
         return (int) (o1.getValue() - o2.getValue());
     }
 });

 PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
 double worstPointValue = worstPoint.getValue();
 assertEquals(Double.NaN, worstPoint.getValue(), 0.0);

 for (int i = 0; i < simplex.getSize() - 1; ++i) {
 PointValuePair bestPoint = simplex.getPoint(i);
 double bestPointValue = bestPoint.getValue();
 assertNotEquals(Double.NaN, bestPointValue);
 assertLess(bestPointValue, worstPointValue);
 }
 }

 @Test
 public void testReplaceWorstPoint() {
 MultivariateFunction evaluationFunction = new MockFunction();
 simplex.evaluate(evaluationFunction, new Comparator<PointValuePair>() {
     @Override
     public int compare(PointValuePair o1, PointValuePair o2) {
         return (int) (o1.getValue() - o2.getValue());
     }
 });
 PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
 simplex.replaceWorstPoint(worstPoint, new Comparator<PointValuePair>() {
     @Override
     public int compare(PointValuePair o1, PointValuePair o2) {
         return (int) (o1.getValue() - o2.getValue());
     }
 });
 PointValuePair newWorstPoint = simplex.getPoint(simplex.getSize() - 1);
 assertNotSame(worstPoint, newWorstPoint);
 }

 @Test
 public void testGetPoints() {
 PointValuePair[] points = simplex.getPoints();
 assertNotNull(points);
 assertEquals(4, points.length);
 }

 @Test
 public void testGetPoint() {
 PointValuePair point = simplex.getPoint(0);
 assertNotNull(point);
 assertEquals(3, point.getPointRef().length);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetPoint_InvalidIndex() {
 simplex.getPoint(-1);
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetPoint_InvalidIndex() {
 PointValuePair point = new PointValuePair(new double[]{1.0, 2.0}, 1.0);
 simplex.setPoint(-1, point);
 }

 @Test
 public void testSetPoint() {
 PointValuePair point = new PointValuePair(new double[]{1.0, 2.0}, 1.0);
 simplex.setPoint(0, point);
 PointValuePair newPoint = simplex.getPoint(0);
 assertEquals(point, newPoint);
 }

 @Test
 public void testSetPoints() {
 PointValuePair[] points = new PointValuePair[] {
         new PointValuePair(new double[]{1.0, 2.0}, 1.0),
         new PointValuePair(new double[]{2.0, 1.0}, 2.0),
         new PointValuePair(new double[]{2.0, 2.0}, 3.0),
         new PointValuePair(new double[]{3.0, 2.0}, 4.0)
 };
 simplex.setPoints(points);
 PointValuePair[] newPoints = simplex.getPoints();
 assertArrayEquals(points, newPoints);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testSetPoints_InvalidSize() {
 PointValuePair[] points = new PointValuePair[]{
         new PointValuePair(new double[]{1.0, 2.0}, 1.0),
         new PointValuePair(new double[]{1.0, 2.0}, 1.0),
         new PointValuePair(new double[]{1.0, 2.0}, 1.0)
 };
 simplex.setPoints(points);
 }
}