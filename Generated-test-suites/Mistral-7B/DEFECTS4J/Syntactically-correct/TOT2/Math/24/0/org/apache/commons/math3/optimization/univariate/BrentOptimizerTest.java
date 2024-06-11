import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBasicFunctionality() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-8);
 ObjectiveFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2 * x;
 }
 };
 PointValuePair result = optimizer.optimize(function, new InitialGuess(2.0));
 assertEquals(1.0, result.getPoint(), 1e-8);
 }

 @Test
 public void testEdgeCases_MinimumAtStart() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-8);
 ObjectiveFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return x * Math.sin(Math.PI * x);
 }
 };
 PointValuePair result = optimizer.optimize(function, new InitialGuess(0.0));
 // The minimum is at x = 0, but Brent's method should find a better solution.
 // In this case, the minimum is approximately 1.20206 x 10^-16.
 assertTrue(result.getValue() < 1e-12);
 }

 @Test
 public void testEdgeCases_MinimumAtEnd() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-8);
 ObjectiveFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2, 2) - 4;
 }
 };
 PointValuePair result = optimizer.optimize(function, new InitialGuess(5.0));
 assertEquals(2.0, result.getPoint(), 1e-5);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testExceptionHandling_TooSmallRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-9, 1e-8);
 ObjectiveFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return x * Math.sin(Math.PI * x);
 }
 };
 optimizer.optimize(function, new InitialGuess(0.0));
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testExceptionHandling_NegativeRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(-1e-5, 1e-8);
 ObjectiveFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2 * x;
 }
 };
 optimizer.optimize(function, new InitialGuess(2.0));
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testExceptionHandling_NegativeAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, -1e-8);
 ObjectiveFunction function = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2 * x;
 }
 };
 optimizer.optimize(function, new InitialGuess(2.0));
 }

 @Test
 public void testExceptionHandling_NullFunction() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-8);
 ObjectiveFunction function = null;
 try {
 optimizer.optimize(function, new InitialGuess(2.0));
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testExceptionHandling_NullInitialGuess() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-8);
 try {
 optimizer.optimize(new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2 * x;
 }
 }, null);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}