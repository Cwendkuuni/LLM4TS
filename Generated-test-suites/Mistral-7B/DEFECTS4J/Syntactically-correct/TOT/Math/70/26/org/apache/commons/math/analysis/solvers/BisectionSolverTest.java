package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Test class for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    /**
     * Test solver with simple function.
     */
    @Test
    public void testBisectionSolver() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -2.0, 2.0);
        assertEquals(0.0, result, 1e-9);
    }

    /**
     * Test solver with function that has no roots.
     */
    @Test
    public void testBisectionSolverNoRoots() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        assertThrows(NoRootsException.class, () -> solver.solve(f, -10.0, 10.0));
    }

    /**
     * Test solver with function that has only one root.
     */
    @Test
    public void testBisectionSolverOneRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, Math.toDegrees(Math.PI), result, 1e-9);
    }

    /**
     * Test solver with function that has a horizontal asymptote.
     */
    @Test
    public void testBisectionSolverHorizontalAsymptote() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2.0) / x;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -1.0, 1.0));
    }

    /**
     * Test solver with function that is not continuous.
     */
    @Test
    public void testBisectionSolverDiscontinuousFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    return Math.sqrt(x);
                } else {
                    throw new UnsupportedOperationException("Not defined for x < 0");
                }
            }
        };
        BisectionSolver solver = new BisectionSolver();
        assertThrows(UnsupportedOperationException.class, () -> solver.solve(f, -1.0, 0.0));
    }

    /**
     * Test solver with function that has a vertical asymptote.
     */
    @Test
    public void testBisectionSolverVerticalAsymptote() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (Math.abs(x) > 1.0) {
                    return 1.0 / x;
                } else {
                    return Math.sin(x) / x;
                }
            }
        };
        BisectionSolver solver = new BisectionSolver();
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -2.0, 2.0));
    }

    /**
     * Test solver with function that is not defined for given interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBisectionSolverFunctionNotDefined() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0.0) {
                    throw new UnsupportedOperationException("Not defined for x < 0");
                }
                return Math.sqrt(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 0.0);
    }

    /**
     * Test solver with function that has negative increment.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBisectionSolverNegativeIncrement() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 2.0, 1.0);
    }

    /**
     * Test solver with function that has too large increment.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBisectionSolverTooLargeIncrement() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, Double.POSITIVE_INFINITY);
    }

    /**
     * Test solver with function that is not monotonic.
     */
    @Test(expected = IllegalStateException.class)
    public void testBisectionSolverNonMonotonicFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    return Math.pow(x, -0.5);
                } else {
                    return Math.pow(-x, -0.5);
                }
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }
}