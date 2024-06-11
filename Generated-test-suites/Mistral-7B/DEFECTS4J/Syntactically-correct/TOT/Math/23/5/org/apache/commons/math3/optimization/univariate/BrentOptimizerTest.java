package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateFunction f = new RootFunction(5.0);
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        PointValuePair solution = optimizer.optimize(f, 1.0, 5.0);
        assertNotNull(solution);
        assertEquals(2.0, solution.getPoint(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        optimizer.optimize(null, 0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNullInterval() {
        UnivariateFunction f = x -> x * x;
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        optimizer.optimize(f, null, 1.0);
    }

    @Test
    public void testBrentOptimizerWithGoalTypeMinimize() {
        UnivariateFunction f = x -> x * x;
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, GoalType.MINIMIZE);
        PointValuePair solution = optimizer.optimize(f, 1.0, 4.0);
        assertNotNull(solution);
        assertEquals(1.0, solution.getPoint(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, -1e-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithTooSmallRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-5);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        UnivariateFunction f = x -> Math.log(1 + x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3, new MyConvergenceChecker());
        PointValuePair solution = optimizer.optimize(f, 1.0, 2.0);
        assertNotNull(solution);
        assertEquals(1.0, solution.getPoint(), 1e-4);
        assertEquals(0.0, solution.getValue(), 1e-4);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceCheckerAndNullCheckConvg() {
        UnivariateFunction f = x -> Math.log(1 + x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3, new MyConvergenceChecker());
        PointValuePair solution = optimizer.optimize(f, 1.0, 2.0);
        assertNotNull(solution);
        assertEquals(1.0, solution.getPoint(), 1e-4);
        assertEquals(0.0, solution.getValue(), 1e-4);
        optimizer.setConvergenceChecker(null);
        PointValuePair solution2 = optimizer.optimize(f, 1.0, 2.0);
        assertNull(solution2);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceCheckerAndInterrupted() {
        UnivariateFunction f = x -> Math.log(1 + x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3, new MyConvergenceChecker());
        boolean interrupted = false;
        optimizer.addConvergenceChecker((iter, oldSolution, newSolution) -> {
            if (iter == 10) {
                interrupted = true;
                return false;
            }
            return true;
        });
        PointValuePair solution = optimizer.optimize(f, 1.0, 2.0);
        assertNotNull(solution);
        assertEquals(1.0, solution.getPoint(), 1e-4);
        assertEquals(0.0, solution.getValue(), 1e-4);
        assertTrue(interrupted);
    }

    private static class RootFunction implements UnivariateFunction {

        private final double root;

        public RootFunction(double root) {
            this.root = root;
        }

        @Override
        public double value(double x) {
            return Math.pow(x, 2) - root;
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {

        @Override
        public boolean converged(int iteration, PointValuePair oldSolution, PointValuePair newSolution) {
            double oldError = Math.abs(oldSolution.getValue() - newSolution.getValue());
            double newError = Math.abs(newSolution.getValue());
            return oldError < newError || (oldError <= 1e-5 && newError <= 1e-5);
        }
    }
}