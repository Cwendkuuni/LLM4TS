package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximizer;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test multi-start optimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** Function to optimize. */
    private UnivariateRealFunction function;

    /** Optimizer to use. */
    private UnivariateRealOptimizer optimizer;

    /** Multi-start optimizer to use. */
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;

    /** Random generator. */
    private MersenneTwister random;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        function = new MyFunction();
        optimizer = new GoalTypeMinimizer(new GoalTypeMaximizer(new BrentOptimizer()));
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister());
        multiStartOptimizer.setMaxEvaluations(100);
        random = new MersenneTwister();
    }

    /**
     * Test optimize with valid input.
     */
    @Test
    public void testOptimizeValid() {
        PointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(0.5, result.getPoint(), 0.001);
        Assert.assertEquals(-0.25, result.getValue(), 0.001);
    }

    /**
     * Test optimize with edge input.
     */
    @Test
    public void testOptimizeEdge() {
        PointValuePair result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -1.0, 0.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Test optimize with error input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeError() {
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, -1.0, 1.0);
    }

    /**
     * Test optimize with convergence checker.
     */
    @Test
    public void testOptimizeConvergenceChecker() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister());
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new MyConvergenceChecker());
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(0.5, result.getPoint(), 0.001);
        Assert.assertEquals(-0.25, result.getValue(), 0.001);
    }

    /**
     * Test optimize with maximum number of evaluations reached.
     */
    @Test
    public void testOptimizeMaxEvaluations() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister());
        optimizer.setMaxEvaluations(1);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        Assert.assertNull(result);
    }

    /**
     * Test getOptima with valid input.
     */
    @Test
    public void testGetOptimaValid() {
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        PointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(10, optima.length);
        for (PointValuePair pointValuePair : optima) {
            Assert.assertNotNull(pointValuePair);
        }
    }

    /**
     * Test getOptima with no optima.
     */
    @Test
    public void testGetOptimaNoOptima() {
        multiStartOptimizer.setMaxEvaluations(1);
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        PointValuePair[] optima = multiStartOptimizer.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertArrayEquals(new PointValuePair[0], optima);
    }

    /**
     * Function to optimize.
     */
    private static class MyFunction implements UnivariateRealFunction {
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        public double derivative(double x) {
            return 2 * Math.sin(x) * Math.cos(x);
        }
    }

    /**
     * Convergence checker.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        public boolean check(PointValuePair lastValue, PointValuePair currentValue) {
            return lastValue != null && FastMath.abs(lastValue.getValue() - currentValue.getValue()) < 0.001;
        }
    }
}