package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeThreshold() {
        new BrentOptimizer(0.0, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsoluteThreshold() {
        new BrentOptimizer(0.001, -0.01);
    }

    @Test
    public void testBrentOptimizerWithNullConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01, convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimizeWithMinimizeGoalType() {
        FunctionFunction f = new AbstractFunctionFunction("x^2") {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(-2.44948974278317, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimizeWithMaximizeGoalType() {
        FunctionFunction f = new AbstractFunctionFunction("x^2") {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01, GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(2.44948974278317, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimizeWithMinValueBelowMaxValue() {
        FunctionFunction f = new AbstractFunctionFunction("x^3 - 3 * x + 2") {
            @Override
            public double value(double x) {
                return x * x * x - 3 * x + 2;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(1.0, result.getPoint(), 0.0);
        assertEquals(2.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimizeWithMinValueAboveMaxValue() {
        FunctionFunction f = new AbstractFunctionFunction("x^3 - 3 * x + 2") {
            @Override
            public double value(double x) {
                return x * x * x - 3 * x + 2;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        try {
            optimizer.optimize(f, 5.0, 5.0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDoOptimizeWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        FunctionFunction f = new AbstractFunctionFunction("x^2") {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01, convergenceChecker);
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        verify(convergenceChecker).converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class), isA(Boolean.class));
        assertEquals(-2.44948974278317, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }
}