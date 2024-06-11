package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateFunction;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

/**
 * Test for the {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test the solve method with a simple polynomial function.
     * @throws Exception 
     */
    @org.junit.Test
    public void testSolvePolynomial() throws Exception {
        double x0 = 1.0;
        double x1 = 2.0;

        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0});

        double root = UnivariateRealSolverUtils.solve(f, x0, x1);
        double expected = Precision.round(1.5, 3);
        assertEquals("Incorrect root found!", expected, root, 1e-5);
    }

    /**
     * Test the solve method with a function that has no real roots.
     * @throws Exception 
     */
    @org.junit.Test
    public void testSolveNoRealRoots() throws Exception {
        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0});

        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    /**
     * Test the solve method with a function that throws an exception during evaluation.
     * @throws Exception 
     */
    @org.junit.Test
    public void testSolveThrowsException() throws Exception {
        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new NumberIsTooLargeException("Value too large!");
            }
        };

        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    /**
     * Test the bracket method with a simple polynomial function.
     * @throws Exception 
     */
    @org.junit.Test
    public void testBracketPolynomial() throws Exception {
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0});

        double[] brackets = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        double[] expected = {1.0, 1.5};
        assertArrayEquals("Incorrect bracketing found!", expected, brackets, 1e-5);
    }

    /**
     * Test the bracket method with a function that has no real roots.
     * @throws Exception 
     */
    @org.junit.Test
    public void testBracketNoRealRoots() throws Exception {
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;

        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0});

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    /**
     * Test the bracket method with a function that throws an exception during evaluation.
     * @throws Exception 
     */
    @org.junit.Test
    public void testBracketThrowsException() throws Exception {
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 1.0;

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new UnsupportedOperationException("Not supported!");
            }
        };

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    /**
     * Test the bracket method with a function that converges to a root.
     * @throws Exception 
     */
    @org.junit.Test
    public void testBracketConverges() throws Exception {
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;

        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -3.0, 1.0, 2.0});

        double[] brackets = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        double[] expected = {-1.0, 1.0};
        assertArrayEquals("Incorrect bracketing found!", expected, brackets, 1e-5);
    }

    /**
     * Test the bracket method with a function that does not converge to a root
     * within the specified number of iterations.
     * @throws Exception 
     */
    @org.junit.Test
    public void testBracketDoesNotConverge() throws Exception {
        double initial = 0.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        int maxIterations = 10;

        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -3.0, 1.0, 2.0});

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Test the solve method with a function that converges to a root using Brent's method.
     * @throws Exception 
     */
    @org.junit.Test
    public void testSolveBrent() throws Exception {
        double x0 = 0.0;
        double x1 = 1.0;

        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0, 1.0});

        UnivariateSolver brentSolver = new BrentSolver();
        double root = brentSolver.solve(f, x0, x1);
        double expected = Precision.round(1.5, 3);
        assertEquals("Incorrect root found!", expected, root, 1e-5);
    }
}