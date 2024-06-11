package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.BrentOptimizerOptions;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with default {@link ConvergenceChecker}.
     */
    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-8);
        UnivariateOptimizer result = optimizer.optimize(Math::exp, -1, 0);
        PointValuePair expected = new PointValuePair(-0.602058983387, 0.540302305868);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with custom {@link ConvergenceChecker}.
     */
    @Test
    public void testBrentOptimizerCustomChecker() {
        BrentOptimizerOptions optimizerOptions = new BrentOptimizerOptions(1e-5, 1e-8);
        BrentOptimizer optimizer = new BrentOptimizer(optimizerOptions, (p, q) -> true);
        UnivariateOptimizer result = optimizer.optimize(Math::sin, 0, 1);
        PointValuePair expected = new PointValuePair(0, 0);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with negative absolute threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(-1e-8, 1e-5);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with relative threshold smaller than machine epsilon.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        new BrentOptimizer(1e-16, 1e-8);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with null convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullConvergenceChecker() {
        new BrentOptimizer(1e-5, 1e-8, null);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double)}
     * with goal type {@code MINIMIZE}.
     */
    @Test
    public void testBrentOptimizerMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-8);
        UnivariateOptimizer result = optimizer.optimize(Math::exp, -1, 0);
        PointValuePair expected = new PointValuePair(-0.602058983387, 0.540302305868);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double)}
     * with goal type {@code MAXIMIZE}.
     */
    @Test
    public void testBrentOptimizerMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-8);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariateOptimizer result = optimizer.optimize(Math::exp, -1, 0);
        PointValuePair expected = new PointValuePair(-0.602058983387, -0.540302305868);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double)}
     * with convergence checker.
     */
    @Test
    public void testBrentOptimizerCustomCheckerComplexFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-8, (p, q) -> p.getValue() < E.E);
        UnivariateOptimizer result = optimizer.optimize(Math::log, 1, 2);
        PointValuePair expected = new PointValuePair(1, 0);
        assertEquals(expected, result);
    }
}