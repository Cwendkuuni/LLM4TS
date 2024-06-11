package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * A univariate function for testing.
     */
    private static class TestFunction implements UnivariateFunction {
        /**
         * Computes the value of the function at given point.
         *
         * @param x Point.
         * @return the value of the function at the point {@code x}.
         */
        @Override
        public double value(double x) {
            return Math.pow(x - 2.0, 2.0);
        }

        /**
         * Returns the minimum of the function.
         *
         * @return the minimum of the function.
         */
        public double min() {
            return 0.0;
        }

        /**
         * Returns the maximum of the function.
         *
         * @return the maximum of the function.
         */
        public double max() {
            return 4.0;
        }
    }

    /**
     * Tests finding minimum of a simple quadratic function.
     */
    @Test
    public void testFindMinimumOfQuadraticFunction() {
        UnivariateFunction function = new TestFunction();
        ConvergenceChecker<UnivariatePointValuePair> checker
            = new MaxEval(100);
        BrentOptimizer optimizer
            = new BrentOptimizer(1e-6, 1e-5, checker);

        UnivariatePointValuePair result
            = optimizer.optimize(function, GoalType.MINIMIZE);

        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Tests finding minimum of a function with absolute threshold smaller than
     * relative threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testFindMinimumWithAbsThresholdSmallerThanRelThreshold() {
        UnivariateFunction function = new TestFunction();
        ConvergenceChecker<UnivariatePointValuePair> checker
            = new MaxEval(100);
        BrentOptimizer optimizer
            = new BrentOptimizer(1e-6, 1e-7, checker);
    }

    /**
     * Tests finding minimum of a function with negative relative threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFindMinimumWithNegativeRelativeThreshold() {
        UnivariateFunction function = new TestFunction();
        ConvergenceChecker<UnivariatePointValuePair> checker
            = new MaxEval(100);
        BrentOptimizer optimizer
            = new BrentOptimizer(-1e-6, 1e-5, checker);
    }

    /**
     * Tests finding minimum of a function with non-positive absolute threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFindMinimumWithNonPositiveAbsoluteThreshold() {
        UnivariateFunction function = new TestFunction();
        ConvergenceChecker<UnivariatePointValuePair> checker
            = new MaxEval(100);
        BrentOptimizer optimizer
            = new BrentOptimizer(1e-6, -1e-5, checker);
    }

    /**
     * Tests finding maximum of a simple quadratic function.
     */
    @Test
    public void testFindMaximumOfQuadraticFunction() {
        UnivariateFunction function = new TestFunction();
        ConvergenceChecker<UnivariatePointValuePair> checker
            = new MaxEval(100);
        BrentOptimizer optimizer
            = new BrentOptimizer(1e-6, 1e-5, checker);

        UnivariatePointValuePair result
            = optimizer.optimize(function, GoalType.MAXIMIZE);

        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(4.0, result.getValue(), 1e-6);
    }
}