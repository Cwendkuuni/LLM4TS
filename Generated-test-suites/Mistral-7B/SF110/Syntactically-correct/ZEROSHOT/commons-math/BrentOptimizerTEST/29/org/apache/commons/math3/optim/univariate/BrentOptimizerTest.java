import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 assertNotNull(optimizer);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInvalidAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0, -1.0, null);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testInvalidRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0.5, 1.0e-6, null);
 }

 @Test
 public void testBrentOptimizerWithConvergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return current.getValue() <= previous.getValue() && iteration > 10;
 }
 });
 assertNotNull(optimizer);
 }

 @Test
 public void testBrentOptimizerMinimization() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 4 * x + 1;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 PointValuePair solution = optimizer.optimize(function, -5.0, 5.0);
 assertTrue(solution != null);
 assertArrayEquals(new double[]{2.0, 1.0}, new double[]{solution.getPoint()[0], solution.getValue()}, 1e-6);
 }

 @Test
 public void testBrentOptimizerMaximization() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) + 4 * x + 1;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 PointValuePair solution = optimizer.optimize(function, -5.0, 5.0);
 assertTrue(solution != null);
 assertArrayEquals(new double[]{-2.0, 1.0}, new double[]{solution.getPoint()[0], solution.getValue()}, 1e-6);
 }

 @Test
 public void testBrentOptimizerMinimizationWithConvergenceChecker() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 4 * x + 1;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return current.getValue() <= previous.getValue() && iteration > 10;
 }
 });
 PointValuePair solution = optimizer.optimize(function, -5.0, 5.0);
 assertTrue(solution != null);
 assertArrayEquals(new double[]{2.0, 1.0}, new double[]{solution.getPoint()[0], solution.getValue()}, 1e-6);
 }

 @Test
 public void testBrentOptimizerMaximizationWithConvergenceChecker() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) + 4 * x + 1;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return current.getValue() <= previous.getValue() && iteration > 10;
 }
 });
 PointValuePair solution = optimizer.optimize(function, -5.0, 5.0);
 assertTrue(solution != null);
 assertArrayEquals(new double[]{-2.0, 1.0}, new double[]{solution.getPoint()[0], solution.getValue()}, 1e-6);
 }
}