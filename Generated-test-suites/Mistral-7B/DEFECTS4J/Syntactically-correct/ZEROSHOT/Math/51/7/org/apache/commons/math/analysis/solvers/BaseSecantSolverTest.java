package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Test class for the BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    /** A simple function for testing. */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return x * x - 2;
        }
    };

    /**
     * Test that the solver returns the correct value for a simple function
     * using the Regula Falsi method.
     */
    @Test
    public void testRegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        double result = solver.solve(10, TEST_FUNCTION, -3.0, 3.0, 0.0);
        assertEquals("The solver should return the square root of 2.",
                     Math.sqrt(2), Precision.round(result, 5));
    }

    /**
     * Test that the solver returns the correct value for a simple function
     * using the Illinois method.
     */
    @Test
    public void testIllinois() {
        BaseSecantSolver solver = new BaseSecantSolver.IllinoisSolver(1e-6);
        double result = solver.solve(10, TEST_FUNCTION, -3.0, 3.0, 0.0);
        assertEquals("The solver should return the square root of 2.",
                     Math.sqrt(2), Precision.round(result, 5));
    }

    /**
     * Test that the solver returns the correct value for a simple function
     * using the Pegasus method.
     */
    @Test
    public void testPegasus() {
        BaseSecantSolver solver = new BaseSecantSolver.PegasusSolver(1e-6);
        double result = solver.solve(10, TEST_FUNCTION, -3.0, 3.0, 0.0);
        assertEquals("The solver should return the square root of 2.",
                     Math.sqrt(2), Precision.round(result, 5));
    }

    /**
     * Test that the solver throws an IllegalArgumentException when the
     * function value at the minimum is equal to zero.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalArgumentExceptionMin() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        new BaseSecantSolver(1e-6, Method.REGULA_FALSI)
                .solve(10, f, 0.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test that the solver throws an IllegalArgumentException when the
     * function value at the maximum is equal to zero.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalArgumentExceptionMax() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        new BaseSecantSolver(1e-6, Method.REGULA_FALSI)
                .solve(10, f, 0.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test that the solver throws an IllegalArgumentException when the
     * function value at the starting point is equal to zero.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalArgumentExceptionStart() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };
        new BaseSecantSolver(1e-6, Method.REGULA_FALSI)
                .solve(10, f, 0.0, 0.0, 0.0);
    }

    /**
     * Test that the solver returns the correct value for a simple function
     * with specified tolerances and allowed solutions.
     */
    @Test
    public void testSpecifiedTolerancesAndAllowedSolutions() {
        BaseSecantSolver solver = new BaseSecantSolver
                (1e-12, 1e-12, 1e-12, Method.ILLINOIS) {
            @Override
            public AllowedSolution getAllowedSolution() {
                return AllowedSolution.BELOW_SIDE;
            }
        };
        double result = solver.solve(10, TEST_FUNCTION, -3.0, 3.0, 0.0);
        assertTrue("The solver should return a solution below the root.",
                  result < Math.sqrt(2));
    }
}