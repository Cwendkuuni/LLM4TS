package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test class for BaseSecantSolver with different methods.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver()} with default
     * constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS);
        assertEquals("Solution is not correct.", -1.0, solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE), 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolder#BaseSecantSolver(double)} with
     * relative accuracy.
     */
    @Test
    public void testRelativeAccuracyConstructor() {
        final UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(0.001, Method.PEGASUS);
        assertEquals("Solution is not correct.", -1.0, solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE), 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double)}
     * with absolute accuracy.
     */
    @Test
    public void testAbsoluteAccuracyConstructor() {
        final UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertEquals("Solution is not correct.", -1.0, solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE), 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double)}
     * with function value accuracy.
     */
    @Test
    public void testFunctionValueAccuracyConstructor() {
        final UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(0.01, 1e-6, Method.PEGASUS);
        assertEquals("Solution is not correct.", -1.0, solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE), 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with a simple polynomial function.
     */
    @Test
    public void testSimplePolynomialFunction() {
        final UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(1e-5, Method.PEGASUS);

        // Test with default bounds
        assertEquals("Solution is not correct.", -1.0, solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE), 1e-5);

        // Test with custom bounds
        assertEquals("Solution is not correct.", -1.0, solver.solve(100, function, -3.0, 0.0, AllowedSolution.ANY_SIDE), 1e-5);

        // Test with custom start value
        assertEquals("Solution is not correct.", -1.0, solver.solve(100, function, -3.0, 0.0, 1.0, AllowedSolution.ANY_SIDE), 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with a simple polynomial function and custom allowed solution.
     */
    @Test
    public void testSimplePolynomialFunctionCustomAllowedSolution() {
        final UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(1e-5, Method.PEGASUS);

        // Test with default bounds and custom allowed solution
        assertEquals("Solution is not correct.", -1.0, solver.solve(100, function, -2.0, 2.0, AllowedSolution.LEFT_SIDE), 1e-5);

        // Test with custom bounds and custom allowed solution
        assertEquals("Solution is not correct.", -1.0, solver.solve(100, function, -3.0, 0.0, AllowedSolution.BELOW_SIDE), 1e-5);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with a function that doesn't bracket the root.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionDoesntBracketTheRoot() {
        final UnivariateRealFunction function = new PolynomialFunction(x -> (x - 1) * (x - 2) * (x - 3));
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(1e-5, Method.PEGASUS);
        solver.solve(100, function, -2.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with a function that returns infinities.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testFunctionReturnsInfinities() {
        final UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new ArithmeticException("Division by zero.");
            }
        };
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(1e-5, Method.PEGASUS);
        solver.solve(100, function, 0.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with a function that returns NaN.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testFunctionReturnsNaN() {
        final UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Double.NaN;
            }
        };
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new BaseSecantSolver(1e-5, Method.PEGASUS);
        solver.solve(100, function, 0.0, 0.0, AllowedSolution.ANY_SIDE);
    }
}