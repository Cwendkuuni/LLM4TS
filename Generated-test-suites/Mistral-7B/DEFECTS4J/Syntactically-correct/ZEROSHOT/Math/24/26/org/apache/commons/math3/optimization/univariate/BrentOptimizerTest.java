package org.apache.commons.math3.optimization.univariate;

import static org.apache.commons.math3.util.Precision.equals;
import static org.apache.commons.math3.util.Precision.isClose;
import static org.apache.commons.math3.util.Precision.isZero;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        double rel = 1e-5;
        double abs = 1e-6;
        optimizer = new BrentOptimizer(rel, abs);
        function = x -> x * x;
    }

    @Test
    public void testEmptyConstructor() {
        try {
            new BrentOptimizer();
            fail("Expected NotStrictlyPositiveException or NumberIsTooSmallException");
        } catch (Exception e) {
            if (e instanceof NotStrictlyPositiveException || e instanceof NumberIsTooSmallException) {
                // Expected exceptions
            } else {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test
    public void testConstructorWithRelAndAbs() {
        optimizer = new BrentOptimizer(1e-4, 1e-7);
        assertEquals(1e-4, optimizer.getRelativeThreshold(), 1e-7);
        assertEquals(1e-7, optimizer.getAbsoluteThreshold(), 1e-9);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbs() {
        new BrentOptimizer(1e-5, -1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithNegativeRel() {
        new BrentOptimizer(-1e-5, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroAbs() {
        new BrentOptimizer(1e-5, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithZeroRel() {
        new BrentOptimizer(0, 1e-6);
    }

    @Test
    public void testDoOptimize_minimize() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        assertEquals(1.25, result.getPoint(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
        assertTrue(optimizer.isConverged());
        assertEquals(MaxEval.CONVERGED, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_minimize_startValueInInterval() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        optimizer = new BrentOptimizer(1e-4, 1e-6, null);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        assertEquals(1.25, result.getPoint(), 1e-5);
        assertEquals(-1.0, result.getValue(), 1e-6);
        assertTrue(optimizer.isConverged());
        assertEquals(MaxEval.CONVERGED, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_minimize_startValueOutsideInterval() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        optimizer = new BrentOptimizer(1e-4, 1e-6, null);
        optimizer.setStartValue(0);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        assertEquals(1.0, result.getPoint(), 1e-5);
        assertEquals(-1.0, result.getValue(), 1e-6);
        assertTrue(optimizer.isConverged());
        assertEquals(MaxEval.CONVERGED, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_minimize_notConverged() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        optimizer = new BrentOptimizer(1e-1, 1e-6, null);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        assertFalse(optimizer.isConverged());
        assertEquals(MaxEval.ITERATIONS_EXCEEDED, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_minimize_functionThrowsException() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        function = x -> { throw new RuntimeException("test"); };
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        assertFalse(optimizer.isConverged());
        assertEquals(MaxEval.FUNCTION_ERROR, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_minimize_convergenceChecker() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, previous, current) -> {
            if (iter > 5) {
                return true;
            } else {
                return false;
            }
        };
        optimizer = new BrentOptimizer(1e-4, 1e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        assertEquals(1.25, result.getPoint(), 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
        assertFalse(optimizer.isConverged());
        assertEquals(MaxEval.ITERATIONS_REACHED, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_minimize_convergenceChecker_converged() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, previous, current) -> {
            if (isClose(previous.getValue(), current.getValue(), 1e-5)) {
                return true;
            } else {
                return false;
            }
        };
        optimizer = new BrentOptimizer(1e-4, 1e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
        assertEquals(1.25, result.getPoint(), 1e-5);
        assertEquals(-1.0, result.getValue(), 1e-6);
        assertTrue(optimizer.isConverged());
        assertEquals(MaxEval.CONVERGED, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_maximize() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        UnivariatePointValuePair result = optimizer.optimize(x -> -x * x, bounds, GoalType.MAXIMIZE);
        assertEquals(1.25, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
        assertTrue(optimizer.isConverged());
        assertEquals(MaxEval.CONVERGED, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_maximize_notConverged() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        optimizer = new BrentOptimizer(1e-1, 1e-6, null);
        UnivariatePointValuePair result = optimizer.optimize(x -> -x * x, bounds, GoalType.MAXIMIZE);
        assertFalse(optimizer.isConverged());
        assertEquals(MaxEval.ITERATIONS_EXCEEDED, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_maximize_functionThrowsException() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        function = x -> { throw new RuntimeException("test"); };
        UnivariatePointValuePair result = optimizer.optimize(function, bounds, GoalType.MAXIMIZE);
        assertFalse(optimizer.isConverged());
        assertEquals(MaxEval.FUNCTION_ERROR, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_maximize_convergenceChecker() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, previous, current) -> {
            if (iter > 5) {
                return true;
            } else {
                return false;
            }
        };
        optimizer = new BrentOptimizer(1e-4, 1e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(x -> -x * x, bounds, GoalType.MAXIMIZE);
        assertEquals(1.25, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
        assertFalse(optimizer.isConverged());
        assertEquals(MaxEval.ITERATIONS_REACHED, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_maximize_convergenceChecker_converged() {
        SimpleBounds bounds = new SimpleBounds(1, 2);
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, previous, current) -> {
            if (isClose(previous.getValue(), current.getValue(), 1e-5)) {
                return true;
            } else {
                return false;
            }
        };
        optimizer = new BrentOptimizer(1e-4, 1e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(x -> -x * x, bounds, GoalType.MAXIMIZE);
        assertEquals(1.25, result.getPoint(), 1e-5);
        assertEquals(1.0, result.getValue(), 1e-6);
        assertTrue(optimizer.isConverged());
        assertEquals(MaxEval.CONVERGED, optimizer.getStatus());
    }

    @Test
    public void testDoOptimize_goalTypeNull() {
        try {
            optimizer.optimize(function, new SimpleBounds(1, 2), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize_boundsNull() {
        try {
            optimizer.optimize(function, null, GoalType.MINIMIZE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}