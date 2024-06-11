package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;

import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_PolynomialFunction() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        double root = solver.solve(function, -1.0, 1.0);
        assertEquals(0.0, root, 1e-7);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_PolynomialFunction_MaxIterationsExceeded() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 0);
        solver.solve(function, -1.0, 1.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_PolynomialFunction_FunctionValueError() {
        UnivariateFunction function = x -> 1.0 / (x * x);
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        solver.solve(function, -1.0, 0.0);
    }

    @Test
    public void testSolve_PolynomialFunction_DifferentBracketing() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(1.0, root, 1e-7);
    }

    @Test
    public void testSolve_PolynomialFunction_DifferentStartValue() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        double root = solver.solve(function, 0.5, 1.0);
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolve_FunctionValueAccuracy() {
        UnivariateFunction function = x -> Math.pow(x, 3.0) - 1.0;
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 1e-8);
        double root = solver.solve(function, -2.0, 2.0);
        assertEquals(-1.0, root, 1e-8);
    }

    @Test
    public void testSolve_AllowedSolutionLEFT_SIDE() {
        UnivariateFunction function = x -> Math.pow(x, 3.0) - 1.0;
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 1e-8);
        double root = solver.solve(function, -2.0, 1.0);
        assertTrue(root < 0);
    }

    @Test
    public void testSolve_AllowedSolutionRIGHT_SIDE() {
        UnivariateFunction function = x -> Math.pow(x, 3.0) - 1.0;
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 1e-8);
        double root = solver.solve(function, 1.0, 2.0);
        assertTrue(root > 0);
    }

    @Test
    public void testSolve_AllowedSolutionLEFTOVER_SIDE() {
        UnivariateFunction function = x -> Math.pow(x, 3.0) - 1.0;
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 1e-8, AllowedSolution.LEFTOVER);
        double root = solver.solve(function, -2.0, 2.0);
        assertTrue(root < 0);
    }

    @Test
    public void testSolve_AllowedSolutionRIGHTOVER_SIDE() {
        UnivariateFunction function = x -> Math.pow(x, 3.0) - 1.0;
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 1e-8, AllowedSolution.RIGHTOVER);
        double root = solver.solve(function, -2.0, 2.0);
        assertTrue(root > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_IllegalAllowedSolution() {
        UnivariateFunction function = x -> Math.pow(x, 3.0) - 1.0;
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 1e-8, AllowedSolution.UNDER);
        solver.solve(function, -2.0, 2.0);
    }

    @Test
    public void testSolve_MaxIterations() {
        UnivariateFunction function = x -> Math.pow(x, 3.0) - 1.0;
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 1e-8, AllowedSolution.ANY_SIDE, 1);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, -2.0, 2.0));
    }

}