package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.fail;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.IllegalArgumentException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveSimple() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;
        double expected = 2.0;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        double tolerance = Precision.EPSILON;
        if (!Precision.isClose(result, expected, tolerance)) {
            fail("Error in solve: expected " + expected + " but got " + result);
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvalException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Error message");
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
        UnivariateRealSolverUtils.solve(f, x0, x1);
        UnivariateRealSolverUtils.solve(f, x0, x1);
        UnivariateRealSolverUtils.solve(f, x0, x1);
        UnivariateRealSolverUtils.solve(f, x0, x1);
        UnivariateRealSolverUtils.solve(f, x0, x1);
        UnivariateRealSolverUtils.solve(f, x0, x1);
        UnivariateRealSolverUtils.solve(f, x0, x1);
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() throws Exception {
        UnivariateRealFunction f = null;
        double x0 = 1.0;
        double x1 = 3.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };
        double x0 = 3.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testSolveAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double x0 = 0.0;
        double x1 = 2.0 * Math.PI;
        double expected = 1.27323984764025; // approx
        double absoluteAccuracy = 0.00001;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        double tolerance = Precision.EPSILON;
        if (!Precision.isClose(result, expected, tolerance)) {
            fail("Error in solve: expected " + expected + " but got " + result);
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvalExceptionWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Error message");
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.0;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceExceptionWithAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.0;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test
    public void testBracket() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        double[] expected = {1.5, 2.5};
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        for (int i = 0; i < 2; i++) {
            double tolerance = Precision.EPSILON;
            if (!Precision.isClose(result[i], expected[i], tolerance)) {
                fail("Error in bracket: expected " + expected[i] + " but got " + result[i]);
            }
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionEvalException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Error message");
            }
        };
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketConvergenceException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketNullFunction() throws Exception {
        UnivariateRealFunction f = null;
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidInterval() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };
        double initial = 1.0;
        double lowerBound = 3.0;
        double upperBound = 2.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBracketMaxIterationsExceededException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        int maximumIterations = 0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    @Test
    public void testBracketWithMaxIterations() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double initial = 0.0;
        double lowerBound = -10.0;
        double upperBound = 10.0;
        int maximumIterations = 5;
        double[] expected = {-1.21918106376401, 1.21918106376401};
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        for (int i = 0; i < 2; i++) {
            double tolerance = Precision.EPSILON;
            if (!Precision.isClose(result[i], expected[i], tolerance)) {
                fail("Error in bracket: expected " + expected[i] + " but got " + result[i]);
            }
        }
    }
}