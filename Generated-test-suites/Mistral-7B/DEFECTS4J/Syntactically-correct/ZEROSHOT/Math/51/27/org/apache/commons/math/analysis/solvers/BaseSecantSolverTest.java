package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver<UnivariateRealFunction> solver;

    @Before
    public void setUp() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, f);
    }

    @Test
    public void testSolve() {
        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveInvalidFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        solver.solve(10, f, -10.0, 10.0, 0.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        solver.solve(10, f, -2.0, 2.0, 0.0);
    }

    @Test
    public void testSolveWithRelativeAndAbsoluteAccuracy() {
        double relativeAccuracy = 0.01;
        double absoluteAccuracy = 1e-6;
        solver = new IllinoisSolver(relativeAccuracy, absoluteAccuracy, IllinoisSolver.Method.ILLINOIS, f);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, Precision.relativelyEqual(relativeAccuracy, 0.0));
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        double functionValueAccuracy = 1e-8;
        solver = new IllinoisSolver(1e-6, 1e-6, IllinoisSolver.Method.ILLINOIS, f, functionValueAccuracy);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, functionValueAccuracy);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        AllowedSolution allowedSolution = AllowedSolution.BELOW_SIDE;
        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, f);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0, allowedSolution);
        assertEquals(0.0, root, 1e-6);
        assertTrue(root < 0.0);
    }

    @Test
    public void testSolveWithAllowedSolutionAndFunctionValueAccuracy() {
        AllowedSolution allowedSolution = AllowedSolution.BELOW_SIDE;
        double functionValueAccuracy = 1e-8;
        solver = new IllinoisSolver(1e-6, 1e-6, IllinoisSolver.Method.ILLINOIS, f, functionValueAccuracy);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0, allowedSolution);
        assertEquals(0.0, root, functionValueAccuracy);
        assertTrue(root < 0.0);
    }

    private UnivariateRealFunction f;
}