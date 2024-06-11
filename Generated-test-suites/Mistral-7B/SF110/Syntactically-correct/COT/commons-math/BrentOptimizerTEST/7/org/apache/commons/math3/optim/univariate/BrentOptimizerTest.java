package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

import java.util.Optional;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        // Test the constructor with valid arguments
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertNotNull(optimizer);

        // Test the constructor with invalid relative tolerance
        try {
            new BrentOptimizer(-0.01, 0.001, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test the constructor with invalid absolute tolerance
        try {
            new BrentOptimizer(0.01, -0.001, null);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testDoOptimize_Minimization() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        Function function = new QuadraticFunction(2.0, 3.0, -1.0);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(function, 1.0, null);
        UnivariatePointValuePair result = optimizer.optimize(problem);

        assertEquals(0.0, result.getPoint(), 0.01);
        assertEquals(-3.0, result.getValue(), 0.01);
    }

    @Test
    public void testDoOptimize_Maximization() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        Function function = new QuadraticFunction(2.0, 3.0, 1.0);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(function, 1.0, null);
        UnivariatePointValuePair result = optimizer.optimize(problem);

        assertEquals(2.0, result.getPoint(), 0.01);
        assertEquals(7.0, result.getValue(), 0.01);
    }

    @Test
    public void testDoOptimize_InitialBracketing() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        Function function = new AbsFunction(1.0);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(function, 1.0, null);
        UnivariatePointValuePair result = optimizer.optimize(problem);

        assertEquals(0.0, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test
    public void testDoOptimize_Convergence() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        Function function = new QuadraticFunction(2.0, 3.0, 0.0);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(function, 1.0, null);
        UnivariatePointValuePair result = optimizer.optimize(problem);

        assertEquals(2.0, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test
    public void testDoOptimize_FunctionThrowsException() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        Function function = new Function() {
            @Override
            public double value(double x) throws MathIllegalArgumentException {
                if (x < 0.0) {
                    throw new MathIllegalArgumentException("x must be non-negative");
                }
                return Math.log(x);
            }
        };
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(function, 1.0, null);

        try {
            optimizer.optimize(problem);
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        }
    }

    @Test
    public void testDoOptimize_ConvergenceChecker() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair best) {
                return best.getValue() <= 1.0E-10;
            }
        });
        Function function = new QuadraticFunction(2.0, 3.0, 0.0);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(function, 1.0, null);
        UnivariatePointValuePair result = optimizer.optimize(problem);

        assertEquals(2.0, result.getPoint(), 0.01);
        assertEquals(0.0, result.getValue(), 1.0E-10);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(3.0, 4.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(5.0, 6.0);

        assertEquals(a, BrentOptimizer.this.best(null, a, true));
        assertEquals(b, BrentOptimizer.this.best(a, b, true));
        assertEquals(c, BrentOptimizer.this.best(b, c, true));

        assertEquals(a, BrentOptimizer.this.best(null, b, false));
        assertEquals(b, BrentOptimizer.this.best(a, c, false));
        assertEquals(c, BrentOptimizer.this.best(b, null, false));
    }

    private static class QuadraticFunction implements UnivariateFunction {
        private final double a;
        private final double b;
        private final double c;

        public QuadraticFunction(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public double value(double x) {
            return a * Math.pow(x, 2) + b * x + c;
        }
    }

    private static class AbsFunction implements UnivariateFunction {
        private final double value;

        public AbsFunction(double value) {
            this.value = value;
        }

        @Override
        public double value(double x) {
            return Math.abs(value);
        }
    }
}