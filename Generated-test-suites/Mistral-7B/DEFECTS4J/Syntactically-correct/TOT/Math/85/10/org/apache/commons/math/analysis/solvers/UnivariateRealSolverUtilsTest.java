package org.apache.commons.math.analysis.solvers.utils;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the UnivariateRealSolverUtils class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * A simple test function for testing the solver.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.sin(x) - x;
        }
    };

    /**
     * Tests the solve method with a simple test function and valid input.
     */
    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        double root = UnivariateRealSolverUtils.solve(TEST_FUNCTION, -1.0, 1.0);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    /**
     * Tests the solve method with a simple test function and invalid input.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Error");
            }
        };
        UnivariateRealSolverUtils.solve(testFunction, 0.0, 0.0);
    }

    /**
     * Tests the solve method with a simple test function and invalid interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgument() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        UnivariateRealSolverUtils.solve(testFunction, Double.NaN, Double.NaN);
    }

    /**
     * Tests the bracket method with a simple test function and valid input.
     */
    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        double[] brackets = UnivariateRealSolverUtils.bracket(TEST_FUNCTION, 0.0, -1.0, 1.0);
        Assert.assertArrayEquals(new double[]{ -1.0, 0.0 }, brackets, 1e-6);
    }

    /**
     * Tests the bracket method with a simple test function and invalid input.
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Error");
            }
        };
        UnivariateRealSolverUtils.bracket(testFunction, 0.0, 0.0, 0.0);
    }

    /**
     * Tests the bracket method with a simple test function and empty interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBracketIllegalArgument() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        UnivariateRealSolverUtils.bracket(testFunction, 0.0, 0.0, 0.0);
    }

    /**
     * Tests the bracket method with a simple test function and maximum iterations.
     */
    @Test
    public void testBracketMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        double[] brackets = UnivariateRealSolverUtils.bracket(TEST_FUNCTION, 0.0, -1.0, 1.0, 0);
        Assert.assertArrayEquals(new double[]{ Double.NaN, Double.NaN }, brackets);
    }
}