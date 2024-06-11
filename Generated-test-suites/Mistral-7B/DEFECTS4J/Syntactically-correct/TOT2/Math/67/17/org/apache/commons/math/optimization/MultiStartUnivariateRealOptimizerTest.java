package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar. PeteAndMarshallFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test for the MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test that the MultiStartUnivariateRealOptimizer correctly finds the minimum of a function with a single local minimum.
     */
    @Test
    public void testMinimumSingleLocal() {
        UnivariateRealFunction f = new PeteAndMarshallFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        int starts = 3;
        int maxIterations = 1000;
        int maxEvaluations = 1000;

        RandomGenerator generator = new MersenneTwister();
        UnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, starts, generator);

        msOptimizer.setMaxIterations(maxIterations);
        msOptimizer.setMaxEvaluations(maxEvaluations);

        double[] expectedOptima = {3.0, 5.5, 7.0};
        double[] expectedOptimaValues = {1.7121223728554694E-11, 3.282951656773293E-11, 7.61983538453246E-11};

        double optimum = msOptimizer.optimize(f, goalType, min, max);

        double[] optima = msOptimizer.getOptima();
        double[] optimaValues = msOptimizer.getOptimaValues();

        assertEquals(goalType, msOptimizer.getGoalType());
        assertEquals(maxIterations, msOptimizer.getMaxIterations(), 0.0);
        assertEquals(maxEvaluations, msOptimizer.getMaxEvaluations(), 0.0);

        assertEquals(optima.length, starts);
        assertArrayEquals(expectedOptima, optima);
        assertArrayEquals(expectedOptimaValues, optimaValues);
        assertEquals(optimum, expectedOptima[0]);
    }

    /**
     * Test that the MultiStartUnivariateRealOptimizer correctly handles an exception during optimization.
     */
    @Test(expected = ConvergenceException.class)
    public void testConvergenceExceptionDuringOptimization() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0.0) {
                    throw new ConvergenceException("Test ConvergenceException");
                }
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        int starts = 3;
        int maxIterations = 100;
        int maxEvaluations = 100;

        RandomGenerator generator = new MersenneTwister();
        UnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, starts, generator);

        msOptimizer.setMaxIterations(maxIterations);
        msOptimizer.setMaxEvaluations(maxEvaluations);

        msOptimizer.optimize(f, goalType, min, max);
    }

    /**
     * Test that the MultiStartUnivariateRealOptimizer correctly handles an exception during optimization if the optimizer
     * cannot converge.
     */
    @Test(expected = OptimizationException.class)
    public void testOptimizationExceptionDuringOptimization() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / (x * x)) / (x * x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        int starts = 3;
        int maxIterations = 100;
        int maxEvaluations = 100;

        RandomGenerator generator = new MersenneTwister();
        UnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, starts, generator);

        msOptimizer.setMaxIterations(maxIterations);
        msOptimizer.setMaxEvaluations(maxEvaluations);

        msOptimizer.optimize(f, goalType, min, max);
    }

    /**
     * Test that the MultiStartUnivariateRealOptimizer correctly handles an exception during optimization if the optimizer
     * throws an exception even when it has converged.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationExceptionDuringOptimization() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0.0) {
                    throw new FunctionEvaluationException("Test FunctionEvaluationException");
                }
                return Math.pow(x, 2.0);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        int starts = 3;
        int maxIterations = 100;
        int maxEvaluations = 100;

        RandomGenerator generator = new MersenneTwister();
        UnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, starts, generator);

        msOptimizer.setMaxIterations(maxIterations);
        msOptimizer.setMaxEvaluations(maxEvaluations);

        msOptimizer.optimize(f, goalType, min, max);
    }

    /**
     * Test that the MultiStartUnivariateRealOptimizer correctly handles an exception during initialization.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionDuringInitialization() {
        UnivariateRealFunction f = new PeteAndMarshallFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        int starts = 0;
        int maxIterations = 100;
        int maxEvaluations = 100;

        RandomGenerator generator = new MersenneTwister();
        UnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, starts, generator);

        msOptimizer.setMaxIterations(maxIterations);
        msOptimizer.setMaxEvaluations(maxEvaluations);
    }

    /**
     * Test that the MultiStartUnivariateRealOptimizer correctly handles an exception during initialization if the optimizer
     * is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionDuringInitializationNullOptimizer() {
        UnivariateRealFunction f = new PeteAndMarshallFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        int starts = 3;
        int maxIterations = 100;
        int maxEvaluations = 100;

        RandomGenerator generator = new MersenneTwister();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(null, starts, generator);

        msOptimizer.setMaxIterations(maxIterations);
        msOptimizer.setMaxEvaluations(maxEvaluations);
    }

    /**
     * Test that the MultiStartUnivariateRealOptimizer correctly handles an exception during initialization if the generator
     * is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionDuringInitializationNullGenerator() {
        UnivariateRealFunction f = new PeteAndMarshallFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        int starts = 3;
        int maxIterations = 100;
        int maxEvaluations = 100;

        UnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, starts, null);

        msOptimizer.setMaxIterations(maxIterations);
        msOptimizer.setMaxEvaluations(maxEvaluations);
    }

    /**
     * Test that the MultiStartUnivariateRealOptimizer correctly handles an exception during initialization if the number of starts
     * is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionDuringInitializationNegativeStarts() {
        UnivariateRealFunction f = new PeteAndMarshallFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        int starts = -1;
        int maxIterations = 100;
        int maxEvaluations = 100;

        RandomGenerator generator = new MersenneTwister();
        UnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, starts, generator);

        msOptimizer.setMaxIterations(maxIterations);
        msOptimizer.setMaxEvaluations(maxEvaluations);
    }

    /**
     * Test that the MultiStartUnivariateRealOptimizer correctly returns the optima and their values.
     */
    @Test
    public void testGetOptimaAndOptimaValues() {
        UnivariateRealFunction f = new PeteAndMarshallFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        int starts = 3;
        int maxIterations = 100;
        int maxEvaluations = 100;

        RandomGenerator generator = new MersenneTwister();
        UnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer msOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, starts, generator);

        msOptimizer.setMaxIterations(maxIterations);
        msOptimizer.setMaxEvaluations(maxEvaluations);

        double[] expectedOptima = {3.0, 5.5, 7.0};
        double[] expectedOptimaValues = {1.7121223728554694E-11, 3.282951656773293E-11, 7.61983538453246E-11};

        try {
            double optimum = msOptimizer.optimize(f, goalType, min, max);
            double[] optima = msOptimizer.getOptima();
            double[] optimaValues = msOptimizer.getOptimaValues();

            assertEquals(goalType, msOptimizer.getGoalType());
            assertEquals(maxIterations, msOptimizer.getMaxIterations(), 0.0);
            assertEquals(maxEvaluations, msOptimizer.getMaxEvaluations(), 0.0);

            assertEquals(optima.length, starts);
            assertArrayEquals(expectedOptima, optima);
            assertArrayEquals(expectedOptimaValues, optimaValues);
            assertEquals(optimum, expectedOptima[0]);
        } catch (ConvergenceException e) {
            throw new RuntimeException(e);
        }
    }
}