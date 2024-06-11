package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.AbstractSimplex;

import java.util.Arrays;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer()}.
     */
    @Test
    public void testEmptyConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
        assertNull(optimizer.getSimplex());
        assertEquals(SimpleValueChecker.DEFAULT_RELATIVE_ACCURACY,
                     optimizer.getConvergenceChecker().getRelativeAccuracy(), 0);
        assertEquals(Double.POSITIVE_INFINITY,
                     optimizer.getConvergenceChecker().getAbsoluteAccuracy(), 0);
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(ConvergenceChecker)}.
     */
    @Test
    public void testCheckerConstructor() {
        SimpleValueChecker checker = new SimpleValueChecker(1.0e-6, 1.0e-8);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
        assertNotNull(optimizer.getSimplex());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(double, double)}.
     */
    @Test
    public void testDoubleConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer(1.0e-6, 1.0e-8);
        assertNotNull(optimizer);
        assertNotNull(optimizer.getSimplex());
        assertEquals(new SimpleValueChecker(1.0e-6, 1.0e-8),
                     optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}.
     */
    @Test
    public void testOptimize1() {
        PolynomialFunction f = new PolynomialFunction(2, 2, 3, -1);

        UnivariateOptimizer optimizer = new SimplexOptimizer();

        OptimizationData optData = new OptimizationData();
        optData.setInitialGuess(new InitialGuess(1.0, 1.0));
        optimizer.parseOptimizationData(new OptimizationData[]{optData});

        PointValuePair result = optimizer.optimize(100, f, SimpleGoalType.MINIMIZE, new double[2]);
        double[] expected = new double[]{1.0, 1.0}; // Minimum is at (1, 1)
        assertArrayEquals(expected, result.getPoint(), 0.001);
        assertEquals(-3.0, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], AbstractSimplex)}.
     */
    @Test
    public void testOptimize2() {
        PolynomialFunction f = new PolynomialFunction(3, 3, 4, 5, -1);

        AbstractSimplex simplex = new SimpleX(3);

        SimplexOptimizer optimizer = new SimplexOptimizer();

        OptimizationData optData = new OptimizationData();
        optData.setInitialGuess(new InitialGuess(1.0, 1.0, 1.0));
        optData.setAbstractSimplex(simplex);
        optimizer.parseOptimizationData(new OptimizationData[]{optData});

        PointValuePair result = optimizer.optimize(100, f, SimpleGoalType.MINIMIZE, new double[3]);
        double[] expected = new double[]{1.0, 1.0, 1.0}; // Minimum is at (1, 1, 1)
        assertArrayEquals(expected, result.getPoint(), 0.001);
        assertEquals(-5.0 / 6.0, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}.
     * Test with a function with multiple local minima.
     */
    @Test
    public void testOptimize3() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.sqrt(Math.abs(x[0] * x[1]))), 2);
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer();

        PointValuePair result = optimizer.optimize(100, f, SimpleGoalType.MINIMIZE, new double[2]);

        double[] expected = new double[]{Math.PI / 2, Math.PI / 2}; // One of the local minima
        assertArrayEquals(expected, result.getPoint(), 0.001);
        assertEquals(0.25, result.getValue(), 0.01);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimize4() {
        PolynomialFunction f = new PolynomialFunction(2, 2, 3, 4, -1);

        OptimizationData optData = new OptimizationData();
        optData.setInitialGuess(new InitialGuess(1.0, 1.0));
        optData.setAbstractSimplex(new SimpleX(2));

        SimplexOptimizer optimizer = new SimplexOptimizer();

        PointValuePair result = optimizer.optimize(100, f, SimpleGoalType.MINIMIZE, new OptimizationData[]{optData});
        double[] expected = new double[]{1.0, 1.0}; // Minimum is at (1, 1)
        assertArrayEquals(expected, result.getPoint(), 0.001);
        assertEquals(-4.0, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[], ConvergenceChecker)}.
     */
    @Test
    public void testOptimize5() {
        PolynomialFunction f = new PolynomialFunction(2, 2, 3, 4, -1);

        SimpleValueChecker checker = new SimpleValueChecker(1.0e-6, 1.0e-8);

        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        PointValuePair result = optimizer.optimize(100, f, SimpleGoalType.MINIMIZE, new double[2]);
        double[] expected = new double[]{1.0, 1.0}; // Minimum is at (1, 1)
        assertArrayEquals(expected, result.getPoint(), 0.001);
        assertEquals(-4.0, result.getValue(), 0.001);
    }
}