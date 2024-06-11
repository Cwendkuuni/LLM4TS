import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);

 // Test function f(x) = x^2
 UnivariateFunction function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return x * x;
 }
 };

 // Test minimization
 UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0, null);
 assertEquals(0.0, result.getPoint(), 1e-6);
 assertEquals(0.0, result.getValue(), 1e-6);

 // Test maximization
 optimizer = new BrentOptimizer(1e-6, 0.0);
 UnivariateFunction maxFunction = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return -x * x;
 }
 };

 UnivariatePointValuePair maxResult = optimizer.optimize(maxFunction, 0.0, 1.0, null);
 assertEquals(1.0, maxResult.getPoint(), 1e-6);
 assertEquals(-1.0, maxResult.getValue(), 1e-6);
 }

 @Test
 public void testBrentOptimizer_ConvergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0, new MyConvergenceChecker());

 // Test function f(x) = sin(x)
 UnivariateFunction function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };

 UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, null);
 assertEquals(0.0, Math.toDegrees(result.getPoint()), 0.1); // Approximately 0 degrees
 assertEquals(0.0, result.getValue(), 1e-6);
 }

 class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
 double difference = Math.abs(current.getPoint() - previous.getPoint());
 return difference < 1e-5 || iteration > 20;
 }
 }
}