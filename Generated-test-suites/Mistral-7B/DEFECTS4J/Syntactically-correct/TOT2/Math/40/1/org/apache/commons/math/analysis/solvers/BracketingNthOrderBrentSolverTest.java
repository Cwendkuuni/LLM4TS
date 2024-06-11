package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithAccuracyAndOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(7, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithOrderBelowTwo() throws NumberIsTooSmallException {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test
    public void testSolveSimpleRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -2.5, 2.5, AllowedSolution.ANY_SIDE);
        assertEquals(0, root, 1e-8);
    }

    @Test
    public void testSolveMultipleRoots() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root1 = solver.solve(100, f, -2, 2, AllowedSolution.ANY_SIDE);
        double root2 = solver.solve(100, f, 0, 2, AllowedSolution.ANY_SIDE);
        double root3 = solver.solve(100, f, 2, 2, AllowedSolution.ANY_SIDE);
        assertEquals(-1, root1, 1e-8);
        assertEquals(0, root2, 1e-8);
        assertEquals(1, root3, 1e-8);
    }

    @Test
    public void testSolveNoBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) * (x - 2);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        try {
            solver.solve(100, f, -3, 1, AllowedSolution.ANY_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionValueTooSmall() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1e-20;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        try {
            solver.solve(100, f, -1, 1, AllowedSolution.ANY_SIDE);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testSolveRelativeAccuracy() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 5);
        double root = solver.solve(100, f, -2, 2, AllowedSolution.ANY_SIDE);
        assertEquals(-1, root, 1e-4);
    }

    @Test
    public void testSolveAbsoluteAccuracy() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-10, 5);
        double root = solver.solve(100, f, -2.5, 2.5, AllowedSolution.ANY_SIDE);
        assertEquals(0, root, 1e-10);
    }

    @Test
    public void testSolveMaximalOrder() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 8) - 8 * x * Math.pow(Math.E, x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        double root = solver.solve(100, f, -5, 5, AllowedSolution.ANY_SIDE);
        assertEquals(-1.20284657, root, 1e-8);
    }

    @Test
    public void testSolveAllowedSolutionLeftSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) * (x - 2);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -3, 1, AllowedSolution.LEFT_SIDE);
        assertEquals(-2, root, 1e-8);
    }

    @Test
    public void testSolveAllowedSolutionRightSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) * (x - 2);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -3, 1, AllowedSolution.RIGHT_SIDE);
        assertEquals(2, root, 1e-8);
    }

    @Test
    public void testSolveAllowedSolutionBelowSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -3, 1, AllowedSolution.BELOW_SIDE);
        assertEquals(-2, root, 1e-8);
    }

    @Test
    public void testSolveAllowedSolutionAboveSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -3, 1, AllowedSolution.ABOVE_SIDE);
        assertEquals(2, root, 1e-8);
    }

    @Test
    public void testSolveWithStartValue() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) * (x - 2);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -3, 1, 1, AllowedSolution.ANY_SIDE);
        assertEquals(1, root, 1e-8);
    }

    @Test
    public void testSolveWithMinValue() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) * (x - 2);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, -5, 1, AllowedSolution.ANY_SIDE);
        assertEquals(-2, root, 1e-8);
    }

    @Test
    public void testSolveWithMaxValue() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        double root = solver.solve(100, f, 1, 5, AllowedSolution.ANY_SIDE);
        assertEquals(4, root, 1e-8);
    }

}