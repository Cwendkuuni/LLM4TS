package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new QuadraticFunction(-1, 1, 0.25);
        UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0);
        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(-0.04, result.getValue(), 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(1e-5, -1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        new BrentOptimizer(1e-9, 1e-5);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iter > 100;
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, checker);
        UnivariateFunction function = new QuadraticFunction(-1, 1, 0.25);
        optimizer.optimize(function, -2.0, 2.0);
        assertFalse(optimizer.isConverged());
    }

    @Test
    public void testBrentOptimizerEmptyInitialInterval() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new LinearFunction(1, 1);
        try {
            optimizer.optimize(function, 0.0, 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertNull(optimizer.getResult());
    }

    @Test
    public void testBrentOptimizerNoImprovement() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new ConstantFunction(0.5);
        optimizer.optimize(function, 0.0, 1.0);
        UnivariatePointValuePair result = optimizer.getResult();
        assertEquals(0.5, result.getPoint(), 1e-8);
        assertEquals(0.5, result.getValue(), 1e-8);
    }

    @Test
    public void testBrentOptimizerNoImprovement2() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new LinearFunction(-1, 1);
        optimizer.optimize(function, 0.0, 1.0);
        UnivariatePointValuePair result = optimizer.getResult();
        assertEquals(-1.0, result.getPoint(), 1e-8);
        assertEquals(-1.0, result.getValue(), 1e-8);
    }

    @Test
    public void testBrentOptimizerNoImprovement3() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new LinearFunction(1, 1);
        optimizer.optimize(function, -1.0, 0.0);
        UnivariatePointValuePair result = optimizer.getResult();
        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    @Test
    public void testBrentOptimizerNoImprovement4() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new LinearFunction(1, -1);
        optimizer.optimize(function, 0.0, 1.0);
        UnivariatePointValuePair result = optimizer.getResult();
        assertEquals(1.0, result.getPoint(), 1e-8);
        assertEquals(1.0, result.getValue(), 1e-8);
    }

    @Test
    public void testBrentOptimizerErrorFunction() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new ErrorFunction();
        try {
            optimizer.optimize(function, -1.0, 1.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertNull(optimizer.getResult());
    }
}

class QuadraticFunction implements UnivariateFunction {

    private double a;
    private double b;
    private double c;

    public QuadraticFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double value(double x) {
        return a * x * x + b * x + c;
    }
}

class LinearFunction implements UnivariateFunction {

    private double m;
    private double b;

    public LinearFunction(double m, double b) {
        this.m = m;
        this.b = b;
    }

    @Override
    public double value(double x) {
        return m * x + b;
    }
}

class ConstantFunction implements UnivariateFunction {

    private double value;

    public ConstantFunction(double value) {
        this.value = value;
    }

    @Override
    public double value(double x) {
        return value;
    }
}

class ErrorFunction implements UnivariateFunction {

    @Override
    public double value(double x) {
        throw new RuntimeException("Error function not defined");
    }
}