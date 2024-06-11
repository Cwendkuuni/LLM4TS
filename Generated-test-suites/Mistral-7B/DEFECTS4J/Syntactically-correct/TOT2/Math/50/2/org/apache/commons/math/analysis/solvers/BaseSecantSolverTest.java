package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private UnivariateFunction f;
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        f = new FunctionWithRoots();
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_FunctionWithRootsAtMidpoint() {
        double result = solver.solve(50, f, -5.0, 5.0);
        assertEquals(0.0, result, 1e-8);
    }

    @Test
    public void testSolve_FunctionWithRootsAtMin() {
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        double result = solver.solve(50, f, -5.0, 5.0);
        assertEquals(-3.0, result, 1e-8);
    }

    @Test
    public void testSolve_FunctionWithRootsAtMax() {
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        double result = solver.solve(50, f, -5.0, 5.0);
        assertEquals(3.0, result, 1e-8);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolve_FunctionWithNoBracketing() {
        UnivariateFunction fNoBracketing = new FunctionWithNoBracketing();
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(50, fNoBracketing, -5.0, 5.0);
    }

    @Test(expected = MaxEvalException.class)
    public void testSolve_MaxEvalException() {
        UnivariateFunction fMaxEval = new FunctionWithMaxEval();
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(1, fMaxEval, -5.0, 5.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolve_UnsupportedOperationException() {
        UnivariateFunction fUnsupported = new FunctionWithUnsupportedOperation();
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        solver.solve(50, fUnsupported, -5.0, 5.0);
    }

    @Test
    public void testSolve_FunctionWithMultipleRoots() {
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        double[] roots = {-2.0, 1.0, 4.0};
        double[] results = new double[roots.length];

        for (int i = 0; i < roots.length; i++) {
            results[i] = solver.solve(50, f, -5.0, 5.0);
            assertEquals(roots[i], results[i], 1e-8);
        }
    }

    @Test
    public void testSolve_FunctionWithRootsCloseToEdge() {
        UnivariateFunction fCloseToEdge = new FunctionWithRootsCloseToEdge();
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        double result = solver.solve(50, fCloseToEdge, -5.0, 5.0);
        assertEquals(-2.9999999999999996, result, 1e-8);
    }

    @Test
    public void testSolve_FunctionWithRootsAtEnd() {
        UnivariateFunction fRootsAtEnd = new FunctionWithRootsAtEnd();
        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);

        double result = solver.solve(50, fRootsAtEnd, -5.0, 5.0);
        assertEquals(5.0, result, 1e-8);
    }

    private static class FunctionWithRoots implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x - 4;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    private static class FunctionWithRootsAtMidpoint implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x - 9;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    private static class FunctionWithRootsAtMin implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x - 9;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    private static class FunctionWithRootsAtMax implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x - 9;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    private static class FunctionWithNoBracketing implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.sin(x) / x;
        }

        @Override
        public double derivative(double x) {
            return (Math.cos(x) / x - Math.sin(x)) / x;
        }
    }

    private static class FunctionWithMaxEval implements UnivariateFunction {

        @Override
        public double value(double x) {
            throw new MaxEvalException("Maximum number of evaluations reached.");
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FunctionWithUnsupportedOperation implements UnivariateFunction {

        @Override
        public double value(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FunctionWithRootsCloseToEdge implements UnivariateFunction {

        @Override
        public double value(double x) {
            return FastMath.pow(x, 3) - 1;
        }

        @Override
        public double derivative(double x) {
            return 3 * FastMath.pow(x, 2);
        }
    }

    private static class FunctionWithRootsAtEnd implements UnivariateFunction {

        @Override
        public double value(double x) {
            return FastMath.pow(x, 3) - 27;
        }

        @Override
        public double derivative(double x) {
            return 3 * FastMath.pow(x, 2);
        }
    }
}