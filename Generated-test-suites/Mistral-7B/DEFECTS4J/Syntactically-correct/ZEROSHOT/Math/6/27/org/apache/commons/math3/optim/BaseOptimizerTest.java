package org.apache.commons.math3.optimization.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.initializer.AroundMinimum;
import org.apache.commons.math3.optim.initializer.RandomStart;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;

import org.junit.Test;

/**
 * Test class for {@link BaseOptimizer}. This is a parametrized JUnit 4 test file.
 *
 * @author John Doe
 */
public class BaseOptimizerTest<PAIR extends PointValuePair> {

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new MockBaseOptimizer(10);
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(10, maxEvaluations);
    }

    /**
     * Test getEvaluations method when optimize method is not called.
     */
    @Test
    public void testGetEvaluationsBeforeOptimize() {
        BaseOptimizer<PAIR> optimizer = new MockBaseOptimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        });
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    /**
     * Test getEvaluations method when optimize method is called.
     */
    @Test
    public void testGetEvaluationsAfterOptimize() {
        BaseOptimizer<PAIR> optimizer = new MockBaseOptimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        });
        ConvergenceChecker<PAIR> convergenceChecker = new SimpleValueChecker(1e-6);
        PointValuePair solution = optimizer.optimize(new AroundMinimum(0.5), convergenceChecker);
        int evaluations = optimizer.getEvaluations();
        assertEquals(11, evaluations); // 10 for function evaluations and one for the initial point
    }

    /**
     * Test getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new MockBaseOptimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        });
        ConvergenceChecker<PAIR> convergenceChecker = new SimpleValueChecker(1e-6);
        optimizer.setConvergenceChecker(convergenceChecker);
        ConvergenceChecker<PAIR> returnedChecker = optimizer.getConvergenceChecker();
        assertEquals(convergenceChecker, returnedChecker);
    }

    /**
     * Test setConvergenceChecker method.
     */
    @Test
    public void testSetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new MockBaseOptimizer(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        });
        ConvergenceChecker<PAIR> convergenceChecker = new SimpleValueChecker(1e-6);
        optimizer.setConvergenceChecker(convergenceChecker);
        assertNull(optimizer.getConvergenceChecker()); // getConvergenceChecker should return null before setting
    }

    /**
     * A mock optimizer for testing BaseOptimizer.
     */
    private static class MockBaseOptimizer<PAIR extends PointValuePair> extends BaseOptimizer<PAIR> {

        private int maxEvaluations;
        private ConvergenceChecker<PAIR> convergenceChecker;
        private int functionEvaluations;

        public MockBaseOptimizer(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public MockBaseOptimizer(UnivariateFunction function) {
            this(new MaxEval(function, maxEvaluations));
        }

        @Override
        public PAIR optimize(final MaxEval maxEval, final ConvergenceChecker<PAIR> checker) {
            PointValuePair solution = new PointValuePair(Math.random(), Math.random());
            functionEvaluations += maxEval.getMax();
            return solution;
        }

        @Override
        public void setConvergenceChecker(ConvergenceChecker<PAIR> checker) {
            convergenceChecker = checker;
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return convergenceChecker;
        }

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return functionEvaluations;
        }
    }
}