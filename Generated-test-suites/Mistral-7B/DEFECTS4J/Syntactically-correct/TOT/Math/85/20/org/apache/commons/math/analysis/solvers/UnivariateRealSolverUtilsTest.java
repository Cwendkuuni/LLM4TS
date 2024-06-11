package org.apache.commons.math.analysis.solvers.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunctionUtils;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.LagrangeSolver;
import org.apache.commons.math3.solver.PowellSolver;
import org.apache.commons.math3.solver.UnivariateSolver;
import org.apache.commons.math3.solver.UnivariateSolverException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link UnivariateRealSolverUtils} class.
 */
public class UnivariateRealSolverUtilsTest {

    /** Function for testing purposes. */
    private static final UnivariateFunction FUNCTION = new PolynomialFunction(
            new double[]{-1.0, 2.0, -1.0, 1.0}, new double[]{0.0});

    /** Solvers for testing purposes. */
    private static UnivariateSolver brentSolver, lagrangeSolver, powellSolver;

    /**
     * Sets up test environment.
     */
    @BeforeClass
    public static void setUp() {
        brentSolver = new BrentSolver();
        lagrangeSolver = new LagrangeSolver();
        powellSolver = new PowellSolver();
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        double result;

        // Basic test
        result = UnivariateRealSolverUtils.solve(FUNCTION, -5.0, 5.0);
        assertEquals(0.0, result, 1e-12);

        // Test with different solvers
        result = UnivariateRealSolverUtils.solve(FUNCTION, -5.0, 5.0, brentSolver);
        assertEquals(0.0, result, 1e-12);

        result = UnivariateRealSolverUtils.solve(FUNCTION, -5.0, 5.0, lagrangeSolver);
        assertEquals(0.0, result, 1e-12);

        result = UnivariateRealSolverUtils.solve(FUNCTION, -5.0, 5.0, powellSolver);
        assertEquals(0.0, result, 1e-12);

        // Test with different initial bounds
        result = UnivariateRealSolverUtils.solve(FUNCTION, 0.0, 0.0);
        assertEquals(0.0, result, 1e-12);

        result = UnivariateRealSolverUtils.solve(FUNCTION, -10.0, 10.0);
        assertEquals(0.0, result, 1e-12);

        // Test with different function
        UnivariateFunction function2 = new PolynomialFunction(
                new double[]{-1.0, 1.0, 1.0, 1.0}, new double[]{1.0});
        result = UnivariateRealSolverUtils.solve(function2, -5.0, 5.0);
        assertEquals(1.0, result, 1e-12);

        // Test with different tolerance
        result = UnivariateRealSolverUtils.solve(FUNCTION, -5.0, 5.0, 1e-10);
        assertEquals(0.0, result, 1e-13);

        // Test with bad initial bounds
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.solve(FUNCTION, 5.0, 5.0);
        });
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}.
     */
    @Test
    public void testBracket() {
        double[] bracketingPoint;

        // Test with default solver
        bracketingPoint = UnivariateRealSolverUtils.bracket(FUNCTION, 0.0, -5.0, 5.0);
        assertEquals(-1.7320508075688772, bracketingPoint[0], 1e-12);
        assertEquals(0.0, bracketingPoint[1], 1e-12);

        // Test with different solver
        UnivariateRealFunction function2 = new PolynomialFunction(
                new double[]{1.0, 1.0, 1.0, 1.0}, new double[]{1.0});
        bracketingPoint = UnivariateRealSolverUtils.bracket(function2, 0.0, -5.0, 5.0, 100);
        assertEquals(-2.0, bracketingPoint[0], 1e-12);
        assertEquals(0.0, bracketingPoint[1], 1e-12);

        // Test with bad initial bounds
        assertThrows(IllegalArgumentException.class, () -> {
            UnivariateRealSolverUtils.bracket(FUNCTION, 5.0, 5.0, -5.0, 100);
        });

        // Test with no bracketing
        assertThrows(NoBracketingException.class, () -> {
            UnivariateRealSolverUtils.bracket(new UnivariateFunction() {
                @Override
                public double value(double x) {
                    return x * x;
                }
            }, 0.0, 0.0, 0.0, 100);
        });

        // Test with max iterations
        assertThrows(MaxIterationsExceededException.class, () -> {
            UnivariateRealSolverUtils.bracket(new UnivariateFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x);
                }
            }, 0.0, 0.0, Math.PI, 1);
        });
    }
}