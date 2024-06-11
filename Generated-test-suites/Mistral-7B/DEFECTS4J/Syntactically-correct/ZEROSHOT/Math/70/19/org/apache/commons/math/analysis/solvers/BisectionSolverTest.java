package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverWithPolynomialFunction() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(2, -1, 1);
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -2, 2);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new FunctionWithRoot(100, 1);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1, 1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBisectionSolverNumberIsTooSmall() throws Exception {
        UnivariateRealFunction function = new FunctionWithRoot(1e-15, 1);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1, 1);
    }

    @Test
    public void testBisectionSolverConstructorWithFunction() throws Exception {
        UnivariateRealFunction function = new FunctionWithRoot(1, 1);
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve();
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testBisectionSolverConstructorWithoutFunction() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new FunctionWithRoot(1, 1);
        double root = solver.solve(function);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testBisectionSolverSolveWithInitialValue() throws Exception {
        UnivariateRealFunction function = new FunctionWithRoot(1, 1);
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 1.1);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testBisectionSolverSolveWithoutInitialValue() throws Exception {
        UnivariateRealFunction function = new FunctionWithRoot(1, 1);
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function);
        assertEquals(1.0, root, 1e-6);
    }

    @Test
    public void testBisectionSolverVerifyInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction function = new FunctionWithRoot(1, 1);

        try {
            solver.solve(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            solver.solve(function, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static class FunctionWithRoot implements UnivariateRealFunction {
        private final double root;
        private final int maxIterations;

        public FunctionWithRoot(double root, int maxIterations) {
            this.root = root;
            this.maxIterations = maxIterations;
        }

        @Override
        public double value(double x) {
            if (x == root) {
                throw new FunctionEvaluationException("Root found!");
            }

            if (Math.abs(x - root) < 1e-12) {
                throw new NumberIsTooSmallException("x is too close to root: " + x);
            }

            return Math.pow(x - root, 2) + 1;
        }

        @Override
        public int getMaxIterations() {
            return maxIterations;
        }
    }
}