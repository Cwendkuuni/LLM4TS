package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonlinearOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.GoalType;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.StopCondition;
import org.apache.commons.math3.special.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithZeroAbsoluteThreshold() {
        new BrentOptimizer(0.1, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithRelativeThresholdBelowMinimum() {
        new BrentOptimizer(0.01, 0.01);
    }

    @Test
    public void testBrentOptimizerWithNullConvergenceChecker() {
        UnivariateFunction f = x -> FastMath.pow(x, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        PointValuePair solution = optimizer.optimize(f, 1.0, 2.0, null);
        assertEquals(1.0, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        UnivariateFunction f = x -> FastMath.pow(x, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, (p, v) -> p.getValue() <= v.getValue());
        PointValuePair solution = optimizer.optimize(f, 1.0, 2.0, null);
        assertEquals(1.0, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
    }

    @Test
    public void testBrentOptimizerMinimization() {
        UnivariateFunction f = x -> FastMath.pow(x - 2.0, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        PointValuePair solution = optimizer.optimize(f, 0.0, 5.0, null);
        assertEquals(2.0, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        UnivariateFunction f = x -> FastMath.pow(x - 2.0, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, GoalType.MAXIMIZE);
        PointValuePair solution = optimizer.optimize(f, 0.0, 5.0, null);
        assertEquals(4.0, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
    }

    @Test
    public void testBrentOptimizerCustomInitialGuess() {
        UnivariateFunction f = x -> FastMath.pow(x - 2.0, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        PointValuePair solution = optimizer.optimize(f, 3.0, 5.0, new InitialGuess() {
            @Override
            public double getInitialGuess() {
                return 3.5;
            }
        });
        assertEquals(3.5, solution.getPoint(), 1.0e-5);
        assertEquals(4.12250372816154, solution.getValue(), 1.0e-5);
    }

    @Test
    public void testBrentOptimizerCustomMaxEval() {
        UnivariateFunction f = x -> FastMath.pow(x - 2.0, 2);
        MaxEval maxEval = new MaxEval(5);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        PointValuePair solution = optimizer.optimize(f, 0.0, 5.0, maxEval);
        assertEquals(2.0, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
    }

    @Test
    public void testBrentOptimizerWithDifferentBounds() {
        UnivariateFunction f = x -> FastMath.pow(x - 2.0, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        PointValuePair solution = optimizer.optimize(f, -1.0, 3.0, null);
        assertEquals(1.0, solution.getPoint(), 1.0e-5);
        assertEquals(1.0, solution.getValue(), 1.0e-5);
    }

    @Test
    public void testBrentOptimizerConvergence() {
        UnivariateFunction f = x -> FastMath.pow(x - 2.0, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, new StopCondition() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return FastMath.abs(current.getValue() - previous.getValue()) < 1.0e-5;
            }
        });
        PointValuePair solution = optimizer.optimize(f, 0.0, 5.0, null);
        assertEquals(2.0, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
        assertTrue(optimizer.isConverged());
    }

    @Test
    public void testBrentOptimizerWithDifferentGoalType() {
        UnivariateFunction f = x -> FastMath.pow(x - 2.0, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, GoalType.MAXIMIZE);
        PointValuePair solution = optimizer.optimize(f, 0.0, 5.0, null);
        assertEquals(4.0, solution.getPoint(), 1.0e-5);
        assertEquals(0.0, solution.getValue(), 1.0e-5);
    }

    @Test
    public void testBrentOptimizerWithDifferentRelativeThreshold() {
        UnivariateFunction f = x -> FastMath.pow(x - 2.0, 2);
        BrentOptimizer optimizer1 = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        PointValuePair solution1 = optimizer1.optimize(f, 0.0, 5.0, null);
        assertEquals(2.0, solution1.getPoint(), 1.0e-6);
        assertEquals(0.0, solution1.getValue(), 1.0e-6);

        BrentOptimizer optimizer2 = new BrentOptimizer(1.0e-7, 1.0e-6, null);
        PointValuePair solution2 = optimizer2.optimize(f, 0.0, 5.0, null);
        assertEquals(2.0, solution2.getPoint(), 1.0e-7);
        assertEquals(0.0, solution2.getValue(), 1.0e-7);

        assertArrayEquals(new double[]{2.0, 0.0}, optimizer1.getLastStep(), 1.0e-6);
        assertArrayEquals(new double[]{2.0, 0.0}, optimizer2.getLastStep(), 1.0e-7);
    }

    @Test
    public void testBrentOptimizerWithDifferentAbsoluteThreshold() {
        UnivariateFunction f = x -> FastMath.pow(x - 2.0, 2);
        BrentOptimizer optimizer1 = new BrentOptimizer(1.0e-6, 1.0e-5, null);
        PointValuePair solution1 = optimizer1.optimize(f, 0.0, 5.0, null);
        assertEquals(2.0, solution1.getPoint(), 1.0e-6);
        assertEquals(0.0, solution1.getValue(), 1.0e-6);

        BrentOptimizer optimizer2 = new BrentOptimizer(1.0e-6, 1.0e-4, null);
        PointValuePair solution2 = optimizer2.optimize(f, 0.0, 5.0, null);
        assertEquals(2.0, solution2.getPoint(), 1.0e-6);
        assertEquals(0.0, solution2.getValue(), 1.0e-4);

        assertArrayEquals(new double[]{2.0, 0.0}, optimizer1.getLastStep(), 1.0e-6);
        assertArrayEquals(new double[]{2.0, 0.0}, optimizer2.getLastStep(), 1.0e-6);
    }

    @Test
    public void testBrentOptimizerWithDifferentConvergenceChecker() {
        UnivariateFunction f = x -> FastMath.pow(x - 2.0, 2);
        BrentOptimizer optimizer1 = new BrentOptimizer(1.0e-5, 1.0e-5, (p1, p2) -> FastMath.abs(p1.getValue() - p2.getValue()) < 1.0e-5);
        PointValuePair solution1 = optimizer1.optimize(f, 0.0, 5.0, null);
        assertEquals(2.0, solution1.getPoint(), 1.0e-5);
        assertEquals(0.0, solution1.getValue(), 1.0e-5);

        BrentOptimizer optimizer2 = new BrentOptimizer(1.0e-5, 1.0e-5, (p1, p2) -> FastMath.abs(p1.getValue() - p2.getValue()) < 1.0e-6);
        PointValuePair solution2 = optimizer2.optimize(f, 0.0, 5.0, null);
        assertEquals(2.0, solution2.getPoint(), 1.0e-5);
        assertEquals(0.0, solution2.getValue(), 1.0e-6);
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsException() {
        UnivariateFunction f = x -> {
            throw new IllegalArgumentException("Invalid input");
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        try {
            optimizer.optimize(f, 0.0, 5.0, null);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
            assertEquals("Invalid input", e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizerWithFunctionReturnsNaN() {
        UnivariateFunction f = x -> FastMath.NaN;
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        try {
            optimizer.optimize(f, 0.0, 5.0, null);
            fail("Should have thrown an exception");
        } catch (IllegalOptimizationException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.isNaN());
        }
    }

    @Test
    public void testBrentOptimizerWithFunctionReturnsInfiniteValue() {
        UnivariateFunction f = x -> Double.POSITIVE_INFINITY;
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        try {
            optimizer.optimize(f, 0.0, 5.0, null);
            fail("Should have thrown an exception");
        } catch (IllegalOptimizationException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.isInfinite());
        }
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsArithmeticException() {
        UnivariateFunction f = x -> 1.0 / (x - 2.0);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        try {
            optimizer.optimize(f, 0.0, 5.0, null);
            fail("Should have thrown an exception");
        } catch (ArithmeticException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getClass() == ArithmeticException.class);
        }
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsRuntimeException() {
        UnivariateFunction f = x -> {
            throw new RuntimeException("Function error");
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        try {
            optimizer.optimize(f, 0.0, 5.0, null);
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            assertNotNull(e.getMessage());
            assertEquals("Function error", e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsUnknownException() {
        UnivariateFunction f = x -> {
            throw new UnsupportedOperationException("Unsupported function");
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        try {
            optimizer.optimize(f, 0.0, 5.0, null);
            fail("Should have thrown an exception");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e.getMessage());
            assertEquals("Unsupported function", e.getMessage());
        }
    }
}