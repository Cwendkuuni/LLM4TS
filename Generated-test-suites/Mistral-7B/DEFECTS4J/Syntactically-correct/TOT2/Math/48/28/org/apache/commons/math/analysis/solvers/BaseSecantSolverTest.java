package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(0.0001, Method.PEGASUS);

        // Test with a simple polynomial function
        UnivariateRealFunction f = f -> Math.pow(x, 2) - 1;
        double root = solver.solve(10, f, -1.5, 1.5, 0.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, root, 1e-6);

        // Test with a function that has no roots in the given interval
        f = x -> Math.sin(x) - 0.5;
        try {
            solver.solve(10, f, -10, 10, 0.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test with a function that has multiple roots in the given interval
        f = x -> Math.pow(x - 1.0, 2) * (x - 2.0);
        try {
            solver.solve(10, f, -5.0, 5.0, 0.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // Expected, as the solver can only find one root in the given interval
        }

        // Test with a function that throws an exception
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        try {
            solver.solve(10, f, 0.0, 0.0, 0.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with a function that returns NaN
        f = x -> Math.sqrt(-1);
        try {
            solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test with a function that has a root very close to one of the bounds
        f = x -> Math.pow(x, 2) - 0.001;
        try {
            solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.ANY_SIDE);
            Assert.fail();
        } catch (ConvergenceException e) {
            // Expected, as the solver may not be able to find the root with the
            // given accuracy
        }

        // Test with a function that has a root very close to the middle of the
        // interval
        f = x -> Math.pow(x, 2) - 0.01;
        double root2 = solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.01, root2, 1e-6);
    }
}