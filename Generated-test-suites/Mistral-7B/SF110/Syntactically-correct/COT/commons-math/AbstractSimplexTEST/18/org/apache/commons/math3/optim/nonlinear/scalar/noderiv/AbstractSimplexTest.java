package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import org.junit.runner.*;
import java.util.*;

@RunWith(JUnit4.class)
public class AbstractSimplexTest {

 private AbstractSimplex simplex;

 @Before
 public void setUp() {
 simplex = new AbstractSimplex(3);
 }

 @Test
 public void testGetDimension() {
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexNullConstructor() {
 new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexZeroConstructor() {
 new AbstractSimplex(0);
 }

 @Test
 public void testAbstractSimplexConstructorWithSteps() {
 double[] steps = {1.0, 1.0, 1.0};
 simplex = new AbstractSimplex(steps);
 PointValuePair[] points = simplex.getPoints();
 assertArrayEquals(new double[][]{
         {0.0, 0.0, 0.0},
         {1.0, 0.0, 0.0},
         {1.0, 1.0, 0.0},
         {1.0, 1.0, 1.0}
 }, points);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexConstructorWithInvalidReferenceSimplex() {
 double[][] referenceSimplex = {{1.0, 1.0}, {1.0, 1.0}};
 new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexConstructorWithInvalidNumberOfPoints() {
 double[][] referenceSimplex = new double[2][];
 referenceSimplex[0] = new double[]{1.0};
 referenceSimplex[1] = new double[]{1.0, 1.0};
 new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexConstructorWithEqualVertices() {
 double[][] referenceSimplex = {
         {1.0, 1.0, 1.0},
         {1.0, 1.0, 1.0}
 };
 new AbstractSimplex(referenceSimplex);
 }

 @Test
 public void testReplaceWorstPoint() {
 PointValuePair worstPoint = new PointValuePair(new double[]{1.0, 1.0, 1.0}, -1.0);
 simplex.replaceWorstPoint(worstPoint, new Comparator<PointValuePair>() {
     @Override
     public int compare(PointValuePair o1, PointValuePair o2) {
         return Double.compare(o1.getValue(), o2.getValue());
     }
 });
 PointValuePair newWorstPoint = simplex.getPoint(3);
 assertArrayEquals(new double[]{1.0, 1.0, 1.0}, newWorstPoint.getPointRef());
 assertEquals(-1.0, newWorstPoint.getValue(), 0.001);
 }

 @Test
 public void testSetPoint() {
 PointValuePair point = new PointValuePair(new double[]{1.0, 1.0, 1.0}, 1.0);
 PointValuePair expectedPoint = simplex.getPoint(2);
 simplex.setPoint(2, point);
 PointValuePair actualPoint = simplex.getPoint(2);
 assertArrayEquals(new double[]{1.0, 1.0, 1.0}, actualPoint.getPointRef());
 assertEquals(1.0, actualPoint.getValue(), 0.001);
 assertEquals(expectedPoint.getValue(), simplex.getPoint(0).getValue(), 0.001);
 }

 @Test
 public void testSetPoints() {
 PointValuePair[] newPoints = new PointValuePair[]{
         new PointValuePair(new double[]{1.0, 1.0, 1.0}, 1.0),
         new PointValuePair(new double[]{2.0, 2.0, 2.0}, 4.0),
         new PointValuePair(new double[]{3.0, 3.0, 3.0}, 9.0)
 };
 simplex.setPoints(newPoints);
 PointValuePair[] expectedPoints = new PointValuePair[]{
         new PointValuePair(new double[]{0.0, 0.0, 0.0}, Double.NaN),
         new PointValuePair(new double[]{1.0, 1.0, 1.0}, 1.0),
         new PointValuePair(new double[]{2.0, 2.0, 2.0}, 4.0),
         new PointValuePair(new double[]{3.0, 3.0, 3.0}, 9.0)
 };
 PointValuePair[] actualPoints = simplex.getPoints();
 assertArrayEquals(expectedPoints, actualPoints);
 }
}