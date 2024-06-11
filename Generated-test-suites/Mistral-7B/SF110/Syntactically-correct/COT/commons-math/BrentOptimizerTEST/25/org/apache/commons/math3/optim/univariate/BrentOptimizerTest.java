package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class BrentOptimizerTest {

 private static final double RELATIVE_TOLERANCE = 0.01;
 private static final double ABSOLUTE_TOLERANCE = 0.01;
 private static final double MINIMUM = 2.5;
 private static final double MAXIMUM = 4.5;
 private static final double START_VALUE = 3.5;

 @Test
 public void testBrentOptimizerConstructorWithRelAndAbs() {
     BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE, null);
     assertEquals(RELATIVE_TOLERANCE, optimizer.getRelativeThreshold(), 0.0);
     assertEquals(ABSOLUTE_TOLERANCE, optimizer.getAbsoluteThreshold(), 0.0);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerConstructorWithSmallRelativeTolerance() {
     new BrentOptimizer(0.001, ABSOLUTE_TOLERANCE, null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerConstructorWithNegativeAbsoluteTolerance() {
     new BrentOptimizer(RELATIVE_TOLERANCE, -ABSOLUTE_TOLERANCE, null);
 }

 @Test
 public void testBrentOptimizerConstructorWithRelAndNullChecker() {
     BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
     assertEquals(RELATIVE_TOLERANCE, optimizer.getRelativeThreshold(), 0.0);
     assertEquals(ABSOLUTE_TOLERANCE, optimizer.getAbsoluteThreshold(), 0.0);
 }

 @Test
 public void testOptimizeMethodWithMinimizingFunction() throws Exception {
     BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
     OptimizationFunction function = new AbstractUnivariateFunction() {
         @Override
         public double value(double x) {
             return Math.pow(x, 2) + Math.sin(Math.PI * x) + 1;
         }
     };
     UnivariatePointValuePair result = optimizer.optimize(function, MINIMUM, MAXIMUM, START_VALUE);
     double expectedMinimum = -3.04355938214472;
     assertEquals(expectedMinimum, result.getValue(), 0.001);
     assertTrue(Math.abs(MINIMUM - result.getPoint()) < 0.01);
 }

 @Test
 public void testOptimizeMethodWithMaximizingFunction() throws Exception {
     BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
     OptimizationFunction function = new AbstractUnivariateFunction() {
         @Override
         public double value(double x) {
             return Math.pow(x, 2) + Math.sin(Math.PI * x) - 1;
         }
     };
     UnivariatePointValuePair result = optimizer.optimize(function, MINIMUM, MAXIMUM, START_VALUE);
     double expectedMaximum = 3.04355938214472;
     assertEquals(expectedMaximum, result.getValue(), 0.001);
     assertTrue(Math.abs(MAXIMUM - result.getPoint()) < 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptimizeMethodWithNullFunction() {
     BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
     optimizer.optimize(null, MINIMUM, MAXIMUM, START_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptimizeMethodWithNullBoundary() {
     BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
     OptimizationFunction function = new AbstractUnivariateFunction() {
         @Override
         public double value(double x) {
             return Math.sin(Math.PI * x);
         }
     };
     optimizer.optimize(function, null, MAXIMUM, START_VALUE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptimizeMethodWithStartValueOutsideBounds() {
     BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
     OptimizationFunction function = new AbstractUnivariateFunction() {
         @Override
         public double value(double x) {
             return Math.sin(Math.PI * x);
         }
     };
     optimizer.optimize(function, MINIMUM, MAXIMUM, MAXIMUM);
 }

 @Test
 public void testBestMethod() {
     UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
     UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
     UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 3.0);
     UnivariatePointValuePair expected = a;
     assertEquals(expected, BrentOptimizer.this.best(null, b, true));
     expected = b;
     assertEquals(expected, BrentOptimizer.this.best(a, null, true));
     expected = a;
     assertEquals(expected, BrentOptimizer.this.best(a, b, false));
     expected = b;
     assertEquals(expected, BrentOptimizer.this.best(a, b, false));
 }
}