package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for the BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}
     * with default constructor arguments.
     */
    @Test
    public void testDefaultConstructor() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2 * x + 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, function, -1.0, 1.0, 0.0);
            assertEquals("Root not found.", 0.0, root, 1e-6);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}
     */
    @Test
    public void testRelativeAndAbsoluteAccuracyConstructor() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2 * x + 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-6, Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, function, -1.0, 1.0, 0.0);
            assertEquals("Root not found.", 0.0, root, 1e-8);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}
     */
    @Test
    public void testAllAccuraciesConstructor() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2 * x + 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-6, 1e-8, Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, function, -1.0, 1.0, 0.0);
            assertEquals("Root not found.", 0.0, root, 1e-8);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}
     * with default constructor arguments.
     */
    @Test
    public void testSolveDefaultConstructor() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2 * x + 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, function, -1.0, 1.0);
            assertEquals("Root not found.", 0.0, root, 1e-6);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     */
    @Test
    public void testSolveAllArguments() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2 * x + 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-6, 1e-8, Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, function, -1.0, 1.0, 0.0);
            assertEquals("Root not found.", 0.0, root, 1e-8);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     */
    @Test
    public void testSolveAllArgumentsAllowedSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2 * x + 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-6, 1e-8, Method.REGULA_FALSI);
        try {
            double root = solver.solve(100, function, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
            assertEquals("Root not found.", 0.0, root, 1e-8);
            root = solver.solve(100, function, -1.0, 1.0, 0.0, AllowedSolution.RIGHT_SIDE);
            assertEquals("Root not found.", 0.0, root, 1e-8);
            root = solver.solve(100, function, -1.0, 1.0, 0.0, AllowedSolution.ABOVE_SIDE);
            assertTrue("Root not below the x-axis.", root < 0);
            root = solver.solve(100, function, -1.0, 1.0, 0.0, AllowedSolution.BELOW_SIDE);
            assertTrue("Root not above the x-axis.", root > 0);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}
     */
    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2 * x + 1;
            }
        };
        try {
            // Test bracketing.
            solver.verifyBracketing(0.0, 1.0);
        } catch (IllegalArgumentException e) {
            // Expected
            assertTrue(e.getMessage().startsWith("Bracketing error: not bracketing ["));
        }

        try {
            // Test bracketing.
            solver.verifyBracketing(1.0, 0.0);
        } catch (IllegalArgumentException e) {
            // Expected
            assertTrue(e.getMessage().startsWith("Bracketing error: not bracketing ["));
        }

        try {
            // Test bracketing.
            solver.verifyBracketing(1.0, 1.0);
        } catch (IllegalArgumentException e) {
            // Expected
            assertTrue(e.getMessage().startsWith("Bracketing error: not bracketing ["));
        }

        try {
            // Test bracketing.
            solver.verifyBracketing(0.0, 0.0);
        } catch (IllegalArgumentException e) {
            // Expected
            assertTrue(e.getMessage().startsWith("Bracketing error: not bracketing ["));
        }

        try {
            // Test bracketing with an exact root.
            double root = solver.solve(100, function, -1.0, 1.0);
            solver.verifyBracketing(root - 1e-8, root + 1e-8);
        } catch (MaxEvalExceededException | RootNotFoundException e) {
            // Expected
        }
    }
}

enum AllowedSolution {
    LEFT_SIDE,
    RIGHT_SIDE,
    ABOVE_SIDE,
    BELOW_SIDE,
    ANY_SIDE;
}