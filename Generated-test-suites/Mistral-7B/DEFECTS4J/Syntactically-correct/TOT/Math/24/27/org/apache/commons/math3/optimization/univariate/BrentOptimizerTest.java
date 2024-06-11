package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    /**
     * Test with a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.01);
        PointValuePair result = optimizer.optimize(f, -5, 5);
        assertEquals(0.0, result.getPoint(), 1e-6);
    }

    /**
     * Test with a simple linear function.
     */
    @Test
    public void testLinearFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 2 * x + 1;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.01);
        PointValuePair result = optimizer.optimize(f, -5, 5);
        assertEquals(-0.5, result.getPoint(), 1e-6);
    }

    /**
     * Test with a function that has multiple local minima,
     * and the algorithm should find the global minimum.
     */
    @Test
    public void testMultiLocalMinimaFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.01);
        PointValuePair result = optimizer.optimize(f, -10, 10);
        assertEquals(-1.2732, result.getPoint(), 1e-6);
    }

    /**
     * Test with a function that is not defined on the whole interval.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFunctionNotDefinedOnInterval() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    throw new UnsupportedOperationException("Function is not defined for x < 0");
                }
                return Math.log(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.01);
        PointValuePair result = optimizer.optimize(f, -5, 5);
    }

    /**
     * Test with a function that throws an exception.
     */
    @Test(expected = RuntimeException.class)
    public void testFunctionThrowingException() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) throws RuntimeException {
                if (x == 0) {
                    throw new RuntimeException("Divide by zero error");
                }
                return 1 / x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.01);
        PointValuePair result = optimizer.optimize(f, -10, 10);
    }
}