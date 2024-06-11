import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;
 private MultivariateFunction testFunction;
 private Comparator<PointValuePair> comparator;

 @Test
 public void testConstructorWithInt() {
 simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testConstructorWithIntAndSideLength() {
 simplex = new AbstractSimplex(3, 1.0);
 assertEquals(3, simplex.getDimension());
 }

 @Test(expected = NullArgumentException.class)
 public void testConstructorWithNullSteps() {
 simplex = new AbstractSimplex(null);
 fail("Should throw NullArgumentException");
 }

 @Test(expected = ZeroException.class)
 public void testConstructorWithZeroSteps() {
 simplex = new AbstractSimplex(new double[0]);
 fail("Should throw ZeroException");
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithInvalidReferenceSimplexFirstDimension() {
 simplex = new AbstractSimplex(new double[1][1]);
 fail("Should throw DimensionMismatchException");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithInvalidReferenceSimplexCorrectSize() {
 simplex = new AbstractSimplex(new double[1][2]);
 fail("Should throw NotStrictlyPositiveException");
 }

 @Test(expected = DimensionMismatchException.class)
 public void testConstructorWithInvalidReferenceSimplexEachRowLength() {
 simplex = new AbstractSimplex(new double[2][3]);
 fail("Should throw DimensionMismatchException");
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testConstructorWithEqualVerticesInSimpleX() {
 simplex = new AbstractSimplex(new double[] {0.0, 0.0, 0.0, 0.0});
 fail("Should throw MathIllegalArgumentException");
 }

 @Test
 public void testGetDimension() {
 simplex = new AbstractSimplex(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 simplex = new AbstractSimplex(new double[] {1.0, 2.0, 3.0});
 assertEquals(4, simplex.getSize());
 }

 @Test
 public void testIterateNullFunction() {
 testFunction = null;
 comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return (int) (o1.getValue() - o2.getValue());
 }
 };
 simplex = new SimpleX(3);
 try {
 simplex.iterate(testFunction, comparator);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testIterateNullComparator() {
 testFunction = new SquaredErrorFunction(2);
 comparator = null;
 simplex = new SimpleX(3);
 try {
 simplex.iterate(testFunction, comparator);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testBuild() {
 simplex = new AbstractSimplex(3);
 double[] startPoint = {1.0, 2.0};
 simplex.build(startPoint);
 PointValuePair expected = new PointValuePair(startPoint, Double.NaN);
 PointValuePair actual = simplex.getPoint(0);
 assertEquals(expected, actual);
 for (int i = 1; i < simplex.getSize(); ++i) {
 PointValuePair actual = simplex.getPoint(i);
 assertNotEquals(expected, actual);
 }
}

 @Test
 public void testEvaluateNullFunction() {
 testFunction = null;
 comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return (int) (o1.getValue() - o2.getValue());
 }
 };
 simplex = new SimpleX(3);
 simplex.build(new double[] {1.0, 2.0});
 try {
 simplex.evaluate(testFunction, comparator);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testReplaceWorstPoint() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {1.0, 2.0});
 PointValuePair worstPoint = simplex.getPoint(0);
 PointValuePair newPoint = new PointValuePair(new double[] {3.0, 4.0}, 5.0);
 simplex.replaceWorstPoint(newPoint, comparator);
 PointValuePair replacedPoint = simplex.getPoint(0);
 assertNotEquals(worstPoint, replacedPoint);
 assertEquals(newPoint, replacedPoint);
 }

 @Test
 public void testGetPoints() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {1.0, 2.0});
 PointValuePair[] expected = {new PointValuePair(new double[] {1.0, 2.0}, Double.NaN),
 new PointValuePair(new double[] {2.0, 1.0}, Double.NaN),
 new PointValuePair(new double[] {2.0, 2.0}, Double.NaN),
 new PointValuePair(new double[] {1.0, 1.0}, Double.NaN)};
 PointValuePair[] actual = simplex.getPoints();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetPoint() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {1.0, 2.0});
 PointValuePair expected = new PointValuePair(new double[] {1.0, 2.0}, Double.NaN);
 PointValuePair actual = simplex.getPoint(0);
 assertEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetPointOutOfRange() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {1.0, 2.0});
 PointValuePair actual = simplex.getPoint(-1);
 fail("Should throw OutOfRangeException");
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetPointOutOfRangePlusSize() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {1.0, 2.0});
 PointValuePair actual = simplex.getPoint(5);
 fail("Should throw OutOfRangeException");
 }

 @Test
 public void testSetPoint() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {1.0, 2.0});
 PointValuePair newPoint = new PointValuePair(new double[] {3.0, 4.0}, 5.0);
 simplex.setPoint(0, newPoint);
 PointValuePair actual = simplex.getPoint(0);
 assertEquals(newPoint, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetPointOutOfRange() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {1.0, 2.0});
 PointValuePair newPoint = new PointValuePair(new double[] {3.0, 4.0}, 5.0);
 simplex.setPoint(-1, newPoint);
 fail("Should throw OutOfRangeException");
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetPointOutOfRangePlusSize() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {1.0, 2.0});
 PointValuePair newPoint = new PointValuePair(new double[] {3.0, 4.0}, 5.0);
 simplex.setPoint(5, newPoint);
 fail("Should throw OutOfRangeException");
 }

 @Test
 public void testSetPoints() {
 simplex = new AbstractSimplex(3);
 PointValuePair[] newPoints = {new PointValuePair(new double[] {3.0, 4.0}, 5.0),
 new PointValuePair(new double[] {6.0, 7.0}, 8.0),
 new PointValuePair(new double[] {9.0, 10.0}, 11.0)};
 simplex.setPoints(newPoints);
 PointValuePair[] expected = newPoints;
 PointValuePair[] actual = simplex.getPoints();
 assertArrayEquals(expected, actual);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testSetPointsWithDifferentSize() {
 simplex = new AbstractSimplex(3);
 PointValuePair[] newPoints = {new PointValuePair(new double[] {3.0, 4.0}, 5.0),
 new PointValuePair(new double[] {3.0, 4.0}, 5.0),
 new PointValuePair(new double[] {3.0, 4.0}, 5.0),
 new PointValuePair(new double[] {4.0, 4.0}, 5.0)};
 simplex.setPoints(newPoints);
 fail("Should throw DimensionMismatchException");
 }
}