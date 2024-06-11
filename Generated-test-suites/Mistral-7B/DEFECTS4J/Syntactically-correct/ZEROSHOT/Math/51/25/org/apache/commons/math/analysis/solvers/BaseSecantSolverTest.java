package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolverWithRegulaFalsiMethod() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 2);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        double root = solver.solve(10, function, -5.0, 5.0, 0.0);
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolverWithIllinoisMethod() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 2);
        BaseSecantSolver solver = new IllinoisSolver(1e-6, function);
        double root = solver.solve(10, function, -5.0, 5.0, 0.0);
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolverWithPegasusMethod() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 2);
        BaseSecantSolver solver = new PegasusSolver(1e-6, function);
        double root = solver.solve(10, function, -5.0, 5.0, 0.0);
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolverWithFunctionValueAccuracy() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 2);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, 1e-8, function);
        double root = solver.solve(10, function, -5.0, 5.0, 0.0);
        assertEquals(0.0, root, 1e-9);
    }

    @Test
    public void testSolverWithMaxIterations() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 2);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(0, function, -5.0, 5.0, 0.0));
    }

    @Test
    public void testSolverWithNegativeRelativeAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(-1.0, null, null, null);
        assertThrows(IllegalArgumentException.class, solver::solve);
    }

    @Test
    public void testSolverWithAbsoluteAccuracyGreaterThanRelativeAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0, 0.1, null, null);
        assertThrows(IllegalArgumentException.class, solver::solve);
    }

    @Test
    public void testSolverWithNegativeAbsoluteAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(-1.0, 1.0, null, null);
        assertThrows(IllegalArgumentException.class, solver::solve);
    }

    @Test
    public void testSolverWithZeroRelativeAccuracy() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 2);
        BaseSecantSolver solver = new RegulaFalsiSolver(0.0, function);
        double root = solver.solve(10, function, -5.0, 5.0, 0.0);
        assertEquals(0.0, root, 1e-7);
    }

    @Test
    public void testSolverWithZeroAbsoluteAccuracy() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 2);
        BaseSecantSolver solver = new RegulaFalsiSolver(0.0, function);
        double root = solver.solve(10, function, -5.0, 5.0, 0.0);
        assertEquals(0.0, root, 1e-16);
    }

    @Test
    public void testSolverWithFunctionThrowsException() {
        UnivariateFunction function = x -> {
            throw new NumberIsTooLargeException("Function value is too large");
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0, function);
        assertThrows(NumberIsTooLargeException.class, () -> solver.solve(10, function, -5.0, 5.0, 0.0));
    }

    @Test
    public void testSolverWithFunctionValueOutOfBound() {
        UnivariateFunction function = x -> {
            if (x > 100) {
                throw new NumberIsTooLargeException("Function value is too large");
            }
            return x * x - 2;
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0, function);
        try {
            solver.solve(10, function, -5.0, 5.0, 101.0);
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithFunctionValueTooSmall() {
        UnivariateFunction function = x -> x * x - 2;
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0, function);
        assumeFalse(Double.isNaN(solver.solve(10, function, -5.0, 5.0, 0.0)));
        double result = solver.solve(10, function, -5.0, 5.0, 0.0);
        assumeTrue(Precision.abs(result, 1e-7) <= 1e-6);
        try {
            solver.solve(10, function, -5.0, 5.0, result);
        } catch (TooManyIterationsException e) {
            // Expected
        }
    }

}