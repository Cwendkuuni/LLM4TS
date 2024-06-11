package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.apache.commons.math3.special.MathConstants;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    /**
     * Test function f(x) = x^3 - 2x^2 + x - 1
     */
    @Test
    public void testBrentOptimizerExampleFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) - 4 * x + 1;
            }
        };

        SimpleBounds bounds = new SimpleBounds(-3, 3);
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);

        PointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);

        assertEquals(-1.0, result.getPoint(), 0.0);
        assertEquals(-2.220446049250313E-16, result.getValue(), 0.0);
    }

    /**
     * Test function f(x) = Math.sin(x)
     */
    @Test
    public void testBrentOptimizerSinFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        SimpleBounds bounds = new SimpleBounds(MathConstants.PI / 2, MathConstants.PI / 2 + Math.PI / 4);
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);

        PointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);

        assertEquals(MathConstants.PI / 2 + Math.PI / 4, result.getPoint(), 0.001);
        assertEquals(-1.0, result.getValue(), 0.0);
    }

    /**
     * Test function f(x) = x^2
     */
    @Test
    public void testBrentOptimizerSquareFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        SimpleBounds bounds = new SimpleBounds(-3, 3);
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);

        PointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);

        assertEquals(-1.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test function with a non-differentiable point
     */
    @Test
    public void testBrentOptimizerNonDifferentiableFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x - 0.5);
            }

            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException("This function is not differentiable at x = 0.5");
            }
        };

        SimpleBounds bounds = new SimpleBounds(-1, 1);
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);

        PointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);

        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test function with a non-bounded domain
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNonBoundedFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1 / x;
            }

            @Override
            public double derivative(double x) {
                return -1 / (Math.pow(x, 2) + 1);
            }
        };

        SimpleBounds bounds = new SimpleBounds(-1, 0);
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);

        optimizer.optimize(function, bounds, GoalType.MINIMIZE);
    }

    /**
     * Test function with a non-convex domain
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testBrentOptimizerNonConvexFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) * (x < 0 ? -1 : 1);
            }

            @Override
            public double derivative(double x) {
                return 2 * Math.pow(x, 1) * (x < 0 ? -1 : 1);
            }
        };

        SimpleBounds bounds = new SimpleBounds(-1, 1);
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);

        optimizer.optimize(function, bounds, GoalType.MINIMIZE);
    }
}