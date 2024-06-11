import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeTestCase;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizerMinimization() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.exp(Math.sin(x)) - E.E;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-8, null);
 optimizer.setInitialGuess(Math.PI / 2.0);
 PointValuePair solution = optimizer.optimize(function);
 assertEquals("Expected solution is -0.66763451893516", -0.66763451893516, solution.getValue(), 1.0e-8);
 }

 @Test
 public void testBrentOptimizerMinimizationWithInitializer() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.exp(Math.sin(x)) - E.E;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-8);
 optimizer.setInitialGuess(new AroundMinimumInitializer(function, 0.5).findInitialPoint());
 PointValuePair solution = optimizer.optimize(function);
 assertEquals("Expected solution is -0.66763451893516", -0.66763451893516, solution.getValue(), 1.0e-8);
 }

 @Test
 public void testBrentOptimizerMinimizationWithChecker() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 3) - 0.1;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-8, new GoalTypeTestCase.ConvergenceChecker() {
 @Override
 public boolean check(int iter, PointValuePair current, PointValuePair previous) {
 return Math.abs(current.getValue() - previous.getValue()) < 1.0e-10;
 }
 });
 optimizer.setInitialGuess(0.0);
 PointValuePair solution = optimizer.optimize(function);
 assertEquals("Expected solution is 0.0", 0.0, solution.getValue(), 1.0e-10);
 }

 @Test
 public void testBrentOptimizerMinimizationWithInvalidParameters() {
 try {
 new BrentOptimizer(-1.0, 1.0e-6, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException ignored) {
 // Expected
 }

 try {
 new BrentOptimizer(1.0e-6, -1.0, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException ignored) {
 // Expected
 }

 try {
 new BrentOptimizer(1.0e-6, 1.0e-6, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException ignored) {
 // Expected
 }
 }

 @Test
 public void testBrentOptimizerMaximization() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.exp(-Math.sin(x)) - E.E;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-8, null);
 optimizer.setInitialGuess(Math.PI / 2.0);
 PointValuePair solution = optimizer.optimize(function);
 assertEquals("Expected solution is 0.66763451893516", 0.66763451893516, solution.getValue(), 1.0e-8);
 }

 @Test
 public void testBrentOptimizerMaximizationWithInitializer() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.exp(-Math.sin(x)) - E.E;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-8);
 optimizer.setInitialGuess(new AroundMinimumInitializer(function.negate(), 0.5).findInitialPoint());
 PointValuePair solution = optimizer.optimize(function.negate());
 assertEquals("Expected solution is 0.66763451893516", 0.66763451893516, solution.getValue(), 1.0e-8);
 }

 @Test
 public void testBrentOptimizerMaximizationWithChecker() {
 UnivariateFunction function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 3) + 0.1;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-8, new GoalTypeTestCase.ConvergenceChecker() {
 @Override
 public boolean check(int iter, PointValuePair current, PointValuePair previous) {
 return Math.abs(current.getValue() - previous.getValue()) < 1.0e-10;
 }
 });
 optimizer.setInitialGuess(Math.PI / 2.0);
 PointValuePair solution = optimizer.optimize(function);
 assertEquals("Expected solution is 1.11124714617235", 1.11124714617235, solution.getValue(), 1.0e-8);
 }

 @Test
 public void testBrentOptimizerMaximizationWithInvalidInitialGuess() {
 try {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-8, null);
 optimizer.setInitialGuess(-1.0);
 optimizer.optimize(new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.exp(Math.sin(x));
 }
 });
 fail("Expected IllegalStateException");
 } catch (IllegalStateException ignored) {
 // Expected
 }
 }
}