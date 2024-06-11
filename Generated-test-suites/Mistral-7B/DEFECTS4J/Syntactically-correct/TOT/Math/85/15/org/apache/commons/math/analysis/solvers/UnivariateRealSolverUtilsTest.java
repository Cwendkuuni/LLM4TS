package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.LinearFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketedBrentSolver;
import org.apache.commons.math3.analysis.solvers.FibonacciSolver;
import org.apache.commons.math3.analysis.solvers.LevenbergMarquardtIterativeSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionException;

import org.junit.Test;

/**
 * Tests for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test solve method with default solver.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveDefaultSolver() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateFunction f = new LinearFunction(1.0, 2.0);
        double root = UnivariateRealSolverUtils.solve(f, 1.0, 3.0);
        assertEquals(2.0, root, 1e-5);
    }

    /**
     * Test solve method with specified solver and absolute accuracy.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveSpecifiedSolverAndAccuracy() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateFunction f = new LinearFunction(1.0, 2.0);
        double root = UnivariateRealSolverUtils.solve(f, 1.0, 3.0, 1e-6);
        assertEquals(2.0, root, 1e-7);
    }

    /**
     * Test bracket method with a linearly increasing function.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testBracketLinearlyIncreasingFunction() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateFunction f = new LinearFunction(1.0, 2.0);
        double[] brackets = UnivariateRealSolverUtils.bracket(f, 1.0, 2.0, 3.0);
        assertEquals(2.0, brackets[0], 1e-5);
        assertEquals(3.0, brackets[1], 1e-5);
    }

    /**
     * Test bracket method with a function with roots at -1 and 1.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testBracketFunctionWithRootsAtMinusOneAndOne() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateFunction f = new LinearFunction(-1.0, 0.0, 1.0);
        double[] brackets = UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, 2.0);
        assertEquals(-1.0, brackets[0], 1e-5);
        assertEquals(1.0, brackets[1], 1e-5);
    }

    /**
     * Test bracket method with a function that does not provide bracketing.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test(expected = NoBracketingException.class)
    public void testBracketFunctionThatDoesNotProvideBracketing() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateFunction f = new LinearFunction(-1.0, 1.0, 1.0);
        UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, 2.0);
    }

    /**
     * Test bracket method with a function that converges in maximum iterations.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testBracketFunctionThatConvergesInMaxIterations() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateFunction f = new LinearFunction(-1.0, 0.0, 1.0);
        UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, 1);
    }

    /**
     * Test solve method with a fibonacci solver.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveFibonacciSolver() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateFunction f = new LinearFunction(1.0, 2.0);
        double root = UnivariateRealSolverUtils.solve(f, 1.0, 3.0,
                new FibonacciSolver());
        assertEquals(2.0, root, 1e-5);
    }

    /**
     * Test solve method with a secant solver.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveSecantSolver() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateFunction f = new LinearFunction(1.0, 2.0);
        double root1 = UnivariateRealSolverUtils.solve(f, 1.0, 3.0,
                new SecantSolver());
        assertEquals(2.0, root1, 1e-5);
        double root2 = UnivariateRealSolverUtils.solve(f, root1, root1 + 1.0,
                new SecantSolver());
        assertEquals(2.0, root2, 1e-5);
    }

    /**
     * Test solve method with a bracketed brent solver.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveBracketedBrentSolver() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateFunction f = new LinearFunction(1.0, 2.0);
        double root = UnivariateRealSolverUtils.solve(f, 1.0, 3.0,
                new BracketedBrentSolver());
        assertEquals(2.0, root, 1e-5);
    }

    /**
     * Test solve method with a levenberg-marquardt iterative solver.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     * @throws PrecisionException
     */
    @Test
    public void testSolveLevenbergMarquardtIterativeSolver() throws ConvergenceException,
            FunctionEvaluationException, PrecisionException {
        UnivariateFunction f = new LinearFunction(1.0, 2.0);
        double root = UnivariateRealSolverUtils.solve(f, 1.0, 3.0,
                new LevenbergMarquardtIterativeSolver());
        assertEquals(2.0, root, 1e-5);
    }

    /**
     * Test solve method with a brent solver.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveBrentSolver() throws ConvergenceException,
            FunctionEvaluationException {
        UnivariateFunction f = new LinearFunction(1.0, 2.0);
        double root = UnivariateRealSolverUtils.solve(f, 1.0, 3.0,
                new BrentSolver());
        assertEquals(2.0, root, 1e-5);
    }
}