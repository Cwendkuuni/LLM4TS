package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.BrentSolver.BrentSolverOptions;
import org.apache.commons.math3.solver.BracketedBrentSolver;
import org.apache.commons.math3.solver.BracketedBrentSolver.BracketedBrentSolverOptions;
import org.apache.commons.math3.solver.UnivariateSolver;
import org.apache.commons.math3.solver.UnivariateSolverFactory;

import org.junit.Test;

/**
 * Test class for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        // Test with a simple polynomial function
        UnivariateFunction f = new PolynomialFunction(3, -1.0, 4.0, 1.0);
        double x0 = 1.0;
        double x1 = 3.0;
        double expected = 2.0;
        double result;
        
        try {
            result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(expected, result, 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            e.printStackTrace();
            throw new AssertionError("Test failed: " + e.getMessage());
        }
        
        // Test with a function that has no roots
        UnivariateFunction fNoRoot = new PolynomialFunction(1, 1.0, 1.0, -1.0);
        x0 = 0.0;
        x1 = 2.0;
        try {
            result = UnivariateRealSolverUtils.solve(fNoRoot, x0, x1);
            fail("Test failed: no roots expected");
        } catch (NoRootsException e) {
            // Expected outcome
        }
        
        // Test with invalid arguments
        UnivariateFunction fInvalid = new PolynomialFunction(3, -1.0, 4.0, null);
        x0 = 1.0;
        x1 = 3.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(fInvalid, x0, x1));
        fInvalid = new PolynomialFunction(3, null, 4.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(fInvalid, x0, x1));
        fInvalid = new PolynomialFunction(3, -1.0, 4.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(fInvalid, null, x1));
        fInvalid = new PolynomialFunction(3, -1.0, null, 1.0);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(fInvalid, x0, x1));
        fInvalid = new PolynomialFunction(3, -1.0, 4.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(fInvalid, x0, null));
        
        // Test with advanced options
        UnivariateRealSolverFactory factory = new org.apache.commons.math3.solvers.BrentSolverFactory();
        UnivariateRealFunction fAdvanced = new PolynomialFunction(3, -1.0, 0.0, 4.0);
        BracketedBrentSolverOptions options = new BracketedBrentSolverOptions();
        options.setMaxIterations(100);
        BracketedBrentSolver solver = new BracketedBrentSolver(factory, options);
        double x0Advanced = 1.0;
        double x1Advanced = 3.0;
        double accuracy = 1e-8;
        try {
            result = solver.solve(fAdvanced, x0Advanced, x1Advanced);
            assertEquals(expected, result, accuracy);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            throw new AssertionError("Test failed: " + e.getMessage());
        }
        solver = null; // Clean up resources

        // Test with BrentSolver
        UnivariateRealFunction fBrent = new PolynomialFunction(3, -1.0, 0.0, 4.0);
        BrentSolverOptions optionsBrent = new BrentSolverOptions();
        UnivariateSolver solverBrent = new BrentSolver(factory, optionsBrent);
        try {
            result = solverBrent.solve(fBrent, x0Advanced);
            assertEquals(expected, result, accuracy);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            throw new AssertionError("Test failed: " + e.getMessage());
        }
        solverBrent = null; // Clean up resources
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}.
     */
    @Test
    public void testBracket() {
        // Test with a simple polynomial function
        UnivariateFunction f = new PolynomialFunction(3, -1.0, 4.0, 1.0);
        double initial = 1.0;
        double lowerBound = -5.0;
        double upperBound = 5.0;
        double[] expected = {2.0, 3.0};
        double[] result;
        
        try {
            result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            for (int i = 0; i < 2; i++) {
                assertEquals(expected[i], result[i], 1e-6);
            }
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            throw new AssertionError("Test failed: " + e.getMessage());
        }
        
        // Test with a function that has no bracketing
        UnivariateFunction fNoBracket = new PolynomialFunction(1, 1.0, 1.0, -1.0);
        initial = 0.0;
        lowerBound = 0.0;
        upperBound = 0.0;
        try {
            result = UnivariateRealSolverUtils.bracket(fNoBracket, initial, lowerBound, upperBound);
            fail("Test failed: no bracketing expected");
        } catch (ConvergenceException e) {
            // Expected outcome
        }
        
        // Test with invalid arguments
        UnivariateFunction fInvalid = new PolynomialFunction(3, -1.0, 4.0, null);
        initial = 1.0;
        lowerBound = -5.0;
        upperBound = 5.0;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(fInvalid, initial, lowerBound, upperBound));
        fInvalid = new PolynomialFunction(3, null, 4.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(fInvalid, initial, lowerBound, upperBound));
        fInvalid = new PolynomialFunction(3, -1.0, 4.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(fInvalid, null, lowerBound, upperBound));
        fInvalid = new PolynomialFunction(3, -1.0, 4.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(fInvalid, initial, null, upperBound));
        fInvalid = new PolynomialFunction(3, -1.0, 4.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(fInvalid, initial, lowerBound, null));
        
        // Test with advanced options
        UnivariateRealFunction fAdvanced = new PolynomialFunction(3, -1.0, 0.0, 4.0);
        int maxIterations = 100;
        double lowerBoundAdvanced = -5.0;
        double upperBoundAdvanced = 5.0;
        try {
            result = UnivariateRealSolverUtils.bracket(fAdvanced, initial, lowerBoundAdvanced, upperBoundAdvanced, maxIterations);
            assertEquals(expected[0], result[0], 1e-6);
            assertEquals(expected[1], result[1], 1e-6);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            e.printStackTrace();
            throw new AssertionError("Test failed: " + e.getMessage());
        }
        maxIterations = 5;
        try {
            result = UnivariateRealSolverUtils.bracket(fAdvanced, initial, lowerBoundAdvanced, upperBoundAdvanced, maxIterations);
            fail("Test failed: bracketing not found with limited iterations");
        } catch (ConvergenceException e) {
            // Expected outcome
        }
    }
}