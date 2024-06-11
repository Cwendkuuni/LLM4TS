import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

 @Test
 public void testAbstractSimplex() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexNullArgument() {
 new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexEmptyConfiguration() {
 new AbstractSimplex(0);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexZeroSteps() {
 double[] steps = new double[3];
 Arrays.fill(steps, 0.0);
 try {
 new AbstractSimplex(steps);
 fail("Expected ZeroException");
 } catch (ZeroException e) {
 // expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexInvalidReferenceSimplex() {
 double[][] referenceSimplex = new double[3][];
 referenceSimplex[0] = new double[3];
 new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexEmptyReferenceSimplex() {
 double[][] referenceSimplex = new double[1][];
 new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexInvalidStartConfiguration() {
 double[][] referenceSimplex = new double[3][3];
 new AbstractSimplex(referenceSimplex);
 }

 @Test
 public void testAbstractSimplexValidReferenceSimplex() {
 double[][] referenceSimplex = new double[][] {
 new double[] { 1.0, 1.0, 1.0 },
 new double[] { 2.0, 2.0, 2.0 },
 new double[] { 3.0, 3.0, 3.0 }
 };
 AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
 assertEquals(3, simplex.getDimension());
 assertEquals(4, simplex.getSize());
 }

 @Test
 public void testBuild() {
 MultivariateFunction function = new ObjectiveFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2) + Math.pow(point[1], 2);
 }
 };
 AbstractSimplex simplex = new AbstractSimplex(3);
 double[] startPoint = new double[] { 1.0, 1.0 };
 simplex.build(startPoint);
 assertEquals(4, simplex.getSize());
 for (int i = 0; i < 4; ++i) {
 PointValuePair vertex = simplex.getPoint(i);
 assertEquals(i < 3 ? startPoint : startPoint.clone(), vertex.getPointRef());
 }
 }

 @Test
 public void testEvaluate() {
 MultivariateFunction function = new ObjectiveFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2) + Math.pow(point[1], 2);
 }
 };
 AbstractSimplex simplex = new AbstractSimplex(3);
 double[] startPoint = new double[] { 1.0, 1.0 };
 simplex.build(startPoint);
 simplex.evaluate(function, null);
 for (int i = 0; i < 4; ++i) {
 PointValuePair vertex = simplex.getPoint(i);
 double value = function.value(vertex.getPointRef());
 assertEquals(vertex.getValue(), value, 0.001);
 }
 }

 @Test
 public void testReplaceWorstPoint() {
 MultivariateFunction function = new ObjectiveFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2) + Math.pow(point[1], 2);
 }
 };
 AbstractSimplex simplex = new AbstractSimplex(3);
 double[] startPoint = new double[] { 1.0, 1.0 };
 simplex.build(startPoint);
 PointValuePair worstPoint = simplex.getPoint(3);
 simplex.replaceWorstPoint(new PointValuePair(new double[] { 2.0, 2.0 }, function.value(new double[] { 2.0, 2.0 })), null);
 PointValuePair newWorstPoint = simplex.getPoint(3);
 assertNotEquals(worstPoint, newWorstPoint);
 }

 @Test
 public void testGetPoints() {
 MultivariateFunction function = new ObjectiveFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2) + Math.pow(point[1], 2);
 }
 };
 AbstractSimplex simplex = new AbstractSimplex(3);
 double[] startPoint = new double[] { 1.0, 1.0 };
 simplex.build(startPoint);
 PointValuePair[] points = simplex.getPoints();
 assertEquals(4, points.length);
 for (int i = 0; i < 4; ++i) {
 PointValuePair vertex = simplex.getPoint(i);
 int index = Arrays.asList(points).indexOf(vertex);
 assertEquals(vertex, points[index]);
 }
 }

 @Test
 public void testIterate() {
 MultivariateFunction function = new ObjectiveFunction() {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0], 2) + Math.pow(point[1], 2);
 }
 };
 Comparator<PointValuePair> comparator = (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());
 AbstractSimplex simplex = new AbstractSimplex(3);
 double[] startPoint = new double[] { 1.0, 1.0 };
 simplex.build(startPoint);
 simplex.iterate(function, comparator);
 PointValuePair bestPoint = simplex.getPoint(0);
 assertEquals(new double[] { 1.0, 1.0 }, bestPoint.getPointRef());
 assertEquals(function.value(new double[] { 1.0, 1.0 }), bestPoint.getValue(), 0.001);
 simplex.replaceWorstPoint(bestPoint, comparator);
 simplex.iterate(function, comparator);
 PointValuePair worstPoint = simplex.getPoint(3);
 PointValuePair newBestPoint = simplex.getPoint(0);
 assertEquals(new double[] { 2.0, 2.0 }, worstPoint.getPointRef());
 assertEquals(Double.NaN, worstPoint.getValue(), 0.001);
 assertEquals(new double[] { 1.0, 1.0 }, newBestPoint.getPointRef());
 assertEquals(1.0, newBestPoint.getValue(), 0.001);
 }
}