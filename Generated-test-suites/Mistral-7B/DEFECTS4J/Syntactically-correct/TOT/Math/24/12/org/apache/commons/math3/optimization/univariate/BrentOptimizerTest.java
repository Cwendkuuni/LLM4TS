package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    /**
     * Test for optimization with a simple quadratic function.
     */
    @Test
    public void testSimpleQuadraticFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        PointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
        Assert.assertEquals(-2.44948974278317, result.getPoint(), 1e-6);
    }

    /**
     * Test for optimization with a function having multiple minima.
     */
    @Test
    public void testFunctionWithMultipleMinima() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                double result = Math.pow(Math.abs(x), 3) - 4 * Math.pow(x, 2);
                if (x > 3) {
                    result += 10 * (x - 3);
                }
                return result;
            }
        };
        PointValuePair result = optimizer.optimize(function, -10.0, 10.0, GoalType.MINIMIZE);
        Assert.assertEquals(-2.85267103624765, result.getPoint(), 1e-6);
    }

    /**
     * Test for optimization with a function having no root.
     */
    @Test(expected = UnivariateOptimizationException.class)
    public void testFunctionWithNoRoot() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        PointValuePair result = optimizer.optimize(function, -10.0, 10.0, GoalType.MINIMIZE);
    }

    /**
     * Test for optimization with a function having a very large minimum.
     */
    @Test
    public void testFunctionWithVeryLargeMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-18, 1e-18);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -E.EXPONENTIAL.value(x);
            }
        };
        PointValuePair result = optimizer.optimize(function, -1000.0, 1000.0, GoalType.MINIMIZE);
        Assert.assertEquals(-8.68575683E+26, result.getPoint(), 1e-18);
    }
}