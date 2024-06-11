package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtil;
import org.apache.commons.math3.util.Doubles;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.math.analysis.solvers.PegasusSolver;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double)}.
     */
    @Test
    public void testBaseSecantSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6);
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double)}.
     */
    @Test
    public void testBaseSecantSolverWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new IllinoisSolver(1e-4, 1e-6);
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(Math.PI / 3.0, root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double)}.
     */
    @Test
    public void testBaseSecantSolverWithAbsoluteAndRelativeAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3.0) - 2.0;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new PegasusSolver(1e-4, 1e-6, 1e-8);
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(-1.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverWithMethod() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2.0;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-4, 1e-6, Method.REGULA_FALSI);
        double root = solver.solve(f, -5.0, 0.0);
        assertEquals(Math.log(2.0), root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 1.0;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new IllinoisSolver(1e-4, 1e-6);
        double root = solver.solve(f, -2.0, 2.0, 0.0);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new IllinoisSolver(1e-4, 1e-6);
        double root = solver.solve(f, -3.0, 3.0, 0.0, AllowedSolution.BELOW_SIDE);
        assertEquals(Math.PI / 6.0, root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolutionAndInvertedInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 0.5;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new PegasusSolver(1e-4, 1e-6);
        double root = solver.solve(f, -3.0, 3.0, 0.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(-Math.PI / 2.0, root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolutionAndExactRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 1.0;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-4, 1e-6);
        double root = solver.solve(f, 0.0, 0.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-8);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithAllowedSolutionAndInvalidAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 1.0;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-4, 1e-6);
        solver.solve(f, 0.0, 0.0, 0.0, AllowedSolution.OUT_OF_BOUNDS);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = MathInternalError.class)
    public void testSolveWithMethodNotImplemented() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 1.0;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-4, 1e-6, Method.SECANT) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException("Secant method not implemented in this class.");
            }
        };
        solver.solve(f, 0.0, 0.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = MathInternalError.class)
    public void testSolveWithIllegalArgumentException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new IllegalArgumentException("Invalid input.");
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-4, 1e-6);
        solver.solve(f, -3.0, 3.0, 0.0, AllowedSolution.ANY_SIDE);
    }
}