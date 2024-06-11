package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueFunction;
import org.apache.commons.math3.optim.initializer.AverageInitializer;
import org.apache.commons.math3.optim.initializer.RandomInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.point.PointValues;
import org.apache.commons.math3.optim.search.BracketingNelderMead;
import org.apache.commons.math3.optim.search.BracketingNelderMead.StoppingCondition;
import org.apache.commons.math3.optim.search.NelderMeadSimplex;
import org.apache.commons.math3.optim.search.SimpleXSearch;
import org.apache.commons.math3.optim.search.convergence.ConvergenceChecker;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for BaseOptimizer interface. Due to its abstract nature,
 * it's impossible to test the interface itself. Instead, we test
 * implementations of optimizers that adhere to the BaseOptimizer
 * interface.
 *
 * @since 3.0
 */
public class BaseOptimizerTest<PAIR extends PointValuePair> {

    private ConvergenceChecker<PAIR> convergenceChecker;
    private SimpleValueFunction function;
    private BaseOptimizer<PAIR> optimizer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        convergenceChecker = new ConvergenceChecker<PAIR>() {
            @Override
            public boolean isConverged(PAIR current, PAIR best) {
                // Convergence check implemented in subclass
                return false;
            }
        };

        function = new SimpleValueFunction(1) {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2);
            }
        };

        optimizer = new BracketingNelderMead<PAIR>(function, GoalType.MINIMIZE) {
            @Override
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return convergenceChecker;
            }
        };
    }

    /**
     * Tests getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    /**
     * Tests getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        optimizer.optimize(new PointValues(new double[] { 1.0, 2.0 }));
        assertEquals(1, optimizer.getEvaluations() - evaluations);
    }

    /**
     * Tests getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test optimization with initial SimpleXSearch in the interval [0, 1].
     */
    @Test
    public void testSimpleXSearchOptimization() {
        optimizer.setMaxEvaluations(100);
        PointValuePair best = optimizer.optimize(new PointValues(new double[] { 0.5, 0.5 }));
        assertEquals(0.0, best.getPoint()[0], 1e-8);
        assertEquals(0.0, best.getValue(), 1e-8);
        assertTrue(optimizer.isConverged(best, null));
        assertEquals(100, optimizer.getEvaluations());
    }

    /**
     * Test optimization with initial NelderMeadSimplex in the interval [-10, 10].
     */
    @Test
    public void testNelderMeadSimplexOptimization() {
        optimizer.setMaxEvaluations(100);
        PointValues initial = new PointValues(new double[][] {
                {-5.0, -5.0},
                {-5.0, 5.0},
                {5.0, -5.0},
                {5.0, 5.0}
        });
        PointValuePair best = optimizer.optimize(initial);
        assertEquals(-1.0, best.getPoint()[0], 1e-8);
        assertEquals(0.0, best.getValue(), 1e-8);
        assertTrue(optimizer.isConverged(best, null));
        assertEquals(100, optimizer.getEvaluations());
    }

    /**
     * Test optimization with initial AverageInitializer.
     */
    @Test
    public void testAverageInitializerOptimization() {
        optimizer.setMaxEvaluations(100);
        PointValues initial = new PointValues(new double[][] {
                {2.5, 2.5},
                {3.5, 3.5},
                {4.5, 4.5}
        });
        PointValuePair best = optimizer.optimize(initial);
        assertEquals(1.0, best.getPoint()[0], 1e-8);
        assertEquals(1.0, best.getValue(), 1e-8);
        assertFalse(optimizer.isConverged(best, null));
        assertEquals(100, optimizer.getEvaluations());
    }

    /**
     * Test optimization with initial RandomInitializer.
     */
    @Test
    public void testRandomInitializerOptimization() {
        optimizer.setMaxEvaluations(100);
        PointValues initial = new PointValues(new double[][] {
                {0.1, 0.1},
                {-1.5, 2.4},
                {3.2, -4.5}
        });
        PointValuePair best = optimizer.optimize(initial);
        // Testing random initialization, no particular value expected
        // The test checks if the algorithm converges and the number of evaluations
        double expectedBest = Math.min(Math.min(-1.0, 1.0), Math.sqrt(100.0 / 3.0));
        assertEquals(expectedBest, best.getValue(), 1e-8);
        assertTrue(optimizer.isConverged(best, null));
        assertEquals(100, optimizer.getEvaluations());
    }

    /**
     * Test optimization with stopping condition.
     */
    @Test
    public void testStoppingConditionOptimization() {
        int maxEvaluations = 10;
        optimizer.setMaxEvaluations(maxEvaluations);
        StoppingCondition stoppingCondition = new StoppingCondition() {
            @Override
            public boolean isStopConditionSatisfied(int iteration, PointValues currentPointValues) {
                return currentPointValues.size() > 10;
            }
        };
        optimizer.setStoppingCondition(stoppingCondition);
        PointValues initial = new PointValues(new double[][] {
                {0.0, 0.0},
                {1.0, 1.0},
                {2.0, 2.0}
        });
        PointValues result = optimizer.optimize(initial);
        assertTrue(result.size() > 10);
        assertEquals(maxEvaluations, optimizer.getEvaluations());
    }
}