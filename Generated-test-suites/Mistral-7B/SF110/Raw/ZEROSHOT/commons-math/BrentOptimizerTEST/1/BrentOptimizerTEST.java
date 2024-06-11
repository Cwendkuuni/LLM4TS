import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.NonlinearScalarFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateOptimizer optimizer;
 private NonlinearScalarFunction function;

 @Test
 public void testBrentOptimizer() {
 // Test constructor with GoalType and ConvergenceChecker
 optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MINIMIZE));
 // Test constructor with only GoalType
 optimizer = new BrentOptimizer(0.01, 0.001);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testInvalidRelativeThreshold() {
 optimizer = new BrentOptimizer(-0.01, 0.001);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testInvalidAbsoluteThreshold() {
 optimizer = new BrentOptimizer(0.01, -0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFunction() {
 optimizer = new BrentOptimizer(0.01, 0.001, null);
 }

 @Test
 public void testOptimize() {
 function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + Math.E;
 }
 };
 optimizer = new BrentOptimizer(0.01, 0.001, null);
 PointValuePair result = optimizer.optimize(function, -5.0, 5.0);
 assertEquals(-1.9704181, result.getPoint(), 0.001);
 assertEquals(-3.2667294, result.getValue(), 0.001);
 }

 @Test
 public void testOptimizeConvergenceChecker() {
 function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + Math.E;
 }
 };
 optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MINIMIZE));
 PointValuePair result = optimizer.optimize(function, -5.0, 5.0);
 assertEquals(-1.9704181, result.getPoint(), 0.001);
 assertEquals(-3.2667294, result.getValue(), 0.001);
 }

 @Test
 public void testMinimize() {
 function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 optimizer = new BrentOptimizer(0.01, 0.001, null);
 PointValuePair result = optimizer.optimize(function, -5.0, 5.0);
 assertEquals(0.0, result.getPoint(), 0.001);
 assertEquals(0.0, result.getValue(), 0.001);
 }

 @Test
 public void testMaximize() {
 function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return -Math.pow(x, 2);
 }
 };
 optimizer = new BrentOptimizer(0.01, 0.001, null);
 PointValuePair result = optimizer.optimize(function, -5.0, 5.0);
 assertEquals(0.0, result.getPoint(), 0.001);
 assertEquals(0.0, result.getValue(), 0.001);
 }
}