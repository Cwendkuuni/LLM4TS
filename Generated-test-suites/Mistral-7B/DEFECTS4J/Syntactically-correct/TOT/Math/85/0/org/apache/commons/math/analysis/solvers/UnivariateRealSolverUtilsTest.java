import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.BracketingNumericalMethod;
import org.apache.commons.math3.solver.LaguerreSolver;
import org.apache.commons.math3.solver.SecantSolver;
import org.apache.commons.math3.solver.BisectionSolver;
import org.apache.commons.math3.solver.PowellSolver;
import org.apache.commons.math3.solver.BrentSolver.BrentSolverOptions;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

/**
 * Test class for UnivariateRealSolverUtils.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolve() {
        double[] roots = new double[]{-1.0, 1.0};
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 2.0, -1.0});

        double x0 = 0.0;
        double x1 = 2.0;
        double solution = UnivariateRealSolverUtils.solve(f, x0, x1);
        double[] expected = {roots[0], roots[1]};
        assertArrayEquals(expected, new double[]{solution, solution}, 0.001);

        x0 = 1.0;
        x1 = 3.0;
        solution = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertArrayEquals(expected, new double[]{solution, solution}, 0.001);

        // Error cases
        x0 = Double.NEGATIVE_INFINITY;
        x1 = Double.POSITIVE_INFINITY;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        x0 = 1.0;
        x1 = 1.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        x0 = x1;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        x0 = 1.0;
        x1 = 0.0;
        try {
            UnivariateRealSolverUtils.solve(f, x0, x1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        f = new UnivariateFunction() {
            public double value(double x) {
                throw new UnsupportedOperationException();
            }

            public int getNumberOfDerivatives() {
                return 0;
            }
        };
        try {
            UnivariateRealSolverUtils.solve(f, 0.0, 1.0);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}.
     */
    @Test
    public void testBracket() {
        double[] roots = new double[]{-1.0, 1.0};
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 2.0, -1.0});

        double initial = 0.0;
        double lowerBound = -5.0;
        double upperBound = 5.0;
        double[] solution = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        double[] expected = {roots[0], roots[1]};
        assertArrayEquals(expected, solution, 0.001);

        initial = 2.0;
        solution = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10);
        assertArrayEquals(expected, solution, 0.001);

        // Error cases
        lowerBound = Double.NEGATIVE_INFINITY;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        upperBound = Double.POSITIVE_INFINITY;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        lowerBound = upperBound;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        initial = lowerBound;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        initial = upperBound;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        f = new UnivariateFunction() {
            public double value(double x) {
                throw new UnsupportedOperationException();
            }

            public int getNumberOfDerivatives() {
                return 0;
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(f, 0.0, 0.0, 0.0, 10);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        f = new PolynomialFunction(0, new double[0]);
        try {
            UnivariateRealSolverUtils.bracket(f, 0.0, 0.0, 0.0, 10);
            fail("Expected NoRootsException");
        } catch (NoRootsException e) {
            // expected
        }

        initial = 0.0;
        lowerBound = 0.0;
        upperBound = 0.0;
        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, 1000001);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)} using different solver strategies.
     */
    @Test
    public void testSolveDifferentSolverStrategies() {
        double[] roots = new double[]{0.5, 2.0};
        UnivariateFunction f = new PolynomialFunction(2, new double[]{1.0, -2.0, 1.0});

        double initial = 0.0;
        double lowerBound = -5.0;
        double upperBound = 5.0;

        // Default solver
        double solutionDefault = UnivariateRealSolverUtils.solve(f, initial, lowerBound, upperBound);
        double[] expectedDefault = {roots[0], roots[1]};
        assertArrayEquals(expectedDefault, new double[]{solutionDefault, solutionDefault}, 0.001);

        // Bisection solver
        BracketingNumericalMethod bisectionSolver = new BisectionSolver();
        double solutionBisection = UnivariateRealSolverUtils.solve(f, initial, lowerBound, upperBound, bisectionSolver);
        assertArrayEquals(expectedDefault, new double[]{solutionBisection, solutionBisection}, 0.001);

        // Secant solver
        double[] previousPoint = new double[]{initial, solutionDefault};
        SecantSolver secantSolver = new SecantSolver();
        double solutionSecant = UnivariateRealSolverUtils.solve(f, initial, solutionDefault, secantSolver, previousPoint);
        assertArrayEquals(expectedDefault, new double[]{solutionSecant, solutionSecant}, 0.001);

        // Laguerre solver
        LaguerreSolver laguerreSolver = new LaguerreSolver();
        double solutionLaguerre = UnivariateRealSolverUtils.solve(f, initial, lowerBound, upperBound, laguerreSolver);
        assertArrayEquals(expectedDefault, new double[]{solutionLaguerre, solutionLaguerre}, 0.001);

        // Powell solver
        PowellSolver powellSolver = new PowellSolver();
        double solutionPowell = UnivariateRealSolverUtils.solve(f, initial, lowerBound, upperBound, powellSolver);
        assertArrayEquals(expectedDefault, new double[]{solutionPowell, solutionPowell}, 0.001);

        // Brent solver
        BrentSolverOptions brentSolverOptions = new BrentSolver.BrentSolverOptions();
        BrentSolver brentSolver = new BrentSolver(brentSolverOptions);
        double solutionBrent = UnivariateRealSolverUtils.solve(f, initial, lowerBound, upperBound, brentSolver);
        assertArrayEquals(expectedDefault, new double[]{solutionBrent, solutionBrent}, 0.001);
    }
}