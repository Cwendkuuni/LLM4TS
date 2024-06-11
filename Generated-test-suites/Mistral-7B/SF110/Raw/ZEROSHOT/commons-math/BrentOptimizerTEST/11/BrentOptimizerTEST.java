import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;

import java.util.Arrays;

public class BrentOptimizerTest {

 private static final ObjectiveFunction TEST_FUNCTION = new ObjectiveFunction() {
  @Override
  public double value(double x) {
    return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
  }

  @Override
  public double value(double[] x) {
    throw new UnsupportedOperationException("This function only accepts one variable");
  }

  @Override
  public int numberOfVariables() {
    return 1;
  }
};

 @org.junit.Test
 public void testBrentOptimizer() {
  double min = -2.0;
  double max = 2.0;
  double relTol = 1e-6;
  double absTol = 1e-7;
  ConvergenceChecker<UnivariatePointValuePair> checker = new SimpleLineSearchConvergenceChecker(1.0 / Math.abs(TestFunction.value(min)), 1.0);

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
  UnivariateOptimizerResult result = optimizer.optimize(TEST_FUNCTION, min, max);

  double expectedResult = -1.7320508075688772;
  assertEquals(expectedResult, result.getPoint().getValue(), 1e-8);
  assertEquals(expectedResult, result.getValue(), 1e-8);
 }

 @org.junit.Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerInvalidRelativeTolerance() {
  double relTol = 0.1;
  double absTol = 1e-7;
  ConvergenceChecker<UnivariatePointValuePair> checker = null;

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
 }

 @org.junit.Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerInvalidAbsoluteTolerance() {
  double relTol = 1e-6;
  double absTol = -1e-7;
  ConvergenceChecker<UnivariatePointValuePair> checker = null;

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerNullFunction() {
  double min = 0.0;
  double max = 1.0;
  double relTol = 1e-6;
  double absTol = 1e-7;
  ConvergenceChecker<UnivariatePointValuePair> checker = null;

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
  try {
    optimizer.optimize(null, min, max);
  } catch (OptimizationException e) {
    fail("Expected IllegalArgumentException, but got " + e.getClass().getName());
  }
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testBrentOptimizerNoConvergenceChecker() {
  double min = 0.0;
  double max = 1.0;
  double relTol = 1e-6;
  double absTol = 1e-7;

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);
  try {
    optimizer.optimize(TEST_FUNCTION, min, max);
  } catch (IllegalArgumentException e) {
    fail("Expected IllegalStateException, but got " + e.getClass().getName());
  }
 }

 @org.junit.Test(expected = GoalTypeNotFoundException.class)
 public void testBrentOptimizerInvalidGoalType() {
  double min = 0.0;
  double max = 1.0;
  double relTol = 1e-6;
  double absTol = 1e-7;
  ConvergenceChecker<UnivariatePointValuePair> checker = null;

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
  try {
    optimizer.setGoalType(GoalType.MAXIMIZE);
    optimizer.optimize(TEST_FUNCTION, min, max);
  } catch (GoalTypeNotFoundException e) {
    fail("Expected GoalTypeNotFoundException, but got " + e.getClass().getName());
  }
 }

 @org.junit.Test
 public void testBrentOptimizerConvergence() {
  double min = -10.0;
  double max = 10.0;
  double relTol = 1e-3;
  double absTol = 1e-4;
  ConvergenceChecker<UnivariatePointValuePair> checker = new SimpleLineSearchConvergenceChecker(1.0 / Math.abs(TestFunction.value(min)), 1.0);

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
  try {
    UnivariateOptimizerResult result = optimizer.optimize(TEST_FUNCTION, min, max);
    double expectedResult = -1.570803360538642;
    assertEquals(expectedResult, result.getPoint().getValue(), 1e-5);
    assertEquals(expectedResult, result.getValue(), 1e-5);
  } catch (OptimizationException e) {
    fail("Test should not throw an exception");
  }
 }

 @org.junit.Test
 public void testBrentOptimizerDifferentMinAndMax() {
  double min = 1.5;
  double max = 2.5;
  double relTol = 1e-6;
  double absTol = 1e-7;
  ConvergenceChecker<UnivariatePointValuePair> checker = null;

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
  try {
    UnivariateOptimizerResult result = optimizer.optimize(TEST_FUNCTION, min, max);
    double expectedResult = 2.0;
    assertEquals(expectedResult, result.getPoint().getValue(), 1e-6);
    assertEquals(expectedResult, result.getValue(), 1e-6);
  } catch (OptimizationException e) {
    fail("Test should not throw an exception");
  }
 }

 @org.junit.Test
 public void testBrentOptimizerSameMinAndMax() {
  double min = 2.0;
  double max = 2.0;
  double relTol = 1e-6;
  double absTol = 1e-7;
  ConvergenceChecker<UnivariatePointValuePair> checker = null;

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
  try {
    UnivariateOptimizerResult result = optimizer.optimize(TEST_FUNCTION, min, max);
    fail("Test should throw an exception");
  } catch (OptimizationException e) {
    // Expected
  }
 }

 @org.junit.Test
 public void testBrentOptimizerMaximization() {
  double min = -10.0;
  double max = 10.0;
  double relTol = 1e-3;
  double absTol = 1e-4;
  ConvergenceChecker<UnivariatePointValuePair> checker = new SimpleLineSearchConvergenceChecker(1.0 / Math.abs(TestFunction.value(min)), 1.0);

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
  optimizer.setGoalType(GoalType.MAXIMIZE);

  try {
    UnivariateOptimizerResult result = optimizer.optimize(TEST_FUNCTION, min, max);
    double expectedResult = 1.570803360538642;
    assertEquals(expectedResult, result.getPoint().getValue(), 1e-5);
    assertEquals(expectedResult, result.getValue(), 1e-5);
  } catch (OptimizationException e) {
    fail("Test should not throw an exception");
  }
 }

 @org.junit.Test
 public void testBrentOptimizerNoConvergence() {
  double min = 0.0;
  double max = 1.0;
  double relTol = 1e-10;
  double absTol = 1e-12;
  ConvergenceChecker<UnivariatePointValuePair> checker = null;

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
  try {
    UnivariateOptimizerResult result = optimizer.optimize(TEST_FUNCTION, min, max);
    fail("Test should not return a result");
  } catch (OptimizationException e) {
    // Expected
  }
 }

 @org.junit.Test
 public void testBrentOptimizerMultipleCalls() {
  double min = -10.0;
  double max = 10.0;
  double relTol = 1e-3;
  double absTol = 1e-4;
  ConvergenceChecker<UnivariatePointValuePair> checker = null;

  BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);

  UnivariateOptimizerResult result1 = null;
  try {
    result1 = optimizer.optimize(TEST_FUNCTION, min, max);
  } catch (OptimizationException e) {
    fail("Test should not throw an exception");
  }

  double expectedResult = -1.570803360538642;
  assertEquals(expectedResult, result1.getPoint().getValue(), 1e-5);
  assertEquals(expectedResult, result1.getValue(), 1e-5);

  UnivariateOptimizerResult result2 = null;
  try {
    result2 = optimizer.optimize(TEST_FUNCTION, min, max);
  } catch (OptimizationException e) {
    fail("Test should not throw an exception");
  }

  assertEquals(result1, result2);
 }
}