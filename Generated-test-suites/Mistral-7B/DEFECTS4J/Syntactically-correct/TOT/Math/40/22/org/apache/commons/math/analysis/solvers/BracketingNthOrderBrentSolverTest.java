package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

/**
 * Tests for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * A simple test for the solver with default parameters.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @org.junit.Test
    public void testDefaultConstruction() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double solution = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals("wrong root found", 0.0, solution, 1e-6);
    }

    /**
     * A test for the solver with custom accuracy and maximal order.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @org.junit.Test
    public void testCustomConstruction() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double solution = solver.solve(100, f, 0.1, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals("wrong root found", 1.0, solution, 1e-9);
    }

    /**
     * A test for the solver with a function that has no bracketing interval.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @org.junit.Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        solver.solve(100, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * A test for the solver with a function that has too small function value accuracy.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testFunctionValueAccuracyTooSmall() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        solver.solve(100, f, -1.0, 1.0, AllowedSolution.ANY_SIDE, 1e-12);
    }

    /**
     * A test for the solver with a function that has a single root.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @org.junit.Test
    public void testSingleRoot() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double[] expectedSolutions = {-1.0, 1.0};
        for (double expectedSolution : expectedSolutions) {
            double solution = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals("wrong root found", expectedSolution, solution, 1e-9);
        }
    }

    /**
     * A test for the solver with a function that has multiple roots.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @org.junit.Test
    public void testMultipleRoots() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 3) - 1;
            }
        };
        double[] expectedSolutions = {-1.0, 1.0, 2.0};
        for (double expectedSolution : expectedSolutions) {
            double solution = solver.solve(100, f, -2.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals("wrong root found", expectedSolution, solution, 1e-9);
        }
    }

    /**
     * A test for the solver with a function that has roots on different sides of the interval.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @org.junit.Test
    public void testRootsOnDifferentSides() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        double solution = solver.solve(100, f, -5.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals("wrong root found", -2.0, solution, 1e-9);
        solution = solver.solve(100, f, -5.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertEquals("wrong root found", 2.0, solution, 1e-9);
    }

}