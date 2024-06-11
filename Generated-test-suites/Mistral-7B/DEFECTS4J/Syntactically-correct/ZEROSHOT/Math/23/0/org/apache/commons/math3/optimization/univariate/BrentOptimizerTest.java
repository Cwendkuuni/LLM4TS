import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 // Test with default constructor
 BrentOptimizer optimizer = new BrentOptimizer(0.0, 1.0);
 FunctionFcn f = new FunctionFcn() {
 @Override
 public double value(double x) {
 return Math.exp(Math.log(Math.abs(x - 0.5)) * 0.5) - 0.5;
 }
 };
 UnivariateOptimizerResult result = optimizer.optimize(f, 0.0, 1.0);
 Assert.assertEquals(0.5, result.getPoint(), 1e-5);
 Assert.assertEquals(0.0, result.getValue(), 1e-5);

 // Test with custom constructor and additional convergence checker
 BrentOptimizer optimizer1 = new BrentOptimizer(0.001, 0.01, new MyConvergenceChecker());
 UnivariateOptimizerResult result1 = optimizer1.optimize(f, 0.0, 1.0);
 Assert.assertEquals(0.5, result1.getPoint(), 1e-5);
 Assert.assertEquals(0.0, result1.getValue(), 1e-5);

 // Test with custom FunctionFcn
 FunctionFcn g = new FunctionFcn() {
 @Override
 public double value(double x) {
 return Math.pow(Math.E, Math.sin(x)) - 1;
 }
 };
 BrentOptimizer optimizer2 = new BrentOptimizer(0.0, 1.0, new MyConvergenceChecker());
 UnivariateOptimizerResult result2 = optimizer2.optimize(g, 0.0, 1.0);
 Assert.assertEquals(Math.PI, result2.getPoint(), 1e-5);
 Assert.assertEquals(-1.0, result2.getValue(), 1e-5);
 }

 @FunctionalInterface
 interface FunctionFcn {
 double value(double x);
 }

 static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return current.getValue() <= previous.getValue() || iteration >= 100;
 }
 }
}