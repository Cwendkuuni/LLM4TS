package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.InitialGuess.MIDPOINT;

/**
 * Test cases for the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Tests the optimization of a simple quadratic function.
     * @throws NotStrictlyPositiveException if {@code abs <= 0} or {@code rel < 2 * Math.ulp(1d)}.
     */
    @Test
    public void testSimpleQuadraticFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-3);
        double function = x -> Math.pow(x, 2) + 2 * x + 1;
        UnivariateOptimizerResult result = optimizer.optimize(function, MIDPOINT, GoalType.MINIMIZE);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(1d, 0.25);
        Assert.assertEquals(expected, result.getPoint());
    }

    /**
     * Tests the optimization of a simple quadratic function with a negative
     * absolute threshold.
     * @throws NotStrictlyPositiveException if {@code abs <= 0} or {@code rel < 2 * Math.ulp(1d)}.
     */
    @Test
    public void testSimpleQuadraticFunctionNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-3);
        double function = x -> Math.pow(x, 2) + 2 * x + 1;
        UnivariateOptimizerResult result = optimizer.optimize(function, MIDPOINT, GoalType.MINIMIZE);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(1d, 0.25);
        Assert.assertEquals(expected, result.getPoint());
    }

    /**
     * Tests the optimization of a simple quadratic function with a convergence
     * checker.
     * @throws NotStrictlyPositiveException if {@code abs <= 0} or {@code rel < 2 * Math.ulp(1d)}.
     */
    @Test
    public void testSimpleQuadraticFunctionConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-3, new CustomConvergenceChecker());
        double function = x -> Math.pow(x, 2) + 2 * x + 1;
        UnivariateOptimizerResult result = optimizer.optimize(function, MIDPOINT, GoalType.MINIMIZE);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(1d, 0.25);
        Assert.assertEquals(expected, result.getPoint());
    }

    /**
     * Tests the optimization of a simple quadratic function with a non-monotonic
     * function.
     * @throws NotStrictlyPositiveException if {@code abs <= 0} or {@code rel < 2 * Math.ulp(1d)}.
     */
    @Test(expected = org.apache.commons.math3.exception.GoalTypeMismatchedException.class)
    public void testSimpleQuadraticFunctionNonMonotonic() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-3);
        double function = x -> Math.pow(x, 2) + 2 * x - 1;
        optimizer.optimize(function, MIDPOINT, GoalType.MINIMIZE);
    }

    /**
     * Tests the optimization of a simple quadratic function with a non-univariate
     * function.
     * @throws NotStrictlyPositiveException if {@code abs <= 0} or {@code rel < 2 * Math.ulp(1d)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleQuadraticFunctionNonUnivariate() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-3);
        double function = (x, y) -> Math.pow(x, 2) + 2 * x + 1;
        optimizer.optimize(function, MIDPOINT, GoalType.MINIMIZE);
    }

    /**
     * Test the behavior when the function argument is {@code null}.
     * @throws NotStrictlyPositiveException if {@code abs <= 0} or {@code rel < 2 * Math.ulp(1d)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-3);
        optimizer.optimize(null, MIDPOINT, GoalType.MINIMIZE);
    }

    /**
     * Test the behavior when the initial guess is outside the function range.
     * @throws NotStrictlyPositiveException if {@code abs <= 0} or {@code rel < 2 * Math.ulp(1d)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInitialGuessOutOfRange() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-3);
        double function = x -> Math.sin(x);
        optimizer.optimize(function, Double.NaN, GoalType.MINIMIZE);
    }

    /**
     * Test the behavior when the goal type is not supported.
     * @throws NotStrictlyPositiveException if {@code abs <= 0} or {@code rel < 2 * Math.ulp(1d)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-3);
        double function = x -> x * x;
        optimizer.optimize(function, MIDPOINT, GoalType.MAXIMIZE);
    }

    /**
     * Custom convergence checker for testing.
     */
    private static class CustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            double error = Precision.abs(current.getValue() - previous.getValue());
            boolean converged = error < 1e-8;
            if (converged) {
                System.out.println("Convergence achieved in " + iteration + " iterations.");
            }
            return converged;
        }
    }
}