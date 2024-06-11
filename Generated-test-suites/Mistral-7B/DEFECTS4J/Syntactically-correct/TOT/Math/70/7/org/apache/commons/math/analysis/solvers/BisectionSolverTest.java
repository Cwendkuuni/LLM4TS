package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solvers.BisectionSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    /**
     * A function with only one root.
     */
    static class SimpleFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(x) - 0.5;
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * A function with no roots.
     */
    static class NoRootFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.PI - x, 2);
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * A function with infinitely many roots.
     */
    static class InfiniteRootFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.cos(x) - 1;
        }

        @Override
        public double derivative(double x) {
            return -Math.sin(x);
        }
    }

    /**
     * A function with a root that is too large.
     */
    static class LargeRootFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 100.0;
        }

        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }
    }

    @Test
    public void testSimpleFunction() {
        UnivariateRealFunction f = new SimpleFunction();
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -1.0, 1.0);
            assertEquals(-0.5, root, 1E-6);
        } catch (MaxIterationsExceededException e) {
            fail("Should have found root with default settings.");
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testNoRootFunction() {
        UnivariateRealFunction f = new NoRootFunction();
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInfiniteRootFunction() {
        UnivariateRealFunction f = new InfiniteRootFunction();
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }

    @Test
    public void testLargeRootFunction() {
        UnivariateRealFunction f = new LargeRootFunction();
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -10.0, 10.0);
            fail("Should not have found root with default settings.");
        } catch (MaxIterationsExceededException e) {
            // expected
        }

        solver.setMaximalIterationCount(1000);
        try {
            double root = solver.solve(f, -10.0, 10.0);
            assertEquals(-10.0, root, 1E-6);
        } catch (MaxIterationsExceededException e) {
            fail("Should have found root with increased settings.");
        }
    }
}