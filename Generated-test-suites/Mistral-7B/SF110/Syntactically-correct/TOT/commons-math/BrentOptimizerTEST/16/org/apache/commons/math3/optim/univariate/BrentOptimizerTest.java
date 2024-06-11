package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;
 private UnivariateFunction function;

 @Before
 public void setup() {
 function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2) + 1.5 * Math.pow(x, 2) - 3.5 * x + 2;
 }
 };
 }

 @Test
 public void testBrentOptimizerMinimization() {
 double minValue = 1.5;
 double minX = 1.0;
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 UnivariatePointValuePair result = optimizer.optimize(function, minX, Double.POSITIVE_INFINITY);
 assertEquals(minValue, result.getValue(), 1.0e-6);
 assertEquals(minX, result.getPoint(), 1.0e-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerMinRelTolTooSmall() {
 optimizer = new BrentOptimizer(1.0e-9, 1.0e-6, null);
 optimizer.optimize(function, 0.0, 1.0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerAbsThresholdNegative() {
 optimizer = new BrentOptimizer(1.0e-6, -1.0, null);
 assertNull(optimizer.optimize(function, 0.0, 1.0));
 }

 @Test
 public void testBrentOptimizerMaximization() {
 double maxValue = -1.5;
 double maxX = -2.0;
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 UnivariatePointValuePair result = optimizer.optimize(function, minX, Double.NEGATIVE_INFINITY);
 assertEquals(maxValue, result.getValue(), 1.0e-6);
 assertEquals(-maxX, result.getPoint(), 1.0e-6);
 }

 @Test
 public void testBrentOptimizerConvergenceChecker() {
 ConvergenceChecker<UnivariatePointValuePair> customChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
 @Override
 public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
 return current.getValue() - previous.getValue() <= 1.0e-8;
 }
 };
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, customChecker);
 optimizer.optimize(function, 0.0, 1.0);
 }

}