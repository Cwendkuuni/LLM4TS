package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test solver with a simple polynomial function.
     *
     * @throws MaxEvalException if max number of evaluations is reached
     * @throws TooManyIterationsException if max number of iterations is reached
     */
    @Test
    public void testSimplePolynomial() throws MaxEvalException, TooManyIterationsException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6);
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, root, 1.0e-9);
    }

    /**
     * Test solver with a function with roots close to bounds.
     *
     * @throws MaxEvalException if max number of evaluations is reached
     * @throws TooManyIterationsException if max number of iterations is reached
     */
    @Test
    public void testRootCloseToBounds() throws MaxEvalException, TooManyIterationsException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) - 0.1;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6);
        double root = solver.solve(f, -3.0, 3.0);
        // Root is close to 0, but not exactly 0, so the tolerance is relaxed
        assertTrue(Precision.abs(root, 1.0e-4) <= 1.0e-3);
    }

    /**
     * Test solver with an oscillating function.
     *
     * @throws MaxEvalException if max number of evaluations is reached
     * @throws TooManyIterationsException if max number of iterations is reached
     */
    @Test
    public void testOscillatingFunction() throws MaxEvalException, TooManyIterationsException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(FastMath.PI * x);
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6);
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals(0.0, root, 1.0e-9);
    }

    /**
     * Test solver with a function having multiple roots.
     *
     * @throws MaxEvalException if max number of evaluations is reached
     * @throws TooManyIterationsException if max number of iterations is reached
     */
    @Test
    public void testMultipleRoots() throws MaxEvalException, TooManyIterationsException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return (x - 1.0) * (x - 2.0) * (x + 1.0);
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6);
        double[] roots = {1.0, 2.0};
        for (double root : roots) {
            double result = solver.solve(f, -5.0, 5.0);
            assertEquals(root, result, 1.0e-9);
        }
    }

    /**
     * Test solver with a function having no roots.
     *
     * @throws MaxEvalException if max number of evaluations is reached
     * @throws TooManyIterationsException if max number of iterations is reached
     */
    @Test(expected = org.apache.commons.math3.NoSolutionException.class)
    public void testNoRoots() throws MaxEvalException, TooManyIterationsException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 10;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test solver with a function having an infinite root.
     *
     * @throws MaxEvalException if max number of evaluations is reached
     * @throws TooManyIterationsException if max number of iterations is reached
     */
    @Test(expected = org.apache.commons.math3.InfiniteSeriesException.class)
    public void testInfiniteRoot() throws MaxEvalException, TooManyIterationsException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test solver with a function having a root that is a complex number.
     *
     * @throws MaxEvalException if max number of evaluations is reached
     * @throws TooManyIterationsException if max number of iterations is reached
     */
    @Test
    public void testComplexRoot() throws MaxEvalException, TooManyIterationsException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2.0) + FastMath.SQRT_2 * FastMath.I * FastMath.sin(FastMath.PI * x);
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6);
        double rootReal = solver.solve(f, -2.0, 2.0).getValue();
        double rootImag = FastMath.sqrt(2.0) / 2.0;
        assertEquals(1.0, rootReal, 1.0e-9);
        assertEquals(rootImag, FastMath.imag(solver.solve(f, -2.0, 2.0).getValue()).getValue(), 1.0e-9);
    }

    /**
     * Test solver with a function having a root at infinity.
     *
     * @throws MaxEvalException if max number of evaluations is reached
     * @throws TooManyIterationsException if max number of iterations is reached
     */
    @Test
    public void testRootAtInfinity() throws MaxEvalException, TooManyIterationsException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new SecantSolver(1.0e-6);
        double result = solver.solve(f, -1.0, 1.0).getValue();
        assertNotEquals(0.0, result);
        // The solver should return a NaN if given a tolerance that is too small
        try {
            solver = new SecantSolver(1.0e-16);
            solver.solve(f, -1.0, 1.0);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }
}