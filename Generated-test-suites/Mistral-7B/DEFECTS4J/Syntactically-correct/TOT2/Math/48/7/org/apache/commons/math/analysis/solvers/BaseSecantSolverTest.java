package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.function.Functions;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** Test function for root finding. */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(double x) {
            return x * x - 2;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    };

    /**
     * Tests the RegulaFalsi solver method with allowed solutions on both sides of the root.
     *
     * @throws ConvergenceException if the solver does not converge.
     * @throws PrecisionException if the solver does not find a solution within the given tolerance.
     */
    @Test
    public void testRegulaFalsiSolver() throws ConvergenceException, PrecisionException {
        final double ACCURACY = 1e-8;
        final double ROOT = 1.0;
        final double MIN = -2.0;
        final double MAX = 2.0;
        final double START_VALUE = 1.5;

        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(ACCURACY);
        final BaseSecantSolver<UnivariateRealFunction> testSolver =
                new BaseSecantSolver<>(solver, ACCURACY);

        testSolver.solve(TEST_FUNCTION, MIN, MAX, START_VALUE);
        assertEquals(ROOT, testSolver.getRoot(), ACCURACY);
    }

    /**
     * Tests the RegulaFalsi solver method with allowed solution on the left side of the root.
     *
     * @throws ConvergenceException if the solver does not converge.
     * @throws PrecisionException if the solver does not find a solution within the given tolerance.
     */
    @Test
    public void testRegulaFalsiSolverLeft() throws ConvergenceException, PrecisionException {
        final double ACCURACY = 1e-8;
        final double ROOT = 1.0;
        final double MIN = -2.0;
        final double MAX = 2.0;
        final double START_VALUE = 2.0;

        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(AllowedSolution.LEFT_SIDE);
        final BaseSecantSolver<UnivariateRealFunction> testSolver =
                new BaseSecantSolver<>(solver, ACCURACY);

        try {
            testSolver.solve(TEST_FUNCTION, MIN, MAX, START_VALUE);
            assertThrows(IllegalArgumentException.class, () -> testSolver.getRoot());
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Tests the RegulaFalsi solver method with allowed solution on the right side of the root.
     *
     * @throws ConvergenceException if the solver does not converge.
     * @throws PrecisionException if the solver does not find a solution within the given tolerance.
     */
    @Test
    public void testRegulaFalsiSolverRight() throws ConvergenceException, PrecisionException {
        final double ACCURACY = 1e-8;
        final double ROOT = 1.0;
        final double MIN = -2.0;
        final double MAX = 2.0;
        final double START_VALUE = -2.0;

        final BracketedUnivariateRealSolver<UnivariateRealFunction> solver =
                new RegulaFalsiSolver(AllowedSolution.RIGHT_SIDE);
        final BaseSecantSolver<UnivariateRealFunction> testSolver =
                new BaseSecantSolver<>(solver, ACCURACY);

        try {
            testSolver.solve(TEST_FUNCTION, MIN, MAX, START_VALUE);
            assertThrows(IllegalArgumentException.class, () -> testSolver.getRoot());
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Tests the Illinois solver method with allowed solutions on both sides of the root.
     *
     * @throws ConvergenceException if the solver does not converge.
     * @throws PrecisionException if the solver does not find a solution within the given tolerance.
     * @throws MaxIterationsExceededException if the maximum number of iterations is exceeded.
     */
    @Test
    public void testIllinoisSolver() throws ConvergenceException, PrecisionException, MaxIterationsExceededException {
        final double ACCURACY = 1e-8;
        final double ROOT = 0.0;
        final double MIN = -2.0;
        final double MAX = 2.0;
        final double START_VALUE = 1.5;

        final BaseSecantSolver<UnivariateRealFunction> testSolver =
                new BaseSecantSolver<>(new IllinoisSolver(ACCURACY), ACCURACY);

        testSolver.solve(TEST_FUNCTION, MIN, MAX, START_VALUE);
        assertEquals(ROOT, testSolver.getRoot(), ACCURACY);
    }

    /**
     * Tests the Illinois solver method with allowed solution on the left side of the root.
     *
     * @throws ConvergenceException if the solver does not converge.
     * @throws PrecisionException if the solver does not find a solution within the given tolerance.
     * @throws MaxIterationsExceededException if the maximum number of iterations is exceeded.
     */
    @Test
    public void testIllinoisSolverLeft() throws ConvergenceException, PrecisionException, MaxIterationsExceededException {
        final double ACCURACY = 1e-8;
        final double ROOT = 0.0;
        final double MIN = -2.0;
        final double MAX = 2.0;
        final double START_VALUE = 2.0;

        final BaseSecantSolver<UnivariateRealFunction> testSolver =
                new BaseSecantSolver<>(new IllinoisSolver(AllowedSolution.LEFT_SIDE), ACCURACY);

        try {
            testSolver.solve(TEST_FUNCTION, MIN, MAX, START_VALUE);
            assertThrows(IllegalArgumentException.class, () -> testSolver.getRoot());
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Tests the Illinois solver method with allowed solution on the right side of the root.
     *
     * @throws ConvergenceException if the solver does not converge.
     * @throws PrecisionException if the solver does not find a solution within the given tolerance.
     * @throws MaxIterationsExceededException if the maximum number of iterations is exceeded.
     */
    @Test
    public void testIllinoisSolverRight() throws ConvergenceException, PrecisionException, MaxIterationsExceededException {
        final double ACCURACY = 1e-8;
        final double ROOT = 0.0;
        final double MIN = -2.0;
        final double MAX = 2.0;
        final double START_VALUE = -2.0;

        final BaseSecantSolver<UnivariateRealFunction> testSolver =
                new BaseSecantSolver<>(new IllinoisSolver(AllowedSolution.RIGHT_SIDE), ACCURACY);

        try {
            testSolver.solve(TEST_FUNCTION, MIN, MAX, START_VALUE);
            assertThrows(IllegalArgumentException.class, () -> testSolver.getRoot());
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Tests the Pegasus solver method with allowed solutions on both sides of the root.
     *
     * @throws ConvergenceException if the solver does not converge.
     * @throws PrecisionException if the solver does not find a solution within the given tolerance.
     */
    @Test
    public void testPegasusSolver() throws ConvergenceException, PrecisionException {
        final double ACCURACY = 1e-8;
        final double ROOT = 1.0;
        final double MIN = -2.0;
        final double MAX = 2.0;
        final double START_VALUE = 1.5;

        final BaseSecantSolver<UnivariateRealFunction> testSolver =
                new BaseSecantSolver<>(new PegasusSolver(ACCURACY), ACCURACY);

        testSolver.solve(TEST_FUNCTION, MIN, MAX, START_VALUE);
        assertEquals(ROOT, testSolver.getRoot(), ACCURACY);
    }

    /**
     * Tests the Pegasus solver method with allowed solution on the left side of the root.
     *
     * @throws ConvergenceException if the solver does not converge.
     * @throws PrecisionException if the solver does not find a solution within the given tolerance.
     */
    @Test
    public void testPegasusSolverLeft() throws ConvergenceException, PrecisionException {
        final double ACCURACY = 1e-8;
        final double ROOT = 1.0;
        final double MIN = -2.0;
        final double MAX = 2.0;
        final double START_VALUE = 2.0;

        final BaseSecantSolver<UnivariateRealFunction> testSolver =
                new BaseSecantSolver<>(new PegasusSolver(AllowedSolution.LEFT_SIDE), ACCURACY);

        try {
            testSolver.solve(TEST_FUNCTION, MIN, MAX, START_VALUE);
            assertThrows(IllegalArgumentException.class, () -> testSolver.getRoot());
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Tests the Pegasus solver method with allowed solution on the right side of the root.
     *
     * @throws ConvergenceException if the solver does not converge.
     * @throws PrecisionException if the solver does not find a solution within the given tolerance.
     */
    @Test
    public void testPegasusSolverRight() throws ConvergenceException, PrecisionException {
        final double ACCURACY = 1e-8;
        final double ROOT = 1.0;
        final double MIN = -2.0;
        final double MAX = 2.0;
        final double START_VALUE = -2.0;

        final BaseSecantSolver<UnivariateRealFunction> testSolver =
                new BaseSecantSolver<>(new PegasusSolver(AllowedSolution.RIGHT_SIDE), ACCURACY);

        try {
            testSolver.solve(TEST_FUNCTION, MIN, MAX, START_VALUE);
            assertThrows(IllegalArgumentException.class, () -> testSolver.getRoot());
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

}