package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolverBasicFunctionality() {
        UnivariateFunction func = new PolynomialFunction(new double[]{-1, 2, -1}, new double[]{0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        double root = solver.solve(100, func, -10, 10);
        assertEquals(0, root, 1e-6);
    }

    @Test
    public void testSolverEdgeCases() {
        UnivariateFunction func = new PolynomialFunction(new double[]{-1, 2, -1}, new double[]{0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        // test bracketing interval
        assertThrows(NoBracketingException.class, () -> solver.solve(100, func, 1, 1));

        // test function value accuracy
        assertThrows(NumberIsTooSmallException.class, () -> solver.solve(100, func, -10, 0));

        // test allowed solution setting
        UnivariateFunction funcLeft = new PolynomialFunction(new double[]{-1, 2, -1}, new double[]{0});
        BracketingNthOrderBrentSolver solverLeft = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.LEFT_SIDE);
        double rootLeft = solverLeft.solve(100, funcLeft, -10, 0);
        assertEquals(-10, rootLeft, 1e-6);

        UnivariateFunction funcRight = new PolynomialFunction(new double[]{-1, 2, -1}, new double[]{0});
        BracketingNthOrderBrentSolver solverRight = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.RIGHT_SIDE);
        double rootRight = solverRight.solve(100, funcRight, 1, 10);
        assertEquals(1, rootRight, 1e-6);

        UnivariateFunction funcBelow = new PolynomialFunction(new double[]{-1, 2, -1}, new double[]{0});
        BracketingNthOrderBrentSolver solverBelow = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.BELOW_SIDE);
        double rootBelow = solverBelow.solve(100, funcBelow, -1, 0);
        assertEquals(-1, rootBelow, 1e-6);

        UnivariateFunction funcAbove = new PolynomialFunction(new double[]{-1, 2, -1}, new double[]{0});
        BracketingNthOrderBrentSolver solverAbove = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.ABOVE_SIDE);
        double rootAbove = solverAbove.solve(100, funcAbove, 1, 1);
        assertEquals(1, rootAbove, 1e-6);

    }

    @Test
    public void testSolverExceptionHandling() {
        UnivariateFunction func = new PolynomialFunction(new double[]{-1, 2, -1}, new double[]{0});
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        // test illegal constructor arguments
        assertThrows(NumberIsTooSmallException.class, () -> new BracketingNthOrderBrentSolver(1e-6, 1));

        // test function evaluation exceptions
        assertThrows(MathInternalError.class, () -> computeObjectiveValue(null));
        assertThrows(NumberIsTooSmallException.class, () -> computeObjectiveValue(-Double.MAX_VALUE));

        // test solver exceptions
        assertThrows(NoBracketingException.class, () -> solver.solve(100, func, 1, 1));
        assertThrows(NumberIsTooSmallException.class, () -> solver.solve(100, func, -10, 0));
        assertThrows(MathInternalError.class, () -> solver.solve(100, func, -10, 10, Double.NaN));
    }

    private double computeObjectiveValue(UnivariateFunction function) {
        if (function == null) {
            throw new MathInternalError("Function cannot be null");
        }
        return function.value(0);
    }

}