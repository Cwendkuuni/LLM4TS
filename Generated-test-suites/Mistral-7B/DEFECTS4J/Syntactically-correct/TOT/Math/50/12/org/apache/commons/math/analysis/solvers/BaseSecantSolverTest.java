package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.analysis.functions.SimpleFunctions.*;
import static org.apache.commons.math3.analysis.solvers.BaseSecantSolver.*;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test a simple regula falsi method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testRegulaFalsi() throws Exception {
        // Create a solver
        final BaseSecantSolver solver = new BaseSecantSolver<>(1e-6, METHOD_REGULA_FALSI);

        // Define and test a simple function
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        // Compute the root with the solver and compare with the known result
        final double root = solver.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct.", -0.5, root, 1e-8);

        // Test with a relative tolerance
        final BaseSecantSolver solver2 = new BaseSecantSolver<>(1e-5, 1e-6, METHOD_REGULA_FALSI);
        final double root2 = solver2.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct with relative tolerance.", -0.5, root2, 5e-7);

        // Test with an absolute tolerance
        final BaseSecantSolver solver3 = new BaseSecantSolver<>(1e-6, 1e-4, METHOD_REGULA_FALSI);
        final double root3 = solver3.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct with absolute tolerance.", -0.5, root3, 1e-4);

        // Test with both tolerances
        final BaseSecantSolver solver4 = new BaseSecantSolver<>(1e-5, 1e-6, 1e-4, METHOD_REGULA_FALSI);
        final double root4 = solver4.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct with both tolerances.", -0.5, root4, 5e-7);

        // Test with an incorrect function
        final UnivariateFunction incorrectFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("This function is not supported.");
            }
        };
        try {
            solver.solve(10, incorrectFunction, -2.5, 2.5);
            fail("An exception should have been thrown.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with a non-bracketing function
        final UnivariateFunction nonBracketingFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        try {
            solver.solve(10, nonBracketingFunction, -2.5, 2.5);
            fail("An exception should have been thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with too many evaluations
        try {
            solver.solve(0, f, -2.5, 2.5);
            fail("An exception should have been thrown.");
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    /**
     * Test a simple Illinois method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIllinois() throws Exception {
        // Create a solver
        final BaseSecantSolver solver = new BaseSecantSolver<>(1e-6, METHOD_ILLINOIS);

        // Define and test a simple function
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        // Compute the root with the solver and compare with the known result
        final double root = solver.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct.", -0.5, root, 1e-8);

        // Test with a relative tolerance
        final BaseSecantSolver solver2 = new BaseSecantSolver<>(1e-5, 1e-6, METHOD_ILLINOIS);
        final double root2 = solver2.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct with relative tolerance.", -0.5, root2, 5e-7);

        // Test with an absolute tolerance
        final BaseSecantSolver solver3 = new BaseSecantSolver<>(1e-6, 1e-4, METHOD_ILLINOIS);
        final double root3 = solver3.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct with absolute tolerance.", -0.5, root3, 1e-4);

        // Test with both tolerances
        final BaseSecantSolver solver4 = new BaseSecantSolver<>(1e-5, 1e-6, 1e-4, METHOD_ILLINOIS);
        final double root4 = solver4.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct with both tolerances.", -0.5, root4, 5e-7);

        // Test with an incorrect function
        final UnivariateFunction incorrectFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("This function is not supported.");
            }
        };
        try {
            solver.solve(10, incorrectFunction, -2.5, 2.5);
            fail("An exception should have been thrown.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with a non-bracketing function
        final UnivariateFunction nonBracketingFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        try {
            solver.solve(10, nonBracketingFunction, -2.5, 2.5);
            fail("An exception should have been thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with too many evaluations
        try {
            solver.solve(0, f, -2.5, 2.5);
            fail("An exception should have been thrown.");
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    /**
     * Test a simple pegasus method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testPegasus() throws Exception {
        // Create a solver
        final BaseSecantSolver solver = new BaseSecantSolver<>(1e-6, METHOD_PEGASUS);

        // Define and test a simple function
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        // Compute the root with the solver and compare with the known result
        final double root = solver.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct.", -0.5, root, 1e-8);

        // Test with a relative tolerance
        final BaseSecantSolver solver2 = new BaseSecantSolver<>(1e-5, 1e-6, METHOD_PEGASUS);
        final double root2 = solver2.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct with relative tolerance.", -0.5, root2, 5e-7);

        // Test with an absolute tolerance
        final BaseSecantSolver solver3 = new BaseSecantSolver<>(1e-6, 1e-4, METHOD_PEGASUS);
        final double root3 = solver3.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct with absolute tolerance.", -0.5, root3, 1e-4);

        // Test with both tolerances
        final BaseSecantSolver solver4 = new BaseSecantSolver<>(1e-5, 1e-6, 1e-4, METHOD_PEGASUS);
        final double root4 = solver4.solve(10, f, -2.5, 2.5);
        assertEquals("The root is not correct with both tolerances.", -0.5, root4, 5e-7);

        // Test with an incorrect function
        final UnivariateFunction incorrectFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("This function is not supported.");
            }
        };
        try {
            solver.solve(10, incorrectFunction, -2.5, 2.5);
            fail("An exception should have been thrown.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with a non-bracketing function
        final UnivariateFunction nonBracketingFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        try {
            solver.solve(10, nonBracketingFunction, -2.5, 2.5);
            fail("An exception should have been thrown.");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with too many evaluations
        try {
            solver.solve(0, f, -2.5, 2.5);
            fail("An exception should have been thrown.");
        } catch (MaxEvalException e) {
            // Expected
        }
    }
}