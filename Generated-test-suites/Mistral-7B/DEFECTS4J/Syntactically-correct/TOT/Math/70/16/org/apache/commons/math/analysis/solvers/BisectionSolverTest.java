package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BisectionSolverTest {

    private static final double DELTA = 1e-9;
    private static final double EPSILON = 1e-14;

    private UnivariateRealFunction f;
    private BisectionSolver solver;

    @Parameterized.Parameters(name = "Function: {0}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                {new MyFunction1()},
                {new MyFunction2()},
                {new MyFunction3()}
        };
        return Arrays.asList(data);
    }

    public BisectionSolverTest(UnivariateRealFunction f) {
        this.f = f;
        this.solver = new BisectionSolver();
    }

    @Before
    public void setup() {
        solver.setMaxIterations(1000);
        solver.setRelativeAccuracy(DELTA);
    }

    @Test
    public void testSolver() throws MaxIterationsExceededException, ZeroException, NumberIsTooSmallException {
        double root = solver.solve(f, -1.0, 1.0);
        double expectedRoot = f.root();
        Assert.assertNotEquals("Unexpected root.", Precision.round(root, EPSILON), 0.0);
        Assert.assertEquals("Root not found.", Precision.round(expectedRoot, EPSILON), Precision.round(root, EPSILON));
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException {
        solver.setMaxIterations(0);
        solver.solve(f, -1.0, 1.0);
    }

    @Test(expected = ZeroException.class)
    public void testZeroFunction() throws ZeroException {
        UnivariateRealFunction zeroFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        solver.solve(zeroFunction, -1.0, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallAccuracy() throws NumberIsTooSmallException {
        solver.setRelativeAccuracy(0.0);
        solver.solve(f, -1.0, 1.0);
    }

    static class MyFunction1 extends UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(x) - 0.5;
        }

        @Override
        public double root() {
            return Math.PI / 2;
        }
    }

    static class MyFunction2 extends UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 1;
        }

        @Override
        public double root() {
            return -1.0;
        }
    }

    static class MyFunction3 extends UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.log(x + 1) - Math.log(x);
        }

        @Override
        public double root() {
            return 0.0;
        }
    }
}