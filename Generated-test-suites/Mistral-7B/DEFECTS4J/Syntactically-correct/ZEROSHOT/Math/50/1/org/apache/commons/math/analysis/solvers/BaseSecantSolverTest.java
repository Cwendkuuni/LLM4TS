package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolverWithDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testBaseSecantSolverWithRelativeAndAbsoluteAccuracy() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-5;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testBaseSecantSolverWithFunctionValueAccuracy() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-5;
        double functionValueAccuracy = 1e-6;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 0.0);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testBaseSecantSolverWithNegativeAbsoluteAccuracy() {
        new BaseSecantSolver(-1.0, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testBaseSecantSolverWithNegativeFunctionValueAccuracy() {
        new BaseSecantSolver(1.0, -1.0, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolveWithDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);

        // Test solving a function with one root.
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double root = solver.solve(function, -2.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, Precision.EPSILON);

        // Test solving a function with no roots.
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        try {
            solver.solve(function, -2.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail();
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test solving a function with multiple roots.
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 3) - 1.0;
            }
        };
        root = solver.solve(function, -3.0, 3.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(1.0, root, Precision.EPSILON);

        root = solver.solve(function, -2.0, 2.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(1.5, root, Precision.EPSILON);

        root = solver.solve(function, -3.0, 1.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(1.0, root, Precision.EPSILON);

        root = solver.solve(function, 2.0, 3.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(0.5, root, Precision.EPSILON);
    }

    @Test
    public void testSolveWithRelativeAndAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test solving a function with one root.
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double root = solver.solve(function, -2.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, Precision.EPSILON);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6, 1e-8, BaseSecantSolver.Method.REGULA_FALSI);

        // Test solving a function with one root.
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double root = solver.solve(function, -2.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, Precision.EPSILON);
    }

    @Test
    public void testSolveWithDifferentMethods() {
        BaseSecantSolver solverRegulaFalsi = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        BaseSecantSolver solverIllinois = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.ILLINOIS);
        BaseSecantSolver solverPegasus = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.PEGASUS);

        // Test solving a function with one root.
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        double rootRegulaFalsi = solverRegulaFalsi.solve(function, -2.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, rootRegulaFalsi, Precision.EPSILON);

        double rootIllinois = solverIllinois.solve(function, -2.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, rootIllinois, Precision.EPSILON);

        double rootPegasus = solverPegasus.solve(function, -2.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, rootPegasus, Precision.EPSILON);
    }
}