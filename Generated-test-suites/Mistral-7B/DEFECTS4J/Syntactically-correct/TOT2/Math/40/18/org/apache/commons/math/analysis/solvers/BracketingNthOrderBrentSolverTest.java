package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    // Test case for the default constructor
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(5, solver.getMaximalOrder());
    }

    // Test case for constructor with custom absolute accuracy and maximal order
    @Test
    public void testCustomConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        assertEquals(1e-9, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(10, solver.getMaximalOrder());
    }

    // Test case for constructor with custom relative accuracy, absolute accuracy, and maximal order
    @Test
    public void testCustomConstructor2() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-7, 1e-8, 10);
        assertEquals(1e-7, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(10, solver.getMaximalOrder());
    }

    // Test case for constructor with custom relative accuracy, absolute accuracy, function value accuracy, and maximal order
    @Test
    public void testCustomConstructor3() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-7, 1e-8, 1e-10, 10);
        assertEquals(1e-7, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 1e-11);
        assertEquals(1e-10, solver.getFunctionValueAccuracy(), 1e-12);
        assertEquals(10, solver.getMaximalOrder());
    }

    // Test case for solving a function with known root using the default constructor
    @Test
    public void testSolveDefaultConstructor() throws MathIllegalArgumentException, NumberIsTooSmallException {
        UnivariateFunction function = f -> Math.pow(f, 2) - 4;
        double root = 2.0;
        double result = new BracketingNthOrderBrentSolver().solve(100, function, -5.0, 5.0);
        assertEquals(root, result, 1e-6);
    }

    // Test case for solving a function with known root using custom constructor
    @Test
    public void testSolveCustomConstructor() throws MathIllegalArgumentException, NumberIsTooSmallException {
        UnivariateFunction function = f -> Math.pow(f, 2) - 4;
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        double result = solver.solve(100, function, -5.0, 5.0);
        assertEquals(root, result, 1e-9);
    }

    // Test case for solving a function with known root using start value
    @Test
    public void testSolveCustomConstructorWithStartValue() throws MathIllegalArgumentException, NumberIsTooSmallException {
        UnivariateFunction function = f -> Math.pow(f, 2) - 4;
        double root = 2.0;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        double startValue = 3.0;
        double result = solver.solve(100, function, -5.0, 5.0, startValue);
        assertEquals(root, result, 1e-9);
    }

    // Test case for unsupported operation: solving a function with non-bracketing interval
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() throws NumberIsTooSmallException {
        UnivariateFunction function = f -> Math.sin(f);
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double[] intervals = {0.0, 0.0, 1.0};
        solver.solve(1, function, intervals[0], intervals[1], intervals[2]);
    }

    // Test case for unsupported operation: solving a function with non-positive maximal order
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws NumberIsTooSmallException {
        UnivariateFunction function = f -> Math.pow(f, 2) - 4;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.setMaximalOrder(-1);
        solver.solve(1, function, -5.0, 5.0);
    }

    // Test case for unsupported operation: solving a function with negative allowed solution
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() throws NumberIsTooSmallException {
        UnivariateFunction function = f -> Math.pow(f, 2) - 4;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        solver.solve(1, function, -5.0, 5.0);
    }

}