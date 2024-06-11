package org.apache.commons.math.analysis.solvers.tests;

import junit.framework.TestCase;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;

public class BaseSecantSolverTest extends TestCase {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, AllowedSolution)}.
     */
    public void testSolve() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(1e-4, 0.5, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(100, f, -10.0, 10.0, AllowedSolution.LEFT_SIDE);
        assertEquals("The root of x^2 - 2 is not found within the interval [-10, 10].", -1.4142135625, root, 1e-5);

        try {
            root = solver.solve(100, f, -10.0, 10.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected NoBracketingException, but none was thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            root = solver.solve(100, f, -10.0, 10.0, AllowedSolution.BELOW_SIDE);
            fail("Expected NoBracketingException, but none was thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            root = solver.solve(100, f, -10.0, 10.0, AllowedSolution.RIGHT_SIDE);
            fail("Expected NoBracketingException, but none was thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            root = solver.solve(100, f, -10.0, 10.0, AllowedSolution.LEFT_SIDE);
            fail("The root of x^2 - 2 is not found within the interval [-10, 10].");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        solver = new BaseSecantSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        root = solver.solve(100, f, -10.0, 10.0, AllowedSolution.LEFT_SIDE);
        assertEquals("The root of x^2 - 2 is not found within the interval [-10, 10] with the given accuracies.", -1.41421376225, root, 1e-8);
    }
}