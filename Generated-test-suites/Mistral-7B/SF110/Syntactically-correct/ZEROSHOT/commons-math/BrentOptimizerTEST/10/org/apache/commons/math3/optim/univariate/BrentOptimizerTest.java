import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateOptimizer optimizer;
 private double[] testFunction;

 @Before
 public void setup() {
 testFunction = new double[]{-1.0, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5};
 }

 @Test
 public void testBrentOptimizerMinimal() {
 optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
 UnivariatePointValuePair result = optimizer.optimize(new MyTestFunction(), 1.0, 10.0);
 assertEquals(2.5, result.getPoint(), 1.0E-6);
 assertEquals(-3.1622776601683795E-7, result.getValue(), 1.0E-6);
 }

 @Test
 public void testBrentOptimizerAbsolutes() {
 optimizer = new BrentOptimizer(1.0E-6, 5.0, null);
 UnivariatePointValuePair result = optimizer.optimize(new MyTestFunction(), 1.0, 10.0);
 assertEquals(5.0, result.getPoint(), 1.0);
 assertEquals(0.0, result.getValue(), 1.0E-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerNegativeRelativeThreshold() {
 optimizer = new BrentOptimizer(-1.0E-6, 1.0E-6, null);
 try {
 optimizer.optimize(new MyTestFunction(), 1.0, 10.0);
 } catch (NumberIsTooSmallException e) {
 // expected
 assertEquals(-1.0E-6, e.getRelativeThreshold(), 0.0);
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerNegativeAbsoluteThreshold() {
 optimizer = new BrentOptimizer(1.0E-6, -1.0, null);
 try {
 optimizer.optimize(new MyTestFunction(), 1.0, 10.0);
 } catch (NotStrictlyPositiveException e) {
 // expected
 assertEquals(-1.0, e.getValue(), 0.0);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerNullChecker() {
 optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
 try {
 optimizer.optimize(new MyTestFunction(), 1.0, 10.0, null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBrentOptimizerMinimization() {
 optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, new MyConvergenceChecker());
 UnivariatePointValuePair result = optimizer.optimize(new MyTestFunctionMinimization(), 1.0, 10.0);
 assertEquals(2.5, result.getPoint(), 1.0E-6);
 assertEquals(-3.1622776601683795E-7, result.getValue(), 1.0E-6);
 }

 @Test
 public void testBrentOptimizerMaximization() {
 optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, new MyConvergenceChecker());
 optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, GoalType.MAXIMIZE);
 UnivariatePointValuePair result = optimizer.optimize(new MyTestFunctionMaximization(), 1.0, 10.0);
 assertEquals(8.5, result.getPoint(), 1.0E-6);
 assertEquals(5.0, result.getValue(), 1.0E-6);
 }

 @Test
 public void testBrentOptimizerMaximumBoundReached() {
 optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, new MyConvergenceChecker());
 optimizer = new BrentOptimizer(1.0E-6, 10.0, GoalType.MAXIMIZE);
 UnivariatePointValuePair result = optimizer.optimize(new MyTestFunctionMaximization(), 1.0, 10.0);
 assertEquals(10.0, result.getPoint(), 1.0E-6);
 assertEquals(10.0, result.getValue(), 1.0E-6);
 }

 @Test
 public void testBrentOptimizerMinimumBoundReached() {
 optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, new MyConvergenceChecker());
 optimizer = new BrentOptimizer(1.0, 1.0E-6, GoalType.MINIMIZE);
 UnivariatePointValuePair result = optimizer.optimize(new MyTestFunctionMinimization(), 1.0, 1.0);
 assertEquals(1.0, result.getPoint(), 1.0E-6);
 assertEquals(0.0, result.getValue(), 1.0E-6);
 }

 private static class MyTestFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2.0;
 }
 }

 private static class MyTestFunctionMinimization implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2.0;
 }
 }

 private static class MyTestFunctionMaximization implements UnivariateFunction {
 @Override
 public double value(double x) {
 return -(Math.pow(x, 2) - 2.0);
 }
 }

 private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair best) {
 return iter > 10;
 }
 }

}