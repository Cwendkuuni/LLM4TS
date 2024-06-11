import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.optim.initializer.StartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePairList;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;

 @Test
 public void testAbstractSimplex_int() {
 simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_int_double() {
 simplex = new AbstractSimplex(3, 2.0);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_doubleArray_throwsException_ifNull() {
 assertThrows(NullArgumentException.class, () -> new AbstractSimplex(null));
 }

 @Test
 public void testAbstractSimplex_doubleArray_throwsException_ifEmpty() {
 assertThrows(ZeroException.class, () -> new AbstractSimplex(new double[0]));
 }

 @Test
 public void testAbstractSimplex_doubleArray() {
 simplex = new AbstractSimplex(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testAbstractSimplex_doubleArrayArray() {
 simplex = new AbstractSimplex(new double[][] {{1.0, 2.0}, {2.0, 1.0}, {1.0, 3.0}});
 assertEquals(2, simplex.getDimension());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplex_doubleArrayArray_throwsException_ifIncorrectDimension() {
 simplex = new AbstractSimplex(new double[][] {{1.0, 2.0}, {2.0}, {1.0, 3.0}});
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplex_doubleArrayArray_throwsException_ifEqualVertices() {
 simplex = new AbstractSimplex(new double[][] {{1.0, 2.0}, {1.0, 2.0}});
 }

 @Test
 public void testGetDimension() {
 simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 simplex = new AbstractSimplex(3);
 assertEquals(4, simplex.getSize());
 }

 @Test
 public void testIterate_throwsAbstractMethodException() {
 assertThrows(AbstractMethodError.class, () -> simplex.iterate(mock(MultivariateFunction.class), mock(Comparator.class)));
 }

 @Test
 public void testBuild_doubleArray_throwsDimensionMismatchException() {
 double[] startPoint = new double[5];
 assertThrows(DimensionMismatchException.class, () -> simplex.build(startPoint));
 }

 @Test
 public void testBuild_doubleArray() {
 simplex.build(new double[] {0.0, 0.0});
 assertEquals(2, simplex.getSize());
 }

 @Test
 public void testEvaluate_MultivariateFunction_Comparator_replacesWorstPoint() {
 MultivariateFunction function = mock(MultivariateFunction.class);
 Comparator<PointValuePair> comparator = mock(Comparator.class);
 simplex.build(new double[] {0.0, 0.0});
 PointValuePair pointToReplace = new PointValuePair(new double[] {1.0, 1.0}, Double.POSITIVE_INFINITY);
 simplex.evaluate(function, comparator);
 simplex.replaceWorstPoint(pointToReplace, comparator);
 PointValuePair worstPointBefore = simplex.getPoint(simplex.getSize() - 1);
 assertNotNull(worstPointBefore);
 assertEquals(Double.POSITIVE_INFINITY, worstPointBefore.getValue(), 0.001);
 PointValuePair worstPointAfter = simplex.getPoint(simplex.getSize() - 1);
 assertNotNull(worstPointAfter);
 assertNotEquals(pointToReplace, worstPointAfter);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetPoint_outOfRangeIndex() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {0.0, 0.0});
 assertThrows(IndexOutOfBoundsException.class, () -> simplex.getPoint(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetPoint_outOfRangeIndex2() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {0.0, 0.0});
 assertThrows(IndexOutOfBoundsException.class, () -> simplex.getPoint(simplex.getSize()));
 }

 @Test
 public void testSetPoint_inRangeIndex() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {0.0, 0.0});
 PointValuePair pointToSet = new PointValuePair(new double[] {1.0, 1.0}, Double.POSITIVE_INFINITY);
 simplex.setPoint(0, pointToSet);
 PointValuePair point = simplex.getPoint(0);
 assertEquals(pointToSet, point);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPoint_outOfRangeIndex() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {0.0, 0.0});
 PointValuePair pointToSet = new PointValuePair(new double[] {1.0, 1.0}, Double.POSITIVE_INFINITY);
 simplex.setPoint(4, pointToSet);
 }

 @Test
 public void testSetPoints_sizeMatch() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {0.0, 0.0});
 PointValuePair[] points = new PointValuePair[4];
 points[0] = new PointValuePair(new double[] {1.0, 1.0}, Double.POSITIVE_INFINITY);
 simplex.setPoints(points);
 PointValuePair point = simplex.getPoint(0);
 assertEquals(points[0], point);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testSetPoints_sizeMismatch() {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[] {0.0, 0.0});
 PointValuePair[] points = new PointValuePair[5];
 simplex.setPoints(points);
 }
}