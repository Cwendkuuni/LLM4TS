package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.random.RandomDataGenerators;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link MultiStartUnivariateRealOptimizer} class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** A simple test function for optimization. */
    static UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 2.0, 2.0);
            }
        };

    /** A test optimizer. */
    static UnivariateRealOptimizer testOptimizer = new FibonacciSearchOptimizer();

    /** A multi-start optimizer. */
    static MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;

    /** Maximum number of evaluations. */
    static int maxEvaluations = 100;

    /** Number of starts. */
    static int starts = 3;

    /** Seed for the random number generator. */
    static long seed = 123456L;

    /** Random number generator. */
    static RandomGenerator random = RandomDataGenerators.createReal(seed);

    /**
     * Sets up the test environment.
     */
    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUp() {
        // Create a multi-start optimizer from a single-start optimizer.
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(testOptimizer, starts, random);

        // Set the maximum number of evaluations.
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
    }

    /**
     * Tests the optimization of a simple function.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testSimpleOptimization() throws Exception {
        // Optimize the function with a single start.
        PointValuePair result1 = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 4.0);
        Assert.assertEquals(2.0, result1.getPoint(), 0.001);
        Assert.assertEquals(0.0, result1.getValue(), 0.001);

        // Optimize the function with multiple starts.
        multiStartOptimizer.setMaxEvaluations(maxEvaluations * starts);
        PointValuePair[] results = new PointValuePair[starts];
        for (int i = 0; i < starts; ++i) {
            results[i] = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 4.0);
        }
        Assert.assertArrayEquals(new PointValuePair[] { new PointValuePair(2.0, 0.0),
                                                        new PointValuePair(3.0, 2.0),
                                                        new PointValuePair(3.5, 3.5) },
                               results, 0.001);
    }

    /**
     * Tests the optimization of a function with bounds.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testOptimizationWithBounds() throws Exception {
        // Define the test function with bounds.
        UnivariateRealFunction testFunctionWithBounds = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x - 2.0, 2.0);
            }

            @Override
            public double value(double x, SimpleBounds bounds)
                throws FunctionEvaluationException {
                return Math.pow(x - bounds.getMin(), 2.0);
            }

            @Override
            public SimpleBounds getDomain() {
                return new SimpleBounds(0.0, 4.0);
            }
        };

        // Optimize the function with a single start.
        PointValuePair result1 = multiStartOptimizer.optimize(testFunctionWithBounds, GoalType.MINIMIZE,
                                                                new SimpleBounds(0.0, 4.0));
        Assert.assertEquals(2.0, result1.getPoint(), 0.001);
        Assert.assertEquals(0.0, result1.getValue(), 0.001);

        // Optimize the function with multiple starts.
        multiStartOptimizer.setMaxEvaluations(maxEvaluations * starts);
        PointValuePair[] results = new PointValuePair[starts];
        for (int i = 0; i < starts; ++i) {
            results[i] = multiStartOptimizer.optimize(testFunctionWithBounds, GoalType.MINIMIZE,
                                                      new SimpleBounds(0.0, 4.0));
        }
        Assert.assertArrayEquals(new PointValuePair[] { new PointValuePair(2.0, 0.0),
                                                        new PointValuePair(3.0, 2.0),
                                                        new PointValuePair(3.5, 3.5) },
                               results, 0.001);
    }

    /**
     * Tests the optimization when the optimizer does not converge.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws Exception {
        // Define a function that does not converge.
        UnivariateRealFunction noConvergenceFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(LocalizedFormats.UNDEFINED, x);
            }
        };

        // Optimize the function with multiple starts.
        multiStartOptimizer.setMaxEvaluations(maxEvaluations * starts);
        multiStartOptimizer.optimize(noConvergenceFunction, GoalType.MINIMIZE, 0.0, 4.0);
    }
}