package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MaxEvalException;
import org.apache.commons.math3.optimization.NoSolutionException;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.simpledirect.GoalTypeThreshold;
import org.apache.commons.math3.optimization.simpledirect.Simplex;
import org.apache.commons.math3.optimization.simpledirect.SimplexOptimizer;
import org.apache.commons.math3.optimization.simplevaluechecker.SimpleValueCheckerWithMessage;
import org.junit.Test;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /**
     * Test of the {@link SimplexOptimizer#SimplexOptimizer()} constructor.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertTrue(optimizer.getConvergenceChecker() instanceof SimpleValueChecker);
    }

    /**
     * Test of the {@link SimplexOptimizer#SimplexOptimizer(ConvergenceChecker)}
     * constructor.
     */
    @Test
    public void testCustomConvergenceCheckerConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertTrue(optimizer.getConvergenceChecker() instanceof SimpleValueChecker);

        SimplexOptimizer optimizer2 = new SimplexOptimizer(new SimpleValueCheckerWithMessage());
        assertEquals(GoalType.MINIMIZE, optimizer2.getGoalType());
        assertTrue(optimizer2.getConvergenceChecker() instanceof SimpleValueCheckerWithMessage);
    }

    /**
     * Test of the {@link SimplexOptimizer#SimplexOptimizer(double, double)}
     * constructor.
     */
    @Test
    public void testCustomConvergenceCheckerWithRelAndAbsThresholdConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.01);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertTrue(convergenceChecker instanceof SimpleValueChecker);
        SimpleValueChecker simpleValueChecker = (SimpleValueChecker) convergenceChecker;
        assertEquals(0.001, simpleValueChecker.getRelativePrecision(), 0.0001);
        assertEquals(0.01, simpleValueChecker.getAbsolutePrecision(), 0.001);
    }

    /**
     * Test of the {@link SimplexOptimizer#setSimplex(AbstractSimplex)} method.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    /**
     * Test of the {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}
     * method.
     */
    @Test
    public void testOptimize() throws InterruptedException {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }
        };
        double[] startPoint = new double[] {1.0, 1.0};
        PointValuePair optimum = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        assertEquals(-1.0, optimum.getValue(), 0.001);
        assertArrayEquals(new double[] {-1.1213153485608286, -1.1213153485608286}, optimum.getPoint(), 0.001);
    }

    /**
     * Test of the {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], OptimizationData[])}
     * method.
     */
    @Test
    public void testOptimizeWithCustomData() throws InterruptedException {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }
        };
        double[] startPoint = new double[] {1.0, 1.0};
        AbstractSimplex simplex = new Simplex(3);
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setParameter("simplex", simplex);

        PointValuePair optimum = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint,
                new OptimizationData[] {optimizationData});
        assertEquals(-1.0, optimum.getValue(), 0.001);
        assertArrayEquals(new double[] {-1.1213153485608286, -1.1213153485608286}, optimum.getPoint(), 0.001);
        assertEquals(simplex, optimizationData.getParameter("simplex"));
    }

    /**
     * Test of the {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], OptimizationData[])}
     * method when the optimization fails to converge.
     */
    @Test(expected = MaxEvalException.class)
    public void testOptimizeMaxEvalException() throws InterruptedException {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.log(Math.abs(Math.sin(x[0]) + Math.sin(x[1])));
            }
        };
        double[] startPoint = new double[] {1.0, 1.0};
        PointValuePair optimum = optimizer.optimize(0, function, GoalType.MINIMIZE, startPoint);
    }

    /**
     * Test of the {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], OptimizationData[])}
     * method when the optimization has no solution.
     */
    @Test(expected = NoSolutionException.class)
    public void testOptimizeNoSolutionException() throws InterruptedException {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2) - 5;
            }
        };
        double[] startPoint = new double[] {1.0, 1.0};
        PointValuePair optimum = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
    }
}