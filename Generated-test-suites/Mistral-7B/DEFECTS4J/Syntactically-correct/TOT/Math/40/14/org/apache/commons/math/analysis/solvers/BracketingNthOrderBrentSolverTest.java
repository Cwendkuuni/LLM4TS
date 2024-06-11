package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolverWithSimpleFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            double root = solver.solve(100, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals("Root should be approximately -1.41421356", Precision.round(root, 12), -1.414213562, 1e-13);
        } catch (NumberIsTooSmallException e) {
            fail("Solver should not throw NumberIsTooSmallException with maximal order 5");
        }
    }

    @Test
    public void testSolverWithFunctionWithTwoRoots() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 3;
            }
        };

        try {
            double root1 = solver.solve(100, f, -3.0, 1.0, AllowedSolution.LEFT_SIDE);
            assertEquals("Left root should be approximately -1.0", Precision.round(root1, 6), -1.0, 1e-6);

            double root2 = solver.solve(100, f, -1.0, 3.0, AllowedSolution.RIGHT_SIDE);
            assertEquals("Right root should be approximately 1.0", Precision.round(root2, 6), 1.0, 1e-6);
        } catch (NumberIsTooSmallException e) {
            fail("Solver should not throw NumberIsTooSmallException with maximal order 5");
        }
    }

    @Test(expected = NoBracketingException.class)
    public void testSolverWithUnbracketedInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            solver.solve(100, f, 0.0, 0.0);
            fail("Solver should throw NoBracketingException for unbracketed interval");
        } catch (NoBracketingException e) {
            // expected
        }
    }
}