package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test a simple case using the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * method.
     */
    @Test
    public void testSimpleCaseRegulaFalsi() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) - 0.5;
            }
        };
        final BracketedUnivariateRealSolver s = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -1.0, 1.0);
            }
        };
        assertEquals(0.0, s.solve(100, f, -1.0, 1.0), 1e-4);
    }

    /**
     * Test a simple case using the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * method with {@code AllowedSolution.LEFT_SIDE}.
     */
    @Test
    public void testSimpleCaseIllinoisLeftSide() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        final BracketedUnivariateRealSolver s = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -2.0, 1.0, AllowedSolution.LEFT_SIDE);
            }
        };
        assertEquals(-1.0, s.solve(100, f, -2.0, 1.0), 1e-4);
    }

    /**
     * Test a simple case using the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * method with {@code AllowedSolution.RIGHT_SIDE}.
     */
    @Test
    public void testSimpleCaseIllinoisRightSide() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        final BracketedUnivariateRealSolver s = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -2.0, 1.0, AllowedSolution.RIGHT_SIDE);
            }
        };
        assertEquals(1.0, s.solve(100, f, -2.0, 1.0), 1e-4);
    }

    /**
     * Test a simple case using the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * method with {@code BaseSecantSolver.Method.PEGASUS}.
     */
    @Test
    public void testSimpleCasePegasus() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(FastMath.E, x) - 2.0;
            }
        };
        final BracketedUnivariateRealSolver s = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -2.0, 0.0);
            }
        };
        assertEquals(1.0, s.solve(100, f, -2.0, 0.0), 1e-4);
    }

    /**
     * Test that {@link BaseSecantSolver} throws a {@link NoBracketingException}
     * if the initial bounds do not bracket the root.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingException() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        final BracketedUnivariateRealSolver s = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, 1.0, 1.0);
            }
        };
        s.solve(100, f, 1.0, 1.0);
    }

    /**
     * Test that {@link BaseSecantSolver} throws a {@link ConvergenceException} when
     * the maximum number of iterations is exceeded.
     */
    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        final BracketedUnivariateRealSolver s = new BaseSecantSolver(1e-12, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected int getMaxIterations() {
                return 1;
            }
        };
        s.solve(1, f, -2.0, 1.0);
    }

    /**
     * Test that {@link BaseSecantSolver} throws a {@link MathInternalError} if an
     * unknown method is provided.
     */
    @Test(expected = MathInternalError.class)
    public void testUnknownMethod() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        final BracketedUnivariateRealSolver s = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.UNKOWN) {
            @Override
            protected double doSolve() {
                return super.solve(100, f, -2.0, 1.0);
            }
        };
        s.solve(100, f, -2.0, 1.0);
    }

    /**
     * Test that {@link BaseSecantSolver} throws a {@link MaxIterationsExceededException} when
     * the maximum number of iterations is exceeded during the test of a maximum number of iterations.
     */
    @Test
    public void testMaxIterationsExceededException() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        final BracketedUnivariateRealSolver s = new BaseSecantSolver(1e-4, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected int getMaxIterations() {
                return 1;
            }
        };
        assertThrows(MaxIterationsExceededException.class, () -> s.solve(1, f, -2.0, 1.0));
    }
}