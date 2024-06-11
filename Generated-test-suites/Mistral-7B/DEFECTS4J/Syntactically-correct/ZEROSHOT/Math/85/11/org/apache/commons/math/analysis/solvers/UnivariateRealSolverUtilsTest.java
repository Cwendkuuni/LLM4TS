package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.BesselFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BisectionSolver;
import org.apache.commons.math3.solvers.SecantSolver;

public class UnivariateRealSolverUtilsTest {

    /**
     * Test for solve method with default solver.
     * @throws Exception
     */
    @org.junit.Test
    public void testSolveDefaultSolver() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test for solve method with specified solver and accuracy.
     * @throws Exception
     */
    @org.junit.Test
    public void testSolveSpecifiedSolverAndAccuracy() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0, 1e-9);
        assertEquals(0.0, root, 1e-10);
    }

    /**
     * Test for bracket method.
     * @throws Exception
     */
    @org.junit.Test
    public void testBracket() throws Exception {
        UnivariateRealFunction function = new BesselFunction(BesselFunction.BesselFunctionType.SIMPLE, 0);
        double[] bracketedRoot = UnivariateRealSolverUtils.bracket(function, 0.5, -1.0, 1.0);
        double a = bracketedRoot[0];
        double b = bracketedRoot[1];
        double root = UnivariateRealSolverUtils.solve(function, a, b);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test for bracket method with specified maximum iterations.
     * @throws Exception
     */
    @org.junit.Test
    public void testBracketMaxIterations() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        double[] bracketedRoot = UnivariateRealSolverUtils.bracket(function, -2.0, 2.0, 1, 100);
        double a = bracketedRoot[0];
        double b = bracketedRoot[1];
        double root = UnivariateRealSolverUtils.solve(function, a, b);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test for midpoint method.
     * @throws Exception
     */
    @org.junit.Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;
        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(1.5, midpoint, 1e-6);
    }

    /**
     * Test for setup method.
     * @throws Exception
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetupNullFunction() throws Exception {
        UnivariateRealSolverUtils.setup(null);
    }

    /**
     * Test for solve method with Brent solver.
     * @throws Exception
     */
    @org.junit.Test
    public void testSolveBrentSolver() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0, new BrentSolver());
        assertEquals(-2.0, root, 1e-6);
    }

    /**
     * Test for solve method with Bisection solver.
     * @throws Exception
     */
    @org.junit.Test
    public void testSolveBisectionSolver() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 2.0, new BisectionSolver());
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test for solve method with Secant solver.
     * @throws Exception
     */
    @org.junit.Test
    public void testSolveSecantSolver() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 1);
        double x1 = 1.0;
        double x2 = 2.0;
        double root = UnivariateRealSolverUtils.solve(function, x1, x2, new SecantSolver());
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test for solve method with FunctionEvaluationException.
     * @throws Exception
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test function evaluation exception");
            }
        };
        UnivariateRealSolverUtils.solve(function, 0.0, 0.0);
    }

    /**
     * Test for solve method with ConvergenceException.
     * @throws Exception
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils.signum(Math.sin(x)) * Math.pow(Math.abs(x), 2);
            }
        };
        UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
    }

    /**
     * Test for solve method with IllegalArgumentException.
     * @throws Exception
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSolveIllegalArgumentException() throws Exception {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };
        UnivariateRealSolverUtils.solve(function, -1.0, Double.POSITIVE_INFINITY);
    }

    /**
     * Test for solve method with bracketed roots.
     * @throws Exception
     */
    @org.junit.Test
    public void testSolveWithBracketedRoots() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4);
        double[] bracketedRoots = UnivariateRealSolverUtils.bracket(function, -2.0, 2.0);
        double a = bracketedRoots[0];
        double b = bracketedRoots[1];
        double root1 = UnivariateRealSolverUtils.solve(function, a, b, new BrentSolver());
        double root2 = UnivariateRealSolverUtils.solve(function, a, b, new SecantSolver());
        assertEquals(-2.0, root1, 1e-6);
        assertEquals(-1.5, root2, 1e-6);
    }
}