package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.*;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        // Test construction with valid arguments
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        assertNotNull(optimizer);

        // Test construction with invalid relative tolerance
        try {
            new BrentOptimizer(-0.01, 1.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test construction with invalid absolute tolerance
        try {
            new BrentOptimizer(0.01, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test construction with null ConvergenceChecker
        BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 1.0, null);
        assertNotNull(optimizer2);
    }

    @Test
    public void testBrentOptimizerMinimize() {
        Function function = new FunctionWithRoots() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 1.0, null);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);

        assertEquals(2.0, result.getPoint(), 0.0);
        assertEquals(-3.999999999999999E-7, result.getValue(), 1.0e-7);
    }

    @Test
    public void testBrentOptimizerMinimizeConvergenceChecker() {
        Function function = new FunctionWithRoots() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -x) - Math.log(2.0);
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return current.getValue() - previous.getValue() < 1e-6;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 1.0, convergenceChecker);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);

        assertEquals(1.0, result.getPoint(), 1.0e-6);
        assertEquals(-0.459685115585647E-7, result.getValue(), 1.0e-7);
    }

    @Test
    public void testBrentOptimizerMaximize() {
        Function function = new FunctionWithRoots() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.exp(-x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 1.0, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);

        assertEquals(0.0, result.getPoint(), 1.0e-6);
        assertEquals(2.0, result.getValue(), 1.0e-6);
    }

    @Test
    public void testBrentOptimizerMaximizeConvergenceChecker() {
        Function function = new FunctionWithRoots() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - Math.log(2.0);
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return current.getValue() - previous.getValue() < 1e-6;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 1.0, convergenceChecker);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 5.0);

        assertEquals(1.0, result.getPoint(), 1.0e-6);
        assertEquals(0.459685115585647E-7, result.getValue(), 1.0e-7);
    }

    @Test
    public void testBrentOptimizerWithMinAndMax() {
        Function function = new FunctionWithRoots() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - Math.log(2.0);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 1.0, null);
        UnivariatePointValuePair minResult = optimizer.optimize(function, 0.0, 5.0);
        UnivariatePointValuePair maxResult = optimizer.optimize(function, 0.0, 5.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        assertEquals(1.0, minResult.getPoint(), 1.0e-6);
        assertEquals(-0.459685115585647E-7, minResult.getValue(), 1.0e-7);
        assertEquals(1.0, maxResult.getPoint(), 1.0e-6);
        assertEquals(0.459685115585647E-7, maxResult.getValue(), 1.0e-7);
    }
}