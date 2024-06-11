package org.apache.commons.math.optimization.univariate.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Simple polynomial function for testing.
     */
    private static class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x * x - 3 * x * x + 2;
        }

        public double derivative(double x) {
            return 3 * x * x - 6 * x;
        }

        public double getMinimum() {
            return -1.0;
        }

        public double getMaximum() {
            return 2.0;
        }
    }

    /**
     * Test method for MultiStartUnivariateRealOptimizer.
     * @throws MathArithmeticException
     */
    @Test
    public void testMultiStartUnivariateRealOptimizer() throws MathArithmeticException {
        // Test with a simple polynomial function and 10 restarts.
        final int maxEvaluations = 50;
        final int starts = 10;
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -5.0;
        final double max = 5.0;

        final UnivariateRealFunction function = new TestFunction();
        final BaseUnivariateRealOptimizer optimizer = new BrentOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, new MersenneTwister(12345));

        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        multiStartOptimizer.setConvergenceChecker(null);

        UnivariateRealPointValuePair bestOptimum = multiStartOptimizer.optimize(function, goal, min, max);

        Assert.assertNotNull(bestOptimum);
        Assert.assertFalse(Double.isNaN(bestOptimum.getPoint()));
        Assert.assertEquals(0.0, bestOptimum.getValue(), 0.001);

        UnivariateRealPointValuePair[] allOptima = multiStartOptimizer.getOptima();
        final double[] values = new double[allOptima.length];
        for (int i = 0; i < allOptima.length; i++) {
            values[i] = allOptima[i] != null ? allOptima[i].getValue() : Double.NaN;
        }

        final double[] expectedValues = StatUtils.sort(StatUtils.sample(new PolynomialFunction(function.derivative(0.0)), starts));
        Assert.assertArrayEquals(expectedValues, values, 0.001);
    }
}