package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.BaseUnivariateOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** Optimizer to wrap. */
    private UnivariateRealOptimizer optimizer;
    /** Multi-start optimizer. */
    private MultiStartUnivariateRealOptimizer<TestFunction> multiStartOptimizer;
    /** Random data generator. */
    private RandomDataGenerator generator;

    /** Test function. */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.exp(-x) + Math.sin(x);
        }

        @Override
        public double derivative(double x) {
            return -Math.exp(-x) + Math.cos(x);
        }
    }

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new FibonacciSearchRealOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomDataGenerator());
    }

    /**
     * Test optimization with minimal and maximal boundaries.
     */
    @Test
    public void testOptimization() throws Exception {
        final TestFunction f = new TestFunction();
        final GoalType goal = GoalType.MINIMIZE;
        final double min = 0.0;
        final double max = 10.0;
        final PointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        assertEquals(0.8654, result.getPoint(), 0.0001);
        assertEquals(-1.5123, result.getValue(), 0.0001);
    }

    /**
     * Test optimization with minimal boundary only.
     */
    @Test
    public void testOptimizationMinOnly() throws Exception {
        final TestFunction f = new TestFunction();
        final GoalType goal = GoalType.MINIMIZE;
        final double min = 0.0;
        final double max = Double.POSITIVE_INFINITY;
        final PointValuePair result = multiStartOptimizer.optimize(f, goal, min);
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    /**
     * Test optimization with maximal boundary only.
     */
    @Test
    public void testOptimizationMaxOnly() throws Exception {
        final TestFunction f = new TestFunction();
        final GoalType goal = GoalType.MAXIMIZE;
        final double min = Double.NEGATIVE_INFINITY;
        final double max = 0.0;
        final PointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(1.0, result.getValue(), 0.0001);
    }

    /**
     * Test optimization with invalid goal type.
     */
    @Test
    public void testOptimizationInvalidGoalType() {
        assertThrows(IllegalArgumentException.class, () -> multiStartOptimizer.optimize(new TestFunction(), GoalType.SADDLE_POINT, 0.0, 1.0));
    }

    /**
     * Test optimization with invalid bounds.
     */
    @Test
    public void testOptimizationInvalidBounds() {
        assertThrows(IllegalArgumentException.class, () -> multiStartOptimizer.optimize(new TestFunction(), GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, new MaxEval(1)));
    }

    /**
     * Test optimization with optimizer that throws an exception.
     */
    @Test
    public void testOptimizationException() {
        optimizer = new BaseUnivariateOptimizer() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction f, GoalType goal, double start, double end)
                throws FunctionEvaluationException {
                throw new FunctionEvaluationException(LocalizedFormats.NO_FURTHER_IMPROVEMENT_POSSIBLE);
            }
        };
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomDataGenerator());
        final TestFunction f = new TestFunction();
        final GoalType goal = GoalType.MINIMIZE;
        final double min = 0.0;
        final double max = 10.0;
        assertThrows(ConvergenceException.class, () -> multiStartOptimizer.optimize(f, goal, min, max));
    }

    /**
     * Test optimization with optimizer that never converges.
     */
    @Test
    public void testOptimizationNeverConverges() {
        optimizer = new BaseUnivariateRealOptimizer<TestFunction>() {
            @Override
            public PointValuePair optimize(UnivariateRealFunction f, GoalType goal, double start, double end)
                throws FunctionEvaluationException {
                return new PointValuePair(start + 0.1, Double.NaN);
            }
        };
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomDataGenerator());
        final TestFunction f = new TestFunction();
        final GoalType goal = GoalType.MINIMIZE;
        final double min = 0.0;
        final double max = 10.0;
        assertThrows(ConvergenceException.class, () -> multiStartOptimizer.optimize(f, goal, min, max));
    }

    /**
     * Test optimization with maximum number of evaluations reached.
     */
    @Test
    public void testOptimizationMaxEvaluations() {
        optimizer = new FibonacciSearchRealOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new RandomDataGenerator());
        final TestFunction f = new TestFunction();
        final GoalType goal = GoalType.MINIMIZE;
        final double min = 0.0;
        final double max = 10.0;
        final int maxEvaluations = 3;
        final PointValuePair result = multiStartOptimizer.optimize(f, goal, min, max, new MaxEval(maxEvaluations));
        assertNull(result);
    }

    /**
     * Test optimization with invalid number of starts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationInvalidStarts() {
        optimizer = new FibonacciSearchRealOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 0, new RandomDataGenerator());
    }
}