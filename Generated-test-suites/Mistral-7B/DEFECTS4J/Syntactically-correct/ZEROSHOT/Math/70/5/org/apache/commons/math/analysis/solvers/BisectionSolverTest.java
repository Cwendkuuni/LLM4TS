package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    /**
     * Test solver with a simple polynomial function.
     */
    @org.junit.Test
    public void testBisectionSolverPolynomial() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0});
        BisectionSolver solver = new BisectionSolver();

        double[] roots = {-3.0, 1.0};
        double[] expected = new double[roots.length];
        for ( int i = 0; i < roots.length; ++i ) {
            expected[i] = roots[i];
        }

        double[] results = new double[roots.length];
        for ( int i = 0; i < roots.length; ++i ) {
            results[i] = solver.solve(f, -10.0, 10.0);
            assertEquals(roots[i], results[i], 1e-12);
        }
    }

    /**
     * Test solver with a function that has no roots in the given interval.
     */
    @org.junit.Test
    public void testBisectionSolverNoRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -3.0, 3.0));
    }

    /**
     * Test solver with a function that does not accept the given argument.
     */
    @org.junit.Test
    public void testBisectionSolverNotAcceptedArgument() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if ( x < 0.0 ) {
                    throw new UnsupportedOperationException("Argument must be non-negative");
                }
                return Math.sin(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();

        assertThrows(UnsupportedOperationException.class, () -> solver.solve(f, -1.0, 1.0));
    }

    /**
     * Test solver with a function that does not accept the given arguments (initial guess).
     */
    @org.junit.Test
    public void testBisectionSolverNotAcceptedInitialArgument() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();

        assertThrows(NumberIsTooSmallException.class, () -> solver.solve(f, -1000.0, 1000.0, -1.0));
    }

    /**
     * Test solver with a function that has a root at the midpoint of the interval.
     */
    @org.junit.Test
    public void testBisectionSolverRootAtMidpoint() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        double result = solver.solve(f, -3.0, 1.0);
        assertEquals(-1.0, result, 1e-12);
    }

    /**
     * Test solver with a function that has a root at the boundary of the interval.
     */
    @org.junit.Test
    public void testBisectionSolverRootAtBoundary() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.sin(x) - 0.5);
            }
        };
        BisectionSolver solver = new BisectionSolver();

        double result = solver.solve(f, -Precision.PI, Precision.PI);
        assertTrue(Math.abs(Math.sin(result) - 0.5) < 1e-8);
    }

    /**
     * Test solver with a function that has roots with different signs.
     */
    @org.junit.Test
    public void testBisectionSolverRootsWithDifferentSigns() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3.0) - 2.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        double[] roots = {-1.0, 1.0};
        double[] results = new double[roots.length];
        for ( int i = 0; i < roots.length; ++i ) {
            results[i] = solver.solve(f, -3.0, 3.0);
            assertEquals(roots[i], results[i], 1e-12);
        }
    }

    /**
     * Test solver with a function that has a root at the midpoint of the interval after a certain number of iterations.
     */
    @org.junit.Test
    public void testBisectionSolverRootAtMidpointAfterIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3.0) - 2.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        double result = solver.solve(f, -10.0, 10.0);
        assertEquals(-1.0, result, 1e-12);
    }

    /**
     * Test solver with a function that has roots with a very small absolute difference.
     */
    @org.junit.Test
    public void testBisectionSolverRootsWithSmallDifference() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3.0) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        double[] roots = {-1.0, 1.0};
        double[] results = new double[roots.length];
        for ( int i = 0; i < roots.length; ++i ) {
            results[i] = solver.solve(f, -10.0, 10.0);
            assertEquals(roots[i], results[i], 1e-14);
        }
    }

    /**
     * Test solver with a function that has roots close to the midpoint of the interval.
     */
    @org.junit.Test
    public void testBisectionSolverRootsCloseToMidpoint() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3.0) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        double[] roots = {-1.2, 1.2};
        double[] expected = new double[roots.length];
        for ( int i = 0; i < roots.length; ++i ) {
            expected[i] = roots[i];
        }

        double[] results = new double[roots.length];
        for ( int i = 0; i < roots.length; ++i ) {
            results[i] = solver.solve(f, -2.0, 2.0);
            assertEquals(roots[i], results[i], 1e-12);
        }
    }
}