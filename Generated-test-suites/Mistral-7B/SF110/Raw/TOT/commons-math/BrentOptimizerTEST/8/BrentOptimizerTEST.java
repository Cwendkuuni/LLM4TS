package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_minimize() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5); // relative and absolute tolerances
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
        double expected = 2.0;
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(expected, result.getValue(), 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_minimize_smallRelativeTolerance() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-9, 1e-5); // relative tolerance too small
        try {
            optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_minimize_negativeAbsoluteTolerance() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, -1e-5); // absolute tolerance negative
        try {
            optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizer_minimize_convergenceChecker() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x - 2.0, 2.0) * FastMath.sin(FastMath.abs(x - 2.0));
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new MyConvergenceChecker());
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
        double expected = 2.0;
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(expected, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_maximize() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 2.0, 2.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5); // relative and absolute tolerances
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MAXIMIZE);
        double expected = 2.0;
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(-expected, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_maximize_goalType_minimize() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 2.0, 2.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5); // relative and absolute tolerances
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
        double expected = 2.0;
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(-expected, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_maximize_boundaries_crossed() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -(x * x) * (x - 2.0) * (x - 3.0);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5); // relative and absolute tolerances
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MAXIMIZE);
        double expected = 3.0;
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(expected * expected * (expected - 2.0), result.getValue(), 1e-6);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iterationNumber, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
            return current.getValue() - previous.getValue() < 1e-6;
        }
    }
}