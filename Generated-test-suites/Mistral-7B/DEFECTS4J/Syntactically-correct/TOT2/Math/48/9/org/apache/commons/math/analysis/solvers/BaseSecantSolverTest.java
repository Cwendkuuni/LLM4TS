package org.apache.commons.math.analysis.solvers;

import static org.apache.commons.math3.util.Precision.abs;
import static org.apache.commons.math3.util.Precision.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.RootFinderException;

import org.junit.Test;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A simple polynomial function for testing. */
    private static final UnivariateFunction TEST_FUNCTION = new PolynomialFunction(x -> x * x - 4.0);

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        // Test with default constructor
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        // Test with relative and absolute accuracies
        BaseSecantSolver solver2 = new BaseSecantSolver(1.0e-8, 1.0e-8, BaseSecantSolver.Method.REGULA_FALSI);
        // Test with max iterations
        BaseSecantSolver solver3 = new BaseSecantSolver(1.0e-8, 1.0e-8, 100, BaseSecantSolver.Method.PEGASUS);

        // Test with simple polynomial function
        double x = solver.solve(TEST_FUNCTION, -2.0, 2.0);
        assertEquals("Solution should be 1.0", 1.0, x, 1e-8);
        x = solver2.solve(TEST_FUNCTION, -2.0, 2.0);
        assertEquals("Solution should be 1.0", 1.0, x, 1e-8);
        x = solver3.solve(TEST_FUNCTION, -2.0, 2.0);
        assertEquals("Solution should be 1.0", 1.0, x, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, 1.0e-8, BaseSecantSolver.Method.REGULA_FALSI);

        // Test left solution
        double x = solver.solve(TEST_FUNCTION, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
        assertTrue("Solution should be less than 0", x < 0);
        x = solver.solve(TEST_FUNCTION, -2.0, 2.0, AllowedSolution.ABOVE_SIDE);
        assertFalse("Solution should be greater than 0", x < 0);

        // Test right solution
        x = solver.solve(TEST_FUNCTION, -2.0, 2.0, AllowedSolution.RIGHT_SIDE);
        assertTrue("Solution should be greater than 0", x > 0);
        x = solver.solve(TEST_FUNCTION, -2.0, 2.0, AllowedSolution.BELOW_SIDE);
        assertFalse("Solution should be less than 0", x > 0);
    }

    /**
     * Test method for setting max iterations.
     */
    @Test
    public void testSetMaxIterations() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, 1.0e-8, BaseSecantSolver.Method.PEGASUS);

        // Test with default value
        try {
            solver.solve(TEST_FUNCTION, -2.0, 2.0);
            fail("Expected MaxEvalException");
        } catch (MaxEvalException e) {
            // Expected
        }

        // Test with max iterations set
        solver = new BaseSecantSolver(1.0e-8, 1.0e-8, 1, BaseSecantSolver.Method.PEGASUS);
        double x = solver.solve(TEST_FUNCTION, -2.0, 2.0);
        assertEquals("Solution should be 1.0", 1.0, x, 1e-8);
    }

    /**
     * Test method for setting function value accuracy.
     */
    @Test
    public void testSetFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, 1.0e-9, BaseSecantSolver.Method.PEGASUS);

        // Test with default value
        try {
            solver.solve(TEST_FUNCTION, -2.0, 2.0);
            fail("Expected RootFinderException");
        } catch (RootFinderException e) {
            // Expected
        }

        // Test with function value accuracy set
        solver = new BaseSecantSolver(1.0e-9, 1.0e-9, BaseSecantSolver.Method.PEGASUS);
        double x = solver.solve(TEST_FUNCTION, -2.0, 2.0);
        assertEquals("Solution should be 1.0", 1.0, x, 1e-9);
    }

    /**
     * Test method for setting absolute accuracy.
     */
    @Test
    public void testSetAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-7, 1.0e-8, BaseSecantSolver.Method.PEGASUS);

        // Test with default value
        try {
            solver.solve(TEST_FUNCTION, -2.0, 2.0);
            fail("Expected RootFinderException");
        } catch (RootFinderException e) {
            // Expected
        }

        // Test with absolute accuracy set
        solver = new BaseSecantSolver(1.0e-8, 1.0e-8, BaseSecantSolver.Method.PEGASUS);
        double x = solver.solve(TEST_FUNCTION, -2.0, 2.0);
        assertEquals("Solution should be 1.0", 1.0, x, 1e-8);
    }

    /**
     * Test method for incorrect allowed solution.
     */
    @Test
    public void testIncorrectAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, 1.0e-8, BaseSecantSolver.Method.PEGASUS);

        // Test with incorrect allowed solution
        assertThrows(IllegalArgumentException.class, () -> solver.solve(TEST_FUNCTION, -2.0, 2.0, AllowedSolution.WRONG_SIDE));
    }

    /**
     * Test method for non-bracketing function.
     */
    @Test
    public void testNonBracketingFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, 1.0e-8, BaseSecantSolver.Method.PEGASUS);
        UnivariateFunction testFunction = x -> x * x - 3.0;

        // Test with non-bracketing function
        try {
            solver.solve(testFunction, -2.0, 2.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Test method for null function.
     */
    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-8, 1.0e-8, BaseSecantSolver.Method.PEGASUS);
        UnivariateFunction testFunction = null;

        solver.solve(testFunction, -2.0, 2.0);
    }
}