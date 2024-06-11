import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.ValueFormat;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private ObjectiveFunction testFunction = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
 }
 };

 @Test
 public void testBrentOptimizer() {
 // Test constructor with ConvergenceChecker
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
 assertEquals("Expected goal type to be MINIMIZE", GoalType.MINIMIZE, optimizer.getGoalType());
 assertEquals("Expected minimum relative tolerance to be 0.01", 0.01, optimizer.getRelativeThreshold(), 0.001);
 assertEquals("Expected minimum absolute tolerance to be 0.001", 0.001, optimizer.getAbsoluteThreshold(), 0.0001);

 // Test constructor without ConvergenceChecker
 BrentOptimizer optimizerNoChecker = new BrentOptimizer(0.01, 0.001);
 assertEquals("Expected goal type to be MINIMIZE", GoalType.MINIMIZE, optimizerNoChecker.getGoalType());
 assertEquals("Expected minimum relative tolerance to be 0.01", 0.01, optimizerNoChecker.getRelativeThreshold(), 0.001);
 assertEquals("Expected minimum absolute tolerance to be 0.001", 0.001, optimizerNoChecker.getAbsoluteThreshold(), 0.0001);

 // Test optimization with no ConvergenceChecker
 UnivariateOptimizerResult result = optimizerNoChecker.optimize(testFunction, 0.1, 1.0, ValueFormat.TOLERANCE);
 PointValuePair minimum = result.getPointValuePair();
 double expectedMinimum = -1.0075975594135;
 assertEquals("Expected minimum to be approximately -1.0075975594135", expectedMinimum, minimum.getValue(), 0.0001);
 assertEquals("Expected minimum x to be approximately 0.5373132253917", 0.53731322539167, minimum.getPoint(), 0.0001);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testThrowExceptionWithInvalidRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.001, null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testThrowExceptionWithInvalidAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.001, null);
 }

}