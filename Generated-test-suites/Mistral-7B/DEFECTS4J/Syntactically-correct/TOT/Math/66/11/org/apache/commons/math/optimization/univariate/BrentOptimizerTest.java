package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * A simple function for testing.
     */
    private static class SimpleFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }

        @Override
        public double minimum() {
            return 0;
        }

        @Override
        public double maximum() {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * A function with no root.
     */
    private static class NoRootFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.sin(x);
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x);
        }

        @Override
        public double minimum() {
            throw new UnsupportedOperationException();
        }

        @Override
        public double maximum() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * A function with multiple roots.
     */
    private static class MultipleRootsFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x - 1, 3) - Math.pow(x + 1, 3);
        }

        @Override
        public double derivative(double x) {
            return 3 * (Math.pow(x - 1, 2) - Math.pow(x + 1, 2));
        }

        @Override
        public double minimum() {
            return -1;
        }

        @Override
        public double maximum() {
            return 1;
        }
    }

    /**
     * Tests the optimization of a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        SimpleFunction f = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer();

        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 0);

        Assert.assertEquals(0, result.getPoint(), 1e-6);
        Assert.assertEquals(0, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimization of a function with no root.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeNoRootFunction() {
        NoRootFunction f = new NoRootFunction();
        BrentOptimizer optimizer = new BrentOptimizer();

        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 0);
    }

    /**
     * Tests the optimization of a function with multiple roots.
     */
    @Test
    public void testOptimizeMultipleRootsFunction() {
        MultipleRootsFunction f = new MultipleRootsFunction();
        BrentOptimizer optimizer = new BrentOptimizer();

        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 0);

        Assert.assertEquals(-1, result.getPoint(), 1e-6);
        Assert.assertEquals(-1, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimization with negative arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeNegativeArguments() {
        SimpleFunction f = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer();

        optimizer.optimize(f, GoalType.MINIMIZE, -1, -0.1, -1);
    }
}