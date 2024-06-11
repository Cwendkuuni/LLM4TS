package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test optimization with a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = {1.0, 1.0};
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double[] point) {
                return FastMath.pow(point[0], 2) + FastMath.pow(point[1], 2);
            }
        };

        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(new PointValuePair(new double[]{0.0, 0.0}, 0.0), result, 1e-8);
    }

    /**
     * Test optimization with a concave function.
     */
    @Test
    public void testConcaveFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        double[] startPoint = {1.0, 1.0};
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double[] point) {
                return FastMath.pow(point[0] - 1.0, 2) * FastMath.sin(FastMath.PI * point[1]);
            }
        };

        try {
            optimizer.optimize(function, startPoint);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    /**
     * Test optimization with a flat function.
     */
    @Test
    public void testFlatFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10);
        double[] startPoint = {0.0, 0.0};
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double[] point) {
                return FastMath.pow(100.0, -FastMath.pow(FastMath.E, 10.0)) * FastMath.sin(FastMath.PI);
            }
        };

        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(new PointValuePair(new double[]{0.0, 0.0}, 0.0), result, 1e-12);
    }
}