package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * with {@link BaseSecantSolver#RegulaFalsi} method.
     */
    @Test
    public void testSolveRegulaFalsi() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, f);
        assertEquals(-2.0, solver.solve(100, f, -5.0, 5.0, 0.0), 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * with {@link BaseSecantSolver#Illinois} method.
     */
    @Test
    public void testSolveIllinois() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-6, f);
        assertEquals(1.2325542516163553776954133445114, solver.solve(100, f, 0.0, 2.0, 1.0), 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * with {@link BaseSecantSolver#Pegasus} method.
     */
    @Test
    public void testSolvePegasus() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3.0) - 2.0;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1e-6, f);
        assertEquals(-1.0, solver.solve(100, f, -2.0, 2.0, 0.0), 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * with non-bracketing interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveNonBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, f);
        solver.solve(100, f, 2.0, 1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * with max evaluation.
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveMaxEval() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -x);
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, f);
        solver.solve(0, f, 0.0, 0.0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * with unsupported method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveUnsupportedMethod() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, f);
        solver.method = BaseSecantSolver.Method.UNDEFINED;
        solver.solve(100, f, 0.0, 2.0, 1.0);
    }

}