import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateFunction f;

 @Test
 public void testBrentOptimizerMinimum() {
 f = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + 1;
 }
 };
 BrentOptimizer opt = new BrentOptimizer(0.001, 0.001, null);
 PointValuePair result = opt.optimize(f, -3.0, 3.0);
 assertEquals(0.0, result.getPoint(), 0.01);
 assertEquals(-1.014451148985631, result.getValue(), 0.001);
 }

 @Test
 public void testBrentOptimizerMaximum() {
 f = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return -Math.pow(x, 3) + 2 * x * Math.pow(Math.E, x) - 1;
 }
 };
 BrentOptimizer opt = new BrentOptimizer(0.001, 0.001, null);
 PointValuePair result = opt.optimize(f, -3.0, 3.0);
 assertEquals(-3.0, result.getPoint(), 0.01);
 assertEquals(1.014451148985631, result.getValue(), 0.001);
 }

 @Test
 public void testBrentOptimizerMinimumWithCustomChecker() {
 f = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.abs(Math.sin(x) - 0.5);
 }
 };
 BrentOptimizer opt = new BrentOptimizer(0.001, 0.0, new CustomConvergenceChecker());
 PointValuePair result = opt.optimize(f, -5.0, 5.0);
 assertEquals(0.0, result.getPoint(), 0.01);
 assertFalse(Double.isNaN(result.getValue()));
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerInvalidRelativeThreshold() {
 f = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3);
 }
 };
 new BrentOptimizer(-0.001, 0.001, null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerInvalidAbsoluteThreshold() {
 f = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3);
 }
 };
 new BrentOptimizer(0.001, -0.001, null);
 }

 @Test
 public void testBrentOptimizerBracketingBoundary() {
 f = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.abs(Math.sin(x) - 0.5);
 }
 };
 BrentOptimizer opt = new BrentOptimizer(0.001, 0.0, new CustomConvergenceChecker());
 PointValuePair result = opt.optimize(f, -5.0, 0.0);
 assertEquals(0.0, result.getPoint(), 0.01);
 }

 @Test
 public void testBrentOptimizerBracketingSingularity() {
 f = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.log(Math.abs(x));
 }
 };
 BrentOptimizer opt = new BrentOptimizer(0.001, 0.0, new CustomConvergenceChecker());
 PointValuePair result = opt.optimize(f, 0.0, 1.0);
 assertEquals(0.0, result.getPoint(), 0.01);
 }

 @Test
 public void testBrentOptimizerBracketingMultipleLocalMinima() {
 f = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + x * Math.pow(Math.E, -x);
 }
 };
 BrentOptimizer opt = new BrentOptimizer(0.001, 0.0, new CustomConvergenceChecker());
 PointValuePair result = opt.optimize(f, -5.0, 5.0);
 assertTrue(result.getValue() < 0.0);
 }

 private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return FastMath.abs(current.getValue() - previous.getValue()) < 0.0001;
 }
 }
}