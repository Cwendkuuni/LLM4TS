package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class BrentOptimizerTest {

 private GoalType goalType = GoalType.MINIMIZE;
 private double min = -10.0;
 private double startValue = 0.0;
 private double max = 10.0;
 private ConvergenceChecker<UnivariatePointValuePair> checker = null;
 private BrentOptimizer optimizer;

 @Test
 public void testBrentOptimizerConstructionWithGoalTypeAndConvergenceChecker() {
 optimizer = new BrentOptimizer(0.01, 0.001, checker, goalType);
 assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerConstructionWithRelativelySmallRelativeTolerance() {
 optimizer = new BrentOptimizer(0.001, 0.001, checker, goalType);
 fail("Should throw NumberIsTooSmallException with a relative tolerance of 0.001");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerConstructionWithNonPositiveAbsoluteTolerance() {
 optimizer = new BrentOptimizer(0.01, -0.001, checker, goalType);
 fail("Should throw NotStrictlyPositiveException with an absolute tolerance of -0.001");
 }

 @Test
 public void testBrentOptimizerConstructionWithRelativeAndAbsoluteTolerances() {
 optimizer = new BrentOptimizer(0.01, 0.001, checker, goalType);
 assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
 assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
 }

 @Test
 public void testDoOptimize_noSolutionFound() {
 optimizer = new BrentOptimizer(goalType, min, startValue, max, checker);
 UnivariateFunction function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2.0) + Math.pow(Math.cos(x), 2.0);
 }
 };
 optimizer.setFunction(function);
 UnivariatePointValuePair result = optimizer.optimize();
 assertNull(result);
 }

 @Test
 public void testDoOptimize_solutionFound() {
 optimizer = new BrentOptimizer(goalType, min, startValue, max, checker);
 UnivariateFunction function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 3.0, 2.0);
 }
 };
 optimizer.setFunction(function);
 UnivariatePointValuePair result = optimizer.optimize();
 assertEquals(3.0, result.getPoint(), 0.001);
 assertEquals(0.0, result.getValue(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimize_nullFunction() {
 optimizer = new BrentOptimizer(goalType, min, startValue, max, checker);
 optimizer.setFunction(null);
 fail("Should throw IllegalArgumentException for null function");
 }

 @Test
 public void testComputeObjectiveValue() {
 optimizer = new BrentOptimizer(goalType, min, startValue, max, checker);
 UnivariateFunction function = new AbstractUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 3.0, 2.0);
 }
 };
 optimizer.setFunction(function);
 double x = 0.0;
 double expected = 9.0;
 double computed = optimizer.computeObjectiveValue(x);
 assertEquals(expected, computed, 0.001);
 }

 @Test
 public void testBest() {
 UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
 UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 4.0);
 UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 9.0);
 UnivariatePointValuePair result = optimizer.best(a, b, goalType);
 assertEquals(a.getValue() <= b.getValue() ? a : b, result);

 result = optimizer.best(null, b, goalType);
 assertEquals(b, result);

 result = optimizer.best(a, null, goalType);
 assertEquals(a, result);
 }
}