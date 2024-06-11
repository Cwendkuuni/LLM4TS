package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;

import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        // Test with a simple function
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new IllinoisSolver(relTol, absTol);
        final double root = solver.solve(10, function, -2, 2, -1.5);
        assertEquals("Root not found or incorrect.", -1.41421356237, root, 1e-9);

        // Test with a function having roots on both sides of the interval
        final UnivariateRealFunction functionTwoRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        final BaseSecantSolver solverTwoRoots = new IllinoisSolver(relTol, absTol);
        assertThrows(NoBracketingException.class, () -> solverTwoRoots.solve(10, functionTwoRoots, -5, 5, 0));

        // Test with a function having a root at a boundary
        final UnivariateRealFunction functionRootAtBoundary = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - Math.sqrt(2);
            }
        };
        final double rootAtBoundary = solver.solve(10, functionRootAtBoundary, -5, 5, 0);
        assertEquals("Root not found or incorrect.", Math.sqrt(2), rootAtBoundary, 1e-9);

        // Test with a function having very close roots
        final UnivariateRealFunction functionCloseRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 0.0001;
            }
        };
        final BaseSecantSolver solverCloseRoots = new IllinoisSolver(relTol, absTol);
        final double rootClose = solver.solve(10, functionCloseRoots, -1, 1, 0);
        assertEquals("Root not found or incorrect.", -1, rootClose, 1e-9);

        // Test with a function having very close roots (right solution)
        final double rootCloseRight = solver.solve(10, functionCloseRoots, -1, 1, 1);
        assertEquals("Root not found or incorrect.", 1, rootCloseRight, 1e-9);

        // Test with a function having roots very close to the end of the interval
        final UnivariateRealFunction functionRootsNearEnd = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2 + x) - 1;
            }
        };
        final BaseSecantSolver solverRootsNearEnd = new IllinoisSolver(relTol, absTol);
        final double rootNearEnd = solver.solve(10, functionRootsNearEnd, -5, 5, 4.999);
        assertEquals("Root not found or incorrect.", 5, rootNearEnd, 1e-9);

        // Test with a function having roots very close to the start of the interval
        final UnivariateRealFunction functionRootsNearStart = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2 + x) - 1;
            }
        };
        final double rootNearStart = solver.solve(10, functionRootsNearStart, -5, 5, -0.001);
        assertEquals("Root not found or incorrect.", -5, rootNearStart, 1e-9);

        // Test with a function having a root with a very large absolute value
        final UnivariateRealFunction functionLargeRoot = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 100;
            }
        };
        final BaseSecantSolver solverLargeRoot = new IllinoisSolver(relTol, absTol);
        final double rootLarge = solver.solve(10, functionLargeRoot, -1000, 1000, 0);
        assertEquals("Root not found or incorrect.", 10, rootLarge, 1e-9);

        // Test with a function having roots with very large absolute values
        final UnivariateRealFunction functionLargeRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 10000;
            }
        };
        final BaseSecantSolver solverLargeRoots = new IllinoisSolver(relTol, absTol);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(10, functionLargeRoots, -1000, 1000, 0));

        // Test with a function having roots with very small absolute values
        final UnivariateRealFunction functionSmallRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1e-10;
            }
        };
        final BaseSecantSolver solverSmallRoots = new IllinoisSolver(relTol, absTol);
        assertThrows(ConvergenceException.class, () -> solver.solve(10, functionSmallRoots, -1e-10, 1e-10, 0));

        // Test with a function having a root with very small absolute value (right solution)
        final double rootSmallRight = solver.solve(10, functionSmallRoots, -1e-10, 1e-10, 1e-10);
        assertEquals("Root not found or incorrect.", 0, rootSmallRight, 1e-12);

        // Test with a function having roots with very small absolute values (left solution)
        final double rootSmallLeft = solver.solve(10, functionSmallRoots, -1e-10, 1e-10, -1e-10);
        assertEquals("Root not found or incorrect.", 0, rootSmallLeft, 1e-12);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        // Test with a simple function
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        final BaseSecantSolver solver = new IllinoisSolver(relTol, absTol);
        final double root = solver.solve(10, function, -2, 2, -1.5, allowedSolution);
        assertEquals("Root not found or incorrect.", -1.41421356237, root, 1e-9);

        // Test with a function having roots on both sides of the interval
        final UnivariateRealFunction functionTwoRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        final BaseSecantSolver solverTwoRoots = new IllinoisSolver(relTol, absTol);
        assertThrows(NoBracketingException.class, () -> solverTwoRoots.solve(10, functionTwoRoots, -5, 5, 0, AllowedSolution.LEFT_SIDE));

        // Test with a function having a root at a boundary
        final UnivariateRealFunction functionRootAtBoundary = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - Math.sqrt(2);
            }
        };
        final double rootAtBoundary = solver.solve(10, functionRootAtBoundary, -5, 5, 0, AllowedSolution.LEFT_SIDE);
        assertEquals("Root not found or incorrect.", Math.sqrt(2), rootAtBoundary, 1e-9);

        // Test with a function having very close roots
        final UnivariateRealFunction functionCloseRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 0.0001;
            }
        };
        final BaseSecantSolver solverCloseRoots = new IllinoisSolver(relTol, absTol);
        final double rootClose = solver.solve(10, functionCloseRoots, -1, 1, 0, AllowedSolution.LEFT_SIDE);
        assertEquals("Root not found or incorrect.", -1, rootClose, 1e-9);

        // Test with a function having very close roots (right solution)
        final double rootCloseRight = solver.solve(10, functionCloseRoots, -1, 1, 1, AllowedSolution.RIGHT_SIDE);
        assertEquals("Root not found or incorrect.", 1, rootCloseRight, 1e-9);

        // Test with a function having roots very close to the end of the interval
        final UnivariateRealFunction functionRootsNearEnd = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2 + x) - 1;
            }
        };
        final BaseSecantSolver solverRootsNearEnd = new IllinoisSolver(relTol, absTol);
        final double rootNearEnd = solver.solve(10, functionRootsNearEnd, -5, 5, 4.999, AllowedSolution.LEFT_SIDE);
        assertEquals("Root not found or incorrect.", 5, rootNearEnd, 1e-9);

        // Test with a function having roots very close to the start of the interval
        final UnivariateRealFunction functionRootsNearStart = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2 + x) - 1;
            }
        };
        final double rootNearStart = solver.solve(10, functionRootsNearStart, -5, 5, -0.001, AllowedSolution.RIGHT_SIDE);
        assertEquals("Root not found or incorrect.", -5, rootNearStart, 1e-9);

        // Test with a function having a root with a very large absolute value
        final UnivariateRealFunction functionLargeRoot = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 100;
            }
        };
        final BaseSecantSolver solverLargeRoot = new IllinoisSolver(relTol, absTol);
        final double rootLarge = solver.solve(10, functionLargeRoot, -1000, 1000, 0, AllowedSolution.LEFT_SIDE);
        assertEquals("Root not found or incorrect.", 10, rootLarge, 1e-9);

        // Test with a function having roots with very large absolute values
        final UnivariateRealFunction functionLargeRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 10000;
            }
        };
        final BaseSecantSolver solverLargeRoots = new IllinoisSolver(relTol, absTol);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(10, functionLargeRoots, -1000, 1000, 0, AllowedSolution.LEFT_SIDE));

        // Test with a function having roots with very small absolute values
        final UnivariateRealFunction functionSmallRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1e-10;
            }
        };
        final BaseSecantSolver solverSmallRoots = new IllinoisSolver(relTol, absTol);
        assertThrows(ConvergenceException.class, () -> solver.solve(10, functionSmallRoots, -1e-10, 1e-10, 0, AllowedSolution.LEFT_SIDE));

        // Test with a function having roots with very small absolute values (left solution)
        final double rootSmallLeft = solver.solve(10, functionSmallRoots, -1e-10, 1e-10, 0, AllowedSolution.LEFT_SIDE);
        assertEquals("Root not found or incorrect.", 0, rootSmallLeft, 1e-12);

        // Test with a function having roots with very small absolute values (right solution)
        final double rootSmallRight = solver.solve(10, functionSmallRoots, -1e-10, 1e-10, 1e-10, AllowedSolution.RIGHT_SIDE);
        assertEquals("Root not found or incorrect.", 0, rootSmallRight, 1e-12);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolveWithoutAllowedSolution() {
        // Test with a simple function
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final double absTol = 1e-9;
        final double relTol = 1e-9;
        final BaseSecantSolver solver = new IllinoisSolver(relTol, absTol);
        final double root = solver.solve(10, function, -2, 2);
        assertEquals("Root not found or incorrect.", -1.41421356237, root, 1e-9);
    }
}