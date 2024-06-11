package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double)}
     * with default constructor arguments.
     * @throws NoBracketingException
     * @throws MaxEvalException
     */
    @Test
    public void testBaseSecantSolverDefaultConstructor()
            throws NoBracketingException, MaxEvalException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}
     * with given absolute accuracy and Regula Falsi method.
     * @throws NoBracketingException
     * @throws MaxEvalException
     */
    @Test
    public void testBaseSecantSolverCustomConstructorWithAbsoluteAccuracyAndRegulaFalsiMethod()
            throws NoBracketingException, MaxEvalException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}
     * with given relative and absolute accuracy and Regula Falsi method.
     * @throws NoBracketingException
     * @throws MaxEvalException
     */
    @Test
    public void testBaseSecantSolverCustomConstructorWithRelativeAndAbsoluteAccuracyAndRegulaFalsiMethod()
            throws NoBracketingException, MaxEvalException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}
     * with given relative, absolute accuracy and function value accuracy and Regula Falsi method.
     * @throws NoBracketingException
     * @throws MaxEvalException
     */
    @Test
    public void testBaseSecantSolverCustomConstructorWithRelativeAbsoluteAndFunctionValueAccuracyAndRegulaFalsiMethod()
            throws NoBracketingException, MaxEvalException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-8, 1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with given function and bounds and Regula Falsi method.
     * @throws NoBracketingException
     * @throws MaxEvalException
     */
    @Test
    public void testBaseSecantSolverSolveWithRegulaFalsiMethod()
            throws NoBracketingException, MaxEvalException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with given function, bounds, start value and allowed solution and Regula Falsi method.
     * @throws NoBracketingException
     * @throws MaxEvalException
     */
    @Test
    public void testBaseSecantSolverSolveWithRegulaFalsiMethodAndAllowedSolution()
            throws NoBracketingException, MaxEvalException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with given function and bounds and Illinois method.
     * @throws NoBracketingException
     * @throws MaxEvalException
     */
    @Test
    public void testBaseSecantSolverSolveWithIllinoisMethod()
            throws NoBracketingException, MaxEvalException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with given function, bounds, start value, allowed solution and Illinois method.
     * @throws NoBracketingException
     * @throws MaxEvalException
     */
    @Test
    public void testBaseSecantSolverSolveWithIllinoisMethodAndAllowedSolution()
            throws NoBracketingException, MaxEvalException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with given function and bounds and Pegasus method.
     * @throws NoBracketingException
     * @throws MaxEvalException
     */
    @Test
    public void testBaseSecantSolverSolveWithPegasusMethod()
            throws NoBracketingException, MaxEvalException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertEquals(Math.E, root, 1e-9);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with given function, bounds, start value, allowed solution and Pegasus method.
     * @throws NoBracketingException
     * @throws MaxEvalException
     */
    @Test
    public void testBaseSecantSolverSolveWithPegasusMethodAndAllowedSolution()
            throws NoBracketingException, MaxEvalException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(10, f, -10.0, 10.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals(Math.E, root, 1e-9);
    }
}