package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertThrowsExactly;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /** A test function for the solver. */
    private static final UnivariateFunction testFunction = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return Math.sin(x) + Math.cos(x);
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x) - Math.sin(x);
        }
    };

    /** The solver under test. */
    private BracketingNthOrderBrentSolver solver;

    /** A bracketing interval for the solver. */
    private double[] interval;

    /** The expected solution. */
    private double expectedSolution;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver(1.0e-8, 5);
        interval = new double[] {-3.0, 3.0};
        expectedSolution = Math.PI;
    }

    /**
     * Test that the solver returns the correct solution for a simple function.
     */
    @Test
    public void testSimpleFunction() {
        double solution = solver.solve(10, testFunction, interval[0], interval[1]);
        assertEquals(expectedSolution, solution, 1.0e-8);
    }

    /**
     * Test that the solver returns the correct solution for a simple function with an initial guess.
     */
    @Test
    public void testSimpleFunctionWithInitialGuess() {
        double solution = solver.solve(10, testFunction, interval[0], interval[1], 1.5);
        assertEquals(expectedSolution, solution, 1.0e-8);
    }

    /**
     * Test that the solver returns the correct solution for a simple function with relative and absolute accuracy.
     */
    @Test
    public void testSimpleFunctionWithAccuracy() {
        double relativeAccuracy = 1.0e-7;
        double absoluteAccuracy = 1.0e-8;
        double solution = solver.solve(10, testFunction, interval[0], interval[1], relativeAccuracy, absoluteAccuracy);
        assertEquals(expectedSolution, solution, absoluteAccuracy);
    }

    /**
     * Test that the solver returns the correct solution for a simple function with relative and absolute accuracy and an initial guess.
     */
    @Test
    public void testSimpleFunctionWithAccuracyAndInitialGuess() {
        double relativeAccuracy = 1.0e-7;
        double absoluteAccuracy = 1.0e-8;
        double solution = solver.solve(10, testFunction, interval[0], interval[1], 1.5, relativeAccuracy, absoluteAccuracy);
        assertEquals(expectedSolution, solution, absoluteAccuracy);
    }

    /**
     * Test that the solver fails if the function is not bracketing the root.
     */
    @Test
    public void testNotBracketingFunction() {
        interval[0] = -3.5;
        interval[1] = 3.5;
        assertThrows(NoBracketingException.class, () -> solver.solve(10, testFunction, interval[0], interval[1]));
    }

    /**
     * Test that the solver fails if the maximal order is too low.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testTooLowMaximalOrder() {
        solver = new BracketingNthOrderBrentSolver(1.0e-8, 1);
    }

    /**
     * Test that the solver fails if the function value accuracy is too low.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testTooLowFunctionValueAccuracy() {
        solver = new BracketingNthOrderBrentSolver(1.0e-10, 5, 1.0e-10, 1.0e-10);
    }

    /**
     * Test that the solver accepts any solution if the allowed solution is set to "ANY\_SIDE".
     */
    @Test
    public void testAnySideAllowedSolution() {
        expectedSolution = 0.0;
        double solution = solver.solve(10, testFunction, interval[0], interval[1], AllowedSolution.ANY_SIDE);
        assertEquals(expectedSolution, solution, 1.0e-8);
    }

    /**
     * Test that the solver returns the left solution if the allowed solution is set to "LEFT\_SIDE".
     */
    @Test
    public void testLeftSideAllowedSolution() {
        expectedSolution = interval[0];
        double solution = solver.solve(10, testFunction, interval[0], interval[1], AllowedSolution.LEFT_SIDE);
        assertEquals(expectedSolution, solution, 1.0e-8);
    }

    /**
     * Test that the solver returns the right solution if the allowed solution is set to "RIGHT\_SIDE".
     */
    @Test
    public void testRightSideAllowedSolution() {
        expectedSolution = interval[1];
        double solution = solver.solve(10, testFunction, interval[0], interval[1], AllowedSolution.RIGHT_SIDE);
        assertEquals(expectedSolution, solution, 1.0e-8);
    }

    /**
     * Test that the solver returns the below solution if the allowed solution is set to "BELOW\_SIDE".
     */
    @Test
    public void testBelowSideAllowedSolution() {
        interval[1] = 2.0;
        expectedSolution = interval[0];
        double solution = solver.solve(10, testFunction, interval[0], interval[1], AllowedSolution.BELOW_SIDE);
        assertEquals(expectedSolution, solution, 1.0e-8);
    }

    /**
     * Test that the solver returns the above solution if the allowed solution is set to "ABOVE\_SIDE".
     */
    @Test
    public void testAboveSideAllowedSolution() {
        interval[0] = 2.0;
        expectedSolution = interval[1];
        double solution = solver.solve(10, testFunction, interval[0], interval[1], AllowedSolution.ABOVE_SIDE);
        assertEquals(expectedSolution, solution, 1.0e-8);
    }

    /**
     * Test that the solver fails if an invalid allowed solution is given.
     */
    @Test
    public void testInvalidAllowedSolution() {
        assertThrowsExactly(IllegalArgumentException.class, () -> solver.solve(10, testFunction, interval[0], interval[1], "INVALID"));
    }

    /**
     * Test that the solver finds the same root multiple times with the same parameters.
     */
    @Test
    public void testSameRootMultipleTimes() {
        double solution1 = solver.solve(10, testFunction, interval[0], interval[1]);
        double solution2 = solver.solve(10, testFunction, interval[0], interval[1]);
        assertEquals(expectedSolution, solution1, 1.0e-8);
        assertEquals(expectedSolution, solution2, 1.0e-8);
        assertNotSame(solution1, solution2);
    }

}