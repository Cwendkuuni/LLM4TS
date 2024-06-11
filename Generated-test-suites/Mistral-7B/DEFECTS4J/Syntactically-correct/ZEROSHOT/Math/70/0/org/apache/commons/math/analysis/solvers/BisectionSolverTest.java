package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.util.Precision;

public class BisectionSolverTest {

    @org.junit.Test
    public void testBisectionSolverWithFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            };
        };

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, -3.0, 3.0);
        double expected = Precision.round(Math.sqrt(2.0), 5);
        assertEquals("Incorrect result", expected, result, 1e-5);
    }

    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverWithFunctionException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function Evaluation Error");
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -3.0, 3.0);
    }

    @org.junit.Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverWithMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(0);
        solver.solve(function, -3.0, 3.0);
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testBisectionSolverWithNumberIsTooSmallException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2.0 * x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setAbsoluteAccuracy(0.0);
        solver.solve(function, -3.0, 3.0);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testBisectionSolverWithNumberIsTooLargeException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setAbsoluteAccuracy(Double.MAX_VALUE);
        solver.solve(function, -3.0, 3.0);
    }
}