package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver.Method;
import org.apache.commons.math.util.Precision;

/**
 * Test class for the BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}
     * with default arguments.
     */
    @Test
    public void testBaseSecantSolverConstruction() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}
     * with given arguments.
     */
    @Test
    public void testBaseSecantSolverConstructionWithArguments() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.PEGASUS);
        assertEquals(Method.PEGASUS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}
     * with default arguments.
     */
    @Test
    public void testBaseSecantSolverConstructionWithDefaultArguments() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS, 1.0e-6);
        assertEquals(Method.ILLINOIS, solver.getMethod());
        assertEquals(1.0e-6, solver.getAbsoluteAccuracy(), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}
     * with default arguments.
     */
    @Test
    public void testBaseSecantSolverConstructionWithDefaultArguments2() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, 1.0e-6, Method.PEGASUS);
        assertEquals(Method.PEGASUS, solver.getMethod());
        assertEquals(1.0e-6, solver.getRelativeAccuracy(), Precision.SAFE_EPSILON);
        assertEquals(1.0e-6, solver.getAbsoluteAccuracy(), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, double, Method)}
     * with default arguments.
     */
    @Test
    public void testBaseSecantSolverConstructionWithDefaultArguments3() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, 1.0e-6, 1.0e-6, Method.ILLINOIS);
        assertEquals(Method.ILLINOIS, solver.getMethod());
        assertEquals(1.0e-6, solver.getRelativeAccuracy(), Precision.SAFE_EPSILON);
        assertEquals(1.0e-6, solver.getAbsoluteAccuracy(), Precision.SAFE_EPSILON);
        assertEquals(1.0e-6, solver.getFunctionValueAccuracy(), Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#getMethod()}.
     */
    @Test
    public void testGetMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.PEGASUS);
        assertEquals(Method.PEGASUS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with a function that has a root between two given bounds.
     */
    @Test
    public void testSolveWithFunctionThatHasARootBetweenTwoGivenBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.PEGASUS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        double rootX = 2.0;
        double root = solver.solve(10, function, -3.0, 3.0, rootX);
        assertEquals(rootX, root, Precision.SAFE_EPSILON);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with a function that has no roots between two given bounds.
     */
    @Test
    public void testSolveWithFunctionThatHasNoRootsBetweenTwoGivenBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.PEGASUS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 9.0;
            }
        };
        assertThrows(ConvergenceException.class, ()->solver.solve(10, function, -3.0, 3.0, 0.0));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with a function that has an infinite number of roots between two given bounds.
     */
    @Test
    public void testSolveWithFunctionThatHasAnInfiniteNumberOfRootsBetweenTwoGivenBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.PEGASUS);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        assertThrows(ConvergenceException.class, ()->solver.solve(10, function, -10.0, 10.0, 0.0));
    }

}