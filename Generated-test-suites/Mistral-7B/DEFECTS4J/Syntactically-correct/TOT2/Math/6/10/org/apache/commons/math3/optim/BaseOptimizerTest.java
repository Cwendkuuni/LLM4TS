// Test cases for the BaseOptimizer interface

package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.apache.commons.math3.optimization.BaseOptimizer;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(100);
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(100, maxEvaluations);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMaxEvaluationsNegative() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenThrow(new IllegalStateException("Maximum number of evaluations cannot be negative."));
        int maxEvaluations = optimizer.getMaxEvaluations();
    }

    @Test
    public void testGetMaxEvaluationsZero() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(0);
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluationsBeforeOptimize() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        int evaluationsBeforeOptimize = optimizer.getEvaluations();
        assertEquals(0, evaluationsBeforeOptimize);
    }

    @Test
    public void testGetEvaluationsMultipleCalls() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(0).thenReturn(5).thenReturn(10);
        int evaluations1 = optimizer.getEvaluations();
        int evaluations2 = optimizer.getEvaluations();
        int evaluations3 = optimizer.getEvaluations();
        assertEquals(0, evaluations1);
        assertEquals(5, evaluations2);
        assertEquals(10, evaluations3);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);

        BaseOptimizer<Object> optimizer2 = new BaseOptimizer<Object>() {};
        assertNull(optimizer2.getConvergenceChecker());
    }

}