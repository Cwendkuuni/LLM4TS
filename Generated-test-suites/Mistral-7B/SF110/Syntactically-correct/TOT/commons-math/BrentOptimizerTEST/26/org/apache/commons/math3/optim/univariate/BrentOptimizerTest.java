import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizerTest;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.ValueBoundFunction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest extends BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 UnivariateFunction function = new ValueBoundFunction("test function", 0.0, 10.0, new MyFunction());
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 PointValuePair solution = optimizer.optimize(function);
 assertEquals(2.0, solution.getPoint(), 1.0e-6);
 assertEquals(0.5, solution.getValue(), 1.0e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerInvalidRelativeThreshold() {
 new BrentOptimizer(-1.0, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerInvalidAbsoluteThreshold() {
 new BrentOptimizer(1.0, -1.0);
 }

 @Test
 public void testBrentOptimizerNullConvergenceChecker() {
 new BrentOptimizer(1.0e-6, 1.0e-6, null);
 }

 @Test
 public void testBrentOptimizerConvergenceChecker() {
 ConvergenceChecker<PointValuePair> checker = new MyConvergenceChecker();
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, checker);
 UnivariateFunction function = new ValueBoundFunction("test function", 0.0, 10.0, new MyFunction());
 PointValuePair solution = optimizer.optimize(function);
 checker.assertConverged(0, null, solution);
 }

 @Test
 public void testBrentOptimizerInvalidStartingPoint() {
 UnivariateFunction function = new ValueBoundFunction("test function", 0.0, 10.0, new MyFunction());
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 assertEquals(Double.NaN, optimizer.optimize(function, -1.0).getPoint(), 0.0);
 }

 @Test
 public void testBrentOptimizerMaximumReached() {
 UnivariateFunction function = new ValueBoundFunction("test function", 0.0, 10.0, new MyFunction());
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 assertEquals(10.0, optimizer.optimize(function, 10.0).getPoint(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerMinimumReached() {
 UnivariateFunction function = new ValueBoundFunction("test function", 0.0, 10.0, new MyFunction());
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 assertEquals(0.0, optimizer.optimize(function, 2.0).getPoint(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerNonConvergence() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 PointValuePair solution = optimizer.optimize(function);
 assertFalse(optimizer.isConverged());
 }

 @Test
 public void testBrentOptimizerGoalTypeMinimize() {
 UnivariateFunction function = new ValueBoundFunction("test function", 0.0, 10.0, new MyFunction());
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, GoalType.MINIMIZE);
 PointValuePair solution = optimizer.optimize(function);
 assertEquals(0.0, solution.getPoint(), 1.0e-6);
 assertEquals(-1.0, solution.getValue(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerGoalTypeMaximize() {
 UnivariateFunction function = new ValueBoundFunction("test function", 0.0, 10.0, new MyFunction());
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, GoalType.MAXIMIZE);
 PointValuePair solution = optimizer.optimize(function);
 assertEquals(10.0, solution.getPoint(), 1.0e-6);
 assertEquals(1.0, solution.getValue(), 1.0e-6);
 }
}

class MyFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4.0;
 }
}

class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 @Override
 public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
 return (previous == null && current != null) || (previous != null && current != null && current.getValue() - previous.getValue() < 1.0e-6);
 }
}