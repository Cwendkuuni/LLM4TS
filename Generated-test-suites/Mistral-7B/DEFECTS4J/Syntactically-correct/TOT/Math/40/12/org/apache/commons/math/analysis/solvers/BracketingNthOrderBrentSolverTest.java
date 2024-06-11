package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBrentRootFindingSimple() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, Precision.round(root, 7));
    }

    @Test(expected = NoBracketingException.class)
    public void testBrentNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        solver.solve(100, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBrentNoDerivativeAllowed() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        solver.solve(100, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testBrentRootFindingWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2);
            }
        };

        double root = solver.solve(100, f, -3.0, 3.0, 1.5, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, Precision.round(root, 7));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testBrentMaxEvalExceeded() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        solver.solve(1, f, -5.0, 5.0, AllowedSolution.ANY_SIDE);
    }
}