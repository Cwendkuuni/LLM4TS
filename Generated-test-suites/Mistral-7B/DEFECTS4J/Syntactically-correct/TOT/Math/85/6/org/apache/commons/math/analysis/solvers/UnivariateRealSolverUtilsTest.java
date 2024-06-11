package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BracketedBrentSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;

/**
 * Test class for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test the solver with a polynomial function.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testPolynomialFunction() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(1, -1, 1, 1);
        double x0 = 1.0;
        double x1 = 0.0;
        double solution = UnivariateRealSolverUtils.solve(function, x0, x1);
        assertEquals(0.0, solution, 1e-12);
    }

    /**
     * Test the solver with a bracketed interval and a polynomial function.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testPolynomialFunctionBracketed() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(1, -1, 1, 1);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        UnivariateRealSolver solver = UnivariateRealSolverFactory.newBrentSolver();
        double solution = solver.solve(function, bracketingValues[0], bracketingValues[1]);
        assertEquals(0.0, solution, 1e-12);
    }

    /**
     * Test the solver with a function where the root is not bracketed.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testUnbracketedRoot() throws Exception {
        UnivariateRealFunction function = new Erf(0.0);
        double initial = 0.0;
        double lowerBound = -5.0;
        double upperBound = 5.0;
        assertThrows(NoRootsException.class, () -> UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound));
    }

    /**
     * Test the solver with a function where the root is at the endpoint.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testRootAtEndpoint() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(1, 1, -1, 1);
        double initial = 1.0;
        double lowerBound = 1.0;
        double upperBound = 1.0;
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        UnivariateRealSolver solver = UnivariateRealSolverFactory.newBrentSolver();
        double solution = solver.solve(function, bracketingValues[0], bracketingValues[1]);
        assertEquals(1.0, solution, 1e-12);
    }

    /**
     * Test the solver with a function where the root is at the initial point.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testRootAtInitialPoint() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(1, 1, 1, -1);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        UnivariateRealSolver solver = UnivariateRealSolverFactory.newBrentSolver();
        double solution = solver.solve(function, bracketingValues[0], bracketingValues[1]);
        assertEquals(1.0, solution, 1e-12);
    }

    /**
     * Test the solver with a function where the root is outside the interval.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testRootOutsideInterval() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(1, 1, -2, 1);
        double initial = 0.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        UnivariateRealSolver solver = UnivariateRealSolverFactory.newBrentSolver();
        assertThrows(ConvergenceException.class, () -> solver.solve(function, bracketingValues[0], bracketingValues[1]));
    }

    /**
     * Test the solver with a function where the maximum number of iterations is exceeded.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(1, 1, -1, 1);
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateRealSolver solver = UnivariateRealSolverFactory.newBracketedBrentSolver(5);
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, bracketingValues[0], bracketingValues[1]));
    }

    /**
     * Test the solver with a function that throws an exception.
     *
     * @throws Exception if test fails
     */
    @Test
    public void testFunctionException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                if (x < 0) {
                    throw new IllegalArgumentException("x must be non-negative");
                }
                return Math.sqrt(x);
            }
        };
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateRealSolver solver = UnivariateRealSolverFactory.newBrentSolver();
        assertThrows(IllegalArgumentException.class, () -> solver.solve(function, initial, lowerBound, upperBound));
    }
}