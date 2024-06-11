package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /** A simple quadratic function for testing. */
    private static final UnivariateFunction FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return x * x - 4;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    };

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a simple quadratic function.
     *
     * @throws MaxEvalException if the solver reaches the maximum number of evaluations
     * @throws NumberIsTooSmallException if the solution is too close to a bound
     * @throws TooManyIterationsException if the solver reaches the maximum number of iterations
     */
    @Test
    public void testSolveQuadratic() throws MaxEvalException, NumberIsTooSmallException, TooManyIterationsException {
        final double tolerance = 1e-9;
        final int maxEvaluations = 50;
        final double[] bounds = {-3.0, 3.0};
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;

        final BaseSecantSolver solver = new IllinoisSolver(tolerance, maxEvaluations, FUNCTION);
        final double root = solver.solve(maxEvaluations, FUNCTION, bounds[0], bounds[1], allowedSolution);

        assertEquals(0.0, root, tolerance);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * method with a function that has no roots.
     *
     * @throws MaxEvalException if the solver reaches the maximum number of evaluations
     * @throws TooManyIterationsException if the solver reaches the maximum number of iterations
     */
    @Test
    public void testSolveNoRoots() throws MaxEvalException, TooManyIterationsException {
        final double tolerance = 1e-9;
        final int maxEvaluations = 50;

        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        final BaseSecantSolver solver = new IllinoisSolver(tolerance, maxEvaluations, function);
        assertThrows(NoRootsException.class, () -> solver.solve(maxEvaluations, function, 0.0, 2.0, AllowedSolution.ANY_SIDE));
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has complex roots.
     *
     * @throws MaxEvalException if the solver reaches the maximum number of evaluations
     * @throws TooManyIterationsException if the solver reaches the maximum number of iterations
     */
    @Test
    public void testSolveComplexRoots() throws MaxEvalException, TooManyIterationsException {
        final double tolerance = 1e-9;
        final int maxEvaluations = 50;

        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sqrt(2) * Math.sin(Math.PI * x);
            }
        };

        final BaseSecantSolver solver = new IllinoisSolver(tolerance, maxEvaluations, function);
        final double root = solver.solve(maxEvaluations, function, 0.0, 2.0, AllowedSolution.ANY_SIDE);

        assertEquals(Precision.round(0.664593084304831, 9), root, tolerance);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has a root at a boundary.
     *
     * @throws MaxEvalException if the solver reaches the maximum number of evaluations
     * @throws TooManyIterationsException if the solver reaches the maximum number of iterations
     */
    @Test
    public void testSolveRootAtBoundary() throws MaxEvalException, TooManyIterationsException {
        final double tolerance = 1e-9;
        final int maxEvaluations = 50;

        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        final BaseSecantSolver solver = new IllinoisSolver(tolerance, maxEvaluations, function);
        final double root = solver.solve(maxEvaluations, function, -1.0, 0.0, AllowedSolution.LEFT_SIDE);

        assertEquals(-1.0, root, tolerance);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has a root at a boundary, but with the allowed
     * solution being on the other side.
     *
     * @throws MaxEvalException if the solver reaches the maximum number of evaluations
     * @throws TooManyIterationsException if the solver reaches the maximum number of iterations
     */
    @Test
    public void testSolveRootAtBoundaryOtherSide() throws MaxEvalException, TooManyIterationsException {
        final double tolerance = 1e-9;
        final int maxEvaluations = 50;

        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        final BaseSecantSolver solver = new IllinoisSolver(tolerance, maxEvaluations, function);
        final double root = solver.solve(maxEvaluations, function, 0.0, 1.0, AllowedSolution.RIGHT_SIDE);

        assertEquals(1.0, root, tolerance);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has roots at the boundaries.
     *
     * @throws MaxEvalException if the solver reaches the maximum number of evaluations
     * @throws TooManyIterationsException if the solver reaches the maximum number of iterations
     */
    @Test
    public void testSolveRootsAtBoundaries() throws MaxEvalException, TooManyIterationsException {
        final double tolerance = 1e-9;
        final int maxEvaluations = 50;

        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        final BaseSecantSolver solver = new IllinoisSolver(tolerance, maxEvaluations, function);
        final double root1 = solver.solve(maxEvaluations, function, -1.0, 0.0, AllowedSolution.LEFT_SIDE);
        final double root2 = solver.solve(maxEvaluations, function, 0.0, 1.0, AllowedSolution.RIGHT_SIDE);

        assertEquals(-1.0, root1, tolerance);
        assertEquals(1.0, root2, tolerance);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has roots at the boundaries and a non-allowed solution.
     *
     * @throws MaxEvalException if the solver reaches the maximum number of evaluations
     * @throws TooManyIterationsException if the solver reaches the maximum number of iterations
     */
    @Test(expected = IllegallyConstructedSolverException.class)
    public void testSolveRootsAtBoundariesNoAllowedSolution() throws MaxEvalException, TooManyIterationsException {
        final double tolerance = 1e-9;
        final int maxEvaluations = 50;

        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        final BaseSecantSolver solver = new IllinoisSolver(tolerance, maxEvaluations, function);
        final double root1 = solver.solve(maxEvaluations, function, -1.0, 0.0, AllowedSolution.ABOVE_SIDE);
        final double root2 = solver.solve(maxEvaluations, function, 0.0, 1.0, AllowedSolution.BELOW_SIDE);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has a singularity at a boundary.
     *
     * @throws MaxEvalException if the solver reaches the maximum number of evaluations
     * @throws TooManyIterationsException if the solver reaches the maximum number of iterations
     */
    @Test(expected = SingularSolverException.class)
    public void testSolveSingularityAtBoundary() throws MaxEvalException, TooManyIterationsException {
        final double tolerance = 1e-9;
        final int maxEvaluations = 50;

        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x == 1) {
                    throw new SingularityException("Singularity at x = 1");
                }
                return Math.pow(x, 2) - 1;
            }

            @Override
            public double derivative(double x) {
                if (x == 1) {
                    throw new SingularityException("Singularity at x = 1");
                }
                return 2 * x;
            }
        };

        final BaseSecantSolver solver = new IllinoisSolver(tolerance, maxEvaluations, function);
        solver.solve(maxEvaluations, function, 0.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with a function that has a vertical asymptote at a boundary.
     *
     * @throws MaxEvalException if the solver reaches the maximum number of evaluations
     * @throws TooManyIterationsException if the solver reaches the maximum number of iterations
     */
    @Test(expected = VerticalAsymptoteException.class)
    public void testSolveVerticalAsymptoteAtBoundary() throws MaxEvalException, TooManyIterationsException {
        final double tolerance = 1e-9;
        final int maxEvaluations = 50;

        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new VerticalAsymptoteException("Vertical asymptote at x = 0");
                }
                return Math.pow(x, 2) - 1;
            }

            @Override
            public double derivative(double x) {
                if (x == 0) {
                    throw new VerticalAsymptoteException("Vertical asymptote at x = 0");
                }
                return 2 * x;
            }
        };

        final BaseSecantSolver solver = new IllinoisSolver(tolerance, maxEvaluations, function);
        solver.solve(maxEvaluations, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }
}