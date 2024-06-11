package org.apache.commons.math3.optim.univariate;

import static org.apache.commons.math3.optim.UnivariateOptimizer.GoalType.MINIMIZE;
import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.point.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class BrentOptimizerTest {

 private static final double EPS = 1e-10;

 @Test
 public void testBrentOptimizerConstructor1() {
  try {
   new BrentOptimizer(-0.01, 0.0);
   fail("Expected NumberIsTooSmallException");
  } catch (NumberIsTooSmallException e) {
   // Expected
  }
 }

 @Test
 public void testBrentOptimizerConstructor2() {
  try {
   new BrentOptimizer(0.0, -0.01);
   fail("Expected NotStrictlyPositiveException");
  } catch (NotStrictlyPositiveException e) {
   // Expected
  }
 }

 @Test
 public void testBrentOptimizerConstructor3() {
  BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
  assertNotNull(optimizer);
  assertEquals(0.01, optimizer.getRelativeThreshold(), EPS);
  assertEquals(0.001, optimizer.getAbsoluteThreshold(), EPS);
 }

 @Test
 public void testBrentOptimizerConstructor4() {
  BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
  assertNotNull(optimizer);
  assertEquals(0.01, optimizer.getRelativeThreshold(), EPS);
  assertEquals(0.001, optimizer.getAbsoluteThreshold(), EPS);
 }

 @Test
 public void testDoOptimizeMinimize() throws OptimizeException {
  BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
  UnivariateFunction func = new UnivariateFunction() {
   @Override
   public double value(double x) {
    return Math.pow(x - 2.0, 2.0);
   }
  };
  UnivariatePointValuePair solution = optimizer.optimize(func, 0.0, 3.0);
  assertEquals(1.5, solution.getX(), EPS);
  assertEquals(-3.0, solution.getValue(), EPS);
 }

 @Test
 public void testDoOptimizeMinimizeWithConvergenceChecker() throws OptimizeException {
  BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
   @Override
   public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
    return current.getValue() - previous.getValue() < 0.001;
   }
  });
  UnivariateFunction func = new UnivariateFunction() {
   @Override
   public double value(double x) {
    return Math.pow(x - 2.0, 2.0);
   }
  };
  UnivariatePointValuePair solution = optimizer.optimize(func, 0.0, 3.0);
  assertEquals(1.5, solution.getX(), EPS);
  assertEquals(-3.0, solution.getValue(), EPS);
 }

 @Test
 public void testDoOptimizeMaximize() throws OptimizeException {
  BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
  UnivariateFunction func = new UnivariateFunction() {
   @Override
   public double value(double x) {
    return -Math.pow(x - 2.0, 2.0);
   }
  };
  UnivariatePointValuePair solution = optimizer.optimize(func, 0.0, 3.0);
  assertEquals(1.5, solution.getX(), EPS);
  assertEquals(3.0, solution.getValue(), EPS);
 }

 @Test
 public void testDoOptimizeMaximizeWithConvergenceChecker() throws OptimizeException {
  BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
   @Override
   public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
    return current.getValue() - previous.getValue() < 0.001;
   }
  });
  UnivariateFunction func = new UnivariateFunction() {
   @Override
   public double value(double x) {
    return -Math.pow(x - 2.0, 2.0);
   }
  };
  UnivariatePointValuePair solution = optimizer.optimize(func, 0.0, 3.0);
  assertEquals(1.5, solution.getX(), EPS);
  assertEquals(3.0, solution.getValue(), EPS);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testInvalidGoalType() {
  BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
  UnivariateFunction func = new UnivariateFunction() {
   @Override
   public double value(double x) {
    return 0.0;
   }
  };
  optimizer.optimize(func, 0.0, 3.0, GoalType.SADDLE);
 }

 @Test
 public void testBest() {
  UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
  UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
  UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 3.0);
  assertEquals(a, optimizer.best(null, a, MINIMIZE));
  assertEquals(b, optimizer.best(a, b, MINIMIZE));
  assertEquals(c, optimizer.best(b, c, MINIMIZE));
  assertEquals(c, optimizer.best(a, b, MINIMIZE));
  assertEquals(a, optimizer.best(b, c, MINIMIZE));
  assertEquals(b, optimizer.best(c, a, MINIMIZE));
  assertEquals(c, optimizer.best(a, c, MINIMIZE));
 }

}