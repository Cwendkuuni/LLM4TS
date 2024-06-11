package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.SimplexOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.junit.Test;

/**
 * Test for {@link SimplexOptimizer} class
 */
public class SimplexOptimizerTest {

    /**
     * Test for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}
     * with a valid objective function and initial simplex.
     */
    @Test
    public void testOptimizeValidObjectiveFunctionAndSimplex() {
        // Test objective function
        MultivariateFunction objectiveFunction = new PolynomialFunction(2, 1.0);

        // Test initial simplex
        AbstractSimplex simplex = new DefaultSimplex(new double[][] {
                {0.0, 0.0}, {1.0, 0.0}, {0.5, 0.5}
        });

        // Test optimization
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        PointValuePair result = optimizer.optimize(50, objectiveFunction, GoalType.MINIMIZE,
                new OptimizationData[] { new InitialGuess(2, new double[] { 0.5, 0.5 }), simplex });

        // Test optimization result
        assertEquals(0.25, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { 1.0 / Math.sqrt(2), 1.0 / Math.sqrt(2) }, result.getPoint(), 1e-6);
    }

    /**
     * Test for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}
     * with a null objective function.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullObjectiveFunction() {
        // Test optimization with null objective function
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.optimize(50, null, GoalType.MINIMIZE, new OptimizationData[0]);
    }

    /**
     * Test for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}
     * with a null optimization data array.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullOptimizationData() {
        // Test optimization with null optimization data array
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.optimize(50, new PolynomialFunction(2, 1.0), GoalType.MINIMIZE, null);
    }

    /**
     * Test for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}
     * with a null initial simplex in optimization data.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullInitialSimplex() {
        // Test optimization with a null initial simplex in optimization data
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.optimize(50, new PolynomialFunction(2, 1.0), GoalType.MINIMIZE,
                new OptimizationData[] { new InitialGuess(2, new double[] { 0.5, 0.5 }) });
    }

    /**
     * Test for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}
     * with an empty initial simplex.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeEmptyInitialSimplex() {
        // Test optimization with an empty initial simplex
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.optimize(50, new PolynomialFunction(2, 1.0), GoalType.MINIMIZE,
                new OptimizationData[] { new InitialGuess(2, new double[] { 0.5, 0.5 }), new AbstractSimplex() {
                    @Override
                    public int getSize() {
                        return 0;
                    }

                    @Override
                    public double[] getPoint(int index) {
                        throw new ArrayIndexOutOfBoundsException();
                    }

                    @Override
                    public void build(double[] start) {
                    }

                    @Override
                    public void evaluate(MultivariateFunction function, Comparator<? super PointValuePair> comparator) {
                    }

                    @Override
                    public int iterate(MultivariateFunction function, Comparator<? super PointValuePair> comparator) {
                        return 0;
                    }
                }});
    }

    /**
     * Test for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}
     * with a non-converging objective function.
     */
    @Test
    public void testOptimizeNonConvergingObjectiveFunction() {
        // Test non-converging objective function
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] * point[0] + point[1] * point[1], 2);
            }
        };

        // Test optimization
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        PointValuePair result = optimizer.optimize(50, objectiveFunction, GoalType.MINIMIZE,
                new OptimizationData[] { new InitialGuess(2, new double[] { 0.5, 0.5 }), new DefaultSimplex(new double[][] {
                        {0.0, 0.0}, {1.0, 0.0}, {0.5, 0.5}
                }) });

        // Test optimization result
        assertArrayEquals(new double[] { 0.5, 0.5 }, result.getPoint(), 1e-6);
        assertEquals(3.125, result.getValue(), 1e-3);
    }
}