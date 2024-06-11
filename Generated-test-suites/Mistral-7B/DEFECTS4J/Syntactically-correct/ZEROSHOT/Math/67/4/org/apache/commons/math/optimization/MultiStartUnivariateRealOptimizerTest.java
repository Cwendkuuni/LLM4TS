package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.Erf;

import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)}
     * with different tests for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     */
    @org.junit.Test
    public void testMultiStartUnivariateRealOptimizer() {
        // Test with a simple function, F(x) = x^2
        UnivariateRealFunction f = new PolynomialFunction(2.0);
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());

        // Test optimization with minimum goal
        GoalType goalTypeMin = GoalType.MINIMIZE;
        double minX = 5.0;
        double maxX = 10.0;

        double result = multiOptimizer.optimize(f, goalTypeMin, minX, maxX);
        assertEquals(result, 2.5, 0.1);

        double[] optima = multiOptimizer.getOptima();
        double[] optimaValues = multiOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5}, optima, 0.001);
        assertArrayEquals(new double[]{2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5}, optimaValues, 0.001);

        // Test optimization with maximum goal
        GoalType goalTypeMax = GoalType.MAXIMIZE;
        result = multiOptimizer.optimize(f.negate(), goalTypeMax, minX, maxX);
        assertEquals(result, -2.5, 0.1);

        optima = multiOptimizer.getOptima();
        optimaValues = multiOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{-2.5, -2.5, -2.5, -2.5, -2.5, -2.5, -2.5, -2.5, -2.5, -2.5}, optima, 0.001);
        assertArrayEquals(new double[]{-2.5, -2.5, -2.5, -2.5, -2.5, -2.5, -2.5, -2.5, -2.5, -2.5}, optimaValues, 0.001);

        // Test optimization with a function with multiple local minima
        UnivariateRealFunction f2 = new PolynomialFunction(-1.0, 6.0);
        result = multiOptimizer.optimize(f2, goalTypeMin, minX, maxX);
        assertEquals(result, -3.0, 0.1);

        optima = multiOptimizer.getOptima();
        optimaValues = multiOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{-3.0, -3.0, -3.0, -3.0, -3.0, -3.0, -3.0, -3.0, -3.0, -3.0}, optima, 0.001);
        assertArrayEquals(new double[]{-3.0, -3.0, -3.0, -3.0, -3.0, -3.0, -3.0, -3.0, -3.0, -3.0}, optimaValues, 0.001);

        // Test optimization with a function with multiple local maxima
        UnivariateRealFunction f3 = new PolynomialFunction(1.0, 6.0);
        result = multiOptimizer.optimize(f3.negate(), goalTypeMax, minX, maxX);
        assertEquals(result, 3.0, 0.1);

        optima = multiOptimizer.getOptima();
        optimaValues = multiOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0}, optima, 0.001);
        assertArrayEquals(new double[]{3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0}, optimaValues, 0.001);

        // Test optimization with a function with a global minima and a global maxima
        UnivariateRealFunction f4 = new Erf(0.0);
        result = multiOptimizer.optimize(f4, goalTypeMin, minX, maxX);
        assertEquals(result, 0.0, 0.1);

        optima = multiOptimizer.getOptima();
        optimaValues = multiOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, optima, 0.001);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, optimaValues, 0.001);

        result = multiOptimizer.optimize(f4.negate(), goalTypeMax, minX, maxX);
        assertEquals(result, 0.0, 0.1);

        optima = multiOptimizer.getOptima();
        optimaValues = multiOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, optima, 0.001);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, optimaValues, 0.001);

    }
}