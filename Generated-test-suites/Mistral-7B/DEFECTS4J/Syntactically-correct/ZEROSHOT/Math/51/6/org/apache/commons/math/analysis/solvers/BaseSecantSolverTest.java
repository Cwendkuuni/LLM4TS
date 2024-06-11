package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private UnivariateFunction function;
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        function = new PolynomialFunction(new double[]{-1.0, 4.0, -4.0, 4.0, -1.0});
        solver = new IllinoisSolver(1e-10, IllinoisSolver.ILLIADIS);
    }

    @Test
    public void testNullFunction() {
        assertNull(solver.solve(1, null, 0.0, 1.0, AllowedSolution.ANY_SIDE));
    }

    @Test
    public void testNegativeMax() {
        assertNull(solver.solve(1, function, 0.0, -1.0, AllowedSolution.ANY_SIDE));
    }

    @Test
    public void testNullAllowedSolution() {
        assertThrows(NullPointerException.class, () -> solver.solve(1, function, 0.0, 1.0, null));
    }

    @Test
    public void testNegativeAllowedSolution() {
        assertThrows(IllegalArgumentException.class, () -> solver.solve(1, function, 0.0, 1.0, AllowedSolution.BELOW_SIDE));
    }

    @Test
    public void testSolveSingleRoot() throws MaxEvalException {
        assertEquals(1.0, solver.solve(1, function, 0.0, 2.0, AllowedSolution.ANY_SIDE), 1e-10);
    }

    @Test
    public void testSolveTwoRoots() throws MaxEvalException {
        assertEquals(0.5, solver.solve(1, function, 0.0, 2.0, AllowedSolution.RIGHT_SIDE), 1e-10);
        assertEquals(-1.5, solver.solve(1, function, 0.0, 2.0, AllowedSolution.LEFT_SIDE), 1e-10);
    }

    @Test
    public void testSolveThreeRoots() throws MaxEvalException {
        assertEquals(-1.0, solver.solve(1, function, 0.0, 2.0, AllowedSolution.LEFT_SIDE), 1e-10);
        assertEquals(1.0, solver.solve(1, function, 0.0, 2.0, AllowedSolution.RIGHT_SIDE), 1e-10);
        assertFalse(solver.solve(1, function, 0.0, 2.0, AllowedSolution.BELOW_SIDE) instanceof Double);
    }

    @Test
    public void testSolveZeroFunction() throws MaxEvalException {
        UnivariateFunction zeroFunction = f -> 0.0;
        assertEquals(0.0, solver.solve(1, zeroFunction, 0.0, 1.0, AllowedSolution.ANY_SIDE), 1e-15);
    }

    @Test
    public void testSolveFailedConvergence() throws MaxEvalException {
        function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0, -1.0});
        assertThrows(MaxEvalException.class, () -> solver.solve(1, function, 0.0, 1.0, AllowedSolution.ANY_SIDE));
    }

    @Test
    public void testSolveDifferentMethods() {
        IllinoisSolver illinoisSolver = new IllinoisSolver(1e-10, IllinoisSolver.ILLIADIS);
        PegasusSolver pegasusSolver = new PegasusSolver(1e-10, PegasusSolver.PEGASUS_F1);
        UnivariateFunction function1 = new PolynomialFunction(new double[]{-1.0, 4.0, -4.0, 4.0, -1.0});

        assertEquals(1.0, illinoisSolver.solve(1, function1, 0.0, 2.0, AllowedSolution.ANY_SIDE), 1e-10);
        assertNotSame(1.0, pegasusSolver.solve(1, function1, 0.0, 2.0, AllowedSolution.ANY_SIDE));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNoBracketing() {
        BaseSecantSolver solver = new IllinoisSolver(1e-10, IllinoisSolver.ILLIADIS);
        UnivariateFunction function = new PolynomialFunction(new double[]{0.0, 1.0, 1.0});
        solver.solve(1, function, 0.0, 0.0);
    }

    private void verifyBracketing(BaseSecantSolver solver, UnivariateFunction function, double min, double max) {
        try {
            solver.solve(1, function, min, max, AllowedSolution.ANY_SIDE);
            assertTrue(FunctionUtils.brackets(function, min, max));
        } catch (MaxEvalException e) {
            assertTrue(FunctionUtils.brackets(function, min, max));
        }
    }

}