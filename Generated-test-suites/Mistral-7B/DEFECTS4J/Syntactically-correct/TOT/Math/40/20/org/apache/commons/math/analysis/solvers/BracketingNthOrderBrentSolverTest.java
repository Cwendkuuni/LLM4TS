package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;

/**
 * Test cases for {@link BracketingNthOrderBrentSolver} class.
 */
public class BracketingNthOrderBrentSolverTest {

    /** Test function for {@link BracketingNthOrderBrentSolver}. */
    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sqrt(x) - 1.5;
        }

        @Override
        public double derivative(double x) {
            return 0.5f / Math.sqrt(x);
        }
    }

    /** Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)} */
    @org.junit.Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
        MyFunction myFunction = new MyFunction();
        double[] roots = {1.0, 2.0, 3.0};

        for (double root : roots) {
            assertEquals(root, solver.solve(100, myFunction, 0.0, 4.0), 1e-6);
        }
    }

    /** Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)} */
    @org.junit.Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
        MyFunction myFunction = new MyFunction();
        double[] roots = {1.0, 2.0, 3.0};

        for (double root : roots) {
            assertEquals(root, solver.solve(100, myFunction, 0.0, 4.0, root), 1e-6);
        }
    }

    /** Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} */
    @org.junit.Test
    public void testSolveWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
        MyFunction myFunction = new MyFunction();
        double[] roots = {1.0, 2.0, 3.0};

        for (double root : roots) {
            assertEquals(root, solver.solve(100, myFunction, 0.0, 4.0, root, AllowedSolution.RIGHT_SIDE), 1e-6);
            assertEquals(root, solver.solve(100, myFunction, 0.0, 4.0, root, AllowedSolution.LEFT_SIDE), 1e-6);
        }
    }

    /** Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)} with invalid input */
    @org.junit.Test
    public void testSolveWithAllowedSolutionInvalidInput() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
        MyFunction myFunction = new MyFunction();

        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, myFunction, 0.0, 1.0, 0.0, AllowedSolution.ANY_SIDE));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, myFunction, 0.0, 1.0, 0.0, AllowedSolution.BELOW_SIDE));
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, myFunction, 0.0, 1.0, 0.0, AllowedSolution.ABOVE_SIDE));
    }

    /** Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)} without bracketing */
    @org.junit.Test
    public void testSolveWithoutBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
        MyFunction myFunction = new MyFunction();

        assertThrows(NoBracketingException.class, () -> solver.solve(100, myFunction, 1.5, 2.5));
    }

    /** Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, double)} with convergence criteria met */
    @org.junit.Test
    public void testSolveConvergenceCriteriaMet() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-12, 3);
        MyFunction myFunction = new MyFunction();

        double root = 2.0;
        double sol = solver.solve(100, myFunction, 0.0, 4.0, root, AllowedSolution.RIGHT_SIDE);
        assertEquals(root, sol, 1e-12);
    }

}