package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleBoundingMethodOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BroydenFletcherGoldfarbShanno;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PowellDogleg;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SecantOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BFGS;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;

/**
 * Test MultiStartUnivariateRealOptimizer class
 */
public class MultiStartUnivariateRealOptimizerTest {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main(MultiStartUnivariateRealOptimizerTest.class.getName());
    }

    @org.junit.Test
    public void testMultiStartOptimizer() throws Exception {
        final int maxIterations = 200;
        final int maxEvaluations = 1000;
        final int maxStarts = 10;

        final UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0}, 2);

        final UnivariateRealOptimizer optimizer = new FibonacciSearchOptimizer();
        final MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, maxStarts, new MersenneTwister());
        multiStartOptimizer.setMaxIterations(maxIterations);
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);

        final double xopt = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        assertNotNull(xopt);
        assertEquals(Precision.safeEqual(0.0, xopt, 1e-4), true);

        double[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{0.0}, optima, 1e-4);

        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.0}, optimaValues, 1e-4);

        multiStartOptimizer.reset();
        assertThrows(IllegalStateException.class, () -> multiStartOptimizer.getOptima());

        multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 5.0);
        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{}, optimaValues, 1e-4);

        multiStartOptimizer.reset();
        multiStartOptimizer.setMaxIterations(5);
        multiStartOptimizer.setMaxEvaluations(5);
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{0.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.0}, optimaValues, 1e-4);

        multiStartOptimizer.setMaxIterations(5);
        multiStartOptimizer.setMaxEvaluations(5);
        multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 5.0);
        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{5.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{5.0}, optimaValues, 1e-4);

        // Test with other optimizers

        optimizer = new BrentOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, maxStarts, new MersenneTwister());
        multiStartOptimizer.setMaxIterations(maxIterations);
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        xopt = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        assertNotNull(xopt);
        assertEquals(Precision.safeEqual(0.0, xopt, 1e-4), true);

        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{0.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.0}, optimaValues, 1e-4);

        multiStartOptimizer.reset();
        multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 5.0);
        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{5.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{5.0}, optimaValues, 1e-4);

        optimizer = new SimpleBoundingMethodOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, maxStarts, new MersenneTwister());
        multiStartOptimizer.setMaxIterations(maxIterations);
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        xopt = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        assertNotNull(xopt);
        assertEquals(Precision.safeEqual(0.0, xopt, 1e-4), true);

        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{0.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.0}, optimaValues, 1e-4);

        multiStartOptimizer.reset();
        multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 5.0);
        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{5.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{5.0}, optimaValues, 1e-4);

        optimizer = new PowellDogleg();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, maxStarts, new MersenneTwister());
        multiStartOptimizer.setMaxIterations(maxIterations);
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        xopt = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        assertNotNull(xopt);
        assertEquals(Precision.safeEqual(0.0, xopt, 1e-4), true);

        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{0.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.0}, optimaValues, 1e-4);

        multiStartOptimizer.reset();
        multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 5.0);
        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{5.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{5.0}, optimaValues, 1e-4);

        optimizer = new SecantOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, maxStarts, new MersenneTwister());
        multiStartOptimizer.setMaxIterations(maxIterations);
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        xopt = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        assertNotNull(xopt);
        assertEquals(Precision.safeEqual(0.0, xopt, 1e-4), true);

        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{0.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.0}, optimaValues, 1e-4);

        multiStartOptimizer.reset();
        multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 5.0);
        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{5.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{5.0}, optimaValues, 1e-4);

        optimizer = new BFGS();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, maxStarts, new MersenneTwister());
        multiStartOptimizer.setMaxIterations(maxIterations);
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        xopt = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        assertNotNull(xopt);
        assertEquals(Precision.safeEqual(0.0, xopt, 1e-4), true);

        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{0.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.0}, optimaValues, 1e-4);

        multiStartOptimizer.reset();
        multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, 0.0, 5.0);
        optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[]{5.0}, optima, 1e-4);

        optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{5.0}, optimaValues, 1e-4);

    }
}