package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method with function {@code f(x) = x^2}.
     */
    @Test
    public void testBrentOptimizer_functionXSquared() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = x -> Math.pow(x, 2);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
        assertEquals(-2.5, result.getPoint(), 1e-6);
    }

    /**
     * Test method with function {@code f(x) = x^3 - 2}.
     */
    @Test
    public void testBrentOptimizer_functionXCubedMinusTwo() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = x -> Math.pow(x, 3) - 2;
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
        assertEquals(-1.324717957244746, result.getPoint(), 1e-6);
    }

    /**
     * Test method with function {@code f(x) = Math.sin(x)}.
     */
    @Test
    public void testBrentOptimizer_functionSin() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = Math::sin;
        UnivariatePointValuePair result = optimizer.optimize(function, -FastMath.PI, FastMath.PI, GoalType.MINIMIZE);
        assertEquals(-1.1752011936438013, result.getPoint(), 1e-6);
    }

    /**
     * Test convergence checker.
     */
    @Test(expected = AssertionError.class)
    public void testBrentOptimizer_convergenceChecker() throws NotStrictlyPositiveException {
        double rel = 1e-8;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker
            = (iter, previous, current) -> {
                if (iter % 1000 == 0) {
                    double xDiff = Math.abs(current.getPoint() - previous.getPoint());
                    double fxDiff = Math.abs(current.getValue() - previous.getValue());
                    double tolerance = rel * Math.max(Math.abs(current.getPoint()), 1) + abs;
                    if (xDiff > tolerance || fxDiff > tolerance) {
                        throw new AssertionError("Convergence criteria not met.");
                    }
                }
            };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        UnivariateFunction function = x -> Math.pow(x, 2);
        optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
    }

    /**
     * Test invalid absolute threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_invalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
    }

    /**
     * Test invalid relative threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_invalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 1e-6);
    }
}