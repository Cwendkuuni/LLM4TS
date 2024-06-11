import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateFunction testFunction = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
 }
 };

 @Test
 public void testBrentOptimizerConstructorWithChecker() {
 try {
 BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0, new UnivariateConvergenceChecker(0.01, 50));
 fail("Expected NumberIsTooSmallException or NotStrictlyPositiveException");
 } catch (NumberIsTooSmallException e) {
 // Expected exception when relative tolerance is less than minimum
 } catch (NotStrictlyPositiveException e) {
 // Expected exception when absolute threshold is non-positive
 }

 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new UnivariateConvergenceChecker(0.001, 100));
 UnivariatePointValuePair result = optimizer.optimize(testFunction, 1.0, Double.NEGATIVE_INFINITY);
 assertEquals(0.0, result.getPoint(), 0.001);
 }

 @Test
 public void testBrentOptimizerConstructorWithoutChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
 UnivariatePointValuePair result = optimizer.optimize(testFunction, 1.0, Double.NEGATIVE_INFINITY);
 assertEquals(0.0, result.getPoint(), 0.001);
 }

 @Test
 public void testDoOptimize_minimization() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
 UnivariatePointValuePair result = optimizer.doOptimize();
 double expectedX = 1.245218134507684;
 double expectedValue = -1.156132263471525;
 assertEquals("Expected minimum", expectedX, result.getPoint(), 0.001);
 assertEquals("Expected minimum value", expectedValue, result.getValue(), 0.001);
 }

 @Test
 public void testDoOptimize_maximization() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 UnivariatePointValuePair result = optimizer.doOptimize();
 double expectedX = 1.745218134507684;
 double expectedValue = 1.156132263471525;
 assertEquals("Expected maximum", expectedX, result.getPoint(), 0.001);
 assertEquals("Expected maximum value", expectedValue, result.getValue(), 0.001);
 }

 @Test
 public void testDoOptimize_bounds() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
 UnivariatePointValuePair result = optimizer.doOptimize(new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.sin(Math.PI * x) + 2.5;
 }
 }, 0.0, 1.0);
 double expectedX = 0.785398163397448;
 double expectedValue = 1.253216658437875;
 assertEquals("Expected minimum with bounds", expectedX, result.getPoint(), 0.001);
 assertEquals("Expected minimum value with bounds", expectedValue, result.getValue(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimize_nullFunction() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
 optimizer.doOptimize(null, 0.0, 1.0);
 }

 @Test(expected = IllegalStateException.class)
 public void testDoOptimize_convergenceCheckerNull() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
 optimizer.doOptimize(testFunction, 1.0, Double.NEGATIVE_INFINITY);
 }
}