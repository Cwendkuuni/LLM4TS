package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** The function to test the solver on. */
    private UnivariateRealFunction function;

    /** The solver to test. */
    private BaseSecantSolver solver;

    /**
     * Set up the test.
     */
    @Before
    public void setUp() {
        function = new PolynomialFunction(new double[]{-1, 0, 1}, new double[]{1, 0, -1});
        solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI);
    }

    /**
     * Test that the solver correctly approximates the root of a function.
     */
    @Test
    public void testSimple() {
        final double root = 0.0;
        final double[] expectedRoots = {root};
        final double[] minMax = {-1.0, 1.0};
        final double[] result = solver.solve(100, function, minMax[0], minMax[1]);
        assertArrayEquals(expectedRoots, new double[]{result[0]});
    }

    /**
     * Test that the solver returns the correct type of root if asked for it.
     */
    @Test
    public void testRootType() {
        final double[] minMax = {-1.0, 1.0};
        RegulaFalsiSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI);
        double root = solver.solve(100, function, minMax[0], minMax[1], AllowedSolution.LEFT_SIDE).getRoot();
        assertTrue(function.value(root) <= 0);
        root = solver.solve(100, function, minMax[0], minMax[1], AllowedSolution.RIGHT_SIDE).getRoot();
        assertTrue(function.value(root) >= 0);
    }

    /**
     * Test that the solver correctly handles a function with a root at a boundary.
     */
    @Test
    public void testBoundary() {
        final double root = 1.0;
        final double[] minMax = {-1.0, 1.0};
        final double[] expectedRoots = {root};
        final BracketedUnivariateRealSolver<? extends UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI);
        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        double[] result = solver.solve(100, function, minMax[0], minMax[1]);
        assertArrayEquals(expectedRoots, new double[]{result[0]});
    }

    /**
     * Test that the solver correctly handles a function with multiple roots.
     */
    @Test
    public void testMultipleRoots() {
        final double root1 = 0.5;
        final double root2 = 1.5;
        final double[] minMax = {-2.0, 2.0};
        final double[] expectedRoots = {root1, root2};
        final BracketedUnivariateRealSolver<? extends UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI);
        solver.setMaxEval(200);
        double[] result = solver.solve(100, function, minMax[0], minMax[1], AllowedSolution.ANY_SIDE);
        assertArrayEquals(expectedRoots, result);
    }

    /**
     * Test that the solver correctly handles a function with a root outside the
     * given interval.
     */
    @Test
    public void testOutsideInterval() {
        final double root = 2.0;
        final double[] minMax = {-1.0, 1.0};
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, function, minMax[0], minMax[1], AllowedSolution.ANY_SIDE));
    }

    /**
     * Test that the solver correctly handles a function that is not monotonic in
     * the given interval.
     */
    @Test
    public void testNonMonotonic() {
        final UnivariateRealFunction nonMonotonicFunction = new PolynomialFunction(new double[]{1, 0, -1, 1}, new double[]{1, 0, -1, 1});
        final double[] minMax = {-1.0, 1.0};
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, nonMonotonicFunction, minMax[0], minMax[1], AllowedSolution.ANY_SIDE));
    }

    /**
     * Test that the solver correctly handles a function that does not have a root
     * in the given interval.
     */
    @Test
    public void testNoRoot() {
        final UnivariateRealFunction noRootFunction = new PolynomialFunction(new double[]{-1, 1}, new double[]{-1, 1});
        final double[] minMax = {-1.0, 1.0};
        assertThrows(NoRootException.class, () -> solver.solve(100, noRootFunction, minMax[0], minMax[1], AllowedSolution.ANY_SIDE));
    }

    /**
     * Test that the solver correctly handles a function that throws an exception
     * when evaluated.
     */
    @Test
    public void testExceptionThrown() {
        final UnivariateRealFunction exFunction = new FunctionWithException();
        final double[] minMax = {-1.0, 1.0};
        assertThrows(RuntimeException.class, () -> solver.solve(100, exFunction, minMax[0], minMax[1], AllowedSolution.ANY_SIDE));
    }

    /**
     * Test that the solver correctly handles a function with a very large root.
     */
    @Test
    public void testLargeRoot() {
        final double root = 1e20;
        final double[] minMax = {-1.0, 1.0};
        final double precision = 1e-10;
        final RegulaFalsiSolver solver = new RegulaFalsiSolver(precision, Method.REGULA_FALSI);
        solver.setMaxIterations(100000);
        double x = minMax[0];
        double fx;
        do {
            fx = solver.solve(1, function, x, x + 1, AllowedSolution.ANY_SIDE).getRoot();
            x = fx;
        } while (Math.abs(fx - root) > precision);
        assertEquals(root, x, precision);
    }

    /**
     * Test that the solver correctly handles a function with a root that is very
     * close to a boundary.
     */
    @Test
    public void testRootNearBoundary() {
        final double root = 1e-15;
        final double[] minMax = {-1.0, 1.0};
        final double precision = 1e-15;
        final RegulaFalsiSolver solver = new RegulaFalsiSolver(precision, Method.REGULA_FALSI);
        solver.setMaxIterations(100000);
        double x = minMax[0];
        double fx;
        do {
            fx = solver.solve(1, function, x, x + 1, AllowedSolution.ANY_SIDE).getRoot();
            x = fx;
        } while (Math.abs(fx - root) > precision);
        assertEquals(root, x, precision);
    }

    /**
     * Test that the solver correctly handles a function with a root that is very
     * close to another root.
     */
    @Test
    public void testRootsClose() {
        final double root1 = 0.1;
        final double root2 = 0.2;
        final double[] minMax = {-1.0, 1.0};
        final double precision = 1e-6;
        final RegulaFalsiSolver solver = new RegulaFalsiSolver(precision, Method.REGULA_FALSI);
        double[] roots = solver.solve(100, function, minMax[0], minMax[1], AllowedSolution.ANY_SIDE);
        assertArrayEquals(new double[]{root1, root2}, roots);
    }

    /**
     * Test that the solver correctly handles a function with a root that is very
     * close to the boundary but still inside the interval.
     */
    @Test
    public void testRootNearBoundaryButInside() {
        final double root = 0.0001;
        final double[] minMax = {-1.0, 1.0};
        final double precision = 1e-6;
        final RegulaFalsiSolver solver = new RegulaFalsiSolver(precision, Method.REGULA_FALSI);
        double[] roots = solver.solve(100, function, minMax[0], minMax[1], AllowedSolution.ANY_SIDE);
        assertEquals(1, roots.length);
        assertEquals(root, roots[0], precision);
    }

    /**
     * Test that the solver correctly handles a function with a root that is very
     * close to the boundary but on the outside.
     */
    @Test
    public void testRootNearBoundaryButOutside() {
        final double root = 1.0001;
        final double[] minMax = {-1.0, 1.0};
        final double precision = 1e-6;
        final RegulaFalsiSolver solver = new RegulaFalsiSolver(precision, Method.REGULA_FALSI);
        double[] roots = solver.solve(100, function, minMax[0], minMax[1], AllowedSolution.RIGHT_SIDE);
        assertEquals(1, roots.length);
        assertEquals(root, roots[0], precision);
    }

    /**
     * Test that the solver correctly handles a function with a root that is very
     * close to the boundary but exactly on it.
     */
    @Test
    public void testRootOnBoundary() {
        final double root = 1.0;
        final double[] minMax = {-1.0, 1.0};
        final double precision = 1e-15;
        final RegulaFalsiSolver solver = new RegulaFalsiSolver(precision, Method.REGULA_FALSI);
        double[] roots = solver.solve(100, function, minMax[0], minMax[1], AllowedSolution.RIGHT_SIDE);
        assertEquals(1, roots.length);
        assertEquals(root, roots[0], Precision.SAFE_EPSILON);
    }

    /**
     * Test that the solver correctly handles a function with a root that is very
     * close to the boundary and the interval is larger than necessary.
     */
    @Test
    public void testRootOnBoundaryWithLargerInterval() {
        final double root = 1.0;
        final double[] minMax = {-2.0, 2.0};
        final double precision = 1e-15;
        final RegulaFalsiSolver solver = new RegulaFalsiSolver(precision, Method.REGULA_FALSI);
        final double eps = Precision.SAFE_EPSILON;
        double[] roots = solver.solve(100, function, minMax[0], minMax[1], AllowedSolution.RIGHT_SIDE);
        assertEquals(1, roots.length);
        assertEquals(root, roots[0], Precision.SAFE_EPSILON);
        assertTrue(FunctionUtils.isClose(minMax[1], root, eps));
    }

    /**
     * Test that the solver correctly handles a function with a root that is very
     * close to the boundary and the interval is smaller than necessary.
     */
    @Test
    public void testRootOnBoundaryWithSmallerInterval() {
        final double root = 1.0;
        final double[] minMax = {-1.001, 1.001};
        final double precision = 1e-15;
        final RegulaFalsiSolver solver = new RegulaFalsiSolver(precision, Method.REGULA_FALSI);
        final double eps = Precision.SAFE_EPSILON;
        double[] roots = solver.solve(100, function, minMax[0], minMax[1], AllowedSolution.RIGHT_SIDE);
        assertEquals(1, roots.length);
        assertEquals(root, roots[0], Precision.SAFE_EPSILON);
        assertFalse(FunctionUtils.isClose(minMax[1], root, eps));
    }

    /**
     * Test that a solver correctly throws a ConvergenceException when the
     * maximum number of iterations is exceeded.
     */
    @Test
    public void testMaxIterations() {
        final double precision = 1e-6;
        final int maxEval = 5;
        final RegulaFalsiSolver solver = new RegulaFalsiSolver(precision, Method.REGULA_FALSI);
        solver.setMaxIterations(maxEval);
        assertThrows(MaxEvalExceededException.class, () -> solver.solve(100, function, -1.0, 1.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test that a solver correctly throws a TooManyIterationsException when the
     * maximum number of iterations is exceeded.
     */
    @Test
    public void testTooManyIterations() {
        final double precision = 1e-6;
        final int maxEval = 10;
        final RegulaFalsiSolver solver = new RegulaFalsiSolver(precision, Method.REGULA_FALSI);
        solver.setMaxIterations(maxEval);
        assertThrows(TooManyIterationsException.class, () -> solver.solve(101, function, -1.0, 1.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Function that throws a runtime exception when evaluated.
     */
    private static class FunctionWithException implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            throw new RuntimeException("Function threw an exception.");
        }

        @Override
        public int getMultiplicity(double x) {
            return 1;
        }
    }

}