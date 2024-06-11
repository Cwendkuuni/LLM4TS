package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointsValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateRealOptimizer;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction testFunction;
    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private RandomGenerator randomGenerator;

    @Before
    public void setUp() {
        testFunction = new MyTestFunction();
        optimizer = new BrentOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwisterFast());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Positive case: finds a minimum with multiple starts.
     */
    @Test
    public void testOptimizeMinimum() throws FunctionEvaluationException, ConvergenceException {
        double min = 1.0;
        double max = 3.0;
        GoalType goal = GoalType.MINIMIZE;

        PointsValuePair result = multiStartOptimizer.optimize(testFunction, goal, min, max);
        assertEquals(min, result.getPoint(), 1e-12);
        assertEquals(-testFunction.value(min), result.getValue(), 1e-12);

        // Check optima
        PointsValuePair[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointsValuePair[]{new PointsValuePair(min, -testFunction.value(min))}, optima);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Positive case: finds a maximum with multiple starts.
     */
    @Test
    public void testOptimizeMaximum() throws FunctionEvaluationException, ConvergenceException {
        double min = 1.0;
        double max = 3.0;
        GoalType goal = GoalType.MAXIMIZE;

        PointsValuePair result = multiStartOptimizer.optimize(testFunction, goal, min, max);
        assertEquals(max, result.getPoint(), 1e-12);
        assertEquals(testFunction.value(max), result.getValue(), 1e-12);

        // Check optima
        PointsValuePair[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointsValuePair[]{new PointsValuePair(max, testFunction.value(max))}, optima);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Edge case: test with a function that has no roots in the interval.
     */
    @Test
    public void testNoRoots() throws FunctionEvaluationException, ConvergenceException {
        double min = 0.0;
        double max = 1.0;
        GoalType goal = GoalType.MINIMIZE;

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwisterFast());
        try {
            multiStartOptimizer.optimize(new MyTestFunctionNoRoots(), goal, min, max);
            // The test should fail because of ConvergenceException
            fail("Test should fail with ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        // Check optima
        PointsValuePair[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointsValuePair[10], optima);
        for (PointsValuePair p : optima) {
            assertEquals(Double.NaN, p.getPoint(), 0.0);
            assertEquals(Double.NaN, p.getValue(), 0.0);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Error case: test with a null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() throws FunctionEvaluationException, ConvergenceException {
        double min = 1.0;
        double max = 3.0;
        GoalType goal = GoalType.MINIMIZE;

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwisterFast());
        multiStartOptimizer.optimize(null, goal, min, max);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with negative bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeBounds() throws FunctionEvaluationException, ConvergenceException {
        double min = -1.0;
        double max = -2.0;
        GoalType goal = GoalType.MINIMIZE;

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwisterFast());
        multiStartOptimizer.optimize(testFunction, goal, min, max);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with overlapping bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOverlappingBounds() throws FunctionEvaluationException, ConvergenceException {
        double min1 = 0.0;
        double max1 = 1.0;
        double min2 = 1.0;
        double max2 = 2.0;
        GoalType goal = GoalType.MINIMIZE;

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwisterFast());
        multiStartOptimizer.optimize(testFunction, goal, min1, max1, min2, max2);
    }

    /**
     * Test function for testing.
     */
    private static class MyTestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x - 2 * x + 1;
        }
    }

    /**
     * Test function for testing with no roots in the interval [0, 1].
     */
    private static class MyTestFunctionNoRoots implements UnivariateRealFunction {
        public double value(double x) {
            return Math.sqrt(Math.abs(x)) - 1;
        }
    }
}