package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BracketedBrentSolver;
import org.apache.commons.math3.solvers.BrentSolver.BrentSolverOptions;
import org.apache.commons.math3.solvers.LaguerreSolver;
import org.apache.commons.math3.solvers.SecantSolver;

/**
 * JUnit test class for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealSolverUtils.solve(null, 0.0, 1.0);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @org.junit.Test
    public void testSolveSimpleCase() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        double solution = UnivariateRealSolverUtils.solve(function, 1.0, 2.0);
        assertEquals(1.0, solution, 1e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with convergence exception.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testSolveWithConvergenceException() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        UnivariateRealSolverUtils.solve(function, 1.0, 3.0);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with absolute accuracy.
     */
    @org.junit.Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        double solution = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, 1e-12);
        assertEquals(1.0, solution, 1e-13);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with null function.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() {
        UnivariateRealSolverUtils.bracket(null, 0.0, 1.0, 2.0);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with points outside the interval.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBracketWithPointsOutsideInterval() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        UnivariateRealSolverUtils.bracket(function, 3.0, 0.0, 2.0);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with function values of the same sign.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testBracketWithSameSignFunctionValues() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with maximum iterations.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        double[] brackets = UnivariateRealSolverUtils.bracket(function, 1.0, 1.0, 2.0, 1);
        assertEquals(1.0, brackets[0], 1e-9);
        assertEquals(2.0, brackets[1], 1e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with brent solver.
     */
    @org.junit.Test
    public void testSolveWithBrentSolver() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BrentSolverOptions options = new BrentSolverOptions();
        UnivariateRealSolver solver = new BrentSolver();
        solver.setOptions(options);
        double solution = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver);
        assertEquals(1.0, solution, 1e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with secant solver.
     */
    @org.junit.Test
    public void testSolveWithSecantSolver() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        SecantSolver solver = new SecantSolver();
        double x0 = 1.0;
        double x1 = 2.0;
        double solution = UnivariateRealSolverUtils.solve(function, x0, x1, solver);
        assertEquals(1.0, solution, 1e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * with laguerre solver.
     */
    @org.junit.Test
    public void testSolveWithLaguerreSolver() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        LaguerreSolver solver = new LaguerreSolver();
        double solution = UnivariateRealSolverUtils.solve(function, 1.0, 2.0, solver);
        assertEquals(1.0, solution, 1e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with bracketed brent solver.
     */
    @org.junit.Test
    public void testBracketWithBracketedBrentSolver() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BracketedBrentSolver solver = new BracketedBrentSolver();
        double[] brackets = UnivariateRealSolverUtils.bracket(function, 1.0, 2.0, 3.0, solver);
        assertEquals(1.0, brackets[0], 1e-9);
        assertEquals(2.0, brackets[1], 1e-9);
    }
}