package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test solver with a simple quadratic function.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @Test
    public void testSolverQuadraticFunction() throws NumberIsTooSmallException {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x * 2.0 - 4.0;
            }
        };
        final double x = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, x, 1e-6);
    }

    /**
     * Test solver with a simple linear function.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @Test
    public void testSolverLinearFunction() throws NumberIsTooSmallException {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x - 1.0;
            }
        };
        final double x = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.0, x, 1e-6);
    }

    /**
     * Test solver with a non-bracketing interval.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @Test(expected = NoBracketingException.class)
    public void testSolverNonBracketingInterval() throws NumberIsTooSmallException {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        solver.solve(100, f, 0.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solver with a negative absolute accuracy.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolverNegativeAbsAccuracy() throws NumberIsTooSmallException {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(100, new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        }, 0.0, 0.0, AllowedSolution.ANY_SIDE, -1e-6);
    }

    /**
     * Test solver with a negative maximal order.
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolverNegativeMaximalOrder() throws NumberIsTooSmallException {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(100, new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        }, 0.0, 0.0, AllowedSolution.ANY_SIDE, -1);
    }

    /**
     * Test solver with a function that always returns zero.
     */
    @Test
    public void testSolverFunctionAlwaysZero() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        final double x = solver.solve(100, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, x, 1e-6);
    }

    /**
     * Test solver with a function that is always increasing.
     */
    @Test
    public void testSolverFunctionAlwaysIncreasing() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        final double x = solver.solve(100, f, 0.0, 1.0, AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(0.0, x, 1e-6);
    }

    /**
     * Test solver with a function that is always decreasing.
     */
    @Test
    public void testSolverFunctionAlwaysDecreasing() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };
        final double x = solver.solve(100, f, 0.0, 1.0, AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(0.0, x, 1e-6);
    }

    /**
     * Test solver with a function with a root at zero.
     */
    @Test
    public void testSolverFunctionRootAtZero() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        final double x = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, x, 1e-6);
    }

    /**
     * Test solver with a function with a root at a large value.
     */
    @Test
    public void testSolverFunctionRootAtLargeValue() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-x * 2.0);
            }
        };
        final double x = solver.solve(100, f, 0.0, 10.0, AllowedSolution.ANY_SIDE);
        Assert.assertTrue(Precision.abs(x - 2.0) < 1e-6);
    }

    /**
     * Test solver with a function with a root at a very small value.
     */
    @Test
    public void testSolverFunctionRootAtVerySmallValue() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(1.0 + x) - Math.log(2.0 + x);
            }
        };
        final double x = solver.solve(100, f, 1e-6, 1e-3, AllowedSolution.ANY_SIDE);
        Assert.assertTrue(Precision.abs(x - 1.0 / 2.0) < 1e-6);
    }

}