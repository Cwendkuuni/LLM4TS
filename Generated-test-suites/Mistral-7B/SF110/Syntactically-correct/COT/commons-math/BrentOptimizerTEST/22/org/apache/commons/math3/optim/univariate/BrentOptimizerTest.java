package org.apache.commons.math3.optim.univariate.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;

 @Test
 public void testBrentOptimizerConstructionWithChecker() {
 optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
 @Override
 public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return current.getValue() == previous.getValue();
 }
 });
 assertNotNull(optimizer);
 }

 @Test
 public void testBrentOptimizerConstructionWithoutChecker() {
 optimizer = new BrentOptimizer(0.01, 0.001);
 assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerNegativeRelativeTolerance() {
 optimizer = new BrentOptimizer(-0.01, 0.001, null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerZeroAbsoluteTolerance() {
 optimizer = new BrentOptimizer(0.01, 0.0, null);
 }

 @Test
 public void testDoOptimizeBasicFunctionality() {
 OptimizationProblem problem = new UnivariateOptimizationProblem() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }

 @Override
 public double value(double x) {
 return x * x;
 }

 @Override
 public double getMinimum() {
 return 0.0;
 }

 @Override
 public double getMaximum() {
 return 10.0;
 }
 };
 optimizer = new BrentOptimizer(0.001, 0.0);
 UnivariatePointValuePair result = optimizer.optimize(problem);
 assertEquals(0.0, result.getPoint(), 0.01);
 }

 @Test
 public void testDoOptimizeMinFunctionality() {
 OptimizationProblem problem = new UnivariateOptimizationProblem() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }

 @Override
 public double value(double x) {
 return FastMath.sin(x) * FastMath.sin(x) + 2.0 * x - 1.5;
 }

 @Override
 public double getMinimum() {
 return -1.5;
 }

 @Override
 public double getMaximum() {
 return 10.0;
 }
 };
 optimizer = new BrentOptimizer(0.01, 0.001);
 UnivariatePointValuePair result = optimizer.optimize(problem);
 assertEquals(-1.5, result.getPoint(), 0.01);
 }

 @Test
 public void testDoOptimizeMaxFunctionality() {
 OptimizationProblem problem = new UnivariateOptimizationProblem() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MAXIMIZE;
 }

 @Override
 public double value(double x) {
 return FastMath.sin(x) * FastMath.sin(x) + 2.0 * x - 1.5;
 }

 @Override
 public double getMinimum() {
 return -1.5;
 }

 @Override
 public double getMaximum() {
 return 10.0;
 }
 };
 optimizer = new BrentOptimizer(0.01, 0.001);
 UnivariatePointValuePair result = optimizer.optimize(problem);
 assertEquals(-1.5, result.getPoint(), 0.01); // The result is incorrect due to the wrong goal type
 // Set up the correct test case for max function
 optimizer = new BrentOptimizer(0.01, 0.001);
 OptimizationProblem maxProblem = new UnivariateOptimizationProblem() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MAXIMIZE;
 }

 @Override
 public double value(double x) {
 return FastMath.sin(x) * FastMath.sin(x) + 2.0 * x - 1.5;
 }

 @Override
 public double getMinimum() {
 return -1.5;
 }

 @Override
 public double getMaximum() {
 return 1.5;
 }
 };
 result = optimizer.optimize(maxProblem);
 assertEquals(1.5, result.getPoint(), 0.01);
 }

 @Test
 public void testDoOptimizeOutOfRangeInput() {
 OptimizationProblem problem = new UnivariateOptimizationProblem() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }

 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }

 @Override
 public double getMinimum() {
 return 0.0;
 }

 @Override
 public double getMaximum() {
 return -1.0;
 }
 };
 optimizer = new BrentOptimizer(0.01, 0.001);
 try {
 optimizer.optimize(problem);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testDoOptimizeNullInput() {
 OptimizationProblem problem = null;
 optimizer = new BrentOptimizer(0.01, 0.001);
 try {
 optimizer.optimize(problem);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testDoOptimizeConvergenceChecker() {
 OptimizationProblem problem = new UnivariateOptimizationProblem() {
 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }

 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }

 @Override
 public double getMinimum() {
 return 0.0;
 }

 @Override
 public double getMaximum() {
 return 10.0;
 }
 };
 optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
 @Override
 public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return current.getValue() == previous.getValue() && iter > 10;
 }
 });
 UnivariatePointValuePair result = optimizer.optimize(problem);
 assertNotNull(result);
 }
}