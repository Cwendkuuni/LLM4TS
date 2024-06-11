package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

import static org.mockito.Mockito.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        // Test constructor with ConvergenceChecker
        ConvergenceChecker<UnivariatePointValuePair> mockChecker = mock(ConvergenceChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, mockChecker);
        assertNotNull(optimizer);
        verify(mockChecker, times(0)).converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class));

        // Test constructor without ConvergenceChecker
        BrentOptimizer optimizer2 = new BrentOptimizer(0.1, 0.01);
        assertNotNull(optimizer2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativetoSmall() {
        new BrentOptimizer(0.0001, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerAbsToNegative() {
        new BrentOptimizer(1.0, -0.01);
    }

    @Test
    public void testBrentOptimizerDoOptimize() {
        UnivariateFunction mockFunction = mock(UnivariateFunction.class);
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, null);
        double min = 2.0;
        when(mockFunction.value(min)).thenReturn(minSquared = min * min);

        UnivariatePointValuePair result = optimizer.optimize(mockFunction, -1.0, 3.0);

        assertEquals(min, result.getPoint(), 0.01);
        assertEquals(minSquared, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerDoOptimizeConvergenceCheck() {
        UnivariateFunction mockFunction = mock(UnivariateFunction.class);
        ConvergenceChecker<UnivariatePointValuePair> mockChecker = mock(ConvergenceChecker.class);
        when(mockChecker.converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class))).thenReturn(true);
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, mockChecker);

        UnivariatePointValuePair result = optimizer.optimize(mockFunction, -1.0, 3.0);

        verify(mockChecker, times(1)).converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class));
    }

    @Test
    public void testBrentOptimizerDoOptimizeConvergenceNotCheck() {
        UnivariateFunction mockFunction = mock(UnivariateFunction.class);
        ConvergenceChecker<UnivariatePointValuePair> mockChecker = mock(ConvergenceChecker.class);
        when(mockChecker.converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class))).thenReturn(false);
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, mockChecker);

        UnivariatePointValuePair result = optimizer.optimize(mockFunction, -1.0, 3.0);

        verify(mockChecker, never()).converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class));
    }
}