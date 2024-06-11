package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.StartingPointGenerator;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * A simple function for testing.
     */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }

        @Override
        public double minimum(double x1, double x2) {
            return Math.min(x1, x2) * 0.1;
        }

        @Override
        public double maximum(double x1, double x2) {
            return Math.max(x1, x2) * 0.1;
        }
    }

    /**
     * Tests the optimizer with a simple function and default settings.
     */
    @Test
    public void testOptimization() {
        final UnivariateRealFunction function = new TestFunction();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(100, 1.0E-10), 10, new UniformRandomGenerator());

        // Optimize the function with the default settings.
        final PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);

        // Check that the optimization was successful.
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getValue(), 0.0, 1e-8);
    }

    /**
     * Tests the optimizer with a simple function, a specific goal type, and custom settings.
     */
    @Test
    public void testOptimizationWithCustomSettings() {
        final UnivariateRealFunction function = new TestFunction();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer(100, 1.0E-12), 20, new UniformRandomGenerator());

        // Optimize the function with specific goal type and settings.
        final PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);

        // Check that the optimization was successful.
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getValue(), 0.0, 1e-12);
    }

    /**
     * Tests the optimizer error handling with a non-differentiable function.
     */
    @Test(expected = ConvergenceException.class)
    public void testErrorHandling() {
        final UnivariateRealFunction function = new TestFunction();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(
                new GoldenSectionFinder(), 1, new UniformRandomGenerator());

        // Attempt to optimize a non-differentiable function.
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Tests the optimizer error handling with a function that violates monotonicity.
     */
    @Test(expected = ConvergenceException.class)
    public void testErrorHandlingWithViolatedMonotonicFunction() {
        final UnivariateRealFunction function = new ViolatedMonotonicityFunction();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(
                new GoldenSectionFinder(), 1, new UniformRandomGenerator());

        // Attempt to optimize a function that violates monotonicity.
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * A non-differentiable function for testing.
     */
    private static class ViolatedMonotonicityFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.abs(Math.sin(Math.PI * x));
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double minimum(double x1, double x2) {
            return 0.0;
        }

        @Override
        public double maximum(double x1, double x2) {
            return 0.0;
        }
    }
}