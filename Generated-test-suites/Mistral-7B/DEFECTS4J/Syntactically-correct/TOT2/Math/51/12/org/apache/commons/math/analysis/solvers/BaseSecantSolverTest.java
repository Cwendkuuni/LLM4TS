package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionExceededException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method with RegulaFalsi.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testRegulaFalsi() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.REGULA_FALSI);
        assertEquals(0.0, solver.solve(10, function, -2.0, 2.0, 0.0), 0.001);
    }

    /**
     * Test method with Illinois.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testIllinois() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x - 1.0, 3.0) - 1.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
        assertEquals(1.0, solver.solve(10, function, 0.0, 2.0, 0.0), 0.001);
    }

    /**
     * Test method with Pegasus.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testPegasus() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, Method.PEGASUS);
        assertEquals(0.9424777960768488, solver.solve(10, function, -2.0, 2.0, 0.0), 0.001);
    }

    /**
     * Test exception handling with RegulaFalsi, maximum iterations exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testRegulaFalsiMaxIterationsExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.REGULA_FALSI);
        solver.solve(0, function, -2.0, 2.0, 0.0);
    }

    /**
     * Test exception handling with RegulaFalsi, precision exceeded.
     */
    @Test(expected = PrecisionExceededException.class)
    public void testRegulaFalsiPrecisionExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.0, Method.REGULA_FALSI);
        solver.solve(10, function, -2.0, 2.0, 0.0);
    }

    /**
     * Test exception handling with Illinois, zero function value.
     */
    @Test(expected = ZeroException.class)
    public void testIllinoisZeroFunctionValue() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
        solver.solve(10, function, 1.0, 1.0, 0.0);
    }
}