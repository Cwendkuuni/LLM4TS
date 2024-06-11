package org.apache.commons.math.optimization;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.multiStart.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.random.MersenneTwisterFast;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealOptimizer baseOptimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;
    private RandomGenerator randomGenerator;

    @Before
    public void setUp() {
        baseOptimizer = new NewtonRaphsonMethod();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(baseOptimizer, 10, new MersenneTwisterFast());
        randomGenerator = multiStartOptimizer.getGenerator();
    }

    /**
     * Test that the optimizer finds the global minimum when called with a {@link PolynomialFunction}.
     */
    @Test
    public void testOptimizePolynomialFunction() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, false);
        double min = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(-1.0, min, 1e-12);
    }

    /**
     * Test that the optimizer finds the global maximum when called with a {@link PolynomialFunction}.
     */
    @Test
    public void testOptimizePolynomialFunctionMaximize() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, false);
        double max = multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, -10.0, 10.0);
        assertEquals(1.0, max, 1e-12);
    }

    /**
     * Test that the optimizer finds the local minimum when called with a {@link PolynomialFunction} (edge case).
     */
    @Test
    public void testOptimizePolynomialFunctionEdgeCase() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0}, false);
        double min = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(0.0, min, 1e-12);
    }

    /**
     * Test that the optimizer handles the {@link FunctionEvaluationException} when called with a {@link PolynomialFunction} (error case).
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizePolynomialFunctionErrorCase() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, Double.NaN}, false);
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test that the optimizer returns the optima when called with a {@link PolynomialFunction} (edge case).
     */
    @Test
    public void testGetOptimaPolynomialFunction() throws IllegalStateException {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, false);
        double[] optima = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        assertArrayEquals(new double[]{ -1.0, -1.0, -1.0, // results from the runs that did converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN}, // run that did not converge
                optima, 0.0);
    }

    /**
     * Test that the optimizer returns the optima values when called with a {@link PolynomialFunction} (edge case).
     */
    @Test
    public void testGetOptimaValuesPolynomialFunction() throws IllegalStateException {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, false);
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{ -1.0, -1.0, -1.0, // results from the runs that did converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN, // run that did not converge
                                         Double.NaN}, // run that did not converge
                optimaValues, 0.0);
    }

    /**
     * Test that the optimizer returns the correct number of iterations when called with a {@link PolynomialFunction}.
     */
    @Test
    public void testGetIterationCountPolynomialFunction() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, false);
        int iterations = multiStartOptimizer.getIterationCount();
        assertEquals(10 * 11, iterations);
    }

    /**
     * Test that the optimizer returns the correct number of evaluations when called with a {@link PolynomialFunction}.
     */
    @Test
    public void testGetEvaluationsPolynomialFunction() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, false);
        int evaluations = multiStartOptimizer.getEvaluations();
        assertEquals(10 * 11, evaluations);
    }
}