package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;

import org.junit.Test;

/**
 * Test cases for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double)}.
     */
    @Test
    public void testConstructor() {
        final double accuracy = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testConstructorWithMethod() {
        final double accuracy = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double)}.
     */
    @Test
    public void testConstructorWithAccuracyAndRelativeAccuracy() {
        final double accuracy = 1e-6;
        final double relativeAccuracy = 1e-8;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy, relativeAccuracy);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double)}.
     */
    @Test
    public void testConstructorWithAccuracyRelativeAccuracyFunctionValueAccuracy() {
        final double accuracy = 1e-6;
        final double relativeAccuracy = 1e-8;
        final double functionValueAccuracy = 1e-9;
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy, relativeAccuracy, functionValueAccuracy);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#getAllowed()}.
     */
    @Test
    public void testGetAllowed() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowed());
    }

    /**
     * Test method for {@link BaseSecantSolver#getMethod()}.
     */
    @Test
    public void testGetMethod() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithMinMax() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.QuadraticFunction(1.0, 2.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final double root = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, root, accuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithMinMaxAllowedSolution() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.QuadraticFunction(1.0, 2.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final double root = solver.solve(f, -1.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, accuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithMinMaxStartValue() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.QuadraticFunction(1.0, 2.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final double root = solver.solve(f, -1.0, 1.0, 0.0);
        assertEquals(0.0, root, accuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithMinMaxStartValueAllowedSolution() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.QuadraticFunction(1.0, 2.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final double root = solver.solve(f, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, accuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolveWithMinMaxStartValueDefaultAllowedSolution() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.QuadraticFunction(1.0, 2.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final double root = solver.solve(f, -1.0, 1.0, 0.0);
        assertEquals(0.0, root, accuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithMinMax() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.CubicFunction(-1.0, 1.0, 2.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final double root = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, root, accuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithMinMaxStartValueAllowedSolution() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.CubicFunction(-1.0, 1.0, 2.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final double root = solver.solve(f, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, accuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolveWithMinMaxStartValueDefaultAllowedSolution() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.CubicFunction(-1.0, 1.0, 2.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final double root = solver.solve(f, -1.0, 1.0, 0.0);
        assertEquals(0.0, root, accuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithMinMaxNoBracketing() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.CubicFunction(-1.0, 1.0, 1.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final int maxEval = 100;
        solver.solve(maxEval, f, -1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithMinMaxStartValueAllowedSolutionNoBracketing() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.CubicFunction(-1.0, 1.0, 1.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final int maxEval = 100;
        solver.solve(maxEval, f, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMinMaxMaxIterationsExceeded() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.CubicFunction(-1.0, 1.0, 2.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final int maxEval = 1;
        solver.solve(maxEval, f, -1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMinMaxStartValueAllowedSolutionMaxIterationsExceeded() {
        final double accuracy = 1e-6;
        final UnivariateRealFunction f = new PolynomialFunctions.CubicFunction(-1.0, 1.0, 2.0, -3.0);
        final BaseSecantSolver solver = new BaseSecantSolver(accuracy);
        final int maxEval = 1;
        solver.solve(maxEval, f, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
    }

}