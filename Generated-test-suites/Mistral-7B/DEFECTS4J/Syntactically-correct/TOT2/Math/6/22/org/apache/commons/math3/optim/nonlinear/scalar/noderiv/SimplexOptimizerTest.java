package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /** Simple bounds for the optimizer. */
    private SimpleBounds bounds;

    /** Simple value checker for the optimizer. */
    private SimpleValueChecker valueChecker;

    /** Simplex optimizer instance. */
    private SimplexOptimizer optimizer;

    /** Initial point for optimization. */
    private double[] startPoint = new double[] { 1.0, 1.0 };

    /**
     * Set up the test environment.
     */
    @Before
    public void setUp() {
        bounds = new SimpleBounds(new double[] { 0.0, 0.0 }, new double[] { 10.0, 10.0 });
        valueChecker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        optimizer = new SimplexOptimizer(valueChecker);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimize() {
        UnivariateFunction function = new PolynomialFunction(new double[] { 1.0, -2.0, 1.0 }, 2);
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimize(100, function, goalType, new OptimizationData[] {
            new InitialGuess(startPoint),
            bounds
        });

        assertArrayEquals(new double[] { 1.0, 1.0 }, result.getPoint(), 0.0);
        assertEquals(-3.0, result.getValue(), 0.0);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * When the goal is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNoGoalType() {
        UnivariateFunction function = new PolynomialFunction(new double[] { 1.0, -2.0, 1.0 }, 2);
        optimizer.optimize(100, function, null, new OptimizationData[] {});
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * When the objective function is null.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullFunction() {
        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(new PolynomialFunction(new double[] { 1.0, -2.0, 1.0 }, 2), null);
        optimizer.optimize(100, problem, GoalType.MINIMIZE, new OptimizationData[] {});
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * When the optimization data is null.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullData() {
        UnivariateFunction function = new PolynomialFunction(new double[] { 1.0, -2.0, 1.0 }, 2);
        optimizer.optimize(100, function, GoalType.MINIMIZE, null);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * When the initial simplex is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNoSimplex() {
        MultivariateFunction function = new PolynomialFunction(new double[] { 1.0, -2.0, 1.0 }, 2);
        optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[] { new InitialGuess(startPoint) });
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * When the optimization is not converged and the maximum number of evaluations is reached.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testOptimizeMaxEvalExceeded() {
        UnivariateFunction function = new PolynomialFunction(new double[] { 1.0, -2.0, 1.0 }, 2);
        optimizer.optimize(1, function, GoalType.MINIMIZE, new OptimizationData[] { new InitialGuess(startPoint) });
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     * When the optimization is not converged.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNotConverged() {
        UnivariateFunction function = new PolynomialFunction(new double[] { 1.0, -2.0, 1.0 }, 2);
        optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[] { new InitialGuess(new double[] { 10.0, 10.0 }) });
    }
}