package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;

 @Before
 public void setUp() {
 simplex = new AbstractSimplex(3);
 }

 @Test
 public void testAbstractSimplexConstructorWithInt() {
 assertEquals(3, simplex.getDimension());
 assertNotNull(simplex.getPoints());
 }

 @Test
 public void testAbstractSimplexConstructorWithIntAndDouble() {
 simplex = new AbstractSimplex(3, 2.0);
 assertEquals(3, simplex.getDimension());
 assertNotNull(simplex.getPoints());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexConstructorWithNullArray() {
 new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexConstructorWithEmptyArray() {
 new AbstractSimplex(new double[0]);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexConstructorWithZeroSteps() {
 new AbstractSimplex(new double[] { 0.0 });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexConstructorWithNonMatchingLengths() {
 new AbstractSimplex(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexConstructorWithEqualVertices() {
 new AbstractSimplex(new double[] { 1.0, 1.0, 1.0 });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexConstructorWithNonMatchingDimensions() {
 new AbstractSimplex(new double[][] { {1.0, 2.0}, { 1.0, 3.0 } });
 }

 @Test
 public void testAbstractSimplexConstructorWithReferenceSimplex() {
 simplex = new AbstractSimplex(new double[][] { {1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2} });
 assertEquals(3, simplex.getDimension());
 assertNotNull(simplex.getPoints());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexConstructorWithEmptyReferenceSimplex() {
 new AbstractSimplex(new double[][] {});
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexConstructorWithNegativeReferenceSimplex() {
 new AbstractSimplex(new double[][] { {-1.0, -2.0}, {-1.1, -2.1}, {-1.2, -2.2} });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexGetDimension() {
 simplex = new AbstractSimplex(new double[][] { {1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2} });
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplexGetSize() {
 simplex = new AbstractSimplex(new double[][] { {1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2} });
 assertEquals(4, simplex.getSize());
 }

 @Test
 public void testAbstractSimplexIterate() {
 // Abstract method, cannot test directly
 }

 @Test
 public void testAbstractSimplexBuild() {
 simplex.build(new double[] { 1.0, 2.0, 3.0 });
 assertNotNull(simplex.getPoints());
 PointValuePair point = simplex.getPoint(0);
 assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, point.getPointRef(), 0.001);
 for (int i = 1; i < simplex.getSize(); i++) {
 point = simplex.getPoint(i);
 assertArrayEquals(new double[] { point.getPointRef()[0] + simplex.startConfiguration[i - 1][0],
 point.getPointRef()[1] + simplex.startConfiguration[i - 1][1],
 point.getPointRef()[2] + simplex.startConfiguration[i - 1][2] }, point.getPointRef(), 0.001);
 }
 }

 @Test
 public void testAbstractSimplexEvaluate() {
 // Abstract method, cannot test directly
 }

 @Test
 public void testAbstractSimplexReplaceWorstPoint() {
 simplex.replaceWorstPoint(new PointValuePair(new double[] { 5.0, 6.0 }, Double.MAX_VALUE), null);
 PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
 assertArrayEquals(new double[] { 5.0, 6.0 }, worstPoint.getPointRef(), 0.001);
 simplex.replaceWorstPoint(new PointValuePair(new double[] { 0.0, 0.0 }, Double.MIN_VALUE), null);
 worstPoint = simplex.getPoint(simplex.getSize() - 1);
 assertArrayEquals(new double[] { 0.0, 0.0 }, worstPoint.getPointRef(), 0.001);
 }

 @Test
 public void testAbstractSimplexGetPoints() {
 assertNotNull(simplex.getPoints());
 }

 @Test
 public void testAbstractSimplexGetPoint() {
 simplex.build(new double[] { 1.0, 2.0, 3.0 });
 PointValuePair point = simplex.getPoint(0);
 assertArrayEquals(new double[] { 1.0, 2.0, 3.0 }, point.getPointRef(), 0.001);
 }

 @Test(expected = OutOfRangeException.class)
 public void testAbstractSimplexGetPointOutOfRange() {
 simplex.build(new double[] { 1.0, 2.0, 3.0 });
 simplex.getPoint(-1);
 }

 @Test(expected = OutOfRangeException.class)
 public void testAbstractSimplexGetPointOutOfRange2() {
 simplex.build(new double[] { 1.0, 2.0, 3.0 });
 simplex.getPoint(simplex.getSize());
 }

 @Test
 public void testAbstractSimplexSetPoint() {
 simplex.build(new double[] { 1.0, 2.0, 3.0 });
 PointValuePair point = simplex.getPoint(0);
 simplex.setPoint(0, new PointValuePair(new double[] { 4.0, 5.0 }, Double.MAX_VALUE));
 assertArrayEquals(new double[] { 4.0, 5.0 }, simplex.getPoint(0).getPointRef(), 0.001);
 }

 @Test(expected = OutOfRangeException.class)
 public void testAbstractSimplexSetPointOutOfRange() {
 simplex.build(new double[] { 1.0, 2.0, 3.0 });
 simplex.setPoint(-1, new PointValuePair(new double[] { 4.0, 5.0 }, Double.MAX_VALUE));
 }

 @Test(expected = OutOfRangeException.class)
 public void testAbstractSimplexSetPointOutOfRange2() {
 simplex.build(new double[] { 1.0, 2.0, 3.0 });
 simplex.setPoint(simplex.getSize(), new PointValuePair(new double[] { 4.0, 5.0 }, Double.MAX_VALUE));
 }

 @Test
 public void testAbstractSimplexSetPoints() {
 PointValuePair[] points = new PointValuePair[5];
 for (int i = 0; i < points.length; i++) {
 points[i] = new PointValuePair(new double[] { i + 1, i + 1 }, Double.MAX_VALUE);
 }
 simplex.setPoints(points);
 assertEquals(5, simplex.getSize());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexSetPointsWithInvalidLength() {
 PointValuePair[] points = new PointValuePair[4];
 simplex.setPoints(points);
 }

}