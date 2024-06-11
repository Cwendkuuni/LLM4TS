package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.PrecisionConvertedToIntegralException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link BisectionSolver} class.
 */
public class BisectionSolverTest {

    private UnivariateRealFunction f;
    private BisectionSolver solver;

    @Before
    public void setUp() {
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        solver = new BisectionSolver();
    }

    /**
     * Test that solver finds root for a simple function.
     */
    @Test
    public void testFindRoot() throws Exception {
        double root = solver.solve(f, -2.5, 2.5);
        Assert.assertEquals(0.0, f.value(root), 1e-6);
    }

    /**
     * Test that solver handles function evaluation exception.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Test");
            }
        };
        solver.solve(f, -2.5, 2.5);
    }

    /**
     * Test that solver handles max iterations exceeded exception.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / x);
            }
        };
        solver = new BisectionSolver(10);
        solver.solve(f, -10.0, 10.0);
    }

    /**
     * Test that solver handles non-converging function.
     */
    @Test(expected = NoRootsException.class)
    public void testNonConvergingFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        solver.solve(f, -10.0, 10.0);
    }

    /**
     * Test that solver handles precision exception.
     */
    @Test(expected = PrecisionConvertedToIntegralException.class)
    public void testPrecisionException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(x) - Math.log10(x);
            }
        };
        solver = new BisectionSolver(10);
        solver.solve(f, 1.0, 10.0);
    }

    /**
     * Test that solver returns initial value if function is constant.
     */
    @Test
    public void testConstantFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0;
            }
        };
        double root = solver.solve(f, -1.0, 1.0);
        Assert.assertEquals(1.0, root, 1e-6);
    }

    /**
     * Test that solver returns initial value if function brackets root.
     */
    @Test
    public void testInitialValue() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 1.0;
            }
        };
        double root = solver.solve(f, 0.0, 2.0);
        Assert.assertEquals(1.0, root, 1e-6);
    }
}