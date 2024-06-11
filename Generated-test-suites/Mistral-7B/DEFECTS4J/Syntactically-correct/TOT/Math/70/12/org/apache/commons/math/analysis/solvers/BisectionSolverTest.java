package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {

    private BisectionSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        function = new MyFunction();
    }

    @Test
    public void testBisectionSolverPositiveRoot() throws Exception {
        double root = solver.solve(function, 1.0, 3.0);
        Assert.assertEquals(2.0, Precision.round(root, 4));
    }

    @Test
    public void testBisectionSolverNegativeRoot() throws Exception {
        double root = solver.solve(function, -3.0, -1.0);
        Assert.assertEquals(-2.0, Precision.round(root, 4));
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverMaxIterationsExceeded() throws Exception {
        solver.setMaxIterations(1);
        solver.solve(function, 1.0, 3.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverFunctionEvaluationException() throws Exception {
        solver.setMaxFunctionEvaluations(1);
        solver.solve(new MyFunctionWithException(), 1.0, 3.0);
    }

    @Test
    public void testBisectionSolverZeroRoot() throws Exception {
        UnivariateRealFunction zeroFunction = new ZeroFunction();
        double root = solver.solve(zeroFunction, -1.0, 1.0);
        Assert.assertEquals(0.0, Precision.round(root, 4));
    }

    @Test
    public void testBisectionSolverSmallRoot() throws Exception {
        UnivariateRealFunction smallFunction = new SmallFunction();
        double root = solver.solve(smallFunction, 0.1, 0.5);
        Assert.assertEquals(0.3, Precision.round(root, 4));
    }

    private static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x - 2;
        }
    }

    private static class MyFunctionWithException implements UnivariateRealFunction {
        @Override
        public double value(double x) throws ZeroException {
            if (x == 1.5) {
                throw new ZeroException();
            }
            return Math.sin(Math.PI / x);
        }
    }

    private static class ZeroFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(Math.PI / x);
        }
    }

    private static class SmallFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 0.05;
        }
    }
}