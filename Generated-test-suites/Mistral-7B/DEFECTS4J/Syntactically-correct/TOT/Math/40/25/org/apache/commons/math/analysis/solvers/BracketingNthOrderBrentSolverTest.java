package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSimple() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionF(-1, 1);
        double root = 0.5;
        double[] solutions = { root, root };
        assertArrayEquals(solutions, solver.solve(100, f, -1, 1, AllowedSolution.ANY_SIDE));
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidOrder() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionF(-1, 1);
        double[] solutions = {};
        solver.solve(100, f, -1, 1, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testLeftSide() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionF(-1, 1);
        double leftRoot = 0.5;
        double[] solutions = { leftRoot };
        assertArrayEquals(solutions, solver.solve(100, f, -1, 1, AllowedSolution.LEFT_SIDE));
    }

    @Test
    public void testRightSide() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionF(-1, 1);
        double rightRoot = 0.5;
        double[] solutions = { rightRoot };
        assertArrayEquals(solutions, solver.solve(100, f, -1, 1, AllowedSolution.RIGHT_SIDE));
    }

    @Test
    public void testBelowSide() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionF(-1, 1);
        double belowRoot = 0.75;
        double[] solutions = { belowRoot };
        assertArrayEquals(solutions, solver.solve(100, f, -1, 1, AllowedSolution.BELOW_SIDE));
    }

    @Test
    public void testAboveSide() throws Exception {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new FunctionF(-1, 1);
        double aboveRoot = 0.25;
        double[] solutions = { aboveRoot };
        assertArrayEquals(solutions, solver.solve(100, f, -1, 1, AllowedSolution.ABOVE_SIDE));
    }

    private static class FunctionF implements UnivariateFunction {
        private final double a;
        private final double b;

        public FunctionF(double a, double b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public double value(double x) {
            return Math.pow(x - a, 2) + b;
        }
    }

}