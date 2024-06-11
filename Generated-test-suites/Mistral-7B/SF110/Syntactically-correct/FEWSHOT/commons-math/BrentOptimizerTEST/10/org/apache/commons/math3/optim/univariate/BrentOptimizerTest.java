package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizerWithMinimizeGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        OptimizeResult result = optimizer.optimize(f, -5.0, 5.0);
        assertNotNull(result);
        assertEquals(3.0, result.getPoint().getX(), 0.001);
        assertEquals(-2.4184614555473214E9, result.getValue(), 1.0e-15);
    }

    @Test
    public void testBrentOptimizerWithMaximizeGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        optimizer.setGoalType(GoalType.MAXIMIZE);
        OptimizeResult result = optimizer.optimize(f, -5.0, 5.0);
        assertNotNull(result);
        assertEquals(-3.0, result.getPoint().getX(), 0.001);
        assertEquals(2.4184614555473214E9, result.getValue(), 1.0e-15);
    }

    @Test
    public void testBrentOptimizerWithNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        FunctionFunction f = null;
        try {
            optimizer.optimize(f, -5.0, 5.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerWithStartValueOutOfRange() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        try {
            optimizer.optimize(f, Double.NEGATIVE_INFINITY, 5.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            optimizer.optimize(f, 5.0, Double.POSITIVE_INFINITY);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerWithNullConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        OptimizeResult result = optimizer.optimize(f, -5.0, 5.0, checker);
        assertNotNull(result);
    }

    @Test
    public void testBrentOptimizerWithInvalidRelativeThreshold() {
        try {
            new BrentOptimizer(-0.01, 0.001);
            fail("NumberIsTooSmallException expected");
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        try {
            new BrentOptimizer(0.0, 0.001);
            fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerWithInvalidAbsoluteThreshold() {
        try {
            new BrentOptimizer(0.01, -0.001);
            fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new BrentOptimizer(0.01, 0.0);
            fail("NumberIsTooSmallException expected");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerWithInvalidGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        try {
            optimizer.setGoalType(GoalType.INF_MIN);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            optimizer.setGoalType(GoalType.INF_MAX);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}