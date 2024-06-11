package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

public class BisectionSolverTest {

    @org.junit.Test
    public void testBisectionSolver() throws Exception {
        // Test empty constructor
        BisectionSolver solver = new BisectionSolver();

        // Test constructor with function
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3);
        BisectionSolver solverWithFunction = new BisectionSolver(function);

        // Test solve method with initial guess
        double min = 1.0;
        double max = 2.0;
        double root = 1.5;
        double initial = 1.3;
        double expectedResult = 1.64804675144112; // Approximate value of the root
        double tolerance = 1e-5;

        double result = solver.solve(function, min, max, initial);
        assertEquals("Solve method with initial guess did not return correct result", expectedResult, result, tolerance);

        // Test solve method without initial guess
        result = solver.solve(function, min, max);
        assertEquals("Solve method without initial guess did not return correct result", root, result, tolerance);

        // Test solve method with function and interval
        UnivariateRealFunction function2 = new PolynomialFunction(new double[]{-4.0, 1.0, 1.0, 6.0}, 4);
        double min2 = 0.0;
        double max2 = 4.0;
        double root2 = 2.0;
        result = solverWithFunction.solve(function2, min2, max2);
        assertEquals("Solve method with function and interval did not return correct result", root2, result, tolerance);

        // Test exception handling
        UnivariateRealFunction function3 = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0}, 3); // Function with a root at x=0
        min = -1.0;
        max = 1.0;
        try {
            solverWithFunction.solve(function3, min, max);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        min = Double.NEGATIVE_INFINITY;
        max = Double.POSITIVE_INFINITY;
        try {
            solverWithFunction.solve(function3, min, max);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        // Test large interval
        UnivariateRealFunction function4 = new PolynomialFunction(new double[]{1.0, -1.0, 1.0}, 3);
        min = -10000.0;
        max = 10000.0;
        try {
            solverWithFunction.solve(function4, min, max);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test solve method with null function
        UnivariateRealFunction nullFunction = null;
        try {
            solverWithFunction.solve(nullFunction, min, max);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testBisectionSolverWithMatrix() throws Exception {
        BisectionSolver solver = new BisectionSolver();

        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3);
        double min = 1.0;
        double max = 2.0;

        RealMatrix jacobian = new Array2DRowRealMatrix(new double[][]{
                {-2.0, 1.0},
                {1.0, -2.0}
        });

        double[] root = solver.solve(function, min, max, jacobian);
        assertEquals("Solve method with Jacobian did not return correct result", 1.5, root[0], 1e-5);
        assertEquals("Solve method with Jacobian did not return correct iteration count", 11, root[1]);
    }
}