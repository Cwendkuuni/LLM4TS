package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.simple.SquaredErrorFunction;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.apache.commons.math3.optim.initialguess.RandomInitialGuess;
import org.apache.commons.math3.optim.initialguess.GaussianInitialGuess;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.convergence.SimpleLineSearchChecker;
import org.apache.commons.math3.optim.convergence.ConvergenceResult;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class PowellOptimizerTest {

    @Test
    public void testMinimizationWithDefaultParameters() {
        MultivariateFunction function = new SquaredErrorFunction(2);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testMinimizationWithCustomParameters() {
        MultivariateFunction function = new SquaredErrorFunction(2);
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, 1e-8, 1e-8, new SimpleLineSearchChecker(1e-8, 1e-8));
        double[] startPoint = new double[]{1.5, 1.5};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-8);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-8);
    }

    @Test
    public void testMinimizationWithUserDefinedConvergenceChecker() {
        MultivariateFunction function = new SquaredErrorFunction(2);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new CustomConvergenceChecker());
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testMinimizationWithRandomStartingPoint() {
        MultivariateFunction function = new SquaredErrorFunction(2);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        InitialGuess initialGuess = new RandomInitialGuess(2);
        PointValuePair result = optimizer.optimize(function, initialGuess.getInitialPoint());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testMinimizationWithGaussianStartingPoint() {
        MultivariateFunction function = new SquaredErrorFunction(2);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        InitialGuess initialGuess = new GaussianInitialGuess(2);
        PointValuePair result = optimizer.optimize(function, initialGuess.getInitialPoint());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters() {
        PowellOptimizer optimizer = new PowellOptimizer(-1.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.0, 1.0);
    }

    @Test
    public void testMaximizationWithDefaultParameters() {
        MultivariateFunction function = new SquaredErrorFunction(2, true);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, result.getPoint(), 1e-6);
    }

    @Test
    public void testMaximizationWithCustomParameters() {
        MultivariateFunction function = new SquaredErrorFunction(2, true);
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, 1e-8, 1e-8, new SimpleLineSearchChecker(1e-8, 1e-8));
        double[] startPoint = new double[]{1.5, 1.5};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 1e-8);
        assertArrayEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, result.getPoint(), 1e-8);
    }

    @Test
    public void testMaximizationWithUserDefinedConvergenceChecker() {
        MultivariateFunction function = new SquaredErrorFunction(2, true);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new CustomConvergenceCheckerMax());
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, result.getPoint(), 1e-6);
    }

    @Test
    public void testMaximizationWithRandomStartingPoint() {
        MultivariateFunction function = new SquaredErrorFunction(2, true);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        InitialGuess initialGuess = new RandomInitialGuess(2);
        PointValuePair result = optimizer.optimize(function, initialGuess.getInitialPoint());
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, result.getPoint(), 1e-6);
    }

    @Test
    public void testMaximizationWithGaussianStartingPoint() {
        MultivariateFunction function = new SquaredErrorFunction(2, true);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        InitialGuess initialGuess = new GaussianInitialGuess(2);
        PointValuePair result = optimizer.optimize(function, initialGuess.getInitialPoint());
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, result.getPoint(), 1e-6);
    }

    @Test
    public void testConvergenceChecking() {
        MultivariateFunction function = new SquaredErrorFunction(2);
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, new CustomConvergenceChecker());
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-8);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-8);
        ConvergenceResult convergenceResult = optimizer.getLastConvergenceResult();
        assertEquals(ConvergenceResult.CONVERGED, convergenceResult.getConvergenceStatus());
    }

    @Test
    public void testConvergenceCheckingWithMaximumNumberOfIterations() {
        MultivariateFunction function = new SquaredErrorFunction(2);
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, new CustomConvergenceChecker(), 10);
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-8);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-8);
        ConvergenceResult convergenceResult = optimizer.getLastConvergenceResult();
        assertEquals(ConvergenceResult.MAX_ITERATIONS_REACHED, convergenceResult.getConvergenceStatus());
    }

    @Test
    public void testConvergenceCheckingWithCustomGoalType() {
        MultivariateFunction function = new SquaredErrorFunction(2);
        PowellOptimizer optimizer = new PowellOptimizer(1e-8, 1e-8, new CustomConvergenceCheckerGoalTypeMax());
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 1e-8);
        assertArrayEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, result.getPoint(), 1e-8);
        ConvergenceResult convergenceResult = optimizer.getLastConvergenceResult();
        assertEquals(ConvergenceResult.CONVERGED, convergenceResult.getConvergenceStatus());
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public ConvergenceResult checkConvergence(int iteration, PointValuePair start, PointValuePair current) {
            double delta = Math.abs(current.getValue() - start.getValue());
            return new ConvergenceResult(delta < 1e-6 ? ConvergenceResult.CONVERGED : ConvergenceResult.ITERATIONS_REACHED);
        }
    }

    private static class CustomConvergenceCheckerMax implements ConvergenceChecker<PointValuePair> {
        @Override
        public ConvergenceResult checkConvergence(int iteration, PointValuePair start, PointValuePair current) {
            double delta = Math.abs(start.getValue() - current.getValue());
            return new ConvergenceResult(delta > 1e-6 ? ConvergenceResult.MAX_ITERATIONS_REACHED : ConvergenceResult.CONVERGED);
        }
    }

    private static class CustomConvergenceCheckerGoalTypeMax implements ConvergenceChecker<PointValuePair> {
        @Override
        public ConvergenceResult checkConvergence(int iteration, PointValuePair start, PointValuePair current) {
            GoalType goalType = PowellOptimizer.this.getGoalType();
            return (goalType == GoalType.MAXIMIZE && current.getValue() > 0)
                    || (goalType == GoalType.MINIMIZE && current.getValue() < 0)
                    ? ConvergenceResult.CONVERGED
                    : ConvergenceResult.ITERATIONS_REACHED;
        }
    }
}