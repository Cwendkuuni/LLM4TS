package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.UnbracketedException;

import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testBaseSecantSolver() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals("Expected RegulaFalsi method", Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverWithAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-8, Method.REGULA_FALSI);
        assertEquals("Expected RegulaFalsi method", Method.REGULA_FALSI, solver.getMethod());
        assertEquals("Expected absAccuracy = 1e-6", 1e-6, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals("Expected relAccuracy = 1e-8", 1e-8, solver.getRelativeAccuracy(), 1e-12);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverWithAccuraciesAndFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-8, 1e-8, Method.REGULA_FALSI);
        assertEquals("Expected RegulaFalsi method", Method.REGULA_FALSI, solver.getMethod());
        assertEquals("Expected absAccuracy = 1e-6", 1e-6, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals("Expected relAccuracy = 1e-8", 1e-8, solver.getRelativeAccuracy(), 1e-12);
        assertEquals("Expected funcValAccuracy = 1e-8", 1e-8, solver.getFunctionValueAccuracy(), 1e-12);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        UnivariateRealFunction f = f -> Math.pow(Math.E, Math.sin(f));
        double[] expectedValues = {-2.46133932366755, -1.33212483276273, -0.66287537559737, 0.66287537559737,
            1.33212483276273, 2.46133932366755};

        for (int i = 0; i < expectedValues.length; i++) {
            double x = solver.solve(100, f, -10.0, 10.0, i / 2.0);
            assertEquals("Expected root = " + expectedValues[i], expectedValues[i], x, 1e-9);
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        UnivariateRealFunction f = f -> Math.pow(Math.E, Math.sin(f));

        for (AllowedSolution allowed : AllowedSolution.values()) {
            double x = solver.solve(100, f, -10.0, 10.0, 0.0, allowed);
            if (allowed == AllowedSolution.RIGHT_SIDE) {
                assertTrue("Expected x > 0", x > 0);
            } else if (allowed == AllowedSolution.LEFT_SIDE) {
                assertTrue("Expected x < 0", x < 0);
            } else {
                // Other allowed solutions should have no constraints
                // on the sign of the solution.
                assertFalse("Expected no sign constraint", x < 0);
                assertFalse("Expected no sign constraint", x > 0);
            }
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with maxEval exceeded.
     */
    @Test
    public void testSolveMaxEvalExceeded() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        UnivariateRealFunction f = f -> Math.pow(Math.E, 1 / Math.sin(f));

        assertThrows(MaxEvalExceededException.class,
                () -> solver.solve(0, f, -10.0, 10.0, 0.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with unbracketed roots.
     */
    @Test
    public void testSolveUnbracketed() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        UnivariateRealFunction f = f -> Math.pow(Math.E, Math.cos(f)) - 2.0;

        assertThrows(UnbracketedException.class, () -> solver.solve(100, f, -10.0, 10.0, 0.0, AllowedSolution.ANY_SIDE));
    }
}