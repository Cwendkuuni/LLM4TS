package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @org.junit.Test
    public void testSolve() {
        // Test with a simple polynomial function
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(0.01, 100);
        final double root = 0.5;
        assertEquals(root, solver.solve(f, -1.0, 1.0), 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with {@link AllowedSolution#LEFT_SIDE}.
     */
    @org.junit.Test
    public void testSolveLeftSide() {
        // Test with a simple polynomial function
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(0.01, 100, AllowedSolution.LEFT_SIDE);
        final double root = 0.5;
        assertEquals(root, solver.solve(f, -1.0, 1.0), 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with {@link AllowedSolution#RIGHT_SIDE}.
     */
    @org.junit.Test
    public void testSolveRightSide() {
        // Test with a simple polynomial function
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(0.01, 100, AllowedSolution.RIGHT_SIDE);
        final double root = 0.5;
        assertEquals(root, solver.solve(f, -1.0, 1.0), 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with a
     * function that does not bracket the root.
     */
    @org.junit.Test
    public void testSolveNoBracketing() {
        // Test with a function that does not bracket the root
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 0.0, -1.0});
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(0.01, 100);
        final double root = 0.0;
        assertThrows(ConvergenceException.class, () -> solver.solve(f, -1.0, 1.0));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with a
     * function that returns infinity at the minimum.
     */
    @org.junit.Test
    public void testSolveMinInfinity() {
        // Test with a function that returns infinity at the minimum
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(0.01, 100);
        final double root = 0.0;
        assertThrows(ConvergenceException.class, () -> solver.solve(f, -1.0, 1.0));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with a
     * function that returns infinity at the maximum.
     */
    @org.junit.Test
    public void testSolveMaxInfinity() {
        // Test with a function that returns infinity at the maximum
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / (x - 1.0);
            }
        };
        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(0.01, 100);
        final double root = 0.5;
        assertThrows(MaxEvalExceededException.class, () -> solver.solve(f, -1.0, 1.0));
    }
}