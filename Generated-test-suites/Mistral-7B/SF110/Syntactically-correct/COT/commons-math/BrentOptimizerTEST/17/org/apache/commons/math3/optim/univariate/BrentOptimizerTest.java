import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateOptimizer optimizer;

 @Before
 public void setUp() {
 optimizer = new BrentOptimizer(1e-6, 1e-6);
 }

 @Test
 public void testConstructorWithRelAndAbs() {
 optimizer = new BrentOptimizer(1e-6, 1e-6);
 assertNotNull(optimizer.getConvergenceChecker());
 assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
 assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 0.0);
 }

 @Test
 public void testConstructorWithRelAndNullAbs() {
 optimizer = new BrentOptimizer(1e-6, null);
 assertNotNull(optimizer.getConvergenceChecker());
 assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
 assertNull(optimizer.getAbsoluteThreshold());
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testExceptionWithRelSmallerThanMinimum() {
 optimizer = new BrentOptimizer(1e-8, 1e-6);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testExceptionWithAbsLeq0() {
 optimizer = new BrentOptimizer(1e-6, 0.0);
 }

 @Test
 public void testDoOptimize() {
 PointValuePair result = optimizer.optimize(x -> Math.pow(x, 2) - 4.0 * x + 4.0);
 assertEquals(-2.0, result.getPoint(), 0.0);
 assertEquals(4.0, result.getValue(), 0.0);
 }

 @Test
 public void testDoOptimizeWithMinLimitSmallerThanMaxLimit() {
 optimizer = new BrentOptimizer(1e-6, 1e-6, new GoalType(GoalType.MINIMIZE), 1.0, 3.0);
 PointValuePair result = optimizer.optimize(x -> Math.pow(x, 2) - 4.0 * x + 4.0);
 assertEquals(-2.0, result.getPoint(), 0.0);
 assertEquals(4.0, result.getValue(), 0.0);
 }

 @Test
 public void testDoOptimizeWithMinLimitGreaterThanMaxLimit() {
 optimizer = new BrentOptimizer(1e-6, 1e-6, new GoalType(GoalType.MINIMIZE), 3.0, 1.0);
 PointValuePair result = optimizer.optimize(x -> Math.pow(x, 2) - 4.0 * x + 4.0);
 assertEquals(-2.0, result.getPoint(), 0.0);
 assertEquals(4.0, result.getValue(), 0.0);
 }

 @Test
 public void testDoOptimizeWithConvergenceChecker() {
 optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
 PointValuePair result = optimizer.optimize(x -> Math.pow(x, 2) - 4.0 * x + 4.0);
 assertEquals(-2.0, result.getPoint(), 0.0);
 assertEquals(4.0, result.getValue(), 0.0);
 }

 @Test
 public void testBest() {
 PointValuePair a = new PointValuePair(1.0, 3.0);
 PointValuePair b = new PointValuePair(2.0, 1.0);
 PointValuePair result = optimizer.best(a, b, true);
 assertEquals(a.getPoint(), result.getPoint(), 0.0);
 assertEquals(a.getValue(), result.getValue(), 0.0);

 PointValuePair c = new PointValuePair(3.0, 2.0);
 result = optimizer.best(a, b, c, true);
 assertEquals(a.getPoint(), result.getPoint(), 0.0);
 assertEquals(a.getValue(), result.getValue(), 0.0);

 result = optimizer.best(a, b, c, false);
 assertEquals(c.getPoint(), result.getPoint(), 0.0);
 assertEquals(c.getValue(), result.getValue(), 0.0);
 }

 private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 @Override
 public boolean converged(int iterations, PointValuePair current, PointValuePair previous) {
 return true;
 }
 }

}