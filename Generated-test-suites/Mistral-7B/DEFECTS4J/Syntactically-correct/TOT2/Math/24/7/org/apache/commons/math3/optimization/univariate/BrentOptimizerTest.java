import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariateFunction function = f -> Math.log(1 + E.EULER) * f;
 PointValuePair result = optimizer.optimize(function, 0.1, 1.0);
 assertEquals(E.EULER.doubleValue(), result.getValue(), 1e-6);
 assertEquals(0.5 * (1 + E.EULER.doubleValue()), result.getPoint(), 1e-6);
 }

 @Test
 public void testBrentOptimizerMinimumAtEnd() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariateFunction function = f -> f * f;
 PointValuePair result = optimizer.optimize(function, -5.0, 5.0);
 assertEquals(25.0, result.getValue(), 1e-6);
 assertEquals(-5.0, result.getPoint(), 1e-6);
 }

 @Test
 public void testBrentOptimizerMinimumAtBeginning() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariateFunction function = f -> f * f;
 PointValuePair result = optimizer.optimize(function, -10.0, 5.0);
 assertEquals(25.0, result.getValue(), 1e-6);
 assertEquals(-10.0, result.getPoint(), 1e-6);
 }

 @Test
 public void testBrentOptimizerWithCustomChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
 UnivariateFunction function = f -> Math.pow(f, 2);
 PointValuePair result = optimizer.optimize(function, 0, 10);
 assertEquals(0.0, result.getValue(), 1e-6);
 assertEquals(0.0, result.getPoint(), 1e-6);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerNegativeAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6);
 UnivariateFunction function = f -> Math.pow(f, 2);
 PointValuePair result = optimizer.optimize(function, 0, 10);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerTooSmallRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-6);
 UnivariateFunction function = f -> Math.pow(f, 2);
 PointValuePair result = optimizer.optimize(function, 0, 10);
 }

 @Test
 public void testBrentOptimizerWithNullConvergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 PointValuePair result = optimizer.optimize(null, 0, 10);
 assertNull(result);
 }
}

class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 @Override
 public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
 // Custom convergence checking condition
 return current.getValue() < 0.01;
 }
}