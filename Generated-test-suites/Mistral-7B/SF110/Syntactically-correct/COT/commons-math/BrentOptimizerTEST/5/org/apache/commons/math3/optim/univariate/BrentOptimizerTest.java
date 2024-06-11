import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

 private GoalType goalType = GoalType.MINIMIZE;
 private BrentOptimizer optimizer;
 private FunctionFunction function;

 @Test
 public void testBrentOptimizerConstruction() {
  try {
   optimizer = new BrentOptimizer(0.0, 0.0, null);
   fail("Should throw NotStrictlyPositiveException for absoluteThreshold");
  } catch (NotStrictlyPositiveException e) {
   // expected
  }

  try {
   optimizer = new BrentOptimizer(-1.0, 0.0, null);
   fail("Should throw NumberIsTooSmallException for relativeThreshold");
  } catch (NumberIsTooSmallException e) {
   // expected
  }

  optimizer = new BrentOptimizer(1.1, 1.2, null);
  assertNotNull(optimizer);
 }

 @Test
 public void testBrentOptimizerMinimization() throws OptimizeException {
  function = new AbstractFunctionFunction() {
   @Override
   public double value(double x) {
    return Math.pow(x, 2);
   }
  };
  optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
  UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
  assertEquals(Math.sqrt(2.0), result.getPoint(), 1e-6);
  assertEquals(-2.0, result.getValue(), 1e-6);
 }

 @Test
 public void testBrentOptimizerMaximization() throws OptimizeException {
  function = new AbstractFunctionFunction() {
   @Override
   public double value(double x) {
    return -Math.pow(x, 2);
   }
  };
  optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
  UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
  assertEquals(-Math.sqrt(2.0), result.getPoint(), 1e-6);
  assertEquals(2.0, result.getValue(), 1e-6);
 }

 @Test
 public void testBrentOptimizerConvergenceChecker() throws OptimizeException {
  function = new AbstractFunctionFunction() {
   @Override
   public double value(double x) {
    return Math.pow(x, 2);
   }
  };
  OptimizeStoppingCondition stoppingCondition = new DefaultOptimizeStoppingCondition(100, 1.0e-6, Double.POSITIVE_INFINITY);
  optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, stoppingCondition);
  UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
  assertEquals(Math.sqrt(2.0), result.getPoint(), 1e-6);
  assertEquals(-2.0, result.getValue(), 1e-6);
 }

 @Test
 public void testBrentOptimizerEdgeCases() throws OptimizeException {
  function = new AbstractFunctionFunction() {
   @Override
   public double value(double x) {
    if (x > 1.0) {
     return Double.MAX_VALUE;
    }
    if (x < 0.0) {
     return Double.MIN_VALUE;
    }
    return Math.abs(x - 1.0) * 10.0;
   }
  };
  optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
  UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
  assertEquals(1.0, result.getPoint(), 1e-6);
  assertEquals(0.0, result.getValue(), 1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerInvalidInterval() throws OptimizeException {
  function = new AbstractFunctionFunction() {
   @Override
   public double value(double x) {
    return Math.sin(x);
   }
  };
  optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
  try {
   optimizer.optimize(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
  } catch (IllegalArgumentException e) {
   // expected
  }
 }

}