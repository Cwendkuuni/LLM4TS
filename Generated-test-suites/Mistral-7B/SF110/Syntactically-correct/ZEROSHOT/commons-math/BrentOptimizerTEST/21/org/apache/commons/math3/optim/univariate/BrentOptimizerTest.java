import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

 private FuncFunction function = new FuncFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
 }
 };

 @Test
 public void testBrentOptimizerMinimum() throws OptimizeException {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-8, null);
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem() {
 @Override
 public FuncFunction getObjectiveFunction() {
 return function;
 }

 @Override
 public double getMin() {
 return -1.0;
 }

 @Override
 public double getMax() {
 return 1.0;
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(problem);
 assertEquals(-0.4285715255731328, result.getPoint(), 1e-6);
 }

 @Test
 public void testBrentOptimizerMaximum() throws OptimizeException {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-8, null);
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem() {
 @Override
 public FuncFunction getObjectiveFunction() {
 return function.negate();
 }

 @Override
 public double getMin() {
 return 1.0;
 }

 @Override
 public double getMax() {
 return 1.0;
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(problem);
 assertEquals(0.4285715255731328, result.getPoint(), 1e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerMinRelativeThresholdTooSmall() {
 BrentOptimizer optimizer = new BrentOptimizer(0.00001, 1.0e-8, null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerAbsoluteThresholdNotPositive() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0, null);
 }

 @Test
 public void testBrentOptimizerConvergenceChecker() throws OptimizeException {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-8, new MyConvergenceChecker());
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem() {
 @Override
 public FuncFunction getObjectiveFunction() {
 return function;
 }

 @Override
 public double getMin() {
 return -1.0;
 }

 @Override
 public double getMax() {
 return 1.0;
 }
 };
 try {
 optimizer.optimize(problem);
 fail("Expected ConvergenceException not thrown.");
 } catch (ConvergenceException e) {
 // Expected
 }
 }

 private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return false;
 }
 }

}