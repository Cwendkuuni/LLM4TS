package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEvalException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;

/**
 * Test class for {@link BaseOptimizer} interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(50);
        assertEquals(50, optimizer.getEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} with convergence.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOptimizeConvergence() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        PointValuePair bestPoint = new PointValuePair(new double[]{1.0, 2.0}, 3.0);
        when(convergenceChecker.isConverged(any(), anyInt())).thenReturn(true);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);

        try {
            optimizer.optimize(new GoalType() {
                @Override
                public double value(double[] input) {
                    return Math.pow(input[0], 2) + Math.pow(input[1], 2);
                }
            }, null, null);
            fail("MaxEvalException expected");
        } catch (MaxEvalException e) {
            // expected
        }

        verify(optimizer).getConvergenceChecker();
        verify(convergenceChecker).isConverged(eq(bestPoint), eq(1));
    }

    /**
     * Test method for {@link BaseOptimizer#optimize()} without convergence.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOptimizeNoConvergence() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.isConverged(any(), anyInt())).thenReturn(false);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);

        try {
            optimizer.optimize(new GoalType() {
                @Override
                public double value(double[] input) {
                    return Math.pow(input[0], 2) + Math.pow(input[1], 2);
                }
            }, null, null);
            fail("MaxEvalException expected");
        } catch (MaxEvalException e) {
            // expected
        }

        verify(optimizer, times(10)).getConvergenceChecker();
    }
}