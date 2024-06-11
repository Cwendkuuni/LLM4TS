package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.FastMath;

public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double)} .
     *
     * @throws ConvergenceException if an error occurs
     */
    @org.junit.Test
    public void testBaseSecantSolver() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals("Unexpected default method", BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double)} .
     *
     * @throws ConvergenceException if an error occurs
     */
    @org.junit.Test
    public void testBaseSecantSolver01() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals("Unexpected default method", BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
        assertEquals("Unexpected absolute accuracy", 0.001, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals("Unexpected relative accuracy", 0.0001, solver.getRelativeAccuracy(), 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double)} .
     *
     * @throws ConvergenceException if an error occurs
     */
    @org.junit.Test
    public void testBaseSecantSolver02() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, 0.00001, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals("Unexpected default method", BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
        assertEquals("Unexpected absolute accuracy", 0.001, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals("Unexpected relative accuracy", 0.0001, solver.getRelativeAccuracy(), 1e-8);
        assertEquals("Unexpected function value accuracy", 0.00001, solver.getFunctionValueAccuracy(), 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#getMethod()} .
     */
    @org.junit.Test
    public void testGetMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
        assertEquals("Unexpected method", BaseSecantSolver.Method.PEGASUS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)} .
     *
     * @throws ConvergenceException if an error occurs
     */
    @org.junit.Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        double root = solver.solve(10, f, -3.0, 3.0);
        assertNotNull("Root not found", root);
        assertEquals("Root not correct", Math.sqrt(2), root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} .
     *
     * @throws ConvergenceException if an error occurs
     */
    @org.junit.Test
    public void testSolve01() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertNotNull("Root not found", root);
        assertEquals("Root not correct", 1.3186557874539893, root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} .
     *
     * @throws ConvergenceException if an error occurs
     */
    @org.junit.Test
    public void testSolve02() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double root = solver.solve(10, f, -10.0, 10.0, 0.0, AllowedSolution.BELOW_SIDE);
        assertNotNull("Root not found", root);
        assertEquals("Root not correct", 1.3186557874539893, root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} .
     *
     * @throws ConvergenceException if an error occurs
     */
    @org.junit.Test
    public void testSolve03() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double root = solver.solve(10, f, -10.0, 10.0, 0.0, AllowedSolution.ABOVE_SIDE);
        assertNull("Root found on wrong side", root);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)} .
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        solver.verifyBracketing(1.0, 1.0);
    }
}

enum AllowedSolution {
    ANY_SIDE,
    LEFT_SIDE,
    RIGHT_SIDE,
    BELOW_SIDE,
    ABOVE_SIDE;
}