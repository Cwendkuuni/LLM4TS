package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithMinimum() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariatePointValuePair solution = optimizer.optimize(f, 0.0, 4.0);
        assertEquals("The solution should be close to 2.0", 2.0, solution.getPoint(), 1.0e-6);
        assertEquals("The optimal value should be 0.0", 0.0, solution.getValue(), 1.0e-6);
    }

    @Test
    public void testBrentOptimizerWithMaximum() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 5.0, 2.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariatePointValuePair solution = optimizer.optimize(f, 0.0, 10.0);
        assertEquals("The solution should be close to 5.0", 5.0, solution.getPoint(), 1.0e-6);
        assertEquals("The optimal value should be 0.0", 25.0, solution.getValue(), 1.0e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 1.0e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNullConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
    }

    @Test
    public void testBrentOptimizerWithEmptyRange() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        try {
            optimizer.optimize(f, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}