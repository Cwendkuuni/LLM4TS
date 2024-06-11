package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializers.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleGoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * A simple test function for testing Brent Optimizer.
     */
    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + 1;
        }

        @Override
        public double derivative(double x) {
            return 3 * Math.pow(x, 2) - 2 * Math.E * x;
        }
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     */
    @Test
    public void testBrentOptimizer() {
        TestFunction testFunction = new TestFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        PointValuePair result = optimizer.optimize(testFunction, -10, 10, null);
        Assert.assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.7320508075688772, result.getPoint(), 1e-8);
        Assert.assertEquals(-4.674640783238338e-10, result.getValue(), 1e-12);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     */
    @Test
    public void testBrentOptimizerWithNoChecker() {
        TestFunction testFunction = new TestFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        PointValuePair result = optimizer.optimize(testFunction, -10, 10, null);
        Assert.assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.7320508075688772, result.getPoint(), 1e-8);
        Assert.assertEquals(-4.674640783238338e-10, result.getValue(), 1e-12);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with custom ConvergenceChecker.
     */
    @Test
    public void testBrentOptimizerWithCustomChecker() {
        TestFunction testFunction = new TestFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, new SimpleGoalTypeChecker(1e-10));
        PointValuePair result = optimizer.optimize(testFunction, -10, 10, null);
        Assert.assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.7320508075688772, result.getPoint(), 1e-8);
        Assert.assertEquals(-1.045055796727114e-10, result.getValue(), 1e-12);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with custom initializer.
     */
    @Test
    public void testBrentOptimizerWithCustomInitializer() {
        TestFunction testFunction = new TestFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, new ArrayRealVectorInitializer(new double[]{3.0}));
        PointValuePair result = optimizer.optimize(testFunction, -10, 10, null);
        Assert.assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.7320508075688772, result.getPoint(), 1e-8);
        Assert.assertEquals(-4.674640783238338e-10, result.getValue(), 1e-12);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with custom initializer and custom ConvergenceChecker.
     */
    @Test
    public void testBrentOptimizerWithCustomInitializerAndCustomChecker() {
        TestFunction testFunction = new TestFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, new ArrayRealVectorInitializer(new double[]{3.0}),
                new SimpleGoalTypeChecker(1e-10));
        PointValuePair result = optimizer.optimize(testFunction, -10, 10, null);
        Assert.assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.7320508075688772, result.getPoint(), 1e-8);
        Assert.assertEquals(-1.045055796727114e-10, result.getValue(), 1e-12);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, ConvergenceChecker)}
     * with a function that doesn't converge.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testBrentOptimizerWithUnconvergingFunction() {
        TestFunction testFunction = new TestFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("This function doesn't converge.");
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        optimizer.optimize(testFunction, -10, 10, null);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, ConvergenceChecker)}
     * with a function that doesn't have a finite minimum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithFunctionWithoutFiniteMinimum() {
        TestFunction testFunction = new TestFunction() {
            @Override
            public double value(double x) {
                return 1 / x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        optimizer.optimize(testFunction, -10, 0, null);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, ConvergenceChecker)}
     * with a function that doesn't have a minimum in the given interval.
     */
    @Test
    public void testBrentOptimizerWithFunctionWithoutMinimumInInterval() {
        TestFunction testFunction = new TestFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        PointValuePair result = optimizer.optimize(testFunction, -10, 10, null);
        Assert.assertNull(result);
    }
}