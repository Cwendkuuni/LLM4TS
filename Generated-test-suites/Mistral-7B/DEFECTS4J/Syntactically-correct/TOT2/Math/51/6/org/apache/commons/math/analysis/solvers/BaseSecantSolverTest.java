package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

/**
 * Test class for BaseSecantSolver.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double)} with
     * default arguments.
     */
    @Test
    public void testBaseSecantSolverDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}
     * with absolute accuracy.
     */
    @Test
    public void testBaseSecantSolverConstructorAbsoluteAccuracy() {
        double absoluteAccuracy = 1e-4;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}
     * with absolute and relative accuracies.
     */
    @Test
    public void testBaseSecantSolverConstructorAbsoluteAndRelativeAccuracies() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}
     * with absolute, relative, and function value accuracies.
     */
    @Test
    public void testBaseSecantSolverConstructorAbsoluteRelativeAndFunctionValueAccuracies() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-7;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)} with
     * relative accuracy.
     */
    @Test
    public void testBaseSecantSolverConstructorRelativeAccuracy() {
        double relativeAccuracy = 1e-5;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#Method#ILLINOIS}.
     */
    @Test
    public void testSolveWithIllinoisMethod() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, function, -3.0, 3.0, 0.0);
        assertEquals(-1.0, root, 1e-7);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#Method#PEGASUS}.
     */
    @Test
    public void testSolveWithPegasusMethod() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(10, function, -3.0, 3.0, 0.0);
        assertEquals(Math.PI / 2.0, root, 1e-7);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#Method#REGULA_FALSI}.
     */
    @Test
    public void testSolveWithRegulaFalsiMethod() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3.0) - 2.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, function, -3.0, 3.0, 0.0);
        assertEquals(-1.0, root, 1e-7);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with invalid method.
     */
    @Test
    public void testSolveWithInvalidMethod() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.INVALID);
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(10, function, -3.0, 3.0, 0.0));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with null function.
     */
    @Test
    public void testSolveWithNullFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-5, BaseSecantSolver.Method.ILLINOIS);
        assertThrows(NullArgumentException.class, () -> solver.solve(10, null, -3.0, 3.0, 0.0));
    }

}