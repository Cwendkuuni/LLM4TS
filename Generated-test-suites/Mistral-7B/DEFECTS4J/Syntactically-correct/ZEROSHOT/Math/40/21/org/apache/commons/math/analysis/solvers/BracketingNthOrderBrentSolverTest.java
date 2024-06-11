package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolveSimple() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionF();
        double result = solver.solve(100, f, -1.5, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, 1e-7);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveInvalidArguments() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, -1);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionG();
        solver.solve(100, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolveLeftSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionF();
        double result = solver.solve(100, f, -1.5, 1.5, AllowedSolution.LEFT_SIDE);
        assertEquals(-1.0, result, 1e-7);
    }

    @Test
    public void testSolveRightSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionF();
        double result = solver.solve(100, f, -1.5, 1.5, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.0, result, 1e-7);
    }

    @Test
    public void testSolveBelowSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionH();
        double result = solver.solve(100, f, -3.0, 3.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-1.5, result, 1e-7);
    }

    @Test
    public void testSolveAboveSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionH();
        double result = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(1.5, result, 1e-7);
    }

    private static class FunctionF implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.sqrt(Math.abs(x)) - Math.sin(Math.PI * x);
        }

    }

    private static class FunctionG implements UnivariateFunction {

        @Override
        public double value(double x) {
            if (x < -1.0) {
                return x * x + 1.0;
            } else if (x > 1.0) {
                return x * x - 1.0;
            }
            throw new IllegalStateException();
        }

    }

    private static class FunctionH implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 3.0) - x;
        }

    }

}