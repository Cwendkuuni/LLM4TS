package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    /** A simple function for testing. */
    private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 2;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    };

    /**
     * Test solver with a simple function.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testBisectionSolver() throws Exception {
        // given
        BisectionSolver solver = new BisectionSolver();

        // when
        double root = solver.solve(FUNCTION, -3.0, 3.0);

        // then
        Assert.assertNotEquals("The solver should return a different value than the initial interval.", -3.0, root);
        Assert.assertNotEquals("The solver should return a different value than the initial interval.", 3.0, root);
        Assert.assertEquals("The solver should find the root of the function.", 0.0, root, 1e-6);
    }

    /**
     * Test solver with a function that has no roots.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverNoRoots() throws Exception {
        // given
        BisectionSolver solver = new BisectionSolver();

        // when
        solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        }, -3.0, 3.0);

        // then - this exception should be thrown.
    }

    /**
     * Test solver with a function that has no bracketing interval.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverNoBracketingInterval() throws Exception {
        // given
        BisectionSolver solver = new BisectionSolver();

        // when
        solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        }, 0.0, 0.0);

        // then - this exception should be thrown.
    }

    /**
     * Test solver with a function that has a root but the initial interval is too wide.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testBisectionSolverTooWideInterval() throws Exception {
        // given
        BisectionSolver solver = new BisectionSolver();

        // when
        double root = solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        }, -10.0, 10.0);

        // then
        Assert.assertNotEquals("The solver should not find the root with such a wide interval.", 0.0, root);
    }

    /**
     * Test solver with a function that has a root but the initial interval is too small.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testBisectionSolverTooSmallInterval() throws Exception {
        // given
        BisectionSolver solver = new BisectionSolver();

        // when
        double root = solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        }, -1.0, 1.0);

        // then - the solver should not find the root with such a small interval.
        Assert.assertTrue("The root should not be found.", root != 0.0);
        Assert.assertTrue("The root should be closer to the midpoint.", Math.abs(root - (1.0 + -1.0) / 2.0) < 0.5);
    }

    /**
     * Test solver with a function that has multiple roots.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testBisectionSolverMultipleRoots() throws Exception {
        // given
        BisectionSolver solver = new BisectionSolver();

        // when
        double root1 = solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return (x - 1) * (x + 1) * (x - 2);
            }

            @Override
            public double derivative(double x) {
                return 3 * (x - 1) * (x + 1) + (x - 1) * (x - 2);
            }
        }, -3.0, 3.0);

        // then
        Assert.assertEquals("The solver should find the first root.", -1.0, root1, 1e-6);

        // given
        double root2 = solver.solve(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return (x - 1) * (x + 1) * (x - 2);
            }

            @Override
            public double derivative(double x) {
                return 3 * (x - 1) * (x + 1) + (x - 1) * (x - 2);
            }
        }, -2.0, 2.0);

        // then
        Assert.assertEquals("The solver should find the second root.", 1.0, root2, 1e-6);
    }
}