package org.apache.commons.math.analysis.solvers;

import static org.apache.commons.math3.util.Precision.abs;
import static org.apache.commons.math3.util.Precision.eq;
import static org.apache.commons.math3.util.Precision.max;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver<UnivariateRealFunction> solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver<UnivariateRealFunction>(0.001, 1.0,
                BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolverWithSimpleFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        double result = solver.solve(10, function, -3.0, 3.0, 0.0);
        assertEquals(0.0, result, 1e-5);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWithInvalidFunction() {
        UnivariateRealFunction function = null;
        solver.solve(10, function, -3.0, 3.0, 0.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolverWithUnbracketedFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };
        solver.solve(10, function, -3.0, 3.0, 0.0);
    }

    @Test
    public void testSolverWithFunctionValueAccuracy() {
        solver = new BaseSecantSolver<UnivariateRealFunction>(0.001, 1e-8,
                1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        double result = solver.solve(10, function, -3.0, 3.0, 0.0);
        assertEquals(Math.PI / 2.0, result, 1e-7);
    }

    @Test
    public void testSolverWithAllowedSolution() {
        solver = new BaseSecantSolver<UnivariateRealFunction>(0.001, 1.0,
                BaseSecantSolver.Method.REGULA_FALSI,
                BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        double result = solver.solve(10, function, -3.0, 3.0, 0.0);
        assertTrue(result < 0.0);
    }

    @Test
    public void testSolverWithInvertedInterval() {
        solver = new BaseSecantSolver<UnivariateRealFunction>(0.001, 1.0,
                BaseSecantSolver.Method.REGULA_FALSI,
                BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        double result = solver.solve(10, function, -3.0, 3.0, 0.0);
        assertFalse(result > 0.0);
    }
}