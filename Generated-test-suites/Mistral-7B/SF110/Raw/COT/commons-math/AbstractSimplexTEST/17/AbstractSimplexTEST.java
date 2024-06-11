package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

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
 simplex = new AbstractSimplex(3, 1.0);
 assertEquals(3, simplex.getDimension());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplex_nullSteps() {
 simplex = new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplex_zeroSteps() {
 simplex = new AbstractSimplex(new double[0]);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplex_equalVertices() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplex_invalidReferenceSimple() {
 simplex = new AbstractSimplex(new double[][] { { 1.0, 0.0 }, { 1.0, 0.0 } });
 }

 @Test
 public void testAbstractSimplex_getDimension() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_getSize() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 assertEquals(4, simplex.getSize());
 }

 @Test
 public void testIterate_nullFunction() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 try {
 simplex.iterate(null, null);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 // Expected
 }
}

 @Test
 public void testIterate_nullComparator() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 MultivariateFunction function = mock(MultivariateFunction.class);
 try {
 simplex.iterate(function, null);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 // Expected
 }
}

 @Test
 public void testBuild() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 double[] startPoint = new double[] { 2.0, 3.0 };
 simplex.build(startPoint);
 assertEquals(4, simplex.getSize());
 PointValuePair[] points = simplex.getPoints();
 for (int i = 0; i < points.length; i++) {
 PointValuePair point = points[i];
 if (i == 0) {
 assertEquals(startPoint, point.getPointRef());
 } else {
 double[] pointRef = point.getPointRef();
 for (int j = 0; j < pointRef.length; j++) {
 assertEquals(startPoint[j] + simplex.startConfiguration[i - 1][j], pointRef[j]);
 }
 }
 }
}

 @Test
 public void testEvaluate_nullFunction() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 Comparator<PointValuePair> comparator = null;
 try {
 simplex.evaluate(null, comparator);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 // Expected
 }
}

 @Test
 public void testEvaluate_nullComparator() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 MultivariateFunction function = mock(MultivariateFunction.class);
 try {
 simplex.evaluate(function, null);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 // Expected
 }
}

 @Test
 public void testReplaceWorstPoint_nullPointValuePair() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 Comparator<PointValuePair> comparator = mock(Comparator.class);
 PointValuePair pointValuePair = null;
 try {
 simplex.replaceWorstPoint(pointValuePair, comparator);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 // Expected
 }
}

 @Test
 public void testGetPoints() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 PointValuePair[] expectedPoints = new PointValuePair[] {
 new PointValuePair(new double[] { 1.0, 0.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 2.0, 0.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 1.0, 0.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 0.0, 1.0 }, Double.NaN),
 new PointValuePair(new double[] { 1.0, 1.0, 1.0 }, Double.NaN),
 new PointValuePair(new double[] { 0.0, 0.0, 0.0 }, Double.NaN) };
 PointValuePair[] points = simplex.getPoints();
 assertArrayEquals(expectedPoints, points);
}

 @Test
 public void testGetPoint_invalidIndex() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 try {
 simplex.getPoint(-1);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected
 }
}

 @Test
 public void testSetPoint_nullPointValuePair() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 int index = 1;
 PointValuePair point = null;
 try {
 simplex.setPoint(index, point);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 // Expected
 }
}

 @Test
 public void testSetPoint_invalidIndex() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 int index = 10;
 PointValuePair point = new PointValuePair(new double[] { 1.0, 0.0, 0.0, 0.0 }, 0.0);
 try {
 simplex.setPoint(index, point);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected
 }
}

 @Test
 public void testSetPoints_nullArray() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 PointValuePair[] points = null;
 try {
 simplex.setPoints(points);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 // Expected
 }
}

 @Test
 public void testSetPoints_invalidSize() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 PointValuePair[] points = new PointValuePair[5];
 try {
 simplex.setPoints(points);
 fail("Expected DimensionMismatchException");
 } catch (DimensionMismatchException e) {
 // Expected
 }
}

 @Test
 public void testSetPoints_correctSize() {
 simplex = new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
 PointValuePair[] points = new PointValuePair[4];
 for (int i = 0; i < points.length; i++) {
 points[i] = new PointValuePair(new double[] { i + 1.0, i + 1.0, i + 1.0 }, i * 2.0);
 }
 simplex.setPoints(points);
 PointValuePair[] expectedPoints = simplex.getPoints();
 assertArrayEquals(points, expectedPoints);
}

}