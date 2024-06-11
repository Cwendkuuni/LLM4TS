package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test a simple case with a bracketing interval.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimpleBracketing() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, f, -1.5, 1.5);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test a case with a non-bracketing interval.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoBracketingException.class)
    public void testNonBracketing() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(100, f, 1.5, -1.5);
    }

    /**
     * Test a case with a small interval.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSmallInterval() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 5);
        double root = solver.solve(100, f, -1.5, 1.5);
        Assert.assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test a case with a large interval.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoBracketingException.class)
    public void testLargeInterval() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(100, f, -10.0, 10.0);
    }

    /**
     * Test a case with a non-convergent function.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxEvalException.class)
    public void testNonConvergentFunction() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(100, f, -2.0, 2.0);
    }

    /**
     * Test a case with a function with a root at a boundary.
     * @throws Exception if an error occurs
     */
    @Test
    public void testRootAtBoundary() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, f, -1.0, 1.0, 0.0);
        Assert.assertEquals(-1.0, root, 1e-6);
    }

    /**
     * Test a case with a function with a root at a specified side.
     * @throws Exception if an error occurs
     */
    @Test
    public void testRootAtSpecifiedSide() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.LEFT_SIDE);
        double root = solver.solve(100, f, -1.0, 1.0, 0.0);
        Assert.assertEquals(-1.0, root, 1e-6);
    }

    /**
     * Test a case with a function with a root at a boundary but not bracketing.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoBracketingException.class)
    public void testRootAtBoundaryNotBracketing() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.LEFT_SIDE);
        solver.solve(100, f, 0.0, 1.0, 0.0);
    }

}