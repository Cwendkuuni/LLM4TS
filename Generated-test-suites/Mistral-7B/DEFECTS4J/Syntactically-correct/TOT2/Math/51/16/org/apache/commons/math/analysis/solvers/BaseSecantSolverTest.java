package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for BaseSecantSolver.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        // Test with default constructor
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 2);
        double root = solver.solve(10, function, -1.0, 1.0);
        assertEquals(0.0, Precision.abs(root - 1.0), 1e-9);

        // Test with constructor taking relative and absolute accuracy
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-3, 1e-6, Method.ILLINOIS);
        UnivariateFunction function2 = new PolynomialFunction(x -> x * x - 2);
        double root2 = solver2.solve(10, function2, -1.0, 1.0);
        assertEquals(0.0, Precision.abs(root2 - 1.0), 1e-9);

        // Test with constructor taking relative, absolute accuracy and function value accuracy
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-3, 1e-6, 1e-9, Method.ILLINOIS);
        UnivariateFunction function3 = new PolynomialFunction(x -> x * x - 2);
        double root3 = solver3.solve(10, function3, -1.0, 1.0);
        assertEquals(0.0, Precision.abs(root3 - 1.0), 1e-9);

        // Test with null function
        try {
            BaseSecantSolver solverNullFunction = new BaseSecantSolver(Method.ILLINOIS);
            solverNullFunction.solve(10, null, 0.0, 1.0);
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with function that does not bracket the root
        BaseSecantSolver solverFunctionNotBracketing = new BaseSecantSolver(Method.ILLINOIS);
        UnivariateFunction functionNotBracketing = x -> x * x - 3;
        try {
            double rootNotBracketing = solverFunctionNotBracketing.solve(10, functionNotBracketing, -1.0, 1.0);
            fail();
        } catch (MaxEvalException e) {
            // Expected
        }

        // Test with function that throws an exception
        BaseSecantSolver solverThrowingException = new BaseSecantSolver(Method.ILLINOIS);
        UnivariateFunction functionThrowingException = x -> { throw new UnsupportedOperationException(); };
        try {
            double rootThrowingException = solverThrowingException.solve(10, functionThrowingException, 0.0, 1.0);
            fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}