package org.apache.commons.math.analysis.solvers.utils;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test solver with a simple function.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolver() throws Exception {
        UnivariateRealFunction f = x -> x * x - 4.0;
        double root = UnivariateRealSolverUtils.solve(f, -2.0, 2.0);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test solver with a function with no root in the interval.
     * @throws Exception if an error occurs
     */
    @Test(expected = ConvergenceException.class)
    public void testSolverNoRoot() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1.0;
        UnivariateRealSolverUtils.solve(f, -2.0, 2.0);
    }

    /**
     * Test solver with a function that is not continuous in the interval.
     * @throws Exception if an error occurs
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testSolverDiscontinuous() throws Exception {
        UnivariateRealFunction f = x -> {
            if (x > 0.0) {
                return Math.sqrt(x);
            } else {
                throw new FunctionEvaluationException("Cannot evaluate the function at x=" + x);
            }
        };
        UnivariateRealSolverUtils.solve(f, -1.0, 1.0);
    }

    /**
     * Test bracketing algorithm with a simple function.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketing() throws Exception {
        UnivariateRealFunction f = x -> x * x - 4.0;
        double[] brackets = UnivariateRealSolverUtils.bracket(f, 0.0, -2.0, 2.0);
        Assert.assertArrayEquals(new double[]{ -1.5, 0.5 }, brackets, 1e-6);
    }

    /**
     * Test bracketing algorithm with a function with no root in the interval.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoBracketingException.class)
    public void testBracketingNoRoot() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1.0;
        UnivariateRealSolverUtils.bracket(f, -2.0, 2.0);
    }

    /**
     * Test bracketing algorithm with a function that is not continuous in the interval.
     * @throws Exception if an error occurs
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testBracketingDiscontinuous() throws Exception {
        UnivariateRealFunction f = x -> {
            if (x > 0.0) {
                return Math.sqrt(x);
            } else {
                throw new FunctionEvaluationException("Cannot evaluate the function at x=" + x);
            }
        };
        UnivariateRealSolverUtils.bracket(f, -1.0, 1.0);
    }

    /**
     * Test bracketing algorithm with a function that requires many iterations to converge.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBracketingManyIterations() throws Exception {
        UnivariateRealFunction f = x -> Math.sin(x) / x;
        double[] brackets = UnivariateRealSolverUtils.bracket(f, -3.0, 3.0, 1000);
        Assert.assertArrayEquals(new double[]{ -1.4, 1.4 }, brackets, 1e-6);
    }

    /**
     * Test bracketing algorithm with a function that has no solution in the given interval.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testBracketingNoSolution() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 2.0;
        UnivariateRealSolverUtils.bracket(f, -2.0, 2.0, 1);
    }

}