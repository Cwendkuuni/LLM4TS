package org.apache.commons.math3.optim.univariate.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        // Test valid input
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        Assert.assertNotNull(optimizer);

        // Test invalid input - relative tolerance too small
        try {
            new BrentOptimizer(0.001, 0.001, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }

        // Test invalid input - absolute tolerance <= 0.0
        try {
            new BrentOptimizer(0.01, -0.001, null);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testEmptyConstructor() {
        // Test empty constructor
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize_InitialValues() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        FunctionFunction function = new FunctionFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        UnivariateOptimizerResult result = optimizer.optimize(function, -10.0, 10.0);
        assertTrue(result.isOptimal());
        assertTrue(Math.abs(result.getPoint().getValue() - Math.sqrt(2.0)) < 0.001);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 3.0);

        // Test with minimum
        assertTrue(new BrentOptimizer(false).best(a, null, true) == a);
        assertTrue(new BrentOptimizer(false).best(null, b, true) == b);
        assertTrue(new BrentOptimizer(false).best(a, b, true) == a);
        assertTrue(new BrentOptimizer(false).best(a, b, false) == b);

        // Test with maximum
        assertTrue(new BrentOptimizer(true).best(a, null, true) == a);
        assertTrue(new BrentOptimizer(true).best(null, b, true) == b);
        assertTrue(new BrentOptimizer(true).best(a, b, true) == b);
        assertTrue(new BrentOptimizer(true).best(a, b, false) == a);
    }

    @Test
    public void testConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> mockChecker = mock(ConvergenceChecker.class);
        when(mockChecker.converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class))).thenReturn(true);

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, mockChecker);
        FunctionFunction function = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateOptimizerResult result = optimizer.optimize(function, -10.0, 10.0);
        assertTrue(result.isOptimal());
        verify(mockChecker).converged(anyInt(), eq(result.getPreviousPoint()), eq(result.getCurrentPoint()));
    }
}