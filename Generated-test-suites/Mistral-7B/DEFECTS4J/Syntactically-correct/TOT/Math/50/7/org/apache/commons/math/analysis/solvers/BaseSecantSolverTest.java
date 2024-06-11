package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.PrecisionException;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolverCollector;
import org.apache.commons.math3.exception.MaxIterationsExceededException;

/**
 * Tests the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test the root-finding of a quadratic polynomial function using
     * {@link BaseSecantSolver}.
     */
    @Test
    public void testQuadraticFunction() {
        UnivariateFunction function = new PolynomialFunctions.Quadratic(1.0, 2.0, 3.0);
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6);
        double root = solver.solve(function, 1.0, 2.0);
        assertEquals(1.0, root, 1e-6);
    }

    /**
     * Test the root-finding of a cubic polynomial function using
     * {@link BaseSecantSolver}.
     */
    @Test
    public void testCubicFunction() {
        UnivariateFunction function = new PolynomialFunctions.Cubic(1.0, 2.0, 3.0, 4.0);
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6);
        double root = solver.solve(function, 1.0, 3.0);
        assertEquals(-1.0, root, 1e-6);
    }

    /**
     * Test the root-finding of a function with a singularity at a root using
     * {@link BaseSecantSolver}.
     */
    @Test
    public void testFunctionWithSingularity() {
        UnivariateFunction function = new FunctionWithSingularity();
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-12);
        try {
            solver.solve(function, -1.0, 0.1);
            fail("Expected a PrecisionException.");
        } catch (PrecisionException e) {
            // expected
        }
    }

    /**
     * Test the root-finding of a function with two roots using
     * {@link BaseSecantSolver}.
     */
    @Test
    public void testFunctionWithTwoRoots() {
        UnivariateFunction function = new PolynomialFunctions.Quartic(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6);
        double[] roots = solver.solve(function, -5.0, 5.0);
        assertEquals(2.0, roots.length);
        assertEquals(-2.61803398875, roots[0], 1e-6);
        assertEquals(2.61803398875, roots[1], 1e-6);
    }

    /**
     * Test the root-finding of a function using multiple solvers and comparing
     * their results.
     */
    @Test
    public void testMultipleSolvers() {
        UnivariateFunction function = new PolynomialFunctions.Quartic(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        UnivariateSolverCollector solverCollector = new UnivariateSolverCollector();
        solverCollector.add(new SecantSolver(1e-6));
        solverCollector.add(new RegulaFalsiSolver(1e-6));
        solverCollector.add(new PegasusSolver(1e-6));
        solverCollector.add(new UnivariateBrentSolver(1e-6));
        UnivariateSolver[] solvers = solverCollector.getSolvers();

        double root = 0.0;
        for (UnivariateSolver solver : solvers) {
            try {
                root = solver.solve(function, -5.0, 5.0)[0];
                break;
            } catch (MaxEvalException e) {
                // ignore
            } catch (MaxIterationsExceededException e) {
                // ignore
            } catch (PrecisionException e) {
                // ignore
            }
        }

        assertEquals(2.61803398875, root, 1e-6);
    }
}

class FunctionWithSingularity implements UnivariateFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public double value(double x) {
        if (x < 0) {
            throw new ArithmeticException("Division by zero.");
        }
        return Math.log1p(Math.pow(x, 2));
    }
}