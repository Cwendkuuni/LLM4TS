package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootFoundException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.function.UnivariateRealFunction;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    /**
     * A function with a root.
     */
    private static final UnivariateRealFunction FUNCTION_WITH_ROOT = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 1;
        }
    };

    /**
     * A function with no root.
     */
    private static final UnivariateRealFunction FUNCTION_NO_ROOT = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 1;
        }
    };

    /**
     * A function with infinite roots.
     */
    private static final UnivariateRealFunction FUNCTION_INFINITE_ROOT = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return 1.0 / x;
        }
    };

    /**
     * A function that doesn't implement required method.
     */
    private static final UnivariateRealFunction FUNCTION_NOT_IMPLEMENTED = new UnivariateRealFunction() {
        @Override
        public double value(double x) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    };

    /**
     * Test solver with a function that has a root.
     */
    @Test
    public void testSolverWithFunctionThatHasRoot() {
        BisectionSolver solver = new BisectionSolver();
        double min = -2.0;
        double max = 2.0;
        try {
            double root = solver.solve(FUNCTION_WITH_ROOT, min, max);
            Assert.assertEquals(0.0, root, Precision.EPSILON);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Expected root, but reached maximum iterations.");
        }
    }

    /**
     * Test solver with a function that has no root.
     */
    @Test(expected = NoRootFoundException.class)
    public void testSolverWithFunctionThatHasNoRoot() {
        BisectionSolver solver = new BisectionSolver();
        double min = -2.0;
        double max = 2.0;
        solver.solve(FUNCTION_NO_ROOT, min, max);
    }

    /**
     * Test solver with a function that has infinite roots.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSolverWithFunctionThatHasInfiniteRoot() {
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(FUNCTION_INFINITE_ROOT, 1.0, 2.0);
            Assert.fail("Expected NumberIsTooSmallException, but got a result.");
        } catch (MaxIterationsExceededException e) {
            // Expected result.
        }
    }

    /**
     * Test solver with a function that doesn't implement the required method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolverWithFunctionThatDoesntImplementRequiredMethod() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(FUNCTION_NOT_IMPLEMENTED, 0.0, 0.0);
    }
}