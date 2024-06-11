package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /** The optimizer to test. */
    private BrentOptimizer optimizer;

    /**
     * Sets up the optimizer for each test.
     */
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1000);
        optimizer.setMaximalIterationCount(100);
        optimizer.setAbsoluteAccuracy(1E-10);
        optimizer.setRelativeAccuracy(1.0e-14);
    }

    /**
     * Tests optimization of a simple function.
     */
    @Test
    public void testSimpleFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 0);
        Assert.assertEquals(-1.0, result.getPoint(), 1.0e-6);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    /**
     * Tests optimization of a function with a local minimum.
     */
    @Test
    public void testFunctionWithLocalMinimum() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < -1) {
                    return Math.pow(x, 2) + 3;
                } else if (x > 2) {
                    return Math.pow(x, 2) + 1;
                }
                return Math.sin(Math.PI * x) / x;
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -5, 5, 0);
        Assert.assertEquals(-1.0386131393523564, result.getPoint(), 1.0e-6);
        Assert.assertEquals(-3.2319307174062197, result.getValue(), 1.0e-6);
    }

    /**
     * Tests optimization of a function with a maximum.
     */
    @Test
    public void testFunctionWithMaximum() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x, 2);
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10, 0);
        Assert.assertEquals(0.0, result.getPoint(), 1.0e-6);
        Assert.assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    /**
     * Tests optimization of a function with a minimum at the boundary.
     */
    @Test
    public void testFunctionWithMinimumAtBoundary() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x;
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10, -1, 0);
        Assert.assertEquals(-2.0, result.getPoint(), 1.0e-6);
        Assert.assertEquals(4.0, result.getValue(), 1.0e-6);
    }

    /**
     * Tests optimization of a function with a maximum at the boundary.
     */
    @Test
    public void testFunctionWithMaximumAtBoundary() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x;
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, 1, 10, 0);
        Assert.assertEquals(2.0, result.getPoint(), 1.0e-6);
        Assert.assertEquals(-4.0, result.getValue(), 1.0e-6);
    }

    /**
     * Tests optimization of a function with an infinite minimum.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testFunctionWithInfiniteMinimum() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        optimizer.optimize(function, GoalType.MINIMIZE, -1, 1, 0);
    }

    /**
     * Tests optimization of a function with an error.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionWithError() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("Division by zero");
            }
        };
        optimizer.optimize(function, GoalType.MINIMIZE, -1, 1, 0);
    }
}