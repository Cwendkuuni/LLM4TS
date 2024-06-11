package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerSimple() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(f, 0, 1);

        assertEquals(0.0, result.getPoint(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(null, 0, 1);
    }

    @Test
    public void testBrentOptimizerEmptyInterval() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        assertNull(result);
    }

    @Test
    public void testBrentOptimizerMaximum() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.exp(-x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(f, 0, 0);

        assertEquals(0.0, result.getPoint(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimum() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(f, -5, 1);

        assertEquals(1.0, result.getPoint(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6);
    }

    @Test
    public void testBrentOptimizerAbsoluteThresholdOnly() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.0, 1e-6);
        PointValuePair result = optimizer.optimize(f, -5, 1);

        assertEquals(1.0, result.getPoint(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerRelativeThresholdOnly() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
        PointValuePair result = optimizer.optimize(f, -5, 1);

        assertEquals(1.0, result.getPoint(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }
}