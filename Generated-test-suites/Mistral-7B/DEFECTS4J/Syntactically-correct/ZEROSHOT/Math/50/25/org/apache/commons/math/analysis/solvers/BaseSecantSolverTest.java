package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        // Test default constructor
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        assertNotNull(solver);

        // Test constructor with absolute accuracy
        BaseSecantSolver solverAbs = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solverAbs);

        // Test constructor with relative and absolute accuracy
        BaseSecantSolver solverRelAbs = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solverRelAbs);

        // Test constructor with relative, absolute, and function value accuracy
        BaseSecantSolver solverRelAbsFunc = new BaseSecantSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        assertNotNull(solverRelAbsFunc);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithoutFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        solver.solve(1, null, 0.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidMin() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        UnivariateRealFunction function = x -> x * x - 4.0;
        solver.solve(1, function, -1.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidMax() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        UnivariateRealFunction function = x -> x * x - 4.0;
        solver.solve(1, function, 0.0, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        UnivariateRealFunction function = x -> x * x - 4.0;
        solver.solve(1, function, 0.0, 0.0, Double.NaN);
    }

    @Test(expected = MaxEvalException.class)
    public void testSolveWithMaxEval() throws MaxEvalException {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        UnivariateRealFunction function = x -> x * x - 4.0;
        solver.solve(0, function, 0.0, 0.0);
    }

    @Test
    public void testSolveSimple() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 0.0, 2.0);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testSolveSimpleInverted() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 2.0, 0.0);
        assertEquals(-1.0, root, 1e-6);
    }

    @Test
    public void testSolveCloseToRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 1.5, 1.7);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testSolveVeryCloseToRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 1.41421356, 1.41421357);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testSolveFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, 1e-8);
        UnivariateRealFunction function = x -> Math.sin(x) / x;
        double root = solver.solve(1, function, 1.0, 2.0);
        assertEquals(1.0, root, 1e-8);
    }

    @Test
    public void testSolveRegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 0.0, 2.0);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testSolveIllinois() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 0.0, 2.0);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testSolvePegasus() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 0.0, 2.0);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testAllowedSolutionAnySide() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 0.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testAllowedSolutionLeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 0.0, 2.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testAllowedSolutionRightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 0.0, 2.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testAllowedSolutionBelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 0.0, 2.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testAllowedSolutionAboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = x -> x * x - 4.0;
        double root = solver.solve(1, function, 0.0, 2.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertNull(root);
    }

    @Test
    public void testAllowedSolutionInvalid() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = x -> x * x - 4.0;
        assertFalse(solver.solve(1, function, 0.0, 2.0, BaseSecantSolver.AllowedSolution.INVALID) instanceof Double);
    }

}