package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
        PointValuePair solution = optimizer.optimize(function, new SimpleBounds(1.0, 2.0));
        assertEquals(2.0, solution.getPoint(), 0.001);
        assertEquals(-3.920421353, solution.getValue(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidBounds() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
        optimizer.optimize(function, new SimpleBounds(-1, 1)); // boundary values are inclusive
        fail("Should have thrown an IllegalArgumentException");
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) * Math.sin(Math.PI * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001, new MyConvergenceChecker());
        PointValuePair solution = optimizer.optimize(function, new SimpleBounds(-1, 1));
        assertEquals(-0.632120559, solution.getPoint(), 0.001);
        assertEquals(0.144665153, solution.getValue(), 0.0001);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            return current.getValue() < 0.01;
        }
    }

    @Test
    public void testBrentOptimizerMaximization() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) / (x + 1);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair solution = optimizer.optimize(function, new SimpleBounds(-1, 1));
        assertEquals(1.368230923, solution.getPoint(), 0.001);
        assertEquals(1.718281828, solution.getValue(), 0.0001);
    }
}