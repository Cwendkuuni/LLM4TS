package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.PrecisionModifiedException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.annotation.Checker;
import org.apache.commons.math3.util.annotation.MathThreadSafe;
import org.junit.Test;

/**
 * Unit tests for {@link BaseSecantSolver}.
 */
@MathThreadSafe
public class BaseSecantSolverTest {

    /**
     * Function for testing.
     */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(double x) {
            return x * x - 4.0;
        }

        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }
    };

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     *
     * @throws MaxEvalExceededException if the maximum number of evaluations is exceeded
     * @throws PrecisionModifiedException if the precision is modified during the execution of the solver
     */
    @Test
    public void testSolve() throws MaxEvalExceededException, PrecisionModifiedException {
        final double relativeAccuracy = 1e-6;
        final double absoluteAccuracy = 1e-6;
        final int maxEval = 10;

        final BaseSecantSolver solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy, FUNCTION);
        assertEquals("Should find the root of x^2 - 4 = 0", 2.0, solver.solve(maxEval, FUNCTION, -3.0, 3.0, 1.0),
                0.001);

        // Test with small interval
        solver.setMaxEval(10);
        solver.setBracketingCheck(false); // since the interval is small, it is not necessary to check it

        final double root = Math.sqrt(2.0);
        assertEquals("Should find the root of x^2 - 4 = 0 with a small interval", root, solver.solve(maxEval, FUNCTION, 1.0 - 1e-7, 1.0 + 1e-7),
                1e-6);

        // Test with wrong function
        try {
            new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public double derivative(double x) {
                    throw new UnsupportedOperationException();
                }
            }).solve(maxEval, FUNCTION, 1.0, 3.0, 1.0);
            fail("Should have thrown an exception");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // Test with a function that doesn't have a root
        final UnivariateRealFunction nonRootFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }

            @Override
            public double derivative(double x) {
                return 1.0;
            }
        };
        try {
            new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, nonRootFunction).solve(maxEval, nonRootFunction, -1.0, 1.0, 0.0);
            fail("Should have thrown an exception");
        } catch (MaxEvalExceededException e) {
            // expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     *
     * @throws MaxEvalExceededException if the maximum number of evaluations is exceeded
     * @throws PrecisionModifiedException if the precision is modified during the execution of the solver
     */
    @Test
    public void testSolveWithAllowedSolution()
            throws MaxEvalExceededException, PrecisionModifiedException {
        final double relativeAccuracy = 1e-6;
        final double absoluteAccuracy = 1e-6;
        final int maxEval = 10;

        final BaseSecantSolver solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy, FUNCTION);

        // Test with left solution
        final AllowedSolution leftSolution = AllowedSolution.LEFT_SIDE;
        assertEquals("Should find the leftmost root of x^2 - 4 = 0", -2.0, solver.solve(maxEval, FUNCTION, -3.0, 3.0, 1.0, leftSolution),
                0.001);

        // Test with right solution
        final AllowedSolution rightSolution = AllowedSolution.RIGHT_SIDE;
        assertEquals("Should find the rightmost root of x^2 - 4 = 0", 2.0, solver.solve(maxEval, FUNCTION, -3.0, 3.0, 1.0, rightSolution),
                0.001);

        // Test with below solution
        final AllowedSolution belowSolution = AllowedSolution.BELOW_SIDE;
        assertEquals("Should find the root of x^2 - 4 = 0 below 0", -2.0, solver.solve(maxEval, FUNCTION, -3.0, 3.0, 0.0, belowSolution),
                0.001);

        // Test with above solution
        final AllowedSolution aboveSolution = AllowedSolution.ABOVE_SIDE;
        assertEquals("Should find the root of x^2 - 4 = 0 above 0", 2.0, solver.solve(maxEval, FUNCTION, -3.0, 3.0, 0.0, aboveSolution),
                0.001);

        // Test with wrong function
        try {
            new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public double derivative(double x) {
                    throw new UnsupportedOperationException();
                }
            }).solve(maxEval, FUNCTION, 1.0, 3.0, 1.0, AllowedSolution.LEFT_SIDE);
            fail("Should have thrown an exception");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     *
     * @throws MaxEvalExceededException if the maximum number of evaluations is exceeded
     * @throws PrecisionModifiedException if the precision is modified during the execution of the solver
     */
    @Test
    public void testSolveWithoutAllowedSolution()
            throws MaxEvalExceededException, PrecisionModifiedException {
        final double relativeAccuracy = 1e-6;
        final double absoluteAccuracy = 1e-6;
        final int maxEval = 10;

        final BaseSecantSolver solver = new RegulaFalsiSolver(relativeAccuracy, absoluteAccuracy, FUNCTION);

        // Test without allowed solution
        assertEquals("Should find the root of x^2 - 4 = 0", 2.0, solver.solve(maxEval, FUNCTION, -3.0, 3.0, 1.0),
                0.001);
    }
}

enum AllowedSolution {
    LEFT_SIDE,
    RIGHT_SIDE,
    BELOW_SIDE,
    ABOVE_SIDE,
    ANY_SIDE
}