package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test constructor with relative and absolute tolerance and ConvergenceChecker.
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-6, null);
        assertEquals("Relative and absolute threshold set correctly.", 1e-5, optimizer.getRelativeThreshold(), 0.0);
        assertEquals("Absolute threshold set correctly.", 1e-6, optimizer.getAbsoluteThreshold(), 0.0);

        // Test constructor with relative and absolute tolerance only.
        optimizer = new BrentOptimizer(1e-5, 1e-6);
        assertEquals("Relative threshold set correctly.", 1e-5, optimizer.getRelativeThreshold(), 0.0);
        assertEquals("Absolute threshold set correctly.", 1e-6, optimizer.getAbsoluteThreshold(), 0.0);

        // Test exception when relative threshold is less than minimum relative tolerance.
        try {
            new BrentOptimizer(0, 1e-6);
        } catch (NumberIsTooSmallException e) {
            assertEquals("Minimum relative tolerance not met.", "Relative threshold is less than minimum relative tolerance.", e.getMessage());
        }

        // Test exception when absolute threshold is less than or equal to zero.
        try {
            new BrentOptimizer(1e-5, 0);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Absolute threshold must be positive.", "Absolute threshold is not positive.", e.getMessage());
        }

        // Test Brent's algorithm with a simple function.
        FunctionFunction func = new FunctionFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2) - 2 * FastMath.pow(x, 1.5) + 1;
            }
        };
        UnivariateOptimizer opt = new BrentOptimizer(1e-5, 1e-6);
        UnivariatePointValuePair solution = opt.optimize(func, -5, 5, null);
        assertEquals("The solution should be approximately -2.71828.", -2.71828, solution.getPoint(), 0.0001);
    }
}