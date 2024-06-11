import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBoundingBox;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;
 private MultivariateFunction testFunction;

 @Before
 public void setUp() {
 testFunction = new TestFunction();
 simplex = new SimplexInitializer(2);
 }

 @After
 public void tearDown() {
 simplex = null;
 testFunction = null;
 }

 @Test
 public void testGetDimension() {
 assertEquals(2, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 assertEquals(3, simplex.getSize());
 }

 @Test
 public void testIterate() {
 GoalType goalType = GoalType.MINIMIZE;
 InitialGuess initialGuess = new InitialGuess(simplex.getPoint(0).getPointRef());
 MaxEval maxEval = new MaxEval(1000);
 SimpleBoundingBox bounds = new SimpleBoundingBox(new PointValuePair(new double[2], Double.NEGATIVE_INFINITY), new PointValuePair(new double[2], Double.POSITIVE_INFINITY));
 SimplexIterator optimizer = new SimplexIterator(testFunction, goalType, initialGuess, maxEval, bounds);
 simplex.iterate(testFunction, Comparator.comparingDouble(pair -> pair.getValue()));
 OptimizationResult result = optimizer.optimize();
 assertEquals(0.0, result.getValue(), 0.001);
 }

 @Test
 public void testBuild() {
 double[] startPoint = {1.0, 1.0};
 simplex.build(startPoint);
 PointValuePair point = simplex.getPoint(0);
 assertArrayEquals(startPoint, point.getPointRef());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testBuildWithInvalidDimension() {
 double[] startPoint = {1.0};
 simplex.build(startPoint);
 fail();
 }

 @Test
 public void testEvaluate() {
 simplex.evaluate(testFunction, Comparator.comparingDouble(PointValuePair::getValue));
 PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
 double worstValue = testFunction.value(worstPoint.getPointRef());
 assertEquals(Double.POSITIVE_INFINITY, worstValue, 0.001);
 }

 @Test
 public void testReplaceWorstPoint() {
 PointValuePair pointToReplace = new PointValuePair(new double[2], 10.0);
 simplex.replaceWorstPoint(pointToReplace, Comparator.comparingDouble(PointValuePair::getValue));
 PointValuePair replacedPoint = simplex.getPoint(simplex.getSize() - 1);
 assertEquals(pointToReplace, replacedPoint);
 }

 @Test(expected = OutOfRangeException.class)
 public void testSetPointWithInvalidIndex() {
 PointValuePair point = new PointValuePair(new double[2], 0.0);
 simplex.setPoint(-1, point);
 fail();
 }

 @Test(expected = DimensionMismatchException.class)
 public void testSetPointsWithInvalidSize() {
 PointValuePair[] points = new PointValuePair[1];
 simplex.setPoints(points);
 fail();
 }
}

 class TestFunction implements MultivariateFunction {
 @Override
 public double value(double[] point) {
 return Math.pow(point[0] - 1.0, 2.0) + Math.pow(point[1] - 1.0, 2.0);
 }
}

 class SimplexInitializer extends AbstractSimplex {
 public SimplexInitializer(int n) {
 super(n);
 }
}