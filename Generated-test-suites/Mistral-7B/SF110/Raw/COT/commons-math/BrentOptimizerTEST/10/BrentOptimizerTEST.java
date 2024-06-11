import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;
 private UnivariateFunction function;

 @Before
 public void setUp() {
 function = new SinFunction();
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, null);
 }

 @Test
 public void testConstructorWithRelativeAndAbsoluteToleranceAndChecker() {
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5, new DummyConvergenceChecker());
 assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithRelativeToleranceBelowMinimum() {
 optimizer = new BrentOptimizer(0.5, 1.0E-5, null);
 fail("Should throw NumberIsTooSmallException for relative tolerance below minimum.");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeAbsoluteTolerance() {
 optimizer = new BrentOptimizer(1.0E-5, -1.0, null);
 fail("Should throw NotStrictlyPositiveException for negative absolute tolerance.");
 }

 @Test
 public void testConstructorWithRelativeAndAbsoluteTolerance() {
 optimizer = new BrentOptimizer(1.0E-5, 1.0E-5);
 assertNotNull(optimizer);
 }

 @Test
 public void testDoOptimize_InitialValuesOutOfOrder_FindMinimum() {
 PointValuePair solution = optimizer.optimize(function, 2.0, 1.0);
 assertEquals(0.841470985008, solution.getPoint(), 1e-6);
 }

 @Test
 public void testDoOptimize_InitialValuesInOrder_FindMinimum() {
 PointValuePair solution = optimizer.optimize(function, 1.0, 2.0);
 assertEquals(0.841470985008, solution.getPoint(), 1e-6);
 }

 @Test
 public void testDoOptimize_InitialValuesTooClose_FindMinimum() {
 PointValuePair solution = optimizer.optimize(function, 1.1, 1.0);
 assertEquals(0.841470985008, solution.getPoint(), 1e-6);
 }

 @Test
 public void testDoOptimize_InitialValuesTooFar_FindMinimum() {
 PointValuePair solution = optimizer.optimize(function, 0.1, 10.0);
 assertEquals(0.841470985008, solution.getPoint(), 1e-6);
 }

 @Test
 public void testDoOptimize_FunctionThrowsException_ReturnNullSolution() {
 function = new FunctionWithException();
 PointValuePair solution = optimizer.optimize(function, 1.0, 2.0);
 assertNull(solution);
 }

 @Test
 public void testBest_NullFirstArgument_ReturnSecondArgument() {
 PointValuePair first = null;
 PointValuePair second = new PointValuePair(0.1, 1.0);
 PointValuePair result = optimizer.best(first, second, true);
 assertEquals(second, result);
 }

 @Test
 public void testBest_NullSecondArgument_ReturnFirstArgument() {
 PointValuePair first = new PointValuePair(0.1, 1.0);
 PointValuePair second = null;
 PointValuePair result = optimizer.best(first, second, true);
 assertEquals(first, result);
 }

 @Test
 public void testBest_Minimize_ReturnMinimum() {
 PointValuePair first = new PointValuePair(1.0, 3.0);
 PointValuePair second = new PointValuePair(0.1, 1.0);
 PointValuePair result = optimizer.best(first, second, true);
 assertEquals(second, result);
 }

 @Test
 public void testBest_Maximize_ReturnMaximum() {
 PointValuePair first = new PointValuePair(1.0, 3.0);
 PointValuePair second = new PointValuePair(2.0, 4.0);
 PointValuePair result = optimizer.best(first, second, false);
 assertEquals(second, result);
 }
}

class DummyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 @Override
 public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
 return true;
 }
}

class SinFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
}

class FunctionWithException implements UnivariateFunction {
 @Override
 public double value(double x) {
 throw new IllegalArgumentException("Function with exception.");
 }
}