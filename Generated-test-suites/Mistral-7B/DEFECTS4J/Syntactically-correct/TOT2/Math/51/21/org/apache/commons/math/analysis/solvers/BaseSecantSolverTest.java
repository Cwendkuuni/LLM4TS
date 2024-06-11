package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        solver = new RegulaFalsiSolver(1e-6, function);
    }

    @Test
    public void testSolve() {
        double solution = solver.solve(100, function, -5.0, 5.0);
        Assert.assertEquals(0.0, Precision.round(solution, 6));
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEval() {
        solver = new RegulaFalsiSolver(1e-6, function);
        solver.solve(0, function, -5.0, 5.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmall() {
        solver = new RegulaFalsiSolver(1e-12, function);
        solver.solve(100, function, -5.0, 5.0);
    }

    @Test(expected = TooManyIterationsException.class)
    public void testTooManyIterations() {
        solver = new RegulaFalsiSolver(1e-6, function, 10);
        solver.solve(100, function, -5.0, 5.0);
    }

    @Test
    public void testSolveWithInitialGuess() {
        double solution = solver.solve(100, function, 1.0, 5.0);
        Assert.assertEquals(1.0, Precision.round(solution, 6));
    }

    @Test
    public void testSolveWithAllowedSolutionLeftSide() {
        solver = new RegulaFalsiSolver(1e-6, function, AllowedSolution.LEFT_SIDE);
        double solution = solver.solve(100, function, -5.0, 5.0);
        Assert.assertLess(0.0, solution);
    }

    @Test
    public void testSolveWithAllowedSolutionRightSide() {
        solver = new RegulaFalsiSolver(1e-6, function, AllowedSolution.RIGHT_SIDE);
        double solution = solver.solve(100, function, -5.0, 5.0);
        Assert.assertGreater(0.0, solution);
    }

    @Test
    public void testSolveWithAllowedSolutionBelowSide() {
        solver = new RegulaFalsiSolver(1e-6, function, AllowedSolution.BELOW_SIDE);
        UnivariateRealFunction functionBelowZero = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -x * x - 2;
            }
        };
        double solution = solver.solve(100, functionBelowZero, -5.0, 5.0);
        Assert.assertLess(0.0, solution);
    }

    @Test
    public void testSolveWithAllowedSolutionAboveSide() {
        solver = new RegulaFalsiSolver(1e-6, function, AllowedSolution.ABOVE_SIDE);
        UnivariateRealFunction functionAboveZero = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double solution = solver.solve(100, functionAboveZero, -5.0, 5.0);
        Assert.assertGreater(0.0, solution);
    }
}