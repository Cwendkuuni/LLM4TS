package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsi() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        try {
            double root = solver.solve(100, f, -3.0, 3.0, 0.0);
            assertEquals("Root should be around 1.41421356", 1.41421356, root, 1.0e-6);
        } catch (NoBracketingException e) {
            fail("Should have found bracketing");
        } catch (MaxEvalException e) {
            fail("Should have found root within max evaluations");
        }
    }

    @Test
    public void testSolver_Illinois() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 6.0 * x + 9.0;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);
        try {
            double root = solver.solve(100, f, -3.0, 3.0, 0.0);
            assertEquals("Root should be around 2.0", 2.0, root, 1.0e-6);
        } catch (NoBracketingException e) {
            fail("Should have found bracketing");
        } catch (MaxEvalException e) {
            fail("Should have found root within max evaluations");
        }
    }

    @Test
    public void testSolver_Pegasus() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.tan(x) - x;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1.0e-6);
        try {
            double root = solver.solve(100, f, -3.0, 3.0, 0.0);
            assertEquals("Root should be around 0.45973313", 0.45973313, root, 1.0e-6);
        } catch (NoBracketingException e) {
            fail("Should have found bracketing");
        } catch (MaxEvalException e) {
            fail("Should have found root within max evaluations");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolver_Secant() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.SECANT);
        solver.solve(100, f, -3.0, 3.0, 0.0);
    }

    @Test
    public void testSolver_RelativeAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(0.01, 1.0e-6);
        try {
            double root = solver.solve(100, f, -3.0, 3.0, 0.0);
            double relativeError = Precision.abs(1.41421356 - root) / Precision.abs(1.41421356);
            assertTrue("Relative error should be below 1%", relativeError < 0.01);
        } catch (NoBracketingException e) {
            fail("Should have found bracketing");
        } catch (MaxEvalException e) {
            fail("Should have found root within max evaluations");
        }
    }

    @Test
    public void testSolver_AbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6, 1.0e-8);
        try {
            double root = solver.solve(100, f, -3.0, 3.0, 0.0);
            assertEquals("Absolute error should be below 1e-8", 0.0, Precision.abs(1.41421356 - root), 1.0e-8);
        } catch (NoBracketingException e) {
            fail("Should have found bracketing");
        } catch (MaxEvalException e) {
            fail("Should have found root within max evaluations");
        }
    }
}