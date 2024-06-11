package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for BracketingNthOrderBrentSolver.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver()}.
     */
    @Test
    public void testConstructorDefault() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}.
     */
    @Test
    public void testConstructorWithAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 8);
        assertEquals(8, solver.getMaximalOrder());
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-11);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-6, 8);
        assertEquals(8, solver.getMaximalOrder());
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(1e-8, solver.getRelativeAccuracy(), 1e-10);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyFunctionValueAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-6, 1e-8, 8);
        assertEquals(8, solver.getMaximalOrder());
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(1e-8, solver.getRelativeAccuracy(), 1e-10);
        assertEquals(1e-8, solver.getFunctionValueAccuracy(), 1e-11);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getAbsoluteAccuracy()}.
     */
    @Test
    public void testGetAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getFunctionValueAccuracy()}.
     */
    @Test
    public void testGetFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getFunctionValueAccuracy(), 1e-10);
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
     * Test method for {@link BracketingNthOrderBrentSolver#getRelativeAccuracy()}.
     */
    @Test
    public void testGetRelativeAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getRelativeAccuracy(), 1e-10);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}.
     */
    @Test
    public void testSolveWithDefaults() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.E, -Math.pow(x, 2));
        double result = solver.solve(100, function, -5, 5);
        assertEquals(0, result, 1e-9);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = x -> Math.log(1 + Math.sin(x));
        double result = solver.solve(100, function, 0, 2, 1.5);
        assertEquals(1.0471975511965978, result, 1e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = x -> Math.log(1 + Math.sin(x));
        double result = solver.solve(100, function, 0, 2, 1.5, AllowedSolution.ABOVE_SIDE);
        assertEquals(1.0471975511965978, result, 1e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithMaximalOrderLowerThanTwo() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1);
        UnivariateFunction function = x -> Math.log(1 + Math.sin(x));
        solver.solve(100, function, 0, 2, 1.5, AllowedSolution.ABOVE_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithNullFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, null, -5, 5);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithNullStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = x -> Math.log(1 + Math.sin(x));
        solver.solve(100, function, null, 2, 1.5);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithNullAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = x -> Math.log(1 + Math.sin(x));
        solver.solve(100, function, -5, 5, 1.5, null);
    }

}