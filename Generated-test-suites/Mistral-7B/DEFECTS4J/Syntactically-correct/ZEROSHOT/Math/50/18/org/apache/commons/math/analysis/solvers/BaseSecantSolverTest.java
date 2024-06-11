package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExcededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.fast.FastMath;
import org.junit.Test;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.Method;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        // Test with default constructor
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, f, -5.0, 5.0, 0.0);
            fail("Expected MaxEvalExcededException");
        } catch (MaxEvalExcededException e) {
            // Expected
        }

        root = solver.solve(100, f, -5.0, 5.0);
        assertEquals(0.0, root, Precision.PRECISION_ULP);

        // Test with constructor taking accuracy and method
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.ILLINOIS);
        root = solver2.solve(100, f, -5.0, 5.0);
        assertEquals(0.0, root, Precision.PRECISION_ULP);

        // Test with constructor taking relative and absolute accuracies and method
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-6, 1e-8, BaseSecantSolver.Method.PEGASUS);
        root = solver3.solve(100, f, -5.0, 5.0);
        assertEquals(0.0, root, Precision.PRECISION_ULP);
    }

    @Test
    public void testAllowedSolutions() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        // Test with any solution
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(100, f, -5.0, 5.0, 0.0);
        assertEquals(0.0, root, Precision.PRECISION_ULP);

        // Test with left solution
        BaseSecantSolver solverLeft = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        root = solverLeft.solve(100, f, -5.0, 5.0, 0.0);
        assertEquals(-sqrt(2.0), root, Precision.PRECISION_ULP);

        // Test with right solution
        BaseSecantSolver solverRight = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS, AllowedSolution.RIGHT_SIDE);
        root = solverRight.solve(100, f, -5.0, 5.0, 0.0);
        assertEquals(sqrt(2.0), root, Precision.PRECISION_ULP);

        // Test with below solution
        BaseSecantSolver solverBelow = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS, AllowedSolution.BELOW_SIDE);
        root = solverBelow.solve(100, f, -5.0, 5.0, 0.0);
        assertEquals(-sqrt(2.0), root, Precision.PRECISION_ULP);

        // Test with above solution
        BaseSecantSolver solverAbove = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS, AllowedSolution.ABOVE_SIDE);
        root = solverAbove.solve(100, f, -5.0, 5.0, 0.0);
        assertEquals(sqrt(2.0), root, Precision.PRECISION_ULP);
    }

    // Helper method to get square root
    private double sqrt(double d) {
        return FastMath.sqrt(d);
    }
}