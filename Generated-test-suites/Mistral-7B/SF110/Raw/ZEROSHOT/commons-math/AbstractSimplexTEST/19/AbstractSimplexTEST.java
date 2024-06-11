package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.junit.*;

public class AbstractSimplexTest {

 @BeforeClass
 static void setUpBeforeClass() {
 // Set up any static data or initialize any static resources here
 }

 @AfterClass
 static void tearDownAfterClass() {
 // Release any static resources here
 }

 @Before
 static void setUp() {
 // Initialize any instance variables here
 }

 @After
 static void tearDown() {
 // Release any allocated resources or tear down any mock objects here
 }

 @Test
 public void testAbstractSimplexWithNullArgException() {
 try {
 new AbstractSimplex(null);
 fail("Should throw NullArgumentException");
 } catch (NullArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testAbstractSimplexWithZeroDimension() {
 try {
 new AbstractSimplex(0);
 fail("Should throw ZeroException");
 } catch (ZeroException e) {
 // Expected exception
 }
 }

 @Test
 public void testAbstractSimplexWithZeroStep() {
 try {
 new AbstractSimplex(new double[] { 0.0 });
 fail("Should throw ZeroException");
 } catch (ZeroException e) {
 // Expected exception
 }
 }

 @Test
 public void testAbstractSimplexWithZeroLengthStepArray() {
 try {
 new AbstractSimplex(new double[0]);
 fail("Should throw ZeroException");
 } catch (ZeroException e) {
 // Expected exception
 }
 }

 @Test
 public void testAbstractSimplexWithEqualVerticesInSimplex() {
 try {
 new AbstractSimplex(new double[] { 1.0, 1.0 });
 fail("Should throw MathIllegalArgumentException");
 } catch (MathIllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testAbstractSimplexWithNonEqualVerticesInSimplex() {
 double[] steps = new double[] { 1.0, 2.0 };
 try {
 new AbstractSimplex(steps);
 } catch (Exception e) {
 fail("Should not throw any exception");
 }
 }

 @Test
 public void testAbstractSimplexWithReferenceSimplex() {
 double[][] referenceSimplex = { { 1.0, 1.0 }, { 2.0, 2.0 }, { 3.0, 3.0 } };
 try {
 new AbstractSimplex(referenceSimplex);
 } catch (Exception e) {
 fail("Should not throw any exception");
 }
 }

 @Test
 public void testAbstractSimplexGetDimension() {
 AbstractSimplex simplex = new SimpleXPoint(3);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplexGetSize() {
 AbstractSimplex simplex = new SimpleXPoint(3);
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAbstractSimplexIterateUnsupported() {
 AbstractSimplex simplex = new SimpleXPoint(3);
 MultivariateFunction function = mock(MultivariateFunction.class);
 simplex.iterate(function, null);
 fail("Should throw UnsupportedOperationException");
 }

 @Test
 public void testAbstractSimplexBuild() {
 double[] startPoint = new double[] { 1.0, 2.0 };
 AbstractSimplex simplex = new SimpleXPoint(3);
 simplex.build(startPoint);

 PointValuePair[] points = simplex.getPoints();
 PointValuePair point0 = points[0];
 PointValuePair point1 = points[1];
 PointValuePair point2 = points[2];
 PointValuePair point3 = points[3];

 assertEquals(startPoint, point0.getPointRef());
 assertArrayEquals(new double[] { 1.0, 2.0, 2.0 }, point1.getPointRef());
 assertArrayEquals(new double[] { 1.0, 2.0, 1.0 }, point2.getPointRef());
 assertArrayEquals(new double[] { 1.0, 2.0, 0.0 }, point3.getPointRef());
 }

 @Test
 public void testAbstractSimplexEvaluate() {
 double[] startPoint = new double[] { 1.0, 2.0 };
 AbstractSimplex simplex = new SimpleXPoint(3);
 simplex.build(startPoint);
 MultivariateFunction function = mock(MultivariateFunction.class);
 simplex.evaluate(function, null);

 PointValuePair point0 = simplex.getPoints()[0];
 PointValuePair point1 = simplex.getPoints()[1];
 PointValuePair point2 = simplex.getPoints()[2];
 PointValuePair point3 = simplex.getPoints()[3];

 assertEquals(Double.NaN, point0.getValue(), 1e-9);
 assertEquals(Double.NaN, point1.getValue(), 1e-9);
 assertEquals(Double.NaN, point2.getValue(), 1e-9);
 assertEquals(Double.NaN, point3.getValue(), 1e-9);
 }

 @Test
 public void testAbstractSimplexReplaceWorstPoint() {
 double[] startPoint = new double[] { 1.0, 2.0 };
 AbstractSimplex simplex = new SimpleXPoint(3);
 simplex.build(startPoint);
 PointValuePair worstPoint = simplex.getPoints()[2];
 PointValuePair newPoint = new PointValuePair(new double[] { 3.0, 4.0 }, 10.0);
 simplex.replaceWorstPoint(newPoint, null);

 PointValuePair[] points = simplex.getPoints();
 PointValuePair point0 = points[0];
 PointValuePair point1 = points[1];
 PointValuePair point2 = points[2];
 PointValuePair point3 = points[3];

 assertEquals(newPoint, point2);
 }

 @Test
 public void testAbstractSimplexGetPoint() {
 double[] startPoint = new double[] { 1.0, 2.0 };
 AbstractSimplex simplex = new SimpleXPoint(3);
 simplex.build(startPoint);
 PointValuePair point = simplex.getPoint(1);
 PointValuePair expectedPoint = simplex.getPoints()[1];

 assertEquals(expectedPoint, point);
 }

 @Test(expected = OutOfRangeException.class)
 public void testAbstractSimplexGetPointOutOfRange() {
 double[] startPoint = new double[] { 1.0, 2.0 };
 AbstractSimplex simplex = new SimpleXPoint(3);
 simplex.build(startPoint);
 PointValuePair point = simplex.getPoint(-1);
 fail("Should throw OutOfRangeException");
 }

 @Test
 public void testAbstractSimplexSetPoint() {
 double[] startPoint = new double[] { 1.0, 2.0 };
 AbstractSimplex simplex = new SimpleXPoint(3);
 simplex.build(startPoint);
 PointValuePair point = new PointValuePair(new double[] { 3.0, 4.0 }, 10.0);
 simplex.setPoint(1, point);

 PointValuePair[] points = simplex.getPoints();
 PointValuePair expectedPoint = new PointValuePair(new double[] { 1.0, 2.0 }, Double.NaN);
 PointValuePair point1 = points[1];

 assertEquals(expectedPoint, simplex.getPoint(0));
 assertEquals(point, simplex.getPoint(1));
 }

 @Test(expected = OutOfRangeException.class)
 public void testAbstractSimplexSetPointOutOfRange() {
 double[] startPoint = new double[] { 1.0, 2.0 };
 AbstractSimplex simplex = new SimpleXPoint(3);
 simplex.build(startPoint);
 PointValuePair point = new PointValuePair(new double[] { 3.0, 4.0 }, 10.0);
 simplex.setPoint(5, point);
 fail("Should throw OutOfRangeException");
 }

 @Test
 public void testAbstractSimplexSetPoints() {
 double[] startPoint = new double[] { 1.0, 2.0 };
 AbstractSimplex simplex = new SimpleXPoint(3);
 simplex.build(startPoint);
 PointValuePair[] newPoints = new PointValuePair[] {
 new PointValuePair(new double[] { 3.0, 4.0 }, 10.0),
 new PointValuePair(new double[] { 4.0, 5.0 }, 11.0),
 new PointValuePair(new double[] { 5.0, 6.0 }, 12.0)
 };
 simplex.setPoints(newPoints);

 PointValuePair[] points = simplex.getPoints();
 PointValuePair expectedPoint0 = new PointValuePair(new double[] { 1.0, 2.0 }, Double.NaN);
 PointValuePair expectedPoint1 = new PointValuePair(new double[] { 3.0, 4.0 }, 10.0);
 PointValuePair expectedPoint2 = new PointValuePair(new double[] { 4.0, 5.0 }, 11.0);
 PointValuePair expectedPoint3 = new PointValuePair(new double[] { 5.0, 6.0 }, 12.0);

 assertEquals(expectedPoints(expectedPoint0, expectedPoint1, expectedPoint2, expectedPoint3), points);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexSetPointsMismatchDimension() {
 double[] startPoint = new double[] { 1.0, 2.0 };
 AbstractSimplex simplex = new SimpleXPoint(3);
 simplex.build(startPoint);
 PointValuePair[] newPoints = new PointValuePair[] {
 new PointValuePair(new double[] { 3.0, 4.0, 5.0 }, 10.0),
 new PointValuePair(new double[] { 4.0, 5.0 }, 11.0),
 new PointValuePair(new double[] { 5.0, 6.0 }, 12.0)
 };
 simplex.setPoints(newPoints);
 fail("Should throw DimensionMismatchException");
 }

 private PointValuePair[] expectedPoints(PointValuePair... points) {
 return Arrays.copyOf(points, points.length);
 }
}