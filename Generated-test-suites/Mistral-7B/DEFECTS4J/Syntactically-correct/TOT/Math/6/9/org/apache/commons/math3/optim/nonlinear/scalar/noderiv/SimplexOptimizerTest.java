package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.SimplexOptimizer;
import org.apache.commons.math3.optimization.SimpleSimplex;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Test optimization of a simple function with minimum at origin.
     */
    @Test
    public void testSimplexOptimization() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setMaxIterations(100);

        double[] startPoint = { 2.0, 3.0 };
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, startPoint,
                                                   new SimpleSimplex(3));

        Assert.assertEquals(0.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] { 0.0, 0.0 }, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization of a function with multiple local minima.
     */
    @Test
    public void testMultipleLocalMinima() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] * x[0] + x[1] * x[1] - 1, 2);
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setMaxIterations(100);

        double[] startPoint = { 2.0, 3.0 };
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, startPoint,
                                                   new SimpleSimplex(3));

        Assert.assertEquals(0.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] { 0.0, 0.0 }, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization failure due to null objective function.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullObjectiveFunction() {
        MultivariateFunction f = null;
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.optimize(f, GoalType.MINIMIZE, new double[] { 0.0, 0.0 },
                           new SimpleSimplex(3));
    }
}