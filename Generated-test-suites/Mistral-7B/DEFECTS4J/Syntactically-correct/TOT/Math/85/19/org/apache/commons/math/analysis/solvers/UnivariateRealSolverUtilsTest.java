package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BracketingNdcbrfSolver;
import org.apache.commons.math3.solvers.LbfgsBByrdQuadraticApproximationSolver;
import org.apache.commons.math3.solvers.PowellDoglegSolver;
import org.junit.Test;

/**
 * Tests for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest {
    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double)}.
     */
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        double root = UnivariateRealSolverUtils.solve(f, 0.0);
        assertEquals(Math.sqrt(2.0), root, 1e-6);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}
     * with default solver.
     */
    @Test
    public void testSolveDefault() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        double root = UnivariateRealSolverUtils.solve(f, 0.0, 2.0);
        assertEquals(Math.sqrt(2.0), root, 1e-6);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double, double)}
     * with default solver.
     */
    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        double root = UnivariateRealSolverUtils.solve(f, 0.0, 2.0, 1e-8);
        assertEquals(Math.sqrt(2.0), root, 1e-9);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}
     * with default solver.
     */
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        double[] bracketing = UnivariateRealSolverUtils.bracket(f, 0.0, 2.0);
        assertEquals(Math.sqrt(2.0), bracketing[1], 1e-6);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with {@link BracketingNdcbrfSolver}.
     */
    @Test
    public void testBracketWithNdcbrfSolver() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        double[] bracketing = UnivariateRealSolverUtils.bracket(f, 0.0, 2.0, 10);
        assertEquals(Math.sqrt(2.0), bracketing[1], 1e-6);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with {@link LbfgsBByrdQuadraticApproximationSolver}.
     */
    @Test
    public void testBracketWithByrdQuadraticApproximationSolver() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        assertThrows(NoBracketingException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, 0.0, 2.0, 2, 1);
        });
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with {@link PowellDoglegSolver}.
     */
    @Test
    public void testBracketWithPowellDoglegSolver() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        assertThrows(MaxIterationsExceededException.class, () -> {
            UnivariateRealSolverUtils.bracket(f, 0.0, 2.0, 100000, 1);
        });
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with {@link BrentSolver}.
     */
    @Test
    public void testBracketWithBrentSolver() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0});
        double[] bracketing = UnivariateRealSolverUtils.bracket(f, 0.0, 2.0, 20, 1);
        assertEquals(Math.sqrt(2.0), bracketing[1], 1e-6);
    }

    /**
     * Test for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}
     * with null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() {
        UnivariateRealSolverUtils.bracket(null, 0.0, 2.0, 10);
    }
}