package org.apache.commons.math3.optim.univariate.test;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;

 @Test
 public void testBrentOptimizerConstructionWithChecker() {
     optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MINIMIZE));
     assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerConstructionWithTooSmallRelativeThreshold() {
     optimizer = new BrentOptimizer(0.001, 0.001, new GoalTypeChecker(GoalType.MINIMIZE));
     fail("Should throw NumberIsTooSmallException");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerConstructionWithNegativeAbsoluteThreshold() {
     optimizer = new BrentOptimizer(0.01, -0.001, new GoalTypeChecker(GoalType.MINIMIZE));
     fail("Should throw NotStrictlyPositiveException");
 }

 @Test
 public void testBrentOptimizerConstructionWithoutChecker() {
     optimizer = new BrentOptimizer(0.01, 0.001);
     assertNotNull(optimizer);
 }

 @Test
 public void testDoOptimize_InitialValuesIncorrectOrder() {
     optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MINIMIZE)) {
         @Override
         protected UnivariatePointValuePair doOptimize() {
             double lo = 1.0;
             double mid = 0.5;
             double hi = 2.0;
             // ... rest of the method
         }
     };
     UnivariateFunction function = new UnivariateFunction() {
         @Override
         public double value(double x) {
             return Math.pow(x, 2.0) - 3.0;
         }
     };
     UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);
     assertNotNull(result);
 }

 @Test
 public void testDoOptimize_ConvergenceCheckerThrowsException() {
     optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker() {
         @Override
         public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
             return iter > 100; // This is an arbitrary condition to make the test fail
         }
     });
     UnivariateFunction function = new UnivariateFunction() {
         @Override
         public double value(double x) {
             return Math.pow(x, 2.0) - 3.0;
         }
     };
     UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);
     assertNull(result);
 }

 @Test
 public void testDoOptimize_FindMinimum() {
     optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MINIMIZE));
     UnivariateFunction function = new UnivariateFunction() {
         @Override
         public double value(double x) {
             return Math.pow(x, 2.0) - 3.0;
         }
     };
     UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);
     assertEquals(-Math.sqrt(3.0), result.getPoint(), 0.01);
     assertEquals(-3.0, result.getValue(), 0.01);
 }

 @Test
 public void testDoOptimize_FindMaximum() {
     optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MAXIMIZE));
     UnivariateFunction function = new UnivariateFunction() {
         @Override
         public double value(double x) {
             return -Math.pow(x, 2.0) + 3.0;
         }
     };
     UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);
     assertEquals(Math.sqrt(3.0), result.getPoint(), 0.01);
     assertEquals(3.0, result.getValue(), 0.01);
 }

 @Test
 public void testDoOptimize_FindMinimumWithInitialValueCloseToMinimum() {
     optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MINIMIZE));
     UnivariateFunction function = new UnivariateFunction() {
         @Override
         public double value(double x) {
             return Math.pow(x, 2.0) - 3.0;
         }
     };
     optimizer.setMin(Math.sqrt(3.0) - 0.1);
     UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);
     assertEquals(-Math.sqrt(3.0), result.getPoint(), 0.01);
     assertEquals(-3.0, result.getValue(), 0.01);
 }

 @Test
 public void testDoOptimize_FindMaximumWithInitialValueCloseToMaximum() {
     optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MAXIMIZE));
     UnivariateFunction function = new UnivariateFunction() {
         @Override
         public double value(double x) {
             return -Math.pow(x, 2.0) + 3.0;
         }
     };
     optimizer.setMax(Math.sqrt(3.0) + 0.1);
     UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);
     assertEquals(Math.sqrt(3.0), result.getPoint(), 0.01);
     assertEquals(3.0, result.getValue(), 0.01);
 }

 @Test
 public void testDoOptimize_FindMinimumWithInitialValueFarFromMinimum() {
     optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MINIMIZE));
     UnivariateFunction function = new UnivariateFunction() {
         @Override
         public double value(double x) {
             return Math.pow(x, 2.0) - 3.0;
         }
     };
     optimizer.setMin(-5.0);
     optimizer.setMax(5.0);
     UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);
     assertEquals(-Math.sqrt(3.0), result.getPoint(), 0.01);
     assertEquals(-3.0, result.getValue(), 0.01);
 }

 @Test
 public void testDoOptimize_FindMaximumWithInitialValueFarFromMaximum() {
     optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MAXIMIZE));
     UnivariateFunction function = new UnivariateFunction() {
         @Override
         public double value(double x) {
             return -Math.pow(x, 2.0) + 3.0;
         }
     };
     optimizer.setMin(-5.0);
     optimizer.setMax(5.0);
     UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);
     assertEquals(Math.sqrt(3.0), result.getPoint(), 0.01);
     assertEquals(3.0, result.getValue(), 0.01);
 }

 @Test
 public void testBest() {
     UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 5.0);
     UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);
     UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 4.0);
     assertEquals(b, optimizer.best(a, null, false));
     assertEquals(a, optimizer.best(null, b, false));
     assertEquals(a, optimizer.best(a, b, true));
     assertEquals(b, optimizer.best(b, a, true));
 }
}