package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolverWithDefaultParameters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction func = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);

        double root = solver.solve(100, func, -3.0, 3.0);
        assertEquals("The root of sin(x)^2 + exp(-x) was not found.", 0.0, root, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolverWithLowerMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1, 2);
        UnivariateFunction func = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);

        solver.solve(100, func, -3.0, 3.0);
    }

    @Test
    public void testSolverWithCustomParameters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 8);
        UnivariateFunction func = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);

        double root = solver.solve(100, func, -3.0, 3.0);
        assertEquals("The root of sin(x)^2 + exp(-x) was not found.", 0.0, root, 1e-8);
    }

    @Test
    public void testSolverWithAllowedSolutionLeftSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction func = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);

        double root = solver.solve(100, func, -3.0, 3.0, AllowedSolution.LEFT_SIDE);
        assertEquals("The root of sin(x)^2 + exp(-x) was not found on the left side.", 0.0, root, 1e-6);
    }

    @Test
    public void testSolverWithAllowedSolutionRightSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction func = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);

        double root = solver.solve(100, func, -3.0, 3.0, AllowedSolution.RIGHT_SIDE);
        assertEquals("The root of sin(x)^2 + exp(-x) was not found on the right side.", 0.0, root, 1e-6);
    }

    @Test
    public void testSolverWithAllowedSolutionBelowSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction func = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);

        double root = solver.solve(100, func, -3.0, 3.0, AllowedSolution.BELOW_SIDE);
        assertTrue("The root of sin(x)^2 + exp(-x) was not found below the interval.", root < 0.0);
    }

    @Test
    public void testSolverWithAllowedSolutionAboveSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction func = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);

        double root = solver.solve(100, func, -3.0, 3.0, AllowedSolution.ABOVE_SIDE);
        assertTrue("The root of sin(x)^2 + exp(-x) was not found above the interval.", root > 0.0);
    }

}