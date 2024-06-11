package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolveRegulaFalsi() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        try {
            double root = solver.solve(20, f, -1.0, 1.0);
            assertEquals(0.0, root, 1e-8);
        } catch (MaxEvalException e) {
            fail("Should have found the root within the allowed number of evaluations.");
        }
    }

    @Test
    public void testSolveIllinois() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        try {
            double root = solver.solve(20, f, -5.0, 5.0);
            assertEquals(Math.PI / 2.0, root, 1e-6);
        } catch (MaxEvalException e) {
            fail("Should have found the root within the allowed number of evaluations.");
        }
    }

    @Test
    public void testSolvePegasus() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2.0;
            }
        };

        BaseSecantSolver solver = new PegasusSolver(1e-6);
        try {
            double root = solver.solve(20, f, -2.0, 2.0);
            assertEquals(-1.0, root, 1e-8);
        } catch (MaxEvalException e) {
            fail("Should have found the root within the allowed number of evaluations.");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetRelativeAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        solver.setRelativeAccuracy(0.1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFunctionValueAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, 1e-8);
        solver.setFunctionValueAccuracy(0.1);
    }

    @Test
    public void testVerifyBracketing() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        try {
            double[] bounds = new double[]{-1.0, -1.0};
            solver.verifyBracketing(bounds[0], bounds[1]);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.verifyBracketing(1.0, 1.0);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.verifyBracketing(0.0, 0.0);
            // Expected no exception
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException.");
        }

        try {
            solver.verifyBracketing(1.0, 0.0);
            // Expected no exception
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException.");
        }

        try {
            solver.verifyBracketing(-1.0, 0.0);
            // Expected IllegalArgumentException
            fail("Unexpected no exception.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, AllowedSolution.LEFT_SIDE);
        try {
            double root = solver.solve(20, f, -1.0, 1.0);
            assertEquals(0.0, root, 1e-8);
            assertTrue(Precision.isClose(solver.getBracket()[0], 0.0, 1e-8));
            assertTrue(Precision.isClose(solver.getBracket()[1], 0.0, 1e-8));
        } catch (MaxEvalException e) {
            fail("Should have found the root within the allowed number of evaluations.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidAllowedSolution() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, AllowedSolution.BELOW_SIDE);
        try {
            solver.solve(20, f, -1.0, 1.0);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}