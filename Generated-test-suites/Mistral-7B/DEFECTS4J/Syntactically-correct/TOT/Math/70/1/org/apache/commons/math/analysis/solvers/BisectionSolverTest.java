package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootFoundException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link BisectionSolver} class.
 */
public class BisectionSolverTest {

    /**
     * A simple test function for the bisection solver.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x - 2.0, 2.0);
        }
    };

    /**
     * Test that the bisection solver finds the root of a simple function.
     */
    @Test
    public void testBisectionSolverSimpleFunction() {
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(TEST_FUNCTION, -2.5, 2.5);
            assertEquals(0.0, TEST_FUNCTION.value(root), 1e-6);
        } catch (MaxIterationsExceededException e) {
            fail("Bisection solver did not converge.");
        }
    }

    /**
     * Test that the bisection solver handles the case where the function
     * is not bracketed by the interval.
     */
    @Test(expected = NoRootFoundException.class)
    public void testBisectionSolverFunctionNotBracketed() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };
        solver.solve(f, -2.0, 0.0);
    }

    /**
     * Test that the bisection solver handles the case where the function
     * is not continuous.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverFunctionDiscontinuous() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    return Math.sqrt(x);
                } else {
                    throw new ArithmeticException("Error: Root not defined.");
                }
            }
        };
        solver.solve(f, -1.0, 1.0);
    }

    /**
     * Test that the bisection solver handles the case where the function
     * does not meet the accuracy requirement.
     */
    @Test
    public void testBisectionSolverFunctionDoesNotMeetAccuracy() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0) / 100.0;
            }
        };
        try {
            solver.solve(f, -2.5, 2.5);
            fail("Bisection solver should not have found a root.");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    /**
     * Test that the bisection solver handles the case where the function
     * is very flat, which can cause problems for other root finding methods.
     */
    @Test
    public void testBisectionSolverFunctionVeryFlat() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x - 1.0, 2.0)) - Math.exp(-Math.pow(x - 2.0, 2.0));
            }
        };
        try {
            double root = solver.solve(f, -3.0, 3.0);
            assertEquals(1.5, root, 1e-6);
        } catch (MaxIterationsExceededException e) {
            fail("Bisection solver did not converge.");
        }
    }

    /**
     * Test that the bisection solver handles the case where the function
     * is very noisy, which can cause problems for other root finding methods.
     */
    @Test
    public void testBisectionSolverFunctionVeryNoisy() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.random() * 0.1 + Math.sin(Math.PI * x);
            }
        };
        try {
            double root = solver.solve(f, -3.0, 3.0);
            fail("Bisection solver found a root in noisy function.");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    /**
     * Test that the bisection solver handles the case where the function
     * is very large, which can cause problems for other root finding methods.
     */
    @Test
    public void testBisectionSolverFunctionVeryLarge() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(100.0 * (x - 1.0));
            }
        };
        try {
            double root = solver.solve(f, -10.0, 10.0);
            fail("Bisection solver found a root in very large function.");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    /**
     * Test that the bisection solver handles the case where the function
     * is very small, which can cause problems for other root finding methods.
     */
    @Test
    public void testBisectionSolverFunctionVerySmall() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-100.0 * (x - 1.0));
            }
        };
        try {
            double root = solver.solve(f, -1.0e-10, 1.0e-10);
            fail("Bisection solver found a root in very small function.");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
}