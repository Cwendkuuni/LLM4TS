package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    /**
     * Test optimization with a simple function f(x) = x^2 for x in [-1,1].
     */
    @Test
    public void testSimpleFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0], 2.0);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{-1.0, -1.0});

        // Check the optimizer result.
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization with a simple function f(x) = x^2 for x in [-10,10].
     */
    @Test
    public void testSimpleFunctionLargeDomain() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0], 2.0);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{-10.0, -10.0});

        // Check the optimizer result.
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization with a non-convex function f(x) = x^2 for x in [-1,1]
     * but with a different goal type: maximization.
     */
    @Test
    public void testNonConvexFunctionMaximization() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0], 2.0);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, new double[]{-1.0, -1.0});

        // Check the optimizer result.
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization with a non-convex function f(x,y) = x^2 + y^2 for x,y in [-1,1]
     */
    @Test
    public void testNonConvexFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{-1.0, -1.0});

        // Check the optimizer result.
        assertEquals(1.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-1.0, -1.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization with a non-convex function f(x,y) = x^2 + y^2 for x,y in [-1,1]
     * but with a different goal type: maximization.
     */
    @Test
     public void testNonConvexFunctionMaximization() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, new double[]{-1.0, -1.0});

        // Check the optimizer result.
        assertEquals(1.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization with a non-differentiable function
     * f(x,y) = |x| + |y|.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNonDifferentiableFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.abs(x[0]) + Math.abs(x[1]);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, GoalType.MINIMIZE, new double[]{1.0, 1.0});
    }

    /**
     * Test optimization with a non-convex function
     * f(x,y) = |x| + |y| + sin(x) + sin(y).
     */
    @Test
    public void testNonConvexFunctionWithSin() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.abs(x[0]) + Math.abs(x[1]) + Math.sin(x[0]) + Math.sin(x[1]);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{-3.14, 2.71828});

        // Check the optimizer result.
        assertFalse(Double.isFinite(result.getValue()));
    }
}