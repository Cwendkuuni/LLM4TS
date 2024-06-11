package org.apache.commons.math3.optim.univariate.test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_Minimization() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 4.0);
        assertEquals(2.0, result.getPoint(), 1.0e-6);
        assertEquals(0.0, result.getValue(), 1.0e-6);
    }

    @Test
    public void testBrentOptimizer_Minimization_ConvergenceChecker() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() == 0.0;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 4.0);
        assertEquals(2.0, result.getPoint(), 1.0e-6);
        assertEquals(0.0, result.getValue(), 1.0e-6);
        checker.assertConverged(1);
    }

    @Test
    public void testBrentOptimizer_Maximization() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 2.0, 2.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 4.0);
        assertEquals(2.0, result.getPoint(), 1.0e-6);
        assertEquals(-0.0, result.getValue(), 1.0e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_InvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_InvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-1.0e-6, 1.0e-6);
    }

    @Test
    public void testBrentOptimizer_RangeOutOfBounds() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        try {
            optimizer.optimize(f, -1.0, 0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizer.optimize(f, 4.0, -1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}