package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testEmptyConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testFunctionConstructor() {
        BisectionSolver solver = new BisectionSolver(new PolynomialFunction(x -> x * x - 4));
        assertNotNull(solver);
    }

    @Test
    public void testSolve_MinMax_ThrowsFunctionEvaluationException() {
        UnivariateRealFunction function = f -> {
            throw new FunctionEvaluationException("FunctionEvaluationException", 0.0);
        };
        try {
            new BisectionSolver().solve(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            fail("FunctionEvaluationException expected.");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_MinMax_ThrowsMaxIterationsExceededException() {
        UnivariateRealFunction function = x -> Math.sin(x) / x;
        try {
            new BisectionSolver().solve(function, -1.0, 1.0, 0.0);
            fail("MaxIterationsExceededException expected.");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_MinMax_ThrowsNumberIsTooSmallException() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 1;
        try {
            new BisectionSolver().solve(function, -1.0, 0.0);
            fail("NumberIsTooSmallException expected.");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_MinMax_ThrowsUnsupportedOperationException() {
        UnivariateRealFunction function = x -> { throw new UnsupportedOperationException("UnsupportedOperationException"); };
        try {
            new BisectionSolver().solve(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            fail("UnsupportedOperationException expected.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_Function_Min_Zero() {
        UnivariateRealFunction function = x -> x * x - 4;
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -2.0, 2.0);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolve_Function_MinMax_SingleRoot() {
        UnivariateRealFunction function = x -> x * x - 4;
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -3.0, 1.0);
        assertEquals(-2.0, root, 1e-6);
    }

    @Test
    public void testSolve_Function_MinMax_TwoRoots() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BisectionSolver solver = new BisectionSolver();
        double root1 = solver.solve(function, -4.0, -2.0);
        double root2 = solver.solve(function, 2.0, 4.0);
        assertEquals(-2.0, root1, 1e-6);
        assertEquals(2.0, root2, 1e-6);
    }

    @Test
    public void testSolve_Function_Min_Max_Initial() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4;
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -5.0, 3.0, 1.0);
        assertEquals(-2.0, root, 1e-6);
    }
}