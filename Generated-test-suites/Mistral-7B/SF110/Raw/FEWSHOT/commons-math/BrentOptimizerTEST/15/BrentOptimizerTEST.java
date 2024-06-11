package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.commons.math3.optim.*;
import org.apache.commons.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.commons.math3.optim.univariate.*;
import org.apache.commons.commons.math3.util.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        try {
            new BrentOptimizer(-1.0, 1.0, null);
            assert false; // NumberIsTooSmallException expected
        } catch (NumberIsTooSmallException e) {
            // expected
        }
        new BrentOptimizer(0.0, 1.0, null);
        new BrentOptimizer(1.0, 0.0, null);
        new BrentOptimizer(1.0, 1.0, null);
        new BrentOptimizer(1.5, 1.0, null);

        new BrentOptimizer(1.0, 1.0, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair best) {
                return iter > 10;
            }
        });
    }

    @Test
    public void testBrentOptimizerDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        OptimizeResult result = optimizer.optimize(function, 0.0, 10.0, null);

        double expectedX = 3.141592653589793;
        double expectedValue = 0.0027586545868683464;

        assertEquals(expectedX, result.getPoint().getX(), 1e-6);
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerDoOptimizeWithMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, GoalType.MINIMIZE);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(Math.sin(x), 2);
            }
        };

        OptimizeResult result = optimizer.optimize(function, 0.0, 10.0, null);

        double expectedX = 3.141592653589793;
        double expectedValue = 0.0027586545868683464;

        assertEquals(expectedX, result.getPoint().getX(), 1e-6);
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerDoOptimizeWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair best) {
                return iter > 5;
            }
        });
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        OptimizeResult result = optimizer.optimize(function, 0.0, 10.0, null);

        double expectedX = 1.0;
        double expectedValue = 1.0;

        assertEquals(expectedX, result.getPoint().getX(), 1e-6);
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerDoOptimizeWithCustomGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, new GoalType() {
            @Override
            public boolean isMinimization() {
                return false;
            }
        });
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        OptimizeResult result = optimizer.optimize(function, 0.0, 10.0, null);

        double expectedX = -3.141592653589793;
        double expectedValue = 0.0027586545868683464;

        assertEquals(expectedX, result.getPoint().getX(), 1e-6);
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }
}