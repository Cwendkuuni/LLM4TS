package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.nDeriv.BrentGradient;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

/**
 * Test class for {@link BaseOptimizer}. All tests are based on the
 * {@link BrentGradient} optimizer.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @org.junit.Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BrentGradient();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals("Maximum number of evaluations should be set to " + MaxEval.MAX_EVALUATIONS,
                MaxEval.MAX_EVALUATIONS, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @org.junit.Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BrentGradient();
        UnivariateFunction f = new MyFunction();
        PointValuePair solution = optimizer.optimize(f, new SimpleBounds(1.0, 3.0), null);
        int evaluations = optimizer.getEvaluations();
        assertEquals("Number of function evaluations should be greater than zero", 1 + f.getNDerivatives(), evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @org.junit.Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new BrentGradient();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull("Convergence checker should not be null", convergenceChecker);
    }

    /**
     * Test function for optimization.
     */
    private static class MyFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * Math.sin(x) * Math.cos(x);
        }

        @Override
        public int getNDerivatives() {
            return 2;
        }
    }
}