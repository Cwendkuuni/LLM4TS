package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithArguments() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-7, 4);
        assertEquals(1e-7, solver.getAbsoluteAccuracy(), 1e-12);
        assertEquals(4, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithLowerThanTwoOrder() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test
    public void testSolveSimpleCase() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double root = solver.solve(100, f, -2, 2);
        assertEquals(0, root, 1e-6);
    }

    @Test
    public void testSolveEdgeCase1() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.sin(1);
            }
        };
        AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        try {
            solver.solve(100, f, -2 * Math.PI, 2 * Math.PI, allowedSolution);
            fail("Expected NoBracketingException to be thrown");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveEdgeCase2() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };
        AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        solver.solve(100, f, -1, 0, allowedSolution);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveEdgeCase3() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        solver.solve(100, f, 0, 1e-15, allowedSolution);
    }

    @Test
    public void testSolveComplexCase() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 6);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 1;
            }
        };
        double root = solver.solve(100, f, -Math.PI, Math.PI);
        assertEquals(0, root, 1e-8);
    }

}