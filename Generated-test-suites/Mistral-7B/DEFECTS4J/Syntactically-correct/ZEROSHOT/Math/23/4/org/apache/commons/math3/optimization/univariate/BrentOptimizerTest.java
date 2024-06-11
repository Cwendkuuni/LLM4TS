import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizerTest;
import org.junit.Assert;

public class BrentOptimizerTest {

 private static final double DELTA = 1e-6;

 @org.junit.Test
 public void testBrentOptimizer() {
 UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 // Test with a function that has a known minimum.
 Function f = new Function("f") {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 4 * x * Math.pow(x, 2) + 4 * x - 4;
 }
 };
 PointValuePair result = optimizer.optimize(f, -5, 5);
 Assert.assertEquals(result.getPoint(), -2.0, DELTA);
 // Test with a function that has a maximum.
 Function g = new Function("g") {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) + 4 * x * Math.pow(x, 2) - 4 * x + 4;
 }
 };
 optimizer = new BrentOptimizer(1e-6, 1e-6);
 result = optimizer.optimize(g, -5, 5);
 Assert.assertEquals(result.getPoint(), 2.0, DELTA);
 // Test with a function that has a minimum at 0.
 Function h = new Function("h") {
 @Override
 public double value(double x) {
 return Math.pow(x, 4) - x;
 }
 };
 optimizer = new BrentOptimizer(1e-6, 1e-6);
 result = optimizer.optimize(h, -5, 5);
 Assert.assertEquals(result.getPoint(), 0.0, DELTA);
 // Test with a function that does not have a minimum in the given interval.
 Function i = new Function("i") {
 @Override
 public double value(double x) {
 return Math.sin(x) / x;
 }
 };
 optimizer = new BrentOptimizer(1e-6, 1e-6);
 result = optimizer.optimize(i, -5, 5);
 Assert.assertNull(result);
 }

 static class Function {
 String name;

 public Function(String name) {
 this.name = name;
 }

 public abstract double value(double x);

 @Override
 public String toString() {
 return name;
 }
 }
}