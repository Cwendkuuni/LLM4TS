package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testMinimization() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-3);
        PointValuePair result = optimizer.optimize(function, -2.0, 2.0);
        assertEquals(-1.8724519125435882, result.getPoint(), 1e-6);
    }

    @Test
    public void testMaximization() {
        UnivariateFunction function = f -> Math.exp(f);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-3);
        PointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(1.7182818284590451, result.getPoint(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-3);
        optimizer.optimize(null, -1.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInterval() {
        UnivariateFunction function = f -> f * f;
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-3);
        optimizer.optimize(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testConvergenceChecker() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return current.getValue() < Precision.EPSILON;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(function, checker);
        PointValuePair result = optimizer.optimize(function, -2.0, 2.0);
        assertEquals(-1.8724519125435882, result.getPoint(), 1e-6);
    }

    @Test
    public void testAbsNegThreshold() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-3, 1.0e-6);
        PointValuePair result = optimizer.optimize(function, -2.0, 2.0);
        assertEquals(-1.8724519125435882, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-1.0e-3, 1.0e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-8, 1.0e-3);
    }

    @Test
    public void testGoldenSectionStep() {
        UnivariateFunction function = f -> E.naturalExp(f);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-3);
        PointValuePair result = optimizer.optimize(function, 3.0, 5.0);
        assertTrue(3.0 < result.getPoint() && result.getPoint() < 5.0);
    }

    @Test
    public void testParabolicInterpolation() {
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-3);
        PointValuePair result = optimizer.optimize(function, -2.0, 2.0);
        assertEquals(-1.8724519125435882, result.getPoint(), 1e-6);
    }
}