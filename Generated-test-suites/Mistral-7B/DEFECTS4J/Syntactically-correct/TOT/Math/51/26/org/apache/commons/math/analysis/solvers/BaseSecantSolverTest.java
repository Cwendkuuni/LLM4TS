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
    public void testRegulaFalsi() {
        // Function f(x) = x^3 - 2
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, f);
        try {
            double root = solver.solve(100, f, -10.0, 10.0);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalException | NoBracketingException e) {
            fail("Exception should not be thrown");
        }

        // Testing with a function that has no real roots
        UnivariateRealFunction fNoRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 1;
            }
        };
        try {
            solver = new RegulaFalsiSolver(1e-6, fNoRoots);
            solver.solve(100, fNoRoots, -10.0, 10.0);
            fail("NoBracketingException should have been thrown");
        } catch (NoBracketingException e) {
            // expected
        }

        // Testing with a function that is not differentiable at the root
        UnivariateRealFunction fNotDifferentiable = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 1), 0.5);
            }

            @Override
            public double derivative(double x) throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }
        };
        try {
            solver = new RegulaFalsiSolver(1e-6, fNotDifferentiable);
            solver.solve(100, fNotDifferentiable, -10.0, 10.0);
            fail("NoBracketingException or MathInternalError should have been thrown");
        } catch (NoBracketingException | MathInternalError e) {
            // expected
        }
    }

    @Test
    public void testIllinois() {
        // Function f(x) = x^3 - 2
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6, f);
        try {
            double root = solver.solve(100, f, -10.0, 10.0);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalException | NoBracketingException e) {
            fail("Exception should not be thrown");
        }

        // Testing with a function that has no real roots
        UnivariateRealFunction fNoRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 1;
            }
        };
        try {
            solver = new IllinoisSolver(1e-6, fNoRoots);
            solver.solve(100, fNoRoots, -10.0, 10.0);
            fail("NoBracketingException should have been thrown");
        } catch (NoBracketingException e) {
            // expected
        }

        // Testing with a function that is not differentiable at the root
        UnivariateRealFunction fNotDifferentiable = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 1), 0.5);
            }

            @Override
            public double derivative(double x) throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }
        };
        try {
            solver = new IllinoisSolver(1e-6, fNotDifferentiable);
            solver.solve(100, fNotDifferentiable, -10.0, 10.0);
            fail("NoBracketingException or MathInternalError should have been thrown");
        } catch (NoBracketingException | MathInternalError e) {
            // expected
        }
    }

    @Test
    public void testPegasus() {
        // Function f(x) = x^3 - 2
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 2;
            }
        };

        BaseSecantSolver solver = new PegasusSolver(1e-6, f);
        try {
            double root = solver.solve(100, f, -10.0, 10.0);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalException | NoBracketingException e) {
            fail("Exception should not be thrown");
        }

        // Testing with a function that has no real roots
        UnivariateRealFunction fNoRoots = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + 1;
            }
        };
        try {
            solver = new PegasusSolver(1e-6, fNoRoots);
            solver.solve(100, fNoRoots, -10.0, 10.0);
            fail("NoBracketingException should have been thrown");
        } catch (NoBracketingException e) {
            // expected
        }

        // Testing with a function that is not differentiable at the root
        UnivariateRealFunction fNotDifferentiable = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 1), 0.5);
            }

            @Override
            public double derivative(double x) throws UnsupportedOperationException {
                throw new UnsupportedOperationException();
            }
        };
        try {
            solver = new PegasusSolver(1e-6, fNotDifferentiable);
            solver.solve(100, fNotDifferentiable, -10.0, 10.0);
            fail("NoBracketingException or MathInternalError should have been thrown");
        } catch (NoBracketingException | MathInternalError e) {
            // expected
        }

        // Testing the function f(x) = x^4 - 4
        UnivariateRealFunction fComplexRoot = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x * x - 4;
            }
        };
        try {
            solver = new PegasusSolver(1e-6, fComplexRoot);
            double root = solver.solve(100, fComplexRoot, -10.0, 10.0);
            assertTrue(Precision.abs(root * root * root * root - 4) < 1e-6);
        } catch (NoBracketingException | MathInternalError e) {
            fail("Exception should not have been thrown");
        }
    }
}