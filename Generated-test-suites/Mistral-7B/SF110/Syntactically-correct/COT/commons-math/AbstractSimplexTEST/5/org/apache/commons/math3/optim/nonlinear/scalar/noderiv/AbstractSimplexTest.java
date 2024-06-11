import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;

 @Test
 public void testConstructorWithInt() {
 simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testConstructorWithIntAndSideLength() {
 simplex = new AbstractSimplex(3, 2.0);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testConstructorWithSteps() {
 double[] steps = {1.0, 2.0, 3.0};
 simplex = new AbstractSimplex(steps);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testConstructorWithReferenceSimplex() {
 double[][] referenceSimplex = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
 simplex = new AbstractSimplex(referenceSimplex);
 assertEquals(2, simplex.getDimension());
 }

 @Test
 public void testGetDimension() {
 AbstractSimplex simplex1 = new AbstractSimplex(3);
 AbstractSimplex simplex2 = new AbstractSimplex(2);
 assertEquals(3, simplex1.getDimension());
 assertEquals(2, simplex2.getDimension());
 }

 @Test
 public void testGetSize() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = NullArgumentException.class)
 public void testConstructorWithNull() {
 new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testConstructorWithEmptyArray() {
 new AbstractSimplex(new double[0]);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithInvalidNumberOfPoints() {
 new AbstractSimplex(new double[]{-1.0, 2.0, 3.0});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithMismatchedPoints() {
 double[] steps = {1.0, 2.0};
 new AbstractSimplex(new double[][]{steps, new double[1]});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithMismatchedNumberOfPointsAndDimensions() {
 double[][] referenceSimplex = {{1.0, 2.0}, {2.0, 3.0}};
 new AbstractSimplex(referenceSimplex);
 }

 @Test
 public void testEqualVerticesInSameSimplex() {
 try {
 new AbstractSimplex(new double[]{-1.0, 0.0, 0.0, 0.0});
 fail("Should have thrown ZeroException");
 } catch (ZeroException e) {
 // Expected
 }
}

 @Test
 public void testIterate() {
 MultivariateFunction function = new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return 0.0;
 }
};
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 simplex = new AbstractSimplex(3);
 simplex.iterate(function, comparator);
}

 @Test(expected = NullArgumentException.class)
 public void testIterateWithNullFunction() {
 simplex = new AbstractSimplex(3);
 simplex.iterate(null, comparator);
}

 @Test
 public void testIterateWithEmptyComparator() {
 MultivariateFunction function = new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return 0.0;
 }
 };
 simplex = new AbstractSimplex(3);
 simplex.iterate(function, (Comparator<PointValuePair>)null);
}

 @Test
 public void testBuild() {
 double[] startPoint = {1.0, 2.0, 3.0};
 simplex = new AbstractSimplex(3);
 simplex.build(startPoint);
 PointValuePair[] points = simplex.getPoints();
 for (int i = 0; i < points.length; i++) {
 PointValuePair point = points[i];
 assertEquals(startPoint[i*3], point.getPointRef()[0], 0.001);
 assertEquals(startPoint[i*3+1], point.getPointRef()[1], 0.001);
 assertEquals(startPoint[i*3+2], point.getPointRef()[2], 0.001);
 }
}

 @Test(expected = DimensionMismatchException.class)
 public void testBuildWithInvalidStartPoint() {
 double[] startPoint = {1.0, 2.0};
 simplex = new AbstractSimplex(3);
 simplex.build(startPoint);
}

 @Test
 public void testEvaluate() {
 MultivariateFunction function = new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0);
 }
 };
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 simplex = new AbstractSimplex(2);
 simplex.build(new double[]{0.0, 0.0});
 simplex.evaluate(function, comparator);
 assertEquals(Double.POSITIVE_INFINITY, simplex.getPoint(0).getValue(), 0.001);
 assertEquals(0.0, simplex.getPoint(1).getValue(), 0.001);
}

 @Test
 public void testReplaceWorstPoint() {
 MultivariateFunction function = new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0);
 }
 };
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 simplex = new AbstractSimplex(2);
 simplex.build(new double[]{0.0, 0.0});
 simplex.evaluate(function, comparator);
 PointValuePair worstPoint = simplex.getPoint(0);
 simplex.replaceWorstPoint(new PointValuePair(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY), comparator);
 PointValuePair newWorstPoint = simplex.getPoint(0);
 assertNotEquals(worstPoint, newWorstPoint);
}

 @Test
 public void testGetPoints() {
 MultivariateFunction function = new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0);
 }
 };
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 simplex = new AbstractSimplex(2);
 simplex.build(new double[]{0.0, 0.0});
 simplex.evaluate(function, comparator);
 PointValuePair[] points = simplex.getPoints();
 PointValuePair point1 = simplex.getPoint(0);
 PointValuePair point2 = simplex.getPoint(1);
 assertArrayEquals(new Object[] {point1, point2}, new Object[] {points[0], points[1]});
}

 @Test
 public void testGetPoint() {
 MultivariateFunction function = new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0);
 }
 };
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 simplex = new AbstractSimplex(2);
 simplex.build(new double[]{0.0, 0.0});
 simplex.evaluate(function, comparator);
 PointValuePair point = simplex.getPoint(1);
 double[] expected = new double[] {0.0, 0.0};
 assertArrayEquals(expected, point.getPointRef());
}

 @Test
 public void testSetPoint() {
 MultivariateFunction function = new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0);
 }
 };
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 simplex = new AbstractSimplex(2);
 simplex.build(new double[]{0.0, 0.0});
 simplex.evaluate(function, comparator);
 PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY);
 simplex.setPoint(0, point);
 PointValuePair newPoint = simplex.getPoint(0);
 assertNotEquals(simplex.getPoint(0), newPoint);
}

 @Test
 public void testSetPoints() {
 MultivariateFunction function = new MultivariateFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0);
 }
 };
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 simplex = new AbstractSimplex(2);
 simplex.build(new double[]{0.0, 0.0});
 simplex.evaluate(function, comparator);
 PointValuePair[] points = new PointValuePair[] {new PointValuePair(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY), new PointValuePair(new double[]{2.0, 2.0}, Double.POSITIVE_INFINITY)};
 simplex.setPoints(points);
 PointValuePair[] newPoints = simplex.getPoints();
 assertArrayEquals(points, newPoints);
}

 @Test
 public void testCreateHypercubeSteps() {
 double[] steps = AbstractSimplex.createHypercubeSteps(4, 2.0);
 assertEquals(4, steps.length);
 for (int i = 0; i < steps.length; i++) {
 assertEquals(2.0, steps[i], 0.001);
 }
}

}