package org.apache.commons.math3.optimization.base;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;

/**
 * Test file for the BaseOptimizer interface.
 *
 * @author John Doe
 */
public class BaseOptimizerTest {

    /**
     * Test method for the getMaxEvaluations() method.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(100);

        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(100, maxEvaluations);
    }

    /**
     * Test method for the getEvaluations() method.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(50);

        int evaluations = optimizer.getEvaluations();
        assertEquals(50, evaluations);
    }

    /**
     * Test method for the getConvergenceChecker() method.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<Object> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);

        ConvergenceChecker<Object> returnedChecker = optimizer.getConvergenceChecker();
        assertEquals(convergenceChecker, returnedChecker);
    }

    /**
     * Test method for BaseOptimizer with default values.
     */
    @Test
    public void testDefaultValues() {
        BaseOptimizer<?> optimizer = new BaseOptimizer<Object>() {
            @Override
            public int getMaxEvaluations() {
                return MaxEval.UNLIMITED;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<? super Object> getConvergenceChecker() {
                return null;
            }
        };

        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(MaxEval.UNLIMITED, maxEvaluations);

        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);

        ConvergenceChecker<?> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);
    }
}