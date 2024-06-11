import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

 @Test
 public void testSimpleMinimization() {
 UnivariateFunction f = x -> Math.pow(x, 2); // a simple quadratic function for testing
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01); // setting the relative and absolute tolerances
 double result = optimizer.optimize(f, -5.0, 5.0).getValue(); // optimizing the function in the interval [-5, 5]
 assertEquals(0.0, result, 0.001); // checking if the result is close to the exact minimum value (0.0)
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRelativeTolerance() {
 new BrentOptimizer(-0.001, 0.01); // relative tolerance is negative
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidAbsoluteTolerance() {
 new BrentOptimizer(0.001, -0.01); // absolute tolerance is negative
 }

 @Test
 public void testInvalidMinValue() {
 UnivariateFunction f = x -> x * x; // a simple quadratic function for testing
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01); // setting the relative and absolute tolerances
 try {
 optimizer.optimize(f, -1.0, 1.0); // optimizing the function in the interval [-1, 1], which contains a negative minimum value
 } catch (IllegalArgumentException e) {
 assertEquals("The interval must not contain a minimum value.", e.getMessage());
 }
 }

 @Test
 public void testInvalidMaxValue() {
 UnivariateFunction f = x -> x * x; // a simple quadratic function for testing
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01); // setting the relative and absolute tolerances
 try {
 optimizer.optimize(f, 1.0, -1.0); // optimizing the function in the interval [1, -1], which contains a maximum value
 } catch (IllegalArgumentException e) {
 assertEquals("The interval must not contain a maximum value.", e.getMessage());
 }
 }

 @Test
 public void testConvergenceChecker() {
 UnivariateFunction f = x -> x * Math.sin(x); // a function with multiple local minima and maxima
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01, new MyConvergenceChecker()); // setting the relative and absolute tolerances and defining a custom convergence checker
 double result = optimizer.optimize(f, -10.0, 10.0).getValue(); // optimizing the function in the interval [-10, 10]
 System.out.println("Result: " + result);
 }

 // Custom convergence checker
 private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
 return iteration > 10; // checking for convergence after a fixed number of iterations
 }
 }

}