package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.apache.commons.math3.analysis.solvers.AbstractUnivariateRealSolver;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {
    /**
     * Test the {@link BaseSecantSolver} with a simple quadratic function.
     * @throws MathIllegalArgumentException if the function is not bracketing
     * @throws MaxEvalException if the solver does not converge
     */
    @org.junit.Test
    public void testQuadratic() {
        UnivariateFunction f = new Polynomials.Quadratic(1.0, 2.0, -3.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-5);
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals(3.0, Precision.round(root, 3), 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver} with a function that has no real roots.
     * @throws MathIllegalArgumentException if the function is not bracketing
     * @throws MaxEvalException if the solver does not converge
     */
    @org.junit.Test (expected = MaxEvalException.class)
    public void testNoRealRoots() {
        UnivariateFunction f = new Polynomials.Quartic(1.0, 0.0, 0.0, 0.0, 1.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-5);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Test the {@link BaseSecantSolver} with a function with very large values.
     * @throws MathIllegalArgumentException if the function is not bracketing
     * @throws NumberIsTooLargeException if the function evaluations are too large
     */
    @org.junit.Test
    public void testLargeValues() {
        UnivariateFunction f = new Polynomials.Quadratic(1e20, 2e20, -3e20);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-5);
        try {
            solver.solve(f, -1e21, 1e21);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    /**
     * Test the {@link BaseSecantSolver} with a function with very small values.
     * @throws MathIllegalArgumentException if the function is not bracketing
     * @throws MaxEvalException if the solver does not converge
     */
    @org.junit.Test
    public void testSmallValues() {
        UnivariateFunction f = new Polynomials.Quadratic(1e-20, 2e-20, -3e-20);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-10);
        double root = solver.solve(f, -1e-21, 1e-21);
        assertEquals(Math.pow(10, -20), Precision.round(root, 20), 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver} with different solver methods.
     * @throws MathIllegalArgumentException if the function is not bracketing
     * @throws MaxEvalException if the solver does not converge
     */
    @org.junit.Test
    public void testDifferentMethods() {
        UnivariateFunction f = new Polynomials.Quadratic(1.0, 2.0, -3.0);
        BaseSecantSolver solverRegulaFalsi = new RegulaFalsiSolver(1e-5);
        double rootRegulaFalsi = solverRegulaFalsi.solve(f, -5.0, 5.0);
        assertEquals(3.0, Precision.round(rootRegulaFalsi, 3), 0.0);

        BaseSecantSolver solverIllinois = new IllinoisSolver(1e-5);
        double rootIllinois = solverIllinois.solve(f, -5.0, 5.0);
        assertEquals(3.0, Precision.round(rootIllinois, 3), 0.0);

        BaseSecantSolver solverPegasus = new PegasusSolver(1e-5);
        double rootPegasus = solverPegasus.solve(f, -5.0, 5.0);
        assertEquals(3.0, Precision.round(rootPegasus, 3), 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver} with a function that is not bracketing.
     * @throws MathIllegalArgumentException if the function is not bracketing
     */
    @org.junit.Test (expected = MathIllegalArgumentException.class)
    public void testNotBracketing() {
        UnivariateFunction f = new Polynomials.Quadratic(1.0, 2.0, -1.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-5);
        solver.solve(f, 1.0, 1.0);
    }

    /**
     * Test the {@link BaseSecantSolver} with a function that has complex roots.
     * @throws MathIllegalArgumentException if the function is not bracketing
     * @throws MaxEvalException if the solver does not converge
     */
    @org.junit.Test (expected = Complex.class)
    public void testComplexRoots() {
        UnivariateFunction f = new Polynomials.Quartic(1.0, 1.0, -1.0, 1.0, 1.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-5);
        solver.solve(f, -5.0, 5.0);
    }
}