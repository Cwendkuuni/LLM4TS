package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** Instance under test. */
    private BaseSecantSolver solver;

    /** Test function. */
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 4.0});
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Check that root is found correctly.
     */
    @Test
    public void testSolve() {
        double root = solver.solve(100, function, -1.0, 1.0);
        assertEquals("Root should be approximately 1.0", 1.0, root, 1e-7);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     * Check that root is found correctly with specific allowed solution.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(100, function, -1.0, 1.0);
        assertEquals("Root should be approximately 1.0", 1.0, root, 1e-7);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Check that a {@link MaxEvalException} is thrown when maximum number of evaluations is reached.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveMaxEval() {
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        solver.solve(0, function, -1.0, 1.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Check that a {@link NoBracketException} is thrown when intervals are not bracketing a root.
     */
    @Test(expected = NoBracketException.class)
    public void testSolveNoBracket() {
        function = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 0.0});
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        solver.solve(100, function, -1.0, 1.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Check that a {@link NullArgumentException} is thrown when function is null.
     */
    @Test(expected = NullArgumentException.class)
    public void testSolveNullFunction() {
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        solver.solve(100, null, -1.0, 1.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Check that a {@link MathIllegalArgumentException} is thrown when function value is not defined at start value.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveUndefinedFunctionValue() {
        function = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 1.0 / 0.0});
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        solver.solve(100, function, -1.0, 1.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Check that a {@link NumberIsTooLargeException} is thrown when start value is too large.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSolveStartValueTooLarge() {
        function = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 4.0});
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        solver.solve(100, function, Double.POSITIVE_INFINITY, 1.0);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Check that a {@link NumberIsTooSmallException} is thrown when start value is too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveStartValueTooSmall() {
        function = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 4.0});
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        solver.solve(100, function, -1.0, Double.NEGATIVE_INFINITY);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Check that a {@link NumberIsTooLargeException} is thrown when end value is too large.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSolveEndValueTooLarge() {
        function = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 4.0});
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        solver.solve(100, function, -1.0, Double.POSITIVE_INFINITY);
    }

    /**
     * Test method {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     * Check that a {@link NumberIsTooSmallException} is thrown when end value is too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveEndValueTooSmall() {
        function = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 4.0});
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        solver.solve(100, function, -1.0, Double.NEGATIVE_INFINITY);
    }

}