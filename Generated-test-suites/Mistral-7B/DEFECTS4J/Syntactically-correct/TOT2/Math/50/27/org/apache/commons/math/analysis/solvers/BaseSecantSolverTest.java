package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** Test function for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} */
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        double root = solver.solve(10, f, -2.5, 2.5, 0.0);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    /** Test function for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)} */
    @Test
    public void testSolveWithInitialValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        try {
            BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
            double root = solver.solve(10, f, -10, 10);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }

        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        double root = solver.solve(10, f, -3.14159, 3.14159);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    /** Test function for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} */
    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return (x - 1) * (x - 2);
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        double root = solver.solve(10, f, 0.0, 2.0, 1.0, AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(1.0, root, 1e-6);

        try {
            solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
            root = solver.solve(10, f, 0.0, 2.0, 1.0, AllowedSolution.ABOVE_SIDE);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /** Test function for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} with very large input values */
    @Test
    public void testSolveWithLargeInputValues() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 100;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-12, IllinoisSolver.ILLINOIS);
        double root;

        root = solver.solve(10, f, -10000, 10000, -10000, AllowedSolution.ANY_SIDE);
        Assert.assertTrue(Precision.abs(root) < 1e-12);

        try {
            root = solver.solve(10, f, -10000, 10000, 10000, AllowedSolution.LEFT_SIDE);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }
    }
}