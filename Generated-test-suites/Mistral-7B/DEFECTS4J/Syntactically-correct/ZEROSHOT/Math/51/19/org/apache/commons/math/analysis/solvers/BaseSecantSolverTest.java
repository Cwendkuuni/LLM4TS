package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.RoundingMode;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction testFunction;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        testFunction = f -> Math.pow(f, 2) - 1;
    }

    @Test
    public void testSolve_minMaxAndAllowedSolution() throws MaxEvalException {
        double root = solver.solve(10, testFunction, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
        assertEquals("Expected root to be less than zero", -Math.sqrt(2), Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndAllowedSolution_inverted() throws MaxEvalException {
        double root = solver.solve(10, testFunction, 5.0, -5.0, AllowedSolution.RIGHT_SIDE);
        assertEquals("Expected root to be greater than zero", Math.sqrt(2), Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndAllowedSolution_below() throws MaxEvalException {
        double root = solver.solve(10, testFunction, -5.0, 5.0, AllowedSolution.BELOW_SIDE);
        assertEquals("Expected root to be less than zero", -Math.sqrt(2), Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndAllowedSolution_above() throws MaxEvalException {
        double root = solver.solve(10, testFunction, 5.0, -5.0, AllowedSolution.ABOVE_SIDE);
        assertEquals("Expected root to be greater than zero", Math.sqrt(2), Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndAllowedSolution_anySide() throws MaxEvalException {
        double root = solver.solve(10, testFunction, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals("Expected root to be equal to the actual root", Math.sqrt(2), Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndStartValue() throws MaxEvalException {
        double root = solver.solve(10, testFunction, -5.0, 5.0, 2.5);
        assertEquals("Expected root to be equal to the actual root", Math.sqrt(2), Precision.round(root, 4), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_minMaxAndAllowedSolution_invalidAllowedSolution() {
        solver.solve(1, testFunction, -5.0, 5.0, AllowedSolution.LEFT_SIDE_BELOW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_minMax_invalidAllowedSolution() {
        solver.solve(10, testFunction, -5.0, 5.0, AllowedSolution.LEFT_SIDE_BELOW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_minMaxAndStartValue_invalidAllowedSolution() {
        solver.solve(10, testFunction, -5.0, 5.0, 2.5, AllowedSolution.LEFT_SIDE_BELOW);
    }

    @Test
    public void testSolve_minMaxAndStartValue_noBracketing() throws MaxEvalException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, testFunction, 0.0, 0.0, 0.0);
        assertNotNull("Expected an exception", root);
    }

    @Test
    public void testSolve_minMaxAndStartValue_inverted() throws MaxEvalException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, testFunction, 5.0, -5.0, 0.0);
        assertNotNull("Expected an exception", root);
    }

    @Test
    public void testSolve_minMaxAndStartValue_functionValueException() throws MaxEvalException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction testFunctionWithException = f -> {
            throw new ArithmeticException("Function threw an exception");
        };
        try {
            solver.solve(10, testFunctionWithException, 0.0, 0.0, 0.0);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolve_minMaxAndStartValue_functionValueOutOfRange() throws MaxEvalException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction testFunctionWithOutOfRange = f -> {
            if (f < 0) {
                return Double.POSITIVE_INFINITY;
            } else {
                return 0;
            }
        };
        try {
            solver.solve(10, testFunctionWithOutOfRange, 0.0, 0.0, 0.0);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolve_minMaxAndStartValue_accuracyException() throws MaxEvalException {
        solver = new BaseSecantSolver(1e-16, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction testFunction = f -> Math.pow(Math.E, f) - 1;
        try {
            solver.solve(10, testFunction, 0.0, 0.0, MathUtils.nextGaussian(0.0, 1.0));
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolve_minMaxAndStartValue_maxEvalException() throws MaxEvalException {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction testFunction = f -> Math.pow(f, 2);
        try {
            solver.solve(0, testFunction, 0.0, 0.0, 0.0);
            fail("Expected exception");
        } catch (MaxEvalException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolve_minMaxAndStartValue_functionValueAccuracyException() throws MaxEvalException {
        solver = new BaseSecantSolver(RoundingMode.HALF_UP, 1e-10, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction testFunction = f -> Math.exp(f) - 2.718281828;
        try {
            solver.solve(10, testFunction, -5.0, 5.0, 0.0);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolve_minMaxAndStartValue_noConvergence() throws MaxEvalException {
        UnivariateRealFunction testFunction = f -> Math.sin(f) / f;
        try {
            solver.solve(10000, testFunction, -Math.PI, Math.PI, 0.0);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolve_minMaxAndStartValue_rootAtEndPoint() throws MaxEvalException {
        UnivariateRealFunction testFunction = f -> f * (f - 1) * (f - 2);
        double root = solver.solve(10, testFunction, 1.0, 2.0, 1.5);
        assertEquals("Expected root to be 1.0", 1.0, Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndStartValue_rootAtStartPoint() throws MaxEvalException {
        UnivariateRealFunction testFunction = f -> f * (f - 1) * (f - 2);
        double root = solver.solve(10, testFunction, 0.0, 1.0, 0.5);
        assertEquals("Expected root to be 0.0", 0.0, Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndStartValue_rootAtMidPoint() throws MaxEvalException {
        UnivariateRealFunction testFunction = f -> f * (f - 1) * (f - 2);
        double root = solver.solve(10, testFunction, 0.0, 1.0, 0.75);
        assertEquals("Expected root to be between 0.75 and 1.0", 1.0, Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndStartValue_rootBetweenBounds() throws MaxEvalException {
        UnivariateRealFunction testFunction = f -> f * (f - 1) * (f - 2);
        double root = solver.solve(10, testFunction, 0.0, 1.0, 0.25);
        assertEquals("Expected root to be between 0.25 and 1.0", 0.5, Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndStartValue_rootOutsideBounds() throws MaxEvalException {
        UnivariateRealFunction testFunction = f -> Math.pow(f, 2) - 1;
        try {
            solver.solve(10, testFunction, -6.0, 4.0, 3.0);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolve_minMaxAndStartValue_rootAtBoundary() throws MaxEvalException {
        UnivariateRealFunction testFunction = f -> Math.pow(f, 2) - 1;
        double root = solver.solve(10, testFunction, -5.0, 5.0, -5.0);
        assertEquals("Expected root to be -5.0", -5.0, Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndStartValue_rootAtBoundary_allowedSolutionLeftSide() throws MaxEvalException {
        UnivariateRealFunction testFunction = f -> Math.pow(f, 2) - 1;
        double root = solver.solve(10, testFunction, -5.0, 5.0, -5.0, AllowedSolution.LEFT_SIDE);
        assertEquals("Expected root to be -5.0", -5.0, Precision.round(root, 4), 0.0);
    }

    @Test
    public void testSolve_minMaxAndStartValue_rootAtBoundary_allowedSolutionRightSide() throws MaxEvalException {
        UnivariateRealFunction testFunction = f -> Math.pow(f, 2) - 1;
        double root = solver.solve(10, testFunction, -5.0, 5.0, 5.0, AllowedSolution.RIGHT_SIDE);
        assertEquals("Expected root to be 5.0", 5.0, Precision.round(root, 4), 0.0);
    }

}