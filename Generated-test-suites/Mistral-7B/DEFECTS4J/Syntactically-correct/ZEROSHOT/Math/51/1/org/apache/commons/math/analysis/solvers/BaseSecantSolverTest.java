package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        double absoluteAccuracy = 1e-8;
        solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f,
                               double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, AllowedSolution.ANY_SIDE);
            }
        };
    }

    @Test
    public void testSolve() {
        // Test solver with default parameters
        double root = solver.solve(100, f, -5.0, 5.0);
        Assert.assertEquals(2.0, root, 1e-8);

        // Test solver with relative accuracy and absolute accuracy
        solver = new BaseSecantSolver(1e-5, 1e-8, BaseSecantSolver.Method.ILLINOIS);
        root = solver.solve(100, f, -5.0, 5.0);
        Assert.assertEquals(2.0, root, 1e-8);

        // Test solver with function value accuracy
        solver = new BaseSecantSolver(1e-5, 1e-8, 1e-7, BaseSecantSolver.Method.ILLINOIS);
        root = solver.solve(100, f, -5.0, 5.0);
        Assert.assertEquals(2.0, root, 1e-8);

        // Test solver with an ill-behaved function
        UnivariateRealFunction g = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    throw new NumberIsTooSmallException();
                }
                if (x > 1) {
                    throw new NumberIsTooLargeException();
                }
                return Math.sin(1.0 / x);
            }
        };
        try {
            solver.solve(100, g, -5.0, 5.0);
            Assert.fail();
        } catch (NoBracketingException e) {
            // expected
        }

        // Test solver with a function with no bracketing
        UnivariateRealFunction h = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 4) - 4;
            }
        };
        try {
            solver.solve(100, h, -5.0, 5.0);
            Assert.fail();
        } catch (NoBracketingException e) {
            // expected
        }

        // Test solver with a function with multiple roots
        UnivariateRealFunction i = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x + 4;
            }
        };
        try {
            solver.solve(100, i, -5.0, 5.0);
            Assert.fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        // Test solver with a function with a vertical asymptote
        UnivariateRealFunction j = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0.0) {
                    throw new MathIllegalArgumentException();
                }
                return 1.0 / x;
            }
        };
        try {
            solver.solve(100, j, -5.0, 5.0);
            Assert.fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

}