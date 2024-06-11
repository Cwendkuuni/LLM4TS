import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizerMinimum() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 Function f = new Function() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
 }
 };
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -5.0, 5.0);
 UnivariatePointValuePair solution = optimizer.optimize(problem);
 assertEquals(-1.0, solution.getPoint(), 1e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerMinimumWithSmallRelativeTolerance() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-9, 1.0e-6, null);
 Function f = new Function() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
 }
 };
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -5.0, 5.0);
 optimizer.optimize(problem);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerMinimumWithNegativeAbsoluteTolerance() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0e-6, null);
 Function f = new Function() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
 }
 };
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -5.0, 5.0);
 optimizer.optimize(problem);
 }

 @Test
 public void testBrentOptimizerMaximum() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 Function f = new Function() {
 @Override
 public double value(double x) {
 return -(Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1);
 }
 };
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -5.0, 5.0);
 UnivariatePointValuePair solution = optimizer.optimize(problem);
 assertEquals(1.0, solution.getPoint(), 1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerWithNullConvergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 }
}