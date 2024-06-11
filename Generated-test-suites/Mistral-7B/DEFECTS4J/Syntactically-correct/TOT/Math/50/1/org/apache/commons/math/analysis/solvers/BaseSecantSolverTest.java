package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        try {
            double root = solver.solve(100, f, -5.0, 5.0, 0.0);
            assertEquals(2.0, root, 1e-6);
        } catch (MaxEvalException e) {
            fail("The root should have been found within the given number of evaluations.");
        } catch (NoBracketingException e) {
            fail("The function should have been bracketing a root.");
        } catch (UnsupportedOperationException e) {
            fail("The solver should be able to solve the given function.");
        }

        try {
            solver = new PegasusSolver(1e-6);
            root = solver.solve(100, f, -5.0, 5.0, 0.0);
            assertEquals(2.0, root, 1e-6);
        } catch (MaxEvalException e) {
            fail("The root should have been found within the given number of evaluations.");
        } catch (NoBracketingException e) {
            fail("The function should have been bracketing a root.");
        } catch (UnsupportedOperationException e) {
            fail("The solver should be able to solve the given function.");
        }

        try {
            solver = new IllinoisSolver(1e-6);
            root = solver.solve(100, f, -5.0, 5.0, 0.0);
            assertEquals(2.0, root, 1e-6);
        } catch (MaxEvalException e) {
            fail("The root should have been found within the given number of evaluations.");
        } catch (NoBracketingException e) {
            fail("The function should have been bracketing a root.");
        } catch (UnsupportedOperationException e) {
            fail("The solver should be able to solve the given function.");
        }

        try {
            solver = new RegulaFalsiSolver(1e-5);
            root = solver.solve(100, f, -5.0, 5.0, 0.0);
            assertTrue(Math.abs(root - 2.0) > 1e-5);
            fail("The root should not have been found with the given relative accuracy.");
        } catch (MaxEvalException e) {
            // Expected
        } catch (NoBracketingException e) {
            fail("The function should have been bracketing a root.");
        } catch (UnsupportedOperationException e) {
            fail("The solver should be able to solve the given function.");
        }

        try {
            solver = new PegasusSolver(1e-5);
            root = solver.solve(100, f, -5.0, 5.0, 0.0);
            assertTrue(Math.abs(root - 2.0) > 1e-5);
            fail("The root should not have been found with the given relative accuracy.");
        } catch (MaxEvalException e) {
            // Expected
        } catch (NoBracketingException e) {
            fail("The function should have been bracketing a root.");
        } catch (UnsupportedOperationException e) {
            fail("The solver should be able to solve the given function.");
        }

        try {
            solver = new IllinoisSolver(1e-5);
            root = solver.solve(100, f, -5.0, 5.0, 0.0);
            assertTrue(Math.abs(root - 2.0) > 1e-5);
            fail("The root should not have been found with the given relative accuracy.");
        } catch (MaxEvalException e) {
            // Expected
        } catch (NoBracketingException e) {
            fail("The function should have been bracketing a root.");
        } catch (UnsupportedOperationException e) {
            fail("The solver should be able to solve the given function.");
        }

        try {
            UnivariateRealFunction g = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.tan(x) - x;
                }
            };
            solver = new RegulaFalsiSolver(1e-6);
            root = solver.solve(100, g, -10.0, 10.0, 0.0);
            fail("The tangent function doesn't have a root.");
        } catch (MaxEvalException e) {
            // Expected
        } catch (NoBracketingException e) {
            // Expected
        } catch (UnsupportedOperationException e) {
            fail("The solver should be able to handle the given function.");
        }

        try {
            UnivariateRealFunction h = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return x * Math.sin(1 / x);
                }
            };
            solver = new RegulaFalsiSolver(1e-6);
            root = solver.solve(100, h, -10.0, 10.0, 0.0);
            fail("The sine function doesn't have a root.");
        } catch (MaxEvalException e) {
            // Expected
        } catch (NoBracketingException e) {
            // Expected
        } catch (UnsupportedOperationException e) {
            fail("The solver should be able to handle the given function.");
        }

        try {
            UnivariateRealFunction i = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.log(x);
                }
            };
            solver = new RegulaFalsiSolver(1e-6);
            root = solver.solve(100, i, 0.0, 1.0, 1.0);
            assertEquals(1.0, root, Precision.EPSILON);
        } catch (MaxEvalException e) {
            fail("The logarithm function should have been solved.");
        } catch (NoBracketingException e) {
            fail("The function should have been bracketing a root.");
        } catch (UnsupportedOperationException e) {
            fail("The solver should be able to handle the given function.");
        }
    }
}