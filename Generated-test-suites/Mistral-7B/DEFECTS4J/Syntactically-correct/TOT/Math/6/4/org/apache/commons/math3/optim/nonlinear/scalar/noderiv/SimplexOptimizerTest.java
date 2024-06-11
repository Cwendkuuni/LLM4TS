package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.apache.commons.math3.optimization.maximumlikelihood.GaussianLikelihoodFunction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * This class tests the {@link SimplexOptimizer} class.
 */
public class SimplexOptimizerTest {

    /**
     * Test a simple scenario where the optimizer can find the minimum of a simple function.
     */
    @Test
    public void testSimpleMinimization() {
        // Define the objective function.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        // Define the optimization data.
        OptimizationData data = new OptimizationData();
        data.setFunction(f);
        data.setGoalType(GoalType.MINIMIZE);
        data.setMaxIterations(100);

        // Perform the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(SimpleValueChecker.DEFAULT);
        PointValuePair result = optimizer.optimize(data);

        // Check the result.
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { -Math.sqrt(2.0) / 2.0, -Math.sqrt(2.0) / 2.0 },
                          result.getPoint(), 1e-6);
    }

    /**
     * Test a scenario where the optimizer finds the minimum of a function with multiple local minima.
     */
    @Test
    public void testMultipleMinima() {
        // Define the objective function.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2.0) + Math.pow(Math.sin(Math.PI * x[1]), 2.0);
            }
        };

        // Define the optimization data.
        OptimizationData data = new OptimizationData();
        data.setFunction(f);
        data.setGoalType(GoalType.MINIMIZE);
        data.setMaxIterations(100);

        // Perform the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(SimpleValueChecker.DEFAULT);
        PointValuePair result = optimizer.optimize(data);

        // Check the result.
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{ 0.7853981633974483, 0.7853981633974483 },
                          result.getPoint(), 1e-6);
    }

    /**
     * Test a scenario where the optimizer cannot find the minimum due to the function having a large gradient.
     */
    @Test(expected = RuntimeException.class)
    public void testLargeGradient() {
        // Define the objective function.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                double sum = 0.0;
                for (double element : x) {
                    sum += Math.pow(element, 10.0);
                }
                return sum;
            }
        };

        // Define the optimization data.
        OptimizationData data = new OptimizationData();
        data.setFunction(f);
        data.setGoalType(GoalType.MINIMIZE);
        data.setMaxIterations(100);

        // Perform the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(SimpleValueChecker.DEFAULT);
        PointValuePair result = optimizer.optimize(data);

        // This test should fail due to the large gradient of the function.
    }

    /**
     * Test a scenario where the optimizer is initialized with a custom simplex.
     */
    @Test
    public void testCustomSimplex() {
        // Define the objective function.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        // Define the optimization data.
        OptimizationData data = new OptimizationData();
        data.setFunction(f);
        data.setGoalType(GoalType.MINIMIZE);
        data.setMaxIterations(100);

        // Define a custom simplex.
        AbstractSimplex simplex = new Simplex();
        simplex.build(new double[]{ 1.0, 1.0, 1.0, 1.0, 1.0 });

        // Perform the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(), simplex);
        PointValuePair result = optimizer.optimize(data);

        // Check the result.
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { -Math.sqrt(2.0) / 2.0, -Math.sqrt(2.0) / 2.0 },
                          result.getPoint(), 1e-6);
    }

    /**
     * Test a scenario where the optimizer is initialized with a custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        // Define the objective function.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.abs(x[0] - 1.0) + Math.abs(x[1] - 2.0);
            }
        };

        // Define the optimization data.
        OptimizationData data = new OptimizationData();
        data.setFunction(f);
        data.setGoalType(GoalType.MINIMIZE);
        data.setMaxIterations(100);

        // Define a custom convergence checker.
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair prev, PointValuePair current) {
                return Math.abs(prev.getValue() - current.getValue()) < 1e-6;
            }
        };

        // Perform the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        PointValuePair result = optimizer.optimize(data);

        // Check the result.
        assertEquals(1.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { 1.0, 2.0 }, result.getPoint(), 1e-6);
    }

    /**
     * Test a scenario where the optimizer is initialized with a custom simplex and convergence checker.
     */
    @Test
    public void testCustomSimplexAndConvergenceChecker() {
        // Define the objective function.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.abs(x[0] - 1.0) + Math.abs(x[1] - 2.0);
            }
        };

        // Define the optimization data.
        OptimizationData data = new OptimizationData();
        data.setFunction(f);
        data.setGoalType(GoalType.MINIMIZE);
        data.setMaxIterations(100);

        // Define a custom simplex and convergence checker.
        AbstractSimplex simplex = new Simplex();
        simplex.build(new double[]{ 1.0, 1.0, 1.0, 1.0, 1.0 });

        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair prev, PointValuePair current) {
                return Math.abs(prev.getValue() - current.getValue()) < 1e-6;
            }
        };

        // Perform the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(checker, simplex);
        PointValuePair result = optimizer.optimize(data);

        // Check the result.
        assertEquals(1.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { 1.0, 2.0 }, result.getPoint(), 1e-6);
    }

    /**
     * Test a scenario where the optimizer is initialized with a custom simplex, convergence checker, and function.
     */
    @Test
    public void testCustomSimplexFunctionAndConvergenceChecker() {
        // Define a custom objective function.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2.0) + Math.pow(Math.sin(Math.PI * x[1]), 2.0);
            }
        };

        // Define the optimization data.
        OptimizationData data = new OptimizationData();
        data.setFunction(f);
        data.setGoalType(GoalType.MINIMIZE);
        data.setMaxIterations(100);

        // Define a custom convergence checker.
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair prev, PointValuePair current) {
                return Math.abs(prev.getValue() - current.getValue()) < 1e-12;
            }
        };

        // Define a custom simplex.
        AbstractSimplex simplex = new Simplex();
        simplex.build(new double[]{ 1.0, 1.0, 1.0, 1.0, 1.0 });

        // Perform the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(checker, simplex, f);
        PointValuePair result = optimizer.optimize(data);

        // Check the result.
        assertEquals(0.0, result.getValue(), 1e-12);
        assertArrayEquals(new double[]{ 0.7853981633974483, 0.7853981633974483 },
                          result.getPoint(), 1e-12);
    }
}