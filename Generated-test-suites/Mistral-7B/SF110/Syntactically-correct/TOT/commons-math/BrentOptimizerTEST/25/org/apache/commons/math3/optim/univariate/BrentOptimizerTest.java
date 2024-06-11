import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizerMinimization() {
 Function f = new Function() {@Override public double value(double x) { return Math.pow(x, 2); }};
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, 1.0);
 OptimizationResult result = optimizer.optimize(problem);
 UnivariatePointValuePair solution = result.getPoint();
 assertEquals(-1.0, solution.getX(), 1.0e-6);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerWithSmallRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-12, 1.0e-6);
 }

 @Test
 public void testBrentOptimizerWithConvergenceChecker() {
 Function f = new Function() {@Override public double value(double x) { return Math.abs(Math.sin(x)) - 0.1; }} ;
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new ConvergenceChecker<UnivariatePointValuePair>() {
 @Override public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return current.getValue() <= 0.0;
 }});
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -10.0, 10.0);
 OptimizationResult result = optimizer.optimize(problem);
 assertEquals(-3.141592653589793, result.getPoint().getX(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerWithMaximumFunction() {
 Function f = new Function() {@Override public double value(double x) { return Math.max(1.0, Math.pow(x, 2)); }} ;
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -10.0, 10.0);
 OptimizationResult result = optimizer.optimize(problem);
 assertEquals(1.0, result.getPoint().getX(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerMaximumFunctionWithInitialGuessGreaterThanMaximum() {
 Function f = new Function() {@Override public double value(double x) { return Math.max(1.0, Math.pow(x, 2)); }} ;
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, 11.0, 10.0);
 OptimizationResult result = optimizer.optimize(problem);
 assertNotNull(result);
 }
}