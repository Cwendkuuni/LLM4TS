package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.special.Erf;

/**
 * Tests for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}
     * with default relative and absolute accuracy, and max order 5.
     */
    @org.junit.Test
    public void testConstructWithDefaults() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}
     * with given relative accuracy, absolute accuracy, and max order 5.
     */
    @org.junit.Test
    public void testConstructWithAccuracyAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1e-9, 5);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 1e-8);
        assertEquals(1e-9, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}
     * with given relative accuracy, absolute accuracy, function value accuracy, and max order 5.
     */
    @org.junit.Test
    public void testConstructWithAllAccuraciesAndOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1e-9, 1e-9, 5);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 1e-8);
        assertEquals(1e-9, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(1e-9, solver.getFunctionValueAccuracy(), 1e-11);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}
     * with max order lower than 2.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testConstructWithLowerThanMinimalOrder() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}
     * with root inside given interval, and allowed solution set to ANY_SIDE.
     */
    @org.junit.Test
    public void testSolveWithRootInside() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double root = solver.solve(100, f, -2.5, 2.5, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}
     * with root outside given interval, and allowed solution set to ANY_SIDE.
     */
    @org.junit.Test
    public void testSolveWithRootOutside() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.sin(1);
            }
        };
        double root = solver.solve(100, f, -5, 5, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with root inside given interval, and allowed solution set to LEFT_SIDE.
     */
    @org.junit.Test
    public void testSolveWithRootInsideLeft() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) - 2.5;
            }
        };
        double root = solver.solve(100, f, 0, 10, AllowedSolution.LEFT_SIDE);
        assertEquals(-3.0, root, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with root outside given interval, and allowed solution set to LEFT_SIDE.
     */
    @org.junit.Test(expected = NoBracketingException.class)
    public void testSolveWithRootOutsideLeft() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) - 5.0;
            }
        };
        solver.solve(100, f, 0, 10, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with root below given interval, and allowed solution set to BELOW_SIDE.
     */
    @org.junit.Test
    public void testSolveWithRootBelow() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.exp(x);
            }
        };
        double root = solver.solve(100, f, -10, -5, AllowedSolution.BELOW_SIDE);
        assertEquals(-5.7775130681, root, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with root above given interval, and allowed solution set to ABOVE_SIDE.
     */
    @org.junit.Test
    public void testSolveWithRootAbove() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x);
            }
        };
        double root = solver.solve(100, f, -5, -10, AllowedSolution.ABOVE_SIDE);
        assertEquals(-4.6051706389, root, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}
     * with root between given intervals and maxEval evaluations.
     */
    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testSolveWithRootBetweenAndMaxEval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.sin(x + 0.1);
            }
        };
        solver.solve(10, f, -1, 1, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, double)}
     * with start value equal to root.
     */
    @org.junit.Test
    public void testSolveWithStartValueEqualToRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) - 2.5;
            }
        };
        double root = solver.solve(100, f, 2.5, 2.5, AllowedSolution.ANY_SIDE);
        assertEquals(-3.0, root, 1e-8);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, double)}
     * with start value equal to root and maxEval evaluations.
     */
    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testSolveWithStartValueEqualToRootAndMaxEval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) - 2.5;
            }
        };
        solver.solve(10, f, 2.5, 2.5, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, double)}
     * with start value outside given interval.
     */
    @org.junit.Test(expected = NoBracketingException.class)
    public void testSolveWithStartValueOutside() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - Math.log(2.5);
            }
        };
        solver.solve(100, f, -5.0, 0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, double)}
     * with given function having a vertical asymptote.
     */
    @org.junit.Test(expected = MathInternalError.class)
    public void testSolveWithVerticalAsymptote() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1 / (x - 0.5);
            }
        };
        solver.solve(100, f, 0, 1, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, double)}
     * with a function that is not monotone on the given interval.
     */
    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithNonMonotoneFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };
        solver.solve(100, f, -3, 3, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, double)}
     * with a function that is not differentiable at given points.
     */
    @org.junit.Test
    public void testSolveWithNondifferentiableFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Erf.erf(x);
            }
        };
        double root = solver.solve(100, f, -3, 3, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-8);
    }

}