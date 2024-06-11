import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.initializer.ValuePyramidInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.convergence.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateFunction function = new MyFunction();
 try {
 PointValuePair result = optimizer.optimize(function, new ValuePyramidInitializer(0.0, 10.0, 10));
 System.out.println("Minimum found: " + result.getPoint() + " with value " + result.getValue());
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 }

 @Test
 public void testBrentOptimizer_Minimum() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateFunction function = new MyFunction();
 GoalType goalType = GoalType.MINIMIZE;
 try {
 PointValuePair result = optimizer.optimize(function, new ValuePyramidInitializer(0.0, 10.0, 10), goalType);
 assertEquals(goalType, optimizer.getGoalType());
 System.out.println("Minimum found: " + result.getPoint() + " with value " + result.getValue());
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 }

 @Test
 public void testBrentOptimizer_Maximum() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateFunction function = new MyFunction();
 GoalType goalType = GoalType.MAXIMIZE;
 try {
 PointValuePair result = optimizer.optimize(function, new ValuePyramidInitializer(0.0, 10.0, 10), goalType);
 assertEquals(goalType, optimizer.getGoalType());
 System.out.println("Maximum found: " + result.getPoint() + " with value " + result.getValue());
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 }

 @Test
 public void testBrentOptimizer_Minimum_ConvergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new SimpleLineSearchConvergenceChecker(1.0e-6));
 UnivariateFunction function = new MyFunction();
 GoalType goalType = GoalType.MINIMIZE;
 try {
 PointValuePair result = optimizer.optimize(function, new ValuePyramidInitializer(0.0, 10.0, 10), goalType);
 assertEquals(goalType, optimizer.getGoalType());
 System.out.println("Minimum found: " + result.getPoint() + " with value " + result.getValue());
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 }

 @Test
 public void testBrentOptimizer_Maximum_ConvergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new SimpleLineSearchConvergenceChecker(1.0e-6));
 UnivariateFunction function = new MyFunction();
 GoalType goalType = GoalType.MAXIMIZE;
 try {
 PointValuePair result = optimizer.optimize(function, new ValuePyramidInitializer(0.0, 10.0, 10), goalType);
 assertEquals(goalType, optimizer.getGoalType());
 System.out.println("Maximum found: " + result.getPoint() + " with value " + result.getValue());
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 }

 @Test (expected = NumberIsTooSmallException.class)
 public void testBrentOptimizer_InvalidRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(-1.0e-6, 1.0e-6);
 UnivariateFunction function = new MyFunction();
 try {
 optimizer.optimize(function, new ValuePyramidInitializer(0.0, 10.0, 10));
 fail("Exception not thrown");
 } catch (Exception e) {
 System.out.println("Expected NumberIsTooSmallException, but got: " + e.getClass().getName());
 }
 }

 @Test (expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizer_InvalidAbsoluteThresholder() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0e-6);
 UnivariateFunction function = new MyFunction();
 try {
 optimizer.optimize(function, new ValuePyramidInitializer(0.0, 10.0, 10));
 fail("Exception not thrown");
 } catch (Exception e) {
 System.out.println("Expected NotStrictlyPositiveException, but got: " + e.getClass().getName());
 }
 }

 @Test
 public void testBrentOptimizer_ConvergenceCheckerNull() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateFunction function = new MyFunction();
 try {
 PointValuePair result = optimizer.optimize(function, new ValuePyramidInitializer(0.0, 10.0, 10), null);
 fail("Exception not thrown");
 } catch (IllegalArgumentException e) {
 System.out.println("Expected IllegalArgumentException, but got: " + e.getClass().getName());
 }
 }

 @Test
 public void testBrentOptimizer_ConvergenceCheckerNotImplemented() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new NotImplementedConvergenceChecker());
 UnivariateFunction function = new MyFunction();
 try {
 optimizer.optimize(function, new ValuePyramidInitializer(0.0, 10.0, 10), GoalType.MINIMIZE);
 fail("Exception not thrown");
 } catch (UnsupportedOperationException e) {
 System.out.println("Expected UnsupportedOperationException, but got: " + e.getClass().getName());
 }
 }

 @Test
 public void testBrentOptimizer_OutOfRange() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateFunction function = new MyFunction();
 try {
 optimizer.optimize(function, new ValuePyramidInitializer(10.0, 0.0, 10), GoalType.MINIMIZE);
 fail("Exception not thrown");
 } catch (IllegalArgumentException e) {
 System.out.println("Expected IllegalArgumentException, but got: " + e.getClass().getName());
 }
 }

 @Test
 public void testBrentOptimizer_ZeroRange() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateFunction function = new MyFunction();
 try {
 optimizer.optimize(function, new ValuePyramidInitializer(0.0, 0.0, 10), GoalType.MINIMIZE);
 fail("Exception not thrown");
 } catch (IllegalArgumentException e) {
 System.out.println("Expected IllegalArgumentException, but got: " + e.getClass().getName());
 }
 }

 @Test
 public void testBrentOptimizer_EmptyInitializer() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
 UnivariateFunction function = new MyFunction();
 try {
 optimizer.optimize(function, new EmptyInitializer(), GoalType.MINIMIZE);
 fail("Exception not thrown");
 } catch (IllegalArgumentException e) {
 System.out.println("Expected IllegalArgumentException, but got: " + e.getClass().getName());
 }
 }

 static class MyFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 4 * x * x + 4;
 }
 }

 static class SimpleLineSearchConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 private final double tolerance;

 public SimpleLineSearchConvergenceChecker(double tolerance) {
 this.tolerance = tolerance;
 }

 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 return (current.getValue() - previous.getValue()) <= tolerance;
 }
 }

 static class NotImplementedConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 @Override
 public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
 throw new UnsupportedOperationException();
 }
 }
}