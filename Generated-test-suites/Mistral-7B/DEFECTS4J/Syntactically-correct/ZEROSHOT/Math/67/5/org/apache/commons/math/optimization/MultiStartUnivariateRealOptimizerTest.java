package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonConvergenceException;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction function;
    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        function = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0, -1.0});
    }

    @Test
    public void testMultiStartOptimizer() throws Exception {
        // Test multi-start optimization with a given optimizer
        UnivariateOptimizer optimizerWrapper = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                optimizerWrapper, 5, new NistBartlettCauchyRandomGenerator());
        double min = -5.0;
        double max = 5.0;
        double optimum = 1.0;

        double[] optima = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
        assertEquals(5, optima.length);
        assertArrayEquals(new double[]{optimum, optimum, optimum, optimum, optimum}, optima, 0.001);

        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[]{function.value(optimum), function.value(optimum), function.value(optimum),
                function.value(optimum), function.value(optimum)}, optimaValues, 0.001);

        assertEquals(5, multiStartOptimizer.getIterationCount());
        assertTrue(multiStartOptimizer.hasConverged());
        assertFalse(multiStartOptimizer.hasDiverged());

        // Test multi-start optimization with a given optimizer and exception handling
        try {
            multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                    optimizerWrapper, 5, new NistBartlettCauchyRandomGenerator());
            multiStartOptimizer.setMaxIterations(1);
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
            fail("Should have thrown an exception");
        } catch (NonConvergenceException e) {
            // Expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testMultiStartOptimizerNoOptimumFound() throws Exception {
        UnivariateOptimizer optimizerWrapper = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                optimizerWrapper, 5, new NistBartlettCauchyRandomGenerator());
        double min = -5.0;
        double max = 5.0;

        multiStartOptimizer.setMaxIterations(0);
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, min, max);
    }

}

class NistBartlettCauchyRandomGenerator implements RandomGenerator {
    @Override
    public double nextGaussian() {
        double u1 = Math.random();
        double u2 = Math.random();
        double z0 = Math.log(1.0 - u1);
        double z1 = Math.atan2(Math.sqrt(-2.0 * Math.log(u2)) * Math.cos(Math.PI * u1),
                Math.sqrt(-2.0 * Math.log(u2)) * Math.sin(Math.PI * u1));
        double z = z0 + z1 * 1.732; // √2
        return z;
    }
}