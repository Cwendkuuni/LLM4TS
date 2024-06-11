package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtils;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Tests for {@link BaseSecantSolver}.
 */
@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    /** The solver to test. */
    private final BaseSecantSolver solver;

    /** The function to solve. */
    private final UnivariateFunction function;

    /** The maximum number of iterations. */
    private final int maxIterations;

    /**
     * Constructor.
     *
     * @param function The function to solve.
     * @param maxIterations The maximum number of iterations.
     */
    public BaseSecantSolverTest(UnivariateFunction function, int maxIterations) {
        this.function = function;
        this.solver = new RegulaFalsiSolver(1e-6, function);
        this.maxIterations = maxIterations;
    }

    /**
     * Tests the solver with a simple function.
     */
    @Test
    public void testSimple() {
        final double x = solver.solve(maxIterations, function, -1.0, 1.0);
        assertEquals(0.0, x, Precision.EPSILON);
    }

    /**
     * Tests the solver with a function having a root between the bounds.
     */
    @Test
    public void testRootBetweenBounds() {
        final double x = solver.solve(maxIterations, function, -2.0, 2.0);
        assertEquals(0.0, x, Precision.EPSILON);
    }

    /**
     * Tests the solver with a function having a root outside the bounds.
     */
    @Test(expected = NoBracketingException.class)
    public void testRootOutsideBounds() {
        solver.solve(maxIterations, function, -3.0, 1.0);
    }

    /**
     * Tests the solver with a function having multiple roots.
     */
    @Test
    public void testMultipleRoots() {
        final double[] roots = {-1.0, 1.0, 2.0};
        final double[] x = new double[3];

        for (int i = 0; i < roots.length; i++) {
            final UnivariateFunction function = new RootFunction(roots[i]);
            x[i] = solver.solve(maxIterations, function, -3.0, 3.0);
            assertEquals(roots[i], x[i], Precision.EPSILON);
        }
    }

    /**
     * Tests the solver with a function that is not monotonic.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNonMonotonicFunction() {
        solver.solve(maxIterations, new NonMonotonicFunction(), -1.0, 1.0);
    }

    /**
     * Tests the solver with a function that is not differentiable at the root.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonDifferentiableFunction() {
        solver.solve(maxIterations, new NonDifferentiableFunction(), 0.0, 0.1);
    }

    /**
     * Tests the solver with a function that throws an exception when evaluated.
     */
    @Test(expected = RuntimeException.class)
    public void testExceptionThrowingFunction() {
        solver.solve(maxIterations, new ExceptionThrowingFunction(), -1.0, 1.0);
    }

    /**
     * Tests the solver with a function having a singularity at the root.
     */
    @Test(expected = ConvergenceException.class)
    public void testSingularityAtRoot() {
        solver.solve(maxIterations, new SingularityAtRootFunction(), 0.0, 0.1);
    }

    /**
     * Tests the solver with a function having a singularity at the boundary.
     */
    @Test(expected = ConvergenceException.class)
    public void testSingularityAtBoundary() {
        solver.solve(maxIterations, new SingularityAtBoundaryFunction(), -1.0, 1.0);
    }

    /**
     * Tests the solver with a function having a root at the boundary.
     */
    @Test
    public void testRootAtBoundary() {
        final double x = solver.solve(maxIterations, new RootAtBoundaryFunction(), -1.0, 1.0);
        assertEquals(1.0, x, Precision.EPSILON);
    }

    /**
     * Tests the solver with a function having a root very close to a boundary.
     */
    @Test
    public void testRootCloseToBoundary() {
        final double x = solver.solve(maxIterations, new RootCloseToBoundaryFunction(), 0.0, 1.0);
        assertEquals(0.01, x, Precision.EPSILON);
    }

    /**
     * Tests the solver with a function having a very large root.
     */
    @Test
    public void testLargeRoot() {
        final double x = solver.solve(maxIterations, new LargeRootFunction(), -1000.0, 1000.0);
        assertEquals(-1000.0 + 1000.0 * Math.sqrt(2.0) / 2.0, x, 1000.0 * Precision.EPSILON);
    }

    /**
     * Tests the solver with a function having a very small root.
     */
    @Test
    public void testSmallRoot() {
        final double x = solver.solve(maxIterations, new SmallRootFunction(), 0.0, 1.0);
        assertEquals(1.0e-15, x, 1.0e-18);
    }

    /**
     * Test utility function.
     */
    private static class RootFunction implements UnivariateFunction {

        /** The root. */
        private final double root;

        /**
         * Constructor.
         *
         * @param root The root.
         */
        public RootFunction(double root) {
            this.root = root;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            return x - root;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            return 1.0;
        }
    }

    /**
     * Test utility function for non-monotonic functions.
     */
    private static class NonMonotonicFunction implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            return Math.sin(x);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            return Math.cos(x);
        }
    }

    /**
     * Test utility function for non-differentiable functions.
     */
    private static class NonDifferentiableFunction implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            return Math.abs(Math.log(Math.abs(x)));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Test utility function for functions throwing an exception.
     */
    private static class ExceptionThrowingFunction implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            throw new RuntimeException("Exception");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Test utility function for functions having a singularity at the root.
     */
    private static class SingularityAtRootFunction implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            return Math.sqrt(x * x - 1.0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            throw new ConvergenceException("Singularity at root");
        }
    }

    /**
     * Test utility function for functions having a singularity at the boundary.
     */
    private static class SingularityAtBoundaryFunction implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            return 1.0 / (Math.sqrt(Math.abs(x)) + 1.0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            throw new ConvergenceException("Singularity at boundary");
        }
    }

    /**
     * Test utility function for functions having a root at the boundary.
     */
    private static class RootAtBoundaryFunction implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            return Math.pow(x, 3.0) - 1.0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            return 3.0 * Math.pow(x, 2.0);
        }
    }

    /**
     * Test utility function for functions having a root very close to a boundary.
     */
    private static class RootCloseToBoundaryFunction implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            return Math.pow(x, 2.0) - 1.0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }
    }

    /**
     * Test utility function for functions having a large root.
     */
    private static class LargeRootFunction implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            return Math.pow(x, 2.0) - 10000.0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }
    }

    /**
     * Test utility function for functions having a small root.
     */
    private static class SmallRootFunction implements UnivariateFunction {

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double x) {
            return Math.pow(x, 2.0);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }
    }

    /**
     * Test data.
     *
     * @return The test data.
     */
    @Parameterized.Parameters(name = "{index}: Function={0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new RootFunction(0.0), 10},
                {new RootFunction(-1.0), 10},
                {new RootFunction(1.0), 10},
                {new RootFunction(0.0), 1},
                {new RootFunction(-1.0), 1},
                {new RootFunction(1.0), 1},
                {new RootFunction(Math.sqrt(2.0)), 10},
                {new NonMonotonicFunction(), 10},
                {new NonDifferentiableFunction(), 10},
                {new ExceptionThrowingFunction(), 10},
                {new SingularityAtRootFunction(), 10},
                {new SingularityAtBoundaryFunction(), 10},
                {new RootAtBoundaryFunction(), 10},
                {new RootCloseToBoundaryFunction(), 10},
                {new LargeRootFunction(), 10},
                {new SmallRootFunction(), 10}
        });
    }
}