package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePairComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Tests the optimizer with a simple function.
     */
    @Test
    public void testSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.pow(x, 2);
        PointValuePair solution = new PointValuePair(2.0, 4.0);
        PointValuePair result = optimizer.optimize(f);
        PointValuePairComparator comparator = new PointValuePairComparator();
        Assert.assertEquals(solution, result, 1e-6);
    }

    /**
     * Tests the optimizer with a function having multiple local minima.
     */
    @Test
    public void testMultipleLocalMinima() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.pow(x, 2) * (Math.sin(x) - Math.cos(x));
        PointValuePair solution = new PointValuePair(1.0, -1.25);
        PointValuePair result = optimizer.optimize(f);
        PointValuePairComparator comparator = new PointValuePairComparator();
        Assert.assertEquals(solution, result, 1e-6);
    }

    /**
     * Tests the optimizer with a function having no real roots.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNoRealRoots() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.pow(x, 2) - 1;
        optimizer.optimize(f);
    }

    /**
     * Tests the optimizer with a function having no stationary points.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNoStationaryPoints() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.sin(x) * Math.cos(x);
        optimizer.optimize(f);
    }

    /**
     * Tests the optimizer with a function having an infinite value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInfiniteValue() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> 1.0 / x;
        optimizer.optimize(f);
    }

    /**
     * Tests the optimizer with a large input.
     */
    @Test
    public void testLargeInput() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.pow(x[0], 2) + Math.pow(x[1], 2);
        double[] startPoint = new double[] {100.0, 100.0};
        PointValuePair solution = new PointValuePair(Arrays.copyOf(new double[] {0.0, 0.0}, 2), 0.0);
        PointValuePair result = optimizer.optimize(f, MaxEval.UNLIMITED, startPoint);
        PointValuePairComparator comparator = new PointValuePairComparator();
        Assert.assertEquals(solution, result, 1e-6);
    }
}