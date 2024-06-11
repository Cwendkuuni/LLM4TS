package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    @Test
    public void testMinimizeSimpleFunction() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double function = Math.pow(Math.sin(Math.PI), 2) + Math.exp(-Math.log(2));
        PointValuePair result = optimizer.optimize(Math::sin, new SimpleBounds(-2, 2), new InitialGuess(-1));
        assertEquals(-1, result.getPoint(), 1e-6);
        assertEquals(-1.4137152725763396, result.getValue(), 1e-6);
    }

    @Test
    public void testMinimizeFunctionWithZeroDerivative() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double function = Math.pow(x, 3) - 3 * x * Math.pow(Math.E, x);
        PointValuePair result = optimizer.optimize(function::getValue, new SimpleBounds(-5, 5), new InitialGuess(1));
        assertEquals(-1.061489827767906, result.getPoint(), 1e-6);
        assertEquals(-3.761794210341824, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        new BrentOptimizer(-1e-6, 1e-6, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeThreshold() {
        new BrentOptimizer(0, 1e-6, null);
    }

    @Test
    public void testMinimizeFunctionWithNaN() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double function = x > 0 ? Math.log(x) : Double.NaN;
        PointValuePair result = optimizer.optimize(function::isNaN, new SimpleBounds(0, 2), new InitialGuess(1));
        assertNull(result);
    }

    @Test
    public void testMinimizeFunctionWithInfiniteValue() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double function = 1 / (x - 1);
        PointValuePair result = optimizer.optimize(function::getValue, new SimpleBounds(-10, 1), new InitialGuess(0));
        assertEquals(1, result.getPoint(), 1e-6);
        assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 1e-6);
    }

    @Test
    public void testMinimizeFunctionWithNegativeBound() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double function = Math.sin(x) * Math.cos(x);
        PointValuePair result = optimizer.optimize(function::getValue, new SimpleBounds(-2, 0), new InitialGuess(-1));
        assertEquals(-1, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }
}