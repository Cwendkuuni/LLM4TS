package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PreciseOptimizer;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.ScalarFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.random.RandomValuesGenerator;
import org.apache.commons.math3.stat.statistics.DescriptiveStatistics;
import org.junit.Test;

import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;

/**
 * Test MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization of a simple function with a given number of starts.
     * @throws Exception if test fails
     */
    @Test
    public void testMultiStartOptimization() throws Exception {
        UnivariateRealFunction f = new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 2.0);
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PreciseOptimizer(f), 10, new RandomValuesGenerator());

        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        double optimum = optimizer.optimize(f, goalType, min, max);
        double expectedOptimum = 1.0;
        assertEquals(expectedOptimum, optimum, 1e-6);

        double[] optima = optimizer.getOptima();
        DescriptiveStatistics descStats = new DescriptiveStatistics(optima);
        double mean = descStats.getMean();
        double stdDev = descStats.getStandardDeviation();
        double[] expectedOptima = new double[10];
        for (int i = 0; i < 10; ++i) {
            expectedOptima[i] = expectedOptimum + Math.random() * stdDev;
        }
        assertArrayEquals(expectedOptima, optima, 1e-6);

        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[] { Math.pow(expectedOptimum, 2.0) }, optimaValues, 1e-6);

    }

    /**
     * Test optimization of a simple function with a given number of starts and
     * a specific start value.
     * @throws Exception if test fails
     */
    @Test
    public void testMultiStartOptimizationWithSpecificStartValue() throws Exception {
        UnivariateRealFunction f = new Polynomials.Quartic(1.0, -1.0, 0.0, -1.0, 1.0);

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PreciseOptimizer(f), 10, new RandomValuesGenerator());

        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 5.0;

        double optimum = optimizer.optimize(f, goalType, min, max, startValue);
        double expectedOptimum = -0.6666666666666667;
        assertEquals(expectedOptimum, optimum, 1e-6);

        double[] optima = optimizer.getOptima();
        DescriptiveStatistics descStats = new DescriptiveStatistics(optima);
        double mean = descStats.getMean();
        double stdDev = descStats.getStandardDeviation();
        double[] expectedOptima = new double[10];
        for (int i = 0; i < 10; ++i) {
            expectedOptima[i] = expectedOptimum + Math.random() * stdDev;
        }
        assertArrayEquals(expectedOptima, optima, 1e-6);

        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[] { Math.pow(expectedOptimum, 2.0) }, optimaValues, 1e-6);

    }

    /**
     * Test optimization with a given number of starts but no convergence.
     * @throws Exception if test fails
     */
    @Test(expected = ConvergenceException.class)
    public void testMultiStartNoConvergence() throws Exception {
        UnivariateRealFunction f = new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PreciseOptimizer(f), 10, new RandomValuesGenerator());

        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        optimizer.optimize(f, goalType, min, max);

    }

    /**
     * Test optimization with a given number of starts but an illegal number
     * of starts equal or less than 1.
     * @throws Exception if test fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiStartIllegalNumberOfStarts() throws Exception {
        UnivariateRealFunction f = new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PreciseOptimizer(f), 0, new RandomValuesGenerator());

    }

}