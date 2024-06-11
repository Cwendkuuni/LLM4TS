package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test file for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test
    public void testConstructor() {
        try {
            new BrentOptimizer(0.0, 0.0);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            new BrentOptimizer(0.5, -0.1);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        new BrentOptimizer(1.0, 1.0);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test
    public void testConstructorWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, null);
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test
    public void testOptimizeWithDefaultValues() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);

        UnivariatePointValuePair result = optimizer.optimize(new MyFunction(), 0.0, 1.0, null);

        assertEquals(0.75, result.getPoint(), 0.0);
        assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test
    public void testOptimizeWithCustomChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, new MyCustomConvergenceChecker(1e-5));

        UnivariatePointValuePair result = optimizer.optimize(new MyFunction(), 0.0, 1.0, null);

        assertEquals(0.75, result.getPoint(), 0.0);
        assertEquals(-1.0, result.getValue(), 1e-6);

        MyCustomConvergenceChecker checker = (MyCustomConvergenceChecker) optimizer.getConvergenceChecker();
        Assert.assertEquals(3, checker.getNumIterations());
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, null);
        optimizer.optimize(null, 0.0, 1.0, null);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test
    public void testOptimizeWithCustomInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);

        UnivariatePointValuePair result = optimizer.optimize(new MyFunction(), 0.1, 0.9, null);

        assertEquals(0.367948970432854, result.getPoint(), 0.0);
        assertEquals(-0.448172962735363, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);

        optimizer.optimize(new MyFunction(), 1.0, 1.0, null);
    }

    /**
     * Test function for {@link BrentOptimizer}.
     */
    private static class MyFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return FastMath.pow(x, 3) - 2 * FastMath.pow(x, 2) + x - 1;
        }

        @Override
        public double derivative(double x) {
            return 3 * FastMath.pow(x, 2) - 4 * x + 1;
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }

    /**
     * Custom convergence checker for testing.
     */
    private static class MyCustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

        private final double tolerance;
        private int numIterations;

        public MyCustomConvergenceChecker(double tolerance) {
            this.tolerance = tolerance;
        }

        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            if (iteration > numIterations) {
                numIterations = iteration;
            }
            return FastMath.abs(current.getValue() - previous.getValue()) <= tolerance;
        }

        public int getNumIterations() {
            return numIterations;
        }
    }
}