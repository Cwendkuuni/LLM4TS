package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.BesselFunctions;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolators.LinearInterpolator;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.PowellSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionIntegrationException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

public class UnivariateRealSolverUtilsTest {

    @org.junit.Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2 * x) - x;
            }
        };

        // Test default solver
        double root = UnivariateRealSolverUtils.solve(f, -10.0, 10.0);
        assertEquals(0.0, root, 1e-9);

        // Test with absolute accuracy
        root = UnivariateRealSolverUtils.solve(f, -10.0, 10.0, 1e-11);
        assertEquals(0.0, root, 1e-12);

        // Test with an unsupported function
        UnivariateFunction unsupported = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        assertThrows(UnsupportedOperationException.class, () -> UnivariateRealSolverUtils.solve(unsupported, -10.0, 10.0));

        // Test with an illegal interval
        UnivariateRealFunction illegalIntervalFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2 * x);
            }
        };
        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(illegalIntervalFunction, 10.0, 10.0));

        // Test with a function that has no roots in the interval
        UnivariateRealFunction noRootFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.solve(noRootFunction, -3.0, 3.0));
    }

    @org.junit.Test
    public void testBracket() throws Exception {
        // Test with a simple function
        UnivariateRealFunction simpleFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };
        double[] bracketed = UnivariateRealSolverUtils.bracket(simpleFunction, 0.5, -1.0, 1.0);
        assertEquals(-0.5, bracketed[0], 1e-9);
        assertEquals(0.5, bracketed[1], 1e-9);

        // Test with a complex function
        UnivariateRealFunction complexFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(Math.sin(x) * Math.cos(x)));
            }
        };
        bracketed = UnivariateRealSolverUtils.bracket(complexFunction, 0.0, -1.0, 1.0);
        assertEquals(-0.5235987755982989, bracketed[0], 1e-7);
        assertEquals(0.5235987755982989, bracketed[1], 1e-7);

        // Test with a function with multiple roots
        UnivariateRealFunction multipleRootsFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 0.25;
            }
        };
        bracketed = UnivariateRealSolverUtils.bracket(multipleRootsFunction, 0.0, -5.0, 5.0);
        assertEquals(-2.3161577249394123, bracketed[0], 1e-7);
        assertEquals(2.3161577249394123, bracketed[1], 1e-7);

        // Test with a function with no roots in the interval
        UnivariateRealFunction noRootFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(noRootFunction, -3.0, 3.0, 1000));

        // Test with a function that is not continuous
        UnivariateRealFunction notContinuousFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0.5) {
                    return Math.sin(Math.PI * x);
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        };
        assertThrows(UnsupportedOperationException.class, () -> UnivariateRealSolverUtils.bracket(notContinuousFunction, 0.5, -1.0, 1.0));
    }

    @org.junit.Test
    public void testBracketWithMaxIterations() throws Exception {
        // Test with Bessel function of first kind and zero order
        UnivariateRealFunction besselFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return BesselFunctions.j0(x);
            }
        };
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] bracketed = UnivariateRealSolverUtils.bracket(besselFunction, initial, lowerBound, upperBound, 5);
        assertEquals(lowerBound, bracketed[0], Precision.SAFE_EPSILON);
        assertEquals(upperBound, bracketed[1], Precision.SAFE_EPSILON);

        // Test with a secant solver
        SecantSolver secantSolver = new SecantSolver();
        UnivariateRealFunction secantFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 0.25;
            }
        };
        double x0 = 1.0;
        double x1 = 2.0;
        double root = secantSolver.solve(secantFunction, x0, x1);
        double[] bracketedWithSecant = UnivariateRealSolverUtils.bracket(secantFunction, x0, lowerBound, upperBound, 5);
        assertEquals(x0, bracketedWithSecant[0], Precision.SAFE_EPSILON);
        assertEquals(x1, bracketedWithSecant[1], Precision.SAFE_EPSILON);

        // Test with a brent solver
        BrentSolver brentSolver = new BrentSolver();
        double rootWithBrent = brentSolver.solve(secantFunction, x0, x1);
        double[] bracketedWithBrent = UnivariateRealSolverUtils.bracket(secantFunction, x0, lowerBound, upperBound, 5);
        assertEquals(x0, bracketedWithBrent[0], Precision.SAFE_EPSILON);
        assertEquals(x1, bracketedWithBrent[1], Precision.SAFE_EPSILON);

        // Test with a powell solver
        PowellSolver powellSolver = new PowellSolver();
        double rootWithPowell = powellSolver.solve(secantFunction, x0, x1);
        double[] bracketedWithPowell = UnivariateRealSolverUtils.bracket(secantFunction, x0, lowerBound, upperBound, 5);
        assertEquals(x0, bracketedWithPowell[0], Precision.SAFE_EPSILON);
        assertEquals(x1, bracketedWithPowell[1], Precision.SAFE_EPSILON);

        // Test with a bisection solver
        BisectionSolver bisectionSolver = new BisectionSolver();
        double rootWithBisection = bisectionSolver.solve(secantFunction, x0, x1);
        double[] bracketedWithBisection = UnivariateRealSolverUtils.bracket(secantFunction, x0, lowerBound, upperBound, 5);
        assertEquals(x0, bracketedWithBisection[0], Precision.SAFE_EPSILON);
        assertEquals(x1, bracketedWithBisection[1], Precision.SAFE_EPSILON);

        // Test with a function with multiple roots
        UnivariateRealFunction multipleRootsFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) - 0.25;
            }
        };
        double initialMultipleRoots = 1.0;
        double lowerBoundMultipleRoots = -3.0;
        double upperBoundMultipleRoots = 3.0;
        double[] bracketedMultipleRoots = UnivariateRealSolverUtils.bracket(multipleRootsFunction, initialMultipleRoots, lowerBoundMultipleRoots, upperBoundMultipleRoots, 5);
        assertEquals(lowerBoundMultipleRoots, bracketedMultipleRoots[0], Precision.SAFE_EPSILON);
        assertEquals(upperBoundMultipleRoots, bracketedMultipleRoots[1], Precision.SAFE_EPSILON);
    }

    @org.junit.Test
    public void testMidpoint() throws Exception {
        assertEquals(3.5, UnivariateRealSolverUtils.midpoint(1.5, 5.5), 1e-9);
    }

}