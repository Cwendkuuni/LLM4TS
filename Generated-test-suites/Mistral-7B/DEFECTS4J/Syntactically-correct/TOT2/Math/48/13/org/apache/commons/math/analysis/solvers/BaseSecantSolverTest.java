package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** A simple quadratic function for testing. */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(final double x) {
            return x * x - 2;
        }

        @Override
        public int getNumberOfDerivatives() {
            return 0;
        }
    };

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     *
     * @throws ConvergenceException If the solver fails to converge.
     */
    @Test
    public void testSolve() throws ConvergenceException {
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Math.max(relTol, absTol),
                                                      BaseSecantSolver.Method.ILLINOIS);

        double root = solver.solve(100, TEST_FUNCTION, -5.0, 5.0, 0.0);
        Assert.assertEquals(Precision.round(Math.sqrt(2), 15), root, absTol);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     *
     * @throws ConvergenceException If the solver fails to converge.
     */
    @Test
    public void testSolveAllowedSolution() throws ConvergenceException {
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Math.max(relTol, absTol),
                                                      BaseSecantSolver.Method.ILLINOIS);

        double root = solver.solve(100, TEST_FUNCTION, -5.0, 5.0, 0.0, AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(Precision.round(Math.sqrt(2), 15), root, absTol);

        root = solver.solve(100, TEST_FUNCTION, -5.0, 5.0, 0.0, AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(Precision.round(Math.sqrt(2), 15), -root, absTol);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     *
     * @throws ConvergenceException If the solver fails to converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveNoBracketing() throws ConvergenceException {
        final double absTol = 1e-6;
        final double relTol = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Math.max(relTol, absTol),
                                                      BaseSecantSolver.Method.ILLINOIS);

        double root = solver.solve(100, TEST_FUNCTION, 1.0, 1.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     *
     * @throws ConvergenceException If the solver fails to converge.
     */
    @Test
    public void testSolveInvalidFunction() throws ConvergenceException {
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        BaseSecantSolver solver = new BaseSecantSolver(relTol, absTol, Math.max(relTol, absTol),
                                                      BaseSecantSolver.Method.ILLINOIS);

        UnivariateRealFunction invalidFunction = new UnivariateRealFunction() {
            @Override
            public double value(final double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getNumberOfDerivatives() {
                return 0;
            }
        };

        try {
            solver.solve(100, invalidFunction, 0.0, 0.0, 0.0);
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}