package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedRealRootFinder;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.junit.Test;

/**
 * Unit tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Tests the bracketing solver with a quadratic function.
     */
    @Test
    public void testQuadratic() {
        final UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0});
        final double min = -5.0;
        final double max = 5.0;
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        try {
            double root = solver.solve(100, f, min, max, 2.5);
            assertEquals(1.0, root, 1e-9);
        } catch (MaxEvalException e) {
            fail("Expected to find root within given tolerance.");
        }
    }

    /**
     * Tests the bracketing solver with a quadratic function where the
     * roots are not bracketted initially.
     */
    @Test
    public void testQuadraticNoBracketing() {
        final UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0});
        final double min = -5.0;
        final double max = 5.0;
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        try {
            solver.solve(100, f, min, max, 0.0);
            fail("Expected NoBracketingException as the roots are not bracketted.");
        } catch (NoBracketingException e) {
            // Expected
        } catch (RootNotFoundException e) {
            fail("Expected NoBracketingException as the roots are not bracketted.");
        }
    }

    /**
     * Tests the bracketing solver with a quadratic function where one root is not bracketted initially.
     */
    @Test
    public void testQuadraticOneSide() {
        final UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0});
        final double min = -5.0;
        final double max = 5.0;
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, AllowedSolution.LEFT_SIDE);

        try {
            double root = solver.solve(100, f, min, max, 0.0);
            fail("Expected RootNotFoundException as one root is not bracketted.");
        } catch (RootNotFoundException e) {
            // Expected
        } catch (MaxEvalException e) {
            fail("Expected RootNotFoundException as one root is not bracketted.");
        }
    }

    /**
     * Tests the bracketing solver with a cubic function.
     */
    @Test
    public void testCubic() {
        final UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 2.0});
        final double min = -5.0;
        final double max = 5.0;
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        try {
            double root = solver.solve(100, f, min, max, 1.5);
            assertEquals(1.0, root, 1e-9);
        } catch (MaxEvalException e) {
            fail("Expected to find root within given tolerance.");
        }
    }

    /**
     * Tests the bracketing solver with a cubic function where the roots are not bracketted initially.
     */
    @Test
    public void testCubicNoBracketing() {
        final UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 2.0});
        final double min = -5.0;
        final double max = 5.0;
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        try {
            solver.solve(100, f, min, max, 0.0);
            fail("Expected NoBracketingException as the roots are not bracketted.");
        } catch (NoBracketingException e) {
            // Expected
        } catch (RootNotFoundException e) {
            fail("Expected NoBracketingException as the roots are not bracketted.");
        }
    }

}