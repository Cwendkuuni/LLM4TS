package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.LBFGSBrentSolver;
import org.apache.commons.math3.solver.NelderMeadSolver;
import org.apache.commons.math3.solver.PowellSolver;
import org.apache.commons.math3.solvers.BrentSolver.BrentMethod;
import org.apache.commons.math3.solvers.LBFGSBrentSolver.LineSearchMethod;
import org.junit.Test;

/**
 * Tests for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * using default solver.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveDefaultSolver() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double x0 = -3.0;
        double x1 = 3.0;
        double solution = -1.0;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(solution, result, 1.0e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using default solver with absolute accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveDefaultSolverWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double x0 = -3.0;
        double x1 = 3.0;
        double solution = -1.0;
        double absoluteAccuracy = 1.0e-10;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(solution, result, absoluteAccuracy);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * using Brent solver.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveBrentSolver() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double x0 = -3.0;
        double x1 = 3.0;
        double solution = -1.0;
        UnivariateRealSolver solver = new BrentSolver();
        solver.setFunction(f);
        solver.setBracketing(true);
        solver.setMaxIterations(100);
        double result = solver.solve(x0, x1);
        assertEquals(solution, result, 1.0e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using Brent solver with absolute accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveBrentSolverWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double x0 = -3.0;
        double x1 = 3.0;
        double solution = -1.0;
        double absoluteAccuracy = 1.0e-10;
        UnivariateRealSolver solver = new BrentSolver();
        solver.setFunction(f);
        solver.setBracketing(true);
        solver.setMaxIterations(100);
        solver.setAbsoluteAccuracy(absoluteAccuracy);
        double result = solver.solve(x0, x1);
        assertEquals(solution, result, absoluteAccuracy);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * using Powell solver.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolvePowellSolver() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double x0 = -3.0;
        double x1 = 3.0;
        double solution = -1.0;
        UnivariateRealSolver solver = new PowellSolver();
        solver.setFunction(f);
        solver.setMaxIterations(100);
        double result = solver.solve(x0, x1);
        assertEquals(solution, result, 1.0e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using Powell solver with absolute accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolvePowellSolverWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double x0 = -3.0;
        double x1 = 3.0;
        double solution = -1.0;
        double absoluteAccuracy = 1.0e-10;
        UnivariateRealSolver solver = new PowellSolver();
        solver.setFunction(f);
        solver.setMaxIterations(100);
        solver.setAbsoluteAccuracy(absoluteAccuracy);
        double result = solver.solve(x0, x1);
        assertEquals(solution, result, absoluteAccuracy);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}
     * using Nelder-Mead solver.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveNelderMeadSolver() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double x0 = -3.0;
        double x1 = 3.0;
        double solution = -1.0;
        UnivariateRealSolver solver = new NelderMeadSolver();
        solver.setFunction(f);
        solver.setMaxIterations(100);
        double result = solver.solve(x0, x1);
        assertEquals(solution, result, 1.0e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using LBFGS-Brent solver.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveLBFGSBrentSolver() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double x0 = -3.0;
        double x1 = 3.0;
        double solution = -1.0;
        UnivariateRealSolver solver = new LBFGSBrentSolver(BrentMethod.BRENT, LineSearchMethod.LINE_SEARCH_BACKTRACKING);
        solver.setFunction(f);
        solver.setMaxIterations(100);
        double result = solver.solve(x0, x1);
        assertEquals(solution, result, 1.0e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * using LBFGS-Brent solver with absolute accuracy.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolveLBFGSBrentSolverWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double x0 = -3.0;
        double x1 = 3.0;
        double solution = -1.0;
        double absoluteAccuracy = 1.0e-10;
        UnivariateRealSolver solver = new LBFGSBrentSolver(BrentMethod.BRENT, LineSearchMethod.LINE_SEARCH_BACKTRACKING);
        solver.setFunction(f);
        solver.setMaxIterations(100);
        solver.setAbsoluteAccuracy(absoluteAccuracy);
        double result = solver.solve(x0, x1);
        assertEquals(solution, result, absoluteAccuracy);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double initial = -2.0;
        double lowerBound = -3.0;
        double upperBound = 3.0;
        double[] brackets = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10);
        assertEquals(lowerBound, brackets[0], 1.0e-9);
        assertEquals(initial, brackets[1], 1.0e-9);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketWithMaxIterations() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double initial = -2.0;
        double lowerBound = -3.0;
        double upperBound = 3.0;
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with invalid initial value.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketWithInvalidInitialValue() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double initial = 3.0;
        double lowerBound = -3.0;
        double upperBound = 3.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with invalid lower bound value.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketWithInvalidLowerBoundValue() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double initial = 0.0;
        double lowerBound = 3.0;
        double upperBound = 3.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with invalid upper bound value.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketWithInvalidUpperBoundValue() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, new double[]{1.0});
        double initial = 0.0;
        double lowerBound = -3.0;
        double upperBound = -3.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}
     * @throws Exception if an error occurs
     */
    @Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;
        double expected = (a + b) / 2.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, result, 1.0e-9);
    }

}