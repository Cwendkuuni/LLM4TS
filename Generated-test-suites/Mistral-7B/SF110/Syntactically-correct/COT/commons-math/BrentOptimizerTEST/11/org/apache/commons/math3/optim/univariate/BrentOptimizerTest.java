import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

 private UnivariateFunction testFunction = new MyTestFunction();

 @Test
 public void testConstructorWithRelAndAbs() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
 assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithRelTooSmall() {
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001, null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithAbsNotPositive() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.001, null);
 }

 @Test
 public void testDoOptimize_minimize() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
 UnivariatePointValuePair result = optimizer.optimize(testFunction, -1.0, 1.0);
 assertNotNull(result);
 assertEquals(-0.712776686857397, result.getPoint(), 0.001);
 }

 @Test
 public void testDoOptimize_maximize() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 UnivariatePointValuePair result = optimizer.optimize(testFunction, -1.0, 1.0);
 assertNotNull(result);
 assertEquals(0.712776686857397, result.getPoint(), 0.001);
 }

 @Test
 public void testDoOptimize_convergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MyTestConvergenceChecker());
 UnivariatePointValuePair result = optimizer.optimize(testFunction, -1.0, 1.0);
 assertNotNull(result);
 }

 @Test
 public void testBest() {
 UnivariatePointValuePair p1 = new UnivariatePointValuePair(1.0, 1.0);
 UnivariatePointValuePair p2 = new UnivariatePointValuePair(2.0, 2.0);
 UnivariatePointValuePair p3 = new UnivariatePointValuePair(3.0, 3.0);
 assertEquals(p1, BrentOptimizer.this.best(null, p2, true));
 assertEquals(p2, BrentOptimizer.this.best(p1, p3, false));
 }

 @Test
 public void testGoldenSection() {
 assertEquals(0.5 * (3.0 - Math.sqrt(5.0)), BrentOptimizer.GOLDEN_SECTION, 0.001);
 }

 private class MyTestFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + 1;
 }
 }

 private static class MyTestConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair best) {
 return (iter > 10);
 }
 }
}