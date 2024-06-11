package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        try {
            new BrentOptimizer(-0.1, 0.0);
            fail("A negative relative tolerance is not allowed.");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            new BrentOptimizer(0.0, -0.1);
            fail("A negative absolute tolerance is not allowed.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new BrentOptimizer(Double.POSITIVE_INFINITY, 0.0);
            fail("The relative tolerance must be finite.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new BrentOptimizer(0.1, Double.NEGATIVE_INFINITY);
            fail("The absolute tolerance must be non-negative.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new BrentOptimizer(Double.NaN, 0.0);
            fail("The relative tolerance must be a number.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new BrentOptimizer(0.1, Double.NaN);
            fail("The absolute tolerance must be a number.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new BrentOptimizer(0.1, 0.1, null);
            fail("A valid ConvergenceChecker must be provided.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new DefaultMaxEval());
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double arg) {
                return Math.pow(arg, 3) - Math.pow(arg, 2) + 1;
            }
        };

        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, 1.0);
        OptimizationResult result = optimizer.optimize(problem);

        assertEquals(0.0, result.getPoint().getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new DefaultMinEval());
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double arg) {
                return Math.pow(arg, 3) + Math.pow(arg, 2) - 1;
            }
        };

        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, 1.0);
        OptimizationResult result = optimizer.optimize(problem);

        assertEquals(1.0, result.getPoint().getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizationWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double arg) {
                return Math.pow(arg, 3) - Math.pow(arg, 2) + 1;
            }
        };

        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, 1.0);
        OptimizationResult result = optimizer.optimize(problem);

        assertEquals(0.0, result.getPoint().getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizationWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double arg) {
                return Math.pow(arg, 3) + Math.pow(arg, 2) - 1;
            }
        };

        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, 1.0);
        OptimizationResult result = optimizer.optimize(problem);

        assertEquals(1.0, result.getPoint().getValue(), 1e-6);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
            return Math.abs(current.getValue() - previous.getValue()) < 1e-8;
        }
    }
}