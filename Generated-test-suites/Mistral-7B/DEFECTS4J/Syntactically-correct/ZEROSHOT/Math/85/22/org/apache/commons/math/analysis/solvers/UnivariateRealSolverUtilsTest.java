package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.BisectionSolver;
import org.apache.commons.math3.solver.BracketedUnivariateFunction;
import org.apache.commons.math3.solver.NDerivativeUnivariateFunction;
import org.apache.commons.math3.solver.PowellSolver;
import org.apache.commons.math3.solver.SecantSolver;
import org.apache.commons.math3.solver.UnivariateFunctionEvaluator;
import org.apache.commons.math3.solver.UnivariateSolver;
import org.apache.commons.math3.solvers.LevenbergMarquardtIterativeSolver;

public class UnivariateRealSolverUtilsTest {

    @org.junit.Test
    public void testSolve() throws Exception {
        // Test with default solver
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double x0 = 1.0;
        double x1 = -1.0;
        double expected = 0.0;
        double actual = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(expected, actual, 1e-10);

        // Test with specified solver
        UnivariateRealFunction g = new PolynomialFunction(new double[]{-4.0, 4.0, 1.0, -1.0, 0.0});
        UnivariateSolver solver = new BrentSolver();
        double a = 0.5;
        double b = 2.5;
        double tolerance = 1e-8;
        double actualSolver = solver.solve(g, a, b);
        assertEquals(expected, actualSolver, tolerance);

        // Test with IllegalArgumentException
        UnivariateRealFunction h = null;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(h, x0, x1));

        // Test with FunctionEvaluationException
        UnivariateRealFunction i = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0 / 0.0});
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(i, x0, x1));

        // Test with ConvergenceException
        UnivariateRealFunction j = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(j, x0, x1, 0.0, 1000));
    }

    @org.junit.Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        // Test with default solver
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double x0 = 1.0;
        double x1 = -1.0;
        double absoluteAccuracy = 1e-12;
        double expected = 0.0;
        double actual = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(expected, actual, absoluteAccuracy);

        // Test with specified solver
        UnivariateRealFunction g = new PolynomialFunction(new double[]{-4.0, 4.0, 1.0, -1.0, 0.0});
        UnivariateSolver solver = new BrentSolver();
        solver.setAbsoluteAccuracy(1e-12);
        double a = 0.5;
        double b = 2.5;
        double tolerance = 1e-12;
        double actualSolver = solver.solve(g, a, b);
        assertEquals(expected, actualSolver, tolerance);

        // Test with IllegalArgumentException
        UnivariateRealFunction h = null;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(h, x0, x1, absoluteAccuracy));

        // Test with FunctionEvaluationException
        UnivariateRealFunction i = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0 / 0.0});
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(i, x0, x1, absoluteAccuracy));

        // Test with ConvergenceException
        UnivariateRealFunction j = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(j, x0, x1, absoluteAccuracy, 1000));
    }

    @org.junit.Test
    public void testBracket() throws Exception {
        // Test with default solver
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double initial = 1.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        double[] expected = {-1.5, 0.5};
        double[] actual = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        for (int i = 0; i < 2; i++) {
            assertEquals(expected[i], actual[i], 1e-10);
        }

        // Test with specified solver
        UnivariateRealFunction g = new PolynomialFunction(new double[]{-4.0, 4.0, 1.0, -1.0, 0.0});
        UnivariateSolver solver = new BrentSolver();
        double a = 0.5;
        double b = 2.5;
        double tolerance = 1e-8;
        double[] expectedSolver = {0.5, 1.5};
        double[] actualSolver = UnivariateRealSolverUtils.bracket(g, a, lowerBound, upperBound, 1000);
        for (int i = 0; i < 2; i++) {
            assertEquals(expectedSolver[i], actualSolver[i], tolerance);
        }

        // Test with IllegalArgumentException
        UnivariateRealFunction h = null;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(h, initial, lowerBound, upperBound));

        // Test with NoBracketException
        UnivariateRealFunction i = new PolynomialFunction(new double[]{0.0, 0.0, 0.0});
        assertThrows(NoBracketException.class, () -> UnivariateRealSolverUtils.bracket(i, initial, lowerBound, upperBound));

        // Test with FunctionEvaluationException
        UnivariateRealFunction j = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0 / 0.0});
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(j, initial, lowerBound, upperBound));

        // Test with MaxIterationsExceededException
        UnivariateRealFunction k = new PolynomialFunction(new double[]{0.0, 1.0, 1.0});
        assertThrows(MaxIterationsExceededException.class, () -> UnivariateRealSolverUtils.bracket(k, initial, lowerBound, upperBound, 0));

        // Test with UnsupportedOperationException
        UnivariateRealFunction l = new PolynomialFunction(new double[]{-1.0, 1.0, Double.NaN});
        assertThrows(UnsupportedOperationException.class, () -> UnivariateRealSolverUtils.bracket(l, initial, lowerBound, upperBound));
    }

    @org.junit.Test
    public void testBracketWithMaxIterations() throws Exception {
        // Test with default solver
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double initial = 1.0;
        double lowerBound = -2.0;
        double upperBound = 2.0;
        int maxIterations = 5;
        double[] expected = {-1.5, 0.5};
        double[] actual = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maxIterations);
        for (int i = 0; i < 2; i++) {
            assertEquals(expected[i], actual[i], 1e-10);
        }

        // Test with specified solver
        UnivariateRealFunction g = new PolynomialFunction(new double[]{-4.0, 4.0, 1.0, -1.0, 0.0});
        UnivariateSolver solver = new BrentSolver();
        solver.setMaxIterations(5);
        double a = 0.5;
        double b = 2.5;
        double tolerance = 1e-8;
        double[] expectedSolver = {0.5, 1.5};
        double[] actualSolver = UnivariateRealSolverUtils.bracket(g, a, lowerBound, upperBound, maxIterations);
        for (int i = 0; i < 2; i++) {
            assertEquals(expectedSolver[i], actualSolver[i], tolerance);
        }

        // Test with IllegalArgumentException
        UnivariateRealFunction h = null;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(h, initial, lowerBound, upperBound, maxIterations));

        // Test with NoBracketException
        UnivariateRealFunction i = new PolynomialFunction(new double[]{0.0, 0.0, 0.0});
        assertThrows(NoBracketException.class, () -> UnivariateRealSolverUtils.bracket(i, initial, lowerBound, upperBound, maxIterations));

        // Test with FunctionEvaluationException
        UnivariateRealFunction j = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0 / 0.0});
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(j, initial, lowerBound, upperBound, maxIterations));

        // Test with MaxIterationsExceededException
        UnivariateRealFunction k = new PolynomialFunction(new double[]{0.0, 1.0, 1.0});
        assertThrows(MaxIterationsExceededException.class, () -> UnivariateRealSolverUtils.bracket(k, initial, lowerBound, upperBound, 0));

        // Test with UnsupportedOperationException
        UnivariateRealFunction l = new PolynomialFunction(new double[]{-1.0, 1.0, Double.NaN});
        assertThrows(UnsupportedOperationException.class, () -> UnivariateRealSolverUtils.bracket(l, initial, lowerBound, upperBound, maxIterations));
    }

    @org.junit.Test
    public void testMidpoint() throws Exception {
        // Test with double values
        double a = 3.0;
        double b = 5.0;
        double expected = 4.0;
        double actual = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, actual, 1e-10);

        // Test with UnivariateFunction
        UnivariateFunction function = new PolynomialFunction(new double[]{1.0, 1.0, -1.0});
        double x = 2.0;
        expected = 1.5;
        actual = UnivariateRealSolverUtils.midpoint(x, function.value(x));
        assertEquals(expected, actual, 1e-10);

        // Test with NDerivativeUnivariateFunction
        NDerivativeUnivariateFunction functionDerivative = new PolynomialFunction(new double[]{1.0, -2.0, 2.0});
        double xDerivative = 2.0;
        expected = 1.5;
        actual = UnivariateRealSolverUtils.midpoint(xDerivative, functionDerivative.value(xDerivative));
        assertEquals(expected, actual, 1e-10);
    }

    @org.junit.Test
    public void testSetup() throws Exception {
        // Test with UnivariateRealFunction
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        UnivariateRealSolverUtils.setup(f);

        // Test with null UnivariateRealFunction
        UnivariateRealFunction g = null;
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.setup(g));
    }
}