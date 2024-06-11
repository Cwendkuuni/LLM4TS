package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Before;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.math.analysis.solvers.PegasusSolver;

/**
 * Test suite for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /** A simple test function for testing the solver. */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(final double x) {
            return Math.pow(x, 2) - 4.0;
        }

        @Override
        public double derivative(final double x) {
            return 2 * x;
        }
    };

    /** A simple test function with no root. */
    private static final UnivariateRealFunction NO_ROOT_FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(final double x) {
            return Math.sin(x) - Math.cos(x);
        }

        @Override
        public double derivative(final double x) {
            return Math.cos(x) + Math.sin(x);
        }
    };

    /** A test function with infinite roots. */
    private static final UnivariateRealFunction INF_ROOT_FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(final double x) {
            return Math.log(Math.abs(x));
        }

        @Override
        public double derivative(final double x) {
            return 1 / x;
        }
    };

    /** A test function with multiple roots. */
    private static final UnivariateRealFunction MULTIPLE_ROOT_FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(final double x) {
            return Math.pow(x, 2) - 4.0;
        }

        @Override
        public double derivative(final double x) {
            return 2 * x;
        }
    };

    /** The secant solver to be tested. */
    private BaseSecantSolver solver;

    /** The solver instances to be tested. */
    private RegulaFalsiSolver regulaFalsiSolver;
    private IllinoisSolver illinoisSolver;
    private PegasusSolver pegasusSolver;

    /** The maximum number of iterations for testing. */
    private static final int MAX_ITERATIONS = 100;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        this.solver = new RegulaFalsiSolver(1e-6);
        this.regulaFalsiSolver = new RegulaFalsiSolver(1e-6);
        this.illinoisSolver = new IllinoisSolver(1e-6);
        this.pegasusSolver = new PegasusSolver(1e-6);
    }

    /**
     * Tests the solver with the simple test function.
     *
     * @throws NoBracketingException if the solver cannot find bracketing
     * @throws MaxIterationsExceededException if the solver fails to converge
     * within the maximum allowed iterations
     */
    @Test
    public void testSimpleFunction() throws Exception {
        final double x1 = -2.0;
        final double x2 = 2.0;
        final double expected = 0.0;

        // Test the RegulaFalsiSolver
        final double result = this.regulaFalsiSolver.solve(MAX_ITERATIONS,
                                                           TEST_FUNCTION,
                                                           x1, x2,
                                                           AllowedSolution.ANY_SIDE);
        assertEquals(expected, Precision.round(result, 10), 1e-10);

        // Test the IllinoisSolver
        solver = new IllinoisSolver(1e-6);
        result = solver.solve(MAX_ITERATIONS,
                             TEST_FUNCTION,
                             x1, x2,
                             AllowedSolution.ANY_SIDE);
        assertEquals(expected, Precision.round(result, 10), 1e-10);

        // Test the PegasusSolver
        solver = new PegasusSolver(1e-6);
        result = solver.solve(MAX_ITERATIONS,
                             TEST_FUNCTION,
                             x1, x2,
                             AllowedSolution.ANY_SIDE);
        assertEquals(expected, Precision.round(result, 10), 1e-10);
    }

    /**
     * Tests the solver with a function that has no roots.
     *
     * @throws NoBracketingException if the solver cannot find bracketing
     * @throws MaxIterationsExceededException if the solver fails to converge
     */
    @Test(expected = NoBracketingException.class)
    public void testNoRootFunction() throws Exception {
        final double x1 = -5.0;
        final double x2 = 5.0;

        // Test the RegulaFalsiSolver
        solver = new RegulaFalsiSolver(1e-6);
        solver.solve(MAX_ITERATIONS,
                     NO_ROOT_FUNCTION,
                     x1, x2,
                     AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests the solver with a function that has infinite roots.
     *
     * @throws UnsupportedOperationException if the solver does not support
     * the infinite roots
     * @throws NoBracketingException if the solver cannot find bracketing
     * @throws MaxIterationsExceededException if the solver fails to converge
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testInfRootFunction() throws Exception {
        final double x1 = -1.0;
        final double x2 = 1.0;

        // Test the RegulaFalsiSolver
        solver = new RegulaFalsiSolver(1e-6);
        solver.solve(MAX_ITERATIONS,
                     INF_ROOT_FUNCTION,
                     x1, x2,
                     AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests the solver with a function that has multiple roots.
     *
     * @throws NoBracketingException if the solver cannot find bracketing
     * @throws MaxIterationsExceededException if the solver fails to converge
     */
    @Test
    public void testMultipleRootFunction() throws Exception {
        final double x1 = -3.0;
        final double x2 = 3.0;
        final double expectedRoot1 = -2.0;
        final double expectedRoot2 = 2.0;

        // Test the RegulaFalsiSolver
        final double[] roots = new double[2];
        solver = new RegulaFalsiSolver(1e-6);
        solver.solve(MAX_ITERATIONS,
                     MULTIPLE_ROOT_FUNCTION,
                     x1, x2,
                     AllowedSolution.ANY_SIDE);
        assertEquals(expectedRoot1, Precision.round(solver.getRoot(), 10), 1e-10);
        assertEquals(expectedRoot2, Precision.round(solver.getRoot2(), 10), 1e-10);

        // Test the IllinoisSolver
        solver = new IllinoisSolver(1e-6);
        roots = solver.solve(MAX_ITERATIONS,
                            MULTIPLE_ROOT_FUNCTION,
                            x1, x2,
                            AllowedSolution.ANY_SIDE);
        assertEquals(expectedRoot1, Precision.round(roots[0], 10), 1e-10);
        assertEquals(expectedRoot2, Precision.round(roots[1], 10), 1e-10);

        // Test the PegasusSolver
        solver = new PegasusSolver(1e-6);
        roots = solver.solve(MAX_ITERATIONS,
                            MULTIPLE_ROOT_FUNCTION,
                            x1, x2,
                            AllowedSolution.ANY_SIDE);
        assertEquals(expectedRoot1, Precision.round(roots[0], 10), 1e-10);
        assertEquals(expectedRoot2, Precision.round(roots[1], 10), 1e-10);
    }

    /**
     * Tests the solver with a function that has a root at the edge of the
     * search interval.
     *
     * @throws NoBracketingException if the solver cannot find bracketing
     * @throws MaxIterationsExceededException if the solver fails to converge
     */
    @Test
    public void testEdgeCaseFunction() throws Exception {
        final double x1 = 0.0;
        final double x2 = 1.0;
        final double expected = 0.0;

        // Test the RegulaFalsiSolver
        final double result = this.regulaFalsiSolver.solve(MAX_ITERATIONS,
                                                           TEST_FUNCTION,
                                                           x1, x2,
                                                           AllowedSolution.LEFT_SIDE);
        assertEquals(expected, Precision.round(result, 10), 1e-10);

        // Test the IllinoisSolver
        solver = new IllinoisSolver(1e-6);
        result = solver.solve(MAX_ITERATIONS,
                             TEST_FUNCTION,
                             x1, x2,
                             AllowedSolution.LEFT_SIDE);
        assertEquals(expected, Precision.round(result, 10), 1e-10);

        // Test the PegasusSolver
        solver = new PegasusSolver(1e-6);
        result = solver.solve(MAX_ITERATIONS,
                             TEST_FUNCTION,
                             x1, x2,
                             AllowedSolution.LEFT_SIDE);
        assertEquals(expected, Precision.round(result, 10), 1e-10);
    }

    /**
     * Tests the solver with a function that has a root outside the search interval.
     *
     * @throws NoBracketingException if the solver cannot find bracketing
     * @throws MaxIterationsExceededException if the solver fails to converge
     */
    @Test(expected = NoBracketingException.class)
    public void testOutsideRootFunction() throws Exception {
        final double x1 = 0.0;
        final double x2 = -1.0;

        // Test the RegulaFalsiSolver
        solver = new RegulaFalsiSolver(1e-6);
        solver.solve(MAX_ITERATIONS,
                     TEST_FUNCTION,
                     x1, x2,
                     AllowedSolution.ANY_SIDE);
    }
}