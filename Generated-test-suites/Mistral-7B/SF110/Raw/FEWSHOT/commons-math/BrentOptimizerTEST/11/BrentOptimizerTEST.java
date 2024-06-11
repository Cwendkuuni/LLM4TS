package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

 private GoalType goalType = GoalType.MINIMIZE;
 private BrentOptimizer optimizer;
 private FunctionEvaluator functionEvaluator;

 @Test
 public void testBrentOptimizer() {
 functionEvaluator = new FunctionEvaluator() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
 }
 };
 optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
 try {
 UnivariatePointValuePair result = optimizer.optimize(functionEvaluator, -5.0, 5.0);
 assertEquals(0.0, result.getPoint(), 1.0e-5);
 assertEquals(0.0, result.getValue(), 1.0e-5);
 } catch (OptimizationException e) {
 fail("Optimization exception should not be thrown");
 }
 }

 @Test
 public void testBrentOptimizerWithConvergenceChecker() {
 functionEvaluator = new FunctionEvaluator() {
 @Override
 public double value(double x) {
 return Math.sin(x) / x;
 }
 };
 ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
 @Override
 public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return Math.abs(current.getPoint() - previous.getPoint()) < 1.0e-6 && Math.abs(current.getValue() - previous.getValue()) < 1.0e-6;
 }
 };
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, convergenceChecker);
 try {
 UnivariatePointValuePair result = optimizer.optimize(functionEvaluator, -5.0, 5.0);
 assertEquals(0.0, result.getPoint(), 1.0e-6);
 assertEquals(0.0, result.getValue(), 1.0e-6);
 } catch (OptimizationException e) {
 fail("Optimization exception should not be thrown");
 }
 }

 @Test
 public void testBrentOptimizerWithInvalidRelativetolerance() {
 assertThrows(NumberIsTooSmallException.class, () -> {
 optimizer = new BrentOptimizer(-1.0e-5, 1.0e-5, null);
 });
 }

 @Test
 public void testBrentOptimizerWithInvalidAbsolutetolerance() {
 assertThrows(NotStrictlyPositiveException.class, () -> {
 optimizer = new BrentOptimizer(1.0e-5, -1.0e-5, null);
 });
 }

 @Test
 public void testBrentOptimizerWithInvalidStartingValues() {
 assertThrows(IllegalArgumentException.class, () -> {
 optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
 optimizer.optimize(functionEvaluator, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
 });
 }

 @Test
 public void testBrentOptimizerWithNoConvergence() {
 functionEvaluator = new FunctionEvaluator() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
 try {
 UnivariatePointValuePair result = optimizer.optimize(functionEvaluator, -5.0, 5.0);
 fail("Optimization exception should be thrown");
 } catch (OptimizationException e) {
 // Expected
 }
 }

}