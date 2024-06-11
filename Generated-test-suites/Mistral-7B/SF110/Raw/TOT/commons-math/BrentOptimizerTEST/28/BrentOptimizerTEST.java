import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @Test
 public void testBrentOptimizerWithMinimizeGoalAndValidInput() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-6, null);
 FunctionOptimizer testFunction = new FunctionOptimizer(new MyFunction(), new GoalType[] {GoalType.MINIMIZE});
 UnivariatePointValuePair result = optimizer.optimize(testFunction, 0.0, 10.0);
 assertEquals(1.0, result.getPoint(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerWithMinimizeGoalAndConvergenceChecker() {
 FunctionOptimizer testFunction = new FunctionOptimizer(new MyFunction(), null);
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-6, new MyConvergenceChecker());
 UnivariatePointValuePair result = optimizer.optimize(testFunction, 0.0, 10.0);
 assertEquals(1.0, result.getPoint(), 1.0e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerWithInvalidRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0e-6, null);
 FunctionOptimizer testFunction = new FunctionOptimizer(new MyFunction(), new GoalType[] {GoalType.MINIMIZE});
 try {
 optimizer.optimize(testFunction, 0.0, 10.0);
 } catch (NumberIsTooSmallException e) {
 fail("Expected exception with message 'Relative tolerance is too small: 0.1'");
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerWithInvalidAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, -1.0, null);
 FunctionOptimizer testFunction = new FunctionOptimizer(new MyFunction(), new GoalType[] {GoalType.MINIMIZE});
 try {
 optimizer.optimize(testFunction, 0.0, 10.0);
 } catch (NotStrictlyPositiveException e) {
 fail("Expected exception with message 'Absolute tolerance is not positive: -1.0'");
 }
 }

 @Test
 public void testBrentOptimizerWithMinimizeGoalAndDivergingFunction() {
 FunctionOptimizer testFunction = new FunctionOptimizer(new MyDivergingFunction(), new GoalType[] {GoalType.MINIMIZE});
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-6, null);
 try {
 optimizer.optimize(testFunction, 0.0, 10.0);
 fail("Expected OptimizationException to be thrown");
 } catch (OptimizationException e) {
 // expected
 }
 }

 @Test
 public void testBrentOptimizerWithMaximizeGoalAndValidInput() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-6, null);
 FunctionOptimizer testFunction = new FunctionOptimizer(new MyFunction(), new GoalType[] {GoalType.MAXIMIZE});
 UnivariatePointValuePair result = optimizer.optimize(testFunction, 0.0, 10.0);
 assertEquals(9.0, result.getPoint(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerWithMaximizeGoalAndConvergenceChecker() {
 FunctionOptimizer testFunction = new FunctionOptimizer(new MyFunction(), null);
 BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-6, new MyConvergenceChecker());
 UnivariatePointValuePair result = optimizer.optimize(testFunction, 0.0, 10.0);
 assertEquals(9.0, result.getPoint(), 1.0e-6);
 }

 private static class MyFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }
 }

 private static class MyDivergingFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return -1 / (x * Math.log(Math.abs(x)));
 }
 }

 private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 @Override
 public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return current.getValue() - previous.getValue() < 1.0e-8;
 }
 }
}