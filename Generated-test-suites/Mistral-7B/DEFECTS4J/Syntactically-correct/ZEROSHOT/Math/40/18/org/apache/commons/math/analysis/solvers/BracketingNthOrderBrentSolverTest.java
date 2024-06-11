package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for BracketingNthOrderBrentSolver.
 */
public class BracketingNthOrderBrentSolverTest {

    /** Test function f(x) = x^3 - 1. */
    private static final UnivariateFunction TEST_FUNCTION = new PolynomialFunction(new double[]{-1, 3, 0});

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver()}.
     */
    @ org.junit.Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}.
     */
    @ org.junit.Test
    public void testConstructorWithAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        assertEquals(3, solver.getMaximalOrder());
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-9);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}.
     */
    @ org.junit.Test
    public void testConstructorWithRelativeAndAbsoluteAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 4);
        assertEquals(4, solver.getMaximalOrder());
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 1e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}.
     */
    @ org.junit.Test
    public void testConstructorWithRelativeAndAbsoluteAccuracyAndFunctionValueAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1e-8, 1e-6, 6);
        assertEquals(6, solver.getMaximalOrder());
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 1e-6);
        assertEquals(1e-6, solver.getFunctionValueAccuracy(), 1e-9);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}.
     * with invalid order.
     */
    @ org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testConstructorWithInvalidOrder() {
        new BracketingNthOrderBrentSolver(1e-5, 1e-8, -2);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getMaximalOrder()}.
     */
    @ org.junit.Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());

        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver(1e-8, 3);
        assertEquals(3, solver2.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}.
     */
    @ org.junit.Test
    public void testSolveWithDefaultValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(100, TEST_FUNCTION, -2.0, 2.0);
        assertEquals(0.0, root, 5e-5);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @ org.junit.Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0);
        assertEquals(0.0, root, 5e-5);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @ org.junit.Test
    public void testSolveWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 5e-5);

        solver = new BracketingNthOrderBrentSolver();
        root = solver.solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertTrue(root < 0);

        solver = new BracketingNthOrderBrentSolver();
        root = solver.solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertTrue(root > 0);

        solver = new BracketingNthOrderBrentSolver();
        root = solver.solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0, AllowedSolution.BELOW_SIDE);
        assertTrue(root < 0);

        solver = new BracketingNthOrderBrentSolver();
        root = solver.solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0, AllowedSolution.ABOVE_SIDE);
        assertTrue(root > 0);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     * with non-bracketing interval.
     */
    @ org.junit.Test(expected = NoBracketingException.class)
    public void testSolveWithNonBracketingInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, TEST_FUNCTION, 1.0, 1.0);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     * with invalid allowed solution.
     */
    @ org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithInvalidAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, TEST_FUNCTION, -2.0, 2.0, 1.0, 12);
    }

}