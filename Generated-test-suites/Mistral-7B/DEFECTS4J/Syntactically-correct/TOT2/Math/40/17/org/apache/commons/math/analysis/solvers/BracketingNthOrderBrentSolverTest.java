package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolveSimpleCase() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 5);
        UnivariateFunction function = f -> Math.pow(Math.sin(x), 2);
        double result = solver.solve(100, function, -1, 1);
        assertEquals("The root of sin^2(x) from -1 to 1 should be 0", 0, result, 1e-6);
    }

    @Test
    public void testSolveFunctionWithRootAtEdgeOfInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 5);
        UnivariateFunction function = x -> Math.pow(x, 2) - 4;
        double result = solver.solve(100, function, -2, 2);
        assertEquals("The root of x^2 - 4 from -2 to 2 should be 2", 2, result, 1e-6);
    }

    @Test
    public void testSolveFunctionWithRootAtMidpointOfInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 5);
        UnivariateFunction function = x -> Math.pow(x, 2) - 1;
        double result = solver.solve(100, function, -1, 1);
        assertEquals("The root of x^2 - 1 from -1 to 1 should be 0", 0, result, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveFunctionWithMaximalOrderLowerThanTwo() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1);
        UnivariateFunction function = Math::sin;
        solver.solve(100, function, -1, 1);
    }

}