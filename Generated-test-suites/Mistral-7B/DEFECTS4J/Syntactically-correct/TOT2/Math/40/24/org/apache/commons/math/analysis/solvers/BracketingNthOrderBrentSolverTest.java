package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.solvers.BracketingRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;

public class BracketingNthOrderBrentSolverTest {

    @org.junit.Test
    public void testBracketingNthOrderBrentSolver() {
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.001, 5);
        BracketingRealSolver result = solver.solve(100, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals("Solver did not find expected root", -1.339745033633939, result.getValue(), 1e-6);
        assertEquals("Solver did not find expected root x", -1.339745033633939, result.getX(), 1e-6);

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 0);
            solver.solve(100, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, null, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, f, -5.0, -5.0, AllowedSolution.ANY_SIDE);
            fail("Expected NoBracketingException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception: " + e.getMessage(), "Range must bracket a root", e.getMessage());
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, f, 5.0, 5.0, AllowedSolution.ANY_SIDE);
            fail("Expected NoBracketingException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception: " + e.getMessage(), "Range must bracket a root", e.getMessage());
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, f, -5.0, 5.0, AllowedSolution.BELOW_SIDE);
            fail("Expected NoBracketingException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception: " + e.getMessage(), "Range must bracket a root", e.getMessage());
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, f, -5.0, 5.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected NoBracketingException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception: " + e.getMessage(), "Range must bracket a root", e.getMessage());
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, f, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
            fail("Expected NoBracketingException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception: " + e.getMessage(), "Range must bracket a root", e.getMessage());
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, f, -5.0, 5.0, AllowedSolution.RIGHT_SIDE);
            fail("Expected NoBracketingException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception: " + e.getMessage(), "Range must bracket a root", e.getMessage());
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, f, -5.0, 5.0, AllowedSolution.MINIMUM);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception: " + e.getMessage(), "Invalid solution type", e.getMessage());
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, f, -5.0, 5.0, AllowedSolution.MAXIMUM);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception: " + e.getMessage(), "Invalid solution type", e.getMessage());
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, f, -5.0, 5.0, AllowedSolution.NEAREST);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception: " + e.getMessage(), "Invalid solution type", e.getMessage());
        }

        try {
            solver = new BracketingNthOrderBrentSolver(0.001, 5);
            solver.solve(100, f, -5.0, 5.0, AllowedSolution.INTERSECT);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected exception: " + e.getMessage(), "Invalid solution type", e.getMessage());
        }
    }

}