package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    @Test
    public void testSimpleMinimization() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FastMath.pow(x[0] - 1.0, 2.0) + FastMath.pow(x[1] - 1.0, 2.0);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, new double[]{3.0, 3.0});

        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(-1e-6, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testZeroAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(null, new double[]{0.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStartingPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        }, null);
    }

    @Test
    public void testCustomConvergenceChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.sin(x[0]) * Math.sin(x[1]);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-8, new MyConvergenceChecker());
        PointValuePair result = optimizer.optimize(function, new double[]{1.0, 1.0});

        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            // Custom convergence check, e.g. based on function value trend.
            return FastMath.abs(current.getValue() - previous.getValue()) < 1e-6;
        }
    }
}