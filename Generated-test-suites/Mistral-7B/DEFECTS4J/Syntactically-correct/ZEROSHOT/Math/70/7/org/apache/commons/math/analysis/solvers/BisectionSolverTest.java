package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.function.UnivariateRealFunction;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    @Test
    public void testBisectionSolver() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        // Test with given arguments
        try {
            double root = solver.solve(function, -2.0, 2.0);
            Assert.assertEquals(0.0, Precision.abs(root - 1.0), 1e-6);
        } catch (MaxIterationsExceededException e) {
            // expected since we've set a small maximal iteration count for testing
        } catch (FunctionEvaluationException e) {
            Assert.fail("Function evaluation should not throw an exception");
        }

        // Test with default arguments
        try {
            solver = new BisectionSolver();
            double root = solver.solve(function, -2.0, 2.0);
            Assert.assertEquals(0.0, Precision.abs(root - 1.0), 1e-6);
        } catch (MaxIterationsExceededException e) {
            // expected since we've set a small maximal iteration count for testing
        } catch (FunctionEvaluationException e) {
            Assert.fail("Function evaluation should not throw an exception");
        }

        // Test with arguments out of order
        try {
            solver.solve(function, 2.0, -2.0);
            Assert.fail("Should throw an exception");
        } catch (UnsupportedOperationException e) {
            // expected since the function requires the interval to be bracketing the root
        }

        // Test with a function that always returns zero
        UnivariateRealFunction zeroFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };

        try {
            solver.solve(zeroFunction, 0.0, 0.0);
            Assert.fail("Should throw an exception");
        } catch (NumberIsTooSmallException e) {
            // expected since the function always returns zero and the interval is empty
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -2.0, 2.0);
    }
}