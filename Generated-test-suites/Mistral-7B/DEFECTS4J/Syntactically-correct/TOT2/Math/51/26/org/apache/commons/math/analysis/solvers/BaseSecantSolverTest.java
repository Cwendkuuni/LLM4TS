package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}
     */
    @Test
    public void testConstructor() {
        final double absoluteAccuracy = 1e-6;
        final Double supplier = () -> absoluteAccuracy;
        BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}
     */
    @Test
    public void testConstructorWithRelativeAccuracy() {
        final double relativeAccuracy = 0.01;
        final double absoluteAccuracy = 1e-6;
        final Double supplier = () -> relativeAccuracy;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}
     */
    @Test
    public void testConstructorWithFunctionValueAccuracy() {
        final double relativeAccuracy = 0.01;
        final double absoluteAccuracy = 1e-6;
        final double functionValueAccuracy = 1e-5;
        final Double supplier = () -> functionValueAccuracy;
        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 1e-9);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)} with {@link RegulaFalsiSolver} method
     */
    @Test
    public void testSolverRegulaFalsi() {
        final double min = 1.0;
        final double max = 2.0;
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 4.0, 3.0, 1.0);

        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test solve method
        double root = solver.solve(10, f, min, max);
        assertEquals(1.5, root, 1e-7);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)} with {@link IllinoisSolver} method
     */
    @Test
    public void testSolverIllinois() {
        final double min = 1.0;
        final double max = 2.0;
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 4.0, 3.0, 1.0);

        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Test solve method
        double root = solver.solve(10, f, min, max);
        assertEquals(1.5, root, 1e-7);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)} with {@link PegasusSolver} method
     */
    @Test
    public void testSolverPegasus() {
        final double min = 1.0;
        final double max = 2.0;
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 4.0, 3.0, 1.0);

        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Test solve method
        double root = solver.solve(10, f, min, max);
        assertEquals(1.5, root, 1e-7);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}
     */
    @Test
    public void testVerifyBracketing() {
        final double min = 1.0;
        final double max = 2.0;
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 4.0, 3.0, 1.0);

        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Test bracketing with function values crossing
        try {
            solver.solve(10, f, max, min);
            assertThrows(IllegalArgumentException.class, () -> solver.solve(10, f, min, max));
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test bracketing with same function value at min and max
        try {
            solver.solve(10, f, min, min);
            assertThrows(IllegalArgumentException.class, () -> solver.solve(10, f, max, max));
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test bracketing with function values of same sign at min and max
        try {
            solver.solve(10, f, min, max);
            assertThrows(IllegalArgumentException.class, () -> solver.solve(10, f, max, min));
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test bracketing with correct function values at min and max
        double root = solver.solve(10, f, min, max);
        assertEquals(1.5, root, 1e-7);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link RegulaFalsiSolver} method
     */
    @Test
    public void testSolverRegulaFalsiWithStartValue() {
        final double min = 1.0;
        final double max = 2.0;
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 4.0, 3.0, 1.0);

        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test solve method with start value in the bracketing interval
        double root = solver.solve(10, f, min, max, 1.5);
        assertEquals(1.5, root, 1e-7);

        // Test solve method with start value outside the bracketing interval
        try {
            root = solver.solve(10, f, min, max, 0.5);
            assertThrows(IllegalArgumentException.class, () -> solver.solve(10, f, min, max, 3.0));
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link IllinoisSolver} method
     */
    @Test
    public void testSolverIllinoisWithStartValue() {
        final double min = 1.0;
        final double max = 2.0;
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 4.0, 3.0, 1.0);

        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Test solve method with start value in the bracketing interval
        double root = solver.solve(10, f, min, max, 1.5);
        assertEquals(1.5, root, 1e-7);

        // Test solve method with start value outside the bracketing interval
        try {
            root = solver.solve(10, f, min, max, 0.5);
            assertThrows(IllegalArgumentException.class, () -> solver.solve(10, f, min, max, 3.0));
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} with {@link PegasusSolver} method
     */
    @Test
    public void testSolverPegasusWithStartValue() {
        final double min = 1.0;
        final double max = 2.0;
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0, 4.0, 3.0, 1.0);

        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Test solve method with start value in the bracketing interval
        double root = solver.solve(10, f, min, max, 1.5);
        assertEquals(1.5, root, 1e-7);

        // Test solve method with start value outside the bracketing interval
        try {
            root = solver.solve(10, f, min, max, 0.5);
            assertThrows(IllegalArgumentException.class, () -> solver.solve(10, f, min, max, 3.0));
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test solve method with start value very close to the root
        try {
            root = solver.solve(10, f, min, max, 1.49999);
            assertEquals(1.5, root, 1e-7);
        } catch (TooManyIterationsException e) {
            // Expected
        }

        // Test solve method with start value very close to the root, but in the wrong side
        try {
            root = solver.solve(10, f, min, max, 1.499999);
            assertEquals(1.5, root, 1e-7);
        } catch (MaxEvalException e) {
            // Expected
        }

        // Test solve method with start value very close to a singularity
        try {
            final double functionValueAccuracy = 1e-12;
            BaseSecantSolver solver2 = new BaseSecantSolver(1e-6, 1e-6, functionValueAccuracy, BaseSecantSolver.Method.PEGASUS);
            root = solver2.solve(10, f, min, max, 1.5 - 1e-15);
            assertEquals(1.5, root, 1e-7);
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#allowedSolutions()}
     */
    @Test
    public void testAllowedSolutions() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        assertEquals(BracketedUnivariateRealSolver.AllowedSolution.ANY_SIDE, solver.getAllowedSolutions());
    }

    /**
     * Test method for {@link BaseSecantSolver#allowedSolutions(AllowedSolution)}
     */
    @Test
    public void testAllowedSolutionsWithAllowedSolution() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        solver.setAllowedSolutions(BracketedUnivariateRealSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(BracketedUnivariateRealSolver.AllowedSolution.LEFT_SIDE, solver.getAllowedSolutions());
    }

    /**
     * Test method for {@link BaseSecantSolver#allowedSolutions(AllowedSolution)} with invalid allowed solution
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAllowedSolutionsWithInvalidAllowedSolution() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        solver.setAllowedSolutions(BracketedUnivariateRealSolver.AllowedSolution.INVALID_SOLUTION);
    }

}