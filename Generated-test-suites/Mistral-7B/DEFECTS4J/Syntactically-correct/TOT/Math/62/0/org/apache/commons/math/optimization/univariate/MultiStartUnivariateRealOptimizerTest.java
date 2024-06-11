package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.goaltypes.MaxEval;
import org.apache.commons.math3.optim.goaltypes.Minimization;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.UniformRandomGenerator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private static final double EPSILON = 1e-6;
    private static final double RANGE = 10.0;
    private static final int MAX_EVAL = 100;
    private static final int NUM_STARTS = 5;

    private UnivariateRealFunction function;
    private UnivariateOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private RandomDataGenerator random;

    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - Math.cos(x) + 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * Math.sin(x) * Math.cos(x) - 1;
            }
        };
        optimizer = new BrentOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS, new UniformRandomGenerator());
        random = new RandomDataGenerator();
    }

    @Test
    public void testMinimizationWithMultipleStarts() throws Exception {
        GoalType goal = Minimization.MINIMIZE;
        double min = 1.5;
        double max = 3.5;

        multiStartOptimizer.optimize(function, goal, min, max);

        PointValuePair best = multiStartOptimizer.getOptima()[0];
        assertEquals(Minimization.MINIMUM, goal);
        assertTrue(best != null);
        assertLessThan(best.getValue(), EPSILON);
        // Check that other optima are worse
        for (int i = 1; i < multiStartOptimizer.getOptima().length; i++) {
            PointValuePair worse = multiStartOptimizer.getOptima()[i];
            if (worse == null) {
                fail("Unexpected null optimum");
            }
            assertGreaterThan(best.getValue(), worse.getValue());
        }
        // Check that all optima are within bounds
        for (PointValuePair optimum : multiStartOptimizer.getOptima()) {
            assertTrue(optimum != null);
            assertTrue(optimum.getPoint() >= min);
            assertTrue(optimum.getPoint() <= max);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testMinimizationWithMultipleStartsNoConvergence() throws Exception {
        GoalType goal = Minimization.MINIMIZE;
        double min = 1.5;
        double max = 3.5;

        multiStartOptimizer.setMaxEvaluations(MAX_EVAL / NUM_STARTS);
        multiStartOptimizer.optimize(function, goal, min, max);

        fail("Expected ConvergenceException");
    }

    @Test
    public void testMaximizationWithMultipleStarts() throws Exception {
        GoalType goal = MaxEval.MAXIMIZE;
        double min = 1.5;
        double max = 3.5;

        multiStartOptimizer.optimize(function, goal, min, max, 0.5);

        PointValuePair best = multiStartOptimizer.getOptima()[0];
        assertEquals(MaxEval.MAXIMUM, goal);
        assertTrue(best != null);
        assertGreaterThan(best.getValue(), EPSILON);
        // Check that other optima are worse
        for (int i = 1; i < multiStartOptimizer.getOptima().length; i++) {
            PointValuePair worse = multiStartOptimizer.getOptima()[i];
            if (worse == null) {
                fail("Unexpected null optimum");
            }
            assertLessThan(best.getValue(), worse.getValue());
        }
        // Check that all optima are within bounds
        for (PointValuePair optimum : multiStartOptimizer.getOptima()) {
            assertTrue(optimum != null);
            assertTrue(optimum.getPoint() >= min);
            assertTrue(optimum.getPoint() <= max);
        }
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testMaximizationWithMultipleStartsMaxEvalExceeded() throws Exception {
        GoalType goal = MaxEval.MAXIMIZE;
        double min = 1.5;
        double max = 3.5;

        multiStartOptimizer.setMaxEvaluations(MAX_EVAL / (NUM_STARTS - 1));
        multiStartOptimizer.optimize(function, goal, min, max, 0.5);

        fail("Expected MaxEvalExceededException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(null, NUM_STARTS, new UniformRandomGenerator());
        try {
            optimizer.optimize(null, Minimization.MINIMIZE, 1.5, 3.5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("function is null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGoalType() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(function, 0, new UniformRandomGenerator());
        try {
            optimizer.optimize(function, null, 1.5, 3.5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("goalType is null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBounds() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(function, NUM_STARTS, new UniformRandomGenerator());
        try {
            optimizer.optimize(function, Minimization.MINIMIZE, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("min or max is null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeBounds() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(function, NUM_STARTS, new UniformRandomGenerator());
        try {
            optimizer.optimize(function, Minimization.MINIMIZE, -RANGE, RANGE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("bounds cannot be negative", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRandomGenerator() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(function, NUM_STARTS, null);
        try {
            optimizer.optimize(function, Minimization.MINIMIZE, 1.5, 3.5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("randomGenerator is null", e.getMessage());
        }
    }
}