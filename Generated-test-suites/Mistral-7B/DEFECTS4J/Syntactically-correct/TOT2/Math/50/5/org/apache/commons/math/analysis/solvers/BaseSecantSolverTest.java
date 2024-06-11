package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        final UnivariateFunction function = new PolynomialFunction(x -> x * x - 2);
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        final double root = solver.solve(function, 0.0, 2.0);
        assertEquals(1.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with {@link AllowedSolution#LEFT_SIDE}.
     */
    @Test
    public void testSolveLeftSide() {
        final UnivariateFunction function = new PolynomialFunction(x -> x * x - 1);
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        final double root = solver.solve(function, 0.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with {@link AllowedSolution#RIGHT_SIDE}.
     */
    @Test
    public void testSolveRightSide() {
        final UnivariateFunction function = new PolynomialFunction(x -> x * x + 1);
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        final double root = solver.solve(function, 1.0, 2.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with
     * non-bracketing bounds.
     */
    @Test(expected = MathException.class)
    public void testNonBracketingBounds() {
        final UnivariateFunction function = new PolynomialFunction(x -> x * x - 1);
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        solver.solve(function, 1.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with
     * {@link AllowedSolution#BELOW_SIDE}.
     */
    @Test
    public void testBelowSide() {
        final UnivariateFunction function = new PolynomialFunction(x -> x * x - 1);
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        final double root = solver.solve(function, 0.0, 0.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-1.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with
     * {@link AllowedSolution#ABOVE_SIDE}.
     */
    @Test
    public void testAboveSide() {
        final UnivariateFunction function = new PolynomialFunction(x -> x * x + 1);
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        final double root = solver.solve(function, 2.0, 2.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with
     * {@link AllowedSolution#ANY_SIDE} and non-converging function.
     */
    @Test(expected = MathException.class)
    public void testNonConvergingFunction() {
        final UnivariateFunction function = x -> Math.sin(x) / x;
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        solver.solve(function, 0.1, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with
     * {@link AllowedSolution#ANY_SIDE} and function with multiple roots.
     */
    @Test
    public void testFunctionWithMultipleRoots() {
        final UnivariateFunction function = x -> (x - 1) * (x - 2) * (x - 3);
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        final double root = solver.solve(function, -1.0, 4.0);
        assertEquals(1.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with
     * {@link AllowedSolution#ANY_SIDE} and very large absolute accuracy.
     */
    @Test
    public void testLargeAbsoluteAccuracy() {
        final UnivariateFunction function = x -> Math.exp(x) - 1;
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e10, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        final double root = solver.solve(function, -10.0, 10.0);
        assertEquals(0.0, root, Precision.SAFE_EPSILON);
    }
}