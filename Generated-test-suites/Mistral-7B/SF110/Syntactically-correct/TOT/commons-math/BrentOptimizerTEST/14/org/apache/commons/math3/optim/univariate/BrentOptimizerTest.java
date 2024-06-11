package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;
 private FunctionEvaluator function;

 @Before
 public void setUp() {
 function = new FunctionEvaluator() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x + 1;
 }
 };
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-7);
 }

 @Test
 public void testMinimization() {
 UnivariatePointValuePair result = optimizer.optimize(function);
 double expected = -1.0;
 assertEquals(expected, result.getPoint(), 1.0e-6);
 assertEquals(expected, result.getValue(), 1.0e-6);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeAbsoluteThreshold() {
 optimizer = new BrentOptimizer(1.0e-6, -1.0e-6);
 optimizer.optimize(function);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testRelativeThresholdTooSmall() {
 optimizer = new BrentOptimizer(0.5 * BrentOptimizer.MIN_RELATIVE_TOLERANCE, 1.0e-7);
 optimizer.optimize(function);
 }

 @Test
 public void testMinimumAtEdgeOfSearchInterval() {
 optimizer = new BrentOptimizer(1.0e-6, 1.0e-7);
 optimizer.setMin(-1.0);
 optimizer.setMax(-1.0);
 UnivariatePointValuePair result = optimizer.optimize(function);
 double expected = -1.0;
 assertEquals(expected, result.getPoint(), 1.0e-6);
 assertEquals(expected, result.getValue(), 1.0e-6);
 }

}