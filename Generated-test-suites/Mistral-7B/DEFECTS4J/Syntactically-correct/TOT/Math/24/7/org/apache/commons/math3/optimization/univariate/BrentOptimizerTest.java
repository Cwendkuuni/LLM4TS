package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test function: f(x) = x^3 - 4x^2 + 6x - 3
     */
    @Test
    public void testBrentOptimizerExample() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 4 * x * x + 6 * x - 3;
            }

            @Override
            public double derivative(double x) {
                return 3 * x * x - 8 * x + 6;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, new SimpleBounds(1, 3), new MaxEval(100));
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(-2.916664044659333E-3, result.getValue(), 1e-6);
    }

    /**
     * Test function: f(x) = |x|
     */
    @Test
    public void testBrentOptimizerAbs() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x);
            }

            @Override
            public double derivative(double x) {
                return x >= 0 ? 1 : -1;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, new SimpleBounds(-1, 1), new MaxEval(100));
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test function: f(x) = x^2
     */
    @Test
    public void testBrentOptimizerQuadratic() {
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
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, new SimpleBounds(0, 1), new MaxEval(100));
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test function: f(x) = 1/(x - 1)
     */
    @Test
    public void testBrentOptimizerSingularity() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x == 1) {
                    throw new ArithmeticException("Divide by zero");
                }
                return 1.0 / (x - 1);
            }

            @Override
            public double derivative(double x) {
                if (x == 1) {
                    throw new ArithmeticException("Divide by zero");
                }
                return -1.0 / (x - 1) * (x - 1) != 0 ? -1.0 / (x - 1) * (x - 1) : Double.NaN;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        try {
            PointValuePair result = optimizer.optimize(function, new SimpleBounds(0.9, 1.1), new MaxEval(100));
            fail("Expected exception in this test case.");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
}