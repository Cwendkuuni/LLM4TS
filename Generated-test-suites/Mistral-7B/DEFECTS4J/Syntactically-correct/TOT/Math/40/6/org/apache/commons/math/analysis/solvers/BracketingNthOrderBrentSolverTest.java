package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionMamaException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.function.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for {@link BracketingNthOrderBrentSolver} class.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test when the root is bracketed.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testRootBracketed() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.1 * Math.sin(x) - 0.5;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 4);
        double result = solver.solve(100, f, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(-0.536559157266623, result, 1e-6);
    }

    /**
     * Test when the root is not bracketed.
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = NoBracketingException.class)
    public void testRootNotBracketed() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 4);
        solver.solve(100, f, -10, 10, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test when the function returns NaN for some input values.
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNaNFunction() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.log(x);
                } else {
                    throw new UnsupportedOperationException("Not a number");
                }
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 4);
        solver.solve(100, f, -1, 1, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test when the function value accuracy is too low.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testFunctionValueAccuracyTooLow() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 4, 1e-10, 4);
            solver.solve(100, f, -1, 1, AllowedSolution.ANY_SIDE);
            fail("Expected MathInternalError when function value accuracy is too low.");
        } catch (MathInternalError e) {
            // expected
        }
    }

    /**
     * Test when the maximal order is too low.
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testMaximalOrderTooLow() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 5) - 1;
            }
        };
        try {
            BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3, 1e-5, 4);
            solver.solve(100, f, -2, 2, AllowedSolution.ANY_SIDE);
            fail("Expected NumberIsTooSmallException when maximal order is too low.");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    /**
     * Test when the root is very close to the minimal value.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testRootVeryCloseToMin() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 0.01;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 4);
        double result = solver.solve(100, f, -0.02, 0.02, AllowedSolution.ANY_SIDE);
        assertEquals(-0.01, result, 1e-8);
    }

    /**
     * Test when the root is very close to the maximal value.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testRootVeryCloseToMax() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 0.01;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 4);
        double result = solver.solve(100, f, 0.02, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.01, result, 1e-8);
    }

    /**
     * Test when the root is between the minimal and maximal values.
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testRootBetweenMinAndMax() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 1;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 4);
        double result = solver.solve(100, f, -1, 1, AllowedSolution.ANY_SIDE);
        assertEquals(-0.5, result, 1e-5);
    }

    /**
     * Test when the maximum number of evaluations is reached.
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxEval() throws Exception {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 4);
        solver.solve(1, f, -1, 1, AllowedSolution.ANY_SIDE);
    }

}