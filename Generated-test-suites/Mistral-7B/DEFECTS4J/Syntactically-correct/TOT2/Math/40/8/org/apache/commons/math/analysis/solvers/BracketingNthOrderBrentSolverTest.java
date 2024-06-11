package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for BracketingNthOrderBrentSolver.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor with default accuracy and maximal order.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        Assert.assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        Assert.assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor with given accuracy and maximal order.
     */
    @Test
    public void testConstructor() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-8;
        int maximalOrder = 3;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, maximalOrder);
        Assert.assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-9);
        Assert.assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        Assert.assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test constructor with given accuracy, maximal order and function value accuracy.
     */
    @Test
    public void testConstructorWithFunctionValueAccuracy() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-7;
        int maximalOrder = 3;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, functionValueAccuracy, maximalOrder);
        Assert.assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-9);
        Assert.assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        Assert.assertEquals(maximalOrder, solver.getMaximalOrder());
        Assert.assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 1e-9);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x);
            }

            public double derivative(double x) {
                return Math.cos(x) * (2 * x * Math.exp(-x * x) - Math.sin(x) * Math.exp(-x * x));
            }
        };
        double min = -2.5;
        double max = 2.5;
        double root = Math.PI / 2;
        double solution = solver.solve(1000, f, min, max);
        Assert.assertEquals(Precision.round(root, 10), solution, 1e-11);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x);
            }

            public double derivative(double x) {
                return Math.cos(x) * (2 * x * Math.exp(-x * x) - Math.sin(x) * Math.exp(-x * x));
            }
        };
        double min = -2.5;
        double max = 2.5;
        double startValue = Math.PI / 4;
        double root = Math.PI / 2;
        double solution = solver.solve(1000, f, min, max, startValue);
        Assert.assertEquals(Precision.round(root, 10), solution, 1e-11);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with LEFT\_SIDE.
     */
    @Test
    public void testSolveWithLeftSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x) - 1.0;
            }

            public double derivative(double x) {
                return Math.cos(x) * (2 * x * Math.exp(-x * x) - Math.sin(x) * Math.exp(-x * x));
            }
        };
        double min = -1.0;
        double max = 1.0;
        double root = -1.0;
        double solution = solver.solve(1000, f, min, max, AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(root, solution, 1e-11);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with RIGHT\_SIDE.
     */
    @Test
    public void testSolveWithRightSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x) + 1.0;
            }

            public double derivative(double x) {
                return Math.cos(x) * (2 * x * Math.exp(-x * x) - Math.sin(x) * Math.exp(-x * x));
            }
        };
        double min = -1.0;
        double max = 1.0;
        double root = 1.0;
        double solution = solver.solve(1000, f, min, max, AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(root, solution, 1e-11);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with ABOVE\_SIDE.
     */
    @Test
    public void testSolveWithAboveSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x);
            }

            public double derivative(double x) {
                return Math.cos(x) * (2 * x * Math.exp(-x * x) - Math.sin(x) * Math.exp(-x * x));
            }
        };
        double min = -2.5;
        double max = 2.5;
        double root = Math.PI / 2;
        double solution = solver.solve(1000, f, min, max, AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(Precision.round(root, 10), solution, 1e-11);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with BELOW\_SIDE.
     */
    @Test
    public void testSolveWithBelowSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x);
            }

            public double derivative(double x) {
                return Math.cos(x) * (2 * x * Math.exp(-x * x) - Math.sin(x) * Math.exp(-x * x));
            }
        };
        double min = -2.5;
        double max = 2.5;
        double root = -Math.PI / 2;
        try {
            double solution = solver.solve(1000, f, min, max, AllowedSolution.BELOW_SIDE);
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with INTERSECTING\_INTERVALS.
     */
    @Test
    public void testSolveWithIntersectingIntervals() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x * x - 4.0;
            }

            public double derivative(double x) {
                return 3 * x * x;
            }
        };
        double min = 0.0;
        double max = 2.0;
        double root1 = -2.0;
        double root2 = 2.0;
        try {
            double solution = solver.solve(1000, f, min, max, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (NoBracketingException e) {
            // expected
        }
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with <code>maxEval</code> set to zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithZeroMaxEval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x);
            }

            public double derivative(double x) {
                return Math.cos(x) * (2 * x * Math.exp(-x * x) - Math.sin(x) * Math.exp(-x * x));
            }
        };
        double min = -2.5;
        double max = 2.5;
        solver.solve(0, f, min, max, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * with <code>functionValueAccuracy</code> set to zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithZeroFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x * x);
            }

            public double derivative(double x) {
                return Math.cos(x) * (2 * x * Math.exp(-x * x) - Math.sin(x) * Math.exp(-x * x));
            }
        };
        double min = -2.5;
        double max = 2.5;
        solver.solve(1000, f, min, max, AllowedSolution.ANY_SIDE, 0);
    }

    /**
     * Test method {@link BracketingNthOrderBrentSolver#getMaximalOrder()}.
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0, 1.0, 3);
        Assert.assertEquals(3, solver.getMaximalOrder());
    }

}