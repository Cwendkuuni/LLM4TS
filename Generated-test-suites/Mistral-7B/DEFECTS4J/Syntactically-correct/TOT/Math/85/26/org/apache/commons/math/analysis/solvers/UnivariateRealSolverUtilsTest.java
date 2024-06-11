package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketedBrentSolver;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.PowellSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionMismatchException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

/**
 * Tests for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test solving method with default solver.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolveWithDefaultSolver() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, -2, 4);
        double root = UnivariateRealSolverUtils.solve(f, -1.0, 1.0);
        assertEquals(0.0, Precision.safeSub(root, 0.5), 1e-3);
    }

    /**
     * Test solving method with specified solver and accuracy.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testSolveWithSpecifiedSolverAndAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, -2, 4);
        double root = UnivariateRealSolverUtils.solve(f, -1.0, 1.0, 1e-6);
        assertEquals(0.0, Precision.safeSub(root, 0.5), 1e-7);
    }

    /**
     * Test thrown exception when endpoints do not specify a valid interval.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenEndpointsInvalid() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, -2, 4);
        UnivariateRealSolverUtils.solve(f, 1.0, 1.0);
    }

    /**
     * Test thrown exception when function is null.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = MathRuntimeException.class)
    public void testMathRuntimeExceptionWhenFunctionIsNull() throws Exception {
        UnivariateRealSolverUtils.solve((UnivariateRealFunction) null, 0.0, 0.0);
    }

    /**
     * Test bracketing method with default solver.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketWithDefaultSolver() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, -2, 4);
        double[] bracketing = UnivariateRealSolverUtils.bracket(f, 0.0, -1.0, 1.0);
        assertArrayEquals(new double[]{ -1.0, 0.5 }, bracketing, 1e-3);
    }

    /**
     * Test bracketing method with specified solver and maximum iterations.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testBracketWithSpecifiedSolverAndMaxIterations() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, -2, 4);
        UnivariateRealSolver solver = new BrentSolver();
        double[] bracketing = UnivariateRealSolverUtils.bracket(f, 0.0, -1.0, 1.0, 5);
        assertArrayEquals(new double[]{ -1.0, 0.5 }, bracketing, 1e-3);
    }

    /**
     * Test thrown exception when function is null.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = MathRuntimeException.class)
    public void testMathRuntimeExceptionWhenFunctionIsNullInBracket() throws Exception {
        UnivariateRealSolverUtils.bracket((UnivariateRealFunction) null, 0.0, 0.0, 0.0);
    }

    /**
     * Test thrown exception when function is not continuous in the interval.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testConvergenceExceptionWhenFunctionNotContinuous() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, -2, 4);
        UnivariateRealSolver solver = new BracketedBrentSolver();
        UnivariateRealSolverUtils.bracket(f, 0.0, 0.0, 1.0);
    }

    /**
     * Test thrown exception when maximum iterations is reached.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testConvergenceExceptionWhenMaxIterationsReached() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, -2, 4);
        UnivariateRealSolver solver = new BracketedBrentSolver();
        UnivariateRealSolverUtils.bracket(f, 0.0, 0.0, 0.0, 0);
    }

    /**
     * Test thrown exception when function value is zero.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationExceptionWhenFunctionValueIsZero() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, -2, 4);
        UnivariateRealSolver solver = new BisectionSolver();
        UnivariateRealSolverUtils.bracket(f, 0.0, 0.0, 0.0);
    }

    /**
     * Test thrown exception when no bracketing is found.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = NoBracketingException.class)
    public void testNoBracketingExceptionWhenNoBracketingFound() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, -2, 4);
        UnivariateRealSolver solver = new SecantSolver();
        UnivariateRealSolverUtils.bracket(f, 0.0, 0.0, 1.0);
    }

    /**
     * Test thrown exception when precision is not valid for the default solver.
     * @throws Exception if an error occurs
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenPrecisionInvalid() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(3, -2, 4);
        UnivariateRealSolver solver = new PowellSolver();
        solver.setAbsoluteAccuracy(1.0 / 0.0);
        solver.solve(f, 0.0, 1.0);
    }
}