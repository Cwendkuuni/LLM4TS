package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method.
     */
    @Test
    public void testSolve() {
        // Test with RegulaFalsiSolver method
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = -3.0;
        double max = 3.0;
        double root = -1.0;
        try {
            root = solver.solve(10, f, min, max, root);
        } catch (MaxEvalException e) {
            fail("Exception should not be thrown.");
        }
        assertTrue(Precision.isClose(root, -1.0, 1e-7));
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        // Test with IllinoisSolver method
        solver = new BaseSecantSolver.IllinoisSolver(1e-6);
        root = 0.0;
        try {
            root = solver.solve(10, f, min, max, root);
        } catch (NoBracketingException | NumberIsTooSmallException e) {
            fail("Exception should not be thrown.");
        }
        assertTrue(Precision.isClose(root, 0.0, 1e-7));
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        // Test with PegasusSolver method
        solver = new BaseSecantSolver.PegasusSolver(1e-6);
        root = Math.log(2);
        try {
            root = solver.solve(10, f, min, max, root);
        } catch (UnsupportedOperationException | MaxEvalException e) {
            fail("Exception should not be thrown.");
        }
        assertTrue(Precision.isClose(root, Math.log(2), 1e-7));
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        // Test with custom allowed solution
        solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6, AllowedSolution.LEFT_SIDE);
        try {
            root = solver.solve(10, f, min, max, root);
        } catch (MaxEvalException e) {
            fail("Exception should not be thrown.");
        }
        assertTrue(Precision.isClose(root, -1.0, 1e-7));
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());

        // Test with custom allowed solution and custom error bounds
        solver = new BaseSecantSolver.RegulaFalsiSolver(1e-8, 1e-9, AllowedSolution.LEFT_SIDE);
        try {
            root = solver.solve(10, f, min, max, root);
        } catch (MaxEvalException e) {
            fail("Exception should not be thrown.");
        }
        assertTrue(Precision.isClose(root, -1.0, 1e-9));
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());

        // Test with negative function
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -1.0 * Math.sin(x);
            }
        };
        solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        try {
            root = solver.solve(10, f, min, max, root);
        } catch (NoBracketingException e) {
            // This is expected since the function is not bracketing.
        }
        assertNull(root);

        // Test with non-bracketing function
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    return x * x;
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        };
        try {
            solver.solve(10, f, min, max, root);
            fail("Exception should be thrown.");
        } catch (UnsupportedOperationException e) {
            // This is expected since the function is not defined for all x.
        }
    }

    /**
     * Test the getAllowedSolution() method.
     */
    @Test
    public void testGetAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6, AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6, AllowedSolution.RIGHT_SIDE);
        assertEquals(AllowedSolution.RIGHT_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6, AllowedSolution.BELOW_SIDE);
        assertEquals(AllowedSolution.BELOW_SIDE, solver.getAllowedSolution());

        solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6, AllowedSolution.ABOVE_SIDE);
        assertEquals(AllowedSolution.ABOVE_SIDE, solver.getAllowedSolution());
    }
}