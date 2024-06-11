package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializers.RandomInitializer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.BesselI;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
        UnivariateFunction function = f -> Math.pow(f, 2);
        PointValuePair solution = optimizer.optimize(function, new SimpleBounds(0.0, 1.0));
        Assert.assertEquals(0.0, solution.getPoint(), 1e-6);
    }

    @Test
    public void testFunctionWithLocalMinima() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        PointValuePair solution = optimizer.optimize(function, new SimpleBounds(-2.0, 2.0));
        Assert.assertEquals(-1.0, solution.getPoint(), 1e-6);
    }

    @Test
    public void testFunctionWithGlobalMinimaAndLocalMaxima() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2) - Math.exp(-Math.abs(f));
        PointValuePair solution = optimizer.optimize(function, new SimpleBounds(-5.0, 5.0));
        Assert.assertEquals(-1.4426950408889637, solution.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new BrentOptimizer(1e-6, -0.01);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeThresholdTooSmall() {
        new BrentOptimizer(0.0, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testFunctionWithInfiniteGradient() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
        UnivariateFunction function = f -> 1.0 / (f * Math.log(Math.abs(f)));
        PointValuePair solution = optimizer.optimize(function, new SimpleBounds(-10.0, 10.0));
    }

    @Test
    public void testFunctionWithRandomInitialValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
        UnivariateFunction function = f -> Math.pow(f, 2);
        PointValuePair initialValue = new RandomInitializer().generateInitialPoint(new SimpleBounds(0.0, 1.0));
        PointValuePair solution = optimizer.optimize(function, initialValue);
        Assert.assertEquals(0.0, solution.getPoint(), 1e-6);
    }

    @Test
    public void testBesselFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
        UnivariateFunction function = f -> BesselI.BesselI(0, f);
        PointValuePair solution = optimizer.optimize(function, new SimpleBounds(0.0, 5.0));
        Assert.assertEquals(2.404825557679492, solution.getPoint(), 1e-6);
    }
}