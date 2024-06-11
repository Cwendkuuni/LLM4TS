import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.UniformRealInitializer;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.NumberIsTooSmallException;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private final BrentOptimizer optimizer;
 private final UnivariateFunction function;

 public BrentOptimizerTest() {
 this.optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 this.function = new MyFunction();
 }

 @Test
 public void testConstructorWithMinRelativeAndAbsoluteToleranceAndChecker() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return current.getValue() == previous.getValue();
 }
 });
 assertNotNull(optimizer);
 }

 @Test
 public void testConstructorWithMinRelativeAndAbsoluteTolerance() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 assertNotNull(optimizer);
 }

 @Test
 public void testDoOptimize_Minimize_InitialValuesOutOfOrder() {
 PointValuePair result = optimizer.optimize(new UnivariateOptimizationProblem(function, new UniformRealInitializer(-10.0, 10.0)) {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 });
 assertEquals(3.0, result.getPoint().getValue(), 1e-6);
 }

 @Test
 public void testDoOptimize_Minimize_InitialValuesInOrder() {
 PointValuePair result = optimizer.optimize(new UnivariateOptimizationProblem(function, new UniformRealInitializer(0.0, 10.0)) {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 });
 assertEquals(2.0, result.getPoint().getValue(), 1e-6);
 }

 @Test
 public void testDoOptimize_Maximize_InitialValuesOutOfOrder() {
 PointValuePair result = optimizer.optimize(new UnivariateOptimizationProblem(function, new UniformRealInitializer(10.0, -10.0)) {
 @Override
 public GoalType getGoalType() {
 return GoalType.MAXIMIZE;
 }
 });
 assertEquals(-3.0, result.getPoint().getValue(), 1e-6);
 }

 @Test
 public void testDoOptimize_Maximize_InitialValuesInOrder() {
 PointValuePair result = optimizer.optimize(new UnivariateOptimizationProblem(function, new UniformRealInitializer(0.0, 10.0)) {
 @Override
 public GoalType getGoalType() {
 return GoalType.MAXIMIZE;
 }
 });
 assertEquals(4.0, result.getPoint().getValue(), 1e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructWithInvalidMinRelativeTolerance() {
 optimizer = new BrentOptimizer(-1.0e-6, 1.0e-6, null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructWithInvalidAbsoluteTolerance() {
 optimizer = new BrentOptimizer(1.0e-6, -1.0e-6, null);
 }

 @Test
 public void testBest() {
 PointValuePair a = new PointValuePair(1.0, 1.0);
 PointValuePair b = new PointValuePair(2.0, 2.0);
 PointValuePair c = new PointValuePair(3.0, 3.0);
 assertTrue(optimizer.best(a, b, true).getValue() <= b.getValue());
 assertTrue(optimizer.best(a, b, false).getValue() >= b.getValue());
 assertTrue(optimizer.best(a, b, true).getValue() <= c.getValue());
 assertTrue(optimizer.best(a, b, false).getValue() >= c.getValue());
 assertTrue(optimizer.best(a, b, true).getValue() <= optimizer.best(b, c, true).getValue());
 assertTrue(optimizer.best(a, b, false).getValue() >= optimizer.best(b, c, false).getValue());
 }

 @Test
 public void testGoldenSection() {
 double a = 0.0;
 double b = 1.0;
 double x = 0.5 * (3.0 - Math.sqrt(5.0)) * (b - a) + a;
 assertEquals(0.38196601125011003, x, 1e-6);
 }

 @Test
 public void testMinRelativeTolerance() {
 double x = 0.000001;
 double tol = 2.0 * Math.ulp(x) + 1e-6;
 assertEquals(tol, BrentOptimizer.MIN_RELATIVE_TOLERANCE, 1e-6);
 }
}

class MyFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2.0) + Math.pow(Math.cos(x), 2.0);
 }
}