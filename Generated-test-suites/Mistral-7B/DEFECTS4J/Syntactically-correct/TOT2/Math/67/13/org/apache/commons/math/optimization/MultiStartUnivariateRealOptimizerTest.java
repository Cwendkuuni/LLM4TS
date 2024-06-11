package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleBoundingMethodOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.leastsq.LevenbergMarquardtOptimizer;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.apache.commons.math.random.MersenneTwister;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction f;
    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;
    private RandomGenerator generator;

    @Before
    public void setup() {
        f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0, -1.0});
        optimizer = new SimpleBoundingMethodOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MersenneTwister());
        generator = new MersenneTwister();
    }

    /**
     * Test optimization with no iterations and no evaluations.
     */
    @Test
    public void testZeroIterationsAndZeroEvaluations() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 0, generator);
        assertFalse("Multi-start optimizer should not be initialized", multiStartOptimizer instanceof MultiStartUnivariateRealOptimizer);
    }

    /**
     * Test optimization with one iteration and one evaluation.
     */
    @Test
    public void testOneIterationAndOneEvaluation() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 1, generator);
        double x = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
        double[] optima = multiStartOptimizer.getOptima();
        assertEquals("Optima array should have one element", 1, optima.length);
        assertEquals("Optima should be the same as single start optimizer", x, optima[0], 1e-12);
    }

    /**
     * Test optimization with multiple iterations and evaluations.
     */
    @Test
    public void testMultipleIterationsAndEvaluations() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 2, generator);
        double[] x = new double[2];
        double[] y = new double[2];
        double[] optima = new double[2];
        double[] optimaValues = new double[2];
        for (int i = 0; i < 2; i++) {
            x[i] = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
            y[i] = optimizer.getFunctionValue();
            multiStartOptimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
            optima[i] = multiStartOptimizer.getOptima()[i];
            optimaValues[i] = multiStartOptimizer.getOptimaValues()[i];
        }

        PointValuePair pvp = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
        double bestX = pvp.getPoint();

        assertArrayEquals("Optima array should have two elements", 2, optima.length);
        assertEquals("Optima should be the same as single start optimizer for first run", x[0], optima[0], 1e-12);
        assertEquals("Optima should be different for second run", x[1], optima[1], 1e-12);
        assertArrayEquals("OptimaValues array should have two elements", 2, optimaValues.length);
        assertEquals("OptimaValues should be the same as single start optimizer for first run", y[0], optimaValues[0], 1e-12);
        assertEquals("OptimaValues should be different for second run", y[1], optimaValues[1], 1e-12);
        assertEquals("Best X should be between the optima", bestX, ((optima[0] + optima[1]) / 2.0), 1e-12);
    }

    /**
     * Test exception handling for optimization with negative number of iterations.
     */
    @Test
    public void testNegativeIterations() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, -1, generator);
        assertThrows("Multi-start optimizer should throw exception for negative number of iterations",
                IllegalArgumentException.class, () -> multiStartOptimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0));
    }

    /**
     * Test exception handling for optimization with zero number of evaluations.
     */
    @Test
    public void testZeroEvaluations() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 1, generator);
        multiStartOptimizer.setMaxEvaluations(0);
        assertThrows("Multi-start optimizer should throw exception for zero number of evaluations",
                IllegalArgumentException.class, () -> multiStartOptimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0));
    }

    /**
     * Test optimization with Levenberg-Marquardt optimizer.
     */
    @Test
    public void testLevenbergMarquardtOptimizer() {
        optimizer = new LevenbergMarquardtOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, generator);
        double[] x = new double[3];
        double[] y = new double[3];
        double[] optima = new double[3];
        double[] optimaValues = new double[3];
        for (int i = 0; i < 3; i++) {
            x[i] = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
            y[i] = optimizer.getFunctionValue();
            multiStartOptimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
            optima[i] = multiStartOptimizer.getOptima()[i];
            optimaValues[i] = multiStartOptimizer.getOptimaValues()[i];
        }

        assertArrayEquals("Optima array should have three elements", 3, optima.length);
        assertEquals("Optima should be different", x[0], optima[1], 1e-12);
        assertEquals("Optima should be different", x[0], optima[2], 1e-12);
        assertEquals("Optima should be different", x[1], optima[2], 1e-12);
        assertArrayEquals("OptimaValues array should have three elements", 3, optimaValues.length);
        assertEquals("OptimaValues should be different", y[0], optimaValues[1], 1e-12);
        assertEquals("OptimaValues should be different", y[0], optimaValues[2], 1e-12);
        assertEquals("OptimaValues should be different", y[1], optimaValues[2], 1e-12);
        assertFalse("Optima array should not contain NaN elements", Double.isNaN(optima[0]));
        assertFalse("Optima array should not contain NaN elements", Double.isNaN(optima[1]));
        assertFalse("Optima array should not contain NaN elements", Double.isNaN(optima[2]));
    }

    /**
     * Test optimization with MaxEval optimizer.
     */
    @Test
    public void testMaxEvalOptimizer() {
        optimizer = new MaxEval();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, generator);
        multiStartOptimizer.setMaxEvaluations(Math.pow(2, 31) - 1);
        assertThrows("Multi-start optimizer should throw exception for too many evaluations",
                IllegalStateException.class, () -> multiStartOptimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0));
    }
}