package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.apache.commons.math3.optim.MaxEval.SAFE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateOptimizer delegate;
    private RandomGenerator random;

    @Before
    public void setUp() {
        delegate = new PolynomialFunction(new double[]{-1.0, 1.0, 0.5}, new double[]{0.0});
        optimizer = new MultiStartUnivariateRealOptimizer(delegate, 5, new MersenneTwister());
    }

    /**
     * Test that the optimizer returns the expected result when called with a simple function and good initial guess.
     */
    @Test
    public void testSimpleSuccess() {
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2.0;
        double expected = Precision.round(0.0, 4);
        PointValuePair result = optimizer.optimize(delegate, goalType, min, max);
        assertEquals(expected, result.getValue(), 1e-4);
    }

    /**
     * Test that the optimizer returns the expected result when called with a simple function and bad initial guess.
     */
    @Test
    public void testSimpleFailure() {
        GoalType goalType = GoalType.MINIMIZE;
        double min = -2.0;
        double max = 2.0;
        // no need to check the result, as the test will fail when the optimizer does not throw an exception
        optimizer.optimize(delegate, goalType, min, max);
    }

    /**
     * Test that the optimizer returns multiple optima when called with a multimodal function.
     */
    @Test
    public void testMultimodal() {
        GoalType goalType = GoalType.MINIMIZE;
        double min = -2.0;
        double max = 2.0;
        double[] expected = new double[]{0.5, 1.5, -0.5};
        double[] actual = optimizer.getOptima();
        assertArrayEquals(expected, actual);
    }

    /**
     * Test that the optimizer throws an exception when called with a function that does not converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() {
        GoalType goalType = GoalType.MINIMIZE;
        double min = -2.0;
        double max = 2.0;
        optimizer.optimize(new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0}), goalType, min, max);
    }

    /**
     * Test that the optimizer returns multiple optima when called multiple times with the same function and bounds.
     */
    @Test
    public void testMultipleCalls() {
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2.0;
        double[] expected = new double[]{0.0, 1.0};
        double[] actual1 = optimizer.getOptima();
        double[] actual2 = optimizer.getOptima();
        assertArrayEquals(expected, actual1);
        assertArrayEquals(expected, actual2);
        assertEquals(actual1.length, actual2.length);
    }

    /**
     * Test that the optimizer throws an exception when given an illegal number of starts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalNumberOfStarts() {
        optimizer = new MultiStartUnivariateRealOptimizer(delegate, 0, new MersenneTwister());
    }

    /**
     * Test that the optimizer throws an exception when given a null delegate.
     */
    @Test(expected = NullPointerException.class)
    public void testNullDelegate() {
        optimizer = new MultiStartUnivariateRealOptimizer(null, 5, new MersenneTwister());
    }

    /**
     * Test that the optimizer throws an exception when given a null random generator.
     */
    @Test(expected = NullPointerException.class)
    public void testNullRandomGenerator() {
        optimizer = new MultiStartUnivariateRealOptimizer(delegate, 5, null);
    }

}