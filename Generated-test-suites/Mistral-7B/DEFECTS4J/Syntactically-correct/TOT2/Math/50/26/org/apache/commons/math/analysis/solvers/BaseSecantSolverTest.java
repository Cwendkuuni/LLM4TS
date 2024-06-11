package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

import org.junit.Test;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Verifies that a solver throws an exception when the function value
     * is too small to be useful.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Precision.SAFE_MIN;
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-9, BaseSecantSolver.Method.ILLINOIS);
        try {
            solver.solve(10, function, 0.0, 1.0, 0.0);
        } catch (final MaxEvalException | NoBracketingException | UnsupportedOperationException e) {
            // Expected exceptions
        }
    }

    /**
     * Verifies that a solver throws an exception when the function is not
     * bracketing.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Math.sin(x);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-9, BaseSecantSolver.Method.ILLINOIS);
        try {
            solver.solve(10, function, 0.0, 0.0, 0.0);
        } catch (final MaxEvalException | NumberIsTooSmallException e) {
            // Expected exceptions
        }
    }

    /**
     * Verifies that a solver throws an exception when an invalid method is
     * provided.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Math.exp(Math.sin(x)) - Math.exp(-Math.sin(x));
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-9, BaseSecantSolver.Method.FOO);
        try {
            solver.solve(10, function, 0.0, 1.0, 0.0);
        } catch (final MaxEvalException | NoBracketingException | NumberIsTooSmallException e) {
            // Expected exceptions
        }
    }

    /**
     * Verifies that a solver returns the correct solution for a simple
     * function.
     */
    @Test
    public void testSimpleFunction() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return x * x - 2.0;
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-9, BaseSecantSolver.Method.ILLINOIS);
        final double solution = solver.solve(10, function, -1.5, 1.5, 0.0);
        final double expected = -1.0;
        assertEquals(expected, solution, 1.0e-12);
    }

    /**
     * Verifies that a solver returns the correct solution for a function
     * with multiple roots.
     */
    @Test
    public void testMultipleRoots() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Math.pow(Math.sin(Math.PI * x), 2.0);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-9, BaseSecantSolver.Method.ILLINOIS);
        final double solution = solver.solve(10, function, -1.5, 1.5, 0.0);
        final double expected = 0.0;
        assertEquals(expected, solution, 1.0e-12);
    }

    /**
     * Verifies that a solver returns the correct solution for a function
     * with a very large root.
     */
    @Test
    public void testLargeRoot() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Math.exp(Math.log(x) - 1.0);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-9, BaseSecantSolver.Method.ILLINOIS);
        final double solution = solver.solve(10, function, 1.0, 1.0e10, 1.0);
        final double expected = Math.E;
        assertEquals(expected, solution, 1.0e-12);
    }

    /**
     * Verifies that a solver returns the correct solution for a function
     * with a very small root.
     */
    @Test
    public void testSmallRoot() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Math.pow(Math.PI, x) - Math.sin(Math.PI * x);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-21, BaseSecantSolver.Method.ILLINOIS);
        final double solution = solver.solve(10, function, 0.0, 1.0, 0.0);
        final double expected = Math.PI / 2.0;
        assertEquals(expected, solution, 1.0e-23);
    }

    /**
     * Verifies that a solver throws an exception when the maximum number
     * of evaluations is reached.
     */
    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                return Math.pow(Math.sin(Math.PI * x), 2.0);
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(1.0e-9, BaseSecantSolver.Method.ILLINOIS);
        try {
            solver.solve(1, function, -1.5, 1.5, 0.0);
        } catch (final NoBracketingException | NumberIsTooSmallException e) {
            // Expected exceptions
        }
    }
}