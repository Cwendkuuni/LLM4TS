package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.Precision;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /** The solver to test. */
    private BaseSecantSolver solver;

    /** A simple test function. */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 2;
        }
    };

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        solver = new IllinoisSolver(1e-4, FUNCTION);
    }

    /**
     * Tests that the solver correctly finds the root of a simple function.
     *
     * @throws ConvergenceException if the solver fails to converge
     * @throws MaxEvalExceededException if the maximum number of iterations is
     *                                exceeded
     * @throws MathIllegalArgumentException if the input is invalid
     */
    @Test
    public void testSimple() throws ConvergenceException, MaxEvalExceededException, MathIllegalArgumentException {
        double root = solver.solve(10, FUNCTION, -2.0, 2.0, 0.0);
        assertEquals(0.0, Precision.abs(root - 1.0), 1e-6);
    }

    /**
     * Tests that the solver correctly handles the case where the function value
     * at the minimum bound is zero.
     *
     * @throws ConvergenceException if the solver fails to converge
     * @throws MaxEvalExceededException if the maximum number of iterations is
     *                                exceeded
     * @throws MathIllegalArgumentException if the input is invalid
     */
    @Test
    public void testMinimumBoundIsRoot() throws ConvergenceException, MaxEvalExceededException, MathIllegalArgumentException {
        solver = new IllinoisSolver(1e-4, FUNCTION);
        double root = solver.solve(10, FUNCTION, -1.0, 1.0, 0.0);
        assertEquals(0.0, Precision.abs(root - 1.0), 1e-6);
    }

    /**
     * Tests that the solver correctly handles the case where the function value
     * at the maximum bound is zero.
     *
     * @throws ConvergenceException if the solver fails to converge
     * @throws MaxEvalExceededException if the maximum number of iterations is
     *                                exceeded
     * @throws MathIllegalArgumentException if the input is invalid
     */
    @Test
    public void testMaximumBoundIsRoot() throws ConvergenceException, MaxEvalExceededException, MathIllegalArgumentException {
        solver = new IllinoisSolver(1e-4, FUNCTION);
        double root = solver.solve(10, FUNCTION, -2.0, 1.0, 0.0);
        assertEquals(0.0, Precision.abs(root - 1.0), 1e-6);
    }

    /**
     * Tests that the solver correctly handles the case where the function values
     * at the bounds are of opposite signs.
     *
     * @throws ConvergenceException if the solver fails to converge
     * @throws MaxEvalExceededException if the maximum number of iterations is
     *                                exceeded
     * @throws MathIllegalArgumentException if the input is invalid
     */
    @Test
    public void testOppositeSigns() throws ConvergenceException, MaxEvalExceededException, MathIllegalArgumentException {
        solver = new IllinoisSolver(1e-4, FUNCTION);
        double root = solver.solve(10, FUNCTION, -1.5, 1.5, 0.0);
        assertEquals(0.0, Precision.abs(root - 1.0), 1e-6);
    }

    /**
     * Tests that the solver correctly handles the case where the function
     * values at the bounds are equal.
     *
     * @throws ConvergenceException if the solver fails to converge
     * @throws MaxEvalExceededException if the maximum number of iterations is
     *                                exceeded
     * @throws MathIllegalArgumentException if the input is invalid
     */
    @Test(expected = ConvergenceException.class)
    public void testEqualSigns() throws ConvergenceException, MaxEvalExceededException, MathIllegalArgumentException {
        solver = new IllinoisSolver(1e-4, FUNCTION);
        solver.solve(10, FUNCTION, -1.0, 1.0, 0.0);
    }

    /**
     * Tests that the solver correctly handles the case where the function
     * is not bracketing the root.
     *
     * @throws ConvergenceException if the solver fails to converge
     * @throws MaxEvalExceededException if the maximum number of iterations is
     *                                exceeded
     * @throws MathIllegalArgumentException if the input is invalid
     */
    @Test(expected = ConvergenceException.class)
    public void testNotBracketing() throws ConvergenceException, MaxEvalExceededException, MathIllegalArgumentException {
        solver = new IllinoisSolver(1e-4, FUNCTION);
        solver.solve(10, FUNCTION, -2.0, 2.0, 3.0);
    }

    /**
     * Tests that the solver correctly handles null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        new IllinoisSolver(null, FUNCTION);
    }

    /**
     * Tests that the solver correctly handles null function.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNullFunction() {
        new IllinoisSolver(1e-4, null);
    }

    /**
     * Tests that the solver correctly handles an invalid method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMethod() {
        new BaseSecantSolver(1e-4, FUNCTION, "InvalidMethod", AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests that the solver correctly handles an invalid method with null
     * function.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidMethodWithNullFunction() {
        new BaseSecantSolver(1e-4, null, "InvalidMethod", AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests that the solver correctly handles an invalid relative accuracy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRelativeAccuracy() {
        new BaseSecantSolver(-1e-4, FUNCTION, Method.ILLINOIS, AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests that the solver correctly handles an invalid absolute accuracy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAbsoluteAccuracy() {
        new BaseSecantSolver(1e-4, FUNCTION, Method.ILLINOIS, -1e-4, AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests that the solver correctly handles an invalid function value accuracy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFunctionValueAccuracy() {
        new BaseSecantSolver(1e-4, FUNCTION, Method.ILLINOIS, 1e-4, -1e-4, AllowedSolution.ANY_SIDE);
    }

}