package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        final double absoluteAccuracy = 1e-8;
        final double relativeAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-10;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                           absoluteAccuracy,
                                                           functionValueAccuracy,
                                                           BaseSecantSolver.Method.ILLINOIS);

        final double actualSolution = solver.solve(100, f, -10.0, 10.0);
        final double expectedSolution = -2.0;
        assertEquals(expectedSolution, actualSolution, Precision.EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithInitialGuess() {
        final double absoluteAccuracy = 1e-8;
        final double relativeAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-10;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                           absoluteAccuracy,
                                                           functionValueAccuracy,
                                                           BaseSecantSolver.Method.ILLINOIS);

        final double actualSolution = solver.solve(100, f, -5.0, 5.0);
        final double expectedSolution = -2.0;
        assertEquals(expectedSolution, actualSolution, Precision.EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        final double absoluteAccuracy = 1e-8;
        final double relativeAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-10;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                           absoluteAccuracy,
                                                           functionValueAccuracy,
                                                           BaseSecantSolver.Method.ILLINOIS);

        final AllowedSolution allowedSolution = AllowedSolution.RIGHT_SIDE;

        final double actualSolution = solver.solve(100, f, -10.0, 10.0,
                                                   AllowedSolution.RIGHT_SIDE);
        final double expectedSolution = -2.0;
        assertEquals(expectedSolution, actualSolution, Precision.EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolutionAndInitialGuess() {
        final double absoluteAccuracy = 1e-8;
        final double relativeAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-10;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                           absoluteAccuracy,
                                                           functionValueAccuracy,
                                                           BaseSecantSolver.Method.ILLINOIS);

        final AllowedSolution allowedSolution = AllowedSolution.RIGHT_SIDE;

        final double actualSolution = solver.solve(100, f, -5.0, 5.0,
                                                   AllowedSolution.RIGHT_SIDE);
        final double expectedSolution = -2.0;
        assertEquals(expectedSolution, actualSolution, Precision.EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveWithMaxEvalExceeded() {
        final double absoluteAccuracy = 1e-8;
        final double relativeAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-10;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1 / x);
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                           absoluteAccuracy,
                                                           functionValueAccuracy,
                                                           BaseSecantSolver.Method.ILLINOIS);

        solver.solve(1, f, 0.5, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test(expected = PrecisionException.class)
    public void testSolveWithPrecisionExceeded() {
        final double absoluteAccuracy = 1e-16;
        final double relativeAccuracy = 1e-14;
        final double functionValueAccuracy = 1e-18;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - 1 / (1 + x);
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                           absoluteAccuracy,
                                                           functionValueAccuracy,
                                                           BaseSecantSolver.Method.ILLINOIS);

        solver.solve(100, f, -1.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithUnsupportedAllowedSolution() {
        final double absoluteAccuracy = 1e-8;
        final double relativeAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-10;

        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy,
                                                           absoluteAccuracy,
                                                           functionValueAccuracy,
                                                           BaseSecantSolver.Method.ILLINOIS);

        solver.solve(100, f, -10.0, 10.0, AllowedSolution.BELOW_SIDE);
    }
}