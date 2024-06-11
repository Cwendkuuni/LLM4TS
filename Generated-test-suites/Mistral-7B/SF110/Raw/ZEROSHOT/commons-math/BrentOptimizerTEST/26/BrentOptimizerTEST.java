import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.ScalarFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
 assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerWithSmallRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-8, 1.0e-5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, -1.0e-5);
 }

 @Test
 public void testBrentOptimizerMinimization() {
 ScalarFunction function = new AbstractScalarFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
 UnivariateOptimizerResult result = optimizer.optimize(function, new SimpleBounds(0.0, 2.0), new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 1.0;
 }
 });
 UnivariatePointValuePair solution = result.getPoint();
 assertEquals(0.0, solution.getX(), 1e-5);
 assertEquals(-2.4495, solution.getValue(), 1e-5);
 }

 @Test
 public void testBrentOptimizerMaximization() {
 ScalarFunction function = new AbstractScalarFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) + 2 * x * Math.pow(Math.E, x);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
 UnivariateOptimizerResult result = optimizer.optimize(function, new SimpleBounds(0.0, 2.0), new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 1.0;
 }
 });
 UnivariatePointValuePair solution = result.getPoint();
 assertEquals(1.125, solution.getX(), 1e-5);
 assertEquals(14.0784, solution.getValue(), 1e-5);
 }

 @Test
 public void testBrentOptimizerConvergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, new BrentConvergenceChecker(1000));
 ScalarFunction function = new AbstractScalarFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 UnivariateOptimizerResult result = optimizer.optimize(function, new SimpleBounds(0.0, 10.0), new ValueInitializer() {
 @Override
 public double getInitialX() {
 return 5.0;
 }
 });
 UnivariatePointValuePair solution = result.getPoint();
 assertEquals(2.0, solution.getX(), 1e-5);
 assertEquals(4.0, solution.getValue(), 1e-5);
 assertTrue(optimizer.getIterations() < 1000);
 }
}

 class BrentConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 private int maxIterations;

 public BrentConvergenceChecker(int maxIterations) {
 this.maxIterations = maxIterations;
 }

 @Override
 public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return iteration >= maxIterations;
 }
}