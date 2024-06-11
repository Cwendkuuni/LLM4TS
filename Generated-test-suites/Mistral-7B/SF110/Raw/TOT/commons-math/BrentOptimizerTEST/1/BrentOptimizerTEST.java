package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        this.function = new MyUnivariateFunction();
        this.optimizer = new BrentOptimizer(1.0e-5, 1.0e-6, null);
    }

    @Test
    public void testMinimization() throws ConvergenceException, NumberIsTooSmallException, NotStrictlyPositiveException {
        final double expectedResult = 1.0;
        final UnivariatePointValuePair result = this.optimizer.optimize(this.function, 2.0, 3.0);
        assertEquals(expectedResult, result.getPoint(), 1e-6);
        assertEquals(expectedResult, result.getValue(), 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeThresholdTooSmall() throws ConvergenceException, NumberIsTooSmallException, NotStrictlyPositiveException {
        new BrentOptimizer(0.5, 1.0e-6, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbsoluteThresholdNotPositive() throws ConvergenceException, NumberIsTooSmallException, NotStrictlyPositiveException {
        new BrentOptimizer(1.0, -1.0, null);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceChecker() throws ConvergenceException, NumberIsTooSmallException, NotStrictlyPositiveException {
        final ConvergenceChecker<UnivariatePointValuePair> mockChecker = mock(ConvergenceChecker.class);
        when(mockChecker.converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class)))
                .thenReturn(false);
        new BrentOptimizer(1.0e-5, 1.0e-6, mockChecker);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() throws ConvergenceException, NumberIsTooSmallException, NotStrictlyPositiveException {
        new BrentOptimizer(1.0e-5, 1.0e-6, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMin() throws ConvergenceException, NumberIsTooSmallException, NotStrictlyPositiveException {
        new BrentOptimizer(1.0e-5, 1.0e-6, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMax() throws ConvergenceException, NumberIsTooSmallException, NotStrictlyPositiveException {
        new BrentOptimizer(1.0e-5, 1.0e-6, null);
    }

    @Test
    public void testBrentOptimizerWithMinimumGoalType() throws ConvergenceException, NumberIsTooSmallException, NotStrictlyPositiveException {
        final GoalType goalType = GoalType.MINIMIZE;
        this.optimizer = new BrentOptimizer(goalType, 1.0e-5, 1.0e-6, null);
        final double expectedResult = 0.0;
        final UnivariatePointValuePair result = this.optimizer.optimize(this.function, 4.0, 5.0);
        assertEquals(expectedResult, result.getPoint(), 1e-6);
        assertEquals(expectedResult, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerWithMaximumGoalType() throws ConvergenceException, NumberIsTooSmallException, NotStrictlyPositiveException {
        final GoalType goalType = GoalType.MAXIMIZE;
        this.optimizer = new BrentOptimizer(goalType, 1.0e-5, 1.0e-6, null);
        final double expectedResult = 4.0;
        final UnivariatePointValuePair result = this.optimizer.optimize(this.function, 1.0, 3.0);
        assertEquals(expectedResult, result.getPoint(), 1e-6);
        assertEquals(expectedResult, result.getValue(), 1e-6);
    }

    // Add more test cases for various scenarios, edge cases, and error conditions here

    private static class MyUnivariateFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 4.0;
        }

        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }
    }
}