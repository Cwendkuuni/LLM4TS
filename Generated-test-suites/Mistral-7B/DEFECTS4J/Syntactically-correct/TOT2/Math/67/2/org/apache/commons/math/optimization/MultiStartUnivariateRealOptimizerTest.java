package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.StatFunctions;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)}.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerConstruction() {
        UnivariateRealFunction f = new PolynomialFunction(2, 1);
        RandomGenerator rg = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchUnivariateRealOptimizer(GoalType.MINIMIZE), 10, rg);
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimize() {
        UnivariateRealFunction f = new PolynomialFunction(2, 1);
        RandomGenerator rg = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchUnivariateRealOptimizer(GoalType.MINIMIZE), 10, rg);
        double min = 0.0;
        double max = 10.0;

        PointValuePair pvp = optimizer.optimize(f, GoalType.MINIMIZE, min, max);
        double result = pvp.getPoint();
        double expected = StatFunctions.GAMMA_DISTRIBUTION_INVERSE(0.95, 2);
        assertEquals(expected, Precision.round(result, 5));
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(GoalType.MINIMIZE, pvp.getGoalType());
        assertEquals(min, pvp.getMin(), 1e-6);
        assertEquals(max, pvp.getMax(), 1e-6);
        assertEquals(f.value(result), pvp.getValue(), 1e-6);

        double[] optima = optimizer.getOptima();
        double[] expectedOptima = new double[optimizer.getStarts()];
        for (int i = 0; i < optimizer.getStarts(); i++) {
            expectedOptima[i] = StatFunctions.GAMMA_DISTRIBUTION_INVERSE(
                    0.95 + i * 0.01, 2);
        }
        assertArrayEquals(expectedOptima, optima, 1e-6);

        double[] optimaValues = optimizer.getOptimaValues();
        double[] expectedOptimaValues = new double[optimizer.getStarts()];
        for (int i = 0; i < optimizer.getStarts(); i++) {
            expectedOptimaValues[i] = f.value(expectedOptima[i]);
        }
        assertArrayEquals(expectedOptimaValues, optimaValues, 1e-6);

        int totalIterations = optimizer.getIterationCount();
        int maxIterations = optimizer.getMaxIterations();
        int expectedTotalIterations = optimizer.getStarts() * maxIterations;
        assertEquals(expectedTotalIterations, totalIterations);

        int totalEvaluations = optimizer.getEvaluations();
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expectedTotalEvaluations = optimizer.getStarts() * maxEvaluations;
        assertEquals(expectedTotalEvaluations, totalEvaluations);

    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealFunction f = new PolynomialFunction(2, 1);
        RandomGenerator rg = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchUnivariateRealOptimizer(GoalType.MINIMIZE), 10, rg);
        double startValue = 5.0;
        double min = 0.0;
        double max = 10.0;

        PointValuePair pvp = optimizer.optimize(f, GoalType.MINIMIZE, min, max, startValue);
        double result = pvp.getPoint();
        double expected = StatFunctions.GAMMA_DISTRIBUTION_INVERSE(0.95, 2);
        assertEquals(expected, Precision.round(result, 5));
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(GoalType.MINIMIZE, pvp.getGoalType());
        assertEquals(min, pvp.getMin(), 1e-6);
        assertEquals(max, pvp.getMax(), 1e-6);
        assertEquals(f.value(result), pvp.getValue(), 1e-6);

    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @Test
    public void testGetOptima() throws IllegalStateException {
        UnivariateRealFunction f = new PolynomialFunction(2, 1);
        RandomGenerator rg = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchUnivariateRealOptimizer(GoalType.MINIMIZE), 10, rg);
        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 10.0);
        double[] optima = optimizer.getOptima();
        double[] expectedOptima = new double[optimizer.getStarts()];
        for (int i = 0; i < optimizer.getStarts(); i++) {
            expectedOptima[i] = StatFunctions.GAMMA_DISTRIBUTION_INVERSE(
                    0.95 + i * 0.01, 2);
        }
        assertArrayEquals(expectedOptima, optima, 1e-6);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptimaValues()}.
     */
    @Test
    public void testGetOptimaValues() throws IllegalStateException {
        UnivariateRealFunction f = new PolynomialFunction(2, 1);
        RandomGenerator rg = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchUnivariateRealOptimizer(GoalType.MINIMIZE), 10, rg);
        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 10.0);
        double[] optimaValues = optimizer.getOptimaValues();
        double[] expectedOptimaValues = new double[optimizer.getStarts()];
        for (int i = 0; i < optimizer.getStarts(); i++) {
            expectedOptimaValues[i] = f.value(expectedOptima[i]);
        }
        assertArrayEquals(expectedOptimaValues, optimaValues, 1e-6);

    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test with a non-converged case.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergedCase() {
        UnivariateRealFunction f = new PolynomialFunction(2, 1);
        RandomGenerator rg = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchUnivariateRealOptimizer(GoalType.MAXIMIZE), 2, rg);
        double min = 0.0;
        double max = 10.0;
        optimizer.optimize(f, GoalType.MAXIMIZE, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction f = new PolynomialFunction(2, 1) {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Test");
            }
        };
        RandomGenerator rg = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchUnivariateRealOptimizer(GoalType.MINIMIZE), 10, rg);
        double min = 0.0;
        double max = 10.0;
        optimizer.optimize(f, GoalType.MINIMIZE, min, max);
    }

}