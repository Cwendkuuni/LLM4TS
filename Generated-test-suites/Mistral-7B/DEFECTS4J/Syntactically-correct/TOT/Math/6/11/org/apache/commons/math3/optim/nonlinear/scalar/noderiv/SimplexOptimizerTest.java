package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimpleXPoint;
import org.apache.commons.math3.optim.SimpleXSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.nonlinear.scalar.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleXOptimizationData;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * A simple test function for testing optimization.
     */
    private static final MultivariateFunction TEST_FUNCTION = new MultivariateFunction() {
        public double value(double[] point) {
            return 2.0 * point[0] * point[0] + 3.0 * point[1] * point[1];
        }
    };

    /**
     * Tests optimization of a simple quadratic function using the {@link SimplexOptimizer}.
     */
    @Test
    public void testQuadraticFunctionSimpleOptimizer() {
        // Test case: optimization of a simple quadratic function.
        final int maxEval = 100;
        final GoalType goalType = GoalType.MINIMIZE;
        final double[] initialPoint = new double[] {1.0, 1.0};

        // Create a simplex optimizer with default convergence checker.
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Create the optimization data.
        OptimizationData optimizationData = new SimpleXOptimizationData(
                TEST_FUNCTION, goalType, initialPoint, new LinearConstraintSet(), maxEval);

        // Set the optimization data to the optimizer.
        optimizer.setOptimizationData(optimizationData);

        // Perform optimization.
        PointValuePair optPointValuePair = optimizer.optimize();

        // Check the result.
        Assert.assertEquals(0.0, optPointValuePair.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[] {-1.0, -1.0}, optPointValuePair.getPoint(), 1e-8);
    }

    /**
     * Tests optimization of a simple quadratic function using the {@link SimplexOptimizer} with custom convergence checker.
     */
    @Test
    public void testQuadraticFunctionCustomConvergenceChecker() {
        // Test case: optimization of a simple quadratic function with a custom convergence checker.
        final int maxEval = 100;
        final double relTolerance = 1e-8;
        final double absTolerance = 1e-8;
        final GoalType goalType = GoalType.MINIMIZE;
        final double[] initialPoint = new double[] {1.0, 1.0};

        // Create a simplex optimizer with a custom convergence checker.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(relTolerance, absTolerance));

        // Create the optimization data.
        OptimizationData optimizationData = new SimpleXOptimizationData(
                TEST_FUNCTION, goalType, initialPoint, new LinearConstraintSet(), maxEval);

        // Set the optimization data to the optimizer.
        optimizer.setOptimizationData(optimizationData);

        // Perform optimization.
        PointValuePair optPointValuePair = optimizer.optimize();

        // Check the result.
        Assert.assertEquals(0.0, optPointValuePair.getValue(), relTolerance);
        Assert.assertArrayEquals(new double[] {-1.0, -1.0}, optPointValuePair.getPoint(), relTolerance);
    }

    /**
     * Tests optimization of a function with linear constraints.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testFunctionWithLinearConstraints() {
        // Test case: optimization of a function with linear constraints.
        final int maxEval = 100;
        final GoalType goalType = GoalType.MINIMIZE;
        final double[] initialPoint = new double[] {1.0, 1.0};

        // Create a simplex optimizer.
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Create the optimization data.
        LinearConstraint xLower = new LinearConstraint(1.0, 0.0, null, 0.0);
        LinearConstraint yLower = new LinearConstraint(1.0, 0.0, null, 0.0);
        LinearConstraintSet constraintSet = new LinearConstraintSet(xLower, yLower);
        OptimizationData optimizationData = new SimpleXOptimizationData(
                TEST_FUNCTION, goalType, initialPoint, constraintSet, maxEval);

        // Set the optimization data to the optimizer.
        optimizer.setOptimizationData(optimizationData);

        // Attempt to perform optimization. This should throw an exception as
        // the provided optimization problem has no feasible solution.
        optimizer.optimize();
    }

    /**
     * Tests optimization of a function with non-linear constraints.
     */
    @Test
    public void testFunctionWithNonlinearConstraints() {
        // Test case: optimization of a function with non-linear constraints.
        final int maxEval = 100;
        final GoalType goalType = GoalType.MINIMIZE;
        final double[] initialPoint = new double[] {1.0, 1.0};

        // Create a simplex optimizer.
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Create the optimization data.
        LinearConstraint xLower = new LinearConstraint(1.0, 0.0, null, 0.0);
        LinearConstraint yLower = new LinearConstraint(1.0, 0.0, null, 0.0);
        LinearConstraint xUpper = new LinearConstraint(1.0, 0.0, null, 2.0);
        LinearConstraint yUpper = new LinearConstraint(1.0, 0.0, null, 2.0);
        LinearConstraintSet constraintSet = new LinearConstraintSet(xLower, yLower, xUpper, yUpper);
        MultivariateFunction nonlinearConstraint = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0) - 1.0;
            }
        };
        OptimizationData optimizationData = new SimpleXOptimizationData(
                TEST_FUNCTION, goalType, initialPoint, constraintSet, nonlinearConstraint, maxEval);

        // Set the optimization data to the optimizer.
        optimizer.setOptimizationData(optimizationData);

        // Perform optimization.
        PointValuePair optPointValuePair = optimizer.optimize();

        // Check the result.
        Assert.assertEquals(1.0, optPointValuePair.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[] {0.5, 0.5}, optPointValuePair.getPoint(), 1e-8);
    }

    /**
     * Tests optimization of a function with a custom objective function.
     */
    @Test
    public void testFunctionWithCustomObjectiveFunction() {
        // Test case: optimization of a function with a custom objective function.
        final int maxEval = 100;
        final GoalType goalType = GoalType.MINIMIZE;
        final double[] initialPoint = new double[] {1.0, 1.0};

        // Create a simplex optimizer.
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Create the optimization data.
        MultivariateFunction customObjectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.sqrt(Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0));
            }
        };
        OptimizationData optimizationData = new SimpleXOptimizationData(
                customObjectiveFunction, goalType, initialPoint, null, maxEval);

        // Set the optimization data to the optimizer.
        optimizer.setOptimizationData(optimizationData);

        // Perform optimization.
        PointValuePair optPointValuePair = optimizer.optimize();

        // Check the result.
        Assert.assertEquals(1.0, optPointValuePair.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[] {0.0, 0.0}, optPointValuePair.getPoint(), 1e-8);
    }

    /**
     * Tests optimization of a function with a custom simplex.
     */
    @Test
    public void testFunctionWithCustomSimplex() {
        // Test case: optimization of a function with a custom simplex.
        final int maxEval = 100;
        final GoalType goalType = GoalType.MINIMIZE;
        final double[] initialPoint = new double[] {1.0, 1.0};

        // Create a custom simplex.
        SimpleXSimplex customSimplex = new SimpleXSimplex(2) {
            @Override
            public void build(SimpleXPoint[] points) {
                // Add custom initialization logic here.
            }

            @Override
            public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
                // Add custom iteration logic here.
            }
        };

        // Create a simplex optimizer.
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Set the custom simplex to the optimizer.
        optimizer.setSimplex(customSimplex);

        // Create the optimization data.
        OptimizationData optimizationData = new SimpleXOptimizationData(
                TEST_FUNCTION, goalType, initialPoint, null, maxEval);

        // Set the optimization data to the optimizer.
        optimizer.setOptimizationData(optimizationData);

        // Perform optimization.
        PointValuePair optPointValuePair = optimizer.optimize();

        // Check the result.
        Assert.assertEquals(0.0, optPointValuePair.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[] {-1.0, -1.0}, optPointValuePair.getPoint(), 1e-8);
    }

    /**
     * Tests optimization of a function with a null objective function.
     */
    @Test(expected = NullPointerException.class)
    public void testFunctionWithNullObjectiveFunction() {
        // Test case: optimization of a function with a null objective function.
        final int maxEval = 100;
        final GoalType goalType = GoalType.MINIMIZE;
        final double[] initialPoint = new double[] {1.0, 1.0};

        // Create a simplex optimizer.
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Set the optimization data with a null objective function.
        OptimizationData optimizationData = new SimpleXOptimizationData(null, goalType, initialPoint, null, maxEval);
        optimizer.setOptimizationData(optimizationData);

        // Attempt to perform optimization. This should throw an exception.
        optimizer.optimize();
    }
}