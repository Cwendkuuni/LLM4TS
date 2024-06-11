package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBoundingMethod;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeComparator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSorter;
import org.apache.commons.math3.random.MersenneTwister;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.RealPointValuePair;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {
    /** Test function. */
    private static final UnivariateRealFunction TEST_FUNCTION = new PolynomialFunction(3, 1);

    /** Test data. */
    private static final double MIN_BOUND = -10.0;
    private static final double MAX_BOUND = 10.0;

    /** Test goal type. */
    private static final GoalType GOAL_TYPE = GoalType.MINIMIZE;

    /** Test number of starts. */
    private static final int TEST_STARTS = 5;

    /** Test maximum number of evaluations. */
    private static final int TEST_MAX_EVALUATIONS = 100;

    /** Test random generator. */
    private static final Random TEST_RANDOM = new MersenneTwister();

    /** Test optimizer. */
    private UnivariateRealOptimizer optimizer;

    /** Test optimizer with multiple starts. */
    private MultiStartUnivariateRealOptimizer optimizerMS;

    /** Test constructor. */
    @org.junit.Before
    public void setUp() {
        optimizer = new SimpleBoundingMethod(new InitialGuess(2.5), new MaxEval(TEST_MAX_EVALUATIONS));
        optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, TEST_STARTS, TEST_RANDOM);
    }

    /** Test basic functionality. */
    @org.junit.Test
    public void testBasicFunctionality() throws Exception {
        PointValuePair result = optimizerMS.optimize(TEST_FUNCTION, GOAL_TYPE, MIN_BOUND, MAX_BOUND);
        assertTrue(result != null);
        PointValuePair expectedResult = optimizer.optimize(TEST_FUNCTION, GOAL_TYPE, MIN_BOUND, MAX_BOUND);
        assertArrayEquals(expectedResult.getPoint(), result.getPoint(), 0.001);
        assertArrayEquals(expectedResult.getValue(), result.getValue(), 0.001);
    }

    /** Test edge cases and error scenarios. */
    @org.junit.Test
    public void testEdgeCasesAndErrors() throws Exception {
        // Test with a function that has no roots.
        UnivariateRealFunction functionNoRoots = new PolynomialFunction(1, 1);
        optimizerMS.optimize(functionNoRoots, GOAL_TYPE, MIN_BOUND, MAX_BOUND);
        assertNull(optimizerMS.getOptima()[0]);

        // Test with a function that has multiple roots.
        UnivariateRealFunction functionMultipleRoots = new PolynomialFunction(3, 1);
        PointValuePair[] expectedOptima = new PointValuePair[] {
            new PointValuePair(2.0, 4.0),
            new PointValuePair(-1.0, 4.0),
            new PointValuePair(0.0, 0.0),
            new PointValuePair(1.0, 4.0)
        };
        PointValuePair[] actualOptima = optimizerMS.optimize(functionMultipleRoots, GOAL_TYPE, MIN_BOUND, MAX_BOUND);
        assertArrayEquals(expectedOptima, actualOptima);

        // Test with a function that does not converge.
        UnivariateRealFunction functionDivergent = new PolynomialFunction(1, 1) {
            @Override
            public double value(double x) {
                throw new RuntimeException("Divergent function");
            }
        };
        assertNull(optimizerMS.getOptima()[0]);

        // Test with a function that has an infinite minimum.
        UnivariateRealFunction functionInfiniteMin = new PolynomialFunction(1, -1) {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        assertNull(optimizerMS.getOptima()[0]);

        // Test with a function that has an infinite maximum.
        UnivariateRealFunction functionInfiniteMax = new PolynomialFunction(1, 1) {
            @Override
            public double value(double x) {
                return x;
            }
        };
        assertNull(optimizerMS.getOptima()[0]);

        // Test with a function that is constantly increasing.
        UnivariateRealFunction functionConstantlyIncreasing = new PolynomialFunction(1, 1) {
            @Override
            public double value(double x) {
                return x + 1.0;
            }
        };
        assertNull(optimizerMS.getOptima()[0]);

        // Test with a function that is constantly decreasing.
        UnivariateRealFunction functionConstantlyDecreasing = new PolynomialFunction(1, 1) {
            @Override
            public double value(double x) {
                return x - 1.0;
            }
        };
        assertNull(optimizerMS.getOptima()[0]);
    }

    /** Test sorting. */
    @org.junit.Test
    public void testSorting() throws Exception {
        UnivariateRealFunction functionMultipleRoots = new PolynomialFunction(3, 1);
        optimizerMS.optimize(functionMultipleRoots, GOAL_TYPE, MIN_BOUND, MAX_BOUND);
        PointValuePair[] optima = optimizerMS.getOptima();
        assertArrayEquals(new PointValuePair[] {
            new PointValuePair(2.0, 4.0),
            new PointValuePair(-1.0, 4.0),
            new PointValuePair(1.0, 4.0),
            new PointValuePair(null, null)
        }, optima, new GoalTypeSorter(new GoalTypeComparator(GOAL_TYPE)));
    }

    /** Test maximum number of evaluations. */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testMaxNumberOfEvaluations() throws Exception {
        optimizerMS.setMaxEvaluations(0);
        optimizerMS.optimize(TEST_FUNCTION, GOAL_TYPE, MIN_BOUND, MAX_BOUND);
    }
}