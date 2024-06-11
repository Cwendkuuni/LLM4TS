package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    /**
     * Test solver with a simple function.
     */
    @Test
    public void testSimpleFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, Precision.relative(root, 0.0, 1e-6));
    }

    /**
     * Test solver with a function with solutions on the left side of the interval.
     */
    @Test
    public void testLeftFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    return Math.pow(x, 2) + 1;
                } else {
                    throw new MathIllegalArgumentException("Argument must be negative.");
                }
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        Assert.assertEquals(-1.0, solver.solve(100, f, -2.0, 0.0, AllowedSolution.LEFT_SIDE).getValue(), 1e-6);
    }

    /**
     * Test solver with a function with solutions on the right side of the interval.
     */
    @Test
    public void testRightFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.pow(x, 2) + 1;
                } else {
                    throw new MathIllegalArgumentException("Argument must be positive.");
                }
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        Assert.assertEquals(1.0, solver.solve(100, f, 0.0, 2.0, AllowedSolution.RIGHT_SIDE).getValue(), 1e-6);
    }

    /**
     * Test solver with a function with solutions below the interval.
     */
    @Test
    public void testBelowFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    return Math.pow(x, 2) - 1;
                } else {
                    throw new MathIllegalArgumentException("Argument must be negative.");
                }
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        Assert.assertEquals(-1.0, solver.solve(100, f, -2.0, 0.0, AllowedSolution.BELOW_SIDE).getValue(), 1e-6);
    }

    /**
     * Test solver with a function with solutions above the interval.
     */
    @Test
    public void testAboveFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.pow(x, 2) + 1;
                } else {
                    throw new MathIllegalArgumentException("Argument must be positive.");
                }
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        Assert.assertEquals(1.0, solver.solve(100, f, 0.0, 2.0, AllowedSolution.ABOVE_SIDE).getValue(), 1e-6);
    }

    /**
     * Test solver with a non-bracketing interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(100, f, -Math.PI, Math.PI, AllowedSolution.ANY_SIDE);
    }

}