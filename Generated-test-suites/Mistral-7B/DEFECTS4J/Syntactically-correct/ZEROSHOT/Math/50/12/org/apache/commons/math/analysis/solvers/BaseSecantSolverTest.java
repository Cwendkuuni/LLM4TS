package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.BeforeClass;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static UnivariateFunction f;

    @BeforeClass
    public static void setup() {
        f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
    }

    @Test
    public void testSolve_default() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, Precision.EPSILON);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolve_maxEval() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, 0);
        solver.solve(f, -1.0, 1.0);
    }

    @Test
    public void testSolve_relative() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                0.001, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testSolve_absolute() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, 1e-5);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, 1e-5);
    }

    @Test
    public void testSolve_functionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                0.001, 1e-5, 1e-5);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testSolve_allowedSolution_left() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                0.001, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        double result = solver.solve(f, -1.0, 1.0);
        assertTrue(result < 0);
    }

    @Test
    public void testSolve_allowedSolution_right() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                0.001, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        double result = solver.solve(f, -1.0, 1.0);
        assertTrue(result > 0);
    }

    @Test
    public void testSolve_allowedSolution_below() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                0.001, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        double result = solver.solve(f, -1.0, 1.0);
        assertTrue(result < 0);
    }

    @Test
    public void testSolve_allowedSolution_above() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                0.001, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        double result = solver.solve(f, -1.0, 1.0);
        assertTrue(result > 0);
    }

    @Test
    public void testSolve_allowedSolution_anySide() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                0.001, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double result = solver.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, Precision.EPSILON);
    }

    @Test
    public void testSolve_allowedSolution_inverted() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(
                0.001, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double[] results = solver.solve(f, -1.0, 1.0, -1.0, 1.0);
        assertArrayEquals(new double[] { 0.0, 0.0 }, results, 0.0);
    }
}