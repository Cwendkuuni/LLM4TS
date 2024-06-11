package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.test.AbstractTestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest extends AbstractTestCase {

    /**
     * Function to test.
     */
    private UnivariateFunction f;

    /**
     * Optimizer to test.
     */
    private UnivariateOptimizer optimizer;

    /**
     * Constructor.
     */
    public BrentOptimizerTest() {
        super();
    }

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        f = new MyFunction();
        optimizer = new BrentOptimizer(1.0e-6, 1.0e-9);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with no {@code ConvergenceChecker}.
     */
    @Test
    public void testBrentOptimizer() {
        final double x = optimizer.optimize(f, GoalType.MINIMIZE).getPoint();
        assertEquals(0.5, x, 1.0e-7);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with default {@code ConvergenceChecker}.
     */
    @Test
    public void testBrentOptimizerDefaultConvergenceChecker() {
        final UnivariateOptimizer optimizer
            = new BrentOptimizer(1.0e-6, 1.0e-9);
        final double x = optimizer.optimize(f, GoalType.MINIMIZE).getPoint();
        assertEquals(0.5, x, 1.0e-7);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with a custom {@code ConvergenceChecker}.
     */
    @Test
    public void testBrentOptimizerCustomConvergenceChecker() {
        final ConvergenceChecker<PointValuePair> checker
            = new MyConvergenceChecker();
        final UnivariateOptimizer optimizer
            = new BrentOptimizer(1.0e-6, 1.0e-9, checker);
        final double x = optimizer.optimize(f, GoalType.MINIMIZE).getPoint();
        assertEquals(0.5, x, 1.0e-7);
        assertTrue(checker.isConverged());
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with custom {@code ConvergenceChecker}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerCustomConvergenceCheckerNull() {
        final ConvergenceChecker<PointValuePair> checker = null;
        new BrentOptimizer(1.0e-6, 1.0e-9, checker);
    }

    /**
     * Test for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType)}
     * with {@code null} function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNullFunction() {
        optimizer.optimize(null, GoalType.MINIMIZE);
    }

    /**
     * Test for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType)}
     * with {@code null} goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNullGoalType() {
        optimizer.optimize(f, null);
    }

    /**
     * Test function.
     */
    private static class MyFunction implements UnivariateFunction {

        /**
         * Compute the value of the function at a given point.
         *
         * @param x the point
         * @return the value of the function at the given point
         */
        @Override
        public double value(double x) {
            return Math.pow(x - 1.0, 2.0) + 0.1 * Math.sin(10.0 * x);
        }

        /**
         * Evaluate the derivative of the function at a given point.
         *
         * @param x the point
         * @return the value of the derivative of the function at the given point
         */
        @Override
        public double derivative(double x) {
            return 2.0 * (x - 1.0) - 10.0 * Math.cos(10.0 * x);
        }
    }

    /**
     * Custom convergence checker.
     */
    private static class MyConvergenceChecker
            implements ConvergenceChecker<PointValuePair> {

        /**
         * Check if the optimization process is converged.
         *
         * @param iter number of iterations
         * @param previous previous optimization result
         * @param current current optimization result
         * @return {@code true} if the optimization process is converged
         */
        @Override
        public boolean isConverged(int iter, PointValuePair previous,
                                   PointValuePair current) {
            return Math.abs(current.getValue() - previous.getValue()) < 1.0e-10;
        }
    }
}