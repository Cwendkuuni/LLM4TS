package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for BracketingNthOrderBrentSolver.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver()}.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}.
     */
    @Test
    public void testConstructorWithAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 6);
        assertEquals(1e-5, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(6, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 1e-5, 6);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 1e-8);
        assertEquals(1e-5, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(6, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyFunctionValueAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-3, 1e-5, 1e-7, 7);
        assertEquals(1e-3, solver.getRelativeAccuracy(), 1e-8);
        assertEquals(1e-5, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(1e-7, solver.getFunctionValueAccuracy(), 1e-8);
        assertEquals(7, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getMaximalOrder()}.
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}.
     */
    @Test
    public void testSolve() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 4, -6, 4}, 3);
        double x = solver.solve(100, function, -2, 2);
        assertEquals(-1.1596526864605193, x, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 4, -6, 4}, 3);
        double x = solver.solve(100, function, -2, 2, -1);
        assertEquals(-1.1596526864605193, x, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 4, -6, 4}, 3);
        double x = solver.solve(100, function, -2, 2, -1, AllowedSolution.LEFT_SIDE);
        assertEquals(-2, x, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithNegativeMaximalOrder() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 4, -6, 4}, 3);
        solver.solve(-1, function, -2, 2, -1, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithMinimalOrder() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 4, -6, 4}, 3);
        solver.solve(1, function, -2, 2, -1, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithoutBracketing() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, -1, -1, 1}, 3);
        solver.solve(100, function, -1, 1, 0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testSolveMaxEval() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, -1, 1}, 3);
        solver.solve(1, function, -1, 1, 0, AllowedSolution.LEFT_SIDE);
    }

}