package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        // Test with a simple function.
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected BracketedUnivariateRealSolver<UnivariateRealFunction> create(
                    double absoluteAccuracy, double relativeAccuracy,
                    double functionValueAccuracy, BaseSecantSolver.Method method) {
                return new RegulaFalsiSolver(absoluteAccuracy, relativeAccuracy,
                        functionValueAccuracy, method);
            }
        };
        double root = solver.solve(10, f, -2.0, 2.0, 0.0);
        assertEquals("Root should be approximately 1.4142", 1.414213741055263, root, 1e-6);

        // Test with a function that doesn't bracket the root.
        UnivariateFunction f2 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected BracketedUnivariateRealSolver<UnivariateRealFunction> create(
                    double absoluteAccuracy, double relativeAccuracy,
                    double functionValueAccuracy, BaseSecantSolver.Method method) {
                return new IllinoisSolver(absoluteAccuracy, relativeAccuracy,
                        functionValueAccuracy, method);
            }
        };
        try {
            solver2.solve(10, f2, -5.0, 5.0, 0.0);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected result
        }

        // Test with a function that doesn't converge.
        UnivariateFunction f3 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected BracketedUnivariateRealSolver<UnivariateRealFunction> create(
                    double absoluteAccuracy, double relativeAccuracy,
                    double functionValueAccuracy, BaseSecantSolver.Method method) {
                return new PegasusSolver(absoluteAccuracy, relativeAccuracy,
                        functionValueAccuracy, method);
            }
        };
        try {
            solver3.solve(100, f3, -5.0, 5.0, 0.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected result
        }

        // Test with a function that takes too many iterations to converge.
        UnivariateFunction f4 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 4) - 2;
            }
        };
        BaseSecantSolver solver4 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected BracketedUnivariateRealSolver<UnivariateRealFunction> create(
                    double absoluteAccuracy, double relativeAccuracy,
                    double functionValueAccuracy, BaseSecantSolver.Method method) {
                return new RegulaFalsiSolver(absoluteAccuracy, relativeAccuracy,
                        functionValueAccuracy, method);
            }
        };
        try {
            solver4.solve(5, f4, 0.0, 3.0, 0.0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected result
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        // Test with a simple function.
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected BracketedUnivariateRealSolver<UnivariateRealFunction> create(
                    double absoluteAccuracy, double relativeAccuracy,
                    double functionValueAccuracy, BaseSecantSolver.Method method) {
                return new RegulaFalsiSolver(absoluteAccuracy, relativeAccuracy,
                        functionValueAccuracy, method);
            }
        };
        double root = solver.solve(10, f, -2.0, 2.0, 0.0, AllowedSolution.RIGHT_SIDE);
        assertEquals("Root should be approximately 1.4142", 1.414213741055263, root, 1e-6);

        // Test with a function that doesn't bracket the root and left solution.
        UnivariateFunction f2 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected BracketedUnivariateRealSolver<UnivariateRealFunction> create(
                    double absoluteAccuracy, double relativeAccuracy,
                    double functionValueAccuracy, BaseSecantSolver.Method method) {
                return new IllinoisSolver(absoluteAccuracy, relativeAccuracy,
                        functionValueAccuracy, method);
            }
        };
        try {
            solver2.solve(10, f2, -5.0, 5.0, 0.0, AllowedSolution.LEFT_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected result
        }

        // Test with a function that doesn't converge and left solution.
        UnivariateFunction f3 = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BaseSecantSolver solver3 = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected BracketedUnivariateRealSolver<UnivariateRealFunction> create(
                    double absoluteAccuracy, double relativeAccuracy,
                    double functionValueAccuracy, BaseSecantSolver.Method method) {
                return new PegasusSolver(absoluteAccuracy, relativeAccuracy,
                        functionValueAccuracy, method);
            }
        };
        try {
            solver3.solve(100, f3, -5.0, 5.0, 0.0, AllowedSolution.LEFT_SIDE);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected result
        }
    }
}