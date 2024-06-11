import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + 1;
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public UnivariateFunction getObjectiveFunction() {
 return function;
 }
 @Override
 public double getLowerBound(int dimension) {
 return -5.0;
 }
 @Override
 public double getUpperBound(int dimension) {
 return 5.0;
 }
 };
 UnivariatePointValuePair solution = optimizer.optimize(problem);
 assertEquals(0.0, solution.getPoint(), 1.0e-6);
 assertEquals(-0.672671931, solution.getValue(), 1.0e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testInvalidRelativeThreshold() {
 new BrentOptimizer(0.0, 1.0e-6);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInvalidAbsoluteThreshold() {
 new BrentOptimizer(1.0e-6, 0.0);
 }

 @Test
 public void testBrentOptimizerWithConvergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new MyConvergenceChecker());
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 OptimizationProblem problem = new OptimizationProblem() {
 @Override
 public UnivariateFunction getObjectiveFunction() {
 return function;
 }
 @Override
 public double getLowerBound(int dimension) {
 return 0.0;
 }
 @Override
 public double getUpperBound(int dimension) {
 return 10.0;
 }
 };
 try {
 UnivariatePointValuePair solution = optimizer.optimize(problem);
 fail("Expected ConvergenceException not thrown");
 } catch (ConvergenceException e) {
 // Expected
 }
 }

 private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iterationNumber, UnivariatePointValuePair currentValue, UnivariatePointValuePair previousValue) {
 return currentValue.getValue() - previousValue.getValue() < 1.0e-9;
 }
 }
}