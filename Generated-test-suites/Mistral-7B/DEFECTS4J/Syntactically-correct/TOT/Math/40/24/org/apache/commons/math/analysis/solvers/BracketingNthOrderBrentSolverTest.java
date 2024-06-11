package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSimpleRoot() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = f -> Math.pow(x, 2) - 2;
        double root = solver.solve(100, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testFunctionWithTwoRoots() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = x -> Math.pow(x, 2) - 4;
        double root1 = solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(-2.0, root1, 1e-6);
        double root2 = solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(2.0, root2, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testFunctionWithNoBracketing() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = x -> Math.pow(x, 2) - 1;
        solver.solve(100, function, 1.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFunctionWithSmallMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1);
        UnivariateFunction function = x -> Math.pow(x, 2) - 4;
        solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testFunctionWithLeftSideAllowedSolution() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = x -> Math.pow(x, 2) - 4;
        double root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(-2.0, root, 1e-6);
    }

    @Test
    public void testFunctionWithRightSideAllowedSolution() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = x -> Math.pow(x, 2) - 4;
        double root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testFunctionWithBelowSideAllowedSolution() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = x -> Math.pow(x, 2) - 4;
        double root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.BELOW_SIDE);
        Assert.assertEquals(-2.0, root, 1e-6);
    }

    @Test
    public void testFunctionWithAboveSideAllowedSolution() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = x -> Math.pow(x, 2) - 4;
        double root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(2.0, root, 1e-6);
    }

}