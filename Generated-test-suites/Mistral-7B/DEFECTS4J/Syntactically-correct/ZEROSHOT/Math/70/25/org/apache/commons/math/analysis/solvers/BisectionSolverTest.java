package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionIterationError;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for BisectionSolver.
 */
public class BisectionSolverTest {

    /**
     * Test method for {@link BisectionSolver#BisectionSolver(UnivariateRealFunction)}.
     */
    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        Assert.assertEquals(0.0, solver.getMaxIterations(), 0);
        Assert.assertEquals(1.0E-6, solver.getRelativeAccuracy(), 1.0E-6);
    }

    /**
     * Test method for {@link BisectionSolver#BisectionSolver()}.
     */
    @Test
    public void testDefaultConstructor() {
        BisectionSolver solver = new BisectionSolver();
        Assert.assertEquals(100, solver.getMaxIterations(), 0);
        Assert.assertEquals(1.0E-6, solver.getRelativeAccuracy(), 1.0E-6);
    }

    /**
     * Test method for {@link BisectionSolver#solve(UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolveWithFunctionAndInterval() throws MaxIterationsExceededException, PrecisionIterationError {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -3.0, 3.0);
        Assert.assertEquals(1.2732119024011234, root, 1.0E-6);
    }

    /**
     * Test method for {@link BisectionSolver#solve(double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 3.0, 3.0);
    }

    /**
     * Test method for {@link BisectionSolver#solve(double, double)}.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceeded() throws MaxIterationsExceededException, PrecisionIterationError {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0, 10);
    }

    /**
     * Test method for {@link BisectionSolver#solve(UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolveWithFunctionAndInitialGuess() throws MaxIterationsExceededException, PrecisionIterationError {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -3.0, 3.0, 1.0);
        Assert.assertEquals(1.2732119024011234, root, 1.0E-6);
    }
}