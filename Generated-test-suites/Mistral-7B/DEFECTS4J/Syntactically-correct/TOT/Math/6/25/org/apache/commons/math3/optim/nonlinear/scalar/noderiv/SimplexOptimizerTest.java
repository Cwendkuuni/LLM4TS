package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimpleValueChecker.Assessor;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeStrategy;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunctionMappingAdapter;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NODerivFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NODerivSimpleValueChecker;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /**
     * Test function f(x, y) = x^2 + y^2.
     */
    @Test
    public void testSimpleFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-10, 1e-12));
        PointValuePair solution = optimizer.optimize(100, f, GoalType.MINIMIZE, null);
        Assert.assertEquals(0.0, solution.getValue(), 1e-12);
        Assert.assertArrayEquals(new double[] { 0.0, 0.0 }, solution.getPoint(), 1e-12);
    }

    /**
     * Test function f(x, y) = |x| + |y|.
     */
    @Test
    public void testAbsoluteValueFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.abs(x[0]) + Math.abs(x[1]);
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-4, 1e-6));
        PointValuePair solution = optimizer.optimize(100, f, GoalType.MINIMIZE, null);
        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] { 0.0, 0.0 }, solution.getPoint(), 1e-6);
    }

    /**
     * Test a function with multiple minima.
     */
    @Test
    public void testFunctionWithMultipleMinima() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return (x[0] - 2.0) * (x[0] - 2.0) + (x[1] - 1.0) * (x[1] - 1.0);
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-8));
        PointValuePair solution = optimizer.optimize(100, f, GoalType.MINIMIZE, null);
        Assert.assertEquals(3.0, solution.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[] { 2.0, 1.0 }, solution.getPoint(), 1e-8);
    }

    /**
     * Test a function with multiple maxima.
     */
    @Test
    public void testFunctionWithMultipleMaxima() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return -(x[0] * x[0] + x[1] * x[1]);
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-8));
        PointValuePair solution = optimizer.optimize(100, f, GoalType.MAXIMIZE, null);
        Assert.assertEquals(-3.0, solution.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[] { -2.0, -1.0 }, solution.getPoint(), 1e-8);
    }

    /**
     * Test a function with an empty simplex.
     */
    @Test(expected = NullArgumentException.class)
    public void testEmptySimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1, new MultivariateFunction() {
            public double value(double[] x) {
                return 0.0;
            }
        }, GoalType.MINIMIZE, null);
    }

    /**
     * Test a function with bounds.
     */
    @Test
    public void testFunctionWithBounds() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        MultivariateFunctionMappingAdapter functionWithBoxConstraints = new MultivariateFunctionMappingAdapter(f,
                new double[][] { { -5.0, -5.0 }, { 5.0, 5.0 } });

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-10, 1e-12));
        PointValuePair solution = optimizer.optimize(100, functionWithBoxConstraints, GoalType.MINIMIZE, null);
        Assert.assertEquals(25.0, solution.getValue(), 1e-12);
        Assert.assertArrayEquals(new double[] { -2.5, -2.5 }, solution.getPoint(), 1e-12);
    }

    /**
     * Test a function with a constraint.
     */
    @Test
    public void testFunctionWithConstraint() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                if (x[0] < 0.0) {
                    throw new IllegalArgumentException("x[0] is negative");
                }
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new NODerivSimpleValueChecker(1e-10, 1e-12));
        PointValuePair solution = optimizer.optimize(100, f, GoalType.MINIMIZE, null);
        Assert.assertEquals(0.0, solution.getValue(), 1e-12);
        Assert.assertArrayEquals(new double[] { 0.0, 0.0 }, solution.getPoint(), 1e-12);
    }

    /**
     * Test a function with derivative-free optimization.
     */
    @Test
    public void testDerivativeFreeFunction() {
        MultivariateFunction f = new NODerivFunction(new MultivariateFunction() {
            public double value(double[] x) {
                return Math.sin(x[0]) * Math.cos(x[1]);
            }
        });

        SimplexOptimizer optimizer = new CMAESOptimizer();
        PointValuePair solution = optimizer.optimize(100, f, GoalType.MAXIMIZE, null);
        Assert.assertTrue(Double.isFinite(solution.getValue()));
        Assert.assertArrayEquals(new double[] { 0.0, 0.0 }, solution.getPoint(), 1e-6);
    }
}