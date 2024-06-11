package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;

/**
 * Test the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test {@link BaseSecantSolver} implementing {@link RegulaFalsiSolver}
     * methods.
     */
    @Test
    public void testRegulaFalsiSolver() {
        final double epsilon = 1e-9;

        final UnivariateRealFunction f = new PolynomialFunction(3, new double[] { 1, -2, 1, -1 });

        final BaseSecantSolver solver = new RegulaFalsiSolver(epsilon);
        assertEquals(-1.0, solver.solve(f, -2.0, 2.0), epsilon);
    }

    /**
     * Test {@link BaseSecantSolver} implementing {@link IllinoisSolver} methods.
     */
    @Test
    public void testIllinoisSolver() {
        final double epsilon = 1e-9;

        final UnivariateRealFunction f = new PolynomialFunction(3, new double[] { 1, -2, 1, -1 });

        final BaseSecantSolver solver = new IllinoisSolver(epsilon);
        assertEquals(-1.0, solver.solve(f, -2.0, 2.0), epsilon);
    }

    /**
     * Test {@link BaseSecantSolver} implementing {@link PegasusSolver} methods.
     */
    @Test
    public void testPegasusSolver() {
        final double epsilon = 1e-9;

        final UnivariateRealFunction f = new PolynomialFunction(3, new double[] { 1, -2, 1, -1 });

        final BaseSecantSolver solver = new PegasusSolver(epsilon);
        assertEquals(-1.0, solver.solve(f, -2.0, 2.0), epsilon);
    }

    /**
     * Test {@link BaseSecantSolver} with function that has no real root.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoRootFunction() {
        final double epsilon = 1e-9;

        final UnivariateRealFunction f = new PolynomialFunction(1, new double[] { 1 });
        new BaseSecantSolver(epsilon).solve(f, -1.0, 1.0);
    }

    /**
     * Test {@link BaseSecantSolver} with function that has two roots.
     */
    @Test
    public void testTwoRootsFunction() {
        final double epsilon = 1e-9;

        final UnivariateRealFunction f = new PolynomialFunction(2, new double[] { 1, -1, 1 });

        final BaseSecantSolver solver = new RegulaFalsiSolver(epsilon);
        assertEquals(-1.0, solver.solve(f, -2.0, 2.0), epsilon);
        assertEquals(1.0, solver.solve(f, -2.0, 2.0), epsilon);
    }

    /**
     * Test {@link BaseSecantSolver} with function that has a root close to a
     * boundary.
     */
    @Test
    public void testRootOnBoundaryFunction() {
        final double epsilon = 1e-9;

        final UnivariateRealFunction f = new PolynomialFunction(1, new double[] { 1 });

        final BaseSecantSolver solver = new RegulaFalsiSolver(epsilon);
        assertEquals(0.0, solver.solve(f, -1e-6, 1e-6), epsilon);
    }

    /**
     * Test {@link BaseSecantSolver} with function that has a root close to
     * another root.
     */
    @Test
    public void testRootsCloseFunction() {
        final double epsilon = 1e-9;

        final UnivariateRealFunction f = new PolynomialFunction(2, new double[] { 1, -1, 1 });

        final BaseSecantSolver solver = new RegulaFalsiSolver(epsilon);
        assertEquals(-1.0, solver.solve(f, -2.0, 1.9), epsilon);
        assertEquals(1.0, solver.solve(f, -1.9, 2.0), epsilon);
    }

    /**
     * Test {@link BaseSecantSolver} with function that has a root very close
     * to another root, but the method doesn't converge.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testRootsVeryCloseFunction() {
        final double epsilon = 1e-9;
        final int maxIterations = 100;

        final UnivariateRealFunction f = new PolynomialFunction(2, new double[] { 1, -1, 1 });

        final BaseSecantSolver solver = new RegulaFalsiSolver(epsilon, maxIterations);
        assertEquals(-1.0, solver.solve(f, -2.0, 0.5), epsilon);
        assertEquals(1.0, solver.solve(f, -0.5, 2.0), epsilon);
    }

    /**
     * Test {@link BaseSecantSolver} with function that has a root in the
     * interval but with the wrong sign.
     */
    @Test(expected = NoBracketingException.class)
    public void testWrongSignFunction() {
        final double epsilon = 1e-9;

        final UnivariateRealFunction f = new PolynomialFunction(1, new double[] { 1 });

        final BaseSecantSolver solver = new RegulaFalsiSolver(epsilon);
        solver.solve(f, 2.0, 1.0);
    }

    /**
     * Test {@link BaseSecantSolver} with function that has a root outside of
     * the interval.
     */
    @Test(expected = NoBracketingException.class)
    public void testRootOutsideFunction() {
        final double epsilon = 1e-9;

        final UnivariateRealFunction f = new PolynomialFunction(1, new double[] { 1 });

        final BaseSecantSolver solver = new RegulaFalsiSolver(epsilon);
        solver.solve(f, -2.0, 1.0);
    }

    /**
     * Test {@link BaseSecantSolver} with function that has a root with a
     * negative value.
     */
    @Test
    public void testNegativeRootFunction() {
        final double epsilon = 1e-9;

        final UnivariateRealFunction f = new PolynomialFunction(1, new double[] { -1 });

        final BaseSecantSolver solver = new RegulaFalsiSolver(epsilon);
        assertEquals(-1.0, solver.solve(f, -1.5, -0.5), epsilon);
    }

    /**
     * Test {@link BaseSecantSolver} with <code>null</code> function.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNullFunction() {
        new RegulaFalsiSolver(1e-9).solve(null, 0.0, 0.0);
    }
}