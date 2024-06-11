package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * A simple test function used to test the {@code BaseSecantSolver}.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 2;
        }

        @Override
        public double derivative(double x) {
             return 2.0 * x;
        }
    };

    /**
     * Test the {@code BaseSecantSolver} with the {@code RegulaFalsi} method
     * for finding the root of {@code x^2 - 2}.
     *
     * @throws NoBracketingException If the initial interval does not bracket the root
     * @throws MaxEvalExceededException If the maximum number of evaluations is exceeded
     */
    @Test
    public void testRegulaFalsi() throws NoBracketingException, MaxEvalExceededException {
        double min = -3.0;
        double max = 3.0;
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected UnivariateRealFunction createFunction() {
                return TEST_FUNCTION;
            }
        };
        double root = solver.solve(100, TEST_FUNCTION, min, max, 0.0);
        assertTrue(Precision.isClose(root, -1.0, 1e-6));
    }

    /**
     * Test the {@code BaseSecantSolver} with the {@code Illinois} method
     * for finding the root of {@code x^2 - 2}.
     *
     * @throws NoBracketingException If the initial interval does not bracket the root
     * @throws MaxEvalExceededException If the maximum number of evaluations is exceeded
     */
    @Test
    public void testIllinois() throws NoBracketingException, MaxEvalExceededException {
        double min = -3.0;
        double max = 3.0;
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected UnivariateRealFunction createFunction() {
                return TEST_FUNCTION;
            }
        };
        double root = solver.solve(100, TEST_FUNCTION, min, max, 0.0);
        assertTrue(Precision.isClose(root, -1.0, 1e-6));
    }

    /**
     * Test the {@code BaseSecantSolver} with the {@code Pegasus} method
     * for finding the root of {@code x^2 - 2}.
     *
     * @throws NoBracketingException If the initial interval does not bracket the root
     * @throws MaxEvalExceededException If the maximum number of evaluations is exceeded
     */
    @Test
    public void testPegasus() throws NoBracketingException, MaxEvalExceededException {
        double min = -3.0;
        double max = 3.0;
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected UnivariateRealFunction createFunction() {
                return TEST_FUNCTION;
            }
        };
        double root = solver.solve(100, TEST_FUNCTION, min, max, 0.0);
        assertTrue(Precision.isClose(root, -1.0, 1e-6));
    }

    /**
     * Test the {@code BaseSecantSolver} with the {@code RegulaFalsi} method
     * for finding the root of a constant function {@code x -> 0}.
     *
     * @throws NoBracketingException If the initial interval does not bracket the root
     * @throws MaxEvalExceededException If the maximum number of evaluations is exceeded
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRegulaFalsiConstantFunction() throws NoBracketingException, MaxEvalExceededException {
        double min = 0.0;
        double max = 0.0;
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected UnivariateRealFunction createFunction() {
                return (x) -> 0.0;
            }
        };
        solver.solve(1, TEST_FUNCTION, min, max, 0.0);
    }

    /**
     * Test the {@code BaseSecantSolver} with the {@code Illinois} method
     * for finding the root of a constant function {@code x -> 0}.
     *
     * @throws NoBracketingException If the initial interval does not bracket the root
     * @throws MaxEvalExceededException If the maximum number of evaluations is exceeded
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIllinoisConstantFunction() throws NoBracketingException, MaxEvalExceededException {
        double min = 0.0;
        double max = 0.0;
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected UnivariateRealFunction createFunction() {
                return (x) -> 0.0;
            }
        };
        solver.solve(1, TEST_FUNCTION, min, max, 0.0);
    }

    /**
     * Test the {@code BaseSecantSolver} with the {@code Pegasus} method
     * for finding the root of a constant function {@code x -> 0}.
     *
     * @throws NoBracketingException If the initial interval does not bracket the root
     * @throws MaxEvalExceededException If the maximum number of evaluations is exceeded
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testPegasusConstantFunction() throws NoBracketingException, MaxEvalExceededException {
        double min = 0.0;
        double max = 0.0;
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected UnivariateRealFunction createFunction() {
                return (x) -> 0.0;
            }
        };
        solver.solve(1, TEST_FUNCTION, min, max, 0.0);
    }
}