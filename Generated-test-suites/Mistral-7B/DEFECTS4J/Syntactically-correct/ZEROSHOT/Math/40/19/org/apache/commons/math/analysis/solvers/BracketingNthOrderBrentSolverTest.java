import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.analysis.functions.ConstantFunction.ONE;
import static org.apache.commons.math3.analysis.functions.LinearFunction.THE_IDENTITY;
import static org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver.*;

/**
 * Test class for BracketingNthOrderBrentSolver
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver()}.
     */
    @Test
    public void testBracketingNthOrderBrentSolverConstructionDefault() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        Assert.assertEquals(1.0e-6, solver.getAbsoluteAccuracy(), 0.0);
        Assert.assertEquals(5, solver.getMaximalOrder());
        Assert.assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, int)}.
     */
    @Test
    public void testBracketingNthOrderBrentSolverConstructionRelativeAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-5, 6);
        Assert.assertEquals(1.0e-5, solver.getRelativeAccuracy(), 0.0);
        Assert.assertEquals(6, solver.getMaximalOrder());
        Assert.assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, int)}.
     */
    @Test
    public void testBracketingNthOrderBrentSolverConstructionAbsoluteAccuracyFunctionValueAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-5, 1.0e-6, 6);
        Assert.assertEquals(1.0e-5, solver.getAbsoluteAccuracy(), 0.0);
        Assert.assertEquals(1.0e-6, solver.getFunctionValueAccuracy(), 0.0);
        Assert.assertEquals(6, solver.getMaximalOrder());
        Assert.assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#BracketingNthOrderBrentSolver(double, double, double, int)}.
     */
    @Test
    public void testBracketingNthOrderBrentSolverConstructionRelativeAccuracyAbsoluteAccuracyFunctionValueAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-4, 1.0e-5, 1.0e-6, 6);
        Assert.assertEquals(1.0e-4, solver.getRelativeAccuracy(), 0.0);
        Assert.assertEquals(1.0e-5, solver.getAbsoluteAccuracy(), 0.0);
        Assert.assertEquals(1.0e-6, solver.getFunctionValueAccuracy(), 0.0);
        Assert.assertEquals(6, solver.getMaximalOrder());
        Assert.assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double)}.
     */
    @Test
    public void testSolveWithDefaultValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
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
        double root = solver.solve(10, f, -2.5, 2.5);
        Assert.assertEquals(-1.0, root, 1.0e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };
        double root = solver.solve(10, f, -3.0, 3.0, 0.0);
        Assert.assertEquals(Math.PI / 2.0, root, 1.0e-5);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getMaximalOrder()}.
     */
    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        Assert.assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getAllowedSolution()}.
     */
    @Test
    public void testGetAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        Assert.assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getFunctionValueAccuracy()}.
     */
    @Test
    public void testGetFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-4, 1.0e-6, 6);
        Assert.assertEquals(1.0e-6, solver.getFunctionValueAccuracy(), 0.0);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getAbsoluteAccuracy()}.
     */
    @Test
    public void testGetAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-5, 6);
        Assert.assertEquals(1.0e-5, solver.getAbsoluteAccuracy(), 0.0);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#getRelativeAccuracy()}.
     */
    @Test
    public void testGetRelativeAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-4, 1.0e-6, 6);
        Assert.assertEquals(1.0e-4, solver.getRelativeAccuracy(), 0.0);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        double root = solver.solve(10, f, -3.0, 3.0, 0.0, AllowedSolution.RIGHT_SIDE);
        Assert.assertEquals(Math.sqrt(2.0), root, 1.0e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolutionBelow() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        double root = solver.solve(10, f, -3.0, 3.0, 0.0, AllowedSolution.BELOW_SIDE);
        Assert.assertEquals(-Math.sqrt(2.0), root, 1.0e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolutionAbove() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        double root = solver.solve(10, f, -3.0, 3.0, 0.0, AllowedSolution.ABOVE_SIDE);
        Assert.assertEquals(Math.sqrt(2.0), root, 1.0e-6);
    }

    /**
     * Test method for {@link BracketingNthOrderBrentSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolutionLeft() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };
        double root = solver.solve(10, f, -3.0, 3.0, 0.0, AllowedSolution.LEFT_SIDE);
        Assert.assertEquals(-Math.sqrt(2.0), root, 1.0e-6);
    }
}