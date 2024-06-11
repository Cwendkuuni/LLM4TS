package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolators.LinearInterpolator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketingNumericalMethod;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.PrecisionRequirementExceededException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.E;

import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_FunctionZero() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double expected = 0.0;
        try {
            double actual = UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
            assertEquals(expected, actual, 1e-14);
        } catch (FunctionEvaluationException e) {
            // This test does not expect an exception
        }
    }

    @Test
    public void testSolve_FunctionNotZero() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        double expected = -1.0; // Approximate root
        try {
            double actual = UnivariateRealSolverUtils.solve(function, -2.0, 0.0);
            assertEquals(expected, actual, 1e-4);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // This test does not expect an exception
        }
    }

    @Test
    public void testSolve_IllegalArguments() {
        UnivariateRealFunction function = null;
        assertThrows(NullArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(function, 0.0, 0.0);
        });

        function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double x0 = Double.NaN;
        double x1 = 0.0;
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(function, x0, x1);
        });

        x0 = 0.0;
        double x1_negInf = Double.NEGATIVE_INFINITY;
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(function, x0, x1_negInf);
        });

        x1 = Double.POSITIVE_INFINITY;
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(function, x0, x1);
        });
    }

    @Test
    public void testSolve_ConvergenceException() {
        UnivariateRealFunction function = new PolynomialFunction(
                new double[]{-1.0, 1.0, 1.0, 1.0e-14, 1.0});
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
        });
    }

    @Test
    public void testSolve_FunctionEvaluationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception", x);
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            UnivariateRealSolverUtils.solve(function, 0.0, 0.0);
        });
    }

    @Test
    public void testBracket_FunctionZero() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double[] expected = {-1.0, 0.0};
        try {
            double[] actual = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
            assertEquals(expected[0], actual[0], 1e-14);
            assertEquals(expected[1], actual[1], 1e-14);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            // This test does not expect an exception
        }
    }

    @Test
    public void testBracket_FunctionNotZero() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        double[] expected = {-2.0, 1.0}; // Approximate bracketing
        try {
            double[] actual = UnivariateRealSolverUtils.bracket(function, 0.0, -2.0, 1.0);
            assertEquals(expected[0], actual[0], 1e-4);
            assertEquals(expected[1], actual[1], 1e-4);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            // This test does not expect an exception
        }
    }

    @Test
    public void testBracket_IllegalArguments() {
        UnivariateRealFunction function = null;
        assertThrows(NullArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 0.0);
        });

        function = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double initial = Double.NaN;
        double lowerBound = 0.0;
        double upperBound = 0.0;
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        });

        lowerBound = -1.0;
        upperBound = lowerBound;
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        });

        upperBound = 1.0;
        initial = upperBound + 1.0;
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
        });
    }

    @Test
    public void testBracket_ConvergenceException() {
        UnivariateRealFunction function = new PolynomialFunction(
                new double[]{-1.0, 1.0, 1.0, 1.0e-14, 1.0});
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 1.0);
        });
    }

    @Test
    public void testBracket_FunctionEvaluationException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception", x);
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 0.0);
        });
    }

    @Test
    public void testBrentSolver() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        double x0 = 0.0;
        double x1 = 1.0;
        double a = 1.0;
        double b = 0.0;
        double tolerance = 1e-6;
        UnivariateRealSolver solver = new BrentSolver();
        try {
            double root = solver.solve(function, x0, x1, tolerance);
            assertEquals(expectedRoot(function), root, tolerance);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // This test does not expect an exception
        }
    }

    @Test
    public void testSecantSolver() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        double x0 = 0.0;
        double x1 = 1.0;
        double tolerance = 1e-6;
        UnivariateRealSolver solver = new SecantSolver();
        try {
            double root = solver.solve(function, x0, x1, tolerance);
            assertEquals(expectedRoot(function), root, tolerance);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // This test does not expect an exception
        }
    }

    @Test
    public void testBracketingNumericalMethod() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        double x0 = 0.0;
        double x1 = 1.0;
        double tolerance = 1e-6;
        int maxIterations = 100;
        UnivariateRealSolver solver = new BracketingNumericalMethod(
                new BrentSolver(), maxIterations);
        try {
            double[] result = solver.solve(function, x0, x1, tolerance);
            double root = result[0];
            assertEquals(expectedRoot(function), root, tolerance);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // This test does not expect an exception
        }
    }

    @Test
    public void testLinearInterpolation() {
        UnivariateFunction xFunction = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0})::value;
        UnivariateFunction yFunction = new PolynomialFunction(new double[]{1.0, -2.0, 1.0})::value;
        double x0 = 0.0;
        double x1 = 1.0;
        double tolerance = 1e-6;
        UnivariateRealSolver solver = new BracketingNumericalMethod(
                new BrentSolver(), 100)::solve;
        UnivariateRealFunction interpolatedFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                double[] xValues = {x0, x1};
                double[] yValues = {xFunction.value(x0), xFunction.value(x1)};
                RealMatrix xy = new Array2DRowRealMatrix(xValues, yValues);
                RealVector y = new Array2DRowRealMatrix(yValues).getColumnVector(0);
                return LinearInterpolator.interpolate(xy, y, x);
            }
        };
        try {
            double root = solver.solve(interpolatedFunction, x0, x1, tolerance);
            assertEquals(expectedRoot(function), root, tolerance);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            // This test does not expect an exception
        }
    }

    @Test
    public void testExpectedRoots() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{1.0, -2.0, 1.0});
        double expectedRoot = E.EULER;
        assertEquals(expectedRoot, expectedRoot(function), 1e-14);

        function = new PolynomialFunction(new double[]{1.0, 1.0, -1.0});
        expectedRoot = Math.log(2.0);
        assertEquals(expectedRoot, expectedRoot(function), 1e-14);

        function = new PolynomialFunction(new double[]{1.0, -2.0, 2.0});
        expectedRoot = Math.sqrt(2.0);
        assertEquals(expectedRoot, expectedRoot(function), 1e-14);
    }

    private static double expectedRoot(UnivariateRealFunction function) {
        return new BrentSolver().solve(function, -1.0, 1.0);
    }

}