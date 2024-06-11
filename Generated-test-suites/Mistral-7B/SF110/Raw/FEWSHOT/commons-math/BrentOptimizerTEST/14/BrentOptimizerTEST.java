package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        assertEquals("Brent Optimizer created with goal type: " + goalType, goalType, optimizer.getGoalType());

        try {
            optimizer = new BrentOptimizer(-1e-3, 1e-5);
            fail("Should not be able to create optimizer with negative relative tolerance.");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            optimizer = new BrentOptimizer(1e-3, -1e-5);
            fail("Should not be able to create optimizer with negative absolute tolerance.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            optimizer = new BrentOptimizer(1e-3, 1e-5, null);
            optimizer = new BrentOptimizer(1e-3, 1e-5, new ConvergenceChecker<UnivariatePointValuePair>() {
                @Override
                public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                    return iter > 10;
                }
            });
        } catch (IllegalArgumentException e) {
            // Expected, since the given ConvergenceChecker implementation does not implement the converged method.
        }
    }

    @Test
    public void testDoOptimize_simpleTestCase() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals("Optimizer did not find the minimum correctly", -1.71828, result.getValue(), 1e-5);
        assertEquals("Optimizer did not find the minimum correctly: x", -1.0, result.getPoint(), 1e-5);
    }

    @Test
    public void testDoOptimize_noSolution() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, 10.0, 10.0);
        assertNull("Optimizer found a solution when none should exist", result);
    }

    @Test
    public void testDoOptimize_multipleSolutions() {
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-5);
        UnivariatePointValuePair result1 = optimizer.optimize(function, -10.0, 0.0);
        UnivariatePointValuePair result2 = optimizer.optimize(function, 0.0, 10.0);

        double tolerance = 1e-5;
        assertEquals("Optimizer did not find the first minimum correctly", -1.71828, result1.getValue(), tolerance);
        assertEquals("Optimizer did not find the first minimum correctly: x", -1.0, result1.getPoint(), tolerance);

        assertEquals("Optimizer did not find the second minimum correctly", -1.71828, result2.getValue(), tolerance);
        assertEquals("Optimizer did not find the second minimum correctly: x", 0.0, result2.getPoint(), tolerance);
    }
}