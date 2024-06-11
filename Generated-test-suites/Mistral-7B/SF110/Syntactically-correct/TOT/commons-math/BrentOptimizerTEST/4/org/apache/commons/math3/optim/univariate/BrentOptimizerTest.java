package org.apache.commons.math3.optim.univariate.test;

import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * A simple objective function for testing.
     */
    private static class TestFunction implements ObjectiveFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }

    /**
     * Test optimization of a simple function using default settings.
     */
    @Test
    public void testDefaultSettings() {
        TestFunction testFunction = new TestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        PointValuePair result = optimizer.optimize(testFunction, 2.0, 4.0);
        Assert.assertEquals(1.0, result.getPoint(), 1.0e-6);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    /**
     * Test optimization of a simple function with a custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        TestFunction testFunction = new TestFunction();
        GoalTypeChecker convergenceChecker = new GoalTypeChecker(GoalType.MINIMIZE, 1.0e-5, 1.0e-5);
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, convergenceChecker);
        PointValuePair result = optimizer.optimize(testFunction, 2.0, 4.0);
        Assert.assertEquals(1.0, result.getPoint(), 1.0e-6);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-6);
        Assert.assertTrue(convergenceChecker.isConverged());
    }

    /**
     * Test optimization of a simple function with a negative relative tolerance.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNegativeRelativeTolerance() {
        TestFunction testFunction = new TestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0e-5, 1.0e-5);
    }

    /**
     * Test optimization of a simple function with a negative absolute tolerance.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteTolerance() {
        TestFunction testFunction = new TestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, -1.0e-5);
    }

    /**
     * Test optimization of a simple function with non-strictly positive start value.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNonStrictlyPositiveStartValue() {
        TestFunction testFunction = new TestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        optimizer.optimize(testFunction, -1.0, 4.0);
    }

    /**
     * Test optimization of a simple function with minimum and maximum values equal.
     */
    @Test
    public void testMinMaxEqual() {
        TestFunction testFunction = new TestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        PointValuePair result = optimizer.optimize(testFunction, 0.0, 0.0);
        Assert.assertEquals(0.0, result.getPoint(), 1.0e-6);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    /**
     * Test optimization of a simple function with initial guess between minimum and maximum values.
     */
    @Test
    public void testInitialGuessBetweenBounds() {
        TestFunction testFunction = new TestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        PointValuePair result = optimizer.optimize(testFunction, 1.5, 2.5);
        Assert.assertEquals(1.0, result.getPoint(), 1.0e-6);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    /**
     * Test optimization of a simple function with initial guess outside bounds.
     */
    @Test
    public void testInitialGuessOutsideBounds() {
        TestFunction testFunction = new TestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        PointValuePair result = optimizer.optimize(testFunction, 5.0, 2.0);
        Assert.assertEquals(2.0, result.getPoint(), 1.0e-6);
        Assert.assertEquals(4.0, result.getValue(), 1.0e-6);
    }
}