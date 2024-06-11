package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolver_DefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testBracketingNthOrderBrentSolver_CustomConstructor_AbsoluteAccuracyAndMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(7, solver.getMaximalOrder());
    }

    @Test
    public void testBracketingNthOrderBrentSolver_CustomConstructor_RelativeAccuracyAndAbsoluteAccuracyAndMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1e-7, 6);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-7, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(6, solver.getMaximalOrder());
    }

    @Test
    public void testBracketingNthOrderBrentSolver_CustomConstructor_RelativeAccuracyAndAbsoluteAccuracyAndFunctionValueAccuracyAndMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1e-7, 1e-9, 6);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-7, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-9, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(6, solver.getMaximalOrder());
    }

    @Test
    public void testBracketingNthOrderBrentSolver_getMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        int maxOrder = solver.getMaximalOrder();
        assertEquals(5, maxOrder);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBracketingNthOrderBrentSolver_CustomConstructor_InvalidMaximalOrder() throws NumberIsTooSmallException {
        new BracketingNthOrderBrentSolver(1e-5, 1e-7, 1);
    }

    @Test
    public void testBracketingNthOrderBrentSolver_solve_SimpleFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        double result = solver.solve(100, f, -10, 10);
        assertEquals(0, result, 1e-7);
    }

    @Test
    public void testBracketingNthOrderBrentSolver_solve_ComplexFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double result = solver.solve(100, f, 1, 4);
        assertEquals(1.2732406028966171, result, 1e-5);
    }

    @Test
    public void testBracketingNthOrderBrentSolver_solve_BracketingInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        try {
            solver.solve(100, f, -1.1, -0.9);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    @Test
    public void testBracketingNthOrderBrentSolver_solve_WithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double result = solver.solve(100, f, 1, 4, 2.2);
        assertEquals(1.2732406028966171, result, 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBracketingNthOrderBrentSolver_CustomConstructor_InvalidRelativeAccuracy() throws NumberIsTooSmallException {
        new BracketingNthOrderBrentSolver(-1e-5, 1e-7, 6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBracketingNthOrderBrentSolver_CustomConstructor_InvalidAbsoluteAccuracy() throws NumberIsTooSmallException {
        new BracketingNthOrderBrentSolver(1e-5, -1e-7, 6);
    }

}