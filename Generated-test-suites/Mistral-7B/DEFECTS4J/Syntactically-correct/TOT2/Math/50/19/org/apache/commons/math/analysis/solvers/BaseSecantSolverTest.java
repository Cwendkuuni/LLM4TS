package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A test function that has a root at x = 1. */
    private static final UnivariateRealFunction TEST_FUNCTION_1
            = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x - 1;
        }
    };

    /** A test function that has a root at x = 2. */
    private static final UnivariateRealFunction TEST_FUNCTION_2
            = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 4;
        }
    };

    /** A test function with no root. */
    private static final UnivariateRealFunction TEST_FUNCTION_NO_ROOT
            = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }
    };

    /** A test function with an infinite root. */
    private static final UnivariateRealFunction TEST_FUNCTION_INFINITE_ROOT
            = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return 1 / x;
        }
    };

    /** Tests the {@link BaseSecantSolver} class with the default constructor. */
    @Test
    public void testDefaultConstructor() {
        final BaseSecantSolver solver = new BaseSecantSolver(
                BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        try {
            solver.solve(10, TEST_FUNCTION_1, 0, 3, 1.5);
            fail("Expected a NoBracketingException.");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    /** Tests the {@link BaseSecantSolver} class with the constructor taking an absolute accuracy. */
    @Test
    public void testAbsoluteAccuracyConstructor() {
        final double absAccuracy = 1e-4;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy,
                BaseSecantSolver.Method.ILLINOIS);
        final double result = solver.solve(10, TEST_FUNCTION_1, 0, 4, 1.5);
        assertEquals(1.0, result, absAccuracy);
    }

    /** Tests the {@link BaseSecantSolver} class with the constructor taking a relative accuracy and an absolute accuracy. */
    @Test
    public void testRelativeAndAbsoluteAccuracyConstructor() {
        final double relAccuracy = 0.01;
        final double absAccuracy = 1e-4;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy,
                BaseSecantSolver.Method.REGULA_FALSI);
        final double result = solver.solve(10, TEST_FUNCTION_1, 0, 5, 1.5);
        assertEquals(1.0, result, absAccuracy + relAccuracy * Precision.SAFE_MIN_VALUE);
    }

    /** Tests the {@link BaseSecantSolver} class with the constructor taking a relative accuracy, an absolute accuracy, and a function value accuracy. */
    @Test
    public void testAllAccuracyConstructors() {
        final double relAccuracy = 0.01;
        final double absAccuracy = 1e-4;
        final double funcValAccuracy = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy,
                funcValAccuracy, BaseSecantSolver.Method.PEGASUS);
        final double result = solver.solve(10, TEST_FUNCTION_1, 0, 6, 1.5);
        assertEquals(1.0, result, absAccuracy + relAccuracy * Precision.SAFE_MIN_VALUE
                + funcValAccuracy * Precision.SAFE_MIN_VALUE);
    }

    /** Tests the {@link BaseSecantSolver} class with the {@link #solve(int, UnivariateRealFunction, double, double)} method. */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithoutStartValue() {
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, TEST_FUNCTION_1, 0, 4);
    }

    /** Tests the {@link BaseSecantSolver} class with the {@link #solve(int, UnivariateRealFunction, double, double, double)} method. */
    @Test
    public void testSolveWithStartValue() {
        final double absAccuracy = 1e-4;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        final double result = solver.solve(10, TEST_FUNCTION_1, 0, 5, 1.5);
        assertEquals(1.0, result, absAccuracy);
    }

    /** Tests the {@link BaseSecantSolver} class with the {@link #solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method. */
    @Test
    public void testSolveWithAllowedSolution() {
        final double absAccuracy = 1e-4;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, BaseSecantSolver.Method.PEGASUS);
        final double result = solver.solve(10, TEST_FUNCTION_1, 0, 4, 1.5, AllowedSolution.LEFT_SIDE);
        assertEquals(1.0, result, absAccuracy);
    }

    /** Tests the {@link BaseSecantSolver} class with a test function that has a root at x = 2. */
    @Test
    public void testSolveComplexFunction() {
        final double absAccuracy = 1e-4;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, BaseSecantSolver.Method.PEGASUS);
        final double result = solver.solve(10, TEST_FUNCTION_2, -10, 10, 1.5);
        assertEquals(2.0, result, absAccuracy);
    }

    /** Tests the {@link BaseSecantSolver} class with a test function that has no root. */
    @Test(expected = MaxEvalException.class)
    public void testSolveNoRootFunction() {
        final double absAccuracy = 1e-4;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, TEST_FUNCTION_NO_ROOT, 0, 3, 1.5);
    }

    /** Tests the {@link BaseSecantSolver} class with a test function that has an infinite root. */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInfiniteRootFunction() {
        final double absAccuracy = 1e-4;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, TEST_FUNCTION_INFINITE_ROOT, -10, 0);
    }

}