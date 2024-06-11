package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_minimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = f -> Math.pow(f, 2);
        double minimum = 0.0;
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(minimum, result.getPoint(), 1e-6);
        assertEquals(-minimum, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_maximum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = f -> Math.exp(-f);
        double maximum = E.E;
        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0, GoalType.MAXIMIZE);
        assertEquals(maximum, result.getPoint(), 1e-6);
        assertEquals(maximum, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_convergenceCheck() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5) {
            @Override
            protected boolean checkConverged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return previous.getValue() - current.getValue() < 1e-6;
            }
        };
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2);
        double minimum = 0.0;
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(minimum, result.getPoint(), 1e-6);
        assertEquals(-minimum, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_invalidAbsoluteThreshold() {
        new BrentOptimizer(1e-5, -1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_invalidRelativeThreshold() {
        new BrentOptimizer(1e-5, 1e-7);
    }

    @Test
    public void testBrentOptimizer_minimum_noImprovement() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = f -> Math.pow(f, 2);
        double minimum = 0.0;
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 0.0);
        assertEquals(minimum, result.getPoint(), 1e-6);
        assertEquals(-minimum, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_maximum_noImprovement() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = f -> Math.exp(-f);
        double maximum = E.E;
        UnivariatePointValuePair result = optimizer.optimize(function, 10.0, 10.0, GoalType.MAXIMIZE);
        assertEquals(maximum, result.getPoint(), 1e-6);
        assertEquals(maximum, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_minimum_edgeCase1() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = f -> Math.pow(f, 3) - 2 * f;
        double minimum = -1.0;
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(minimum, result.getPoint(), 1e-6);
        assertEquals(-minimum, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_maximum_edgeCase1() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = f -> Math.pow(f, 3) + 2 * f;
        double maximum = 1.0;
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MAXIMIZE);
        assertEquals(maximum, result.getPoint(), 1e-6);
        assertEquals(maximum, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_minimum_edgeCase2() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = f -> 3 * Math.pow(Math.abs(f), 0.3) - 2 * Math.abs(f);
        double minimum = -1.0;
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(minimum, result.getPoint(), 1e-6);
        assertEquals(-minimum, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_maximum_edgeCase2() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = f -> 3 * Math.pow(Math.abs(f), 0.3) + 2 * Math.abs(f);
        double maximum = 1.0;
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MAXIMIZE);
        assertEquals(maximum, result.getPoint(), 1e-6);
        assertEquals(maximum, result.getValue(), 1e-6);
    }
}