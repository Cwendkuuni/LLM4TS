package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver()}.
     */
    @Test
    public void testConstructor() {
        final BaseSecantSolver solver = new BaseSecantSolver();
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double)}.
     */
    @Test
    public void testConstructorWithAbsoluteAccuracy() {
        final double absoluteAccuracy = 0.001;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double)}.
     */
    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracy() {
        final double relativeAccuracy = 0.01;
        final double absoluteAccuracy = 0.001;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyFunctionValueAccuracy() {
        final double relativeAccuracy = 0.01;
        final double absoluteAccuracy = 0.001;
        final double functionValueAccuracy = 0.0001;
        final BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
        assertEquals(-1.41421356237, solver.solve(100, function, -2.0, 2.0, 0.0), 0.00001);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
        assertEquals(-1.41421356237, solver.solve(100, function, -2.0, 2.0, 0.0, AllowedSolution.LEFT_SIDE), 0.00001);
        assertEquals(-1.41421356237, solver.solve(100, function, -2.0, 2.0, 0.0, AllowedSolution.RIGHT_SIDE), 0.00001);
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(100, function, -2.0, 2.0, 0.0, AllowedSolution.BELOW_SIDE));
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(100, function, -2.0, 2.0, 0.0, AllowedSolution.ABOVE_SIDE));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolveWithoutAllowedSolution() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final BaseSecantSolver solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
        assertEquals(-1.41421356237, solver.solve(100, function, -2.0, 2.0), 0.00001);
    }

}