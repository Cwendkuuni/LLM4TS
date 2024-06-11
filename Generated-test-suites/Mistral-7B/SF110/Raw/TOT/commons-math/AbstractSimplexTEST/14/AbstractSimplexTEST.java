package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.*;

@RunWith(Parameterized.class)
public class AbstractSimplexTest {

 private AbstractSimplex simplex;
 private MultivariateFunction function;

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 new Object[] { new SphereFunction(1.0), new double[] { 1.0, 1.0, 1.0 } },
 new Object[] { new RastriginFunction(10), new double[] { -5.123, -3.21, -1.414 } },
 new Object[] { new RosenbrockFunction(5), new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 } }
 });
 }

 public AbstractSimplexTest(MultivariateFunction function, double[] startPoint) {
 this.function = function;
 simplex = new SimplexNelderMead(function.getDimension());
 simplex.build(startPoint);
 }

 @Test
 public void testGetDimension() {
 assertEquals(function.getDimension(), simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 assertEquals(function.getDimension() + 1, simplex.getSize());
 }

 @Test
 public void testIterate() {
 simplex.iterate(function, ComparatorUtils.MIN_VALUE_COMPARATOR);
assertEquals(simplex.getSize(), function.getNumberOfVariables());
}

 @Test(expected = IllegalArgumentException.class)
 public void testIterateWithNullFunction() {
 simplex.iterate(null, ComparatorUtils.MIN_VALUE_COMPARATOR);
}

 @Test(expected = NullPointerException.class)
 public void testIterateWithNullComparator() {
 simplex.iterate(function, null);
}

 @Test
 public void testBuild() {
 PointValuePair[] points = simplex.getPoints();
 for (PointValuePair point : points) {
 assertEquals(function.getDimension(), point.getPointRef().length);
 }
}

 @Test(expected = IllegalStateException.class)
 public void testSetPointWithNaNValue() {
 PointValuePair point = new PointValuePair(new double[] { 0.0 }, Double.NaN);
 simplex.setPoint(0, point);
}

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPointWithInvalidIndex() {
 PointValuePair point = new PointValuePair(new double[] { 0.0 }, 0.0);
 simplex.setPoint(-1, point);
}

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPointsWithInvalidSize() {
 PointValuePair[] points = new PointValuePair[simplex.getSize() - 1];
 simplex.setPoints(points);
}

 @Test
 public void testReplaceWorstPoint() {
 PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
 PointValuePair newPoint = new PointValuePair(new double[] { 0.0 }, 0.0);
 simplex.replaceWorstPoint(newPoint, ComparatorUtils.MIN_VALUE_COMPARATOR);
 assertNotEquals(worstPoint, simplex.getPoint(simplex.getSize() - 1));
}

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReplaceWorstPointWithInvalidIndex() {
 PointValuePair worstPoint = simplex.getPoint(simplex.getSize());
 PointValuePair newPoint = new PointValuePair(new double[] { 0.0 }, 0.0);
 simplex.replaceWorstPoint(newPoint, ComparatorUtils.MIN_VALUE_COMPARATOR);
}

 @Test
 public void testEvaluate() {
 simplex.evaluate(function, ComparatorUtils.MIN_VALUE_COMPARATOR);
 PointValuePair bestPoint = simplex.getPoint(0);
 double bestValue = bestPoint.getValue();
 PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
 double worstValue = worstPoint.getValue();
 assertTrue(bestValue <= worstValue);
}

 @Test(expected = NullPointerException.class)
 public void testEvaluateWithNullComparator() {
 simplex.evaluate(function, null);
}

 @Test
 public void testGetPoint() {
 PointValuePair point = simplex.getPoint(0);
 assertArrayEquals(startPoint, point.getPointRef(), 0.001);
}

 @Test
 public void testGetPointWithInvalidIndex() {
 try {
 simplex.getPoint(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
}

}