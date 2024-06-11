package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Test case for {@link PowellOptimizer#PowellOptimizer(double, double)}
     * constructor with default convergence checker.
     */
    @Test
    public void testDefaultConstructor() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-7, 1e-8);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, new double[] {1.0, 1.0});
        Assert.assertEquals(0.0, result.getValue(), Precision.epsEpsilon(1e-8));
        Assert.assertArrayEquals(new double[] {0.0, 0.0}, result.getPoint(), Precision.epsEpsilon(1e-8));
    }

    /**
     * Test case for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}
     * constructor with custom convergence checker.
     */
    @Test
    public void testCustomConstructor() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.sin(x[0]) + Math.sin(x[1]);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-6, new MyCustomConvergenceChecker());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, new double[] {0.0, 0.0});
        Assert.assertEquals(0.0, result.getValue(), Precision.epsEpsilon(1e-6));
        Assert.assertArrayEquals(new double[] {-1.0, 0.0}, result.getPoint(), Precision.epsEpsilon(1e-6));
    }

    /**
     * Test case for edge case with negative numbers.
     */
    @Test
    public void testNegativeNumbers() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] - x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-7);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, new double[] {-1.0, -1.0});
        Assert.assertEquals(1.0, result.getValue(), Precision.epsEpsilon(1e-6));
        Assert.assertArrayEquals(new double[] {-1.0, -1.0}, result.getPoint(), Precision.epsEpsilon(1e-6));
    }

    /**
     * Test case for error scenarios: non-quadratic function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonQuadraticFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.sin(x[0]) + Math.cos(x[1]);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-7);
        optimizer.optimize(f, GoalType.MINIMIZE, new double[] {0.0, 0.0});
    }
}

class MyCustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {

    @Override
    public boolean converged(int iterationCount, PointValuePair previous, PointValuePair current) {
        double fPrev = previous.getValue();
        double fCurr = current.getValue();
        double maxError = Math.max(Math.abs(fPrev - fCurr), 1.0);
        return maxError < 1e-5;
    }
}