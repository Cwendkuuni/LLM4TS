package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.precision.AbstractPrecisionException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * Test solver with a simple quadratic function.
     *
     * @throws MaxEvalException
     * @throws NumberIsTooLargeException
     * @throws AbstractPrecisionException
     * @throws TooManyIterationsException
     */
    @Test
    public void testSolveQuadratic() throws MaxEvalException,
            NumberIsTooLargeException, AbstractPrecisionException,
            TooManyIterationsException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2 * x + 1;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        double root = solver.solve(10, function, -2, 2);
        assertTrue(Precision.abs(root - 1.0, 1e-7) < 1e-6);
    }

    /**
     * Test solver with a function that has roots on both sides.
     *
     * @throws MaxEvalException
     * @throws NumberIsTooLargeException
     * @throws AbstractPrecisionException
     * @throws TooManyIterationsException
     */
    @Test
    public void testSolveRootsOnBothSides() throws MaxEvalException,
            NumberIsTooLargeException, AbstractPrecisionException,
            TooManyIterationsException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-6);

        double root1 = solver.solve(10, function, -2, 2);
        assertTrue(Precision.abs(root1 - 2.0, 1e-7) < 1e-6);
        double root2 = solver.solve(10, function, 2, 4);
        assertTrue(Precision.abs(root2 - 2.0, 1e-7) < 1e-6);
    }

    /**
     * Test solver with a function that has a root at a very large number.
     *
     * @throws MaxEvalException
     * @throws NumberIsTooLargeException
     * @throws AbstractPrecisionException
     * @throws TooManyIterationsException
     */
    @Test
    public void testSolveRootAtLargeNumber() throws MaxEvalException,
            NumberIsTooLargeException, AbstractPrecisionException,
            TooManyIterationsException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 1;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1e-6);

        double root = solver.solve(10, function, 0, 100);
        assertTrue(Precision.abs(Math.E - root, 1e-6) < 1e-6);
    }

    /**
     * Test solver with a function that has no roots.
     *
     * @throws MaxEvalException
     * @throws NumberIsTooLargeException
     * @throws AbstractPrecisionException
     * @throws TooManyIterationsException
     */
    @Test(expected = NoRootsException.class)
    public void testSolveNoRoots() throws MaxEvalException,
            NumberIsTooLargeException, AbstractPrecisionException,
            TooManyIterationsException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        solver.solve(10, function, -10, 10);
    }
}