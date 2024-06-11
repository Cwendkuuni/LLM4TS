package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction testFunction;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        testFunction = new TestFunction();
    }

    /**
     * Test that the solver returns the correct root for a simple function.
     */
    @Test
    public void testSolveSimpleFunction() {
        double root = solver.solve(testFunction, -1.0, 1.0);
        assertEquals(0.5, root, 1e-5);
    }

    /**
     * Test that the solver returns an error when the function is not continuous.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveNonContinuousFunction() {
        UnivariateRealFunction discontinuousFunction = new DiscontinuousFunction();
        solver.solve(discontinuousFunction, -1.0, 1.0);
    }

    /**
     * Test that the solver returns an error when the function is not defined in the interval.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionNotDefinedInInterval() {
        UnivariateRealFunction functionNotDefinedInInterval = new FunctionNotDefinedInInterval();
        solver.solve(functionNotDefinedInInterval, -1.0, 1.0);
    }

    /**
     * Test that the solver returns an error when the function does not bracket the root.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveFunctionDoesNotBracketRoot() {
        UnivariateRealFunction functionDoesNotBracketRoot = new FunctionDoesNotBracketRoot();
        solver.solve(functionDoesNotBracketRoot, -1.0, 1.0);
    }

    /**
     * Test that the solver returns an error when the maximum number of iterations is exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction functionWithMaxIterationsExceeded = new FunctionWithMaxIterationsExceeded();
        solver.setMaxIterations(10);
        solver.solve(functionWithMaxIterationsExceeded, -1.0, 1.0);
    }

    /**
     * Test that the solver returns an error when the function is too close to zero.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveFunctionTooCloseToZero() {
        UnivariateRealFunction functionTooCloseToZero = new FunctionTooCloseToZero();
        solver.solve(functionTooCloseToZero, -1.0, 1.0);
    }

    /**
     * A test function for the bisection solver.
     */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x - 2.0;
        }
    }

    /**
     * A discontinuous function for the bisection solver.
     */
    private static class DiscontinuousFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (x > 0.0) {
                throw new UnsupportedOperationException("Function not supported "
                        + "for positive x values.");
            }
            return x * x;
        }
    }

    /**
     * A function not defined in the interval for the bisection solver.
     */
    private static class FunctionNotDefinedInInterval implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (x < 0.0) {
                throw new FunctionEvaluationException("Function not defined "
                        + "for negative x values.");
            }
            return Math.sin(1.0/x);
        }
    }

    /**
     * A function that does not bracket the root for the bisection solver.
     */
    private static class FunctionDoesNotBracketRoot implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(x) * (x < 0.0 ? -1.0 : 1.0);
        }
    }

    /**
     * A function with max iterations exceeded for the bisection solver.
     */
    private static class FunctionWithMaxIterationsExceeded implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (Math.abs(x - 1.0) < 1e-12) {
                throw new MaxIterationsExceededException("Maximal number of iterations "
                        + "exceeded.");
            }
            return Math.tan(Math.PI / (x + 1.0));
        }
    }

    /**
     * A function that is too close to zero for the bisection solver.
     */
    private static class FunctionTooCloseToZero implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (Math.abs(x) < 1e-12) {
                throw new NumberIsTooSmallException("Function value is too small.");
            }
            return Math.log(Math.abs(x));
        }
    }
}