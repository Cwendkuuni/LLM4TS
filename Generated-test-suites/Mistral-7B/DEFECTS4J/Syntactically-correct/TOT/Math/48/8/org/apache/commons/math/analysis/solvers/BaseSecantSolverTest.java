package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.BracketedUnivariateRealFunction;
import org.apache.commons.math.analysis.FunctionUtils;
import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.analysis.Solver;
import org.apache.commons.math.analysis.UnivariateRealFunction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** The function to test. */
    private UnivariateRealFunction function;

    /** The solver to test. */
    private Solver solver;

    /** The maximum number of iterations. */
    private static final int MAX_ITERATIONS = 100;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        function = new PolynomialFunction(new double[]{-1.0, 3.0, 2.0, 1.0}, 3);
        solver = new RegulaFalsiSolver(1e-6, 10);
    }

    /**
     * Tests solving a simple polynomial equation.
     */
    @Test
    public void testPolynomial() {
        double root = solver.solve(MAX_ITERATIONS, function, -10.0, 10.0, 0.0);
        assertEquals(3.0, root, 1e-7);
    }

    /**
     * Tests solving a function with a root at a boundary.
     */
    @Test
    public void testBoundary() {
        solver = new RegulaFalsiSolver(1e-6, 10);
        double root = solver.solve(MAX_ITERATIONS, FunctionUtils.NEG_ONE, -1.0, 1.0, 0.0);
        assertEquals(0.0, root, 1e-7);

        solver = new RegulaFalsiSolver(1e-6, 10);
        root = solver.solve(MAX_ITERATIONS, FunctionUtils.ONE, -1.0, 1.0, 0.0);
        assertEquals(1.0, root, 1e-7);
    }

    /**
     * Tests solving a function with a root at a boundary with machine epsilon.
     */
    @Test
    public void testBoundaryWithEpsilon() {
        solver = new RegulaFalsiSolver(1e-15, 10);
        double root = solver.solve(MAX_ITERATIONS, FunctionUtils.NEG_ONE, -1.0, 1.0, 0.0);
        assertEquals(0.0, root, 1e-16);

        solver = new RegulaFalsiSolver(1e-15, 10);
        root = solver.solve(MAX_ITERATIONS, FunctionUtils.ONE, -1.0, 1.0, 0.0);
        assertEquals(1.0, root, 1e-16);
    }

    /**
     * Tests solving a function with a root outside of the bracketing interval.
     */
    @Test
    public void testOutside() {
        solver = new RegulaFalsiSolver(1e-6, 10);
        assertThrows(ConvergenceException.class, () -> solver.solve(MAX_ITERATIONS, function, -20.0, -10.0, 0.0));
    }

    /**
     * Tests solving a function with a root outside of the bracketing interval
     * with machine epsilon.
     */
    @Test
    public void testOutsideWithEpsilon() {
        solver = new RegulaFalsiSolver(1e-15, 10);
        assertThrows(ConvergenceException.class, () -> solver.solve(MAX_ITERATIONS, function, -20.0, -10.0, 0.0));
    }

    /**
     * Tests solving a function with an invalid allowed solution.
     */
    @Test
    public void testInvalidAllowedSolution() {
        solver = new RegulaFalsiSolver(1e-6, 10);
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(MAX_ITERATIONS, function, -10.0, 10.0, null));
    }

    /**
     * Tests solving a function with an invalid relative accuracy.
     */
    @Test
    public void testInvalidRelativeAccuracy() {
        solver = new RegulaFalsiSolver(-1e-6, 10);
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(MAX_ITERATIONS, function, -10.0, 10.0, 0.0));
    }

    /**
     * Tests solving a function with an invalid absolute accuracy.
     */
    @Test
    public void testInvalidAbsoluteAccuracy() {
        solver = new RegulaFalsiSolver(1e-6, -1.0);
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(MAX_ITERATIONS, function, -10.0, 10.0, 0.0));
    }

    /**
     * Tests solving a function with an invalid function value accuracy.
     */
    @Test
    public void testInvalidFunctionValueAccuracy() {
        solver = new RegulaFalsiSolver(1e-6, 10, 1e-6);
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(MAX_ITERATIONS, function, -10.0, 10.0, 0.0));
    }

    /**
     * Tests solving a function with a root at machine zero.
     */
    @Test
    public void testMachineZero() {
        solver = new RegulaFalsiSolver(1e-15, 10);
        double root = solver.solve(MAX_ITERATIONS, function, -1e-308, 1e-308, 0.0);
        assertEquals(0.0, root, 1e-16);
    }

    /**
     * Tests solving a function with a root at machine infinity.
     */
    @Test
    public void testMachineInfinity() {
        solver = new RegulaFalsiSolver(1e-6, 10);
        assertThrows(NumberIsTooLargeException.class, () -> solver.solve(MAX_ITERATIONS, FunctionUtils.ONE_OVER_X, 1e-308, 1.0, 0.0));
    }

    /**
     * Tests solving a function with a root at machine negative infinity.
     */
    @Test
    public void testMachineNegativeInfinity() {
        solver = new RegulaFalsiSolver(1e-6, 10);
        assertThrows(NumberIsTooSmallException.class, () -> solver.solve(MAX_ITERATIONS, FunctionUtils.ONE_OVER_X, -1e308, -1.0, 0.0));
    }

    /**
     * Tests solving a function that doesn't bracket the root.
     */
    @Test
    public void testNoBracketing() {
        solver = new RegulaFalsiSolver(1e-6, 10);
        assertThrows(ConvergenceException.class, () -> solver.solve(MAX_ITERATIONS, new PolynomialFunction(new double[]{-1.0, 3.0}, 2), -10.0, 10.0, 0.0));
    }

    /**
     * Tests solving a function that doesn't bracket the root with machine epsilon.
     */
    @Test
    public void testNoBracketingWithEpsilon() {
        solver = new RegulaFalsiSolver(1e-15, 10);
        assertThrows(ConvergenceException.class, () -> solver.solve(MAX_ITERATIONS, new PolynomialFunction(new double[]{-1.0, 3.0}, 2), -10.0, 10.0, 0.0));
    }

    /**
     * Tests solving a function with a root outside of the allowed solutions.
     */
    @Test
    public void testOutsideAllowedSolutions() {
        solver = new RegulaFalsiSolver(1e-6, 10, AllowedSolution.BELOW_SIDE);
        assertThrows(ConvergenceException.class, () -> solver.solve(MAX_ITERATIONS, function, -10.0, 10.0, 0.0));
    }

    /**
     * Tests solving a function with a root outside of the allowed solutions with machine epsilon.
     */
    @Test
    public void testOutsideAllowedSolutionsWithEpsilon() {
        solver = new RegulaFalsiSolver(1e-15, 10, AllowedSolution.BELOW_SIDE);
        assertThrows(ConvergenceException.class, () -> solver.solve(MAX_ITERATIONS, function, -10.0, 10.0, 0.0));
    }

    /**
     * Tests solving a function that exceeds the maximum number of iterations.
     */
    @Test
    public void testMaxIterationsExceeded() {
        solver = new RegulaFalsiSolver(1e-6, 10);
        ExpectedException exception = ExpectedException.none();
        exception.expect(MaxIterationsExceededException.class);
        solver.solve(1, function, -10.0, 10.0, 0.0);
    }

}