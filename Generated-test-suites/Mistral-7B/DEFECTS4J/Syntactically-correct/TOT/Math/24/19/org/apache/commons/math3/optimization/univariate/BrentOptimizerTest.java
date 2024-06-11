package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * A simple test function.
     */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * Math.sin(x) * Math.cos(x);
        }
    };

    /**
     * The optimizer to test.
     */
    private UnivariateOptimizer optimizer;

    /**
     * Sets up the optimizer for the tests.
     */
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-5, 1e-5);
    }

    /**
     * Tests optimization of a function with a minimum value.
     */
    @Test
    public void testMinimum() {
        UnivariatePointValuePair minimum = optimizer.optimize(TEST_FUNCTION, 0, 10);
        Assert.assertEquals(1.2096152426188254d, minimum.getPoint(), 1e-6);
        Assert.assertEquals(-3.141592653589793d, minimum.getValue(), 1e-6);
    }

    /**
     * Tests optimization of a function with a maximum value.
     */
    @Test
    public void testMaximum() {
        optimizer = new BrentOptimizer(1e-5, 1e-5, new AbsoluteGoalType());
        UnivariatePointValuePair maximum = optimizer.optimize(TEST_FUNCTION, 0, 10);
        Assert.assertEquals(2.0411254669232157d, maximum.getPoint(), 1e-6);
        Assert.assertEquals(3.141592653589793d, maximum.getValue(), 1e-6);
    }

    /**
     * Tests optimization of a function with no real roots.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoRealRoots() {
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x)) * Math.sin(Math.PI / x);
            }

            @Override
            public double derivative(double x) {
                // Not defined for x = 0.
                if (Double.isNaN(x)) {
                    throw new IllegalArgumentException("Derivative is not defined for x = 0.");
                }
                return -Math.sqrt(Math.abs(x)) * (Math.PI / (Math.pow(x, 2.0) + 1.0))
                       + Math.cos(Math.PI / x) / (2 * Math.sqrt(Math.abs(x)));
            }
        }, 0, 1);
    }

    /**
     * Tests optimization of a function with a minimum and a maximum.
     */
    @Test
    public void testMinAndMax() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) * Math.sin(Math.PI / (x - 1));
            }

            @Override
            public double derivative(double x) {
                return 2 * Math.sin(x) * Math.cos(x) * Math.sin(Math.PI / (x - 1))
                      + Math.pow(Math.sin(x), 2) * Math.cos(Math.PI / (x - 1)) / (x - 1)
                      - Math.PI / ((x - 1) * Math.pow(Math.sin(Math.PI / (x - 1)), 2))
                      * Math.cos(Math.PI / (x - 1));
            }
        };

        UnivariatePointValuePair minimum = optimizer.optimize(function, -5, 5);
        Assert.assertEquals(-1.926518352378275d, minimum.getPoint(), 1e-6);
        Assert.assertEquals(-1.5707963267948966d, minimum.getValue(), 1e-6);

        optimizer = new BrentOptimizer(1e-5, 1e-5, new AbsoluteGoalType());
        UnivariatePointValuePair maximum = optimizer.optimize(function, -5, 5);
        Assert.assertEquals(1.9265183523782762d, maximum.getPoint(), 1e-6);
        Assert.assertEquals(1.5707963267948966d, maximum.getValue(), 1e-6);
    }
}