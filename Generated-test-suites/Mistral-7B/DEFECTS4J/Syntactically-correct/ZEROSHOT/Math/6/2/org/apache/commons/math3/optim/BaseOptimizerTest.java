package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link BaseOptimizer}.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer<>();
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertNotNull(maxEvaluations);
        Assert.assertEquals(0, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer<>();
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(0, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = new DummyBaseOptimizer<>();
        ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
    }

    /**
     * Dummy implementation of {@link BaseOptimizer}.
     */
    private static class DummyBaseOptimizer<PAIR> implements BaseOptimizer<PAIR> {

        private int evaluations = 0;

        @Override
        public int getMaxEvaluations() {
            return MaxEval.UNLIMITED;
        }

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return new DummyConvergenceChecker<>();
        }
    }

    /**
     * Dummy implementation of {@link ConvergenceChecker}.
     */
    private static class DummyConvergenceChecker implements ConvergenceChecker<Object> {

        @Override
        public boolean isConverged(Object value) {
            return false;
        }
    }
}