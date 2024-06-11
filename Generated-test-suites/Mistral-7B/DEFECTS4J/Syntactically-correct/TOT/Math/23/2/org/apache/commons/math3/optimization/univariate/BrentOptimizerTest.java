package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Function f(x) = x^3 - 2x^2 + x - 1.
     */
    static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
        }

        @Override
        public double derivative(double x) {
            return 3 * Math.pow(x, 2) - 4 * x + 1;
        }

        @Override
        public double derivativeValue(double x) {
            return value(x) + derivative(x);
        }
    }

    /**
     * Test of BrentOptimizer with a simple function.
     */
    @Test
    public void testOptimize() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new MyFunction();
        SimpleBounds bounds = new SimpleBounds(-10, 10);
        PointValuePair optimum = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        Assert.assertEquals(-1.0, optimum.getPoint(), 1e-6);
        Assert.assertEquals(1.0000001623334133, function.value(optimum.getPoint()), 1e-6);
    }

    /**
     * Test of BrentOptimizer when the function is not defined in the bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeOutOfBounds() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new MyFunction();
        SimpleBounds bounds = new SimpleBounds(-100, -10);
        optimizer.optimize(function, bounds, GoalType.MINIMIZE);
    }

    /**
     * Test of BrentOptimizer when the function is not differentiable at the
     * minim.
     */
    @Test
    public void testOptimizeUndefinedDerivative() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new MyFunction();
        SimpleBounds bounds = new SimpleBounds(-1, 1);
        PointValuePair optimum = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        Assert.assertEquals(0.0, optimum.getPoint(), 1e-6);
        Assert.assertEquals(E.EULER_NUMBER.doubleValue(), function.value(optimum.getPoint()), 1e-6);
    }

    /**
     * Test of BrentOptimizer when the function is not monotonic.
     */
    @Test
    public void testOptimizeNonMonotonicFunction() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new MyFunction();
        SimpleBounds bounds = new SimpleBounds(-1, 1);
        PointValuePair optimum = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        Assert.assertEquals(0.0, optimum.getPoint(), 1e-6);
        Assert.assertEquals(E.EULER_NUMBER.doubleValue(), function.value(optimum.getPoint()), 1e-6);
    }

    /**
     * Test of BrentOptimizer when the convergence criterion is not met.
     */
    @Test
    public void testOptimizeConvergenceCriterionNotMet() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12);
        UnivariateFunction function = new MyFunction();
        SimpleBounds bounds = new SimpleBounds(-1, 1);
        PointValuePair optimum = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        Assert.assertNotNull(optimum);
        Assert.assertEquals(0.0, optimum.getPoint(), 1e-6);
        Assert.assertEquals(E.EULER_NUMBER.doubleValue(), function.value(optimum.getPoint()), 1e-6);
    }

    /**
     * Test of BrentOptimizer with a complex function.
     */
    @Test
    public void testOptimizeComplexFunction() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = x -> Math.log(Math.pow(Math.sin(x), 2) + 1);
        SimpleBounds bounds = new SimpleBounds(0, Math.PI / 2);
        PointValuePair optimum = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        Assert.assertEquals(Math.PI / 4, optimum.getPoint(), 1e-6);
        Assert.assertEquals(-Math.log(Math.sqrt(2) / 2), function.value(optimum.getPoint()), 1e-6);
    }
}