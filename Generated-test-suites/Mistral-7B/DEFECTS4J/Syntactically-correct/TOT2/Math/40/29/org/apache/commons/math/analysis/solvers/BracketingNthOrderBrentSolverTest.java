package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.function.UnivariateFunction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor with specified accuracy and maximal order.
     */
    @Test
    public void testConstructorWithArguments() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        int maximalOrder = 10;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test constructor with invalid maximal order.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        int maximalOrder = 1;
        new BracketingNthOrderBrentSolver(relativeAccuracy, absoluteAccuracy, maximalOrder);
    }

    /**
     * Test solver with a function that has a root in the given interval.
     */
    @Test
    public void testSolveWithRootInInterval() {
        double a = 1.0;
        double b = 2.0;
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(f, a, b);
        assertEquals(1.5, root, 1e-6);
    }

    /**
     * Test solver with a function that has a root outside the given interval.
     */
    @Test
    public void testSolveWithRootOutsideInterval() {
        double a = 1.0;
        double b = 2.0;
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 3, 2) - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * (x - 3);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        try {
            solver.solve(f, a, b);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    /**
     * Test solver with a function that has multiple roots in the given interval.
     */
    @Test
    public void testSolveWithMultipleRootsInInterval() {
        double a = 0.0;
        double b = 2.0;
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 3) - Math.pow(x - 1, 2) + x - 1;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x - 1, 2) - 2 * (x - 1) + 1;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(f, a, b);
        assertEquals(1.0, root, 1e-6);
    }

    /**
     * Test solver with a function that has no bracketing interval.
     */
    @Test
    public void testSolveWithNoBracketingInterval() {
        double a = 0.0;
        double b = 0.0;
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        try {
            solver.solve(f, a, b);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    /**
     * Test solver with a function that has a very small root.
     */
    @Test
    public void testSolveWithVerySmallRoot() {
        double a = 0.0;
        double b = 1.0e-10;
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - Math.pow(1e-12, 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(f, a, b);
        assertEquals(1e-12, root, 1e-15);
    }

    /**
     * Test solver with a function that has a very large root.
     */
    @Test
    public void testSolveWithVeryLargeRoot() {
        double a = 1.0;
        double b = 1e10;
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        try {
            solver.solve(f, a, b);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }
}