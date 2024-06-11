import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizerMinimization() {
 // Test case for minimization of a simple quadratic function.
 UnivariateFunction function = new QuadraticFunction(-1, 1, 1);
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariatePointValuePair result = optimizer.optimize(function, new QuadraticFunctionInitialGuess(3));

 // Check the result.
 assertEquals(-1, result.getPoint(), 1e-6);
 assertEquals(0, result.getValue(), 1e-6);
 }

 @Test
 public void testBrentOptimizerMinimizationWithConvergenceChecker() {
 // Test case for minimization of a simple quadratic function using a ConvergenceChecker.
 UnivariateFunction function = new QuadraticFunction(-1, 1, 1);
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
 UnivariatePointValuePair result = optimizer.optimize(function, new QuadraticFunctionInitialGuess(3));

 // Check the result.
 assertEquals(-1, result.getPoint(), 1e-6);
 assertEquals(0, result.getValue(), 1e-6);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerThrowsExceptionWithInvalidAbsoluteThreshold() {
 // Test case for throwing exception with invalid absolute threshold.
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6, null);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerThrowsExceptionWithInvalidRelativeThreshold() {
 // Test case for throwing exception with invalid relative threshold.
 BrentOptimizer optimizer = new BrentOptimizer(-1e-6, 1e-6, null);
 }

 @Test
 public void testBrentOptimizerMaximization() {
 // Test case for maximization of a simple quadratic function.
 UnivariateFunction function = new QuadraticFunction(1, 1, 1);
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariatePointValuePair result = optimizer.optimize(function, new QuadraticFunctionInitialGuess(3));

 // Check the result.
 assertEquals(-1, result.getPoint(), 1e-6); // Reversed sign due to maximization.
 assertEquals(0, result.getValue(), 1e-6);
 }

 @Test
 public void testBrentOptimizerMaximizationWithConvergenceChecker() {
 // Test case for maximization of a simple quadratic function using a ConvergenceChecker.
 UnivariateFunction function = new QuadraticFunction(1, 1, 1);
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
 UnivariatePointValuePair result = optimizer.optimize(function, new QuadraticFunctionInitialGuess(3));

 // Check the result.
 assertEquals(-1, result.getPoint(), 1e-6); // Reversed sign due to maximization.
 assertEquals(0, result.getValue(), 1e-6);
 }

 @Test
 public void testBrentOptimizerWithSingularFunction() {
 // Test case for test with singular function.
 UnivariateFunction function = new SingularFunction();
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 try {
 UnivariatePointValuePair result = optimizer.optimize(function, null);
 fail("Expected exception");
 } catch (ArithmeticException e) {
 // Expected exception.
 }
 }

 @Test
 public void testBrentOptimizerWithNonMonotonicFunction() {
 // Test case for test with non-monotonic function.
 UnivariateFunction function = new NonMonotonicFunction();
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariatePointValuePair result = optimizer.optimize(function, null);
 // Check the result.
 assertTrue(result.getValue() > 0);
 }
}