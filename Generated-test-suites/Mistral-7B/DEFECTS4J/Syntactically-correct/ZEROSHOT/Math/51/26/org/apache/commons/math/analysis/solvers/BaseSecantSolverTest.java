package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction func = f -> Math.pow(f, 2) - 1;

        double root = solver.solve(100, func, -10, 10);
        assertEquals("Solver failed to find the root.", 0, root, 1e-8);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolver_RegulaFalsi_MaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction func = f -> Math.pow(f, 2) - 1;

        solver.solve(1, func, -10, 10);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolver_RegulaFalsi_NoBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction func = f -> FastMath.sin(f);

        solver.solve(100, func, -10, 10);
    }

    @Test
    public void testSolver_Illinois() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction func = f -> Math.pow(f, 2) - 1;

        double root = solver.solve(100, func, -10, 10);
        assertEquals("Solver failed to find the root.", 0, root, 1e-8);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolver_Illinois_MaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction func = f -> Math.pow(f, 2) - 1;

        solver.solve(1, func, -10, 10);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolver_Illinois_NoBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction func = f -> FastMath.sin(f);

        solver.solve(100, func, -10, 10);
    }

    @Test
    public void testSolver_Pegasus() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction func = f -> Math.pow(f, 2) - 1;

        double root = solver.solve(100, func, -10, 10);
        assertEquals("Solver failed to find the root.", 0, root, 1e-8);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolver_Pegasus_MaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction func = f -> Math.pow(f, 2) - 1;

        solver.solve(1, func, -10, 10);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolver_Pegasus_NoBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction func = f -> FastMath.sin(f);

        solver.solve(100, func, -10, 10);
    }

    @Test
    public void testAllowedSolutions() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        double root = solver.solve(100, f -> Math.pow(f, 2) - 1, -10, 10, 0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals("Solver returned an incorrect root.", 0, root, 1e-8);

        root = solver.solve(100, f -> Math.pow(f, 2) - 1, -10, 10, 0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals("Solver returned an incorrect root.", 0, root, 1e-8);

        root = solver.solve(100, f -> Math.pow(f, 2) - 1, -10, 10, 0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertTrue("Solver returned an incorrect side.", root < 0);

        root = solver.solve(100, f -> Math.pow(f, 2) - 1, -10, 10, 0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertTrue("Solver returned an incorrect side.", root > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllowedSolutions_Invalid() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        solver.solve(100, f -> Math.pow(f, 2) - 1, -10, 10, 0, BaseSecantSolver.AllowedSolution.QUARTER_SIDE);
    }
}