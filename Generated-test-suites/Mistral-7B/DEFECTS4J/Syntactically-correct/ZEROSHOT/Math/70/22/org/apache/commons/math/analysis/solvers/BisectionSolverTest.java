package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolverWithPolynomialFunction() throws Exception {
        UnivariateFunction function = new PolynomialFunction(2, -1, 1, -1);
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -2.0, 2.0);
        assertEquals(0.0, Precision.abs(root - 1.0), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtInfinity();
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateFunction function = new FunctionWithDivideByZeroException();
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0);
    }

    @Test(expected = ZeroException.class)
    public void testZeroException() throws Exception {
        UnivariateFunction function = new FunctionWithZeroRoot();
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0);
    }

    @Test
    public void testSolverWithInitialGuess() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtOne();
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 1.5, 2.0);
        assertEquals(1.0, Precision.abs(root - 1.0), 1e-6);
    }

    @Test
    public void testSolverWithNarrowInterval() throws Exception {
        UnivariateFunction function = new FunctionWithRootAtZero();
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -1e-6, 1e-6);
        assertEquals(0.0, Precision.abs(root), 1e-9);
    }

    private static class FunctionWithRootAtInfinity implements UnivariateFunction {
        @Override
        public double value(double x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private static class FunctionWithDivideByZeroException implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x == 0.0) {
                throw new ArithmeticException("Divide by zero");
            }
            return 1.0 / x;
        }
    }

    private static class FunctionWithZeroRoot implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x == 0.0) {
                throw new ZeroException("Function has a zero root");
            }
            return Math.sin(x) / x;
        }
    }

    private static class FunctionWithRootAtOne implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x - 1.0;
        }
    }
}