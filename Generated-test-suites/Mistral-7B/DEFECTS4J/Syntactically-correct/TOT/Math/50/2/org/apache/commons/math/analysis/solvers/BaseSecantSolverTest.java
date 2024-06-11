package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Simple test for the {@link BaseSecantSolver}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolver() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x) - Math.cos(x);
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.PegasusSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double x = solver.solve(100, f, -10.0, 10.0);
        assertEquals(0.0, x, 1e-6);
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that has no roots.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverNoRoot() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return x * x - 1;
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        assertThrows(NoBracketException.class, () -> solver.solve(100, f, -10.0, 10.0));
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that is always positive.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverAlwaysPositive() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.pow(x, 2);
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, f, -1.0, 1.0));
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that is always negative.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverAlwaysNegative() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return -Math.pow(x, 2);
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, f, -1.0, 1.0));
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that is never evaluated.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverNeverEvaluated() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException();
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        assertThrows(UnsupportedOperationException.class, () -> solver.solve(100, f, -10.0, 10.0));
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that throws an exception.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverFunctionException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    throw new PrecisionException("Exception thrown");
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        assertThrows(PrecisionException.class, () -> solver.solve(1, f, -10.0, 10.0));
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that has roots at the
     * boundaries.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverRootsAtBounds() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(Math.PI) * (x - Math.PI);
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        try {
            solver.solve(100, f, -Math.PI, Math.PI);
            assertEquals(Math.PI, solver.getPreviousRootEstimate(), 1e-6);
        } catch (NoBracketException e) {
            // Expected
        }
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that has roots at the
     * boundaries and a tolerance that is too large.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverRootsAtBoundsWithTooLargeTolerance() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(Math.PI) * (x - Math.PI);
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-2, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            solver.solve(100, f, -Math.PI, Math.PI);
            fail();
        } catch (NoBracketException e) {
            // Expected
        }
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that has a root
     * between the bounds.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverRootBetweenBounds() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x) - Math.cos(x);
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        double x = solver.solve(100, f, -Math.PI, Math.PI);
        assertEquals(0.0, x, 1e-6);
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that has a root
     * very close to one of the bounds.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverRootCloseToBound() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x) - Math.cos(x) + 1e-12;
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        double x = solver.solve(100, f, -Math.PI, Math.PI);
        assertEquals(Math.PI, x, 1e-6);
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that has roots at the
     * same x value.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverRootsAtSameX() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.pow(x, 2) - 4 * x + 4;
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        double x = solver.solve(100, f, -2.0, 2.0);
        assertEquals(-1.0, x, 1e-6);
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that has roots
     * close to each other.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverRootsCloseToEachOther() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x) - Math.cos(x);
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-12);
        double x = solver.solve(100, f, -Math.PI + 0.001, Math.PI - 0.001);
        assertEquals(0.0, x, 1e-12);
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that has a root
     * at a very large value.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverRootAtLargeValue() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.exp(-x) - Math.exp(-Math.pow(x, 2));
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        double x = solver.solve(100, f, -100.0, 100.0);
        assertEquals(-Math.log(Math.E), x, 1e-6);
    }

    /**
     * Test for the {@link BaseSecantSolver} with a function that has roots at
     * very large and very small values.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBaseSecantSolverRootsAtLargeAndSmallValues() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.exp(-x) - Math.exp(-Math.pow(x, 2));
                }
            };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        double x = solver.solve(100, f, -1000.0, 1000.0);
        assertEquals(-Math.log(Math.E), x, 1e-6);
        x = solver.solve(100, f, -1e-12, 1e-12);
        assertEquals(-Math.log(Math.E), x, 1e-6);
    }
}