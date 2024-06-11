package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method
     * with the {@link IllinoisSolver}.
     */
    @Test
    public void testIllinoisSolver() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double eps = 1e-6;
        final double[] roots = {-1.0, 1.0};

        for (double root : roots) {
            final BaseSecantSolver solver = new BaseSecantSolver(eps, Method.ILLINOIS);
            final double solution = solver.solve(10, f, -2.0, 2.0, root < 0 ? root + 0.1 : root - 0.1);
            Assert.assertEquals(root, Precision.round(solution, eps), eps);
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method
     * with the {@link PegasusSolver}.
     */
    @Test
    public void testPegasusSolver() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        final double eps = 1e-6;
        final double root = Math.PI / 2;

        final BaseSecantSolver solver = new BaseSecantSolver(eps, Method.PEGASUS);
        final double solution = solver.solve(10, f, -3.0, 3.0, 1.0);
        Assert.assertEquals(root, Precision.round(solution, eps), eps);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method
     * with invalid inputs.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidInputs() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(1.0, Method.ILLINOIS);
        solver.solve(10, f, 1.0, 1.0, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method
     * with the {@link IllinoisSolver}.
     */
    @Test
    public void testIllinoisSolverAllowedSolution() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double eps = 1e-6;
        final double root = -1.0;

        final BaseSecantSolver solver = new BaseSecantSolver(eps, Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        final double solution = solver.solve(10, f, -2.0, 2.0, root);
        Assert.assertEquals(root, Precision.round(solution, eps), eps);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method
     * with the {@link PegasusSolver}.
     */
    @Test
    public void testPegasusSolverAllowedSolution() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        final double eps = 1e-6;
        final double root = Math.PI / 2;

        final BaseSecantSolver solver = new BaseSecantSolver(eps, Method.PEGASUS, AllowedSolution.ABOVE_SIDE);
        final double solution = solver.solve(10, f, -3.0, 3.0, 1.0);
        Assert.assertEquals(root, Precision.round(solution, eps), eps);
    }
}