package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializers.CalculusAveragingInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LocalOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest extends AbstractTest {

    /**
     * Test function {@code f(x) = x^3 - 3x^2 + 2x - 1}.
     */
    @Test
    public void testFunctionF() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 3 * x * x + 2 * x - 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * x * x - 6 * x + 2;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        PointValuePair result = optimizer.optimize(function, new CalculusAveragingInitializer(1.0), GoalType.MINIMIZE);

        Assert.assertEquals("minimum not found", -1.0, result.getPoint(), 1e-6);
        Assert.assertEquals("minimum value not correct", -2.0, result.getValue(), 1e-6);
    }

    /**
     * Test function {@code g(x) = exp(x) - x - 1}.
     */
    @Test
    public void testFunctionG() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return E.exp(x) - x - 1;
            }

            @Override
            public double derivative(double x) {
                return E.exp(x) - 1.0;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        PointValuePair result = optimizer.optimize(function, new CalculusAveragingInitializer(0.0), GoalType.MINIMIZE);

        Assert.assertEquals("minimum not found", 0.567143290863683, result.getPoint(), 1e-6);
        Assert.assertEquals("minimum value not correct", -0.606501332950622, result.getValue(), 1e-6);
    }

    /**
     * Test function {@code h(x) = x^2 + 1}.
     */
    @Test
    public void testFunctionH() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x + 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        PointValuePair result = optimizer.optimize(function, new CalculusAveragingInitializer(1.0), GoalType.MINIMIZE);

        Assert.assertEquals("minimum not found", 0.0, result.getPoint(), 1e-6);
        Assert.assertEquals("minimum value not correct", 1.0, result.getValue(), 1e-6);
    }

    /**
     * Test function {@code i(x) = sin(x)}.
     */
    @Test
    public void testFunctionI() {
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

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        PointValuePair result = optimizer.optimize(function, new CalculusAveragingInitializer(Math.PI / 2.0), GoalType.MINIMIZE);

        Assert.assertEquals("minimum not found", Math.PI / 2.0, result.getPoint(), 1e-6);
        Assert.assertEquals("minimum value not correct", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test function {@code j(x) = x^2}.
     */
    @Test
    public void testFunctionJ() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        PointValuePair result = optimizer.optimize(function, new CalculusAveragingInitializer(2.0), GoalType.MINIMIZE);

        Assert.assertEquals("minimum not found", 0.0, result.getPoint(), 1e-6);
        Assert.assertEquals("minimum value not correct", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test function {@code k(x) = |x|}.
     */
    @Test
    public void testFunctionK() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x);
            }

            @Override
            public double derivative(double x) {
                return x >= 0.0 ? 1.0 : -1.0;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        PointValuePair result = optimizer.optimize(function, new CalculusAveragingInitializer(-1.0), GoalType.MINIMIZE);

        Assert.assertEquals("minimum not found", 0.0, result.getPoint(), 1e-6);
        Assert.assertEquals("minimum value not correct", 0.0, result.getValue(), 1e-6);
    }

    /**
     * Test null input.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        UnivariateFunction function = null;
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        optimizer.optimize(function, new CalculusAveragingInitializer(0.0), GoalType.MINIMIZE);
    }

    /**
     * Test null function derivative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullDerivative() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        optimizer.optimize(function, null, GoalType.MINIMIZE);
    }

    /**
     * Test invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        optimizer.optimize(function, new CalculusAveragingInitializer(2.0), GoalType.MAXIMIZE);
    }
}