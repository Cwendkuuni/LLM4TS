package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private UnivariateRealFunction testFunction;
    private BaseSecantSolver solver;

    @Before
    public void setup() {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        solver = new RegulaFalsiSolver(1.0e-6, testFunction);
    }

    @Test
    public void testSolveWithDefaults() throws MaxEvalException, NoBracketException {
        double result = solver.solve(100, testFunction, -5.0, 5.0);
        assertEquals(-2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithCustomStartValueAndDefaults() throws MaxEvalException, NoBracketException {
        double result = solver.solve(100, testFunction, -5.0, 5.0, 0.0);
        assertEquals(-2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithCustomAccuraciesAndDefaults() throws MaxEvalException, NoBracketException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-8, 1.0e-8, testFunction);
        double result = solver.solve(100, testFunction, -5.0, 5.0);
        assertEquals(-2.0, result, 1e-8);
    }

    @Test
    public void testSolveWithCustomAccuraciesFunctionValueAccuracyAndMethod() throws MaxEvalException, NoBracketException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-8, 1.0e-8, 1.0e-8, testFunction.getValue(0.0), Method.ILLINOIS);
        double result = solver.solve(100, testFunction, -5.0, 5.0);
        assertEquals(-2.0, result, 1e-8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAllowedSolution() {
        new RegulaFalsiSolver(1.0e-6, testFunction, AllowedSolution.NEITHER_SIDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeAbsoluteAccuracy() {
        new RegulaFalsiSolver(-1.0e-6, testFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRelativeAccuracy() {
        new RegulaFalsiSolver(1.0e-6, -1.0e-6, testFunction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeFunctionValueAccuracy() {
        new RegulaFalsiSolver(1.0e-6, 1.0e-6, -1.0e-6, testFunction);
    }

    @Test(expected = IllegalStateException.class)
    public void testSolverWithoutBounds() throws MaxEvalException, NoBracketException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, testFunction);
        solver.solve(100, testFunction);
        fail("IllegalStateException expected");
    }

    @Test(expected = NoBracketException.class)
    public void testSolverWithoutBracketing() throws MaxEvalException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, testFunction);
        solver.setMin(2.0);
        solver.setMax(2.0);
        solver.solve(100, testFunction);
        fail("NoBracketException expected");
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() throws MaxEvalException, NoBracketException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, testFunction);
        solver.setMaxEval(0);
        solver.solve(100, testFunction, -5.0, 5.0);
        fail("MaxEvalException expected");
    }

    @Test
    public void testAllowedSolution() throws MaxEvalException, NoBracketException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, testFunction);
        try {
            solver.solve(100, testFunction, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        solver = new RegulaFalsiSolver(1.0e-6, testFunction, AllowedSolution.LEFT_SIDE);
        double result = solver.solve(100, testFunction, -5.0, 5.0);
        assertEquals(-2.0, result, 1e-6);
        assertTrue(result < -1.0);

        solver = new RegulaFalsiSolver(1.0e-6, testFunction, AllowedSolution.RIGHT_SIDE);
        result = solver.solve(100, testFunction, -5.0, 5.0);
        assertEquals(-2.0, result, 1e-6);
        assertTrue(result > -1.0);

        solver = new RegulaFalsiSolver(1.0e-6, testFunction, AllowedSolution.BELOW_SIDE);
        result = solver.solve(100, testFunction, -5.0, 5.0);
        assertEquals(-2.0, result, 1e-6);
        assertTrue(testFunction.value(result) < 0);

        solver = new RegulaFalsiSolver(1.0e-6, testFunction, AllowedSolution.ABOVE_SIDE);
        result = solver.solve(100, testFunction, -5.0, 5.0);
        assertEquals(-2.0, result, 1e-6);
        assertTrue(testFunction.value(result) > 0);

        solver = new RegulaFalsiSolver(1.0e-6, testFunction, AllowedSolution.NEITHER_SIDE);
        try {
            solver.solve(100, testFunction, -5.0, 5.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithInvertedInterval() throws MaxEvalException, NoBracketException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, testFunction);
        solver.setMin(5.0);
        solver.setMax(-5.0);
        solver.solve(100, testFunction, -5.0, 5.0);
        double result = solver.getSolution();
        assertEquals(-2.0, result, 1e-6);
        assertTrue(result < 0);

        solver.setMin(-5.0);
        solver.setMax(5.0);
        result = solver.solve(100, testFunction, -5.0, 5.0);
        assertEquals(-2.0, result, 1e-6);
        assertTrue(result > 0);
    }

    @Test
    public void testSolverWithBracketingFunctionValueAndFunctionValueAccuracy() throws MaxEvalException, NoBracketException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, testFunction, 1.0e-8, 1.0e-8);
        solver.setMin(-5.5);
        solver.setMax(5.5);
        solver.solve(100, testFunction);
        double result = solver.getSolution();
        assertEquals(-2.0, result, 1e-8);

        solver.setMin(5.5);
        solver.setMax(-5.5);
        try {
            solver.solve(100, testFunction);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithRelativeAccuracy() throws MaxEvalException, NoBracketException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-4, testFunction);
        solver.setMin(-5.0);
        solver.setMax(5.0);
        solver.solve(100, testFunction);
        double result = solver.getSolution();
        assertEquals(-2.0, result, Precision.relative(1.0e-4, Math.abs(result)));
    }

    @Test
    public void testSolverWithAbsoluteAccuracy() throws MaxEvalException, NoBracketException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-4, testFunction, 1.0e-4);
        solver.setMin(-5.0);
        solver.setMax(5.0);
        solver.solve(100, testFunction);
        double result = solver.getSolution();
        assertEquals(-2.0, result, 1e-4);
    }

    @Test
    public void testSolverWithFunctionValueAccuracy() throws MaxEvalException, NoBracketException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, testFunction, 1.0e-8);
        solver.setMin(-5.0);
        solver.setMax(5.0);
        solver.solve(100, testFunction);
        double result = solver.getSolution();
        assertEquals(-2.0, result, 1e-8);
    }

}