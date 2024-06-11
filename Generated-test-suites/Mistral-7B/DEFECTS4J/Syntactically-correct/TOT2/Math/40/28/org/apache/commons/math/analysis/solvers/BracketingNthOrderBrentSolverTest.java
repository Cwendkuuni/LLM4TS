package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolverWithDefaultSettings() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.E, Math.sin(x));
        double min = -1.5;
        double max = 1.5;
        double root = Math.PI / 2;
        double expectedResult = Precision.round(Math.pow(Math.E, Math.sin(root)), 5);
        double actualResult = solver.solve(100, function, min, max);
        assertEquals(expectedResult, actualResult, 1e-5);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWithIllegalArguments() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = null;
        double min = Double.NaN;
        double max = Double.NaN;
        solver.solve(100, function, min, max);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolverWithNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = x -> x * x - 4;
        double min = -2.0;
        double max = 2.0;
        solver.solve(100, function, min, max);
    }

    @Test
    public void testSolverWithCustomSettings() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2);
        double min = -1.0;
        double max = 1.0;
        double root = Math.PI / 4;
        double expectedResult = Precision.round(Math.pow(Math.sin(root), 2), 8);
        double actualResult = solver.solve(100, function, min, max);
        assertEquals(expectedResult, actualResult, 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 0);
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 8);
        int maximalOrder = solver.getMaximalOrder();
        assertEquals(8, maximalOrder);
    }

}