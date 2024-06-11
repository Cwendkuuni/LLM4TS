package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.maximizer.MaxEval;
import org.apache.commons.math3.optim.maximizer.SimpleBoundingBoxMinimizer;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners. Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class BaseOptimizerTest<PAIR extends PointValuePair> {

    private BaseOptimizer<PAIR> baseOptimizer;
    private int maxEvaluations;
    private ConvergenceChecker<PAIR> convergenceChecker;

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new SimpleBoundingBoxMinimizer<>(), 100, new SimpleConvergenceChecker<>()}
        });
    }

    public BaseOptimizerTest(BaseOptimizer<PAIR> baseOptimizer, int maxEvaluations, ConvergenceChecker<PAIR> convergenceChecker) {
        this.baseOptimizer = baseOptimizer;
        this.maxEvaluations = maxEvaluations;
        this.convergenceChecker = convergenceChecker;
    }

    @Before
    public void setUp() {
        baseOptimizer.setMaxEvaluations(maxEvaluations);
        baseOptimizer.setConvergenceChecker(convergenceChecker);
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(maxEvaluations, baseOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        baseOptimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        }, new AveragedStartingPointGenerator(1.0, 1.0), new MaxEval(100));
        assertEquals(1, baseOptimizer.getEvaluations());

        baseOptimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        }, new AveragedStartingPointGenerator(1.0, 1.0), new MaxEval(100));
        assertEquals(2, baseOptimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        assertEquals(convergenceChecker, baseOptimizer.getConvergenceChecker());
    }

    // Add more test methods here as needed

    static class SimpleConvergenceChecker implements ConvergenceChecker<PAIR> {
        @Override
        public boolean isConverged(PointValuePair best, PointValuePair current) {
            return Math.abs(best.getValue() - current.getValue()) < 1e-10;
        }
    }
}